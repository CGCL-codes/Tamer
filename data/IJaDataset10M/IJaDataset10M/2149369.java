package net.sourceforge.jdbconverter;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.xml.parsers.*;
import net.sourceforge.jdbconverter.databases.DatabaseInfos;
import net.sourceforge.jdbconverter.utils.ConvertUtils;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class UpdateParser extends DefaultHandler {

    private DatabaseInfos dbInfos;

    private TableCommand tableCommand;

    /**
     * Update a database structure with the database description in an XML file.
     * @param con the Connection to the database
     * @param file the XML with the description of the database 
     * @throws Exception if a database error occur or an parser error on XML.
     */
    public void update(Connection con, File file) throws Exception {
        int dbType = ConvertUtils.getDatabaseType(con);
        dbInfos = ConvertUtils.getDatabaseInfos(dbType, con);
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        sp.parse(file, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            System.out.println(">" + uri + " " + localName + " " + qName + " " + attributes.getValue("name"));
            String name = attributes.getValue("name");
            switch(QualifiedNames.valueOf(qName)) {
                case table:
                    try {
                        ResultSetMetaData rsmd = dbInfos.getResultSetMetaData(name);
                        tableCommand = dbInfos.alterTable(name, rsmd);
                    } catch (SQLException ex) {
                        tableCommand = dbInfos.createTable(name);
                    }
                    break;
                case col:
                    tableCommand.addCol(name, attributes);
                    break;
                case constraint:
                    tableCommand.addConstraint(name, attributes);
                    break;
                case index:
                    break;
                case database:
                    break;
                default:
                    throw new SAXException("Unkown XML Element:" + qName);
            }
        } catch (SAXException ex) {
            throw ex;
        } catch (Throwable ex) {
            SAXException saxEx = new SAXException(ex.getMessage());
            saxEx.initCause(ex);
            throw saxEx;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if ("table".equals(qName)) {
                tableCommand.execute();
                tableCommand = null;
            }
        } catch (Throwable ex) {
            SAXException saxEx = new SAXException(ex.getMessage());
            saxEx.initCause(ex);
            throw saxEx;
        }
    }

    private enum QualifiedNames {

        database, table, col, constraint, index
    }
}
