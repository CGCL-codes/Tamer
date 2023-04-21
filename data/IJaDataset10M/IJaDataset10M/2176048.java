package org.gjt.sp.jedit.actions;

import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.io.File;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;

public class record_macro extends EditAction {

    public void actionPerformed(ActionEvent evt) {
        View view = getView(evt);
        String settings = jEdit.getSettingsDirectory();
        if (settings == null) {
            GUIUtilities.error(view, "no-settings", new String[0]);
            return;
        }
        if (view.getInputHandler().getMacroRecorder() != null) {
            GUIUtilities.error(view, "already-recording", new String[0]);
            return;
        }
        String name = GUIUtilities.input(view, "record", null);
        if (name == null) return;
        name = name.replace(' ', '_');
        Buffer buffer = jEdit.openFile(null, null, MiscUtilities.constructPath(settings, "macros", name + ".macro"), false, true);
        if (buffer == null) return;
        try {
            buffer.remove(0, buffer.getLength());
            buffer.insertString(0, jEdit.getProperty("macro.header"), null);
        } catch (BadLocationException bl) {
            Log.log(Log.ERROR, this, bl);
        }
        Macros.beginRecording(view, name, buffer);
        view.showStatus(null);
    }

    public boolean isRecordable() {
        return false;
    }
}
