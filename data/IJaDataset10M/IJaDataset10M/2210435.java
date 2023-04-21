package org.ashkelon.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.ashkelon.util.Logger;
import org.ashkelon.util.StringUtils;

/**
 * A class that can parse a .sql script into a list of database statements
 * (ignores -- comments, blank lines, and properly handle multi-line statements
 * terminated by ';'
 *
 * @author Eitan Suez
 */
public class ScriptParser {

    public ScriptParser() {
    }

    public static List parse(String script) {
        return parse(script, true);
    }

    /**
    * @param script the fully qualified path & name of the .sql script file
    * to parse
    * @return the list of sql statements in the script file, as strings
    */
    public static List parse(String script, boolean warn) {
        List commands = new ArrayList();
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(script);
            if (is == null) {
                if (warn) Logger.getInstance().brief("could not find resource:  " + script);
                return commands;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = br.readLine()) != null) {
                line = clean_line(line);
                if (StringUtils.isBlank(line)) continue;
                int end_idx = line.indexOf(";");
                while (end_idx == -1) {
                    String nextline = br.readLine();
                    if (nextline == null) break;
                    line += clean_line(nextline);
                    end_idx = line.indexOf(";");
                }
                line = line.substring(0, end_idx);
                commands.add(line);
            }
            br.close();
            is.close();
        } catch (IOException ex) {
            Logger.getInstance().error("Unable to print usage!");
            Logger.getInstance().error("IOException: " + ex.getMessage());
        }
        return commands;
    }

    private static String clean_line(String line) {
        line = line.trim();
        if (StringUtils.isBlank(line) || line.startsWith("--")) return "";
        int comment_idx = line.indexOf("--");
        if (comment_idx > 0) line = line.substring(0, comment_idx).trim();
        return line + " ";
    }

    public static void main(String[] args) {
    }
}
