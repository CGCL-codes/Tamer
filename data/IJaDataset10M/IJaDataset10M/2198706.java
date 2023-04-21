package com.fivenineuptime.dbmodeller.fileio;

import com.fivenineuptime.dbmodeller.metadata.*;
import com.fivenineuptime.dbmodeller.gui.Canvas;
import java.net.URL;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import java.util.Iterator;
import java.io.File;
import java.net.MalformedURLException;

/**
  * Used to open a saved database schema file. Files are saved in xml format.
  * @author Jonathon Horsman
  * @version $Revision: 1.2 $
  * $Log: FileOpener.java,v $
  * Revision 1.2  2007/10/08 21:14:54  nzdev
  * Made the interface prettier.
  * Remove old foreign key connection types
  *
  * Revision 1.1.1.1  2007/10/08 18:40:01  nzdev
  * Initial Sourceforge import
  *
  * Revision 1.9  2004/01/20 03:41:09  jonathon
  * DBM38 Added support for Sybase IDENTITY.
  *
  * Revision 1.8  2003/12/17 02:52:29  jonathon
  * Use data type IDs instead of type names.
  *
  * Revision 1.7  2003/12/16 00:50:03  jonathon
  * Improved table positioning to avoid most overlaps.
  * Correctly handle cancel option when exiting with unsaved work.
  *
  * Revision 1.6  2003/12/15 22:07:48  jonathon
  * Added names to foreign keys.
  * Added equals method to table fields for improved handling of modifiedfields.
  *
  * Revision 1.5  2003/09/08 04:55:39  jonathon
  * Moved SQL generator into SQL dir.
  * Bugfix: Show scrollbars when opening & import tables as needed.
  * Added foreign key import from Sybase database.
  * Center windows on app now.
  *
  * Revision 1.4  2003/07/19 05:23:00  jonathon
  * Added GPL licencing message.
  * Added scroll bars to canvas.
  * Added generate SQL methods.
  * Added window and menu option for SQL output.
  * Implemented New menu option
  *
  * Revision 1.3  2003/07/12 04:36:52  jonathon
  * Added log CVS comment.
  * Reworked foreign key to use dropdown selectors for tables/fields
  *
  */
public class FileOpener {

    public static final String XML_ELEMENT_CANVAS = "canvas";

    public static final String XML_ELEMENT_TABLES = "tables";

    public static final String XML_ELEMENT_FOREIGN_KEYS = "foreignkeys";

    public static final String XML_ELEMENT_TABLE = "table";

    public static final String XML_ELEMENT_FOREIGN_KEY = "foreignkey";

    public static final String XML_ELEMENT_LOCALE = "locale";

    public static final String XML_ELEMENT_FIELD = "field";

    public static final String XML_ELEMENT_DATATYPE = "datatype";

    public static final String XML_ELEMENT_TYPE = "type";

    public static final String XML_ELEMENT_PRECISION = "precision";

    public static final String XML_ELEMENT_PK = "primarykey";

    public static final String XML_ELEMENT_NOTNULL = "notnull";

    public static final String XML_ELEMENT_DEFAULT = "default";

    public static final String XML_ELEMENT_IDENTITY = "identity";

    public static final String ATTRIBUTE_FIELD_NAME = "name";

    public static final String ATTRIBUTE_TABLE_NAME = "name";

    public static final String ATTRIBUTE_FK_NAME = "name";

    public static final String ATTRIBUTE_LOCALE_X = "x";

    public static final String ATTRIBUTE_LOCALE_Y = "y";

    public static final String ATTRIBUTE_FK_TYPE = "type";

    private static final String DEFAULT_LOCALE_X = "0";

    private static final String DEFAULT_LOCALE_Y = "0";

    private static final String DEFAULT_TABLE_NAME = "new_table_";

    private Canvas canvas;

    /**
     * Creates a new instance of FileOpener. This will parse the file, iterate through the contents adding each
     * table and foreign key to the given canvas object.
     * @param inputFile the file to read
     * @param canvas the parent object to open the file onto.
     */
    public FileOpener(File inputFile, Canvas canvas) {
        this.canvas = canvas;
        try {
            readDocument(parse(inputFile));
        } catch (Exception e) {
            System.err.println("Open file failed: " + e);
        }
    }

    /**
     *
     * Parse a file and return as an xml document.
     * @param file the xml file for parsing.
     * @return an document representing the XML file for easy iteration.
     * @throws DocumentException if an error occurs during parsing
     * @throws MalformedURLException if a URL could not be made for the given file
     */
    private Document parse(File file) throws DocumentException, MalformedURLException {
        SAXReader reader = new SAXReader();
        return reader.read(file);
    }

    /**
     * Parses an XML document, adding the contents (Tables and ForeignKeys) to the canvas
     * @param document the document to parse.
     * @throws DocumentException if an error occurs whilst reading the document.
     */
    public void readDocument(Document document) throws DocumentException {
        Element root = document.getRootElement();
        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            if (element.getQualifiedName().equals(XML_ELEMENT_TABLES)) {
                parseTables(element.elementIterator());
            } else if (element.getQualifiedName().equals(XML_ELEMENT_FOREIGN_KEYS)) {
                parseForeignKeys(element.elementIterator());
            }
        }
        canvas.repaint();
    }

    /**
     *  Parse through the tables element, adding all the tables to the canvas
     *  @param i the iterator of <code>Element</code>'s
     */
    private void parseTables(Iterator i) {
        while (i.hasNext()) {
            Element element = (Element) i.next();
            if (element.getQualifiedName().equals(XML_ELEMENT_TABLE)) {
                Table table = parseTable(element.elementIterator());
                table.setName(element.attributeValue(ATTRIBUTE_TABLE_NAME, DEFAULT_TABLE_NAME + i));
                try {
                    canvas.add(table);
                } catch (DuplicateNameException e) {
                    System.err.println("Create table failed: " + e);
                }
            }
        }
    }

    /**
     *  Parse through a table element, initialising to a given locale and adding fields
     *  @param i the iterator of <code>Element</code>'s
     *  @return an initialised table with fields added
     */
    private Table parseTable(Iterator i) {
        Table table = null;
        while (i.hasNext()) {
            Element element = (Element) i.next();
            if (element.getQualifiedName().equals(XML_ELEMENT_LOCALE)) {
                table = parseLocale(element);
            } else if (element.getQualifiedName().equals(XML_ELEMENT_FIELD)) {
                String fieldName = element.attributeValue("name");
                if (table != null) {
                    table.add(parseField(element.elementIterator(), fieldName));
                } else {
                    System.err.println("Cannot add field to table because the locale has not yet been found!");
                }
            }
        }
        return table;
    }

    /**
     *  Parse a locale element, constructing a new table from the X and Y attributes
     *  @param element the locale element
     *  @return the initialised Table object
     */
    private Table parseLocale(Element element) {
        return new Table(Integer.parseInt(element.attributeValue(ATTRIBUTE_LOCALE_X, DEFAULT_LOCALE_X)), Integer.parseInt(element.attributeValue(ATTRIBUTE_LOCALE_Y, DEFAULT_LOCALE_Y)));
    }

    /**
     *  Parse a field element, setting the various parameters such as datatype, PK, not null etc.
     *  @param i the iterator to obtain field properties
     *  @param fieldName the name of field necessary to construct the field object
     *  @return the initialised <code>TableField</code> object
     */
    private TableField parseField(Iterator i, String fieldName) {
        DataType dataType = null;
        boolean notNull = false;
        boolean pk = false;
        String defaultVal = null;
        boolean identity = false;
        while (i.hasNext()) {
            Element element = (Element) i.next();
            if (element.getQualifiedName().equals(XML_ELEMENT_DATATYPE)) {
                dataType = parseDataType(element.elementIterator());
            } else if (element.getQualifiedName().equals(XML_ELEMENT_NOTNULL)) {
                notNull = true;
            } else if (element.getQualifiedName().equals(XML_ELEMENT_PK)) {
                pk = true;
                notNull = true;
            } else if (element.getQualifiedName().equals(XML_ELEMENT_IDENTITY)) {
                identity = true;
            } else if (element.getQualifiedName().equals(XML_ELEMENT_DEFAULT)) {
                defaultVal = element.getTextTrim();
            } else {
                System.err.println("Warning: Unknown element in field: " + element.getQualifiedName());
            }
        }
        return new TableField(fieldName, dataType, !notNull, pk, defaultVal, identity);
    }

    /**
      * @param i the element itertor to obtain the datatype parameters.
      * @return the parsed DataType from the given iterator.
      */
    private DataType parseDataType(Iterator i) {
        String type = null;
        int precision = -1;
        while (i.hasNext()) {
            Element e = (Element) i.next();
            if (e.getQualifiedName().equals(XML_ELEMENT_TYPE)) type = e.getTextTrim(); else if (e.getQualifiedName().equals(XML_ELEMENT_PRECISION)) precision = Integer.parseInt(e.getTextTrim());
        }
        int typeId = -1;
        try {
            typeId = Integer.parseInt(type);
        } catch (NumberFormatException e) {
            typeId = DataType.resolveDataType(type);
        }
        return (precision == -1) ? new DataType(typeId) : new DataType(typeId, precision);
    }

    /**
     *  Iterates through a given iterator of <code>Element</code>'s, adding them as foreign keys to the canvas
     *  @param i the iterator
     */
    private void parseForeignKeys(Iterator i) {
        while (i.hasNext()) {
            Element element = (Element) i.next();
            if (element.getQualifiedName().equals(XML_ELEMENT_FOREIGN_KEY)) {
                try {
                    ForeignKey key = parseForeignKey(element.elementIterator());
                    key.setName(element.attributeValue(ATTRIBUTE_FK_NAME, key.defaultName()));
                    canvas.add(key);
                } catch (NonExistentObjectException e) {
                    System.err.println("Error adding foreign key: " + e);
                }
            } else {
                System.err.println("Warning: Unrecognised element parsing foreign keys: " + element.getQualifiedName());
            }
        }
    }

    private ForeignKey parseForeignKey(Iterator i) throws NonExistentObjectException {
        ForeignKeyConnection con1 = null;
        ForeignKeyConnection con2 = null;
        while (i.hasNext()) {
            Element element = (Element) i.next();
            if (element.getQualifiedName().equals(XML_ELEMENT_TABLE)) {
                if (con1 == null) {
                    con1 = parseFkConnection(element);
                } else {
                    con2 = parseFkConnection(element);
                }
            } else {
                System.err.println("Warning: Unrecognised element parsing foreign key: " + element.getQualifiedName());
            }
        }
        return new ForeignKey(con1, con2);
    }

    private ForeignKeyConnection parseFkConnection(Element element) throws NonExistentObjectException {
        String tableName = element.attributeValue(ATTRIBUTE_TABLE_NAME);
        Table table = canvas.findTableByName(tableName);
        TableField field = parseConnectionField((Element) element.elementIterator().next(), table);
        int type = Integer.parseInt(element.attributeValue(ATTRIBUTE_FK_TYPE));
        return new ForeignKeyConnection(table, field);
    }

    private TableField parseConnectionField(Element element, Table t) throws NonExistentObjectException {
        if (element.getQualifiedName().equals(XML_ELEMENT_FIELD)) {
            return t.findField(element.getTextTrim());
        } else {
            System.err.println("Warning: Unrecognised element parsing connection field: " + element.getQualifiedName());
        }
        return null;
    }
}
