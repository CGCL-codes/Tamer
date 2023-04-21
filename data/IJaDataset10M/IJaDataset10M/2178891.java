package org.tzi.use.gui.main;

import java.awt.print.PageFormat;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import org.tzi.use.config.Options;
import org.tzi.use.gui.views.PrintableView;
import org.tzi.use.gui.views.View;

/** 
 * An internal frame holding a view of a system state.
 * 
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public class ViewFrame extends JInternalFrame {

    private View fView;

    ViewFrame(String title, View view, String iconFilename) {
        super(title, true, true, true, true);
        fView = view;
        setFrameIcon(new ImageIcon(Options.iconDir + iconFilename));
    }

    void close() {
        fView.detachModel();
    }

    boolean isPrintable() {
        return fView instanceof PrintableView;
    }

    void print(PageFormat pf) {
        if (fView instanceof PrintableView) ((PrintableView) fView).printView(pf);
    }

    /**
     * Returns the view of this ViewFrame.
     */
    View getView() {
        return fView;
    }
}
