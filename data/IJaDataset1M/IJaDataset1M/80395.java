package jifx.commons.messages;

public class FieldID {

    private String path;

    private Object element;

    public FieldID(String path, Object element) {
        this.path = path;
        this.element = element;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FieldID) {
            FieldID fi = (FieldID) obj;
            return path.equals(fi.path) && element != null && element.equals(fi.element);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return path.hashCode() + element.hashCode();
    }
}
