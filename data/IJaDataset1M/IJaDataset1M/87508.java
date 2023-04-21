package org.mokolo.fon;

import java.util.*;
import java.io.IOException;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Attribute;
import nu.xom.ParsingException;
import org.mokolo.fon.Scripter;
import org.mokolo.fon.Support;

public class Table {

    protected String _basedir;

    protected Document _document;

    protected Element _rootElement;

    protected Support _support;

    protected boolean _useAbb;

    /**
   * Create a new table with name '_new' and abb='_new'.
   * You must set the name with setName and set or change the other
   * attributes. You cannot store the table with its default name.
   */
    public Table(String basedir) {
        _rootElement = new Element("table");
        Attribute aName = new Attribute("name", "_new");
        _rootElement.addAttribute(aName);
        Attribute aAbb = new Attribute("abb", "_new");
        _rootElement.addAttribute(aAbb);
        _document = new Document(_rootElement);
        init(basedir);
    }

    /**
   * Create table object and initialize using the contents of
   * an XML file.
   */
    public Table(String basedir, String tableName) throws IOException {
        try {
            Builder builder = new Builder();
            _document = builder.build(basedir + "/tables/" + tableName);
            _rootElement = _document.getRootElement();
            init(basedir);
        } catch (ParsingException e) {
            System.err.println(e.getCause() + " (line=" + e.getLineNumber() + ", column=" + e.getColumnNumber() + ")");
        }
    }

    private void init(String basedir) {
        _support = new Support();
        _useAbb = false;
        _basedir = basedir;
    }

    /**
   * Sets the basedir of the table.
   */
    public void setBasedir(String basedir) {
        _basedir = basedir;
    }

    /**
   * Returns the basedir
   */
    public String getBasedir() {
        return _basedir;
    }

    /**
   * Sets the name of the table. The name is automatically converted to
   * lowercase. This name will also be used for the filename if you
   * store the table.
   */
    public void setName(String theName) {
        _support.setStringAttribute(_rootElement, "name", theName.toLowerCase());
    }

    /**
   * Returns the name of the table
   */
    public String getName() {
        return _support.getStringAttribute(_rootElement, "name");
    }

    /**
   * Sets the 'abb' attribute
   */
    public void setAbb(String theAbb) {
        _support.setStringAttribute(_rootElement, "abb", theAbb.toLowerCase());
    }

    /**
   * Returns the 'abb' attribute
   */
    public String getAbb() {
        return _support.getStringAttribute(_rootElement, "abb");
    }

    /**
   * Sets the 'useAbb' field
   */
    public void setUseAbb(boolean useAbb) {
        _useAbb = useAbb;
    }

    /**
   * Gets the 'useAbb' field
   */
    public boolean getUseAbb() {
        return _useAbb;
    }

    /**
   * Returns the table name as created in the ddl
   */
    public String ddlName() {
        if (_useAbb) return getAbb(); else return getName();
    }

    /**
   * Returns the table name as named in comments (opposite of DDLName)
   */
    public String commentName() {
        if (_useAbb) return getName(); else return getAbb();
    }

    /**
   * Returns class name (CamelCaps). Used if class name not provided
   * in table specification. It transforms a table name in plural form
   * and with underscores to separate 'words' into a camelcaps class name.
   * Example: table name 'data_sources' gets class name 'DataSource'. 
   */
    public String className() {
        StringBuffer retval = new StringBuffer(getName().toLowerCase());
        retval.setCharAt(0, Character.toUpperCase(retval.charAt(0)));
        int i = 0;
        while ((i = retval.indexOf("_", i + 1)) >= 0 && i < retval.length() - 1) {
            retval.deleteCharAt(i);
            retval.setCharAt(i, Character.toUpperCase(retval.charAt(i)));
        }
        if (retval.charAt(retval.length() - 1) == 's') {
            retval.deleteCharAt(retval.length() - 1);
        }
        return retval.toString();
    }

    /**
   * Returns indented XML document
   */
    public String getXML() {
        return _document.toXML();
    }

    /**
   * Returns a list of table names referenced in foreign keys.
   */
    public String[] getForeignKeysTableList() {
        try {
            ArrayList tableNames = new ArrayList();
            Element columnGroupsElement = _rootElement.getFirstChildElement("column-groups");
            if (columnGroupsElement == null) throw (new Exception("No 'column-groups' element found"));
            Elements columnGroupElements = columnGroupsElement.getChildElements("column-group");
            Element columnGroupElement;
            for (int i = 0; i < columnGroupElements.size(); i++) {
                columnGroupElement = columnGroupElements.get(i);
                String cgType = _support.getStringAttribute(columnGroupElement, "type");
                if (cgType.compareTo("foreign key") == 0) {
                    String tableName = _support.getStringAttribute(columnGroupElement, "references-table");
                    tableNames.add(tableName);
                }
            }
            String[] retval = (String[]) tableNames.toArray(new String[tableNames.size()]);
            return retval;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
   * Returns a DDL script to create the sequences for the table.
   */
    public String getCreateTableSequencesScript(Scripter gen) {
        try {
            String script = "";
            Element columnsElement = _rootElement.getFirstChildElement("columns");
            if (columnsElement == null) throw (new Exception("No 'columns' element found"));
            Elements columnElements = columnsElement.getChildElements("column");
            Element columnElement;
            for (int i = 0; i < columnElements.size(); i++) {
                columnElement = columnElements.get(i);
                script += gen.getCreateColumnSequence(ddlName(), columnElement);
            }
            if (script.length() > 0) script += "\n";
            return script;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    /**
   * Returns a DDL script to drop the sequences for the table.
   */
    public String getDropTableSequencesScript(Scripter gen) {
        try {
            String script = "";
            Element columnsElement = _rootElement.getFirstChildElement("columns");
            if (columnsElement == null) throw (new Exception("No 'columns' element found"));
            Elements columnElements = columnsElement.getChildElements("column");
            Element columnElement;
            for (int i = 0; i < columnElements.size(); i++) {
                columnElement = columnElements.get(i);
                script += gen.getDropColumnSequence(ddlName(), columnElement);
            }
            if (script.length() > 0) script += "\n";
            return script;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    /**
   * Returns a DDL script to create the table.
   */
    public String getCreateTableScript(Scripter gen) {
        try {
            String script = "";
            String columnString = "";
            script += "CREATE\n TABLE " + gen.getFullPrefix() + ddlName() + " (\n";
            Element columnsElement = _rootElement.getFirstChildElement("columns");
            if (columnsElement == null) throw (new Exception("No 'columns' element found"));
            Elements columnElements = columnsElement.getChildElements("column");
            Element columnElement;
            int firstColumn = 1;
            for (int i = 0; i < columnElements.size(); i++) {
                columnElement = columnElements.get(i);
                columnString = gen.getCreateColumnLine(ddlName(), columnElement);
                if (firstColumn == 0) columnString = columnString.substring(0, 5) + "," + columnString.substring(6); else firstColumn = 0;
                script += columnString + "\n";
            }
            script += ")" + gen.getGo() + "\n";
            for (int i = 0; i < columnElements.size(); i++) {
                columnElement = columnElements.get(i);
                script += gen.getCreateColumnTrigger(ddlName(), columnElement);
            }
            return script;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    /**
   * Returns a DDL script to drop the table.
   */
    public String getDropTableScript(Scripter gen) {
        try {
            String script = "";
            script += "DROP TABLE " + gen.getFullPrefix() + ddlName();
            script += gen.getGo();
            return script;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    /**
   * Returns a DDL script to create primary key, foreign keys, unique and
   * check constraints for the table.
   */
    public String getCreateTableConstraintsScript(Scripter gen) {
        try {
            String script = "";
            Element columnGroupsElement = _rootElement.getFirstChildElement("column-groups");
            if (columnGroupsElement == null) throw (new Exception("No 'column-groups' element found"));
            Elements columnGroupElements = columnGroupsElement.getChildElements("column-group");
            Element columnGroupElement;
            for (int i = 0; i < columnGroupElements.size(); i++) {
                columnGroupElement = columnGroupElements.get(i);
                String cgName = _support.getStringAttribute(columnGroupElement, "name");
                String cgType = _support.getStringAttribute(columnGroupElement, "type");
                String cgCreate = _support.getStringAttribute(columnGroupElement, "create");
                if (cgCreate.compareTo("yes") == 0 && (cgType.compareTo("primary key") == 0 || cgType.compareTo("unique key") == 0 || cgType.compareTo("foreign key") == 0)) {
                    script += "ALTER TABLE " + gen.getFullPrefix() + ddlName() + " ADD CONSTRAINT " + getAbb() + "_" + cgName;
                    script += " ";
                    if (cgType.compareTo("primary key") == 0) script += "PRIMARY KEY"; else if (cgType.compareTo("unique key") == 0) script += "UNIQUE"; else if (cgType.compareTo("foreign key") == 0) script += "FOREIGN KEY";
                    script += " " + getColumnGroupString(cgName);
                    if (cgType.compareTo("foreign key") == 0) {
                        String cgReferencesTable = _support.getStringAttribute(columnGroupElement, "references-table");
                        String cgReferencesColumnGroup = _support.getStringAttribute(columnGroupElement, "references-column-group");
                        Table otherTable = new Table(_basedir, cgReferencesTable);
                        otherTable.setUseAbb(this.getUseAbb());
                        script += " REFERENCES " + gen.getFullPrefix() + otherTable.ddlName();
                        script += " " + otherTable.getColumnGroupString(cgReferencesColumnGroup);
                    }
                    script += gen.getGo();
                }
            }
            return script;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    /**
   * Returns a DDL script to drop primary key, foreign keys, unique and
   * check constraints for the table.
   */
    public String getDropTableConstraintsScript(Scripter gen) {
        try {
            String script = "";
            Element columnGroupsElement = _rootElement.getFirstChildElement("column-groups");
            if (columnGroupsElement == null) throw (new Exception("No 'column-groups' element found"));
            Elements columnGroupElements = columnGroupsElement.getChildElements("column-group");
            Element columnGroupElement;
            for (int i = 0; i < columnGroupElements.size(); i++) {
                columnGroupElement = columnGroupElements.get(i);
                String cgName = _support.getStringAttribute(columnGroupElement, "name");
                String cgType = _support.getStringAttribute(columnGroupElement, "type");
                String cgCreate = _support.getStringAttribute(columnGroupElement, "create");
                if (cgCreate.compareTo("yes") == 0 && (cgType.compareTo("primary key") == 0 || cgType.compareTo("unique key") == 0 || cgType.compareTo("foreign key") == 0)) {
                    script += "ALTER TABLE " + gen.getFullPrefix() + ddlName() + " DROP CONSTRAINT " + getAbb() + "_" + cgName;
                    script += gen.getGo();
                }
            }
            return script;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    /**
   * Returns a DDL script to create the indexes for the table.
   */
    public String getCreateTableIndexesScript(Scripter gen) {
        try {
            String script = "";
            Element columnGroupsElement = _rootElement.getFirstChildElement("column-groups");
            if (columnGroupsElement == null) throw (new Exception("No 'column-groups' element found"));
            Elements columnGroupElements = columnGroupsElement.getChildElements("column-group");
            Element columnGroupElement;
            for (int i = 0; i < columnGroupElements.size(); i++) {
                columnGroupElement = columnGroupElements.get(i);
                String cgName = _support.getStringAttribute(columnGroupElement, "name");
                String cgType = _support.getStringAttribute(columnGroupElement, "type");
                String cgCreate = _support.getStringAttribute(columnGroupElement, "create");
                if (cgCreate.compareTo("yes") == 0 && cgType.compareTo("index") == 0) {
                    script += "CREATE INDEX " + gen.getFullPrefix() + getAbb() + "_" + cgName + " ON " + getName() + getColumnGroupString(cgName);
                    script += gen.getGo();
                }
            }
            return script;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    /**
   * Returns a DDL script to drop the indexes for the table.
   */
    public String getDropTableIndexesScript(Scripter gen) {
        try {
            String script = "";
            Element columnGroupsElement = _rootElement.getFirstChildElement("column-groups");
            if (columnGroupsElement == null) throw (new Exception("No 'column-groups' element found"));
            Elements columnGroupElements = columnGroupsElement.getChildElements("column-group");
            Element columnGroupElement;
            for (int i = 0; i < columnGroupElements.size(); i++) {
                columnGroupElement = columnGroupElements.get(i);
                String cgName = _support.getStringAttribute(columnGroupElement, "name");
                String cgType = _support.getStringAttribute(columnGroupElement, "type");
                String cgCreate = _support.getStringAttribute(columnGroupElement, "create");
                if (cgCreate.compareTo("yes") == 0 && cgType.compareTo("index") == 0) {
                    script += "DROP INDEX " + gen.getFullPrefix() + getAbb() + "_" + cgName;
                    script += gen.getGo();
                }
            }
            return script;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    /**
   * Returns the columns in a column group between brackets.
   */
    public String getColumnGroupString(String theGroup) {
        try {
            int firstColumn = 1;
            String retval = "(";
            Element columnGroupsElement = _rootElement.getFirstChildElement("column-groups");
            Elements columnGroupElements = columnGroupsElement.getChildElements("column-group");
            Element columnGroupElement = columnGroupsElement.getFirstChildElement("column-group");
            int i = 0;
            int foundit = 0;
            while (i < columnGroupElements.size() && foundit == 0) {
                columnGroupElement = columnGroupElements.get(i);
                String columnGroupName = _support.getStringAttribute(columnGroupElement, "name");
                if (columnGroupName.compareTo(theGroup) == 0) foundit = 1; else i++;
            }
            if (i == columnGroupElements.size()) return "No column group named '" + theGroup + "' found";
            Element columnGroupColumnsElement = columnGroupElement.getFirstChildElement("cg-columns");
            Elements columnGroupColumnsColumnElements = columnGroupColumnsElement.getChildElements("cg-column");
            Element columnGroupColumnsColumnElement;
            for (int j = 0; j < columnGroupColumnsColumnElements.size(); j++) {
                columnGroupColumnsColumnElement = columnGroupColumnsColumnElements.get(j);
                String columnName = _support.getStringAttribute(columnGroupColumnsColumnElement, "name");
                if (firstColumn == 0) retval += ", "; else firstColumn = 0;
                retval += columnName;
            }
            retval += ")";
            return retval;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    /**
   * Return a header comment with the table name. To be used in scripts.
   */
    public String getTableHeaderComment(Scripter gen) {
        try {
            String retval = "";
            retval += gen.startComment() + "\n";
            retval += gen.comment("Table '" + ddlName() + "' (" + commentName() + ")") + "\n";
            retval += gen.endComment() + "\n";
            return retval;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }
}
