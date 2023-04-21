package net.sourceforge.dbsa.structure;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import net.sourceforge.dbsa.common.UserCancelledException;
import net.sourceforge.dbsa.structure.DBDifference.DBEntityDifference;
import net.sourceforge.dbsa.structure.DBDifference.DBEntityDifferenceMissing;
import net.sourceforge.dbsa.structure.DBDifference.DBEntityDifferenceOrder;
import net.sourceforge.dbsa.structure.DBDifference.DBEntityDifferenceProp;
import net.sourceforge.dbsa.structure.DBDifference.DBEntityDifferenceRename;
import net.sourceforge.dbsa.structure.DBDifference.DBEntityMatch;
import net.sourceforge.dbsa.structure.DBFormat.Schema.Table.ForeignKey.ForeignKeyColumn;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Class containing static methods and static classes
 * @author Oriol Alcaraz
 */
public class DBFormat {

    private static final String XMLTAG_IDXCOL = "INDEX_COLUMN";

    private static final String XMLTAG_SCH = "SCHEMA";

    private static final String XMLTAG_TBL = "TABLE";

    private static final String XMLTAG_COL = "COLUMN";

    private static final String XMLTAG_IDX = "INDEX";

    private static final String XMLTAG_FGK = "FK";

    private static final String XMLTAG_FKCOL = "FK_COLUMN";

    private static final String XMLTAG_PKCOL = "PK_COLUMN";

    private static final String XMLATR_NAME = "NAME";

    private static final String XMLATR_POS = "POSITION";

    private static final String XMLATR_DFLT = "DEFAULT";

    private static final String XMLATR_NULL = "NULL";

    private static final String XMLATR_DEC = "DECIMALS";

    private static final String XMLATR_SIZE = "SIZE";

    private static final String XMLATR_TYPE = "TYPE";

    private static final String XMLATR_PRI = "PRIMARY";

    private static final String XMLATR_PKCOL = "PKCOLUMN";

    private static final String XMLATR_PKTBL = "PKTABLE";

    private static final String XMLATR_CATALOG = "CATALOG_NAME";

    private static final String XMLATR_SCHEMA = "SCHEMA_NAME";

    private static final String XMLATR_ASC = "ASCDESC";

    private static final String XMLATR_UNI = "UNIQUE";

    /**
	 * Attributes of database objects. Each instance represents an
	 * attribute of one or more database entity classes. Attributes
	 * have name (its enum name), type, and default value. For an
	 * object of a database, the value for certain attribute can
	 * be evaluated using {@link DBFormat.DBEntity#getAttributeValue(net.sourceforge.dbsa.structure.DBFormat.DBEntityAttribute)}
	 * @author Oriol Alcaraz
	 */
    public static enum DBEntityAttribute {

        NAME(DBAttributeType.STRING, null), ORDINAL_POSITION(DBAttributeType.INTEGER, null), DEFAULT_VALUE(DBAttributeType.STRING, null), NULL_ALLOWED(DBAttributeType.YESNO, Boolean.FALSE), SIZE(DBAttributeType.INTEGER, new Integer(0)), DECIMALS(DBAttributeType.INTEGER, new Integer(0)), TYPE(DBAttributeType.STRING, null), PRIMARY(DBAttributeType.INTEGER, new Integer(0)), UNIQUE(DBAttributeType.YESNO, Boolean.FALSE), ASCDESC(DBAttributeType.CHAR, new Character(' ')), PKTABLE(DBAttributeType.STRING, null), PKCOLUMN(DBAttributeType.STRING, null), CATALOG_NAME(DBAttributeType.STRING, null), SCHEMA_NAME(DBAttributeType.STRING, null);

        public final DBAttributeType type;

        public final Object defaultValue;

        private DBEntityAttribute(DBAttributeType _type, Object _defaultValue) {
            type = _type;
            defaultValue = _defaultValue;
        }

        public Object getAttributeValue(DBEntity _p) {
            return _p.getAttributeValue(this);
        }

        public String getAttributeValueString(DBEntity _p) {
            return type.valToString(_p.getAttributeValue(this));
        }
    }

    /**
	 * Convenience Enum for classes extending {@link DBEntity}. 
	 * Methods depending of the class but not the object are grouped here.
	 * @author Oriol Alcaraz
	 */
    public static enum DBEntityType {

        SCHEMA(XMLTAG_SCH, true, DBEntityAttribute.NAME, DBEntityAttribute.CATALOG_NAME, DBEntityAttribute.SCHEMA_NAME), TABLE(XMLTAG_TBL, true, DBEntityAttribute.NAME), COLUMN(XMLTAG_COL, false, DBEntityAttribute.NAME, DBEntityAttribute.ORDINAL_POSITION, DBEntityAttribute.SIZE, DBEntityAttribute.DECIMALS, DBEntityAttribute.TYPE, DBEntityAttribute.DEFAULT_VALUE, DBEntityAttribute.NULL_ALLOWED, DBEntityAttribute.PRIMARY), INDEX(XMLTAG_IDX, true, DBEntityAttribute.NAME, DBEntityAttribute.UNIQUE), FK(XMLTAG_FGK, true, DBEntityAttribute.NAME), INDEX_COLUMN(XMLTAG_IDXCOL, false, DBEntityAttribute.NAME, DBEntityAttribute.ORDINAL_POSITION, DBEntityAttribute.ASCDESC), FK_COLUMN(XMLTAG_FKCOL, false, DBEntityAttribute.NAME, DBEntityAttribute.ORDINAL_POSITION, DBEntityAttribute.PKCOLUMN, DBEntityAttribute.PKTABLE), PK_COLUMN(XMLTAG_PKCOL, false, DBEntityAttribute.NAME, DBEntityAttribute.ORDINAL_POSITION);

        private final String xmlTag;

        private final boolean bIsContainer;

        private final EnumSet<DBEntityAttribute> attributes;

        private DBEntityType(String _XMLTag, boolean _container, DBEntityAttribute... _attributes) {
            xmlTag = _XMLTag;
            bIsContainer = _container;
            attributes = EnumSet.copyOf(Arrays.asList(_attributes));
        }

        public boolean isContainer() {
            return bIsContainer;
        }

        public String xml() {
            return xmlTag;
        }

        public boolean hasAttribute(DBEntityAttribute attr) {
            return attributes.contains(attr);
        }

        public String single() {
            switch(this) {
                case COLUMN:
                    return "Column";
                case FK:
                    return "Foreign key";
                case FK_COLUMN:
                    return "Foreign key column reference";
                case INDEX:
                    return "Index";
                case INDEX_COLUMN:
                    return "Index column reference";
                case PK_COLUMN:
                    return "Primary key";
                case SCHEMA:
                    return "Schema";
                case TABLE:
                    return "Table";
                default:
                    return null;
            }
        }

        public String plural() {
            switch(this) {
                case COLUMN:
                    return "Columns";
                case FK:
                    return "Foreign keys";
                case FK_COLUMN:
                    return "Foreign key column references";
                case INDEX:
                    return "Indexes";
                case INDEX_COLUMN:
                    return "Index column references";
                case PK_COLUMN:
                    return "Primary keys";
                case SCHEMA:
                    return "Schemas";
                case TABLE:
                    return "Tables";
                default:
                    return null;
            }
        }
    }

    /**
	 * Types of atribute values (string, int,...).
	 * @author Oriol Alcaraz
	 *
	 */
    public static enum DBAttributeType {

        STRING {

            @Override
            public Object stringToVal(String value) {
                return value;
            }
        }
        , CHAR {

            @Override
            public Object stringToVal(String value) {
                return new Character((value == null || value.length() == 0) ? ' ' : value.charAt(0));
            }
        }
        , INTEGER {

            @Override
            public Object stringToVal(String value) {
                return new Integer(value == null ? 0 : Integer.parseInt(value));
            }
        }
        , YESNO {

            @Override
            public Object stringToVal(String value) {
                return new Boolean("YES".equals(value));
            }

            @Override
            public String valToString(Object value) {
                return (value != null && ((Boolean) value).booleanValue()) ? "YES" : "NO";
            }
        }
        ;

        public abstract Object stringToVal(String _attrValue);

        public String valToString(Object value) {
            return value == null ? null : value.toString();
        }

        public String valToString(Object value, Object defaultValue) {
            if (defaultValue == null) return valToString(value);
            return (defaultValue.equals(value) ? null : valToString(value));
        }

        public Object stringToVal(String value, Object defaultValue) {
            Object o = stringToVal(value);
            return (defaultValue != null && defaultValue.equals(o)) ? null : o;
        }
    }

    /**
	 * Exception generated when an evaluation of an atribute for a database
	 * entity object 
	 * @author Oriol Alcaraz
	 */
    public static class DBInvalidAttributeException extends RuntimeException {

        private final DBEntityType entityType;

        private final DBEntityAttribute attribute;

        public DBInvalidAttributeException(DBEntityType _entityType, DBEntityAttribute _attribute) {
            super("Attribute \"".concat(_attribute.name()).concat("\" does not apply to entity type \"").concat(_entityType.name()).concat("\""));
            attribute = _attribute;
            entityType = _entityType;
        }

        public final DBEntityType getEntityType() {
            return entityType;
        }

        public final DBEntityAttribute getAttribute() {
            return attribute;
        }
    }

    public abstract static class DBEntity {

        public final String name;

        private final EnumSet<DBEntityType> containingTypes;

        protected DBEntity xmlCurrentTag = null;

        protected DBEntityMatch match = null;

        public abstract DBEntityType getType();

        public abstract Object getAttributeValue(DBEntityAttribute _attr);

        public abstract void findDifferences(DBEntity _o, Collection<DBEntityDifference> differences);

        protected DBEntity(String _name) {
            name = _name;
            final DBEntityType t = getType();
            containingTypes = EnumSet.of(t);
            DBEntity cont = container();
            while (cont != null && !cont.containingTypes.contains(t)) {
                cont.containingTypes.add(t);
                cont = cont.container();
            }
        }

        protected boolean isContainer() {
            return true;
        }

        protected final String getXMLTag() {
            return getType().xml();
        }

        /**
		 * 
		 * @return Returns the direct container of the object. For the topmost object (schema) it returns null.
		 */
        public abstract DBEntity container();

        public String sqlCreateStatement() {
            return null;
        }

        public String sqlDropStatement() {
            return null;
        }

        public DBEntity sqlRebuildAncestor() {
            return null;
        }

        /**
		 * Check the presence of an object type in the hierarchy of contained objects. 
		 * @param _t
		 * @return true if there is an object having this type in the hierarchy 
		 */
        public boolean containsType(DBEntityType _t) {
            return containingTypes.contains(_t);
        }

        public DBEntityMatch getInnerContainerMatch() {
            DBEntityMatch retVal = null;
            DBEntity o = this;
            while (o != null && o.match == null) {
                o = o.container();
            }
            if (o != null) retVal = o.match;
            return retVal;
        }

        public DBEntity getContainerByType(DBEntityType _t) {
            DBEntity retVal = this;
            while (retVal != null && retVal.getType() != _t) retVal = retVal.container();
            return retVal;
        }

        protected void xmlProp(Hashtable<String, String> _k) {
            _k.put(XMLATR_NAME, this.name);
        }

        public DBEntity[] getContent() {
            return null;
        }

        protected boolean xmlOpenTag(String _tag, Attributes att) throws UserCancelledException {
            if (isContainer() && xmlCurrentTag != null) {
                return xmlCurrentTag.xmlOpenTag(_tag, att);
            }
            return false;
        }

        protected DBEntity xmlCloseTag(String _tag) {
            if (isContainer() && xmlCurrentTag != null) {
                xmlCurrentTag = xmlCurrentTag.xmlCloseTag(_tag);
            } else if (_tag.equals(getXMLTag())) {
                return null;
            }
            return this;
        }

        protected void writeToXML(Writer _b) throws IOException {
            Hashtable<String, String> k = new Hashtable<String, String>();
            xmlProp(k);
            _b.append("<").append(getXMLTag());
            for (Enumeration<String> e = k.keys(); e.hasMoreElements(); ) {
                String key = e.nextElement();
                _b.append(" ").append(key).append("=\"").append(k.get(key).replaceAll("\\\"", "\\\"")).append("\"");
            }
            if (isContainer()) {
                _b.append(">");
                _b.append("\n");
                DBEntity[] vContent = getContent();
                if (vContent != null) {
                    int i;
                    for (i = 0; i < vContent.length; i++) {
                        vContent[i].writeToXML(_b);
                    }
                }
                _b.append("</").append(getXMLTag()).append(">\n");
            } else {
                _b.append("/>\n");
            }
        }

        /**
		 * <p>Test if this object matches another. Two objects match when their identification into their respective containers are the same: Two tables with
		 * the same name, two columns with the same name, two index column references with the same ordinal position,...<ul>
		 * <li>a.matchingEntity(a)</li>
		 * <li>a.matchingEntity(b) implies b.matchingEntity(a)</li>
		 * <li>a.matchingEntity(b) && b.matchingEntity(c) implies a.matchingEntity(c)</li>
		 * </ul>
		 * <p>This equivalence relation is wider than equals:
		 * <ul><li>a.equals(b) implies a.matchingEntity(b)</li></ul>
		 * <p>and is compatible with getType:
		 * <ul><li>a.matchingEntity(b) implies a.getType()==b.getType()</li></ul>
		 * @param _o
		 * @return true if this object and _o are considered functionally equivalents
		 */
        public boolean matchingEntity(DBEntity _o) {
            return (_o != null) && (getType() == _o.getType()) && name.equals(_o.name);
        }

        /**
		 * <p>Test if the object is functionally equivalent to other object. Its internal semantics is similar to that of equals(Object) method:<ul>
		 * <li>a.functionallyEquivalent(a)</li>
		 * <li>a.functionallyEquivalent(b) implies b.functionallyEquivalent(a)</li>
		 * <li>a.functionallyEquivalent(b) && b.funcitonallyEquivalent(c) implies a.functionallyEquivalent(c)</li>
		 * </ul>
		 * <p>This equivalence relation is wider than matchingEntity:
		 * <ul><li>a.matchingEntity(b) implies a.functionallyEquivalent(b)</li></ul>
		 * and it is used for comparing entities for which the name or position into its container is not significant.</p>
		 * <p>Like matchingEntity, it is compatible with getType:
		 * <ul><li>a.functionallyEquivalent(b) implies a.getType()==b.getType()</li></ul>
		 * @param _o
		 * @return true if this object and _o are considered functionally equivalents
		 */
        public boolean functionallyEquivalent(DBEntity _o) {
            return matchingEntity(_o);
        }

        public String getRelativeName(DBEntity _from) {
            if (_from == this) return "";
            if (_from == container()) return name;
            return (container().getRelativeName(_from) + "." + name);
        }

        public String getQualifiedName() {
            DBEntity ctn = container();
            if (ctn == null) return name;
            return ctn.getQualifiedName() + "." + name;
        }

        public DBEntityMatch getMatch() {
            return match;
        }

        @Override
        public String toString() {
            return getType().name() + " : " + name;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof DBEntity)) return false;
            final DBEntity ent = (DBEntity) o;
            return name.equals(ent.name) && (container() == ent.container() || (container() != null && container().equals(ent.container())));
        }

        @Override
        public int hashCode() {
            return Tools.hash(name, container());
        }
    }

    public abstract static class DBPositionalEntity extends DBEntity {

        protected final int ordinalPosition;

        protected DBPositionalEntity(String _name, int _ordinalPos) {
            super(_name);
            this.ordinalPosition = _ordinalPos;
        }

        @Override
        public void findDifferences(DBEntity _o, Collection<DBEntityDifference> differences) {
            if (_o == null) throw new NullPointerException();
            if (!(_o instanceof DBPositionalEntity)) throw new IllegalArgumentException();
            if (((DBPositionalEntity) _o).ordinalPosition != ordinalPosition) differences.add(new DBEntityDifferenceOrder(match));
        }

        @Override
        protected void xmlProp(Hashtable<String, String> _k) {
            super.xmlProp(_k);
            _k.put(XMLATR_POS, Integer.toString(ordinalPosition));
        }
    }

    public static class Schema extends DBEntity {

        private final Hashtable<String, Table> tables;

        protected final String jdbcSchemaName;

        protected final String jdbcCatalogName;

        private Progression loadMonitor;

        @Override
        public DBEntity container() {
            return null;
        }

        public Schema(String _catalog, String _schema, Progression _loadMonitor) {
            super(buildSchemaName(_catalog, _schema));
            jdbcCatalogName = _catalog;
            jdbcSchemaName = _schema;
            tables = new Hashtable<String, Table>();
            loadMonitor = _loadMonitor;
        }

        public final String getSchemaName() {
            return jdbcSchemaName;
        }

        public final String getCatalogName() {
            return jdbcCatalogName;
        }

        public final int getTableCount() {
            return tables.size();
        }

        public final Table.Column getColumn(String _tableName, String _columnName) {
            if (tables.containsKey(_tableName)) return tables.get(_tableName).getColumn(_columnName);
            return null;
        }

        @Override
        public final DBEntityType getType() {
            return DBEntityType.SCHEMA;
        }

        @Override
        public Object getAttributeValue(DBEntityAttribute _attr) {
            switch(_attr) {
                case NAME:
                    return name;
                case CATALOG_NAME:
                    return jdbcCatalogName;
                case SCHEMA_NAME:
                    return jdbcSchemaName;
                default:
                    throw new DBInvalidAttributeException(getType(), _attr);
            }
        }

        @Override
        protected void xmlProp(Hashtable<String, String> _k) {
            super.xmlProp(_k);
            if (jdbcSchemaName != null && jdbcSchemaName.length() > 0) _k.put(XMLATR_SCHEMA, jdbcSchemaName);
            if (jdbcCatalogName != null && jdbcCatalogName.length() > 0) _k.put(XMLATR_CATALOG, jdbcCatalogName);
        }

        @Override
        public void findDifferences(DBEntity _o, Collection<DBEntityDifference> differences) {
            if (_o == null) throw new NullPointerException();
            if (!(_o instanceof Schema)) throw new IllegalArgumentException();
            DBFormat.processHashtableDifferences(this.tables, ((Schema) _o).tables, differences, false);
        }

        protected void readFromDB(Connection _connexionJDBC, Progression _loadMonitor) throws SQLException, UserCancelledException {
            Table t;
            if (_loadMonitor != null) {
                _loadMonitor.setValue(0);
                _loadMonitor.setMax(2);
                _loadMonitor.setCurrentItemDescription("Scanning tables...");
            }
            ResultSet rsTable = _connexionJDBC.getMetaData().getTables(jdbcCatalogName, jdbcSchemaName, null, new String[] { "TABLE" });
            while (rsTable.next()) {
                t = new Table(rsTable.getString("TABLE_NAME"));
                tables.put(t.name, t);
            }
            rsTable.close();
            if (_loadMonitor != null) {
                _loadMonitor.setMax(1 + tables.size());
                _loadMonitor.setValue(1);
            }
            String sTableCount = "/" + Integer.toString(tables.size()) + ": ";
            int i = 0;
            DatabaseMetaData md = _connexionJDBC.getMetaData();
            for (Table tb : tables.values()) {
                tb.readFromDB(md);
                if (_loadMonitor != null) {
                    i++;
                    _loadMonitor.incValue();
                    _loadMonitor.setCurrentItemDescription("Table " + Integer.toString(i) + sTableCount + tb.name);
                    sleep();
                }
            }
            rebuildReferences();
        }

        @Override
        public DBEntity[] getContent() {
            return tables.values().toArray(new DBEntity[tables.size()]);
        }

        @Override
        protected boolean xmlOpenTag(String _tag, Attributes att) throws UserCancelledException {
            if (!super.xmlOpenTag(_tag, att)) if (_tag.equals(XMLTAG_TBL)) {
                Table t = new Table(att.getValue(XMLATR_NAME));
                tables.put(t.name, t);
                xmlCurrentTag = t;
                if (loadMonitor != null) loadMonitor.incValue();
                sleep();
            }
            return false;
        }

        private final synchronized void sleep() {
        }

        @Override
        protected DBEntity xmlCloseTag(String _tag) {
            if (getType().xml().equals(_tag)) {
                rebuildReferences();
            }
            return super.xmlCloseTag(_tag);
        }

        protected void rebuildReferences() {
            for (Table tbl : tables.values()) {
                tbl.rebuildReferences();
            }
        }

        public class Table extends DBEntity {

            public final Hashtable<String, Index> indexes;

            public final Hashtable<String, Column> columns;

            public final Hashtable<String, ForeignKey> foreignKeys;

            public final Hashtable<String, PrimaryKeyColumn> primaryKeyColumns;

            private final Hashtable<String, ForeignKey> foreignKeyReferences;

            public Table(String _name) {
                super(_name);
                indexes = new Hashtable<String, Index>();
                columns = new Hashtable<String, Column>();
                foreignKeys = new Hashtable<String, ForeignKey>();
                foreignKeyReferences = new Hashtable<String, ForeignKey>();
                primaryKeyColumns = new Hashtable<String, PrimaryKeyColumn>();
            }

            @Override
            public String sqlCreateStatement() {
                StringBuffer sb = new StringBuffer();
                sb.append("CREATE TABLE ").append(name).append(" (");
                int i = 0;
                for (String columnName : columns.keySet()) {
                    if (i > 0) sb.append(",\n   ");
                    sb.append(columnName).append(' ').append(columns.get(columnName).sqlColumnDefinition());
                    i++;
                }
                for (String idxName : indexes.keySet()) {
                    sb.append(",\n   ");
                    sb.append(indexes.get(idxName).sqlDefinitionClause());
                }
                if (primaryKeyColumns.size() > 0) {
                    sb.append(",\n   ");
                    sb.append(sqlPKDefinition());
                }
                sb.append(")");
                return sb.toString();
            }

            @Override
            public String sqlDropStatement() {
                return new StringBuffer().append("DROP TABLE ").append(Table.this.name).toString();
            }

            public String sqlDropPKStatement() {
                return new StringBuffer().append("ALTER TABLE ").append(Table.this.name).append(" DROP PRIMARY KEY").toString();
            }

            public String sqlAddPKStatement() {
                StringBuffer sb = new StringBuffer().append("ALTER TABLE ").append(Table.this.name).append(" ADD ").append(sqlPKDefinition());
                return sb.toString();
            }

            public String sqlPKDefinition() {
                StringBuffer sb = new StringBuffer();
                sb.append("PRIMARY KEY (");
                int i = 0;
                PrimaryKeyColumn[] positions = primaryKeyColumns.values().toArray(new PrimaryKeyColumn[primaryKeyColumns.size()]);
                Arrays.sort(positions, dbeComparator);
                for (PrimaryKeyColumn col : positions) {
                    if (i > 0) sb.append(",");
                    sb.append(col.name);
                    i++;
                }
                sb.append(")");
                return sb.toString();
            }

            public void addForeignKeyReference(ForeignKey _fk) {
                String sKey = _fk.container().name + "." + _fk.name;
                Table.this.foreignKeyReferences.put(sKey, _fk);
            }

            public final Column getColumn(String _name) {
                if (columns.containsKey(_name)) return columns.get(_name);
                return null;
            }

            protected void rebuildReferences() {
                for (ForeignKey fk : foreignKeys.values()) {
                    fk.rebuildColumnReferences();
                }
            }

            @Override
            public final DBEntityType getType() {
                return DBEntityType.TABLE;
            }

            @Override
            public Object getAttributeValue(DBEntityAttribute _attr) {
                switch(_attr) {
                    case NAME:
                        return name;
                    default:
                        throw new DBInvalidAttributeException(getType(), _attr);
                }
            }

            @Override
            public DBEntity container() {
                return Schema.this;
            }

            @Override
            public void findDifferences(DBEntity _o, Collection<DBEntityDifference> differences) {
                if (_o == null) throw new NullPointerException();
                if (!(_o instanceof Table)) throw new IllegalArgumentException();
                final Table o = (Table) _o;
                DBFormat.processListDifferences(this.columns.values(), o.columns.values(), differences);
                DBFormat.processHashtableDifferences(this.indexes, o.indexes, differences, true);
                DBFormat.processHashtableDifferences(this.foreignKeys, o.foreignKeys, differences, true);
                DBFormat.processHashtableDifferences(this.primaryKeyColumns, o.primaryKeyColumns, differences, false);
            }

            public boolean samePrimaryKey(Table _table) {
                boolean bRetVal;
                bRetVal = (this.primaryKeyColumns.size() == _table.primaryKeyColumns.size());
                for (String sKey : _table.primaryKeyColumns.keySet()) {
                    if (!primaryKeyColumns.containsKey(sKey)) {
                        bRetVal = false;
                        break;
                    }
                }
                return bRetVal;
            }

            @Override
            public DBEntity[] getContent() {
                ArrayList<DBEntity> c;
                c = new ArrayList<DBEntity>(columns.values());
                c.addAll(indexes.values());
                c.addAll(foreignKeys.values());
                return c.toArray(new DBEntity[c.size()]);
            }

            @Override
            protected boolean xmlOpenTag(String _tag, Attributes att) throws UserCancelledException {
                if (!super.xmlOpenTag(_tag, att)) {
                    if (_tag.equals(XMLTAG_COL)) {
                        Column c = new Column(att.getValue(XMLATR_NAME), Integer.parseInt(att.getValue(XMLATR_POS)), att.getValue(XMLATR_TYPE), Integer.parseInt(att.getValue(XMLATR_SIZE)), Integer.parseInt(att.getValue(XMLATR_DEC)), att.getIndex(XMLATR_NULL) >= 0, att.getIndex(XMLATR_DFLT) < 0 ? null : att.getValue(XMLATR_DFLT));
                        columns.put(c.name, c);
                        xmlCurrentTag = c;
                        if (att.getIndex(XMLATR_PRI) >= 0) {
                            int pos = -1;
                            try {
                                pos = Integer.parseInt(att.getValue(XMLATR_PRI));
                            } catch (NumberFormatException e) {
                                pos = -1;
                            }
                            c.primary = pos;
                            primaryKeyColumns.put(c.name, new PrimaryKeyColumn(c.name, c, pos));
                        }
                    } else if (_tag.equals(XMLTAG_IDX)) {
                        Index in = new Index(att.getValue(XMLATR_NAME), att.getIndex(XMLATR_UNI) >= 0);
                        indexes.put(in.name, in);
                        xmlCurrentTag = in;
                    } else if (_tag.equals(XMLTAG_FGK)) {
                        ForeignKey fKey = new ForeignKey(att.getValue(XMLATR_NAME));
                        foreignKeys.put(fKey.name, fKey);
                        xmlCurrentTag = fKey;
                    }
                }
                return false;
            }

            public void readFromDB(DatabaseMetaData _metaData) throws SQLException {
                Index ind = null;
                ForeignKey fKey = null;
                ResultSet rsKey;
                rsKey = _metaData.getIndexInfo(jdbcCatalogName, jdbcSchemaName, name, false, true);
                while (rsKey.next()) {
                    String sKey = rsKey.getString("INDEX_NAME");
                    if (sKey != null && rsKey.getShort("TYPE") != DatabaseMetaData.tableIndexStatistic) {
                        if (!indexes.containsKey(sKey)) {
                            ind = new Index(sKey, !rsKey.getBoolean("NON_UNIQUE"));
                            indexes.put(ind.name, ind);
                        } else {
                            ind = indexes.get(sKey);
                        }
                        ind.addColumn(rsKey.getString("COLUMN_NAME"), rsKey.getShort("ORDINAL_POSITION"), rsKey.getString("ASC_OR_DESC"));
                    }
                }
                rsKey.close();
                rsKey = _metaData.getImportedKeys(jdbcCatalogName, jdbcSchemaName, name);
                while (rsKey.next()) {
                    String sKey = rsKey.getString("FK_NAME");
                    if (sKey != null) {
                        if (!foreignKeys.containsKey(sKey)) {
                            fKey = new ForeignKey(sKey);
                            foreignKeys.put(fKey.name, fKey);
                        } else {
                            fKey = foreignKeys.get(sKey);
                        }
                        fKey.addColumn(rsKey.getString("FKCOLUMN_NAME"), rsKey.getShort("KEY_SEQ"), rsKey.getString("PKTABLE_NAME"), rsKey.getString("PKCOLUMN_NAME"));
                    }
                }
                rsKey.close();
                if (columns.size() == 0) {
                    ResultSet rsColumn = _metaData.getColumns(jdbcCatalogName, jdbcSchemaName, name, null);
                    while (rsColumn.next()) {
                        String sColumn = rsColumn.getString("COLUMN_NAME");
                        Table.Column c = new Column(sColumn, rsColumn.getInt("ORDINAL_POSITION"), rsColumn.getString("TYPE_NAME"), rsColumn.getInt("COLUMN_SIZE"), rsColumn.getInt("DECIMAL_DIGITS"), rsColumn.getInt("NULLABLE") != DatabaseMetaData.columnNoNulls, rsColumn.getString("COLUMN_DEF"));
                        columns.put(sColumn, c);
                    }
                }
                rsKey = _metaData.getPrimaryKeys(jdbcCatalogName, jdbcSchemaName, name);
                while (rsKey.next()) {
                    String sKey = rsKey.getString("COLUMN_NAME");
                    if (sKey != null) {
                        if (columns.containsKey(sKey)) {
                            Column xColumn = columns.get(sKey);
                            xColumn.primary = rsKey.getInt("KEY_SEQ");
                            primaryKeyColumns.put(sKey, new PrimaryKeyColumn(sKey, xColumn, rsKey.getInt("KEY_SEQ")));
                        }
                    }
                }
                rsKey.close();
            }

            public class Column extends DBPositionalEntity {

                private final String typeName;

                private final int size;

                private final int decimals;

                private final boolean nullable;

                private final String defaultValue;

                protected int primary;

                private final ArrayList<ForeignKeyColumn> foreignKeyColumnReferences;

                protected Column(String _name, int _ordinalPosition, String _typeName, int _size, int _decimals, boolean _nullable, String _defaultValue) {
                    super(_name, _ordinalPosition);
                    typeName = _typeName;
                    size = _size;
                    decimals = _decimals;
                    nullable = _nullable;
                    defaultValue = _defaultValue;
                    foreignKeyColumnReferences = new ArrayList<ForeignKeyColumn>();
                    primary = 0;
                }

                public final boolean isPrimary() {
                    return primary > 0;
                }

                public String sqlColumnDefinition() {
                    StringBuffer sb = new StringBuffer();
                    sb.append(typeName);
                    if (size > 0) {
                        sb.append("(").append(size);
                        if (decimals > 0) sb.append(",").append(decimals);
                        sb.append(")");
                    }
                    if (!nullable) sb.append(" NOT NULL");
                    if (defaultValue != null) {
                        sb.append(" DEFAULT ").append(sqlParseValue(defaultValue));
                    }
                    return sb.toString();
                }

                @Override
                public String sqlCreateStatement() {
                    StringBuffer sb = new StringBuffer().append("ALTER TABLE ").append(Table.this.name).append(" ADD COLUMN ").append(Column.this.name).append(' ').append(sqlColumnDefinition());
                    return sb.toString();
                }

                @Override
                public String sqlDropStatement() {
                    return new StringBuffer().append("ALTER TABLE ").append(Table.this.name).append(" DROP COLUMN ").append(Column.this.name).toString();
                }

                public String sqlRedefineStatement() {
                    return new StringBuffer().append("ALTER TABLE ").append(Table.this.name).append(" MODIFY COLUMN ").append(Column.this.name).append(' ').append(sqlColumnDefinition()).toString();
                }

                public void addForeignKeyColumnReference(ForeignKeyColumn _fk) {
                    if (!foreignKeyColumnReferences.contains(_fk)) {
                        foreignKeyColumnReferences.add(_fk);
                        ForeignKey fk = (ForeignKey) _fk.container();
                        Table.this.addForeignKeyReference(fk);
                    }
                }

                @Override
                public Object getAttributeValue(DBEntityAttribute _attr) {
                    switch(_attr) {
                        case NAME:
                            return name;
                        case ORDINAL_POSITION:
                            return new Integer(ordinalPosition);
                        case TYPE:
                            return typeName;
                        case SIZE:
                            return new Integer(size);
                        case DECIMALS:
                            return new Integer(decimals);
                        case NULL_ALLOWED:
                            return new Boolean(nullable);
                        case DEFAULT_VALUE:
                            return defaultValue;
                        case PRIMARY:
                            return new Integer(primary);
                        default:
                            throw new DBInvalidAttributeException(getType(), _attr);
                    }
                }

                @Override
                public final DBEntityType getType() {
                    return DBEntityType.COLUMN;
                }

                @Override
                public DBEntity container() {
                    return Table.this;
                }

                @Override
                public boolean isContainer() {
                    return false;
                }

                @Override
                protected void xmlProp(Hashtable<String, String> _k) {
                    super.xmlProp(_k);
                    _k.put(XMLATR_TYPE, typeName);
                    _k.put(XMLATR_SIZE, Integer.toString(size));
                    _k.put(XMLATR_DEC, Integer.toString(decimals));
                    if (nullable) _k.put(XMLATR_NULL, "YES");
                    if (primary > 0) _k.put(XMLATR_PRI, Integer.toString(primary));
                    if (defaultValue != null) _k.put(XMLATR_DFLT, defaultValue);
                }

                @Override
                public void findDifferences(DBEntity _o, Collection<DBEntityDifference> differences) {
                    super.findDifferences(_o, differences);
                    if (!(_o instanceof Column)) throw new IllegalArgumentException();
                    Column col = (Column) _o;
                    if (!typeName.equals(col.typeName)) differences.add(new DBEntityDifferenceProp(match, DBEntityAttribute.TYPE));
                    if (size != col.size) differences.add(new DBEntityDifferenceProp(match, DBEntityAttribute.SIZE));
                    if (decimals != col.decimals) differences.add(new DBEntityDifferenceProp(match, DBEntityAttribute.DECIMALS));
                    if (nullable != col.nullable) differences.add(new DBEntityDifferenceProp(match, DBEntityAttribute.NULL_ALLOWED));
                    if (primary != col.primary) differences.add(new DBEntityDifferenceProp(match, DBEntityAttribute.PRIMARY));
                    if (!equalsOrNull(defaultValue, col.defaultValue)) differences.add(new DBEntityDifferenceProp(match, DBEntityAttribute.DEFAULT_VALUE));
                }
            }

            public class Index extends DBEntity {

                private final Hashtable<Integer, IndexColumn> columnRef;

                private final boolean unique;

                protected Index(String _name, boolean _unique) {
                    super(_name);
                    columnRef = new Hashtable<Integer, IndexColumn>();
                    unique = _unique;
                }

                @Override
                public final DBEntityType getType() {
                    return DBEntityType.INDEX;
                }

                @Override
                public DBEntity container() {
                    return Table.this;
                }

                @Override
                public Object getAttributeValue(DBEntityAttribute _attr) {
                    switch(_attr) {
                        case NAME:
                            return name;
                        case UNIQUE:
                            return new Boolean(unique);
                        default:
                            throw new DBInvalidAttributeException(getType(), _attr);
                    }
                }

                @Override
                public boolean functionallyEquivalent(DBEntity _o) {
                    boolean bResult = false;
                    if ((_o != null) && (_o instanceof Index)) {
                        Index o = (Index) _o;
                        bResult = (this.unique == o.unique) && (functionallyEquivalentSortedList(this.columnRef, o.columnRef));
                    }
                    return bResult;
                }

                @Override
                public void findDifferences(DBEntity _o, Collection<DBEntityDifference> differences) {
                    if (_o == null) throw new NullPointerException();
                    if (!(_o instanceof Index)) throw new IllegalArgumentException();
                    Index o = (Index) _o;
                    if (!name.equals(o.name)) differences.add(new DBEntityDifferenceRename(match));
                    DBFormat.processListDifferences(this.columnRef.values(), o.columnRef.values(), differences);
                    if (unique != o.unique) differences.add(new DBEntityDifferenceProp(match, DBEntityAttribute.UNIQUE));
                }

                @Override
                protected boolean xmlOpenTag(String _tag, Attributes att) throws UserCancelledException {
                    if (!super.xmlOpenTag(_tag, att)) {
                        if (_tag.equals(XMLTAG_IDXCOL)) {
                            xmlCurrentTag = addColumn(att.getValue(XMLATR_NAME), Integer.parseInt(att.getValue(XMLATR_POS)), att.getValue(XMLATR_ASC));
                        }
                    }
                    return false;
                }

                @Override
                protected void xmlProp(Hashtable<String, String> _k) {
                    super.xmlProp(_k);
                    if (unique) _k.put(XMLATR_UNI, "YES");
                }

                @Override
                public DBEntity[] getContent() {
                    return columnRef.values().toArray(new DBEntity[columnRef.size()]);
                }

                public IndexColumn addColumn(String _name, int _ordinalPosition, String _ascDesc) {
                    IndexColumn c = new IndexColumn(_name, _ordinalPosition, (_ascDesc != null && _ascDesc.length() > 0) ? _ascDesc.charAt(0) : ' ');
                    columnRef.put(new Integer(_ordinalPosition), c);
                    return c;
                }

                @Override
                public String sqlDropStatement() {
                    StringBuffer sb = new StringBuffer().append("ALTER TABLE ").append(Table.this.name).append(" DROP ");
                    if (unique) sb.append("UNIQUE KEY "); else sb.append("INDEX ");
                    sb.append(Index.this.name);
                    return sb.toString();
                }

                @Override
                public String sqlCreateStatement() {
                    StringBuffer sb = new StringBuffer().append("ALTER TABLE ").append(Table.this.name).append(" ADD ").append(sqlDefinitionClause());
                    return sb.toString();
                }

                public String sqlDefinitionClause() {
                    StringBuffer sb = new StringBuffer();
                    if (unique) sb.append("UNIQUE KEY "); else sb.append("INDEX ");
                    sb.append(Index.this.name).append(" (");
                    int i = 0;
                    Integer[] positions = columnRef.keySet().toArray(new Integer[columnRef.size()]);
                    Arrays.sort(positions);
                    for (Integer pos : positions) {
                        if (i > 0) sb.append(",");
                        IndexColumn col = columnRef.get(pos);
                        sb.append(col.name);
                        switch(col.ascDesc) {
                            case 'A':
                                sb.append(" ASC");
                                break;
                            case 'D':
                                sb.append(" DESC");
                        }
                        i++;
                    }
                    sb.append(")");
                    return sb.toString();
                }

                public class IndexColumn extends DBPositionalEntity {

                    protected final char ascDesc;

                    protected IndexColumn(String _name, int _ordinalPosition, char _ascDesc) {
                        super(_name, _ordinalPosition);
                        ascDesc = _ascDesc;
                    }

                    @Override
                    public Object getAttributeValue(DBEntityAttribute _attr) {
                        switch(_attr) {
                            case NAME:
                                return name;
                            case ORDINAL_POSITION:
                                return new Integer(ordinalPosition);
                            case ASCDESC:
                                return new Character(ascDesc);
                            default:
                                throw new DBInvalidAttributeException(getType(), _attr);
                        }
                    }

                    @Override
                    public final DBEntityType getType() {
                        return DBEntityType.INDEX_COLUMN;
                    }

                    @Override
                    public DBEntity container() {
                        return Index.this;
                    }

                    @Override
                    public boolean isContainer() {
                        return false;
                    }

                    @Override
                    public boolean functionallyEquivalent(DBEntity _o) {
                        return matchingEntity(_o) && (((IndexColumn) _o).ascDesc == this.ascDesc);
                    }

                    @Override
                    public void findDifferences(DBEntity _o, Collection<DBEntityDifference> differences) {
                        super.findDifferences(_o, differences);
                        if (!(_o instanceof IndexColumn)) throw new IllegalArgumentException();
                        if (ascDesc != ((IndexColumn) _o).ascDesc) differences.add(new DBEntityDifferenceProp(match, DBEntityAttribute.ASCDESC));
                    }

                    @Override
                    protected void xmlProp(Hashtable<String, String> _k) {
                        super.xmlProp(_k);
                        _k.put(XMLATR_ASC, Character.toString(ascDesc));
                    }
                }
            }

            public class ForeignKey extends DBEntity {

                private final Hashtable<Integer, ForeignKeyColumn> columnRef;

                protected ForeignKey(String _name) {
                    super(_name);
                    columnRef = new Hashtable<Integer, ForeignKeyColumn>();
                }

                public final void rebuildColumnReferences() {
                    for (ForeignKeyColumn fkcol : columnRef.values()) {
                        fkcol.rebuildColumnReference();
                    }
                }

                @Override
                public final DBEntityType getType() {
                    return DBEntityType.FK;
                }

                @Override
                public DBEntity container() {
                    return Table.this;
                }

                @Override
                public Object getAttributeValue(DBEntityAttribute _attr) {
                    switch(_attr) {
                        case NAME:
                            return name;
                        default:
                            throw new DBInvalidAttributeException(getType(), _attr);
                    }
                }

                @Override
                public boolean functionallyEquivalent(DBEntity _o) {
                    boolean bResult = false;
                    if ((_o != null) && (_o instanceof ForeignKey)) {
                        ForeignKey o = (ForeignKey) _o;
                        bResult = (functionallyEquivalentSortedList(this.columnRef, o.columnRef));
                    }
                    return bResult;
                }

                @Override
                public void findDifferences(DBEntity _o, Collection<DBEntityDifference> differences) {
                    if (_o == null) throw new NullPointerException();
                    if (!(_o instanceof ForeignKey)) throw new IllegalArgumentException();
                    ForeignKey o = (ForeignKey) _o;
                    if (!name.equals(o.name)) differences.add(new DBEntityDifferenceRename(match));
                    DBFormat.processListDifferences(this.columnRef.values(), o.columnRef.values(), differences);
                }

                @Override
                protected boolean xmlOpenTag(String _tag, Attributes att) throws UserCancelledException {
                    if (!super.xmlOpenTag(_tag, att)) {
                        if (_tag.equals(XMLTAG_FKCOL)) {
                            xmlCurrentTag = addColumn(att.getValue(XMLATR_NAME), Integer.parseInt(att.getValue(XMLATR_POS)), att.getValue(XMLATR_PKTBL), att.getValue(XMLATR_PKCOL));
                        }
                    }
                    return false;
                }

                @Override
                protected void xmlProp(Hashtable<String, String> _k) {
                    super.xmlProp(_k);
                }

                @Override
                public DBEntity[] getContent() {
                    return columnRef.values().toArray(new DBEntity[columnRef.size()]);
                }

                public ForeignKeyColumn addColumn(String _name, int _ordinalPosition, String _pkTable, String _pkColumn) {
                    ForeignKeyColumn c = new ForeignKeyColumn(_name, _ordinalPosition, _pkTable, _pkColumn);
                    columnRef.put(new Integer(_ordinalPosition), c);
                    return c;
                }

                public class ForeignKeyColumn extends DBPositionalEntity {

                    /** PKTABLE_NAME */
                    private final String pkTable;

                    /** PKCOLUMN_NAME */
                    private final String pkColumn;

                    private Column columnReference;

                    protected ForeignKeyColumn(String _name, int _ordinalPosition, String _pkTable, String _pkColumn) {
                        super(_name, _ordinalPosition);
                        pkTable = _pkTable;
                        pkColumn = _pkColumn;
                        columnReference = null;
                    }

                    public final void rebuildColumnReference() {
                        columnReference = Schema.this.getColumn(pkTable, pkColumn);
                        if (columnReference != null) columnReference.addForeignKeyColumnReference(this); else throw new RuntimeException("Broken foreign key reference");
                    }

                    @Override
                    public Object getAttributeValue(DBEntityAttribute _attr) {
                        switch(_attr) {
                            case NAME:
                                return name;
                            case ORDINAL_POSITION:
                                return new Integer(ordinalPosition);
                            case PKTABLE:
                                return pkTable;
                            case PKCOLUMN:
                                return pkColumn;
                            default:
                                throw new DBInvalidAttributeException(getType(), _attr);
                        }
                    }

                    @Override
                    public final DBEntityType getType() {
                        return DBEntityType.FK_COLUMN;
                    }

                    @Override
                    public DBEntity container() {
                        return ForeignKey.this;
                    }

                    @Override
                    public boolean isContainer() {
                        return false;
                    }

                    @Override
                    public boolean functionallyEquivalent(DBEntity _o) {
                        if (_o == null) throw new NullPointerException();
                        if (!(_o instanceof ForeignKeyColumn)) throw new IllegalArgumentException();
                        final ForeignKeyColumn o = (ForeignKeyColumn) _o;
                        return name.equals(o.name) && pkTable.equals(o.pkTable) && pkColumn.equals(o.pkColumn);
                    }

                    @Override
                    public void findDifferences(DBEntity _o, Collection<DBEntityDifference> differences) {
                        super.findDifferences(_o, differences);
                        if (!(_o instanceof ForeignKeyColumn)) throw new IllegalArgumentException();
                        final ForeignKeyColumn o = (ForeignKeyColumn) _o;
                        if (!pkTable.equals(o.pkTable)) differences.add(new DBEntityDifferenceProp(match, DBEntityAttribute.PKTABLE));
                        if (!pkColumn.equals(o.pkColumn)) differences.add(new DBEntityDifferenceProp(match, DBEntityAttribute.PKCOLUMN));
                    }

                    @Override
                    protected void xmlProp(Hashtable<String, String> _k) {
                        super.xmlProp(_k);
                        _k.put(XMLATR_PKTBL, pkTable);
                        _k.put(XMLATR_PKCOL, pkColumn);
                    }

                    public Column getColumnReference() {
                        return columnReference;
                    }
                }
            }

            public class PrimaryKeyColumn extends DBPositionalEntity {

                private final Column col;

                protected PrimaryKeyColumn(String _name, Column _col, int _ordPos) {
                    super(_name, _ordPos);
                    col = _col;
                }

                @Override
                public Object getAttributeValue(DBEntityAttribute _attr) {
                    switch(_attr) {
                        case NAME:
                            return name;
                        case ORDINAL_POSITION:
                            return new Integer(this.ordinalPosition);
                        default:
                            throw new DBInvalidAttributeException(getType(), _attr);
                    }
                }

                @Override
                public final DBEntityType getType() {
                    return DBEntityType.PK_COLUMN;
                }

                @Override
                public DBEntity container() {
                    return Table.this;
                }

                @Override
                public boolean isContainer() {
                    return false;
                }

                @Override
                public boolean functionallyEquivalent(DBEntity _o) {
                    if (_o == null) throw new NullPointerException();
                    if (!(_o instanceof PrimaryKeyColumn)) throw new IllegalArgumentException();
                    final PrimaryKeyColumn o = (PrimaryKeyColumn) _o;
                    return name.equals(o.name);
                }

                @Override
                public void findDifferences(DBEntity _o, Collection<DBEntityDifference> differences) {
                    if (!(_o instanceof PrimaryKeyColumn)) throw new IllegalArgumentException();
                    super.findDifferences(_o, differences);
                }

                public final Column getCol() {
                    return col;
                }
            }
        }
    }

    /**
	 * Check if two sorted lists of entities are functionally equivalent.
	 * Test is done by comparing all entities at the same ordinal position.
	 * Enitity pair comparison is done using <code>functionallyEquivalent</code>.
	 * <p>
	 * This method is used for checking the equivalence between two index 
	 * definitions.
	 * @param _a first "list" of entities
	 * @param _b second "list" of entities
	 * @return true if the two "lists" are equivalent
	 */
    protected static boolean functionallyEquivalentSortedList(Hashtable<Integer, ? extends DBPositionalEntity> _a, Hashtable<Integer, ? extends DBPositionalEntity> _b) {
        DBPositionalEntity itemA, itemB;
        Integer sKey;
        boolean bEquivalent = (_a.size() == _b.size());
        if (bEquivalent) {
            Iterator<Integer> itKey = _a.keySet().iterator();
            while (bEquivalent && itKey.hasNext()) {
                sKey = itKey.next();
                if (_b.containsKey(sKey)) {
                    itemA = _a.get(sKey);
                    itemB = _b.get(sKey);
                    bEquivalent = itemB.functionallyEquivalent(itemA);
                } else {
                    bEquivalent = false;
                }
            }
        }
        return bEquivalent;
    }

    /**
	 * Searches the matching pairs between two key-referenced "sets" of entities.
	 * Difference instances are generated for all mismatching entities 
	 * using {@link DBFormat#processMissingEntities(Collection, Collection, Collection)}. 
	 * Matching pairs are processed by {@link DBFormat#processMatchingEntities(Collection, Collection)}.
	 * @param _a the first key-referenced "set" of entities
	 * @param _b the second key-referenced "set" of entities
	 * @param _differences the list where all differences are cumulated
	 * @param _functionalMatch use {@code DBFormat.DBEntity#functionallyEquivalent(net.sourceforge.dbsa.structure.DBFormat.DBEntity)} 
	 * instead of {@code DBFormat.DBEntity#matchingEntity(net.sourceforge.dbsa.structure.DBFormat.DBEntity)} 
	 * @see DBFormat#processListDifferences(Collection, Collection, Collection)
	 */
    protected static void processHashtableDifferences(Hashtable<String, ? extends DBEntity> _a, Hashtable<String, ? extends DBEntity> _b, Collection<DBEntityDifference> _differences, boolean _functionalMatch) {
        Hashtable<String, DBEntity> htA, htB;
        Collection<DBEntity> a, b;
        Collection<DBEntityMatch> matching = new ArrayList<DBEntityMatch>();
        htA = new Hashtable<String, DBEntity>(_a);
        htB = new Hashtable<String, DBEntity>(_b);
        a = htA.values();
        b = htB.values();
        if (_functionalMatch) {
            extractKeyMatchingEntities(htA, htB, matching, true);
            extractMatchingEntities(a, b, matching, true);
        }
        extractKeyMatchingEntities(htA, htB, matching, false);
        extractMatchingEntities(a, b, matching, false);
        processMissingEntities(a, b, _differences);
        processMatchingEntities(matching, _differences);
    }

    /** 
	 * Like <code>extractMatchingEntities</code>, extract matching pairs from
	 * two "sets" of database entities. In this case, objects are passed inside
	 * Hashtable instances. Used when one of the match conditions is some kind 
	 * of key equality (usualy the name). Entity lookup is done by key which
	 * is faster.
	 * @param _a the first map of name/entity
	 * @param _b the second map of name/entity
	 * @param _matching a collection where to add the matching pairs
	 * @param _functionalMatch use functional equivalence instead of 
	 * name comparison
	 * @see DBFormat#extractMatchingEntities(Collection, Collection, Collection, boolean)
	 */
    private static void extractKeyMatchingEntities(Hashtable<String, DBEntity> _a, Hashtable<String, DBEntity> _b, Collection<DBEntityMatch> _matching, boolean _functionalMatch) {
        boolean bMatch;
        DBEntity itemA, itemB;
        String sKey;
        Iterator<String> itKey = _a.keySet().iterator();
        while (itKey.hasNext()) {
            sKey = itKey.next();
            if (_b.containsKey(sKey)) {
                itemA = _a.get(sKey);
                itemB = _b.get(sKey);
                if (_functionalMatch) bMatch = itemB.functionallyEquivalent(itemA); else bMatch = itemB.matchingEntity(itemA);
                if (bMatch) {
                    _matching.add(new DBEntityMatch(itemA, itemB));
                    itKey.remove();
                    _b.remove(sKey);
                }
            }
        }
    }

    /**
	 * Extract matching pairs from two collection of database entities.
	 * Objects contained in the first collection are compared to the objects in 
	 * the second collection. 
	 * When a match is found, both objects are removed, then 
	 * a match instance is created and added to the collection of matches.
	 * Comparison may be done by name (DBEntityMatch.matchingEntity) or
	 * by functional equivalence (DBEntityMatch.functionallyEquivalent)
	 * @param _a the first map of name/entity
	 * @param _b the second map of name/entity
	 * @param _matching a collection where to add the matching pairs
	 * @param _functionalMatch use functional equivalence instead of 
	 * name comparison
	 */
    private static void extractMatchingEntities(Collection<? extends DBEntity> _a, Collection<? extends DBEntity> _b, Collection<DBEntityMatch> _matching, boolean _functionalMatch) {
        Iterator<? extends DBEntity> ita = _a.iterator();
        while (ita.hasNext()) {
            DBEntity itemA;
            itemA = ita.next();
            Iterator<? extends DBEntity> itb = _b.iterator();
            while (itemA != null && itb.hasNext()) {
                DBEntity itemB;
                itemB = itb.next();
                if (_functionalMatch ? itemB.functionallyEquivalent(itemA) : itemB.matchingEntity(itemA)) {
                    _matching.add(new DBEntityMatch(itemA, itemB));
                    ita.remove();
                    itb.remove();
                    itemA = null;
                }
            }
        }
    }

    /**
	 * Searches the matching pairs between two collections of entities.
	 * Difference instances are generated for all mismatching entities 
	 * using {@link DBFormat#processMissingEntities(Collection, Collection, Collection)}. 
	 * Matching pairs are processed by {@link DBFormat#processMatchingEntities(Collection, Collection)}.
	 * @param _a the first collection "set" of entities
	 * @param _b the second collection "set" of entities
	 * @param _differences the list where all differences are cumulated
	 */
    protected static void processListDifferences(Collection<? extends DBEntity> _a, Collection<? extends DBEntity> _b, Collection<DBEntityDifference> _differences) {
        ArrayList<DBEntityMatch> matching;
        Collection<DBEntity> a = new ArrayList<DBEntity>(_a);
        Collection<DBEntity> b = new ArrayList<DBEntity>(_b);
        matching = new ArrayList<DBEntityMatch>();
        extractMatchingEntities(a, b, matching, false);
        processMissingEntities(a, b, _differences);
        processMatchingEntities(matching, _differences);
    }

    /**
	 * Generates one mismatch difference instance for each entity.
	 * Two lists of entities are passed: Entities in the first list have no
	 * matching correspondence in the second so a {@code DBDifferenceType#DELETED}
	 * difference is generated. For entities in the second list a {@code DBDifferenceType#ADDED}
	 * difference is generated.  
	 * @param _a the first list of entities
	 * @param _b the second list of entities
	 * @param _differences a collection where all the differences are added
	 */
    private static void processMissingEntities(Collection<? extends DBEntity> _a, Collection<? extends DBEntity> _b, Collection<DBEntityDifference> _differences) {
        Iterator<? extends DBEntity> itEnt;
        itEnt = _a.iterator();
        while (itEnt.hasNext()) _differences.add(new DBEntityDifferenceMissing(new DBEntityMatch(itEnt.next(), null)));
        itEnt = _b.iterator();
        while (itEnt.hasNext()) _differences.add(new DBEntityDifferenceMissing(new DBEntityMatch(null, itEnt.next())));
    }

    /**
	 * Launch a difference search for each matching pair.
	 * @param _matching the list of matching entity pairs
	 * @param _differences a collection where all the differences are added
	 */
    private static void processMatchingEntities(Collection<DBEntityMatch> _matching, Collection<DBEntityDifference> _differences) {
        DBEntityMatch matchPair;
        Iterator<DBEntityMatch> itMatch;
        itMatch = _matching.iterator();
        while (itMatch.hasNext()) {
            matchPair = itMatch.next();
            matchPair.a.findDifferences(matchPair.b, _differences);
        }
    }

    private static IOException internalException(SAXException e) {
        IOException ioex = new IOException("Internal DBFormat Exception: Invalid XML format");
        ioex.initCause(e);
        return ioex;
    }

    private static void initDifferenceTest(DBEntity _e) {
        DBEntity[] cnt = _e.getContent();
        if (cnt != null) for (DBEntity sub : cnt) initDifferenceTest(sub);
        _e.match = null;
    }

    /**
	 * Static method to launch a recursive search between two 
	 * schema instances. One of the schema parameters may be null, meaning
	 * that the whole schema is missing (added or dropped)
	 * @param _a the first (left side) schema
	 * @param _b the second (right side) schema
	 * @return the list of differences
	 */
    public static Collection<DBEntityDifference> getDifferences(Schema _a, Schema _b) {
        ArrayList<DBEntityDifference> retVal = new ArrayList<DBEntityDifference>();
        if (_a != null) initDifferenceTest(_a);
        if (_b != null) initDifferenceTest(_b);
        DBEntityMatch m = new DBEntityMatch(_a, _b);
        if (_a != null && _b != null) _a.findDifferences(_b, retVal); else retVal.add(new DBEntityDifferenceMissing(m));
        return retVal;
    }

    protected static boolean equalsOrNull(String _a, String _b) {
        return ((_a == _b) || (_a != null && _a.equals(_b)));
    }

    protected static String buildSchemaName(String _catalog, String _schema) {
        return (_schema != null && _schema.length() > 0) ? _schema : _catalog;
    }

    public static Schema readFromDB(String _catalog, String _schema, Connection _connection) throws SQLException, UserCancelledException {
        return readFromDB(_catalog, _schema, _connection, null);
    }

    /**
	 * Read meta data information of a schema using a JDBC connection.
	 * @param _catalog
	 * @param _schema
	 * @param _connection
	 * @param _loadMonitor Progress bar dialog or null
	 * @return the schema. if no schema content is found the returnded object is empty 
	 * (this includes the case where the schema does not exists). 
	 * @throws SQLException 
	 * @throws UserCancelledException
	 */
    public static Schema readFromDB(String _catalog, String _schema, Connection _connection, Progression _loadMonitor) throws SQLException, UserCancelledException {
        Schema result = new Schema(_catalog, _schema, null);
        result.readFromDB(_connection, _loadMonitor);
        return result;
    }

    private static class DbXMLParserSchemaList extends DefaultHandler {

        private final ArrayList<String> schemaNames;

        public DbXMLParserSchemaList() {
            schemaNames = new ArrayList<String>();
        }

        @Override
        public void startElement(String uri, String _name, String qName, Attributes atts) {
            if (qName.equals(XMLTAG_SCH)) {
                String name;
                if (atts.getIndex(XMLATR_SCHEMA) < 0) name = atts.getValue(XMLATR_CATALOG); else name = atts.getValue(XMLATR_SCHEMA);
                schemaNames.add(name);
            }
        }

        public Collection<String> getSchemaNames() {
            return schemaNames;
        }
    }

    private static class DbXMLParser extends DefaultHandler {

        private DBEntity currentSchema;

        private final ArrayList<Schema> schemaList;

        private final Progression loadMonitor;

        private final boolean onlySchemaList;

        protected DbXMLParser(Progression _progress, boolean _onlyList) {
            schemaList = new ArrayList<Schema>();
            loadMonitor = _progress;
            onlySchemaList = _onlyList;
        }

        @Override
        public void startElement(String uri, String _name, String qName, Attributes atts) {
            if (currentSchema == null) {
                if (qName.equals(XMLTAG_SCH)) {
                    currentSchema = new Schema(atts.getIndex(XMLATR_CATALOG) < 0 ? null : atts.getValue(XMLATR_CATALOG), atts.getIndex(XMLATR_SCHEMA) < 0 ? null : atts.getValue(XMLATR_SCHEMA), loadMonitor);
                    schemaList.add((Schema) currentSchema);
                }
            } else if (!onlySchemaList) currentSchema.xmlOpenTag(qName, atts);
        }

        @Override
        public void endElement(String uri, String localName, String _name) {
            if (currentSchema != null) currentSchema = currentSchema.xmlCloseTag(_name);
        }

        public Schema[] getSchemaList() {
            return schemaList.toArray(new Schema[schemaList.size()]);
        }
    }

    /**
	 * Load schema instances contained in an XML-formated data. 
	 * @param _xmlStream 
	 * @param _progress Progress bar dialog or null
	 * @return
	 * @throws IOException
	 */
    public static Schema[] readFromXML(InputStream _xmlStream, Progression _progress) throws IOException {
        return readFromXML(new InputSource(_xmlStream), _progress, false);
    }

    /**
	 * Load schema instances contained in an XML-formated data. 
 	 * @param _xmlStream
	 * @param _progress
	 * @return
	 * @throws IOException
	 */
    public static Schema[] readFromXML(Reader _xmlStream, Progression _progress) throws IOException {
        return readFromXML(new InputSource(_xmlStream), _progress, false);
    }

    /**
	 * Load EMPTY schema instances contained in an XML-formated data. Tables are not
	 * loaded. 
	 * @param _xmlStream
	 * @return
	 * @throws IOException
	 */
    public static Schema[] readFromXMLSchemaList(Reader _xmlStream) throws IOException {
        return readFromXML(new InputSource(_xmlStream), null, true);
    }

    private static Schema[] readFromXML(InputSource _xmlInputSource, Progression _progress, boolean _skipTables) throws IOException {
        if (_progress != null) _progress.setMax(100);
        DbXMLParser handler = new DbXMLParser(_progress, _skipTables);
        try {
            XMLReader xr = XMLReaderFactory.createXMLReader();
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);
            xr.parse(_xmlInputSource);
        } catch (SAXException e) {
            throw internalException(e);
        }
        return (_progress != null && _progress.isCancelled()) ? null : handler.getSchemaList();
    }

    /**
	 * Get the names of the schemas contained in an XML stream.
	 * @param _xmlStream
	 * @return
	 * @throws IOException
	 */
    public static Collection<String> readFromXMLSchemaNames(Reader _xmlStream) throws IOException {
        DbXMLParserSchemaList handler = new DbXMLParserSchemaList();
        try {
            XMLReader xr = XMLReaderFactory.createXMLReader();
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);
            xr.parse(new InputSource(_xmlStream));
        } catch (SAXException e) {
            throw internalException(e);
        }
        return (handler.getSchemaNames());
    }

    public static void writeToXML(Writer _xmlStream, Schema... schemas) throws IOException {
        _xmlStream.write("<?xml version=\"1.0\"?>");
        int numTables = 0;
        for (int i = 0; i < schemas.length; i++) numTables += schemas[i].getTableCount();
        _xmlStream.write("<repositoryelement>\n");
        _xmlStream.write("<statistics tables=\"".concat(Integer.toString(numTables)).concat("\"/>\n"));
        for (int i = 0; i < schemas.length; i++) schemas[i].writeToXML(_xmlStream);
        _xmlStream.write("</repositoryelement>\n");
    }

    public static class DBEntityComparator implements Comparator<DBEntity> {

        public int compare(DBEntity a, DBEntity b) {
            if (a.getType() == b.getType()) {
                if (a instanceof DBPositionalEntity && b instanceof DBPositionalEntity) {
                    int posA, posB;
                    posA = ((DBPositionalEntity) a).ordinalPosition;
                    posB = ((DBPositionalEntity) b).ordinalPosition;
                    if (posA < posB) return -1;
                    if (posA > posB) return 1;
                }
                return a.name.compareTo(b.name);
            }
            return a.getType().compareTo(b.getType());
        }
    }

    public static final DBEntityComparator dbeComparator = new DBEntityComparator();

    public static String sqlParseValue(String _v) {
        if (_v == null) return "NULL";
        return "'" + _v.replaceAll("'", "''") + "'";
    }
}
