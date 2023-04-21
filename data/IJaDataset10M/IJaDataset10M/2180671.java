package org.xmlcml.cif;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Logger;
import nu.xom.Element;
import nu.xom.Elements;

/**
 * A row in a CIF Loop.
 * 
 * can access rows and columns of the loop. may also wrap each table element in
 * a cell tag
 */
public class CIFRow extends CIFElement {

    /**
	 * row tag.
	 * 
	 */
    public static final String TAG = "row";

    static Logger logger = Logger.getLogger(CIFRow.class.getName());

    /**
	 * create empty row.
	 * 
	 */
    public CIFRow() {
        super(TAG);
    }

    /** copy constructor.
	 * 
	 * @param cifLoop
	 */
    public CIFRow(CIFRow cifRow) {
        super(cifRow);
    }

    /**
	 * create from values.
	 * 
	 * @param values
	 * @throws CIFException
	 */
    public CIFRow(List<String> values) throws CIFException {
        this();
        for (String value : values) {
            this.appendChild(new CIFTableCell(value));
        }
    }

    /**
	 * create recursively from an XML element.
	 * 
	 * @param element
	 *            with name "row"
	 * @param failOnError
	 *            fail if violates schema
	 * @throws CIFException
	 *             if this and descendants violate schema.
	 */
    public CIFRow(Element element, boolean failOnError) throws CIFException {
        this();
        validateName("row");
        copyAttributes(element, new String[] {}, failOnError);
        copyChildren(element, new String[] { "cell" }, failOnError);
        if (element.getChildElements("cell").size() == 0) {
            throw new CIFException("row must have cell children");
        }
    }

    /**
	 * writes row to an HTML table.
	 * 
	 * @param w
	 *            writer
	 * @throws IOException
	 */
    public void writeHTML(Writer w) throws IOException {
        Elements cells = this.getChildElements(CIFTableCell.TAG);
        w.write("<tr>\n");
        for (int i = 0; i < cells.size(); i++) {
            ((CIFTableCell) cells.get(i)).writeHTML(w);
        }
        w.write("</tr>\n");
    }
}
