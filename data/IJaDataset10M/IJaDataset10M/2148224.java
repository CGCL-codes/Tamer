package net.sourceforge.thinfeeder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import net.sourceforge.thinfeeder.model.dao.DAOSystem;
import thinlet.Thinlet;

/**
 * @author fabianofranz@users.sourceforge.net
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ThinFeederLauncher extends Frame implements WindowListener {

    private static final long serialVersionUID = 1L;

    private transient ThinFeeder content;

    private transient Image doublebuffer;

    private int width = 700;

    private int height = 550;

    public ThinFeederLauncher(String title, Thinlet content) throws Exception {
        super(title);
        this.content = (ThinFeeder) content;
        add(content, BorderLayout.CENTER);
        addWindowListener(this);
        pack();
        if (DAOSystem.getSystem().getBounds() == null) {
            Insets is = getInsets();
            width += is.left + is.right;
            height += is.top + is.bottom;
            Dimension ss = getToolkit().getScreenSize();
            width = Math.min(width, ss.width);
            height = Math.min(height, ss.height);
            setBounds((ss.width - width) / 2, (ss.height - height) / 2, width, height);
        } else {
            setBounds(DAOSystem.getSystem().getBounds());
        }
        this.setIconImage(content.getIcon("/icons/icon.png"));
        setVisible(true);
    }

    /**
	 * Call the paint method to redraw this component without painting a
	 * background rectangle
	 */
    public void update(Graphics g) {
        paint(g);
    }

    /**
	 * Create a double buffer if needed,
	 * the <i>thinlet</i> component paints the content
	 */
    public void paint(Graphics g) {
        if (doublebuffer == null) {
            Dimension d = getSize();
            doublebuffer = createImage(d.width, d.height);
        }
        Graphics dg = doublebuffer.getGraphics();
        dg.setClip(g.getClipBounds());
        super.paint(dg);
        dg.dispose();
        g.drawImage(doublebuffer, 0, 0, this);
    }

    /**
	 * Clear the double buffer image (because the frame has been resized),
	 * the overriden method lays out its components
	 * (centers the <i>thinlet</i> component)
	 */
    public void doLayout() {
        if (doublebuffer != null) {
            doublebuffer.flush();
            doublebuffer = null;
        }
        super.doLayout();
    }

    public void windowClosing(WindowEvent e) {
        content.destroy();
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
        setVisible(false);
        content.setDisposed(true);
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
