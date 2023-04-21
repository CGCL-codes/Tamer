package com.touchgraph.graphlayout.interaction;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import com.touchgraph.graphlayout.TGPanel;

/**
 * TGAbstractMouseMotionUI allows one to write user interfaces that handle what happends when a mouse is moved over the screen
 * 
 * @author Alexander Shapiro
 * @version 1.22-jre1.1 $Id: TGAbstractMouseMotionUI.java,v 1.1 2003/05/05 01:25:43 sauerf Exp $
 */
public abstract class TGAbstractMouseMotionUI extends TGUserInterface {

    TGPanel tgPanel;

    private AMMUIMouseMotionListener mml;

    /**
	 * Constructor with TGPanel <tt>tgp</tt>.
	 */
    public TGAbstractMouseMotionUI(TGPanel tgp) {
        tgPanel = tgp;
        mml = new AMMUIMouseMotionListener();
    }

    @Override
    public final void activate() {
        tgPanel.addMouseMotionListener(mml);
    }

    @Override
    public final void deactivate() {
        tgPanel.removeMouseMotionListener(mml);
        super.deactivate();
    }

    public abstract void mouseMoved(MouseEvent e);

    public abstract void mouseDragged(MouseEvent e);

    private class AMMUIMouseMotionListener extends MouseMotionAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            TGAbstractMouseMotionUI.this.mouseMoved(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            TGAbstractMouseMotionUI.this.mouseMoved(e);
        }
    }
}
