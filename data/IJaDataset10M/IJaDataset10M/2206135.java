package prodata.exceptions;

public class MissingValueExc extends ProDataExc {

    static final long serialVersionUID = 0;

    public MissingValueExc(String i_fieldName, String i_classOfDataObject) {
        super("The field: " + i_fieldName + " of the dataobject: " + i_classOfDataObject + " is not set.");
    }
}
