package ru.spb.leonidv.lvcontrols.fileedit;

import java.util.EventObject;

public class FileSelectedEvent extends EventObject {

    /**
     * 
     */
    private static final long serialVersionUID = 2037323822053979856L;

    private String fileName;

    public FileSelectedEvent(Object source) {
        super(source);
        if (!(source instanceof FileEdit)) {
            throw new IllegalArgumentException("Only FileEdit objects available as source");
        }
        fileName = getSource().getFileName();
    }

    public FileEdit getSource() {
        return (FileEdit) source;
    }

    public String getSelectedFileName() {
        return fileName;
    }
}
