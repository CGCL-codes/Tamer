package tafat.metamodel.entity;

import java.text.ParseException;

public abstract class PowerProducer extends PowerEquipment {

    public void loadAttribute(String name, String value) throws ParseException {
        super.loadAttribute(name, value);
    }

    public void setDefaultValues() throws ParseException {
        super.setDefaultValues();
    }
}
