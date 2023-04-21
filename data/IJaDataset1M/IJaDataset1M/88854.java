package org.columba.addressbook.facade;

public interface IHeaderItem {

    public String getId();

    public String getName();

    public void setId(String id);

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public boolean isContact();
}
