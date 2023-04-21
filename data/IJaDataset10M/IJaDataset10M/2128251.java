package de.catsoft.rdbs2j.db.db2;

import java.util.*;
import de.catsoft.rdbs2j.db.SqlParser;
import org.w3c.dom.Document;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
import de.catsoft.rdbs2j.db.DBException;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import de.catsoft.rdbs2j.db.DBParserException;
import java.io.Reader;
import de.catsoft.rdbs2j.db.XMLGenerationException;

/**
 * Diese Klasse liest ein Datenbank beschreibung im DB2 SQL Format und wandelt
 * diese in ein DOM zur erzeugung enes ObjectModel um.
 * @author <a href="mailto:GBeutler@cat-gmbh.de">Guido Beutler</a>
 * @version 1.0
 * @copyright (c)2002,2003,2004 by CAT Computer Anwendung Technologie GmbH
 * Oststr. 34
 * 50374 Erftstadt
 * Germany
 *
 * @license This file is part of RDBS2J.
 *
 * RDBS2J is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * RDBS2J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RDBS2J; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
public class DB2Parser implements SqlParser {

    private String _filename = null;

    private String _source_package = "de.catsoft.rdbs2j.testmodel";

    private String _schema_name = "testmodel";

    private File _file = null;

    private SQLElementFactory _sQLElementFactory = new SQLElementFactory();

    private Collection _sQLTable = new ArrayList();

    /**
    * Map der Tabellennamen.
    * Die Map wird als Index �ber die tabellennamen alle Tabellen benutzt. Damit wird
    * eine sequenzielle Suche in _sQLTable bei einem Alter Table vermieden.
    */
    private Map _sQLTableIndex = new HashMap();

    /**
    * @roseuid 3C6F977701F0
    */
    public DB2Parser() {
    }

    /**
    * Access method for the _filename property.
    *
    * @return   the current value of the _filename property
    */
    public String get_filename() {
        return _filename;
    }

    /**
    * Sets the value of the _filename property.
    *
    * @param a_filename the new value of the _filename property
    */
    public void set_filename(String a_filename) {
        _filename = a_filename;
    }

    /**
    * Access method for the _source_package property.
    *
    * @return   the current value of the _source_package property
    */
    public String get_source_package() {
        return _source_package;
    }

    /**
    * Sets the value of the _source_package property.
    *
    * @param a_source_package the new value of the _source_package property
    */
    public void set_source_package(String a_source_package) {
        _source_package = a_source_package;
    }

    /**
    * Access method for the _schema_name property.
    *
    * @return   the current value of the _schema_name property
    */
    public String get_schema_name() {
        return _schema_name;
    }

    /**
    * Sets the value of the _schema_name property.
    *
    * @param a_schema_name the new value of the _schema_name property
    */
    public void set_schema_name(String a_schema_name) {
        _schema_name = a_schema_name;
    }

    /**
    * Access method for the _file property.
    *
    * @return   the current value of the _file property
    */
    public File get_file() {
        return _file;
    }

    /**
    * Sets the value of the _file property.
    *
    * @param a_file the new value of the _file property
    */
    public void set_file(File a_file) {
        _file = a_file;
    }

    /**
    * @param name
    * @roseuid 3C6F9777020E
    */
    public void setFilename(String name) {
        set_filename(name);
    }

    /**
    * @return org.w3c.dom.Document
    * @throws javax.xml.parsers.ParserConfigurationException
    * @throws org.xml.sax.SAXException
    * @throws java.io.IOException
    * @throws de.catsoft.rdbs2j.db.DBException
    * @roseuid 3C6F9777024A
    */
    public Document getrdbs2jDOM() throws ParserConfigurationException, SAXException, IOException, DBException {
        parse();
        return (new RDBS2JDOMBuilder(_source_package, _schema_name, _sQLTable)).getrdbs2jDOM();
    }

    /**
    * @throws java.io.FileNotFoundException
    * @throws de.catsoft.rdbs2j.db.DBParserException
    * @roseuid 3C7279440076
    */
    public void parse() throws FileNotFoundException, DBParserException {
        if (_filename != null && _file != null) {
            throw new DBParserException("Entweder Datei oder File Attribut setzen. Ung�ltige Konfiguration");
        }
        ArrayList element_list = null;
        if (_file != null) {
            element_list = tableToElements(new FileReader(_file));
        } else {
            element_list = tableToElements(new FileReader(_filename));
        }
        ListIterator iter = element_list.listIterator();
        _sQLTable.clear();
        try {
            SQLElement[] element = new SQLElement[2];
            String tablename = null;
            while (iter.hasNext()) {
                try {
                    element[0] = _sQLElementFactory.getSQLElement((String) iter.next());
                    if (!(element[0] instanceof SQLCreate) && !(element[0] instanceof SQLAlter)) {
                        System.out.println("Skiped : " + element[0]);
                        continue;
                    }
                    element[1] = _sQLElementFactory.getSQLElement((String) iter.next());
                    if (!(element[1] instanceof SQLCTable)) {
                        System.out.println("Skiped : " + element[1]);
                        continue;
                    }
                    if (!iter.hasNext()) {
                        break;
                    }
                    tablename = (String) iter.next();
                    iter.previous();
                    iter.previous();
                    iter.previous();
                } catch (UnknownSQLElementException e) {
                    System.out.println("Skiped : " + e.getMessage());
                    continue;
                }
                SQLTable acct_table = null;
                if (element[0] instanceof SQLCreate) {
                    acct_table = new SQLTable();
                    acct_table.handleInteratorInput(iter, _sQLElementFactory);
                    _sQLTable.add(acct_table);
                    if (acct_table.get_schema() == null) {
                        _sQLTableIndex.put(acct_table.get_name(), acct_table);
                    } else {
                        _sQLTableIndex.put(acct_table.get_schema() + "." + acct_table.get_name(), acct_table);
                    }
                } else {
                    acct_table = (SQLTable) _sQLTableIndex.get(tablename);
                    acct_table.handleInteratorInput(iter, _sQLElementFactory);
                }
            }
        } catch (DBParserException e) {
            int pos = iter.nextIndex();
            pos--;
            System.out.println("Fehler bei Element " + pos);
            for (int i = 0; i <= pos; i++) {
                System.out.println(element_list.get(i));
            }
            e.printStackTrace();
            throw e;
        }
    }

    /**
    * @param reader
    * @return java.util.ArrayList
    * @throws de.catsoft.rdbs2j.db.db2.UnknownSQLElementException
    * @roseuid 3C736F3D0036
    */
    private ArrayList tableToElements(Reader reader) throws UnknownSQLElementException {
        ArrayList result = new ArrayList();
        SQLStringEnumeration enumeration = new SQLStringEnumeration(reader);
        while (enumeration.hasMoreElements()) {
            result.add(enumeration.nextElement());
        }
        return result;
    }

    /**
    * @param file
    * @roseuid 3C94731E0227
    */
    public void setFile(File file) {
        _file = file;
    }

    /**
    * @return java.lang.String
    * @throws de.catsoft.rdbs2j.db.XMLGenerationException
    * @roseuid 3D9C8BFC0153
    */
    public String buildXMLDocument() throws XMLGenerationException {
        return (new RDBS2JDOMBuilder(_source_package, _schema_name, _sQLTable)).buildXMLDocument();
    }
}
