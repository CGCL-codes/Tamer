package com.gargoylesoftware.htmlunit.javascript.host;

/**
 * A JavaScript object representing a table footer.
 * 
 * @author Daniel Gredler
 * @version $Revision: 829 $
 */
public class TableFooter extends RowContainer {

    private static final long serialVersionUID = 3257571723745768497L;

    /**
     * Create an instance.
     */
    public TableFooter() {
    }

    /**
     * Javascript constructor. This must be declared in every JavaScript file because
     * the Rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }
}
