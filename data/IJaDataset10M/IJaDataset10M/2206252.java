package com.tensegrity.palobrowser.util;

/**
 * <code>NamedRunnableCommand</code>, a runnable with a name
 * and the notion of an associated user-object.
 *
 * @author Stepan Rutz
 * @version $ID$
 */
public abstract class NamedRunnableCommand implements RunnableCommand {

    private String name;

    private Object userObject;

    public NamedRunnableCommand(String name) {
        this(name, null);
    }

    public NamedRunnableCommand(String name, Object userObject) {
        this.name = name;
        this.userObject = userObject;
    }

    public String getName() {
        return name;
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public abstract void run();
}
