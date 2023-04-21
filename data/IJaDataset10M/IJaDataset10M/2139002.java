package uk.co.demon.ursus.cif;

import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;
import nu.xom.Element;

/**
 * A cell in a row in a CIF Loop.
 * 
 */
public class CIFTableCell extends AbstractValueElement {

    /**
	 * cell tag.
	 * 
	 */
    public static final String TAG = "cell";

    static Logger logger = Logger.getLogger(CIFTableCell.class.getName());

    /**
	 * create empty cell.
	 */
    public CIFTableCell() {
        super(TAG);
    }

    /**
	 * create from value.
	 * 
	 * @param value
	 * @throws CIFException
	 */
    public CIFTableCell(String value) throws CIFException {
        this();
        this.setTextValue(value);
    }

    /**
	 * create recursively from an XML element.
	 * 
	 * @param element
	 *            with name "cell"
	 * @param failOnError
	 *            fail if violates schema
	 * @throws CIFException
	 *             if this and descendants violate schema.
	 */
    public CIFTableCell(Element element, boolean failOnError) throws CIFException {
        this();
        validateName("cell");
        copyAttributes(element, new String[] { "id", "value", "dataType", "su" }, failOnError);
        this.setTextValue(element.getValue());
    }

    /**
	 * writes cell to HTML.
	 * 
	 * @param w
	 *            writer
	 * @throws IOException
	 */
    public void writeHTML(Writer w) throws IOException {
        w.write("<td>");
        String v = this.getValue();
        v = CIFUtil.translateCIF2ISO(v);
        v = CIFUtil.translateCIF2HTML(v);
        w.write(v);
        w.write("</td>\n");
    }
}
