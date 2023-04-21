package mp3.archivos;

import java.io.*;
import java.util.ArrayList;
import mp3.services.Reporter;
import mp3.services.ServiceSetter;

/**
 *
 * @author juavill
 */
public class ArchivosM3u extends AbstractArchivos {

    @Override
    public void escribir(File fichero, String[] lista) throws FileNotFoundException, IOException {
        ((Reporter) ServiceSetter.getServiceSetter().getServiceByName(Reporter.class.getName())).addCount("m3u");
        String[] lcorrect;
        int i;
        PrintStream f = new PrintStream(fichero);
        lcorrect = comprobar(fichero, lista);
        f.println("#EXTM3U");
        for (i = 0; i < lcorrect.length; i++) {
            f.println("#EXTINF:0," + new File(lcorrect[i]).getName().replaceAll(".mp3$", ""));
            f.println(lcorrect[i]);
        }
    }

    @Override
    public String[] leer(File fichero) throws FileNotFoundException, IOException {
        ((Reporter) ServiceSetter.getServiceSetter().getServiceByName(Reporter.class.getName())).addCount("m3u");
        String[] salida;
        String lineabuffer;
        int i;
        boolean ficheroleido = false;
        ArrayList<String> list;
        FileReader f = new FileReader(fichero);
        BufferedReader bf;
        bf = new BufferedReader(f);
        list = new ArrayList<String>();
        while (!ficheroleido) {
            lineabuffer = bf.readLine();
            if (lineabuffer != null) {
                if (lineabuffer.charAt(0) != '#') list.add(lineabuffer);
            } else ficheroleido = true;
        }
        salida = new String[list.size()];
        for (i = 0; i < list.size(); i++) {
            salida[i] = list.get(i);
        }
        return comprobar(fichero, salida);
    }

    @Override
    public void escribirRelativo(File fichero, String[] lista) throws FileNotFoundException, IOException {
        ((Reporter) ServiceSetter.getServiceSetter().getServiceByName(Reporter.class.getName())).addCount("m3u");
        String[] lcorrect;
        String relativeCorrect;
        int i;
        PrintStream f = new PrintStream(fichero);
        lcorrect = comprobar(fichero, lista);
        f.println("#EXTM3U");
        for (i = 0; i < lcorrect.length; i++) {
            relativeCorrect = RelativePath.getRelativePath(fichero.getParentFile(), new File(lcorrect[i]));
            f.println("#EXTINF:0," + new File(relativeCorrect).getName().replaceAll(".mp3$", ""));
            f.println(relativeCorrect);
        }
    }
}
