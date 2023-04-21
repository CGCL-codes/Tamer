package org.geoforge.guitlc.frame.secrun.menu;

import java.awt.event.ActionListener;
import org.geoforge.guillc.menuitem.MimAbs;
import org.geoforge.guitlc.frame.secrun.menuitem.MimIcnCloneViewer;

/**
 *
 * 
 */
public abstract class MenWindowWinViewAbs extends MenWindowWinAbs {

    private MimAbs _mimCloneViewer = null;

    public void removeActionListenerRun(ActionListener actListenerController) {
        this._mimCloneViewer.removeActionListener(actListenerController);
    }

    public void addActionListenerRun(ActionListener actListenerController) {
        this._mimCloneViewer.addActionListener(actListenerController);
    }

    protected MenWindowWinViewAbs(ActionListener alr) {
        super();
        this._mimCloneViewer = new MimIcnCloneViewer(alr);
        super.setVisible(false);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!this._mimCloneViewer.init()) return false;
        super.add(this._mimCloneViewer);
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
