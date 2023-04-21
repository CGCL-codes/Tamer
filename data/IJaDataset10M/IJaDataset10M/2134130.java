package cx.ath.contribs.internal.xerces.impl.validation;

import java.util.Enumeration;
import java.util.Hashtable;
import cx.ath.contribs.internal.xerces.impl.dv.ValidationContext;
import cx.ath.contribs.internal.xerces.util.SymbolTable;
import cx.ath.contribs.internal.xerces.xni.NamespaceContext;

/**
 * Implementation of ValidationContext inteface. Used to establish an
 * environment for simple type validation.
 * 
 * @xerces.internal
 *
 * @author Elena Litani, IBM
 * @version $Id: ValidationState.java,v 1.2 2007/07/13 07:23:29 paul Exp $
 */
public class ValidationState implements ValidationContext {

    private boolean fExtraChecking = true;

    private boolean fFacetChecking = true;

    private boolean fNormalize = true;

    private boolean fNamespaces = true;

    private EntityState fEntityState = null;

    private NamespaceContext fNamespaceContext = null;

    private SymbolTable fSymbolTable = null;

    private final Hashtable fIdTable = new Hashtable();

    private final Hashtable fIdRefTable = new Hashtable();

    private static final Object fNullValue = new Object();

    public void setExtraChecking(boolean newValue) {
        fExtraChecking = newValue;
    }

    public void setFacetChecking(boolean newValue) {
        fFacetChecking = newValue;
    }

    public void setNormalizationRequired(boolean newValue) {
        fNormalize = newValue;
    }

    public void setUsingNamespaces(boolean newValue) {
        fNamespaces = newValue;
    }

    public void setEntityState(EntityState state) {
        fEntityState = state;
    }

    public void setNamespaceSupport(NamespaceContext namespace) {
        fNamespaceContext = namespace;
    }

    public void setSymbolTable(SymbolTable sTable) {
        fSymbolTable = sTable;
    }

    /**
     * return null if all IDREF values have a corresponding ID value;
     * otherwise return the first IDREF value without a matching ID value.
     */
    public String checkIDRefID() {
        Enumeration en = fIdRefTable.keys();
        String key;
        while (en.hasMoreElements()) {
            key = (String) en.nextElement();
            if (!fIdTable.containsKey(key)) {
                return key;
            }
        }
        return null;
    }

    public void reset() {
        fExtraChecking = true;
        fFacetChecking = true;
        fNamespaces = true;
        fIdTable.clear();
        fIdRefTable.clear();
        fEntityState = null;
        fNamespaceContext = null;
        fSymbolTable = null;
    }

    /**
     * The same validation state can be used to validate more than one (schema)
     * validation roots. Entity/Namespace/Symbol are shared, but each validation
     * root needs its own id/idref tables. So we need this method to reset only
     * the two tables.
     */
    public void resetIDTables() {
        fIdTable.clear();
        fIdRefTable.clear();
    }

    public boolean needExtraChecking() {
        return fExtraChecking;
    }

    public boolean needFacetChecking() {
        return fFacetChecking;
    }

    public boolean needToNormalize() {
        return fNormalize;
    }

    public boolean useNamespaces() {
        return fNamespaces;
    }

    public boolean isEntityDeclared(String name) {
        if (fEntityState != null) {
            return fEntityState.isEntityDeclared(getSymbol(name));
        }
        return false;
    }

    public boolean isEntityUnparsed(String name) {
        if (fEntityState != null) {
            return fEntityState.isEntityUnparsed(getSymbol(name));
        }
        return false;
    }

    public boolean isIdDeclared(String name) {
        return fIdTable.containsKey(name);
    }

    public void addId(String name) {
        fIdTable.put(name, fNullValue);
    }

    public void addIdRef(String name) {
        fIdRefTable.put(name, fNullValue);
    }

    public String getSymbol(String symbol) {
        if (fSymbolTable != null) return fSymbolTable.addSymbol(symbol);
        return symbol.intern();
    }

    public String getURI(String prefix) {
        if (fNamespaceContext != null) {
            return fNamespaceContext.getURI(prefix);
        }
        return null;
    }
}
