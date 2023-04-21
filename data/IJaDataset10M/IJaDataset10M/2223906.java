package com.soramaki.fna.commands;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Hashtable;

/**
 * A Command subclass representing the 'help' command.
 */
public class HelpCommand extends Command {

    private static final String CHAPTER = "!chapter:";

    private static Hashtable<String, String> helptexts = null;

    private static void readHelpFile() {
        File file = null;
        if (helptexts == null) {
            file = Utils.getFile("help/help.txt", true);
            if (file == null) {
                return;
            }
            helptexts = new Hashtable<String, String>();
            try {
                String line = null;
                String text = "";
                String key = null;
                LineNumberReader lnr = new LineNumberReader(new FileReader(file));
                while (true) {
                    line = lnr.readLine();
                    if (line == null) {
                        if (key != null && text != null) {
                            helptexts.put(key, text);
                        }
                        break;
                    }
                    if (line.startsWith(CHAPTER)) {
                        if (key != null && text != null) {
                            helptexts.put(key, text);
                        }
                        key = line.substring(CHAPTER.length()).trim();
                        text = "";
                    } else {
                        if (key == null) continue; else {
                            text += "\n" + line;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String param = null;

    public HelpCommand() {
        super();
    }

    @Override
    public void init(String params) {
        if (helptexts == null) {
            readHelpFile();
            if (helptexts == null) {
                errorstring = "Could not load help file";
                return;
            }
        }
        param = params.toLowerCase().trim();
    }

    @Override
    public boolean execute() {
        if (helptexts == null) {
            errorstring = "Could not load help file";
            return false;
        }
        String text = helptexts.get(param);
        if (text == null) text = helptexts.get("help");
        infostring = text;
        return true;
    }
}
