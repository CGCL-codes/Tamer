package ar.com.telefonica.fwk.hbm;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * Base Class for Custom hibernate types that represents enums
 * by their simple name. All you have to do is implement the
 * public Class<? extends Enum> returnedClas() method
 * 
 * This Class is only good for java 5 enums
 * @author mgonzalez
 */
@SuppressWarnings("unchecked")
public abstract class AbstractEnumTypeH implements UserType, Serializable {

    private static final long serialVersionUID = 1233997639783578277L;

    private static final Map<Class<? extends Enum>, Map<Enum, String>> enum2keyMaps = new HashMap<Class<? extends Enum>, Map<Enum, String>>();

    private static final Map<Class<? extends Enum>, Map<String, Enum>> key2enumMaps = new HashMap<Class<? extends Enum>, Map<String, Enum>>();

    /**
     * Implementations must override this method to specify the enum class to handle 
     */
    public abstract Class<? extends Enum> returnedClass();

    private void init() {
        if (!this.isInited()) {
            this.initMaps();
        }
    }

    /*************************
         * De UserType
         **************************/
    private static final int[] SQL_TYPES = new int[] { Types.VARCHAR };

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        this.init();
        String code = (String) Hibernate.STRING.nullSafeGet(rs, names[0]);
        if (code != null) {
            Object item = this.getKey2EnumMap().get(code);
            if (item == null) {
                throw new HibernateException("incorrect key '" + code + "' from database for enum '" + this.returnedClass().getName() + "'. There is no any enum instance with that key.");
            }
            return item;
        }
        return null;
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        this.init();
        String key = (String) this.getEnum2KeyMap().get(value);
        Hibernate.STRING.nullSafeSet(st, key, index);
    }

    public int hashCode(Object x) throws HibernateException {
        if (x != null) {
            return x.hashCode();
        }
        return 0;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    /************************
             * CONTROL DE MAPAS
             ************************/
    private void addEnum2KeyMapItem(Enum item, String key) {
        this.getEnum2KeyMap().put(item, key);
    }

    private void addKey2EnumMapItem(String key, Enum item) {
        this.getKey2EnumMap().put(key, item);
    }

    private synchronized void initMaps() {
        if (!this.isInited()) {
            key2enumMaps.put(this.returnedClass(), new HashMap<String, Enum>());
            enum2keyMaps.put(this.returnedClass(), new HashMap<Enum, String>());
            doInitMapsFor(this.returnedClass());
        }
    }

    /**
                 * Populates the maps for the enum class
                 * @author mgonzalez
                 */
    protected synchronized <T extends Enum<T>> void doInitMapsFor(Class<T> enumClass) {
        for (Enum<T> aEnum : EnumSet.allOf(enumClass)) {
            if (!this.getEnum2KeyMap().containsKey(aEnum)) {
                String name = aEnum.name();
                this.addEnum2KeyMapItem(aEnum, name);
                this.addKey2EnumMapItem(name, aEnum);
            }
        }
    }

    /**
                 * Returns the name of the enum
                 * @author mgonzalez
                 */
    protected String getEnumName(Enum theEnum) {
        return theEnum.name();
    }

    private Map<Enum, String> getEnum2KeyMap() {
        return enum2keyMaps.get(this.returnedClass());
    }

    private Map<String, Enum> getKey2EnumMap() {
        return key2enumMaps.get(this.returnedClass());
    }

    private boolean isInited() {
        return this.getEnum2KeyMap() != null;
    }

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public boolean isMutable() {
        return false;
    }

    public boolean equals(Object x, Object y) {
        return x == y;
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }
}
