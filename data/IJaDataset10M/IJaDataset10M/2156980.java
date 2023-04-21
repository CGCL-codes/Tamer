package mimosa.scheduler;

/**
 * This exception is raised when a given object failed to be converted to a simple type.
 *
 * @author Jean-Pierre Muller
 */
@SuppressWarnings("serial")
public class ConversionException extends EntityException {

    public static final int SHORT = 0;

    public static final int INT = 1;

    public static final int LONG = 2;

    public static final int FLOAT = 3;

    public static final int DOUBLE = 4;

    public static final int BOOLEAN = 5;

    private int type;

    private Object value;

    public ConversionException(Entity entity, int type, Object value) {
        super(entity);
        this.type = type;
        this.value = value;
    }

    /**
	 * @return Returns the type.
	 */
    public int getType() {
        return type;
    }

    /**
	 * @return Returns the value.
	 */
    public Object getValue() {
        return value;
    }
}
