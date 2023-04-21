package proveRead;

import java.io.File;
import java.util.List;
import model.JOFSFile;
import model.JOFSModel;
import model.exception.NotExistentException;

/**
 * Questa classe serve per provare la seguente funzionalità: elencare tutte le proprietà
 * asserite (e non dedotte) per un dato File
 * @author mashiro
 *
 */
public class proveListObjectFileProperties {

    public static void main(String[] args) {
        JOFSModel jofs;
        try {
            jofs = new JOFSModel("prove_jofs");
            File f1 = new File("/home/rayman/Scrivania/xyz/Duomo_Milano copia.tif");
            File f2 = new File("/home/rayman/Scrivania/xyz/01 - Processi di decisione Markoviana - MDP.odt");
            jofs.submitTranslation(f1, f2);
            try {
                List<String> list = jofs.formatObjectFileProperties(f1);
                for (String s : list) {
                    if (s != null) {
                        System.out.println(s);
                    }
                }
            } catch (NotExistentException e) {
                System.out.println(e.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
