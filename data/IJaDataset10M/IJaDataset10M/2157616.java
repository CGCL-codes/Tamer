package de.tud.kom.nat.util;

import java.util.ArrayList;
import java.util.HashMap;

class OptionType {

    private final String name;

    private final boolean hasValue;

    public OptionType(String name, boolean hasValue) {
        this.name = name;
        this.hasValue = hasValue;
    }

    public String getName() {
        return name;
    }

    public boolean hasValue() {
        return hasValue;
    }
}

class Option {

    private final String content;

    private final OptionType type;

    public Option(OptionType type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public OptionType getType() {
        return type;
    }
}

class Argument {

    private final String content;

    private final int index;

    public Argument(int index, String content) {
        this.index = index;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getIndex() {
        return index;
    }
}

public abstract class ArgumentParser {

    private HashMap<String, OptionType> optionTypes = new HashMap<String, OptionType>(5);

    private ArrayList<Option> options = new ArrayList<Option>(5);

    public ArgumentParser(String args[]) {
        addOptionTypes();
        parseArguments(args);
    }

    public abstract void addOptionTypes();

    protected void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].length() > 1 && args[i].startsWith("-")) {
                String name = args[i].substring(1);
                OptionType ot = findOptionType(name);
                if (!ot.hasValue()) options.add(new Option(ot, null)); else {
                }
            }
        }
    }

    protected OptionType findOptionType(String name) {
        OptionType ot = optionTypes.get(name);
        if (ot == null) {
            throw new IllegalStateException("Unknown argument: " + name);
        }
        return ot;
    }

    protected void addOptionType(String name, boolean hasValue) {
        optionTypes.put(name, new OptionType(name, hasValue));
    }
}
