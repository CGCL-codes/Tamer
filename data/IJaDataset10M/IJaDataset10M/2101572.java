package tafat.metamodel.entity;

import java.text.ParseException;

public abstract class Factory extends Location {

    public void loadAttribute(String name, String value) throws ParseException {
        super.loadAttribute(name, value);
    }

    public void setDefaultValues() throws ParseException {
        super.setDefaultValues();
    }
}
