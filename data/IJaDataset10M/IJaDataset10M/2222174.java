package cz.cesnet.meta.accounting.pbsclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    /**
   * @param args
   */
    public static void main(String[] args) {
        if (args.length != 2) {
            printUsage();
        }
        String url = args[0];
        String path = args[1];
        BufferedReader pbsFileReader = null;
        try {
            pbsFileReader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException ex) {
            System.err.println("Pbs file " + path + " not found");
            System.exit(1);
        }
        String line = "";
        HttpURLConnection conn = null;
        BufferedWriter out = null;
        BufferedReader in = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            while (true) {
                line = pbsFileReader.readLine();
                if (line == null) {
                    break;
                }
                out.write(line);
                out.newLine();
                System.err.println(line);
            }
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            line = "";
            while (true) {
                line = in.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar acct_pbc_client.jar hostname:port/path/to/server/resource pbs_file");
        System.exit(1);
    }
}
