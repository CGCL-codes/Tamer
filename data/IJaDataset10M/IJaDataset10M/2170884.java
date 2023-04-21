package ru.adv.xml.newt;

import ru.adv.util.ErrorCodeException;
import ru.adv.db.config.ConfigParserLog;
import ru.adv.db.create.DBCreateLog;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import java.util.*;
import java.io.Serializable;

/**
 * @version $Revision: 1.2 $
 */
public class SyncLog implements DBCreateLog, ConfigParserLog, Serializable {

    private static final long serialVersionUID = 322935690012858L;

    private ErrorCodeException _prepareConfigException;

    private ErrorCodeException _checkSystemTablesException;

    private ErrorCodeException _dropTriggersException;

    private ErrorCodeException _dropFunctionsException;

    private ErrorCodeException _dropConstraintsException;

    private ErrorCodeException _dropViewsException;

    private ErrorCodeException _dropUnusedObjectsException;

    private ErrorCodeException _createFunctionsException;

    private ErrorCodeException _createNonExistentObjectsException;

    private ErrorCodeException _sychronizeAttributesException;

    private ErrorCodeException _createViewsException;

    private ErrorCodeException _createConstrainsException;

    private ErrorCodeException _createTriggersException;

    private ErrorCodeException _createIndexesException;

    private ErrorCodeException _setPermissionsException;

    private ErrorCodeException _createDatabaseException;

    private ErrorCodeException _checkIntegrityException;

    private HashMap _defaultAttributes = new HashMap();

    private HashMap _foreignAttributes = new HashMap();

    private boolean _finished;

    private ErrorCodeException _dropIndexesException;

    public void setDefaultAttribute(String object, String attribute) {
        addString(_defaultAttributes, object, attribute);
    }

    public void setForeignAttribute(String object, String attribute) {
        addString(_foreignAttributes, object, attribute);
    }

    private void addString(Map map, String key, String value) {
        List attrs = (List) map.get(key);
        if (attrs == null) {
            attrs = new LinkedList();
            map.put(key, attrs);
        }
        attrs.add(value);
    }

    public void setPrepareConfigException(ErrorCodeException prepareConfigException) {
        _prepareConfigException = prepareConfigException;
    }

    public ErrorCodeException getPrepareConfigException() {
        return _prepareConfigException;
    }

    public void setCheckSystemTablesException(ErrorCodeException checkSystemTablesException) {
        _checkSystemTablesException = checkSystemTablesException;
    }

    public void setDropTriggersException(ErrorCodeException dropTriggersException) {
        _dropTriggersException = dropTriggersException;
    }

    public void setDropFunctionsException(ErrorCodeException dropFunctionsException) {
        _dropFunctionsException = dropFunctionsException;
    }

    public void setDropConstrainsException(ErrorCodeException dropConstraintsException) {
        _dropConstraintsException = dropConstraintsException;
    }

    public void setDropViewsException(ErrorCodeException dropViewsException) {
        _dropViewsException = dropViewsException;
    }

    public void setDropUnusedObjectsException(ErrorCodeException dropUnusedObjectsException) {
        _dropUnusedObjectsException = dropUnusedObjectsException;
    }

    public void setCreateFunctionsException(ErrorCodeException createFunctionsException) {
        _createFunctionsException = createFunctionsException;
    }

    public void setIntegrityCheckException(ErrorCodeException checkIntegrityException) {
        _checkIntegrityException = checkIntegrityException;
    }

    public void setCreateNonExistentObjectsException(ErrorCodeException createNonExistentObjectsException) {
        _createNonExistentObjectsException = createNonExistentObjectsException;
    }

    public void setSychronizeAttributesException(ErrorCodeException sychronizeAttributesException) {
        _sychronizeAttributesException = sychronizeAttributesException;
    }

    public void setCreateViewsException(ErrorCodeException createViewsException) {
        _createViewsException = createViewsException;
    }

    public void setCreateConstrainsException(ErrorCodeException createConstrainsException) {
        _createConstrainsException = createConstrainsException;
    }

    public void setCreateTriggersException(ErrorCodeException createTriggersException) {
        _createTriggersException = createTriggersException;
    }

    public void setCreateIndexesException(ErrorCodeException createIndexesException) {
        _createIndexesException = createIndexesException;
    }

    public void setSetPermissionsException(ErrorCodeException setPermissionsException) {
        _setPermissionsException = setPermissionsException;
    }

    public Element toXML(Document doc) {
        Element result = doc.createElement("log");
        appendExceptions(result);
        appendAdditionalData(result);
        appendFinished(result);
        return result;
    }

    private void appendExceptions(Element result) {
        appendException(result, _prepareConfigException, "prepare-config");
        appendException(result, _createDatabaseException, "create-database");
        appendException(result, _checkSystemTablesException, "check-system-tables");
        appendException(result, _dropTriggersException, "drop-triggers");
        appendException(result, _dropFunctionsException, "drop-functions");
        appendException(result, _dropConstraintsException, "drop-constraints");
        appendException(result, _dropViewsException, "drop-views");
        appendException(result, _dropUnusedObjectsException, "drop-unused-objects");
        appendException(result, _checkIntegrityException, "check-integrity-database");
        appendException(result, _createFunctionsException, "create-functions");
        appendException(result, _createNonExistentObjectsException, "create-nonexistent-objects");
        appendException(result, _sychronizeAttributesException, "synchronize-attributes");
        appendException(result, _createViewsException, "create-views");
        appendException(result, _createConstrainsException, "create-constrains");
        appendException(result, _createTriggersException, "create-triggers");
        appendException(result, _dropIndexesException, "drop-indexes");
        appendException(result, _createIndexesException, "create-indexes");
        appendException(result, _setPermissionsException, "set-permissions");
    }

    private void appendFinished(Element result) {
        if (_finished) {
            Element finished = result.getOwnerDocument().createElement("finished");
            result.appendChild(finished);
        }
    }

    private void appendAdditionalData(Element parent) {
        Set objects = new TreeSet(_defaultAttributes.keySet());
        objects.addAll(_foreignAttributes.keySet());
        for (Iterator i = objects.iterator(); i.hasNext(); ) {
            String object = (String) i.next();
            Element objectElement = parent.getOwnerDocument().createElement("object");
            objectElement.setAttribute("id", object);
            if (_defaultAttributes.containsKey(object)) {
                appendAttrs(object, _defaultAttributes, objectElement, "default");
            }
            if (_foreignAttributes.containsKey(object)) {
                appendAttrs(object, _foreignAttributes, objectElement, "foreign");
            }
        }
    }

    private void appendAttrs(String object, Map map, Element parent, String reason) {
        List attributes = getList(map, object);
        for (Iterator iterator = attributes.iterator(); iterator.hasNext(); ) {
            String attr = (String) iterator.next();
            Element attrElement = parent.getOwnerDocument().createElement("attr");
            attrElement.setAttribute("id", attr);
            attrElement.setAttribute("reason", reason);
            parent.appendChild(attrElement);
        }
    }

    private static List getList(Map map, String object) {
        List result = (List) map.get(object);
        if (result == null) {
            result = Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(result);
    }

    private void appendException(Element parent, ErrorCodeException exception, String elementName) {
        if (exception == null) {
            return;
        }
        Element element = parent.getOwnerDocument().createElement(elementName);
        parent.appendChild(element);
        if (ErrorCodeException.OK.equals(exception)) {
            element.setAttribute("status", "OK");
        } else if (ErrorCodeException.STARTED.equals(exception)) {
            element.setAttribute("status", "STARTED");
        } else {
            element.setAttribute("status", "FAIL");
            element.appendChild(exception.toXML(parent.getOwnerDocument()));
        }
    }

    public void setCreateDatabaseException(ErrorCodeException e) {
        _createDatabaseException = e;
    }

    public boolean isFinished() {
        return _finished;
    }

    public void setFinished(boolean finished) {
        _finished = finished;
    }

    public boolean isDafaultValuesNeeded() {
        return !_defaultAttributes.isEmpty() || !_foreignAttributes.isEmpty();
    }

    public void setDropIndexesException(ErrorCodeException e) {
        _dropIndexesException = e;
    }
}
