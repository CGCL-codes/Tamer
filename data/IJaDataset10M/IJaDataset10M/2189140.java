package ti.plato.components.ui.oscript.internal.action;

import java.io.File;
import ti.oscript.eclipse.ui.editor.OScriptEditor;
import ti.plato.components.ui.oscript.internal.file.PlatoEditorUtil;
import ti.plato.scripts.ScriptsPlugin;

public class RunScriptUtil {

    public static void runScript() {
        OScriptEditor editor = PlatoEditorUtil.getActiveOScriptEditor();
        if (editor == null) {
            return;
        }
        File f = editor.getEditedFile();
        if (f != null) {
            ScriptsPlugin.runScript(f);
        }
    }
}
