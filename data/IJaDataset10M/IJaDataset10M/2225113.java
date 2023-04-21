package tafat.metamodel.connection;

import tafat.engine.ModelObject;
import tafat.engine.Connection;
import java.text.ParseException;

public abstract class DataConnection extends ModelObject implements Connection {

    public void loadAttribute(String name, String value) throws ParseException {
        super.loadAttribute(name, value);
    }

    public void setDefaultValues() throws ParseException {
        super.setDefaultValues();
    }
}
