package org.columba.mail.gui.composer.util;

import java.awt.Font;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.columba.core.desktop.ColumbaDesktop;
import org.columba.core.gui.util.FontProperties;
import org.columba.core.util.TempFileStore;
import org.columba.mail.gui.composer.AbstractEditorController;
import org.columba.mail.util.MailResourceLoader;

public class ExternalEditor {

    String Cmd;

    public ExternalEditor() {
    }

    public ExternalEditor(String EditorCommand) {
    }

    private File writeToFile(final AbstractEditorController editController) {
        File tmpFile = TempFileStore.createTempFileWithSuffix("txt");
        FileWriter FO;
        try {
            FO = new FileWriter(tmpFile);
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot write to temp file needed " + "for external editor.");
            return null;
        }
        try {
            String M = editController.getViewText();
            if (M != null) FO.write(M);
            FO.close();
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot write to temp file needed " + "for external editor:\n" + ex.getMessage());
            return null;
        }
        return tmpFile;
    }

    private String readFromFile(File tmpFile) {
        FileReader FI;
        try {
            FI = new FileReader(tmpFile);
        } catch (java.io.FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot read from temp file used " + "by external editor.");
            return "";
        }
        char[] buf = new char[1000];
        int i;
        String message = "";
        try {
            while ((i = FI.read(buf)) >= 0) message += new String(buf, 0, i);
            FI.close();
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot read from temp file used " + "by external editor.");
            return "";
        }
        return message;
    }

    public boolean startExternalEditor(final AbstractEditorController editController) throws IOException {
        File tmpFile = writeToFile(editController);
        final Font OldFont = editController.getViewFont();
        Font font = FontProperties.getTextFont();
        font = font.deriveFont(30);
        editController.setViewFont(font);
        editController.setViewText(MailResourceLoader.getString("menu", "composer", "extern_editor_using_msg"));
        ColumbaDesktop.getInstance().openAndWait(tmpFile);
        final String message = readFromFile(tmpFile);
        editController.setViewFont(OldFont);
        editController.setViewText(message);
        return true;
    }
}
