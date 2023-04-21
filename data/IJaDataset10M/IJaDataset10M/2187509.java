package lebah.portal.element;

import java.util.*;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Tab {

    String id;

    String title;

    String link;

    String displaytype;

    int sequence;

    boolean locked;

    Vector<Module> modules = new Vector<Module>();

    public Tab() {
    }

    public Tab(String command, String title) {
        this.title = title;
        this.id = command;
        this.displaytype = "left_navigation";
    }

    public Tab(String command, String title, String displaytype) {
        this.title = title;
        this.id = command;
        this.displaytype = displaytype;
    }

    public String getDisplaytype() {
        return displaytype;
    }

    public void setDisplaytype(String displaytype) {
        this.displaytype = displaytype;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setModules(Vector<Module> modules) {
        this.modules = modules;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getCommand() {
        return id;
    }

    public String getId() {
        return id;
    }

    public String getDisplayType() {
        return displaytype;
    }

    public void addModule(Module module) {
        modules.addElement(module);
    }

    public Vector<Module> getModules() {
        return modules;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public boolean equals(Object o) {
        Tab tab = (Tab) o;
        if (tab.getId().equals(id)) return true;
        return false;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
