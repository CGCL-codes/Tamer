package uqdsd.infosec.model;

import java.util.EventObject;

@SuppressWarnings("serial")
public class NameChangeEvent extends EventObject {

    private String oldName, newName;

    public NameChangeEvent(Object source, String oldName, String newName) {
        super(source);
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getNewName() {
        return newName;
    }

    public String getOldName() {
        return oldName;
    }
}
