package dovetaildb.dbservice;

public class UnencodableValueException extends Exception {

    private static final long serialVersionUID = -3030929241673585846L;

    public UnencodableValueException(String reason) {
        super(reason);
    }
}
