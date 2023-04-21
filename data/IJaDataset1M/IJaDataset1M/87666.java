package net.sf.RecordEditor;

import net.sf.RecordEditor.utils.Run;

/**
 * run the Full Editor
 *
 * @author Bruce Martin
 *
 */
public final class RunCobolEditor {

    private static final String[] JARS = { "<lib>/JRecord.jar", "<lib>/RecordEdit.jar", "<lib>/properties.zip" };

    /**
     * run the full record editor
     * @param args program arguments
     */
    public static void main(String[] args) {
        new Run(Run.SYSTEM_JARS_FILENAME, JARS, "net.sf.RecordEditor.editFileLayout.Edit", args);
    }
}
