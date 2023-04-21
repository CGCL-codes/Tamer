package ru.adv.db.config;

import ru.adv.db.adapter.Types;
import ru.adv.db.filter.FilterCodes;
import ru.adv.db.filter.FilterCollections;
import ru.adv.db.filter.FilterException;
import ru.adv.db.filter.FilterParser;
import ru.adv.io.UnknownIOSourceException;
import ru.adv.util.*;
import ru.adv.xml.newt.NewtContext;
import ru.adv.xml.verifier.Verifier;
import ru.adv.xml.verifier.VerifierException;
import org.w3c.dom.*;
import ru.adv.cache.Cache;
import java.util.*;

/**
 * User: roma
 * Date: 28.04.2005
 * Time: 17:00:52
 */
class SourceConfigParser {

    public static final String VID_ATTR_ID = "vid";

    private static final String VID_CLASS = "ru.adv.db.calculated.Vid";

    private static final String CALCULATE = "calculate";

    public static final String VERSION_ATTR_LENGTH = "200";

    private Document _sourceDoc;

    private HashMap<String, Element> _types;

    private HashMap<String, Element> _commonAttrs;

    private HashMap<String, Element> _sequences;

    private HashMap<String, Element> _commonObjects;

    private FilterCodes _codes = new FilterCodes();

    private FilterCollections _colls = new FilterCollections();

    private ConfigParserLog _log;

    private Element _result;

    private Element _baseElement;

    private Element _configElement;

    private Map<String, Element> _objects;

    private Map<String, Element> _baseObjects;

    private Map<Element, List<String>> _attrIds;

    public SourceConfigParser(Document sourceDoc, ConfigParserLog log) {
        _sourceDoc = sourceDoc;
        _log = log;
    }

    public void parse(String dbId, NewtContext ctx) throws ConfigParserException, UnknownIOSourceException, VerifierException {
        _types = new HashMap<String, Element>();
        _commonAttrs = new HashMap<String, Element>();
        _commonObjects = new HashMap<String, Element>();
        _sequences = new HashMap<String, Element>();
        verifySource(_sourceDoc, ctx);
        _configElement = XmlUtils.findElement(_sourceDoc, "config");
        initElementLists();
        _result = parseSourceConfig(dbId);
    }

    public Element getResult() {
        return _result;
    }

    public Map<String, Element> getCommonObjects() {
        return Collections.unmodifiableMap(_commonObjects);
    }

    public FilterCodes getCodes() {
        return _codes;
    }

    public FilterCollections getColls() {
        return _colls;
    }

    private Element parseSourceConfig(String dbId) throws ConfigParserException {
        initCommonAttrs();
        initCommonObjects();
        setDbId(dbId);
        createIndexElements();
        checkAliasTo();
        return _baseElement;
    }

    private void checkAliasTo() throws ConfigParserException {
        for (String id : _commonObjects.keySet()) {
            Element object = _commonObjects.get(id);
            if (object.hasAttribute("alias-to")) {
                String aliasTo = object.getAttribute("alias-to");
                if (!_commonObjects.containsKey(aliasTo)) {
                    throw new ConfigParserException("Object '" + id + "' has wrong alias-to=" + aliasTo);
                }
                XmlUtils.removeAllChildren(object);
            }
        }
    }

    private void checkAttrId(Element node) throws BadNameException {
        String id = node.getAttribute("id");
        if (!DBConfig.isVariable(id) && id.lastIndexOf('_') != -1) {
            throw new BadNameException(ADVExceptionCode.PREP_INVALID_ID, "Illegal identificator", "id", id);
        }
    }

    private void setDbId(String dbId) {
        if (dbId != null && dbId.length() > 0) {
            _baseElement.setAttribute("id", dbId);
        }
    }

    private void initType(Element elem) throws ConfigParserException {
        checkTypeElement(elem);
        _types.put(elem.getAttribute("id"), elem);
    }

    private void initCommonAttrs() throws ConfigParserException {
        for (Element element : _commonAttrs.values()) {
            inheritCommonAttrs(element, new HashSet<String>());
            checkAttribute(element);
        }
    }

    private void initCommonObjects() throws ConfigParserException {
        for (Element element : _commonObjects.values()) {
            inheritCommonObjects(element, new HashSet<String>());
        }
        for (Element element1 : _baseObjects.values()) {
            checkAllAttrsForObject(element1);
        }
    }

    private void initFilters(Node n) throws ConfigParserException {
        FilterParser fp = new FilterParser(n);
        FilterCodes tcodes = fp.parseCodes();
        for (Object o : tcodes.keySet()) {
            String id = (String) o;
            if (_codes.containsKey(id)) throw new ConfigParserException("Filter '" + id + "' already defined");
            _codes.put(id, tcodes.get(id));
        }
        FilterCollections tcolls;
        try {
            tcolls = fp.parseCollections();
        } catch (FilterException e) {
            throw new ConfigParserException(e);
        }
        for (Object o1 : tcolls.keySet()) {
            String id = (String) o1;
            if (_colls.containsKey(id)) throw new ConfigParserException("Filter '" + id + "' already defined");
            _colls.put(id, tcolls.get(id));
        }
    }

    private ConfigParserException newConfigParserException(String msg) {
        ConfigParserException result = new ConfigParserException(ConfigParserException.DBCONFIG_CANNOT_PREPARE, msg);
        if (_log != null) {
            _log.setPrepareConfigException(result);
        }
        return result;
    }

    private void addIDType(boolean stringId) {
        Element elem = _sourceDoc.createElement("type");
        elem.setAttribute("id", "id");
        if (stringId) {
            elem.setAttribute("dbt", "string");
            elem.setAttribute("length", "25");
        } else {
            elem.setAttribute("dbt", "long");
        }
        _types.put("id", elem);
    }

    private void addCalculateType() {
        Element elem = _sourceDoc.createElement("type");
        elem.setAttribute("id", CALCULATE);
        elem.setAttribute("dbt", "calculated");
        _types.put(CALCULATE, elem);
    }

    private void checkTypeElement(Element elem) throws ConfigParserException {
        hasIdAttribute(elem);
        if (!elem.hasAttribute("dbt")) {
            throw new ConfigParserException("Missing 'dbt' attribute: " + XmlUtils.toString(elem));
        }
        if (elem.getAttribute("id").equals("id")) {
            throw new ConfigParserException("You may not define 'id' type: " + XmlUtils.toString(elem));
        }
    }

    private void inheritCommonAttrs(Element elem, HashSet<String> inherited) throws ConfigParserException {
        String classId = elem.getAttribute("class");
        if (classId.length() > 0) {
            if (_commonAttrs.containsKey(classId)) {
                if (inherited.contains(classId)) {
                    throw (new ConfigParserException("Class " + classId + " inherited recursively: " + XmlUtils.toString(elem)));
                } else {
                    inherited.add(classId);
                }
                Element parent = _commonAttrs.get(classId);
                inheritCommonAttrs(parent, inherited);
                inheritAllAttributes(elem, parent);
            } else {
                throw (new ConfigParserException("Class not found: " + XmlUtils.toString(elem)));
            }
        }
    }

    private void inheritCommonObjects(Element elem, HashSet<String> inherited) throws ConfigParserException {
        String classId = elem.getAttribute("class");
        if (classId.length() == 0) {
            inheritAttrsForObject(elem);
        } else {
            if (_commonObjects.containsKey(classId)) {
                if (inherited.contains(classId)) {
                    throw new ConfigParserException("Class " + classId + " inherited recursively: " + XmlUtils.toString(elem));
                } else {
                    inherited.add(classId);
                }
                Element parent = _commonObjects.get(classId);
                if (parent.getParentNode() != null && (parent.getParentNode()).getNodeName().equals("base")) {
                    throw new ConfigParserException("Can not inherit objects from <base> : " + XmlUtils.toString(elem));
                }
                inheritCommonObjects(parent, inherited);
                inheritAllAttributes(elem, parent);
                inheritAttrsForObject(elem);
            } else throw (new ConfigParserException("Class not found: " + XmlUtils.toString(elem)));
        }
    }

    private void checkAllAttrsForObject(Element object) throws ConfigParserException {
        List<Element> list = getAttributes(object);
        for (Element attr : list) {
            checkAttribute(attr);
        }
    }

    private void checkAttribute(Element elem) throws ConfigParserException {
        hasIdAttribute(elem);
        if (elem.hasAttribute(ObjectAttr.CALCULATE_ATTR_NAME)) {
            elem.setAttribute("dbt", Types.DBT_CALCULATED);
            return;
        }
        if (elem.hasAttribute("foreign")) {
            elem.setAttribute("type", "id");
        }
        String typeId = elem.getAttribute("type");
        if (typeId.length() == 0 && !elem.hasAttribute("dbt")) {
            throw new ConfigParserException("Missing 'type' attribute: " + XmlUtils.toString(elem));
        }
        if (typeId.length() > 0 && !_types.containsKey(typeId)) {
            throw new ConfigParserException("Type '" + typeId + "' not found: " + XmlUtils.toString(elem));
        }
        inheritAllAttributes(elem, _types.get(typeId));
        if (elem.getAttribute("id").equals("tree")) {
            if (!typeId.equals("id")) {
                throw new ConfigParserException("Attribute 'tree' must have 'id' type only: " + XmlUtils.toString(elem));
            } else {
                elem.setAttribute("index", "yes");
            }
        }
        if (elem.hasAttribute("input-filter")) {
            String filter = elem.getAttribute("input-filter");
            if (!_codes.containsKey(filter) && !_colls.containsKey(filter)) throw new ConfigParserException("Filter '" + filter + "' not found: " + XmlUtils.toString(elem));
        }
        if (elem.hasAttribute("output-filter")) {
            String filter = elem.getAttribute("output-filter");
            if (!_codes.containsKey(filter) && !_colls.containsKey(filter)) throw new ConfigParserException("Filter '" + filter + "' not found: " + XmlUtils.toString(elem));
        }
        if (elem.hasAttribute("sequence")) {
            String seq = elem.getAttribute("sequence");
            if (!seq.equals(ObjectAttr.SEQUENCE_NONE) && !seq.equals(ObjectAttr.SEQUENCE_DEFAULT)) {
                if (!_sequences.containsKey(seq)) throw new ConfigParserException("Sequence '" + seq + "' not found: " + XmlUtils.toString(elem));
            }
        }
    }

    private void inheritAttrsForObject(Element elem) throws ConfigParserException {
        List<Element> list = getAttributes(elem);
        for (Element attr : list) {
            hasIdAttribute(attr);
            String classId = attr.getAttribute("class");
            if (classId.length() > 0) {
                if (_commonAttrs.containsKey(classId)) inheritAllAttributes(attr, _commonAttrs.get(classId)); else throw new ConfigParserException("Class '" + classId + "' not found: " + XmlUtils.toString(attr));
            }
        }
        String classId = elem.getAttribute("class");
        if (classId.length() > 0) {
            if (_commonObjects.containsKey(classId)) {
                Element parent = _commonObjects.get(classId);
                list = getAttributes(parent);
                for (Element parentAttr : list) {
                    Element attr = getObjectAttribute(elem, parentAttr.getAttribute("id"));
                    if (attr == null) {
                        Element newChild = (Element) parentAttr.cloneNode(false);
                        addObjectAttribute(elem, newChild);
                    } else {
                        inheritAllAttributes(attr, parentAttr);
                    }
                }
            } else {
                throw (new ConfigParserException("Class '" + classId + "' not found: " + XmlUtils.toString(elem)));
            }
        }
    }

    private void addObjectAttribute(Element elem, Element newChild) {
        elem.appendChild(newChild);
    }

    private void hasIdAttribute(Element elem) throws ConfigParserException {
        if (!elem.hasAttribute("id")) throw (new ConfigParserException("Missing 'id' attribute: " + XmlUtils.toString(elem)));
    }

    private void inheritAllAttributes(Element elem, Element parent) {
        if (parent == null) {
            return;
        }
        NamedNodeMap map = parent.getAttributes();
        for (int i = 0; i < map.getLength(); i++) {
            Attr attr = (Attr) map.item(i);
            String attrName = attr.getName();
            if (!elem.hasAttribute(attrName)) {
                elem.setAttribute(attrName, attr.getValue());
            }
        }
    }

    private void verifySource(Document sourceDoc, NewtContext ctx) throws VerifierException, UnknownIOSourceException {
        Verifier v = new Verifier(InputOutput.create(ConfigParser.SOURCE_BASE_CONFIG_SCHEMA), getVerifierCache(ctx));
        v.verify(sourceDoc);
    }

    private Cache getVerifierCache(NewtContext ctx) {
        Cache cache = null;
        if (ctx != null) {
            cache = ctx.getVerifierCache();
        }
        return cache;
    }

    private void addVidAttribute(Element object) throws ConfigParserException {
        Element vidAttr = getObjectAttribute(object, VID_ATTR_ID);
        if (vidAttr != null) {
            if (isCorrectVidAttribute(vidAttr)) {
                return;
            } else {
                throw new ConfigParserException("Attribute identificator '" + VID_ATTR_ID + "' reserved");
            }
        }
        vidAttr = object.getOwnerDocument().createElement("attr");
        vidAttr.setAttribute("id", VID_ATTR_ID);
        vidAttr.setAttribute("label", VID_ATTR_ID);
        vidAttr.setAttribute(CALCULATE, VID_CLASS);
        vidAttr.setAttribute("type", CALCULATE);
        insertBeforeFirstChild(object, vidAttr);
    }

    private void addObjectVersionAttribute(Element object) throws ConfigParserException {
        Element attrVersion = getObjectAttribute(object, ConfigParser.VERSION_ATTR_ID);
        if (attrVersion != null) {
            throw new ConfigParserException("Attribute identificator '" + ConfigParser.VERSION_ATTR_ID + "' reserved");
        }
        attrVersion = object.getOwnerDocument().createElement("attr");
        attrVersion.setAttribute("id", ConfigParser.VERSION_ATTR_ID);
        attrVersion.setAttribute("dbt", "string");
        attrVersion.setAttribute("label", ConfigParser.VERSION_ATTR_ID);
        attrVersion.setAttribute("unique", "no");
        attrVersion.setAttribute("length", VERSION_ATTR_LENGTH);
        attrVersion.setAttribute("default", "0");
        attrVersion.setAttribute("system", "yes");
        insertBeforeFirstChild(object, attrVersion);
    }

    private void addObjectRemovedAttribute(Element object) throws ConfigParserException {
        Element attrRemoved = getObjectAttribute(object, ConfigParser.REMOVED_ATTR_ID);
        if (attrRemoved != null) {
            throw new ConfigParserException("Attribute identificator '" + ConfigParser.REMOVED_ATTR_ID + "' reserved");
        }
        attrRemoved = object.getOwnerDocument().createElement("attr");
        attrRemoved.setAttribute("id", ConfigParser.REMOVED_ATTR_ID);
        attrRemoved.setAttribute("dbt", "boolean");
        attrRemoved.setAttribute("label", ConfigParser.REMOVED_ATTR_ID);
        attrRemoved.setAttribute("unique", "no");
        attrRemoved.setAttribute("default", "false");
        attrRemoved.setAttribute("system", "yes");
        insertBeforeFirstChild(object, attrRemoved);
    }

    private void addIdAttribute(Element object) throws ConfigParserException {
        Element attrId = getObjectAttribute(object, "id");
        if (attrId == null) {
            attrId = object.getOwnerDocument().createElement("attr");
            attrId.setAttribute("id", "id");
            insertBeforeFirstChild(object, attrId);
        }
        attrId.setAttribute("id", "id");
        attrId.setAttribute("type", "id");
        attrId.setAttribute("label", "Id");
        attrId.setAttribute("unique", "yes");
        if (!attrId.hasAttribute("sequence")) {
            attrId.setAttribute("sequence", "default");
        }
    }

    private boolean isCorrectVidAttribute(Element vidAttr) {
        return VID_CLASS.equals(vidAttr.getAttribute(CALCULATE));
    }

    private void insertBeforeFirstChild(Element object, Element attrId) {
        if (object.hasChildNodes()) {
            object.insertBefore(attrId, object.getFirstChild());
        } else {
            object.appendChild(attrId);
        }
    }

    private Element getObjectAttribute(Element elem, String id) throws ConfigParserException {
        AttrFinder finder = new AttrFinder(id);
        try {
            XmlUtils.findAllElements(elem, (String) null, finder);
        } catch (ErrorCodeException e) {
            throw new ConfigParserException(e);
        }
        return finder.getAttr();
    }

    private void checkId(Element elem) throws ConfigParserException {
        if (getObjectAttribute(elem, "id") == null) throw new ConfigParserException("Missing 'id' object attribute: " + XmlUtils.toString(elem));
    }

    private void initElementLists() throws ConfigParserException {
        _objects = new HashMap<String, Element>();
        _baseObjects = new HashMap<String, Element>();
        NodeList list = _configElement.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i) {
            Element element = XmlUtils.checkIfElement(list.item(i));
            if (element == null) {
                continue;
            }
            if ("base".equals(element.getTagName())) {
                if (_baseElement == null) {
                    _baseElement = element;
                    initBaseElements();
                }
            } else if ("object".equals(element.getTagName())) {
                checkObject(element);
                hasIdAttribute(element);
                addCommonObject(element);
            } else if ("sequence".equals(element.getTagName())) {
                _sequences.put(element.getAttribute("id"), element);
            } else if ("filters".equals(element.getTagName())) {
                initFilters(element);
            } else if ("type".equals(element.getTagName())) {
                initType(element);
            } else if ("attr".equals(element.getTagName())) {
                hasIdAttribute(element);
                _commonAttrs.put(element.getAttribute("id"), element);
            }
        }
        addIDType(_baseElement.getAttribute("id-type").equals("string"));
        addCalculateType();
    }

    private void initBaseElements() throws ConfigParserException {
        NodeList list = _baseElement.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i) {
            Element element = XmlUtils.checkIfElement(list.item(i));
            if (element == null) {
                continue;
            }
            if ("object".equals(element.getTagName())) {
                addCommonObject(element);
                checkBaseObject(element);
                addPrerequiredAttributes(element);
                checkId(element);
                hasIdAttribute(element);
            } else if ("sequence".equals(element.getTagName())) {
                _sequences.put(element.getAttribute("id"), element);
            }
        }
    }

    private void checkObject(Element element) throws ConfigParserException {
        String id = element.getAttribute("id");
        if (_objects.containsKey(id)) {
            throw newConfigParserException("Object '" + id + "' defined more than once");
        }
        _objects.put(id, element);
    }

    private void checkBaseObject(Element element) throws ConfigParserException {
        String id = element.getAttribute("id");
        if (_baseObjects.containsKey(id)) {
            throw newConfigParserException("Object '" + id + "' defined more than once");
        }
        _baseObjects.put(id, element);
    }

    private void addPrerequiredAttributes(Element element) throws ConfigParserException {
        if (element.hasAttribute("system")) {
            checkId(element);
        } else {
            addObjectVersionAttribute(element);
            if ("yes".equals(_baseElement.getAttribute(ConfigParser.BASE_REMOVED_IDENT_ATTR))) {
                addObjectRemovedAttribute(element);
            }
            addVidAttribute(element);
            addIdAttribute(element);
        }
    }

    private class AttrVisitor implements XMLVisitor {

        private Element _object;

        private List<Element> _attrList;

        public AttrVisitor(Element object, boolean common) {
            _object = object;
            _attrList = new LinkedList<Element>();
        }

        public void visit(Element attr, int level) throws ErrorCodeException {
            if ("attr".equals(attr.getTagName())) {
                checkAttrId(attr);
                _attrList.add(attr);
                Element group = (Element) attr.getParentNode();
                if (group != null && group.getAttribute("occurs").equals("required")) {
                    attr.setAttribute("occurs", "required");
                }
            } else if ("view".equals(attr.getTagName())) {
                _object.setAttribute("view", "yes");
            }
        }

        public List<Element> getAttrList() {
            return _attrList;
        }
    }

    private class AttrFinder implements XMLVisitor {

        private Element _elem = null;

        private String _attrId;

        public AttrFinder(String attrId) {
            _attrId = attrId;
        }

        public void visit(Element attr, int level) throws ErrorCodeException {
            if (_elem != null) {
                return;
            }
            Element parent = (Element) attr.getParentNode();
            if (parent != null && !(parent.getTagName().equals("object") || parent.getTagName().equals("group"))) {
                return;
            }
            if ("attr".equals(attr.getTagName()) && _attrId.equals(attr.getAttribute("id"))) {
                _elem = attr;
            }
        }

        public Element getAttr() {
            return _elem;
        }
    }

    private void addCommonObject(Element element) {
        _commonObjects.put(element.getAttribute("id"), element);
    }

    private void createIndexElements() throws ConfigParserException {
        for (Element object : _baseObjects.values()) {
            if (object.hasAttribute("alias-to") || object.hasAttribute("view")) {
                continue;
            }
            createIndexForObject(object);
            List<String> attrNames = createAttrOrder(object);
            setAttrOrder(object, attrNames);
        }
    }

    private void setAttrOrder(Element objectElem, List<String> attrNames) throws ConfigParserException {
        Map<String, Element> attrs = new HashMap<String, Element>();
        List<Element> nodeList = getAttributes(objectElem);
        for (Element attr : nodeList) {
            String attrId = attr.getAttribute("id");
            attrs.put(attrId, (Element) objectElem.removeChild(attr));
        }
        for (String attrId : attrNames) {
            Element attr = (Element) attrs.get(attrId);
            if (attr == null) {
                throw new ConfigParserException("Unexpeceted error in sort of object attributes: object '" + objectElem.getAttribute("id") + "' does not contain attribute '" + attrId + "'");
            } else {
                objectElem.appendChild(attr);
                attrs.remove(attrId);
            }
        }
        if (attrs.size() != 0) {
            throw new ConfigParserException("Unexcepted error: there are not sorted attrs " + attrs.keySet());
        }
    }

    private List<String> createAttrOrder(Element objectElem) throws ConfigParserException {
        List<String> attrs = getAttrIds(objectElem);
        do {
            String parentId = objectElem.getAttribute("class");
            if (parentId.length() > 0) {
                objectElem = _commonObjects.get(parentId);
                if (objectElem == null) {
                    throw new ConfigParserException("Unexpected error: can't find " + parentId + " object");
                }
                List<String> parentAttrNames = getAttrIds(objectElem);
                for (ListIterator<String> i = parentAttrNames.listIterator(parentAttrNames.size()); i.hasPrevious(); ) {
                    String attrName = i.previous();
                    int idx = attrs.indexOf(attrName);
                    if (idx > 1) {
                        attrs.remove(idx);
                    }
                    attrs.add(0, attrName);
                }
            } else {
                objectElem = null;
            }
        } while (objectElem != null);
        return Collections.unmodifiableList(attrs);
    }

    private List<String> getAttrIds(Element object) throws ConfigParserException {
        if (_attrIds == null) {
            _attrIds = new HashMap<Element, List<String>>();
        }
        List<String> result = _attrIds.get(object);
        if (result == null) {
            result = new LinkedList<String>();
            for (Element element : getAttributes(object)) {
                result.add(element.getAttribute("id"));
            }
            _attrIds.put(object, result);
        }
        return result;
    }

    private void createIndexForObject(Element object) throws ConfigParserException {
        List<Element> list = getAttributes(object);
        for (Element attr : list) {
            if (attr.getParentNode() == object) {
                createIndexForAttr(object, attr);
            }
        }
        Element group = deepestGroup(object);
        while (group != null) {
            createIndexForGroup(object, group);
            group = deepestGroup(object);
        }
        deleteDoublesAndSetId(object);
    }

    private List<Element> getAttributes(Element object) throws ConfigParserException {
        AttrVisitor visitor = new AttrVisitor(object, true);
        try {
            XmlUtils.findAllElements(object, (String) null, visitor);
        } catch (ErrorCodeException e) {
            throw new ConfigParserException(e);
        }
        return visitor.getAttrList();
    }

    private class GroupChildrenVisitor implements XMLVisitor {

        Element _group;

        Node _parent;

        public GroupChildrenVisitor(Element group) {
            _group = group;
            _parent = _group.getParentNode();
        }

        public void visit(Element element, int level) throws ErrorCodeException {
            if (!"attrref".equals(element.getTagName())) _parent.insertBefore(element, _group);
        }
    }

    private void createIndexForGroup(Element object, Element group) throws ConfigParserException {
        boolean unique = isTrue(group, "unique");
        Element indexElem = object.getOwnerDocument().createElement("index");
        indexElem.setAttribute("unique", unique ? "yes" : "no");
        List<Element> attrs = XmlUtils.findChildElements(group, "attrref");
        List<String> ids = getAttrIds(object);
        for (Element attrref : attrs) {
            if (!ids.contains(attrref.getAttribute("id"))) throw new ConfigParserException("Unknown attrref in object '" + object.getAttribute("id") + "': " + XmlUtils.elementToString(attrref));
        }
        attrs.addAll(XmlUtils.findChildElements(group, "attr"));
        for (Element attr : attrs) {
            if (attr.hasAttribute(ObjectAttr.CALCULATE_ATTR_NAME)) {
                throw new ConfigParserException("Calculated attribute can't be grouped. " + XmlUtils.elementToString(attr));
            }
            createIndexForAttr(object, attr);
            Element attrElem = object.getOwnerDocument().createElement("index-attr");
            attrElem.setAttribute("id", attr.getAttribute("id"));
            indexElem.appendChild(attrElem);
        }
        object.appendChild(indexElem);
        try {
            XmlUtils.findChildElements(group, null, new GroupChildrenVisitor(group));
        } catch (ErrorCodeException e) {
            throw new UnreachableCodeReachedException(e);
        }
        group.getParentNode().removeChild(group);
    }

    private class IndexVisistor implements XMLVisitor {

        private HashMap<Integer, Element> hashCodes = new HashMap<Integer, Element>();

        private int no = 0;

        public void visit(Element index, int level) throws ErrorCodeException {
            Integer hashCode = XmlUtils.hashCode(index);
            if (hashCodes.containsKey(hashCode)) {
                index.getParentNode().removeChild(index);
            } else {
                hashCodes.put(hashCode, index);
                index.setAttribute("id", Integer.toString(++no));
            }
        }
    }

    private void deleteDoublesAndSetId(Element object) {
        try {
            XmlUtils.findChildElements(object, "index", new IndexVisistor());
        } catch (ErrorCodeException e) {
            throw new UnreachableCodeReachedException(e);
        }
    }

    private void createIndexForAttr(Element object, Element attr) throws ConfigParserException {
        boolean unique = isTrue(attr, "unique");
        boolean index = isTrue(attr, "index") || attr.hasAttribute("foreign");
        if (unique || index) {
            if (attr.hasAttribute(ObjectAttr.CALCULATE_ATTR_NAME)) {
                throw new ConfigParserException("Calculated attribute can't be indexed. " + XmlUtils.elementToString(attr));
            }
            Element indexElem = object.getOwnerDocument().createElement("index");
            Element attrElem = object.getOwnerDocument().createElement("index-attr");
            indexElem.setAttribute("unique", unique ? "yes" : "no");
            attrElem.setAttribute("id", attr.getAttribute("id"));
            indexElem.appendChild(attrElem);
            object.appendChild(indexElem);
        }
    }

    private class GroupsVisitor implements XMLVisitor {

        Element deepestGroup;

        int maxLevel = -1;

        public void visit(Element element, int level) throws ErrorCodeException {
            if (level > maxLevel) {
                deepestGroup = element;
                maxLevel = level;
            }
        }

        public Element getDeepestGroup() {
            return deepestGroup;
        }
    }

    private Element deepestGroup(Element object) throws ConfigParserException {
        GroupsVisitor visitor = new GroupsVisitor();
        try {
            XmlUtils.findAllElements(object, "group", visitor);
        } catch (ErrorCodeException e) {
            throw new ConfigParserException(e);
        }
        return visitor.getDeepestGroup();
    }

    /**
     * проверить boolean аттрибут
     */
    private boolean isTrue(Element elem, String attrName) throws ConfigParserException {
        boolean value = false;
        if (elem.hasAttribute(attrName)) {
            try {
                value = StringParser.toBoolean(elem.getAttribute(attrName));
            } catch (BadBooleanException e) {
                throw new ConfigParserException("Bad '" + attrName + "' attribute value: " + e.getMessage());
            }
        }
        return value;
    }
}
