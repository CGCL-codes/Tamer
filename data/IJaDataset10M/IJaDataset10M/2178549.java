package com.jcorporate.expresso.services.html;

import java.io.PrintWriter;

/**
 *
 *
 * @version        $Revision: 3 $  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 * @author        Michael Nash
 */
public class TextField extends HtmlElement {

    protected int maxLength = 0;

    protected int size = 0;

    protected String fieldName = null;

    protected String fieldValue = ("");

    /**
     * Constructor
     *
     * @throws    HtmlException
     */
    public TextField() throws HtmlException {
        super();
    }

    /**
     * Constructor
     *
     * @param    newFieldName field name
     * @param    newFieldValue field value
     * @param    newMaxLength maximum input length
     * @param    newSize maximum display size
     * @throws    HtmlException
     */
    public TextField(String newFieldName, String newFieldValue, int newMaxLength, int newSize) throws HtmlException {
        super(newFieldName);
        fieldName = newFieldName;
        fieldValue = newFieldValue;
        maxLength = newMaxLength;
        size = newSize;
    }

    /**
     * Display the text field on the page
     *
     * @param    out output stream
     * @param depth the number of tabs to indent
     * @throws    HtmlException
     */
    protected void display(PrintWriter out, int depth) throws HtmlException {
        this.padWithTabs(out, depth);
        out.print("<input");
        if (cSSClass != null) {
            out.print(" class=\"" + cSSClass + "\"");
        }
        if (cSSID != null) {
            out.print(" id=\"" + cSSID + "\"");
        }
        out.println(" type=text name=" + fieldName + " value=\"" + fieldValue + "\" maxlength=" + maxLength + " size=" + size + ">");
        setDisplayed();
    }
}
