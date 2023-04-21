package proveRead;

import java.io.File;
import java.util.List;
import model.JOFSFile;
import model.JOFSModel;
import model.exception.NotExistentException;

/**
 * Questa classe serve per provare la seguente funzionalità: mostrare tutte le classi alle quali
 * appartiene un dato File
 * @author mashiro
 *
 */
public class proveListFileClasses {

    public static void main(String[] args) {
        JOFSModel jofs;
        try {
            jofs = new JOFSModel("prove_jofs");
            try {
                JOFSFile ff = jofs.getFile(new File("/home/rayman/Examples/oo-welcome.odt"));
                List<String> list = jofs.listFileClasses(ff.getFile());
                for (String s : list) if (s != null) System.out.println(s);
            } catch (NotExistentException e) {
                System.out.println(e.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
