package sirf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class ComprimirTar {

    public static final String EXTENSION = ".tar";

    private String getFilename(String path) {
        String filename = path.substring(path.lastIndexOf('/') + 1);
        return filename;
    }

    public String createTar(List<String> listado, String tar_filename) {
        String filename = null;
        try {
            filename = this.createTar(listado, tar_filename, new FileOutputStream(tar_filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    /**
	 * Crea un fichero tar pasado en tar_filename con los ficheros listado, el flujo de este fichero
	 * a crear ser� pasado por el parametro out.
	 * 
	 * @param listado Cadenas de nombre de ficheros con su path incluido.
	 * @param tar_filename	Nombre del fichero tar que se va a crear.
	 * @param out	Es un flujo de salida en el que se ha usado el nombre de fichero pasado por
	 * el par�metro tar_filename
	 * @return El nombre del fichero tar completo con su path incluido.
	 */
    public String createTar(List<String> listado, String tar_filename, OutputStream out) {
        FileInputStream fis = null;
        try {
            TarArchiveOutputStream aos = new TarArchiveOutputStream(out);
            for (String path : listado) {
                File entrada = new File(path);
                TarArchiveEntry tae = new TarArchiveEntry(entrada, this.getFilename(path));
                aos.putArchiveEntry(tae);
                fis = new FileInputStream(entrada);
                int leido = fis.read();
                while (leido != -1) {
                    aos.write(leido);
                    leido = fis.read();
                }
                fis.close();
                aos.closeArchiveEntry();
            }
            aos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tar_filename;
    }
}
