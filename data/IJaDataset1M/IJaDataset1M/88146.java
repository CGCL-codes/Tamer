package org.fudaa.fudaa.sig;

import javax.swing.JComponent;
import javax.swing.JRootPane;

/**
 * @author fred deniger
 * @version $Id: FSigWizardStepInterface.java,v 1.2 2006-09-19 15:10:20 deniger Exp $
 */
public interface FSigWizardStepInterface {

    String getStepText();

    String getStepTitle();

    void setParentRootPane(JRootPane _dialog);

    JComponent getComponent();
}
