package com.ixora.rms.ui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.ixora.common.MessageRepository;
import com.ixora.common.ui.UIExceptionMgr;
import com.ixora.common.ui.UIUtils;
import com.ixora.rms.ui.RMSViewContainer;
import com.ixora.rms.ui.XMLInputDialog;
import com.ixora.rms.ui.messages.Msg;

/**
 * Reusable action that allows data to be viewed as XML.
 */
public abstract class ActionViewXML extends AbstractAction {

    private static final long serialVersionUID = -6151965450430577133L;

    protected RMSViewContainer fContainer;

    /**
	 * Constructor.
	 * @param dbh
	 */
    public ActionViewXML(RMSViewContainer vc) {
        super();
        fContainer = vc;
        UIUtils.setUsabilityDtls(MessageRepository.get(Msg.ACTIONS_VIEW_XML), this);
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent arg0) {
        try {
            XMLInputDialog dlg = new XMLInputDialog(fContainer.getAppFrame(), getXML());
            UIUtils.centerDialogAndShow(fContainer.getAppFrame(), dlg);
        } catch (Exception e) {
            UIExceptionMgr.userException(e);
        }
    }

    /**
	 * @return xml data
	 * @throws Exception
	 */
    protected abstract String getXML() throws Exception;
}
