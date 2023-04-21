package org.wings.plaf.css;

import org.wings.script.ScriptListener;

public class LayoutFillScript implements ScriptListener {

    String name;

    public LayoutFillScript(String name) {
        this.name = name;
    }

    public String getEvent() {
        return null;
    }

    public String getCode() {
        return null;
    }

    public String getScript() {
        return "layoutFill(document.getElementById('" + name + "'));\n";
    }

    public int getPriority() {
        return 0;
    }
}
