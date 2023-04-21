package net.andycole.xmiparser.parser;

import net.andycole.xmiparser.transfer.ASTypes;
import java.util.Hashtable;
import java.util.HashSet;
import net.andycole.xmiparser.transfer.TransferClass;

/**
 * Base class for all Actionscript parsers.
 * @author Andrew Cole
 */
public class AbstractASParser {

    protected Hashtable _classTable;

    protected HashSet<String> _asTypeTable;

    protected Hashtable<String, String> _importTypeTable;

    protected String _packageName;

    protected TransferClass _classInfo;

    public AbstractASParser() {
        _classTable = new Hashtable();
        _asTypeTable = new HashSet();
        for (ASTypes p : ASTypes.values()) {
            _asTypeTable.add(p.toString());
        }
        _importTypeTable = new Hashtable();
        _packageName = "";
        _classInfo = new TransferClass();
    }

    protected boolean isCustomType(String type) {
        if (_asTypeTable.contains(type)) return false;
        return true;
    }

    protected String getQualifiedType(String type) {
        if (type == null) return "void";
        if (!isCustomType(type)) return type;
        if (type.indexOf(".") > -1) return type;
        if (_importTypeTable.get(type) != null) return _importTypeTable.get(type);
        if (_packageName.length() == 0) return type;
        return _packageName + "." + type;
    }
}
