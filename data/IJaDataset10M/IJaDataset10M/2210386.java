package libjdc.hublist;

import libjdc.hublist.HubEntry;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Iterator;
import org.apache.tools.bzip2.CBZip2OutputStream;

/**
 *
 * @author root
 */
public class ConfigHublistWriter {

    private static String formaterPlain = "{0}|{1}|{2}|{3}||||||";

    private HublistEntry hublistEntry;

    /** Creates a new instance of XMLHublistWriter */
    public ConfigHublistWriter(HublistEntry hublistEntry, String url) throws IOException {
        this.hublistEntry = hublistEntry;
        BufferedWriter fMed = new BufferedWriter(new FileWriter(url));
        System.out.println(this.hublistEntry.getHubEntries().size());
        writeConfigFile(fMed, this.hublistEntry.getHubEntries().iterator());
        fMed.flush();
        fMed.close();
        File source = new File(url);
        pack(new File(url), new File(url + ".bz2"));
        source.delete();
    }

    protected void pack(File source, File zipFile) {
        CBZip2OutputStream zOut = null;
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zipFile));
            bos.write('B');
            bos.write('Z');
            zOut = new CBZip2OutputStream(bos);
            zipFile(source, zOut);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (zOut != null) {
                try {
                    zOut.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * zip a file to an output stream
     * @param file the file to zip
     * @param zOut the output stream
     * @throws IOException on error
     */
    protected void zipFile(File file, OutputStream zOut) throws IOException {
        FileInputStream fIn = new FileInputStream(file);
        try {
            zipFile(fIn, zOut);
        } finally {
            fIn.close();
        }
    }

    /**
     * zip a stream to an output stream
     * @param in   the stream to zip
     * @param zOut the output stream
     * @throws IOException
     */
    private void zipFile(InputStream in, OutputStream zOut) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        int count = 0;
        do {
            zOut.write(buffer, 0, count);
            count = in.read(buffer, 0, buffer.length);
        } while (count != -1);
    }

    private void writeConfigFile(BufferedWriter fMed, Iterator<HubEntry> it) throws IOException {
        Object[] args;
        while (it.hasNext()) {
            HubEntry hub = it.next();
            args = new Object[] { hub.getName().replaceAll("[|\n]", "").trim(), hub.getAddress(), hub.getDescription().replaceAll("[|\n]", "").trim(), "" + hub.getUsers() };
            fMed.write(MessageFormat.format(formaterPlain, args));
            fMed.newLine();
        }
    }
}
