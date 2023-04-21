package com.jme3.gde.codepalette.scene;

import com.jme3.gde.codepalette.JmePaletteUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.openide.text.ActiveEditorDrop;

/**
 *
 * @author normenhansen, zathras
 */
public class JmePaletteJ3OLoad implements ActiveEditorDrop {

    public JmePaletteJ3OLoad() {
    }

    private String createBody() {
        String body = " /** Load a Node from a .j3o file */\n String userHome = System.getProperty(\"user.home\");\n BinaryImporter importer = BinaryImporter.getInstance();\n importer.setAssetManager(assetManager);\n File file = new File(userHome+\"/somefile.j3o\");\n try {\n   Node loadedNode = (Node)importer.load(file);\n   loadedNode.setName(\"loaded node\");\n   rootNode.attachChild(loadedNode);\n } catch (IOException ex) {\n   Logger.getLogger(Main.class.getName()).log(Level.SEVERE, \"No saved node loaded.\", ex);\n } \n";
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
