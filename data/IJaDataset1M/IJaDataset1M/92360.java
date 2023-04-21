package org.jdmp.sigmen.client.menu;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.jdmp.gui.ImageNotReadableException;
import org.jdmp.gui.JImage;
import org.jdmp.sigmen.client.Main;

public abstract class JImagePoper extends JImage implements Poper {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5672301659618215860L;

    private JInternalFrame pop;

    @Override
    public abstract JInternalFrame createInternalFrame();

    public JImagePoper() {
        super();
        init();
    }

    public JImagePoper(Image img) {
        super(img);
        init();
    }

    public JImagePoper(String img) {
        super();
        try {
            setImage(img, true);
        } catch (ImageNotReadableException e) {
            Main.erreur(e);
        }
        init();
    }

    public JImagePoper(String img, int width, int height) {
        super(width, height);
        try {
            setImage(img, false);
        } catch (ImageNotReadableException e) {
            Main.erreur(e);
        }
        init();
    }

    public JImagePoper(int width, int height) {
        super(width, height);
        init();
    }

    public JImagePoper(Image img, int width, int height) {
        super(img, width, height);
        init();
    }

    private void init() {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new ThisListener());
    }

    public JInternalFrame getInternalFrame() {
        if (pop != null) {
            return pop;
        } else {
            pop = createInternalFrame();
            pop.addInternalFrameListener(new PopListener());
            return pop;
        }
    }

    public boolean existInternalFrame() {
        return pop != null;
    }

    public void eraseInternalFrame() {
        pop.dispose();
        pop = null;
    }

    private class ThisListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (getInternalFrame().isVisible()) {
                eraseInternalFrame();
            } else {
                getInternalFrame().setVisible(true);
                Main.fenetre().addPop(getInternalFrame());
                getInternalFrame().toFront();
                try {
                    getInternalFrame().setSelected(true);
                } catch (PropertyVetoException e1) {
                    Main.erreur("Impossible de sélectionner la fenètre.", e1);
                }
            }
        }
    }

    private class PopListener implements InternalFrameListener {

        @Override
        public void internalFrameActivated(InternalFrameEvent e) {
        }

        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
        }

        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            eraseInternalFrame();
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
        }

        @Override
        public void internalFrameDeiconified(InternalFrameEvent e) {
        }

        @Override
        public void internalFrameIconified(InternalFrameEvent e) {
        }

        @Override
        public void internalFrameOpened(InternalFrameEvent e) {
        }
    }
}
