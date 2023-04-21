package net.sourceforge.rcontrol.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import net.sourceforge.rcontrol.base.CfgFileFilter;

public class ScriptFile extends File {

    private String script;

    public ScriptFile(String arg0, String arg1) {
        super(arg0, arg1);
    }

    public ScriptFile(String arg0) {
        super(arg0);
    }

    public ScriptFile(URI arg0) {
        super(arg0);
    }

    public ScriptFile(File arg0, String arg1) {
        super(arg0, arg1);
    }

    public static String[] getCfgFiles(String path) {
        ScriptFile workfile = new ScriptFile(path);
        return workfile.list(new CfgFileFilter());
    }

    public static boolean readScript(ScriptFile file) {
        boolean retVal = false;
        if (file.exists() && file.canRead()) {
            FileReader fr = null;
            try {
                fr = new FileReader(file);
            } catch (FileNotFoundException e) {
                ;
            }
            BufferedReader reader = new BufferedReader(fr);
            String line;
            String script = "";
            try {
                while ((line = reader.readLine()) != null) {
                    script = script + line + "\n";
                }
                file.setScript(script.trim());
                retVal = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return retVal;
    }

    public static boolean writeScript(ScriptFile file) {
        boolean retVal = false;
        if (file.canWrite()) {
            FileWriter out_file = null;
            try {
                out_file = new FileWriter(file);
                BufferedWriter out = new BufferedWriter(out_file);
                out.write(file.getScript());
                out.close();
                retVal = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return retVal;
    }

    /**
	 * @return Returns the script.
	 */
    public String getScript() {
        return script;
    }

    /**
	 * @param script The script to set.
	 */
    public void setScript(String script) {
        this.script = script;
    }
}
