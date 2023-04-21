package com.spagettikod.t437.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import com.spagettikod.t437.T437;
import com.spagettikod.t437.component.domain.DomainJList;
import com.xerox.amazonws.sdb.SDBException;
import com.xerox.amazonws.sdb.SimpleDB;

public class CreateDomainAction extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6087812806869264060L;

    private DomainJList domainJList;

    public CreateDomainAction(DomainJList domainJList) {
        this.domainJList = domainJList;
        putValue(Action.NAME, "New Domain...");
    }

    public void actionPerformed(ActionEvent arg0) {
        SimpleDB sdb = T437.getConfigurator().getSimpleDB();
        String name = (String) JOptionPane.showInputDialog(T437.getApplicationFrame(), "Enter the name of the domain you wish to create", "Create domain", JOptionPane.QUESTION_MESSAGE);
        if (name != null) {
            try {
                T437.setBusyCursor(true);
                sdb.createDomain(name);
                domainJList.refresh();
            } catch (SDBException e) {
                T437.displayErrorMessage("Error Creating Domain", e.getLocalizedMessage());
            } finally {
                T437.setBusyCursor(false);
            }
        }
    }
}
