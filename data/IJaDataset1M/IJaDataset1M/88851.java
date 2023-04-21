package com.jme3.gde.codepalette.scene;

import com.jme3.gde.codepalette.JmePaletteUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.openide.text.ActiveEditorDrop;

/**
 *
 * @author normenhansen, zathras
 */
public class JmePaletteShowNormals implements ActiveEditorDrop {

    public JmePaletteShowNormals() {
    }

    private String createBody() {
        String body = "    Material mat = new Material( assetManager, \"Common/MatDefs/Misc/ShowNormals.j3md\");\n    geometry.setMaterial(mat);\n";
        return body;
    }

    public boolean handleTransfer(JTextComponent targetComponent) {
        String body = createBody();
        try {
            JmePaletteUtilities.insert(body, targetComponent);
        } catch (BadLocationException ble) {
            return false;
        }
        return true;
    }
}
