package astcentric.structure.basic.property;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Factory of {@link Property} instances.  
 * The following property types are available:
 * <table border="1" cellspacing="0" cellpadding="5">
 * <tr><th><code>PropertyType</code> enum</th><th>Type Code</th><th>Class</th></tr>
 * <tr><td><code>BOOLEAN</code></td><td><code>B</code></td><td>{@link BooleanProperty}</td></tr>
 * <tr><td><code>INTEGER</code></td><td><code>I</code></td><td>{@link IntegerProperty}</td></tr>
 * <tr><td><code>LONG</code></td><td><code>L</code></td></td><td>{@link LongProperty}</td></tr>
 * <tr><td><code>FLOAT</code></td><td><code>F</code></td><td>{@link FloatProperty}</td></tr>
 * <tr><td><code>DOUBLE</code></td><td><code>D</code></td><td>{@link DoubleProperty}</td></tr>
 * <tr><td><code>STRING</code></td><td><code>S</code></td><td>{@link StringProperty}</td></tr>
 * </table>
 * <p>
 * The instances created by this factory are mainly used for deserialization
 * of concrete <code>Property</code> objects from their string representation. 
 */
public final class PropertyFactory {

    private static final Pattern TYPE_PATTERN = Pattern.compile("[a-zA-Z]+");

    private static final Map<String, Class<? extends Property>> TYPE_TO_CLASS_MAP = new LinkedHashMap<String, Class<? extends Property>>();

    private static final Map<Class<? extends Property>, PropertyType> CLASS_TO_TYPE_MAP = new HashMap<Class<? extends Property>, PropertyType>();

    static {
        PropertyType[] values = PropertyType.values();
        for (PropertyType propertyType : values) {
            register(propertyType);
        }
    }

    /**
   * Registers a new property type. A valid type contains only
   * letters (case sensitive). Note, that a property class can be registered
   * only once.
   * 
   * @param type Type.
   * @param clazz Property class.
   * @throws IllegalArgumentException if <code>type</code> is invalid.
   * @throws IllegalStateException if a registration has already been made
   *         for <code>type</code> or <code>clazz</code>.
   */
    private static void register(PropertyType propertyType) {
        String type = propertyType.getTypeCode();
        if (TYPE_PATTERN.matcher(type).matches() == false) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        if (TYPE_TO_CLASS_MAP.containsKey(type)) {
            throw new IllegalStateException("Property type '" + type + "' is already registered");
        }
        Class<? extends Property> clazz = propertyType.getPropertyClass();
        TYPE_TO_CLASS_MAP.put(type, clazz);
        if (CLASS_TO_TYPE_MAP.containsKey(clazz)) {
            throw new IllegalStateException("Property class '" + clazz.getName() + "' is already registered");
        }
        CLASS_TO_TYPE_MAP.put(clazz, propertyType);
    }

    /**
   * Creates a new <code>Property</code> instance of the specified type.
   *  
   * @param type Type of the property.
   * @return property with default value.
   * @throws IllegalArgumentException if <code>type</code> is unknown.
   * @throws RuntimeException if no new instance couldn't be created for
   *         some reason. 
   */
    public static Property create(String type) {
        Class<? extends Property> clazz = TYPE_TO_CLASS_MAP.get(type);
        if (clazz == null) {
            throw new IllegalArgumentException("No Property class registered for type '" + type + "'.");
        }
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't create an instance of " + clazz, e);
        }
    }

    /**
   * Returns the type string of the specified property.
   * 
   * @deprecated use {@link #getPropertyType(Property)}
   * @throws IllegalArgumentException if the class of <code>property</code>
   *         hasn't been registered.
   */
    public static String getType(Property property) {
        return getPropertyType(property).getTypeCode();
    }

    /**
   * Returns the type of the specified property.
   * 
   * @throws IllegalArgumentException if the class of <code>property</code>
   *         hasn't been registered.
   */
    public static PropertyType getPropertyType(Property property) {
        Class<? extends Property> clazz = property.getClass();
        PropertyType type = CLASS_TO_TYPE_MAP.get(clazz);
        if (type == null) {
            throw new IllegalArgumentException("Unregistered property " + clazz);
        }
        return type;
    }

    private PropertyFactory() {
    }
}
