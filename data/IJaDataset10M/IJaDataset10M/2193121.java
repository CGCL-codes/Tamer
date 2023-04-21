package org.jabusuite.webclient.address.contact;

import nextapp.echo2.app.Column;
import org.jabusuite.webclient.controls.JbsExtent;
import org.jabusuite.webclient.controls.container.JbsGrid;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import org.jabusuite.core.users.JbsUser;
import org.jabusuite.webclient.controls.JbsLabel;
import org.jabusuite.webclient.main.JbsL10N;
import org.jabusuite.webclient.windows.JbsDialogWindowOKCancel;

public class FmSelectContactType extends JbsDialogWindowOKCancel {

    private static final long serialVersionUID = 6793758396033561045L;

    protected SelContactType cbbContactType;

    public FmSelectContactType(String msg) {
        super(JbsL10N.getString("Contact.selectContactType"), new JbsExtent(350), new JbsExtent(200));
        this.initPanel(msg);
        this.cbbContactType.setSelectedIndex(0);
    }

    public FmSelectContactType(String msg, SelContactType.ContactType contactType) {
        super(JbsL10N.getString("Contact.selectContactType"), new JbsExtent(350), new JbsExtent(200));
        this.initPanel(msg);
        this.setSelectedContactType(contactType);
    }

    protected void createComponents() {
        cbbContactType = new SelContactType();
    }

    protected void initPanel(String msg) {
        this.createComponents();
        this.setModal(true);
        Column colMain = new Column();
        colMain.setInsets(new Insets(5, 5));
        colMain.add(new JbsLabel(msg));
        JbsGrid grdMain = new JbsGrid(2);
        grdMain.setInsets(new Insets(5, 10));
        grdMain.setColumnWidth(0, new JbsExtent(150));
        grdMain.add(new Label(JbsL10N.getString("Contact.selectContactType")));
        grdMain.add(cbbContactType);
        colMain.add(grdMain);
        this.getPnMain().add(colMain);
    }

    /**
     * Selects a specified <code>ContactType</code>
     * @param contactType 
     */
    public void setSelectedContactType(SelContactType.ContactType contactType) {
        this.cbbContactType.setSelectedContactType(contactType);
    }

    /**
     * Returns the selected <code>ContactType</code>
     * @return The selected <code>ContactType</code> or null if nothing is selected.
     */
    public SelContactType.ContactType getSelectedContactType() {
        return this.cbbContactType.getSelectedContactType();
    }

    @Override
    protected boolean cancelOK() {
        return true;
    }

    @Override
    protected boolean postOK() {
        return true;
    }
}
