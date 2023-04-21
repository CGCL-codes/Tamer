package net.sourceforge.jruntimedesigner.editor.example;

import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.UIManager;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

public class SeasonEditor extends ComboBoxPropertyEditor {

    public SeasonEditor() {
        super();
        setAvailableValues(new String[] { "Spring", "Summer", "Fall", "Winter" });
        Icon[] icons = new Icon[4];
        Arrays.fill(icons, UIManager.getIcon("Tree.openIcon"));
        setAvailableIcons(icons);
    }
}
