package rothserve;

import java.io.*;
import java.io.InputStreamReader;
import java.net.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Alex
 */
public class php {

    PrintWriter writer2;

    public void phpWrite(String newPath, PrintWriter writer, Socket client) {
        try {
            writer2 = writer;
            System.out.println("PHP serving");
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("php ".concat(newPath));
            System.out.println("PHP process started");
            BufferedReader phpReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            p.waitFor();
            while (phpReader.ready() != false) {
                String phpReadLine = phpReader.readLine();
                writer.println(phpReadLine);
                System.out.println(phpReadLine);
            }
            System.out.println("PHP page Served");
        } catch (FileNotFoundException e) {
            typeErrorHTTPFNF();
        } catch (Exception ex) {
            typeErrorHTTPGSE();
        }
    }

    public void typeErrorHTTPFNF() {
        writer2.println("HTTP/1.1 404 Not Found");
        writer2.println("");
        writer2.println("<html><head><title>404 Not Found</title></head><body><h1>Not Found</h1><p>The requested URL was not found on this server.</p><hr><address>RothServe/0.01 Mac Server</address></body></html>");
        writer2.close();
        System.out.println("404Sent");
    }

    public void typeErrorHTTPGSE() {
        writer2.println("HTTP/1.1 500 Internal Server Error");
        writer2.println("");
        writer2.println("<html><head><title>500 Internal Server Error</title></head><body><h1>Not Found</h1><p>There is an internal problem with this server, check back later.</p><hr><address>RothServe/0.01 Mac Server</address></body></html>");
        writer2.close();
        System.out.println("500Sent");
    }
}
