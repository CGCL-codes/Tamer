package org.terentich.ox.cli;

import java.util.ArrayList;

/**
 * <b>Project:</b> OX Framework <br>
 * <b>Description: </b> <br>
 * <b>Date: </b> 10.01.2009 <br>
 * @author Alexey V. Terentyev
 */
public class CLAnalyzer {

    private String[] args;

    private CLKey[] keys;

    private ArrayList<CLKey> reqKeys = new ArrayList<CLKey>();

    private boolean required;

    public CLAnalyzer(String[] _args, CLKey[] _keys) {
        args = _args;
        keys = _keys;
        for (CLKey key : keys) {
            if (key.isRequired()) {
                reqKeys.add(key);
                required = true;
            }
        }
    }

    public String getCLValue(String _key) {
        if (!isCorrectLine(false)) return null;
        for (CLKey key : keys) {
            if (key.isRequiredValues() && key.getKey().equals(_key)) return args[1];
        }
        return null;
    }

    public boolean containsKey(String _key) {
        if (!isCorrectLine(false)) return false;
        for (CLKey key : keys) {
            if (key.getKey().equals(_key)) {
                if (args[0].contains(key.getKey())) return true; else return false;
            }
        }
        return false;
    }

    public boolean isCorrectLine(boolean _showMessages) {
        if (required) {
            if (args.length == 0 || !args[0].startsWith("-")) {
                if (_showMessages) printErrorMessage();
                return false;
            }
        }
        for (CLKey key : reqKeys) {
            if (!args[0].contains(key.getKey())) {
                if (_showMessages) printErrorMessage(key);
                return false;
            }
        }
        for (CLKey key : keys) {
            if (key.isRequiredValues()) {
                if (args.length < 2) {
                    if (_showMessages) {
                        System.out.print("The key '" + key.getKey() + "' - must have " + "a one of these values: ");
                        for (String value : key.getValues()) System.out.println(value + " ");
                    }
                    return false;
                } else {
                    for (String value : key.getValues()) {
                        if (args[1].equals(value)) return true;
                    }
                    if (_showMessages) {
                        System.out.print("The key '" + key.getKey() + "' - must have " + "a one of these values: ");
                        for (String value : key.getValues()) System.out.println(value + " ");
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private void printErrorMessage(CLKey _key) {
        System.out.println("You must specify this argument: ");
        System.out.println(_key.getKey() + " - " + _key.getDescription());
    }

    private void printErrorMessage() {
        System.out.println("You must specify these arguments: ");
        for (CLKey key : reqKeys) System.out.println(key.getKey() + " - " + key.getDescription());
    }

    public static void main(String[] _args) {
        CLKey[] keys = new CLKey[] { new CLKey("h", "help command").setRequired(true), new CLKey("v", "verbose mode", "all"), new CLKey("f", "output file") };
        CLAnalyzer cline = new CLAnalyzer(_args, keys);
        System.out.println(cline.getCLValue("v"));
        if (cline.containsKey("h")) {
            System.out.println("This program - just for fun.");
        }
    }
}
