package mymogo.models.combobox;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import mymogo.handler.MymogoIcons;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author wusel
 */
public class IconComboBoxModel extends DefaultComboBoxModel {

    private static final Log log = LogFactory.getLog(IconComboBoxModel.class);

    private Vector<String> icons = new Vector<String>();

    public IconComboBoxModel() {
        for (String string : MymogoIcons.getIcons()) {
            icons.add(string);
        }
    }

    @Override
    public int getSize() {
        return icons.size();
    }

    @Override
    public Object getElementAt(int index) {
        return icons.get(index);
    }
}
