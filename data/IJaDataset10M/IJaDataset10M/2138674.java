package de.huxhorn.lilith.services.clipboard;

import de.huxhorn.sulky.groovy.GroovyInstance;
import java.io.File;

public class GroovyFormatter implements ClipboardFormatter {

    private GroovyInstance groovyInstance;

    public GroovyFormatter() {
        groovyInstance = new GroovyInstance();
    }

    public GroovyFormatter(String fileName) {
        this();
        setGroovyFileName(fileName);
    }

    public void setGroovyFileName(String fileName) {
        groovyInstance.setGroovyFileName(fileName);
    }

    private ClipboardFormatter getFormatter() {
        return groovyInstance.getInstanceAs(ClipboardFormatter.class);
    }

    public String getName() {
        ClipboardFormatter formatter = getFormatter();
        if (formatter != null) {
            return formatter.getName();
        }
        String fileName = groovyInstance.getGroovyFileName();
        if (fileName != null) {
            File file = new File(fileName);
            return file.getName();
        }
        return "Missing file!";
    }

    public String getDescription() {
        ClipboardFormatter formatter = getFormatter();
        if (formatter != null) {
            return formatter.getDescription();
        }
        String fileName = groovyInstance.getGroovyFileName();
        String shortName = "Missing file!";
        if (fileName != null) {
            File file = new File(fileName);
            shortName = file.getName();
        }
        String errorMessage = groovyInstance.getErrorMessage();
        if (errorMessage == null) {
            Class instanceClass = groovyInstance.getInstanceClass();
            if (instanceClass != null) {
                return shortName + " - Expected ClipboardFormatter but received " + instanceClass.getName() + "!";
            }
        }
        return shortName + " - " + errorMessage;
    }

    public String getAccelerator() {
        ClipboardFormatter formatter = getFormatter();
        return formatter == null ? null : formatter.getAccelerator();
    }

    public boolean isCompatible(Object object) {
        ClipboardFormatter formatter = getFormatter();
        return formatter != null && formatter.isCompatible(object);
    }

    public String toString(Object object) {
        ClipboardFormatter formatter = getFormatter();
        if (formatter == null) {
            return null;
        }
        return formatter.toString(object);
    }
}
