package media_archiver_base.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import javax.xml.bind.JAXB;
import media_archiver_base.constants.Constants;
import media_archiver_base.data.Catalog;

public class CatalogWriter {

    public CatalogWriter() {
    }

    public void saveCatalog(File file, Catalog c) {
        try {
            File tmpfile = File.createTempFile("mediafinder", null);
            FileWriter fw = new FileWriter(tmpfile);
            JAXB.marshal(c, fw);
            zip(tmpfile, file);
            tmpfile.deleteOnExit();
            tmpfile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void zip(File input, File output) {
        OutputStream os = null;
        InputStream is = null;
        try {
            os = new GZIPOutputStream(new FileOutputStream(output));
            is = new FileInputStream(input);
            byte[] buffer = new byte[8192];
            for (int length; (length = is.read(buffer)) != -1; ) os.write(buffer, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
