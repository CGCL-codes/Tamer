package ru.adv.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Helper class to parse command line arguments 
 * 
 * @author vic
 *
 */
public class Arguments {

    private List<Argument> argumentList;

    private Map<String, String> namedOptions;

    private String helpMessage;

    /**
     *  Constructor
     */
    public Arguments(String helpMassage) {
        this.helpMessage = helpMassage;
        this.argumentList = new ArrayList<Argument>();
        this.namedOptions = new HashMap<String, String>();
    }

    public void printHelpMessage() {
        System.out.println(this.helpMessage);
    }

    public String getNamedOption(String optionName) {
        return this.namedOptions.get(optionName);
    }

    /**
     * fill argumentList
     * @param arguments
     */
    public void parse(String[] arguments, String[] knownOptions) throws Exception {
        int i = 0;
        while (i < arguments.length) {
            String key = null;
            String value = null;
            String item = arguments[i];
            if (item.startsWith("-")) {
                if (item.length() > 1) {
                    key = item.substring(1);
                    testKey(key, knownOptions);
                    if (i + 1 < arguments.length && !arguments[i + 1].startsWith("-")) {
                        i += 1;
                        value = arguments[i];
                    } else {
                        value = "";
                    }
                } else {
                    value = item;
                }
            } else {
                value = item;
            }
            final Argument arg = new Argument(key, value);
            argumentList.add(arg);
            if (arg.getKey() != null) {
                this.namedOptions.put(key, value);
            }
            i += 1;
        }
    }

    private void testKey(String optionName, String[] knownOptions) throws Exception {
        if (!containsInArray(knownOptions, optionName)) {
            throw new Exception("Unknown option: " + optionName);
        }
        if (this.namedOptions.containsKey(optionName)) {
            throw new Exception("Option '" + optionName + "' is defined twice in command line");
        }
    }

    /**
     * 
     */
    public List<Argument> getArguments() {
        return argumentList;
    }

    private boolean containsInArray(String[] arr, String value) {
        for (String arrValue : arr) {
            if (arrValue != null && arrValue.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     */
    public class Argument {

        String key, value;

        public Argument(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
