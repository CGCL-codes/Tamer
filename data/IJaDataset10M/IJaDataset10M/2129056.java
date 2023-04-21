package org.vikamine.rcp.core.editors.taskeditorpages;

import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;

/**
 * The Class TaskTreeItemSearchspace.
 * 
 * @author Ferdinand Hahmann
 */
public class TaskTreeItemSearchspace extends TaskTreeItemSubgroup {

    /**
     * Instantiates a new TaskTreeItemSearchspace.
     * 
     * @param page
     *            the page
     */
    public TaskTreeItemSearchspace(AttributePages page) {
        super(page);
    }

    @Override
    public int getKey() {
        return 2;
    }

    @Override
    public String getText() {
        return "Searchspace";
    }

    @Override
    public void header(Composite composite) {
    }

    @Override
    public Element getMainElement() throws XMLNotValidException {
        Element elementOperator = super.getEditor().getDoc().getRootElement().getChild("searchSpace");
        if (elementOperator == null) throw new XMLNotValidException();
        return elementOperator;
    }
}
