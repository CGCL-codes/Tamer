package tafat.metamodel.entity;

import tafat.engine.ModelObject;
import tafat.engine.Entity;
import java.text.ParseException;

public abstract class Outdoor extends ModelObject implements Entity {

    public void loadAttribute(String name, String value) throws ParseException {
        super.loadAttribute(name, value);
    }

    public void setDefaultValues() throws ParseException {
        super.setDefaultValues();
    }
}
