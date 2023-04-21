package com.tensegrity.palobrowser.editors.subseteditor.re;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import com.tensegrity.palobrowser.dbtree.DbTreeLabelProvider;

/**
 * <code></code>
 *
 * @author Stepan Rutz
 * @version $ID$
 */
public class PreviewLabelProvider extends DbTreeLabelProvider implements IColorProvider {

    private PreviewTreeViewer viewer;

    private Color colorInactive;

    PreviewLabelProvider(PreviewTreeViewer viewer) {
        this.viewer = viewer;
        Display display = viewer.getTree().getDisplay();
        if (Platform.getOS().equals("linux")) this.colorInactive = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND); else this.colorInactive = new Color(display, 150, 150, 150);
    }

    public Color getBackground(Object element) {
        if (Platform.getOS().equals("linux")) {
            Display display = viewer.getTree().getDisplay();
            if (!viewer.isMarked(element)) return colorInactive; else return display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
        } else return null;
    }

    public Color getForeground(Object element) {
        return !viewer.isMarked(element) ? colorInactive : null;
    }
}
