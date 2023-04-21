package com.aragost.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author  Jan Sorensen
 * @version
 */
public class FormElement extends CompositeHtmlElement {

    public FormElement(String action) {
        this(action, new ArrayList());
    }

    /** Creates new FormElement */
    public FormElement(String action, Collection hiddenParameters) {
        if (action != null) {
            setAttribute("action", action);
        }
        this.hiddenParameters = hiddenParameters;
    }

    /** Creates new FormElement */
    public FormElement(Collection hiddenParameters) {
        this(null, hiddenParameters);
    }

    public void postBodyWrite(HtmlWriter writer) {
        for (Iterator i = hiddenParameters.iterator(); i.hasNext(); ) {
            String[] a = (String[]) i.next();
            (new Hidden(a[0], a[1])).writeTo(writer);
        }
    }

    public String getTag() {
        return "form";
    }

    /** Check if the receiver is cell like. If this form is wrapping a cell, 
   * it is cell like.
   */
    public boolean isCellLike() {
        List elements = getElements();
        if (elements.size() != 1) return false;
        return elements.get(0) instanceof Cell;
    }

    public Collection getHiddenParameters() {
        return hiddenParameters;
    }

    private Collection hiddenParameters = new ArrayList();
}
