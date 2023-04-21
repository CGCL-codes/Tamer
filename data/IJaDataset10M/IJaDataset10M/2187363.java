package org.xmlcml.cml.html;

import org.apache.log4j.Logger;

/** base class for lightweight generic SVG element.
 * no checking - i.e. can take any name or attributes
 * @author pm286
 *
 */
public class HtmlFrameset extends HtmlElement {

    private static final Logger LOG = Logger.getLogger(HtmlFrameset.class);

    public static final String TAG = "frameset";

    /** constructor.
	 * 
	 */
    public HtmlFrameset() {
        super(TAG);
    }

    public void setCols(String cols) {
        this.setAttribute("cols", cols);
    }

    public void setRows(String rows) {
        this.setAttribute("rows", rows);
    }
}
