package com.tensegrity.palobrowser.editors.databaseeditor;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import com.tensegrity.palobrowser.PalobrowserPlugin;

/**
 * <code>CubeWrapperLabelProvider</code>
 * 
 * @author Stepan Rutz
 * @version $ID$
 */
public class CubeWrapperLabelProvider extends LabelProvider implements ITableLabelProvider {

    public CubeWrapperLabelProvider() {
    }

    public String getColumnText(Object obj, int index) {
        if (!(obj instanceof CubeWrapper)) return obj.toString();
        CubeWrapper cube = (CubeWrapper) obj;
        String name = cube.getName();
        if (cube instanceof FutureCubeWrapper) name += " *";
        return name;
    }

    public Image getColumnImage(Object obj, int index) {
        if (!(obj instanceof CubeWrapper)) return null;
        return PalobrowserPlugin.getDefault().getImageRegistry().get("icons/cube_on.gif");
    }

    public Image getImage(Object obj) {
        if (!(obj instanceof CubeWrapper)) return null;
        return PalobrowserPlugin.getDefault().getImageRegistry().get("icons/cube_on.gif");
    }
}
