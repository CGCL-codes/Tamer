package at.morphic.apps.musicplayer;

import java.util.Vector;
import java.io.*;

public class AppleScript {

    private String script;

    private Vector result;

    public AppleScript(String script) {
        this.script = script;
        result = new Vector();
    }

    public Vector execute() {
        result.removeAllElements();
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("script_tmp"));
            out.write(this.script);
            out.close();
            Process process = Runtime.getRuntime().exec("osascript script_tmp");
            DataInputStream reader = new DataInputStream(process.getInputStream());
            process.waitFor();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                result.add(line);
            }
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    public Vector getResult() {
        return result;
    }
}
