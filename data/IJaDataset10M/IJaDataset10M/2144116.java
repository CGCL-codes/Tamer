package src.fileUtilities;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;
import src.lib.CurrentVersion;
import src.lib.ioInterfaces.Generic_AlignRead_Iterator;
import src.lib.ioInterfaces.Log_Buffer;
import src.lib.objects.AlignedRead;

/**
 * Separate Eland files based on chromosome
 * output is <chr>.part.aligner based losely
 * upon matthew Bainbridge's SeparateReads.java
 *
 * @author Genome Sciences Centre
 * @version $Revision: 3247 $
 */
public class SortFiles {

    private static Log_Buffer LB;

    private static final int FREQ_PARAM = 100000;

    /** Dummy Constructor */
    private SortFiles() {
    }

    /**
	 *
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("usage: <format> <output path/ (log only)> <Unsorted file(s)>");
            System.exit(0);
        }
        String output_path = args[1];
        if (!output_path.endsWith(System.getProperty("file.separator"))) {
            output_path = output_path.concat(System.getProperty("file.separator"));
        }
        LB = Log_Buffer.getLogBufferInstance();
        LB.addPrintStream(System.out);
        LB.addLogFile(output_path + "SortFile.log");
        Thread th = new Thread(LB);
        th.start();
        new CurrentVersion(LB);
        LB.Version("SortFiles", "$Revision: 3247 $");
        String aligner = args[0];
        if (!aligner.equalsIgnoreCase("bed") && !aligner.equalsIgnoreCase("bowtie") && !aligner.equalsIgnoreCase("eland") && !aligner.equalsIgnoreCase("elandext") && !aligner.equalsIgnoreCase("gff") && !aligner.equalsIgnoreCase("interval") && !aligner.equalsIgnoreCase("mapview")) {
            LB.error("Unrecognized aligner: " + aligner);
            LB.error("Must be one of: bed, bowtie, eland, elandext, interval, gff, mapview");
            LB.die();
        }
        for (int x = 2; x < args.length; x++) {
            String filename = args[x];
            LB.notice("Processing file: " + filename);
            int cnt = 0;
            Vector<AlignedRead> reads = new Vector<AlignedRead>(1000000);
            Generic_AlignRead_Iterator it = new Generic_AlignRead_Iterator(LB, args[0], "source", args[x], 0, 0, null, 0, false);
            AlignedRead ar = null;
            while (it.hasNext()) {
                cnt++;
                if (cnt % FREQ_PARAM == 0) {
                    LB.notice(cnt + " lines read.");
                }
                try {
                    ar = it.next();
                } catch (NoSuchElementException nsee) {
                    continue;
                }
                reads.add(ar);
            }
            it.close();
            if (reads.size() < 1) {
                LB.error("No reads found in this file: " + filename);
                continue;
            }
            LB.notice("Sorting reads...");
            AlignedRead[] sorted_reads = reads.toArray(new AlignedRead[reads.size()]);
            reads.clear();
            Arrays.sort(sorted_reads);
            BufferedWriter bw = null;
            if (!filename.endsWith(".gz")) {
                filename = filename.concat(".gz");
                LB.notice("Creating and writing to gzipped file in same directory...");
            } else {
                LB.notice("Creating and writing to new file in place...");
            }
            try {
                bw = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(filename))));
            } catch (IOException io) {
                LB.error("Could not create gziped aligner file. file.");
                LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                LB.die();
            }
            assert (bw != null);
            cnt = 0;
            String current_chr = null;
            boolean warning = true;
            for (AlignedRead r : sorted_reads) {
                if (current_chr == null) {
                    current_chr = r.get_chromosome();
                } else if (warning && !current_chr.equals(r.get_chromosome())) {
                    LB.error("Multiple chromosomes present in file - this process will probably not generate a usable file!");
                    LB.error("Please use Separate Reads first!");
                    warning = false;
                }
                cnt++;
                if (cnt % FREQ_PARAM == 0) {
                    LB.notice(cnt + " lines written.");
                }
                try {
                    if (aligner.equalsIgnoreCase("eland")) {
                        bw.write(r.outEland());
                    } else if (aligner.equalsIgnoreCase("elandext")) {
                        bw.write(r.outElandExt());
                    } else if (aligner.equalsIgnoreCase("gff")) {
                        bw.write(r.outGff());
                    } else if (aligner.equalsIgnoreCase("bed")) {
                        bw.write(r.outBed());
                    } else if (aligner.equalsIgnoreCase("mapview")) {
                        bw.write(r.outMapview());
                    } else if (aligner.equalsIgnoreCase("bowtie")) {
                        bw.write(r.outBowtie());
                    } else if (aligner.equalsIgnoreCase("interval")) {
                        bw.write(r.outInterval());
                    }
                    bw.newLine();
                } catch (IOException io) {
                    LB.error("Could not write to file.");
                    LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                    LB.die();
                }
            }
            try {
                bw.close();
            } catch (IOException io) {
                LB.error("Could not close file " + filename);
                LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                LB.die();
            }
        }
        LB.close();
    }
}
