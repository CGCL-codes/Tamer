package uk.co.weft.dealersys2;

import uk.co.weft.dbutil.*;
import uk.co.weft.domutil.*;
import uk.co.weft.htform.*;
import uk.co.weft.xhtmlgen.*;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class InvoiceGenerator extends XHTMLDocumentGenerator {

    /**
	 * Creates a new InvoiceGenerator object.
	 */
    public InvoiceGenerator() {
        rootGenerator = new InvoiceBodyGenerator();
    }

    /**
	 * generator for the body element of the HTML document, builds all the
	 * content
	 */
    protected class InvoiceBodyGenerator extends ElementGenerator {

        /**
		 * whether prices on this system are VAT exclusive (and we show VAT)
		 * or VAT inclusive (and we don't)
		 */
        boolean showVAT = true;

        /**
		 * rate of VAT to be computed and added on to the total.  TODO: This
		 * ought <b>not</b> to be compiled in!
		 */
        float vat = (float) 0.175;

        public InvoiceBodyGenerator() {
            super("body", "invoice", "invoice");
            addGenerator(new InvariantGenerator("h1", "Invoice"));
            ElementGenerator eg = new ElementGenerator("div", "invoice", null, null, true);
            addGenerator(eg);
            eg.addGenerator(new ClassGenerator("invoice_number"));
            eg = new ElementGenerator("div", "vat", null, null, true);
            addGenerator(eg);
            eg.addGenerator(new ClassGenerator("vat"));
            addGenerator(new ListGenerator("item", "invoice_item", "invoice", "invoice", "invoice", "items", "show_invoice?invoice=", "name"));
            addGenerator(new BylineGenerator());
        }

        public void init(Context config) throws InitialisationException {
            Float v = config.getValueAsFloat("vat_rate");
            if (v != null) {
                vat = v.floatValue() / 100;
            }
            Boolean b = config.getValueAsBoolean("show_vat");
            if (b != null) {
                showVAT = b.booleanValue();
            }
        }
    }
}
