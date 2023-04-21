package ru.adv.db.config;

import ru.adv.util.XmlUtils;
import ru.adv.util.XMLObject;
import ru.adv.util.StringCollectionComparator;
import ru.adv.db.base.MAttribute;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import java.util.*;

/**
 * Представление index
 * @version $Revision: 1.17 $
 */
public class ConfigIndex implements XMLObject, Comparable<ConfigIndex> {

    public static String INDEX_NAME_SEPARATOR = "-idx-";

    private String _name;

    private List<String> _columnNames;

    private boolean _unique;

    private Element _element;

    private boolean _primaryKey;

    private DBConfig _config;

    /**
	 * Constructor ConfigIndex
	 * Анализирует <code>indexElem</code>, после чего вызывает конструктор
	 */
    protected ConfigIndex(ConfigObject object, Element element) throws DBConfigException {
        _element = element;
        _config = object.getDBConfig();
        String indexName = "";
        List<String> columns = new ArrayList<String>();
        if (element.getAttribute("id").length() == 0) {
            throw new EmptyIndexIdException("Bad 'id' attribute for index element " + XmlUtils.toString(element), object.getName(), XmlUtils.toString(element), object.getDBConfig().getId());
        } else {
            indexName = object.getName() + INDEX_NAME_SEPARATOR + element.getAttribute("id");
        }
        boolean unique = element.getAttribute("unique").equals("yes");
        NodeList attrs = element.getElementsByTagName("index-attr");
        for (int i = 0; i < attrs.getLength(); i++) {
            columns.add(((Element) attrs.item(i)).getAttribute("id"));
        }
        if (columns.isEmpty()) {
            throw new EmptyIndexException("There are not 'attr-index' in index element '" + element.getTagName() + "'", object.getName(), indexName, object.getDBConfig().getId());
        }
        init(indexName, columns, unique);
    }

    /**
	 * Constructor ConfigIndex
	 */
    public ConfigIndex(String name, List<String> columnNames, boolean unique) throws DBConfigException {
        init(name, columnNames, unique);
    }

    /**
	 * get name of index
	 * @return name
	 */
    public String getName() {
        return _name;
    }

    /**
	 * get List with column names
	 * @return names of columns
	 */
    public List<String> getColumns() {
        return Collections.unmodifiableList(_columnNames);
    }

    /**
	 * Is index unique
	 * @return unique
	 */
    public boolean isUnique() {
        return _unique;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("Index ");
        str.append(_name);
        str.append(" unique=" + _unique);
        str.append(" attrs=" + _columnNames);
        return str.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigIndex)) return false;
        final ConfigIndex configIndex = (ConfigIndex) o;
        if (_unique != configIndex._unique) return false;
        if (!_columnNames.equals(configIndex._columnNames)) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = _columnNames.hashCode();
        result = 29 * result + (_unique ? 1 : 0);
        return result;
    }

    private void init(String name, List<String> columnNames, boolean unique) throws DBConfigException {
        _name = name;
        _columnNames = new ArrayList<String>(columnNames.size());
        for (String attrName : columnNames) {
            if (_config != null) {
                _config.isPermitNameForIdentify(attrName);
            } else {
                DBConfig.isPermitNameForIdentify(attrName, null);
            }
            _columnNames.add(attrName);
        }
        _unique = unique;
        _primaryKey = _unique && _columnNames.size() == 1 && _columnNames.get(0).toString().equals("id");
    }

    public Element toXML(Document doc) {
        return (Element) doc.importNode(_element, true);
    }

    public int compareTo(ConfigIndex o) {
        if (this == o) return 0;
        if (!(o instanceof ConfigIndex)) return -1;
        final ConfigIndex configIndex = (ConfigIndex) o;
        int result = StringCollectionComparator.INSTANCE.compare(_columnNames, configIndex._columnNames);
        if (result == 0) {
            if (_unique && !configIndex._unique) {
                return 1;
            } else if (!_unique && configIndex._unique) {
                return -1;
            }
        }
        return result;
    }

    public boolean isPrimaryKey() {
        return _primaryKey;
    }

    /**
	 * Returns true if serachAttrs contains all attributes with same names as this index columns
	 * @param searchAttrs list of MAttribute or list of ObjectAttr or list of column names
	 */
    public boolean matchColumns(List<Object> searchAttrs) {
        boolean result = false;
        if (searchAttrs != null) {
            int size = 0;
            List<String> columns = getColumns();
            for (Object o : searchAttrs) {
                String attribute;
                if (o instanceof MAttribute) {
                    attribute = ((MAttribute) o).getName();
                } else if (o instanceof ObjectAttr) {
                    attribute = ((ObjectAttr) o).getName();
                } else {
                    attribute = o.toString();
                }
                if (columns.contains(attribute)) {
                    ++size;
                }
            }
            result = size == columns.size();
        }
        return result;
    }
}
