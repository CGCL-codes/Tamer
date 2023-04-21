package org.berlin.p1.dslpdf1;

import java.util.ArrayList;
import java.util.List;

public class PdfClientOrder {

    public enum BuySell {

        BUY, SELL
    }

    private String accountNo;

    private List<PdfLineItem> lineItems = new ArrayList<PdfLineItem>();

    public PdfClientOrder(final List<PdfLineItem> lineItems, final String acct) {
        this.lineItems = lineItems;
        this.accountNo = acct;
    }

    public String toString() {
        return this.toStringBasic();
    }

    public String toStringBasic() {
        return "@ClientOrder - " + this.accountNo + " line-items : { " + ((lineItems.size() > 0) ? lineItems.get(0) : "empty") + "}";
    }

    public List<PdfLineItem> getLineItems() {
        return lineItems;
    }

    public String getAccountNo() {
        return accountNo;
    }
}
