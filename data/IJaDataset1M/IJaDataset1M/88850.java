package org.slasoi.slamodel.sla.business;

import org.slasoi.slamodel.core.Annotated;
import org.slasoi.slamodel.primitives.ID;

public class Product extends Annotated {

    private static final long serialVersionUID = 1L;

    private ID _id = null;

    private String _name = null;

    public Product(ID id, String name) {
        setId(id);
        setName(name);
    }

    public void setId(ID id) {
        if (id == null) throw new IllegalArgumentException("No id specified");
        _id = id;
    }

    public ID getId() {
        return _id;
    }

    public void setName(String name) {
        if (name == null) throw new IllegalArgumentException("No name specified");
        _name = name;
    }

    public String getName() {
        return _name;
    }
}
