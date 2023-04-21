package com.danikenan.p4jb.gui.wizard;

import java.awt.BorderLayout;
import java.awt.Color;
import com.borland.primetime.util.VetoException;
import com.borland.primetime.wizard.*;
import com.danikenan.p4jb.gui.DepotTreePanel;
import com.danikenan.p4jb.gui.P4ParamsPanel;
import com.danikenan.p4jb.svr.cmd.P4GlobalParams;
import com.danikenan.p4jb.util.Util;

/**
 * @author   dani kenan
 * @todo     cleanup the mess here with commands, models and properties
 */
public class CheckoutWizard extends BasicWizard {

    /** Description of the Field */
    private P4GlobalParams _p4ParamsModel = new P4GlobalParams();

    /** Description of the Field */
    private DepotTreePanel _treePanel = new DepotTreePanel(_p4ParamsModel);

    /** Description of the Field */
    private P4ParamsPanel _p4paramsPanel = new P4ParamsPanel(_p4ParamsModel);

    /** Description of the Field */
    private boolean _isFirstTimeTreeShows = true;

    /** Constructor for the CheckoutWizard object */
    public CheckoutWizard() {
    }

    /**
	 * Description of the Method
	 *
	 * @param host  Description of Parameter
	 * @return      Description of the Returned Value
	 */
    public WizardPage invokeWizard(WizardHost host) {
        setWizardTitle("Checkout project from Perforce");
        P4ParamsWizPage page1 = new P4ParamsWizPage();
        DepotFolderSelectionWizardPage page2 = new DepotFolderSelectionWizardPage();
        page2.setLayout(new BorderLayout());
        page2.add(_treePanel, BorderLayout.CENTER);
        page1.add(_p4paramsPanel);
        addWizardPage(page1);
        addWizardPage(page2);
        return super.invokeWizard(host);
    }

    /**
	 * Description of the Method
	 *
	 * @param currPage           Description of Parameter
	 * @param host               Description of Parameter
	 * @return                   Description of the Returned Value
	 * @exception VetoException  Description of Exception
	 */
    public WizardPage next(WizardPage currPage, WizardHost host) throws VetoException {
        if (currPage instanceof P4ParamsWizPage) {
            Util.info("WizardPage next on P4GlobalParamsWizardPage");
            if (_p4paramsPanel.isChanged() || _isFirstTimeTreeShows) {
                _p4paramsPanel.writeFieldsToModel();
                _isFirstTimeTreeShows = false;
            }
        }
        return super.next(currPage, host);
    }

    /**
	 * Description of the Method
	 *
	 * @exception com.borland.primetime.util.VetoException  Description of
	 *      Exception
	 */
    protected void finish() throws com.borland.primetime.util.VetoException {
        super.finish();
    }
}
