package org.jmule.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
* Helper Progy for the win32 system. Used for creation of file.encoding aware command line.
*/
public class WinLauncherGen {

    public static final void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: \n" + " WinLauncherGen 'mode.file' ['starterTemplate']\n");
            System.exit(-1);
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            String line = br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            line = br.readLine();
            String consoleWidth = "80";
            int idx = line.indexOf(":");
            idx = idx != -1 ? idx : line.indexOf("=");
            if (idx != -1) {
                consoleWidth = line.substring(idx + 1).trim();
            }
            line = br.readLine();
            while (line != null && line.toLowerCase().indexOf("codepage") < 0) {
                line = br.readLine();
            }
            String codePage = "CP850";
            idx = line.indexOf(":");
            if (idx != -1) {
                codePage = "CP" + line.substring(idx + 1).trim();
            }
            System.setProperty("org.jmule.util.ConsoleLineFormatter.consoleWidth", consoleWidth);
            System.setProperty("org.jmule.resource.Messages.encoding", codePage);
            if (args.length > 1) System.out.println(args[1].replaceAll("!!fileEncoding!!", codePage).replaceAll("!!consoleWidth!!", consoleWidth));
        } catch (IOException io_err) {
            System.err.println("Trouble reading/parsing " + args[1] + ". (" + io_err.getMessage() + ")");
            System.exit(-2);
        }
    }
}
