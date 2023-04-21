package parse;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;
import java.util.HashMap;

public class config {

    public HashMap laConf;

    public config() {
        laConf = readFile();
    }

    public HashMap readFile() {
        String fichier = new String();
        fichier = "config.cfg";
        HashMap<String, String> retour = new HashMap();
        try {
            InputStream ips = new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne = new String();
            while ((ligne = br.readLine()) != null) {
                if (ligne.matches("(.*)=(.*)")) {
                    String[] items = ligne.split("=");
                    retour.put(items[0], items[1]);
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
            retour = null;
        }
        return retour;
    }
}
