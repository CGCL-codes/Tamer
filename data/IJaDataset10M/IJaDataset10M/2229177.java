package org.equanda.domain.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.equanda.domain.xml.aid.ListFilter;
import org.equanda.domain.xml.aid.ObjectGetter;
import org.equanda.domain.xml.aid.TemplateUtil;
import org.equanda.domain.xml.aid.ValidationUtil;
import org.equanda.domain.xml.transform.HasSetLastModified;
import org.equanda.domain.xml.transform.RootTable;
import java.util.*;

/**
 * root node for the XML tree
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
@XStreamAlias("equanda")
public class DomainModel implements HasSetLastModified {

    @XStreamImplicit
    private List<Type> types;

    @XStreamImplicit
    private List<Table> tables;

    @XStreamOmitField
    private List<RootTable> rootTables;

    @XStreamOmitField
    private Parser parser;

    @XStreamOmitField
    private Map<String, String> linkNameMap;

    public List<Type> getTypes() {
        if (types == null) types = new ArrayList<Type>();
        return types;
    }

    public Type getType(String name) {
        if (name == null) return null;
        for (Type type : getTypes()) if (name.equals(type.getName())) return type;
        return null;
    }

    public List<Table> getTables() {
        if (tables == null) tables = new ArrayList<Table>();
        return tables;
    }

    public Table getTable(String name) {
        if (name == null) return null;
        for (Table table : getTables()) if (name.equals(table.getName())) return table;
        return null;
    }

    public List<RootTable> getRootTables() {
        if (rootTables == null) rootTables = new ArrayList<RootTable>();
        return rootTables;
    }

    public List<RootTable> getDefinedRootTables() {
        if (rootTables == null) rootTables = new ArrayList<RootTable>();
        List<RootTable> res = new ArrayList<RootTable>();
        new TemplateUtil<RootTable>().filterList(rootTables, res, new ListFilter<RootTable>() {

            public boolean isSuitable(RootTable table) {
                return !table.getName().startsWith("Equanda");
            }
        });
        return res;
    }

    public RootTable getRootTable(String name) {
        if (name == null) return null;
        for (RootTable rootTable : getRootTables()) if (name.equals(rootTable.getName())) return rootTable;
        return null;
    }

    public void addRootTable(RootTable rootTable) {
        getRootTables().add(rootTable);
    }

    public void setLastModified(long lastModified) {
        for (Table table : tables) table.setLastModified(lastModified);
    }

    public void handleIncludes(Parser parser) {
        try {
            boolean succeeded = false;
            while (!succeeded) {
                try {
                    for (Table table : getTables()) table.handleIncludes(parser, this);
                    succeeded = true;
                } catch (ConcurrentModificationException cme) {
                }
            }
        } catch (Exception e) {
            parser.addError("Problem while transforming om : ", e);
        }
    }

    public void handleTransformation(Parser parser) {
        this.parser = parser;
        for (Table table : getTables()) table.handleRootTransformation(this);
        for (Type type : getTypes()) type.handleTransformation(this);
        for (Table table : getTables()) table.handleTransformation(this);
        for (RootTable rootTable : getRootTables()) rootTable.handleTransformation();
    }

    public void handleValidation(Parser parser) {
        this.parser = parser;
        for (Type type : getTypes()) type.handleValidation(parser);
        for (Table table : getTables()) table.handleValidation(parser);
        for (RootTable rootTable : getRootTables()) rootTable.handleValidation(parser);
        ValidationUtil<Type> vuType = new ValidationUtil<Type>();
        String duplicateTypeName = (String) vuType.allUnique(getTypes(), new ObjectGetter<Type>() {

            public Object get(Type type) {
                return type.getName();
            }
        });
        if (duplicateTypeName != null) parser.addError("Duplicate type " + duplicateTypeName + " in domain model");
        ValidationUtil<Table> vuTable = new ValidationUtil<Table>();
        String duplicateTableName = (String) vuTable.allUnique(getTables(), new ObjectGetter<Table>() {

            public Object get(Table table) {
                return table.getName();
            }
        });
        if (duplicateTableName != null) {
            parser.addError("Duplicate table " + duplicateTableName + " in root domain model");
        }
    }

    public void addError(String msg) {
        parser.addError(msg);
    }

    public void addError(String msg, Exception ex) {
        parser.addError(msg, ex);
    }

    public Parser getParser() {
        return parser;
    }

    /**
     * Get map with linkname-table mappings, used for verification of link-name usage. When relation is defined, "+" is
     * set as map value (to allow duplicate detection).
     *
     * @return link-name, table-name mappings
     */
    public Map<String, String> getLinkNameMap() {
        if (linkNameMap == null) linkNameMap = new HashMap<String, String>();
        return linkNameMap;
    }
}
