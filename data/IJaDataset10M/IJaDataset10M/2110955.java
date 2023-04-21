package vxmlsurfer.bugreport;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class SendFile {

    String filesPath = "./vxmlsurfer/resources/bugreport/";

    boolean sendFile(String fileName, String server) {
        boolean success = false;
        DataOutputStream output;
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(fileName + "$");
            BufferedReader inFile = new BufferedReader(new FileReader(filesPath + fileName));
            String str;
            while ((str = inFile.readLine()) != null) {
                str = str.replace(" ", "%20");
                sb.append(str);
                sb.append("$");
            }
            sb.append("<end>");
            String contents = sb.toString();
            System.out.println(contents);
            URL servlet = new URL(server + "?File=" + contents);
            success = true;
            URLConnection con = servlet.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
            out.writeObject(new String("hello"));
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            in.close();
            success = true;
        } catch (Exception e) {
            System.out.println("Exception!!!");
            success = false;
        }
        return success;
    }
}
