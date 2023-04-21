package cookxml.core;

import cookxml.core.exception.AccessException;
import cookxml.core.exception.VarLookupException;
import cookxml.core.interfaces.VarLookup;
import cookxml.core.util.ClassUtils;

/**
 * Default behavior of handling a variable field.  It basically looks up the field from the class
 * using the name provided.
 *
 * @author Heng Yuan
 * @version $Id: DefaultVarLookup.java 244 2007-06-07 04:29:13Z coconut $
 * @since CookXml 2.0
 */
public class DefaultVarLookup implements VarLookup {

    public final Object varObject;

    public DefaultVarLookup(Object varObject) {
        this.varObject = varObject;
    }

    public Object getVariable(String name, DecodeEngine decodeEngine) throws VarLookupException {
        if (varObject == null || name == null || name.length() == 0) return null;
        try {
            return ClassUtils.getField(varObject, name, decodeEngine.getCookXml().isAccessible());
        } catch (SecurityException ex) {
            try {
                decodeEngine.handleException(null, new AccessException(decodeEngine, ex, name));
                return null;
            } catch (Exception ex2) {
                throw new VarLookupException(decodeEngine, ex, this, name, null, false);
            }
        } catch (Exception ex) {
            throw new VarLookupException(decodeEngine, ex, this, name, null, true);
        }
    }

    public void setVariable(String name, Object value, DecodeEngine decodeEngine) throws VarLookupException {
        if (varObject == null || name == null || name.length() == 0) return;
        try {
            ClassUtils.setField(varObject, name, value, decodeEngine.getCookXml().isAccessible());
        } catch (SecurityException ex) {
            try {
                decodeEngine.handleException(null, new AccessException(decodeEngine, ex, name));
            } catch (Exception ex2) {
                throw new VarLookupException(decodeEngine, ex, this, name, value, false);
            }
        } catch (Exception ex) {
            throw new VarLookupException(decodeEngine, ex, this, name, value, false);
        }
    }
}
