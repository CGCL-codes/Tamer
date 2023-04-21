package blue.udo;

public class UDOBuffer {

    private static UDOBuffer instance = null;

    Object bufferObject = null;

    private UDOBuffer() {
    }

    public static UDOBuffer getInstance() {
        if (instance == null) {
            instance = new UDOBuffer();
        }
        return instance;
    }

    public void setBufferedObject(Object object) {
        bufferObject = object;
    }

    public Object getBufferedObject() {
        return bufferObject;
    }
}
