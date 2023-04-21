package jtk.project4.fleet.field;

import jtk.project4.fleet.domain.Po;
import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.control.GroupControl;

public class PurchaseOrderPartUpLeftField extends GroupControl<Po> {

    public PurchaseOrderPartUpLeftField(String bindID) {
        setBindID(bindID);
        createLayout();
    }

    private void createLayout() {
        setLayout(new FormLayout()).addField(new DataBuyerField("po")).addRow().addField(new PurchasedFromVendorField("po"));
    }
}
