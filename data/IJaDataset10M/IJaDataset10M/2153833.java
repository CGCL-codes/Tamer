package com.ixora.rms.ui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import com.ixora.common.MessageRepository;
import com.ixora.common.ui.UIConfiguration;
import com.ixora.common.ui.UIExceptionMgr;
import com.ixora.common.ui.UIUtils;
import com.ixora.rms.ui.RMSViewContainer;
import com.ixora.rms.ui.dataviewboard.legend.LegendDialog;
import com.ixora.rms.ui.messages.Msg;

/**
 * Shows the legend window.
 */
public final class ActionShowLegendWindow extends AbstractAction {

    private static final long serialVersionUID = 2010948856907948925L;

    /** View container */
    private RMSViewContainer viewContainer;

    /**
     * Constructor.
     * @param vc
     */
    public ActionShowLegendWindow(RMSViewContainer vc) {
        super();
        this.viewContainer = vc;
        UIUtils.setUsabilityDtls(MessageRepository.get(Msg.ACTIONS_SHOWLEGEND), this);
        ImageIcon icon = UIConfiguration.getIcon("legend.gif");
        if (icon != null) {
            putValue(Action.SMALL_ICON, icon);
        }
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent ev) {
        try {
            LegendDialog.showLegendDialog(viewContainer.getAppFrame());
        } catch (Exception e) {
            UIExceptionMgr.userException(e);
        }
    }
}
