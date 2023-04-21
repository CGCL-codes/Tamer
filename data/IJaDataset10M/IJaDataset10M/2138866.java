package org.gnf.seqtracs.assembly;

import org.gnf.seqtracs.software.Software;
import org.gnf.seqtracs.utilities.Utilities;
import org.gnf.seqtracs.seq.*;
import org.gnf.seqtracs.dbinterface.SeqDbInterface;
import java.io.*;
import java.net.*;
import java.lang.Runtime;
import java.lang.Process;
import java.sql.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class PhredPhrap extends Software {

    public PhredPhrap() {
        this(DEFAULT_PPZ_NAME, DEFAULT_PATH_TO_PPZ, DEFAULT_PPZ_VERSION, DEFAULT_PPZ_OS_NAME, DEFAULT_PPZ_REL_DATE);
    }

    public PhredPhrap(final String name_of_phredphrapz, final File path_to_phredphrapz, final String version, final String os_name, final Date release_date) {
        super(name_of_phredphrapz, path_to_phredphrapz, version, os_name, release_date, TYPE);
    }

    public boolean execute(final AssemblyProject assemb_project) throws SQLException, IOException, InterruptedException {
        File work_dir = assemb_project.getWorkDir();
        Utilities.isWriteableDirectory(new File(work_dir, CHROMAT_DIR_NAME));
        Utilities.isWriteableDirectory(new File(work_dir, EDIT_DIR_NAME));
        Utilities.isWriteableDirectory(new File(work_dir, PHD_DIR_NAME));
        int[] bio_seq_oids = assemb_project.getRawSeqOids();
        if (bio_seq_oids == null || bio_seq_oids.length < 1) {
            System.out.println("PhredPhrap.execute: No bio seq oids");
            return false;
        }
        SeqDbInterface seq_db = new SeqDbInterface(assemb_project.getDbConnection());
        int files_written = 0;
        for (int i = 0; i < bio_seq_oids.length; ++i) {
            int bio_seq_oid = bio_seq_oids[i];
            System.out.println(" bio_seq_oid = " + bio_seq_oid);
            String name = null, path = null;
            try {
                name = seq_db.retrieveTraceFileName(bio_seq_oid);
            } catch (SQLException ex) {
                if (DEBUG) {
                    ex.printStackTrace();
                }
                continue;
            }
            try {
                path = seq_db.retrieveTraceFilePath(bio_seq_oid);
            } catch (SQLException ex) {
                if (DEBUG) {
                    ex.printStackTrace();
                }
                continue;
            }
            writeTraceFile(new File(new File(work_dir, CHROMAT_DIR_NAME), name), name, path);
            files_written++;
        }
        if (files_written < 2) {
            return false;
        }
        assemble(assemb_project.getWorkDir(), assemb_project.getOptions());
        return true;
    }

    private void assemble(final File work_dir, final String options) throws SQLException, IOException, InterruptedException {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        int es = 0;
        File phredphrapz = new File(getPath().toString(), getName().toString());
        Utilities.doesExist(phredphrapz);
        p = r.exec(phredphrapz + " " + work_dir + " " + options);
        es = p.waitFor();
        if (es != 0) {
            throw new IOException(phredphrapz + " error or file write error");
        }
    }

    private void writeTraceFile(final File destination_file, final String trace_file_name, final String trace_file_path) throws IOException {
        URL url = null;
        BufferedInputStream is = null;
        FileOutputStream fo = null;
        BufferedOutputStream os = null;
        int b = 0;
        url = new URL("http://" + trace_file_path + "/" + trace_file_name);
        is = new BufferedInputStream(url.openStream());
        fo = new FileOutputStream(destination_file);
        os = new BufferedOutputStream(fo);
        while ((b = is.read()) != -1) {
            os.write(b);
        }
        os.flush();
        is.close();
        os.close();
    }

    private static final boolean DEBUG = true;

    public static final String CHROMAT_DIR_NAME = "chromat_dir", EDIT_DIR_NAME = "edit_dir", PHD_DIR_NAME = "phd_dir", ADD_SEQS_DIR_NAME = "add_seqs_dir", PHRED_PHRAP_CONTIGS_SEQ_FILE_SUFFIX = ".fasta.screen.contigs", PHRED_PHRAP_CONTIGS_QUAL_FILE_SUFFIX = ".fasta.screen.contigs.qual";

    public static final String TYPE = "ASSEMBLER", CONTACT = "czmasek@gnf.org";

    public static final String DEFAULT_PPZ_NAME = "phredPhrapZ", DEFAULT_PPZ_VERSION = "1.0", DEFAULT_PPZ_OS_NAME = "Linux";

    public static final File DEFAULT_PATH_TO_PPZ = new File("/nfs/dm3/homedir1/czmasek/ASSEMBLY");

    public static final Date DEFAULT_PPZ_REL_DATE = new Date(0);
}
