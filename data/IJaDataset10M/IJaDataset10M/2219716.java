package miniminer.utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DataFileReader {

    public static String getFileExtension(String filename) {
        String ext = "";
        int i = filename.lastIndexOf('.');
        if (i > 0 && i < filename.length() - 1) {
            ext = filename.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    @SuppressWarnings("unchecked")
    public static BufferedReader readAFile(String filename) throws IOException {
        BufferedReader buf_reader = null;
        String ext = getFileExtension(filename);
        if (ext.equals("zip")) {
            ZipFile zf = new ZipFile(filename);
            Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zf.entries();
            if (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                InputStream z_e = zf.getInputStream(e);
                InputStreamReader is_z_e = new InputStreamReader(z_e);
                buf_reader = new BufferedReader(is_z_e);
            }
        } else if (ext.equals("gz")) {
            FileInputStream f = new FileInputStream(filename);
            GZIPInputStream gz_f = new GZIPInputStream(f);
            InputStreamReader is_gz_f = new InputStreamReader(gz_f);
            buf_reader = new BufferedReader(is_gz_f);
        } else {
            FileReader fr = new FileReader(filename);
            buf_reader = new BufferedReader(fr);
        }
        return buf_reader;
    }
}
