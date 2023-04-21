package slurper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ThreadCollect extends Thread {

    private String lien;

    private int cptLiens;

    String rep;

    public ThreadCollect(String rep, String lien, int cptLiens) {
        this.rep = rep;
        File f = new File("./corpus/" + rep + "/");
        if (!f.exists()) f.mkdir();
        this.lien = lien;
        this.cptLiens = cptLiens;
    }

    public void run() {
        URLConnection connectLien = null;
        URL urlLien = null;
        InputStream in = null;
        OutputStream out;
        GetHTMLText H2T = new GetHTMLText();
        try {
            urlLien = new URL(lien);
            connectLien = urlLien.openConnection();
            in = connectLien.getInputStream();
            out = new FileOutputStream("./corpus/" + rep + "/" + cptLiens + ".html");
            BufferedInputStream inBuffer = new BufferedInputStream(in);
            BufferedOutputStream outBuffer = new BufferedOutputStream(out);
            FileWriter fw = new FileWriter("./corpus/" + rep + "/" + cptLiens + ".txt", true);
            BufferedWriter output = new BufferedWriter(fw);
            int theByte = 0;
            while ((theByte = inBuffer.read()) > -1) {
                outBuffer.write(theByte);
            }
            outBuffer.flush();
            outBuffer.close();
            inBuffer.close();
            out.flush();
            out.close();
            in.close();
            String text = H2T.getText("./corpus/" + rep + "/" + cptLiens + ".html");
            output.write(text);
            output.flush();
            output.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
