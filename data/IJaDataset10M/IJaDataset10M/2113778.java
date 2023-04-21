package graphlab.ui.components.GPropertyEditor.editors;

import graphlab.ui.components.GPropertyEditor.utils.JFontChooser;
import java.awt.Font;
import javax.swing.JComponent;

/**
 * User: root
 */
public class GFontEditor extends GDialogEditor<Font> {

    JFontChooser jFontChooser;

    public JComponent getComponent(Font font) {
        jFontChooser = new JFontChooser();
        jFontChooser.setFont(font);
        return jFontChooser;
    }

    public Font getEditorValue() {
        return jFontChooser.getFont();
    }

    public void setEditorValue(Font font) {
        jFontChooser.setFont(font);
    }
}
