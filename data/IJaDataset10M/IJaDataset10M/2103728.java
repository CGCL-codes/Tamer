package net.sourceforge.javautil.gui.swing.handler;

import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

/**
 * Applicator for a {@link JComponent}.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class LayoutApplicatorJComponent<C extends JComponent> extends LayoutApplicatorBase<C, LayoutManager> {

    protected JPopupMenu popup;

    public LayoutApplicatorJComponent(C container) {
        super(container);
    }

    /**
	 * @param border The border for the container
	 */
    public void setBorder(Border border) {
        this.container.setBorder(border);
    }

    @Override
    public void addMenu(JMenu menu) {
        if (this.popup == null) {
            this.popup = new JPopupMenu();
            this.container.setComponentPopupMenu(popup);
        }
        this.popup.add(menu);
    }
}
