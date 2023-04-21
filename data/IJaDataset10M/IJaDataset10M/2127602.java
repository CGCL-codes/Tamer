package org.cubictest.model;

import static org.cubictest.model.IdentifierType.ID;
import static org.cubictest.model.IdentifierType.LABEL;
import java.util.ArrayList;
import java.util.List;

public class Image extends PageElement {

    @Override
    public String getType() {
        return "Image";
    }

    @Override
    public List<IdentifierType> getIdentifierTypes() {
        List<IdentifierType> list = new ArrayList<IdentifierType>();
        list.add(ID);
        list.add(LABEL);
        return list;
    }
}
