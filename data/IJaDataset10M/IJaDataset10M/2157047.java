package oscript.data;

import oscript.exceptions.*;

/**
 * An <code>OSpecial</code> is used for different special values that
 * aren't really members of any other type.  The values of Special are
 * unique, and special care is taken when constructing, and serializing
 * and unserializing the special to ensure that this property is
 * preservied... ie. there are never two instances of <code>NULL</code>.
 * 
 * @author Rob Clark (rob@ti.com)
 */
class OSpecial extends Value {

    private static java.util.Hashtable specials = new java.util.Hashtable();

    private String str;

    static Value makeSpecial(String str) {
        Value val = null;
        synchronized (specials) {
            val = (Value) (specials.get(str));
            if (val == null) {
                val = new OSpecial(str);
                specials.put(str, val);
            }
        }
        return val;
    }

    /**
   * Class Constructor.
   * 
   * @param str          the string representation of this special
   */
    protected OSpecial(String str) {
        super();
        synchronized (specials) {
            if (specials.get(str) != null) throw new ProgrammingErrorException("duplicate special!");
        }
        this.str = str;
    }

    /**
   * Class Constructor.
   * 
   * @param args         arguments to this constructor
   * @throws PackagedScriptObjectException(Exception) if wrong number of args
   */
    public OSpecial(oscript.util.MemberTable args) {
        super();
        throw PackagedScriptObjectException.makeExceptionWrapper(new OIllegalArgumentException("wrong number of args!"));
    }

    /**
   * Get the type of this object.  The returned type doesn't have to take
   * into account the possibility of a script type extending a built-in
   * type, since that is handled by {@link #getType}.
   * 
   * @return the object's type
   */
    protected Value getTypeImpl() {
        return this;
    }

    /**
   * If this object is a type, determine if an instance of this type is
   * an instance of the specified type, ie. if this is <code>type</code>,
   * or a subclass.
   * 
   * @param type         the type to compare this type to
   * @return <code>true</code> or <code>false</code>
   * @throws PackagedScriptObjectException(NoSuchMemberException)
   */
    public boolean isA(Value type) {
        return bopEquals(type).castToBoolean();
    }

    /**
   * Convert this object to a native java <code>String</code> value.
   * 
   * @return a String value
   * @throws PackagedScriptObjectException(NoSuchMemberException)
   */
    public String castToString() throws PackagedScriptObjectException {
        return str;
    }

    /**
   * Convert this object to a native java <code>Object</code> value.
   * 
   * @return a java object
   * @throws PackagedScriptObjectException(NoSuchMemberException)
   */
    public Object castToJavaObject() throws PackagedScriptObjectException {
        return null;
    }

    protected PackagedScriptObjectException noSuchMember(String member) {
        return PackagedScriptObjectException.makeExceptionWrapper(new ONullReferenceException(str));
    }

    Object readResolve() throws java.io.ObjectStreamException {
        Object obj;
        synchronized (specials) {
            obj = specials.get(str);
            if (obj == null) {
                specials.put(str, this);
                obj = this;
            }
        }
        return obj;
    }
}
