package ops;

import ops.OPSObject;

public class AckData extends OPSObject {

    public boolean dataAccepted;

    public String message;

    public String id;

    public AckData() {
        dataAccepted = false;
        message = "";
        id = "";
    }

    public Object clone() {
        return null;
    }
}
