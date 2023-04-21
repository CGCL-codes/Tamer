package org.fao.fenix.web.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FenixMenuItem implements IsSerializable {

    private String name;

    private int level;

    private String parent;

    private boolean children;

    private String command;

    public FenixMenuItem() {
    }

    public FenixMenuItem(String name, int level, String parent, boolean children, String command) {
        this.setName(name);
        this.setLevel(level);
        this.setParent(parent);
        this.setChildren(children);
        this.setCommand(command);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isChildren() {
        return children;
    }

    public void setChildren(boolean children) {
        this.children = children;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
