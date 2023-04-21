package org.mozilla.javascript;

/**
 * This class reflects Java packages into the JavaScript environment.  We
 * lazily reflect classes and subpackages, and use a caching/sharing
 * system to ensure that members reflected into one JavaPackage appear
 * in all other references to the same package (as with Packages.java.lang
 * and java.lang).
 *
 * @author Mike Shaver
 * @see NativeJavaArray
 * @see NativeJavaObject
 * @see NativeJavaClass
 */
public class NativeJavaPackage extends ScriptableObject {

    static final long serialVersionUID = 7445054382212031523L;

    public String getClassName() {
        return "JavaPackage";
    }

    public boolean has(String id, Scriptable start) {
        return true;
    }

    public boolean has(int index, Scriptable start) {
        return false;
    }

    public void put(String id, Scriptable start, Object value) {
    }

    public void put(int index, Scriptable start, Object value) {
        throw Context.reportRuntimeError0("msg.pkg.int");
    }

    public Object get(String id, Scriptable start) {
        throw new UnsupportedOperationException();
    }

    public Object get(int index, Scriptable start) {
        return NOT_FOUND;
    }

    synchronized Object getPkgProperty(String name, Scriptable start, boolean createPkg) {
        throw new UnsupportedOperationException();
    }

    public Object getDefaultValue(Class ignored) {
        return toString();
    }

    public String toString() {
        return "[JavaPackage " + packageName + "]";
    }

    public boolean equals(Object obj) {
        if (obj instanceof NativeJavaPackage) {
            NativeJavaPackage njp = (NativeJavaPackage) obj;
            return packageName.equals(njp.packageName);
        }
        return false;
    }

    public int hashCode() {
        return packageName.hashCode();
    }

    private String packageName;
}
