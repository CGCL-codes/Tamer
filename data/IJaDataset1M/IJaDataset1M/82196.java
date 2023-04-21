package ru.onlytime.ssh.deploy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PSAuxParser {

    private static final Pattern pattern = Pattern.compile("\\w* +(\\d+) +\\d+");

    public static String getCommand(String prefix, String line) {
        int index = line.indexOf(prefix);
        if (index == -1) {
            return null;
        }
        String command = line.substring(index + prefix.length());
        return command;
    }

    public static Integer getPID(String line) {
        Matcher m = pattern.matcher(line);
        if (!m.find()) {
            return null;
        }
        return Integer.parseInt(m.group(1));
    }
}
