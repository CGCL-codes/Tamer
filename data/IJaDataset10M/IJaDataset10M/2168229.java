package StoMpd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.SwingWorker;

/**
 *
 * @author espenk
 */
public class RandomInfo extends SwingWorker<String, Void> {

    private ConfigHandler ch = ConfigHandler.getInstance();

    List<String> msgCollection = new ArrayList<String>();

    int i = 0;

    @Override
    protected String doInBackground() {
        msgCollection = readTextFromJar("/StoMpd/resources/msgcoll");
        Random rand = new Random();
        i = rand.nextInt(msgCollection.size() - 1);
        return "ok";
    }

    @Override
    protected void done() {
    }

    protected String getString() {
        return msgCollection.get(i);
    }

    public static List<String> readTextFromJar(String s) {
        InputStream is = null;
        BufferedReader br = null;
        String line;
        ArrayList<String> list = new ArrayList<String>();
        try {
            is = RandomInfo.class.getResourceAsStream(s);
            br = new BufferedReader(new InputStreamReader(is));
            while (null != (line = br.readLine())) {
                list.add(line);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        return list;
    }
}
