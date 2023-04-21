package util;

import java.io.*;

public class CommandRunner {

    public void executeCommand(String command) {
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int i = p.waitFor();
            if (i == 0) System.out.println("Command executed successfully"); else System.out.println("Failed to execute command");
            String s = null;
            while ((s = in.readLine()) != null) System.out.println(s);
        } catch (Exception ex) {
            System.out.println("invalid command");
        }
    }
}
