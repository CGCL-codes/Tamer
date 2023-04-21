package com.fddtool.si.export;

import com.fddtool.util.Utils;

/**
 * This class represents a an informational note that may be generated when
 * importing a project from XML file. The purpose is to show the notes to the
 * user once the import is over.
 * 
 * @author SKhramtc
 */
public class ImportNote {

    /**
     * The text message of the string.
     */
    private String message;

    /**
     * The severity of the note. (e.g. info, warning, error)
     */
    private ImportNoteSeverity severity;

    /**
     * Creates a new immutable import note.
     * 
     * @param message
     *            the String with text message for this note.
     * @param severity
     *            the ImportNoteSeverity instnce that deifines the severity of
     *            the note.
     */
    public ImportNote(String message, ImportNoteSeverity severity) {
        Utils.assertNotNullOrEmptyParam(message, "message");
        Utils.assertNotNullParam(severity, "severity");
        this.message = message;
        this.severity = severity;
    }

    /**
     * Returns the human-readable message of this note.
     * 
     * @return non-empty String with message. 
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the severity of the note, e.g. Info, Warning, Error
     * 
     * @return non-null ImportNoteSeverity object that defines he severity.
     */
    public ImportNoteSeverity getSeverity() {
        return severity;
    }

    public String toString() {
        return severity.getLabel() + ": " + getMessage();
    }
}
