package blomo.executables.createhbm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import blomo.executables.createhbm.relations.Attribute;
import blomo.executables.createhbm.relations.IndexedManyToMany;
import blomo.executables.createhbm.relations.ManyToMany;
import blomo.executables.createhbm.relations.OneToMany;
import blomo.executables.createhbm.relations.OneToOne;
import blomo.executables.createhbm.relations.Relation;
import blomo.util.Pair;

/**
 * @author Malte Schulze
 *
 */
public class MappingFileCreatorImpl implements MappingFileCreator {

    private String classpackage;

    enum SetTypes {

        one2many, many2many, simple, one2one, many2one
    }

    @Override
    public void createMappingFiles(Set<Table> tables, Map<Table, Set<Relation>> tableRelations, String classpackage, String directory, Util util, Map<Pair<String, String>, List<Pair<String, String>>> alias) {
        List<String> mappingFiles = new LinkedList<String>();
        this.classpackage = classpackage;
        for (Table table : tables) {
            if (util.shouldTableBeSkipped(table)) continue;
            Document doc = new Document();
            DocType dt = new DocType("hibernate-mapping");
            dt.setPublicID("-//Hibernate/Hibernate Mapping DTD 3.0//EN");
            dt.setSystemID("http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd");
            doc.setDocType(dt);
            Element root = new Element("hibernate-mapping");
            doc.setRootElement(root);
            Element classEle = new Element("class");
            root.addContent(classEle);
            classEle.setAttribute("name", classpackage + "." + util.formatNameForEntity(table.getName()));
            classEle.setAttribute("table", table.getName());
            Set<Relation> relations = tableRelations.get(table);
            if (relations == null) continue;
            for (Relation r : relations) {
                if (r instanceof Attribute) {
                    Attribute rel = (Attribute) r;
                    Column c = rel.getC();
                    if (util.isIdColumn(c)) {
                        createAttribute(classEle, util.transformDBTypeToHibType(c.getType()), util.getAlias(table, table, util.formatNameForAttribute(c.getName()), alias), c.getName(), c.getDefaultValue(), c.getSequenceId(), util.isIdColumn(c), SetTypes.simple);
                    }
                }
            }
            for (Relation r : relations) {
                if (r instanceof Attribute) {
                    Attribute rel = (Attribute) r;
                    Column c = rel.getC();
                    if (!util.isIdColumn(c)) {
                        createAttribute(classEle, util.transformDBTypeToHibType(c.getType()), util.getAlias(table, table, util.formatNameForAttribute(c.getName()), alias), c.getName(), c.getDefaultValue(), c.getSequenceId(), util.isIdColumn(c), SetTypes.simple);
                    }
                } else if (r instanceof OneToOne) {
                    OneToOne rel = (OneToOne) r;
                    Column c;
                    Table foreignTable;
                    if (rel.getT1().equals(table)) {
                        c = rel.getC1();
                        foreignTable = rel.getT2();
                    } else {
                        c = rel.getC2();
                        foreignTable = rel.getT1();
                    }
                    String type = classpackage + "." + util.formatNameForEntity(foreignTable.getName());
                    createAttribute(classEle, type, util.getAlias(table, foreignTable, util.formatNameForAttribute(c.getName()), alias), c.getName(), c.getDefaultValue(), c.getSequenceId(), util.isIdColumn(c), SetTypes.one2one);
                } else if (r instanceof OneToMany) {
                    OneToMany rel = (OneToMany) r;
                    if (rel.getT1().equals(table)) {
                        Column c = rel.getC1();
                        String type = classpackage + "." + util.formatNameForEntity(rel.getT2().getName());
                        SetTypes setType;
                        if (util.tableHasOneId(rel.getT2())) {
                            setType = SetTypes.many2one;
                        } else setType = SetTypes.simple;
                        createAttribute(classEle, type, util.getAlias(table, rel.getT2(), util.formatNameForAttribute(c.getName()), alias), c.getName(), c.getDefaultValue(), c.getSequenceId(), util.isIdColumn(c), setType);
                        if (rel.getT2().equals(table)) {
                            createSet(classEle, util.getAlias(table, rel.getT1(), util.formatNameForAttribute(c.getName()) + util.formatNameForEntity(rel.getT1().getName()), alias), rel.getT1().getName(), rel.getC1().getName(), util.transformDBTypeToHibType(rel.getC1().getType()), rel.getC1().getName(), classpackage + "." + util.formatNameForEntity(rel.getT1().getName()), SetTypes.one2many, true);
                        }
                    } else {
                        createSet(classEle, util.getAlias(table, rel.getT1(), util.formatNameForEntity(rel.getT1().getName()), alias), rel.getT1().getName(), rel.getC1().getName(), util.transformDBTypeToHibType(rel.getC1().getType()), rel.getC1().getName(), classpackage + "." + util.formatNameForEntity(rel.getT1().getName()), SetTypes.one2many, true);
                    }
                } else if (r instanceof ManyToMany) {
                    ManyToMany rel = (ManyToMany) r;
                    Column localColumn, foreignColumn;
                    Table foreignTable;
                    boolean inverse;
                    if (rel.getT1().equals(table)) {
                        localColumn = rel.getC1();
                        foreignColumn = rel.getC2();
                        foreignTable = rel.getT2();
                        inverse = false;
                    } else {
                        localColumn = rel.getC2();
                        foreignColumn = rel.getC1();
                        foreignTable = rel.getT1();
                        inverse = true;
                    }
                    SetTypes setType;
                    Column connectionColumn;
                    if (foreignColumn != null) {
                        setType = SetTypes.many2many;
                        connectionColumn = foreignColumn;
                    } else {
                        setType = SetTypes.simple;
                        connectionColumn = rel.getC3();
                    }
                    String foreignEntityType;
                    if (foreignTable != null) {
                        foreignEntityType = classpackage + "." + util.formatNameForEntity(foreignTable.getName());
                    } else foreignEntityType = null;
                    createSet(classEle, util.getAlias(table, rel.getT3(), util.formatNameForAttribute(localColumn.getName()), alias), rel.getT3().getName(), localColumn.getName(), util.transformDBTypeToHibType(connectionColumn.getType()), connectionColumn.getName(), foreignEntityType, setType, inverse);
                    if (rel.getT1().equals(rel.getT2())) {
                        localColumn = rel.getC2();
                        foreignColumn = rel.getC1();
                        foreignTable = rel.getT1();
                        connectionColumn = foreignColumn;
                        foreignEntityType = classpackage + "." + util.formatNameForEntity(foreignTable.getName());
                        createSet(classEle, util.getAlias(table, rel.getT3(), util.formatNameForAttribute(localColumn.getName()), alias), rel.getT3().getName(), localColumn.getName(), util.transformDBTypeToHibType(connectionColumn.getType()), connectionColumn.getName(), foreignEntityType, setType, !inverse);
                    }
                } else if (r instanceof IndexedManyToMany) {
                    IndexedManyToMany rel = (IndexedManyToMany) r;
                    Column localColumn, foreignColumn, keyColumn;
                    Table foreignTable, keyTable;
                    boolean inverse;
                    if (rel.getT1().equals(table)) {
                        localColumn = rel.getC1();
                        foreignColumn = rel.getC2();
                        foreignTable = rel.getT2();
                        inverse = false;
                        keyColumn = rel.getC3();
                        keyTable = rel.getT4();
                    } else {
                        localColumn = rel.getC2();
                        foreignColumn = rel.getC1();
                        foreignTable = rel.getT1();
                        inverse = true;
                        keyColumn = rel.getC3();
                        keyTable = rel.getT4();
                    }
                    String typeKey, typeValue;
                    if (keyTable == null) {
                        typeKey = util.transformDBTypeToHibType(keyColumn.getType());
                    } else {
                        typeKey = classpackage + "." + util.formatNameForEntity(keyTable.getName());
                    }
                    if (foreignTable == null) {
                        typeValue = util.transformDBTypeToHibType(foreignColumn.getType());
                    } else {
                        typeValue = classpackage + "." + util.formatNameForEntity(foreignTable.getName());
                    }
                    createMap(classEle, util.getAlias(table, rel.getT3(), util.formatNameForAttribute(localColumn.getName()), alias), rel.getT3().getName(), localColumn.getName(), keyColumn.getName(), typeKey, foreignColumn.getName(), typeValue, inverse);
                    if (rel.getT1().equals(rel.getT2())) {
                        localColumn = rel.getC2();
                        foreignColumn = rel.getC1();
                        foreignTable = rel.getT1();
                        typeValue = classpackage + "." + util.formatNameForEntity(foreignTable.getName());
                        createMap(classEle, util.getAlias(table, rel.getT3(), util.formatNameForAttribute(localColumn.getName()), alias), rel.getT3().getName(), localColumn.getName(), keyColumn.getName(), typeKey, foreignColumn.getName(), typeValue, !inverse);
                    }
                }
            }
            XMLOutputter out = new XMLOutputter();
            out.setFormat(Format.getPrettyFormat());
            java.io.FileWriter writer;
            try {
                String filename = directory + "/" + util.raiseFirstChar(util.formatNameForEntity(table.getName())) + ".hbm.xml";
                writer = new java.io.FileWriter(filename);
                out.output(doc, writer);
                writer.flush();
                writer.close();
                mappingFiles.add(classpackage.replaceAll("\\.", "/") + "/" + util.raiseFirstChar(util.formatNameForEntity(table.getName())) + ".hbm.xml");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        try {
            java.io.FileWriter writer = new java.io.FileWriter(directory + "/BLoMoResources.txt");
            for (String s : mappingFiles) {
                writer.append("\t\t<mapping resource=\"" + s + "\"/>\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void createMap(Element classEle, String attrName, String tableName, String keyColumnName, String indexColumn, String indexClass, String m2mColumn, String m2mClass, boolean inverse) {
        Element mapEle = new Element("map");
        classEle.addContent(mapEle);
        mapEle.setAttribute("name", attrName);
        mapEle.setAttribute("table", tableName);
        Element keyEle = new Element("key");
        mapEle.addContent(keyEle);
        keyEle.setAttribute("column", keyColumnName);
        if (indexClass.startsWith(classpackage + ".")) {
            Element indexEle = new Element("index-many-to-many");
            mapEle.addContent(indexEle);
            indexEle.setAttribute("column", indexColumn);
            indexEle.setAttribute("class", indexClass);
        } else {
            Element indexEle = new Element("map-key");
            mapEle.addContent(indexEle);
            indexEle.setAttribute("column", indexColumn);
            indexEle.setAttribute("type", indexClass);
        }
        if (m2mClass.startsWith(classpackage + ".")) {
            Element manyEle = new Element("many-to-many");
            mapEle.addContent(manyEle);
            manyEle.setAttribute("column", m2mColumn);
            manyEle.setAttribute("class", m2mClass);
        } else {
            Element manyEle = new Element("element");
            mapEle.addContent(manyEle);
            manyEle.setAttribute("column", m2mColumn);
            manyEle.setAttribute("type", m2mClass);
        }
        if (inverse) mapEle.setAttribute("inverse", "true");
    }

    private void createSet(Element classEle, String attrName, String tableName, String idColumnName, String type, String columnName, String className, SetTypes setType, boolean inverse) {
        Element setEle = new Element("set");
        classEle.addContent(setEle);
        setEle.setAttribute("name", attrName);
        setEle.setAttribute("table", tableName);
        Element keyEle = new Element("key");
        setEle.addContent(keyEle);
        keyEle.setAttribute("column", idColumnName);
        Element eleEle = new Element("element");
        setEle.addContent(eleEle);
        if (setType == SetTypes.many2many) {
            eleEle.setAttribute("column", columnName);
            eleEle.setName("many-to-many");
            eleEle.setAttribute("class", className);
        } else if (setType == SetTypes.one2many) {
            eleEle.setName("one-to-many");
            eleEle.setAttribute("class", className);
            keyEle.setAttribute("update", "false");
        } else if (setType == SetTypes.simple) {
            eleEle.setAttribute("column", columnName);
            if (type != null) eleEle.setAttribute("type", type);
        }
        if (inverse) setEle.setAttribute("inverse", "true");
    }

    private void createAttribute(Element classEle, String type, String name, String columnName, String defaultValue, String sequenceId, boolean idColumn, SetTypes m2o) {
        Element colEle;
        if (!idColumn) {
            colEle = new Element("property");
            if (m2o.equals(SetTypes.many2one)) {
                colEle.setName("many-to-one");
            } else if (m2o.equals(SetTypes.one2one)) {
                colEle.setName("one-to-one");
            }
            if ((m2o.equals(SetTypes.many2one) || m2o.equals(SetTypes.one2one)) && type != null) colEle.setAttribute("class", type); else if (!m2o.equals(SetTypes.many2one) && type != null) colEle.setAttribute("type", type);
        } else {
            colEle = new Element("id");
        }
        classEle.addContent(colEle);
        colEle.setAttribute("name", name);
        if (!m2o.equals(SetTypes.one2one)) colEle.setAttribute("column", columnName);
        if (sequenceId != null) {
            Element genEle = new Element("generator");
            colEle.addContent(genEle);
            genEle.setAttribute("class", "sequence");
            Element paramEle = new Element("param");
            genEle.addContent(paramEle);
            paramEle.setAttribute("name", "sequence");
            paramEle.setText(sequenceId);
        }
    }
}
