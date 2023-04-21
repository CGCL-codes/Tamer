package org.akrogen.tkui.grammars.xul.ui.listboxes;

import org.akrogen.tkui.grammars.xul.ui.IXULElement;

public interface IListcol extends IXULElement {

    public Integer getSortDirection();

    public void setSortDirection(Integer sortDirection);

    public String getSortProperty();

    public void setSortProperty(String sortProperty);

    public Integer getTextalign();

    public void setTextalign(Integer align);
}
