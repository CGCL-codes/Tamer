package dash.context.manager;

public class ContextKey {

    public final Class declClass;

    public final String fieldName;

    public ContextKey(Class declClass, String fieldName) {
        if (declClass == null || fieldName == null) throw new NullPointerException();
        this.declClass = declClass;
        this.fieldName = fieldName;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ContextKey) {
            ContextKey k = (ContextKey) other;
            return this.declClass.equals(k.declClass) && this.fieldName.equals(k.fieldName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return declClass.hashCode() / 2 + fieldName.hashCode() / 2;
    }

    @Override
    public String toString() {
        return "ContextKey[" + declClass.getName() + ", " + fieldName + "]";
    }
}
