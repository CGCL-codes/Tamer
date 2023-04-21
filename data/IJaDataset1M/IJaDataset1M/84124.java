package org.geoforge.guitlcolg.frame.secrun.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.geoforge.guitlcolg.frame.secrun.toolbar.*;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class PnlContentsWindowViewerOgl3dTime extends PnlContentsWindowViewerOlgAbs {

    public PnlContentsWindowViewerOgl3dTime(ActionListener alrController) throws Exception {
        super(true);
        super._pnlControls_ = new PnlControlsViewerOgl3dTime();
        super._pnlProps_ = new PnlPropertiesViewerOgl3dTime();
        super._pnlDisplay_ = new PnlDisplayViewerOgl3dTime();
        super._tbrControls_ = new TbrWinViewSubControlOlg3dTimeExp((ActionListener) super._pnlContainerControl_, alrController, (MouseListener) super._pnlContainerControl_.getBorder());
        super._tbrProps_ = new TbrWinViewSubPropsOgl3dTime((ActionListener) super._pnlContainerProps_, (MouseListener) super._pnlContainerProps_.getBorder());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("TODO: PnlContentsWindowViewerOgl3dTime.actionPerformed(e)");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
