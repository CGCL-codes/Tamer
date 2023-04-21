package de.sharpner.jcmd.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class AdvancedConfigLoader {

    private static final String CONFIG_FOLDER = "/.jcmd/";

    private String home = System.getProperty("user.home");

    private LinkedList<String> lines = new LinkedList<String>();

    private File f;

    private boolean first_time = false;

    public AdvancedConfigLoader(String name) {
        File folder = new File(home + CONFIG_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }
        f = new File(name);
        if (!f.isAbsolute()) {
            f = new File(home + CONFIG_FOLDER + name);
        }
        if (f.exists()) {
            loadConfiguration();
        } else {
            first_time = true;
        }
    }

    public boolean isFirstTime() {
        return this.first_time;
    }

    public String getValue(String identifier) {
        for (int i = 0; i < lines.size(); i++) {
            String temp = lines.get(i).trim();
            if (temp.startsWith(identifier)) {
                String[] tmp = temp.split("!");
                if (tmp.length != 2) {
                    System.err.println("Warning: Argument not in valid format (identifier value)");
                    System.err.println("Line is: " + lines.get(i));
                } else {
                    if (tmp[1].equals("null")) {
                        return null;
                    }
                    return tmp[1];
                }
            }
        }
        return null;
    }

    public void setValue(String identifier, String value) {
        String line = identifier + "!" + value;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().startsWith(identifier)) {
                lines.set(i, line);
                return;
            }
        }
        lines.add(line);
    }

    public void clearValues() {
        lines = new LinkedList<String>();
    }

    private boolean isWritable = true;

    public void disableWriteAcces() {
        isWritable = false;
    }

    private void loadConfiguration() {
        try {
            lines = FileUtil.read(f.getAbsolutePath(), "#");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeConfiguration() throws IOException {
        if (isWritable) {
            FileUtil.write(lines, f.getAbsolutePath());
            first_time = false;
        }
    }
}
