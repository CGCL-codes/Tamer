package diuf.diva.hephaistk.recognizers.datatypes;

public class ClientData extends AbstractRecognizerDataType {

    private static final long serialVersionUID = -4503140984168573926L;

    public String data;

    public ClientData() {
    }

    public ClientData(String data) {
        this.data = data;
    }

    @Override
    public String getLogMessage() {
        return data;
    }

    public String toString() {
        return (data);
    }

    @Override
    public RecognizerVariableDescriptor getStandardDataObject() {
        return new RecognizerVariableDescriptor("data", null, String.class, this.getClass(), null);
    }

    @Override
    public String getDataTypeName() {
        return "Client Data";
    }
}
