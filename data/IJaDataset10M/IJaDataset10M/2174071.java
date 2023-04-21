package blomo.executables.createhbm;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import blomo.util.Pair;

/**
 * @author Malte Schulze
 *
 */
public class UtilImpl implements Util {

    private String classpackage;

    private Set<String> deleteSuffixs;

    private Set<String> relationTables = new HashSet<String>();

    private Map<String, String> hibernateTypes = new Hashtable<String, String>();

    private Map<String, String> javaTypes = new Hashtable<String, String>();

    private Map<String, Document> documents = new Hashtable<String, Document>();

    @Override
    public Document loadXMLFile(String filename) {
        Document doc = documents.get(filename);
        if (doc != null) return doc;
        SAXBuilder builder = new SAXBuilder();
        try {
            doc = builder.build(new File(filename));
            documents.put(filename, doc);
            return doc;
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isRelationTable(Table table) {
        boolean allColumnsReferenced = true;
        for (Column c : table.getColumns()) {
            if (c.getReferences().size() == 0) {
                allColumnsReferenced = false;
                break;
            }
        }
        if (allColumnsReferenced) return allColumnsReferenced;
        for (String name : relationTables) {
            if (table.getName().matches(name)) return true;
        }
        return false;
    }

    @Override
    public boolean isInverseCollection(Document hibMapping, String name) {
        for (Object obj : hibMapping.getRootElement().getChild("class").getChildren()) {
            Element ele = (Element) obj;
            if (name.equals(ele.getAttributeValue("name"))) return "true".equals(ele.getAttributeValue("inverse"));
        }
        return false;
    }

    public boolean tableHasOneId(Table table) {
        boolean hasId = false;
        int numberOfIds = 0;
        for (Column c : table.getColumns()) {
            if (isIdColumn(c)) {
                hasId = true;
                numberOfIds++;
            }
        }
        return hasId && numberOfIds == 1;
    }

    public boolean shouldTableBeSkipped(Table table) {
        boolean hasId = tableHasOneId(table);
        if (!hasId) return true;
        return false;
    }

    protected int getReferenceCount(Table table) {
        int i = 0;
        for (Column c : table.getColumns()) {
            if (c.getReferences().size() != 0) {
                i++;
            }
        }
        return i;
    }

    public String formatNameForEntity(String name) {
        if (name.indexOf("_") == -1) return name.substring(0, 1).toUpperCase() + name.substring(1);
        String[] parts = name.split("_");
        String result = "";
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            result = result + part.substring(0, 1).toUpperCase() + part.substring(1);
        }
        return result;
    }

    public String formatNameForAttribute(String name) {
        for (String s : deleteSuffixs) {
            if (name.endsWith(s)) name = name.substring(0, name.length() - s.length());
        }
        String result = formatNameForEntity(name);
        return lowerFirstChar(result);
    }

    @Override
    public Pair<String, String> getTypesFromMap(SourceAttribute attr) {
        String type1, type2;
        type1 = attr.getType().substring(attr.getType().indexOf("<") + 1, attr.getType().indexOf(","));
        type2 = attr.getType().substring(attr.getType().indexOf(",") + 1, attr.getType().indexOf(">"));
        return new Pair<String, String>(type1, type2);
    }

    @Override
    public boolean isPrimitiveAttribute(Column c) {
        return c.getReferences().size() == 0;
    }

    @Override
    public boolean isIdColumn(Column c) {
        return c.isPk();
    }

    @Override
    public String raiseFirstChar(String name) {
        if (name == null || "".equals(name)) return name;
        if (name.startsWith(classpackage)) {
            int len = classpackage.length();
            return classpackage + "." + name.substring(len + 1, len + 2).toUpperCase() + name.substring(len + 2);
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    protected String lowerFirstChar(String name) {
        if (name.startsWith(classpackage)) {
            int len = classpackage.length();
            return classpackage + "." + name.substring(len + 1, len + 2).toLowerCase() + name.substring(len + 2);
        }
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public void setClasspackage(String classpackage) {
        this.classpackage = classpackage;
    }

    @Override
    public void setDeleteSuffixs(Set<String> deleteSuffixs) {
        this.deleteSuffixs = deleteSuffixs;
    }

    private Map<Table, List<String>> attributeCache = new Hashtable<Table, List<String>>();

    private Map<String, String> nameCache = new Hashtable<String, String>();

    private boolean mlOneToOne;

    @Override
    public String getAlias(Table owningTable, Table referencedTable, String name, Map<Pair<String, String>, List<Pair<String, String>>> alias) {
        String origName = name;
        String cacheKey = owningTable.getName() + "," + referencedTable.getName() + "," + name;
        String existingName = nameCache.get(cacheKey);
        if (existingName != null) return nameCache.get(cacheKey);
        String name1, name2;
        if (owningTable != null) name1 = owningTable.getName(); else name1 = "";
        if (referencedTable != null) name2 = referencedTable.getName(); else name2 = "";
        outer: for (Pair<String, String> s : alias.keySet()) if (name1.matches(s.getKey()) && name2.matches(s.getValue())) {
            for (Pair<String, String> p : alias.get(s)) {
                if (p.getKey().equals(name)) {
                    name = p.getValue();
                    break outer;
                }
            }
        }
        List<String> lst = attributeCache.get(owningTable);
        if (lst == null) lst = new LinkedList<String>();
        boolean ignoreUniqueCheck = owningTable.equals(referencedTable);
        while (!ignoreUniqueCheck) {
            boolean unique = true;
            for (String storedName : lst) {
                if (storedName.equals(name)) {
                    name = name + "x";
                    unique = false;
                    break;
                }
            }
            if (unique) break;
        }
        lst.add(name);
        attributeCache.put(owningTable, lst);
        nameCache.put(cacheKey, name);
        if (isRelationTable(referencedTable)) {
            for (Column c : referencedTable.getColumns()) {
                for (Pair<Table, Column> ref : c.getReferences()) {
                    String otherKey = ref.getKey().getName() + "," + referencedTable.getName() + "," + origName;
                    nameCache.put(otherKey, name);
                }
            }
        }
        return name;
    }

    @Override
    public boolean isReferencingCollection(Document hibMapping, String name) {
        for (Object obj : hibMapping.getRootElement().getChild("class").getChildren()) {
            Element ele = (Element) obj;
            if (name.equals(ele.getAttributeValue("name"))) return ele.getChild("many-to-many") != null;
        }
        return false;
    }

    @Override
    public String getRelationTableNameForAttribute(Document hibMapping, String name) {
        for (Object obj : hibMapping.getRootElement().getChild("class").getChildren()) {
            Element ele = (Element) obj;
            if (name.equals(ele.getAttributeValue("name"))) {
                String table = ele.getAttributeValue("table");
                if (table == null) return getTableNameForAttribute(hibMapping, name);
                return table;
            }
        }
        return null;
    }

    @Override
    public String getForeignAttributeNameForAttribute(Document hibMapping, String name) {
        for (Object obj : hibMapping.getRootElement().getChild("class").getChildren()) {
            Element ele = (Element) obj;
            if (name.equals(ele.getAttributeValue("name"))) {
                String result = "";
                Element eleChild = ele.getChild("many-to-many");
                if (eleChild != null) {
                    String referencedColumn = eleChild.getAttributeValue("column");
                    String referencedTable = ele.getAttributeValue("table");
                    String referencedEntityClass = eleChild.getAttributeValue("class");
                    String xml = referencedEntityClass.substring(classpackage.length() + 1) + ".hbm.xml";
                    Document hibMappingReference = null;
                    String uri = hibMapping.getBaseURI();
                    int i = uri.lastIndexOf("/");
                    hibMappingReference = loadXMLFile(uri.substring(6, i) + "\\" + xml);
                    if (hibMappingReference == null) return null;
                    for (Object o : hibMappingReference.getRootElement().getChild("class").getChildren("map")) {
                        Element foreignEle = (Element) o;
                        if (foreignEle.getAttributeValue("table").equals(referencedTable) && foreignEle.getChild("key").getAttributeValue("column").equals(referencedColumn)) {
                            return foreignEle.getAttributeValue("name");
                        }
                    }
                    for (Object o : hibMappingReference.getRootElement().getChild("class").getChildren("set")) {
                        Element foreignEle = (Element) o;
                        if (foreignEle.getAttributeValue("table").equals(referencedTable) && foreignEle.getChild("key").getAttributeValue("column").equals(referencedColumn)) {
                            return foreignEle.getAttributeValue("name");
                        }
                    }
                }
                eleChild = ele.getChild("one-to-many");
                if (eleChild != null) {
                    String referencedColumn = ele.getChild("key").getAttributeValue("column");
                    String referencedClass = hibMapping.getRootElement().getChild("class").getAttributeValue("name");
                    String referencedEntityClass = eleChild.getAttributeValue("class");
                    String xml = referencedEntityClass.substring(classpackage.length() + 1) + ".hbm.xml";
                    Document hibMappingReference = null;
                    String uri = hibMapping.getBaseURI();
                    int i = uri.lastIndexOf("/");
                    hibMappingReference = loadXMLFile(uri.substring(6, i) + "\\" + xml);
                    if (hibMappingReference == null) return null;
                    for (Object o : hibMappingReference.getRootElement().getChild("class").getChildren("many-to-one")) {
                        Element foreignEle = (Element) o;
                        if (foreignEle.getAttributeValue("class").equals(referencedClass) && foreignEle.getAttributeValue("column").equals(referencedColumn)) {
                            return foreignEle.getAttributeValue("name");
                        }
                    }
                }
                if ("many-to-one".equals(ele.getName())) {
                    String referencedColumn = ele.getAttributeValue("column");
                    String referencedClass = hibMapping.getRootElement().getChild("class").getAttributeValue("name");
                    String referencedEntityClass = ele.getAttributeValue("class");
                    String xml = referencedEntityClass.substring(classpackage.length() + 1) + ".hbm.xml";
                    Document hibMappingReference = null;
                    String uri = hibMapping.getBaseURI();
                    int i = uri.lastIndexOf("/");
                    hibMappingReference = loadXMLFile(uri.substring(6, i) + "\\" + xml);
                    for (Object o : hibMappingReference.getRootElement().getChild("class").getChildren("set")) {
                        Element foreignEle = (Element) o;
                        Element eKey = foreignEle.getChild("key");
                        Element eOneToMany = foreignEle.getChild("one-to-many");
                        if (eKey != null && eOneToMany != null) {
                            if (referencedColumn.equals(eKey.getAttributeValue("column")) && referencedClass.equals(eOneToMany.getAttributeValue("class"))) return foreignEle.getAttributeValue("name");
                        }
                    }
                }
                if ("one-to-one".equals(ele.getName())) {
                    String referencedClass = hibMapping.getRootElement().getChild("class").getAttributeValue("name");
                    String referencedEntityClass = ele.getAttributeValue("class");
                    String xml = referencedEntityClass.substring(classpackage.length() + 1) + ".hbm.xml";
                    Document hibMappingReference = null;
                    String uri = hibMapping.getBaseURI();
                    int i = uri.lastIndexOf("/");
                    hibMappingReference = loadXMLFile(uri.substring(6, i) + "\\" + xml);
                    if (hibMappingReference == null) return null;
                    for (Object o : hibMappingReference.getRootElement().getChild("class").getChildren("one-to-one")) {
                        Element foreignEle = (Element) o;
                        if (referencedClass.equals(foreignEle.getAttributeValue("class"))) return foreignEle.getAttributeValue("name");
                    }
                }
                if (result.equals("")) return null; else return result;
            }
        }
        return null;
    }

    @Override
    public String getTableNameForAttribute(Document hibMapping, String name) {
        for (Object obj : hibMapping.getRootElement().getChild("class").getChildren()) {
            Element ele = (Element) obj;
            if (name.equals(ele.getAttributeValue("name"))) {
                String result = "";
                Element eleChild = ele.getChild("many-to-many");
                if (eleChild != null) {
                    String referencedEntityClass = eleChild.getAttributeValue("class");
                    String xml = referencedEntityClass.substring(classpackage.length() + 1) + ".hbm.xml";
                    Document hibMappingReference = null;
                    String uri = hibMapping.getBaseURI();
                    int i = uri.lastIndexOf("/");
                    hibMappingReference = loadXMLFile(uri.substring(6, i) + "\\" + xml);
                    if (hibMappingReference == null) return null;
                    result += hibMappingReference.getRootElement().getChild("class").getAttributeValue("table");
                }
                eleChild = ele.getChild("index-many-to-many");
                if (eleChild != null) {
                    String referencedEntityClass = eleChild.getAttributeValue("class");
                    String xml = referencedEntityClass.substring(classpackage.length() + 1) + ".hbm.xml";
                    Document hibMappingReference = null;
                    String uri = hibMapping.getBaseURI();
                    int i = uri.lastIndexOf("/");
                    hibMappingReference = loadXMLFile(uri.substring(6, i) + "\\" + xml);
                    if (hibMappingReference == null) return null;
                    if (!result.equals("")) result += ",";
                    result += hibMappingReference.getRootElement().getChild("class").getAttributeValue("table");
                }
                if (ele.getName().equals("many-to-one")) {
                    String referencedEntityClass = ele.getAttributeValue("class");
                    String xml = referencedEntityClass.substring(classpackage.length() + 1) + ".hbm.xml";
                    Document hibMappingReference = null;
                    String uri = hibMapping.getBaseURI();
                    int i = uri.lastIndexOf("/");
                    hibMappingReference = loadXMLFile(uri.substring(6, i) + "\\" + xml);
                    if (hibMappingReference == null) return null;
                    if (!result.equals("")) result += ",";
                    result += hibMappingReference.getRootElement().getChild("class").getAttributeValue("table");
                }
                if (ele.getName().equals("one-to-one")) {
                    String referencedEntityClass = ele.getAttributeValue("class");
                    String xml = referencedEntityClass.substring(classpackage.length() + 1) + ".hbm.xml";
                    Document hibMappingReference = null;
                    String uri = hibMapping.getBaseURI();
                    int i = uri.lastIndexOf("/");
                    hibMappingReference = loadXMLFile(uri.substring(6, i) + "\\" + xml);
                    if (hibMappingReference == null) return null;
                    if (!result.equals("")) result += ",";
                    result += hibMappingReference.getRootElement().getChild("class").getAttributeValue("table");
                }
                if (result.equals("")) return null; else return result;
            }
        }
        return null;
    }

    @Override
    public String transformDBTypeToHibType(String originalType) {
        for (String type : hibernateTypes.keySet()) {
            if (type.equals(originalType)) return hibernateTypes.get(type);
        }
        System.out.println("Missing type definition for dbtype: " + originalType);
        System.exit(1);
        return null;
    }

    @Override
    public String transformDBTypeToJavaType(Column c) {
        if (c.getType() == null || c.getReferences().size() > 0) {
            for (Pair<Table, Column> p : c.getReferences()) {
                if (tableHasOneId(p.getKey())) return raiseFirstChar(classpackage + "." + formatNameForEntity(p.getKey().getName()));
            }
        }
        String originalType = c.getType();
        for (String type : javaTypes.keySet()) {
            if (type.equals(originalType)) return javaTypes.get(type);
        }
        return null;
    }

    public void setHibernateTypes(Map<String, String> hibernateTypes) {
        this.hibernateTypes = hibernateTypes;
    }

    public void setJavaTypes(Map<String, String> javaTypes) {
        this.javaTypes = javaTypes;
    }

    @Override
    public boolean isOneToOneRelation(Table origin, Table destination) {
        if (origin.equals(destination)) return false;
        boolean points = false;
        outer: for (Column c : origin.getColumns()) {
            for (Pair<Table, Column> fk : c.getForeignKeys()) {
                if (fk.getKey().equals(destination)) {
                    points = true;
                    break outer;
                }
            }
        }
        if (points) {
            for (Column c : destination.getColumns()) {
                for (Pair<Table, Column> fk : c.getForeignKeys()) {
                    if (fk.getKey().equals(origin)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isOneToOneRelation(Document hibMapping, String name) {
        for (Object obj : hibMapping.getRootElement().getChild("class").getChildren("one-to-one")) {
            Element ele = (Element) obj;
            if (name.equals(ele.getAttributeValue("name"))) return true;
        }
        return false;
    }

    @Override
    public void addLicense(SourceUnit unit) {
        List<String> lines = unit.getComment();
        lines.add("Copyright 2009 Malte Schulze");
        lines.add("");
        lines.add("This file is part of \"BLoMo - Server\".");
        lines.add("");
        lines.add("Permission is hereby granted, free of charge, to any person");
        lines.add("obtaining a copy of this software and associated documentation");
        lines.add("files (the \"Software\"), to deal in the Software without");
        lines.add("restriction, including without limitation the rights to use,");
        lines.add("copy, modify, merge, publish, distribute, sublicense, and/or sell");
        lines.add("copies of the Software, and to permit persons to whom the");
        lines.add("Software is furnished to do so, subject to the following");
        lines.add("conditions:");
        lines.add("");
        lines.add("The above copyright notice and this permission notice shall be");
        lines.add("included in all copies or substantial portions of the Software.");
        lines.add("");
        lines.add("THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,");
        lines.add("EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES");
        lines.add("OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND");
        lines.add("NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT");
        lines.add("HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,");
        lines.add("WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING");
        lines.add("FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR");
        lines.add("OTHER DEALINGS IN THE SOFTWARE.");
    }

    @Override
    public String getTableIdName(Table table, Map<Pair<String, String>, List<Pair<String, String>>> alias) {
        for (Column c : table.getColumns()) {
            if (isIdColumn(c)) return getAlias(table, table, formatNameForAttribute(c.getName()), alias);
        }
        return null;
    }

    @Override
    public void setRelationTables(Set<String> relationTables) {
        this.relationTables = relationTables;
    }

    @Override
    public String getEntityIdName(Document hibMapping) {
        return hibMapping.getRootElement().getChild("class").getChild("id").getAttributeValue("name");
    }

    @Override
    public boolean isMultiLingualAttribute(Column c) {
        if (isPrimitiveAttribute(c)) {
            if (c.getName().endsWith("_ml")) return true;
        }
        return false;
    }

    @Override
    public boolean isMultilingualOneToOne() {
        return mlOneToOne;
    }

    @Override
    public void setMultilingualOneToOne(boolean multilingualOneToOne) {
        this.mlOneToOne = multilingualOneToOne;
    }
}
