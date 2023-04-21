package com.googlecode.progobots.ui.text;

public abstract class AbstractCommand implements Command {

    private String description;

    private String name;

    private int argCount;

    public AbstractCommand(String name, String description) {
        this(name, description, 0);
    }

    public AbstractCommand(String name, String description, int argCount) {
        this.name = name;
        this.description = description;
        this.argCount = argCount;
    }

    public boolean isCommand(String... values) {
        if (name.equals(values[0])) {
            return true;
        }
        return false;
    }

    public void checkArgCount(String... values) {
        if (argCount != values.length - 1) {
            throw new CommandException("Invalid number of arguments");
        }
    }

    public boolean isExit() {
        return false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
