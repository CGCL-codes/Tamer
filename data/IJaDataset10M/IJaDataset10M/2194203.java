package mod.datagen.generic.castor;

import java.util.Enumeration;
import org.dlib.tools.TVector;
import org.dlib.xml.XmlAttribute;
import org.dlib.xml.XmlElement;
import druid.core.AttribList;
import druid.core.DataLib;
import druid.core.DataTypeLib;
import druid.core.DocManager;
import druid.data.AbstractNode;
import druid.data.DatabaseNode;
import druid.data.FieldNode;
import druid.data.TableNode;
import druid.data.datatypes.TypeInfo;
import druid.interfaces.BasicModule;
import druid.interfaces.Logger;
import java.util.HashSet;

public class Generator {

    private Logger logger;

    private BasicModule mod;

    private DatabaseNode dbNode;

    private DatabaseSettings dbSett;

    private HashSet hsAlerts = new HashSet();

    public Generator(Logger l, BasicModule m, DatabaseNode node) {
        logger = l;
        mod = m;
        dbNode = node;
        dbSett = new DatabaseSettings(dbNode.modsConfig, mod);
    }

    public XmlElement generate() {
        XmlElement elRoot = new XmlElement("mapping");
        addDescr(elRoot, dbNode);
        AttribList al = dbSett.getIncludes();
        for (int i = 0; i < al.size(); i++) {
            String include = al.get(i).getString("include");
            XmlElement elInc = new XmlElement("include");
            elInc.setAttribute(new XmlAttribute("href", include));
            elRoot.addChild(elInc);
        }
        for (Enumeration e = dbNode.preorderEnumeration(); e.hasMoreElements(); ) {
            AbstractNode node = (AbstractNode) e.nextElement();
            if (node instanceof TableNode) elRoot.addChild(genClass(mod, (TableNode) node));
        }
        genKeyGens(elRoot);
        return elRoot;
    }

    private XmlElement genClass(BasicModule mod, TableNode node) {
        TableSettings sett = new TableSettings(node.modsConfig, mod);
        XmlElement elTable = new XmlElement("class");
        String name = dbSett.getPackage() + "." + node.attrSet.getString("name") + dbSett.getClassSuffix();
        elTable.setAttribute(new XmlAttribute("name", name));
        int extID = sett.getExtends();
        if (extID != 0) {
            TableNode extTable = dbNode.getTableByID(extID);
            if (extTable != null) {
                name = dbSett.getPackage() + "." + extTable.attrSet.getString("name") + dbSett.getClassSuffix();
                elTable.setAttribute(new XmlAttribute("extends", name));
            } else {
                logger.log(Logger.ALERT, "Base table removed for table : " + node.attrSet.getString("name"));
            }
        }
        int depID = sett.getDepends();
        if (depID != 0) {
            TableNode depTable = dbNode.getTableByID(depID);
            if (depTable != null) {
                name = dbSett.getPackage() + "." + depTable.attrSet.getString("name") + dbSett.getClassSuffix();
                elTable.setAttribute(new XmlAttribute("depends", name));
            } else {
                logger.log(Logger.ALERT, "Master table removed for table : " + node.attrSet.getString("name"));
            }
        }
        if (sett.isAutoComplete()) elTable.setAttribute(new XmlAttribute("auto-complete", sett.isAutoComplete()));
        if (!sett.isVerifyConstr()) elTable.setAttribute(new XmlAttribute("verify-constructable", sett.isVerifyConstr()));
        if (!sett.getAccess().equals(TableSettings.ACCESS_SHARED)) elTable.setAttribute(new XmlAttribute("access", sett.getAccess()));
        if (!sett.getKeyGen().equals(TableSettings.KEYGEN_NONE)) elTable.setAttribute(new XmlAttribute("key-generator", sett.getKeyGen()));
        addDescr(elTable, node);
        if (!sett.getCacheType().equals(TableSettings.CACHETYPE_COUNTLIM) || !sett.getCacheCap().equals("")) {
            XmlElement elCache = new XmlElement("cache-type");
            if (!sett.getCacheType().equals(TableSettings.CACHETYPE_COUNTLIM)) elCache.setAttribute(new XmlAttribute("type", sett.getCacheType()));
            if (!sett.getCacheCap().equals("")) elCache.setAttribute(new XmlAttribute("capacity", sett.getCacheCap()));
            elTable.addChild(elCache);
        }
        XmlElement elMapTo = new XmlElement("map-to");
        elMapTo.setAttribute(new XmlAttribute("table", node.attrSet.getString("name")));
        elTable.addChild(elMapTo);
        TVector v = new TVector();
        v.setSeparator(" ");
        for (int i = 0; i < node.getChildCount(); i++) {
            FieldNode field = (FieldNode) node.getChild(i);
            if (DataLib.isPrimaryKey(field)) v.add(field.attrSet.getString("name"));
            elTable.addChild(genField(mod, field));
        }
        if (v.size() != 0) elTable.setAttribute(new XmlAttribute("identity", v.toString()));
        return elTable;
    }

    private XmlElement genField(BasicModule mod, FieldNode node) {
        FieldSettings sett = new FieldSettings(node.modsConfig, mod);
        TypeInfo ti = DataTypeLib.getTypeInfo(node);
        XmlElement elField = new XmlElement("field");
        elField.setAttribute(new XmlAttribute("name", node.attrSet.getString("name")));
        String type = convertTypeToJava(ti);
        elField.setAttribute(new XmlAttribute("type", type));
        if (DataLib.isNotNull(node)) elField.setAttribute(new XmlAttribute("required", "true"));
        if (sett.isDirect()) elField.setAttribute(new XmlAttribute("direct", sett.isDirect()));
        if (sett.isLazy()) elField.setAttribute(new XmlAttribute("lazy", sett.isLazy()));
        if (!sett.getHandler().equals("")) elField.setAttribute(new XmlAttribute("handler", sett.getHandler()));
        if (!sett.getGetMethod().equals("")) elField.setAttribute(new XmlAttribute("get-method", sett.getGetMethod()));
        if (!sett.getSetMethod().equals("")) elField.setAttribute(new XmlAttribute("set-method", sett.getSetMethod()));
        if (!sett.getHasMethod().equals("")) elField.setAttribute(new XmlAttribute("has-method", sett.getHasMethod()));
        if (!sett.getCreateMethod().equals("")) elField.setAttribute(new XmlAttribute("create-method", sett.getCreateMethod()));
        if (sett.isTransient()) elField.setAttribute(new XmlAttribute("transient", sett.isTransient()));
        if (!sett.getContainer().equals(FieldSettings.OMIT)) elField.setAttribute(new XmlAttribute("container", sett.getContainer()));
        if (!sett.getCollection().equals(FieldSettings.COLL_NONE)) elField.setAttribute(new XmlAttribute("collection", sett.getCollection()));
        addDescr(elField, node);
        XmlElement elSql = new XmlElement("sql");
        elSql.setAttribute(new XmlAttribute("name", node.attrSet.getString("name")));
        String sqlType = convertTypeToSql(ti);
        elSql.setAttribute(new XmlAttribute("type", sqlType));
        if (node.isFkey()) {
            int refTable = node.attrSet.getInt("refTable");
            int refField = node.attrSet.getInt("refField");
            TableNode t = dbNode.getTableByID(refTable);
            FieldNode f = t.getFieldByID(refField);
            elSql.setAttribute(new XmlAttribute("many-table", t.attrSet.getString("name")));
            elSql.setAttribute(new XmlAttribute("many-key", f.attrSet.getString("name")));
        }
        if (sett.isSqlReadOnly()) elSql.setAttribute(new XmlAttribute("read-only", sett.isSqlReadOnly()));
        if (!sett.getSqlDirty().equals(FieldSettings.DIRTY_CHECK)) elSql.setAttribute(new XmlAttribute("dirty", sett.getSqlDirty()));
        if (sett.isSqlTransient()) elSql.setAttribute(new XmlAttribute("transient", sett.isSqlTransient()));
        elField.addChild(elSql);
        return elField;
    }

    private void genKeyGens(XmlElement el) {
        XmlElement elKeyGen;
        elKeyGen = new XmlElement("key-generator");
        elKeyGen.setAttribute(new XmlAttribute("name", "HIGH-LOW"));
        if (!dbSett.getHLTable().equals("")) setParam(elKeyGen, "table", dbSett.getHLTable());
        if (!dbSett.getHLKeyField().equals("")) setParam(elKeyGen, "key-column", dbSett.getHLKeyField());
        if (!dbSett.getHLValueField().equals("")) setParam(elKeyGen, "value-column", dbSett.getHLValueField());
        if (!dbSett.getHLGrabSize().equals("")) setParam(elKeyGen, "grab-size", dbSett.getHLGrabSize());
        if (dbSett.isHLSameConn()) setParam(elKeyGen, "same-connection", dbSett.isHLSameConn());
        if (dbSett.isHLSameConn()) setParam(elKeyGen, "global", dbSett.isHLGlobal());
        el.addChild(elKeyGen);
        elKeyGen = new XmlElement("key-generator");
        elKeyGen.setAttribute(new XmlAttribute("name", "SEQUENCE"));
        if (!dbSett.getSEQSequence().equals("")) setParam(elKeyGen, "sequence", dbSett.getSEQSequence());
        if (!dbSett.getSEQIncrement().equals("")) setParam(elKeyGen, "increment", dbSett.getSEQIncrement());
        if (dbSett.isSEQReturning()) setParam(elKeyGen, "returning", dbSett.isSEQReturning());
        if (dbSett.isSEQTrigger()) setParam(elKeyGen, "trigger", dbSett.isSEQTrigger());
        el.addChild(elKeyGen);
    }

    private void setParam(XmlElement el, String name, String value) {
        XmlElement elParam = new XmlElement("param");
        elParam.setAttribute(new XmlAttribute("name", name));
        elParam.setAttribute(new XmlAttribute("value", value));
        el.addChild(elParam);
    }

    private void setParam(XmlElement el, String name, boolean value) {
        XmlElement elParam = new XmlElement("param");
        elParam.setAttribute(new XmlAttribute("name", name));
        elParam.setAttribute(new XmlAttribute("value", value));
        el.addChild(elParam);
    }

    private void addDescr(XmlElement el, AbstractNode node) {
        String descr = DocManager.toText(node.xmlDoc).trim();
        if (!descr.equals("")) el.addChild(new XmlElement("description", descr));
    }

    private String convertTypeToJava(TypeInfo ti) {
        if (dbSett.isUsingDDEquiv()) {
            if (ti.ddEquiv.equals("")) {
                if (!hsAlerts.contains(ti.name)) {
                    hsAlerts.add(ti.name);
                    logger.log(Logger.ALERT, "DDEquiv is empty for type : " + ti.name);
                }
            }
            return ti.ddEquiv;
        }
        String type = ti.basicType.toLowerCase();
        if ("int4".equals(ti.basicType)) type = "integer";
        if ("int8".equals(ti.basicType)) type = "long";
        if ("text".equals(ti.basicType)) type = "string";
        if ("varchar".equals(ti.basicType)) type = "string";
        if ("varchar2".equals(ti.basicType)) type = "string";
        if ("nvarchar2".equals(ti.basicType)) type = "string";
        if ("number".equals(ti.basicType)) type = "integer";
        return type;
    }

    private String convertTypeToSql(TypeInfo ti) {
        String type = ti.basicType.toLowerCase();
        return type;
    }
}
