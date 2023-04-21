package jsclang.supercollider.proxys;

import iscclasslibrary.IJSCObject;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import scclasslibrary.common.core.SCObject;

/**
 * An implementation of {@link AbstractJSCProxy} that converts the provided value Object
 * into a compilable SC code when its getSCInstanceCode() method is invoked.
 * <p>
 * JSCValueProxys get created dynamically by the class {@link JSCClassLoader} for 
 * returning instances of JSCObjects that are considered to be constant SC objects like Symbols,
 * Integers, Floats, 'nil', 'thisProcess' and so on. {@link JSCClassLoader} is responsible for 
 * storing a {@link WeakReference} to the created JSCValueProxy in its HashMap 'constantsKeyMap'
 * Once the created JSCValueProxys gets finalized it will remove its fValue from the 'constantsKeyMap'. 
 * <p>
 * This class should <strong>only</strong> be instantiated by {@link JSCClassLoader} for the
 * purpose of returning new constant JSCObjects.
 * <p>
 * The method toString() will always return value.toString()<br>
 * The method hashCode() will always return the hashCode of the wrapped SC instance.<br>
 * The method equals(Object obj) will return true if:
 * <li> <code>this == obj</code> returns true</li>
 * <li> <code>obj instanceof IJSCObject</code> returns true and <code>JSCInterpreter.equals(this, 
 * (IJSCObject)obj)</code> returns true.</li>
 * <br>
 * <br>
 * If an error is catched while invoking the SC Interpreter during the methods hashCode() or
 * equals(Object obj) an {@link IllegalStateException} is thrown.<br>
 * </p>
 *  <br>
 * <p>
 * This class is <strong>not</strong> intended to be used by clients.
 * <p><br>
 * example code:
 * <blockquote>
 * <code>
 * JSCValueProxy aSymbol = new JSCValueProxy("name of the Symbol", JSCClassLoader.SYMBOL);<br>
 * JSCValueProxy aFloat = new JSCValueProxy(3.456, JSCClassLoader.FLOAT);<br>
 * </code>
 * </blockquote>
 * @author Dieter Kleinrath (kleinrath@mur.at)
 *
 */
class JSCValueProxy extends AbstractJSCProxy implements IJSCProxyConstants {

    private Object fValue;

    private int fType;

    private String fSCInstanceCode;

    /**
	 * Creates a new JSCValueProxy with the provided value and the given type. 
	 * Both value and type will be used when converting this JSCValueProxy into compilable
	 * SC code. If the value is not important for the conversion it may be null. In this
	 * case this constructor will assign the converted SC code to the value.
	 * 
	 * @param value
	 * @param type
	 */
    JSCValueProxy(Object value, int type) {
        this.fType = type;
        if (value == null) {
            value = getSCInstanceCode();
        } else {
            this.fValue = value;
        }
    }

    @Override
    public String getSCInstanceCode() {
        if (fSCInstanceCode == null) {
            fSCInstanceCode = convertJavaToSC();
        }
        return fSCInstanceCode;
    }

    @Override
    public String toString() {
        return fValue.toString();
    }

    @Override
    public int hashCode() {
        try {
            return (Integer) invoke("hash", int.class, null);
        } catch (SCProxyException e) {
            throw new IllegalStateException("An unexpected error occurred while interpreting the SC code '" + getSCInstanceCode() + ".hash'.", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof IJSCObject) return false;
        IJSCObject other = (IJSCObject) obj;
        try {
            return JSCInterpreter.equals(this, other);
        } catch (SCProxyException e) {
            throw new IllegalStateException("An unexpected error occurred while interpreting the SC code'" + getSCInstanceCode() + " == " + other.getSCInstanceCode() + "'.", e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        HashMap<Object, WeakReference<SCObject>> map = JSCClassLoader.constantsKeyMap();
        synchronized (map) {
            map.remove(fValue);
        }
    }

    /**
	 * Called by JSCValueProxy when its value should be converted to SC code.
	 * 
	 * @param javaType The type of the JSCValueProxy
	 * @param javaValue The value to be converted
	 * @return the converted SC code
	 */
    private String convertJavaToSC() {
        switch(fType) {
            case SYMBOL:
                return "'" + fValue.toString() + "'";
            case INTEGER:
            case FLOAT:
            case THIS:
            case THIS_PROCESS:
            case THIS_THREAD:
            case NIL:
                return fValue.toString();
            case TRUE:
                return SC_TRUE;
            case FALSE:
                return SC_FALSE;
            case CHAR:
                return "$" + fValue;
            default:
                throw new IllegalStateException("The type '" + fValue + "' is no defined type for JSCValueProxys.");
        }
    }
}
