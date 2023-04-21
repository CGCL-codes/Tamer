package org.geoforge.guitlc.frame.secrun.menu;

import java.awt.event.ActionListener;
import org.geoforge.guillc.menu.MenIcnToolsAbs;
import org.geoforge.guitlc.frame.secrun.menuitem.MimRenameWindow;
import org.geoforge.guillc.menuitem.MimAbs;
import org.geoforge.guitlc.frame.secrun.menuitem.MimSynchroDisplayViewer;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 *
 * should contains "rename this window"
 */
public abstract class MenToolsWinAbs extends MenIcnToolsAbs {

    private MimAbs _mimRenameWindow = null;

    protected MimAbs _mimSynchronize = null;

    protected MenToolsWinAbs(ActionListener actListenerController) {
        super();
        this._mimRenameWindow = new MimRenameWindow(actListenerController);
        this._mimSynchronize = new MimSynchroDisplayViewer(actListenerController);
        super.add(this._mimRenameWindow);
        super.addSeparator();
        super.add(this._mimSynchronize);
    }

    public void removeActionListenerRun(ActionListener actListenerController) {
        this._mimRenameWindow.removeActionListener(actListenerController);
        this._mimSynchronize.removeActionListener(actListenerController);
    }

    public void addActionListenerRun(ActionListener actListenerController) {
        this._mimRenameWindow.addActionListener(actListenerController);
        this._mimSynchronize.addActionListener(actListenerController);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (this._mimRenameWindow != null) {
            this._mimRenameWindow.destroy();
            this._mimRenameWindow = null;
        }
        if (this._mimSynchronize != null) {
            this._mimSynchronize.destroy();
            this._mimSynchronize = null;
        }
    }
}
