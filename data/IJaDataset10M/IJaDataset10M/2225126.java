package testsSrc;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import junit.framework.TestCase;
import axs.jdbc.dataSourceConfiguration.JdbcSourceConfiguration;
import axs.jdbc.dataSourceConfiguration.SpecificationException;

/**
 * This class is meant to test the WSV driver, its methods can be invoked by junit. WSV is a JDBC driver that allows to have a SQL interface
 * over data files. For a file of data you can specify a set of properties having to do with connection to the 
 * file of data and to data characteristics. These properties are:
 * - charset			   the set of characters data are written with.<br>
 * - fileExtension		this can be everything (it can even be empty).<br>
 * - separator				it is a single character (e.g.: ';' or '\t') or fixed_length (in this case data are not separated: each column has a fixed length set of characters in the data file).<br>
 * - suppressHeaders    this can be used to specify that the first line of the data file has not data but column names instead.<br>
 * - columnNames			they can be explicitly specified (e.g.: SOCIETA;CSAMPERS;COGNOME;NOME;HID;MAGNETICA) or desumed by the first line of the data file.<br>
 * - columnTypes        they can be explicitly specified as integer constants (e.g.: 4;12;12;12;12;12) according to this mapping: <br><br>
 *<table width="20%" border="1" align="center">
  *  <tr>     <td width="36%"><b>NAME</b></td>    <td width="50%"><b>Java type</b></td>    <td width="14%"><b>TYPE</b></td>  </tr>
  *  <tr>    <td width="36%">VARCHAR</td>    <td width="50%">String.class</td>    <td width="14%">12</td>  </tr>
  *  <tr>    <td width="36%">BOOLEAN</td>    <td width="50%">Boolean.class</td>    <td width="14%">16</td>  </tr>
  *  <tr>    <td width="36%">TINYINT</td>    <td width="50%">Byte.class</td>    <td width="14%">-6</td>  </tr>
  *  <tr>    <td width="36%">CHAR</td>    <td width="50%">Character.class</td>    <td width="14%">1</td>  </tr>
  *  <tr>    <td width="36%">DOUBLE</td>    <td width="50%">Double.class</td>    <td width="14%">8</td>  </tr>
  *  <tr>    <td width="36%">FLOAT</td>    <td width="50%">Float.class</td>    <td width="14%">6</td>  </tr>
  *  <tr>    <td width="36%">INTEGER</td>    <td width="50%">Integer.class</td>    <td width="14%">4</td>  </tr>
  *  <tr>    <td width="36%">BIGINT</td>    <td width="50%">Long.class</td>    <td width="14%">-5</td>  </tr>
  *  <tr>    <td width="36%">SMALLINT</td>    <td width="50%">Short.class</td>    <td width="14%">5</td>  </tr>
  *  <tr>    <td width="36%">TIMESTAMP</td>    <td width="50%">Timestamp.class</td>    <td width="14%">93</td>  </tr>
  *  <tr>    <td width="36%">DATE</td>    <td width="50%">Date.class</td>    <td width="14%">91</td>  </tr>
  *  <tr>    <td width="36%">TIME</td>    <td width="50%">Time.class</td>    <td width="14%">92</td>  </tr>
  *  <tr>    <td width="36%">VARBINARY</td>    <td width="50%">byte[].class</td>    <td width="14%">-3</td>  </tr>
  *  <tr>    <td width="36%">NULL</td>    <td width="50%">null</td>    <td width="14%">0</td>  </tr>
  *  <tr>    <td width="36%">JAVA_OBJECT</td>    <td width="50%">Object.class</td>    <td width="14%">2000</td>  </tr>
  *  <tr>    <td width="36%">P_BOOLEAN</td>    <td width="50%">boolean.class</td>    <td width="14%">100016</td>  </tr>
  *  <tr>    <td width="36%">P_TINYINT</td>    <td width="50%">byte.class</td>    <td width="14%">9994</td>  </tr>
  *  <tr>    <td width="36%">P_CHAR</td>    <td width="50%">char.class</td>    <td width="14%">100001</td>  </tr>
  *  <tr>    <td width="36%">P_DOUBLE</td>    <td width="50%">double.class</td>    <td width="14%">100008</td>  </tr>
  *  <tr>    <td width="36%">P_FLOAT</td>    <td width="50%">float.class</td>    <td width="14%">100006</td>  </tr>
  *  <tr>    <td width="36%">P_INTEGER</td>    <td width="50%">int.class</td>    <td width="14%">100004</td>  </tr>
  *  <tr>    <td width="36%">P_BIGINT</td>    <td width="50%">long.class</td>    <td width="14%">9995</td>  </tr>
  *  <tr>    <td width="36%">P_SMALLINT</td>    <td width="50%">short.class</td>    <td width="14%">100005</td>  </tr>
 *</table><br>
 * 							The types of columns can be specified either by their type integer identifiers or by their names.
 * 							If column types are not specified for the columns, they are assumed to be of type VARCHAR.<br>
 * - columnWidths			you can specify the maximum lenght of a VARCHAR column. In case of fixed lenght data, in the
 * 						   evaluated width of a column exceeds this width, then an exception is raised.<br>
 * - columnBoundaries   columns can be identified with fixed lenght mode. In this case each column hosts data in
 * 							a fixed lenght interval. In this case you must specify "fixed_length" as separator and define
 * 							the columns' intervals by their boundaries (e.g.: 1-3;4-6;7-16;17-22;23-23;24-33). Note: first
 * 					      character is at position 0.<br>
 */
public class WsvDriverTestSuite extends TestCase {

    final String FIRST_LINE_HAS_COL_NAMES = "false";

    /**
	 * Tests connection to plain text file data source (LOCAL URL).
	 * Getting specifications from file.
	 */
    public void testPropertiesFromSpecFileAndLocalUrl() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(1) >>> testing connection to plain text file data source (LOCAL URL)\n" + "- getting specifications from file\n");
        assertTrue(propertiesFromSpecFile(null));
        System.out.println("test done!\n");
    }

    /**
	 * Tests connection to plain text file data source (LOCAL URL).
	 * Gets specifications from file.
	 * Columns' names and types (testing getObject).
	 * Note: in table anagrafica there's an INTEGER field that is gathered as this string " 101". This is to test
	 * that balnks are not considered for numbers.
	 */
    public void testPropsFromSpecFileSelectAllNamesAndTypesAndLocalUrl() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(2) >>> testing connection to plain text file data source (LOCAL URL)\n" + "- getting specifications from file\n- selecting columns' names and types (testing getObject)\n");
        assertTrue(selectAllNamesAndTypes(null, null, null, null, null, "anagrafica", "", "", "", "", 0, null));
        System.out.println("test done!\n");
    }

    /**
	 * Tests connection to plain text file data source (LOCAL URL).
	 * Gets specifications from file.
	 * Counts rows (testing getObject).
	 */
    public void testCountLocalAndUrl(String tableName) {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(3)>>> testing connection to plain text file data source (LOCAL URL)\n" + "- getting specifications from file\n- counting rows (testing getObject)\n");
        String table = tableName == null || tableName.equals("") ? "anagrafica" : tableName;
        try {
            assertTrue(count(table));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("test done!\n");
    }

    /**
	 * Tests connection to plain text file data source (REMOTE URL).
	 * Gets specifications from file.
	 * Explicitly connects to jdbc:wsv:ftp://danpes:danpes@wsvTestPc:/pozzo/lavoro (overriding file specifications).
	 * For this test to be run you need to set the name 'wsvTestPc' in your HOSTS file (or in your DSN), to identify
	 * the pc exposing the data file inside your network. Then you must create an account for user 'danpes' with password 
	 * 'danpes'. You must also have the path /pozzo/lavoro under user danpes's home. Of course you can change these parameters
	 * according to your configuration.
	 */
    public void testConnectToRemoteUrl() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(4) >>> testing connection to plain text file data source (REMOTE URL)\n" + "- getting specifications from file\n" + "- explicitly connecting to jdbc:wsv:ftp://danpes:danpes@wsvTestPc:/pozzo/lavoro (overriding file specifications)\n");
        assertTrue(propertiesFromSpecFile("jdbc:wsv:ftp://danpes:danpes@wsvTestPc:/pozzo/lavoro"));
        System.out.println("test done!\n");
    }

    /**
	 * Tests connection to plain text file data source (REMOTE URL).
	 * Gets specifications from file.
	 * Columns' names and types (testing getObject).
	 * Explicitly connects to jdbc:wsv:ftp://danpes:danpes@wsvTestPc:/pozzo/lavoro (overriding file specifications).
	 * For this test to be run you need to set the name 'wsvTestPc' in your HOSTS file (or in your DSN), to identify
	 * the pc exposing the data file inside your network. Then you must create an account for user 'danpes' with password 
	 * 'danpes'. You must also have the path /pozzo/lavoro, having a data file named anagrafica, under user danpes's home. 
	 * Of course you can change these parameters according to your configuration.
	 */
    public void testSelectAllNamesAndTypesFromRemoteUrl() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(5) >>> testing connection to plain text file data source (REMOTE URL)\n" + "- getting specifications from file\n" + "- selecting columns' names and types (testing getObject)\n" + "- explicitly connecting to jdbc:wsv:ftp://danpes:danpes@wsvTestPc:/pozzo/lavoro (overriding file specifications)\n");
        assertTrue(selectAllNamesAndTypes("jdbc:wsv:ftp://danpes:danpes@wsvTestPc:/pozzo/lavoro", null, null, null, null, "anagrafica", "", "", "", "", 0, null));
        System.out.println("test done!\n");
    }

    /**
	 * Tests connection to plain text file data source (LOCAL URL).
	 * Explicitly passing specifications (excluding columns specifications).
	 */
    public void testConnectionToLocalUrlByExplicitProperiesPassingWithoutColNamesAndTypes() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(6) >>> testing connection to plain text file data source (LOCAL URL)\n" + "- explicitly passing specifications (excluding columns specifications)\n");
        assertTrue(explicitlyPassedProperties(null, false, false, false, true, "anagrafica", "", "", "", "", 0));
        System.out.println("test done!\n");
    }

    /**
	 * Tests connection to plain text file data source (LOCAL URL).
	 * Explicitly passing specifications (complete).
	 */
    public void testConnectionToLocalUrlByExplicitProperiesPassingWithCols() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(7) >>> testing connection to plain text file data source (LOCAL URL)\n" + "- explicitly passing specifications (complete)\n");
        assertTrue(explicitlyPassedProperties(null, true, true, true, true, "anagrafica", "", "", "", "", 0));
        System.out.println("test done!\n");
    }

    /**
	 * Tests connection to plain text file data source (LOCAL URL).
	 * Explicitly passing specifications (with just column boundaries): all columns will be named using COLUMNi 
	 * and will have type VARCHAR.
	 */
    public void testExplicitProperiesPassingWithoutColNamesAndTypesSelectAllNamesAndTypesAndLocalUrl() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(8) >>> testing connection to plain text file data source (LOCAL URL)\n" + "- explicitly passing specifications (with just column boundaries) -> all columns will be named using COLUMNi and will have type VARCHAR\n");
        assertTrue(selectAllNamesAndTypes(null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, "anagrafica", "", "", "", "", 0, null));
        System.out.println("test done!\n");
    }

    /**
	 * Tests connection to plain text file data source (LOCAL URL).
	 *	Explicitly passing specifications (with no columns) -> all columns will be VARCHAR.
	 *	The first line sets column names.
	 *	Data file's extension is different from '.wsv'.
	 *	Separator is ';'.
	 */
    public void testExplicitProperiesPassingWithoutCols() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(9) >>> testing connection to plain text file data source (LOCAL URL)\n" + "- explicitly passing specifications (with no columns) -> all columns will be VARCHAR\n" + "- first line sets column names\n" + "- data file's extension is different from '.wsv'\n" + "- separator is ';'\n");
        assertTrue(selectAllNamesAndTypes(null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "anagraficaWithHeaders", "ISOOA-8859-1", ".csv", ";", FIRST_LINE_HAS_COL_NAMES, 0, null));
        System.out.println("test done!\n");
    }

    /**
	 * testing connection to plain text file data source (LOCAL URL).
	 * Explicitly passing specifications (with no columns) -> all columns will be VARCHAR.
	 * The first line sets column names.
	 * Data file's extension is different from '.wsv'.
	 * Separator is TAB.
	 * The driver is set as a system property and not explicitly loaded
	 */
    public void testConnectionAnsSelectWithNoExplicitDriverLoading() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(10) >>> testing connection to plain text file data source (LOCAL URL)\n" + "- explicitly passing specifications (with no columns) -> all columns will be VARCHAR\n" + "- first line sets column names\n" + "- data file's extension is different from '.wsv'\n" + "- separator is TAB\n" + "- the driver is set as a system property and not explicitly loaded\n");
        assertTrue(connectionAnsSelectWithNoExplicitDriverLoading());
        System.out.println("\ntest done!\n");
    }

    /**
	 * Tests connection to plain text file data source (LOCAL invalid URL).
	 * Gets specifications from file.
	 * Explicitly connects to jdbc:wsv:file://<ej�fqoprjep (overriding file specifications).	 
	 */
    public void testConnectToLocalInvalidUrl() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(11) >>> testing connection to plain text file data source (LOCAL URL)\n" + "- the URL is Malformed 'cause it doesn't point to an existing directory\n" + "- getting specifications from file\n" + "- explicitly connecting to jdbc:wsv:file://<ej�fqoprjep (overriding file specifications)\n");
        assertFalse(propertiesFromSpecFile("jdbc:wsv:file://<ej�fqoprjep"));
        System.out.println("test done!\n");
    }

    /**
	 * Tests connection to plain text file data source (LOCAL invalid URL).
	 * Gets specifications from file.
	 * Explicitly connects to jdbc:wsv:file://./tests/schema.ini (overriding file specifications).	 
	 */
    public void testConnectToLocalInvalidUrl2() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(12) >>> testing connection to plain text file data source (LOCAL URL)\n" + "- the URL is Malformed 'cause it doesn't point to an existing directory (it points to an existing file but not do a directory)\n" + "- getting specifications from file\n" + "- explicitly connecting to jdbc:wsv:file://./tests/schema.ini (overriding file specifications)\n");
        assertFalse(propertiesFromSpecFile("jdbc:wsv:file://./tests/schema.ini"));
        System.out.println("test done!\n");
    }

    /**
	 * Tests connection to plain text file data source (LOCAL URL).
	 * Explicitly passing specifications 
	 * Counts rows (testing getObject).
	 */
    public void testLocalUrlSkippingLines() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(13)>>> testing connection to plain text file data source (LOCAL URL)\n" + "- getting specifications from file\n" + "- counting rows (testing getObject)\n");
        final int HOW_MANY_LINES_TO_SKIP = 3;
        try {
            assertTrue(selectAllNamesAndTypes(null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "anagraficaWithHeadersAndLinesToSkip", "ISOOA-8859-1", ".wsv", ";", FIRST_LINE_HAS_COL_NAMES, HOW_MANY_LINES_TO_SKIP, null));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("test done!\n");
    }

    /**
	 * Tests connection to plain text file data source (LOCAL URL: anagraficaTypes.wsv).
	 * Gets specifications from file having name different from schema.ini (schemaTypes.abc).
	 * Columns' names and TYPES (testing getObject).
	 * Separator is ;
	 * Note: 0 is specified as boolean and recognized as false 
	 */
    public void testTypesPropsFromSpecFileSelectAllNamesAndTypesAndLocalUrl() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(14) >>> testing connection to plain text file data source (LOCAL URL: anagraficaTypes.wsv)\n" + "- specifications from file (schemaTypes.ini)\n" + "- selecting specifications from file (schemaTypes.abc)\n" + "- Note: 0 is specified as boolean and recognized as false\n");
        assertTrue(selectAllNamesAndTypes(null, null, null, null, null, "anagraficaTypes", "", ".wsv", ";", FIRST_LINE_HAS_COL_NAMES, 0, "schemaTypes.abc"));
        System.out.println("test done!\n");
    }

    /**
    * Tests connection to plain text file data source (LOCAL URL).
    * Gets specifications from file.
    * Columns' names and types (testing getObject).
    * Note: this is to test trimming for foxed length fields
    * that balnks are not considered for numbers.
    */
    public void testPropsFromSpecFileSelectAllNamesAndTypesAndLocalUrlWithTrim() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("(15) >>> testing connection to plain text file data source (LOCAL URL)\n" + "- getting specifications from file\n- selecting columns' names and types (testing getObject)\n" + "- this is to test trimming for foxed length fields\n" + "- selecting specifications from file (schema_Anagrafica.txt)\n");
        assertTrue(selectAllNamesAndTypes(null, null, null, null, null, "Anagrafica", null, ".dat", null, null, 0, "schema_Anagrafica.txt"));
        System.out.println("test done!\n");
    }

    private boolean propertiesFromSpecFile(String url) {
        boolean result = true;
        try {
            Connection conn = getConnectionFromPropertiesFile(url, null);
            if (conn != null) conn.close();
        } catch (Exception e) {
            result = false;
            System.out.println(e.getMessage());
        }
        return result;
    }

    private boolean explicitlyPassedProperties(String url, boolean withColumnNames, boolean withColumnTypes, boolean withColumnWidths, boolean withColumnBoundaries, String nameOfInputTable, String charset, String fileExtension, String separator, String suppressHeaders, int skipLines) {
        boolean result = true;
        try {
            Connection conn = connectByExplicitProperties(url, withColumnNames, withColumnTypes, withColumnWidths, withColumnBoundaries, nameOfInputTable, charset, fileExtension, separator, suppressHeaders, skipLines);
            if (conn != null) {
                System.out.println("Got connection to plain text source data file (" + conn.toString() + ")");
                conn.close();
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    private Connection getConnectionFromPropertiesFile(String url, String specFileName) throws SpecificationException, ClassNotFoundException, SQLException {
        Properties props;
        PrintWriter log = new PrintWriter(System.out, false);
        DriverManager.setLogWriter(log);
        Connection conn = null;
        JdbcSourceConfiguration cf;
        props = new java.util.Properties();
        File f = new File(".");
        if (f.canRead()) {
            System.out.println("current working dir is " + f.getAbsoluteFile().toString());
        }
        String wd = f.getAbsoluteFile().toString();
        wd = wd.substring(0, wd.length() - 1) + "tests";
        specFileName = specFileName == null ? "/schema.ini" : "/" + specFileName;
        cf = new JdbcSourceConfiguration(wd + specFileName);
        props = cf.getConnectionProperties();
        System.out.println("properties got from specification file are:\n" + props + "\n");
        System.out.println("the DRIVER is: " + props.getProperty("jdbc.sourceDriver"));
        Class.forName(props.getProperty("jdbc.sourceDriver"));
        url = url == null ? props.getProperty("jdbc.sourceUrl") : url;
        System.out.println("the URL is: " + url);
        conn = DriverManager.getConnection(url, props);
        return conn;
    }

    private boolean selectAllNamesAndTypes(String url, Boolean passColumnNames, Boolean passCollumnTypes, Boolean passColumnWidths, Boolean passColumnBoundaries, String nameOfInputTable, String charset, String fileExtension, String separator, String suppressHeaders, int skipLines, String schemaFileName) {
        boolean result = true;
        try {
            Connection conn;
            if ((passColumnNames != null || passCollumnTypes != null || passColumnWidths != null || passColumnBoundaries != null) && (schemaFileName == null)) conn = connectByExplicitProperties(url, passColumnNames.booleanValue(), passCollumnTypes.booleanValue(), passColumnWidths.booleanValue(), passColumnBoundaries.booleanValue(), nameOfInputTable, charset, fileExtension, separator, suppressHeaders, skipLines); else {
                conn = getConnectionFromPropertiesFile(url, schemaFileName);
            }
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + nameOfInputTable);
            int i = 0;
            Object recCol;
            while (rs.next()) {
                i++;
                System.out.println("record number " + i + " has " + rs.getMetaData().getColumnCount() + " columns");
                for (int j = 1; j <= rs.getMetaData().getColumnCount(); j++) {
                    recCol = rs.getObject(j);
                    System.out.print(rs.getMetaData().getColumnLabel(j) + " = " + recCol + " - type = " + rs.getMetaData().getColumnTypeName(j) + "\n");
                }
                System.out.println();
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            result = false;
            System.out.println(e.getMessage());
        }
        return result;
    }

    private boolean count(String tableName) throws Exception {
        boolean result = true;
        try {
            Connection conn = getConnectionFromPropertiesFile(null, null);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
            int i = 0;
            Object recCol;
            while (rs.next()) {
                i++;
                System.out.println("record number" + i + " has " + rs.getMetaData().getColumnCount() + " columns");
                for (int j = 1; j <= rs.getMetaData().getColumnCount(); j++) {
                    recCol = rs.getObject(j);
                    System.out.print(rs.getMetaData().getColumnLabel(j) + " = " + recCol + "  -  type = " + rs.getMetaData().getColumnTypeName(j) + "\n");
                }
                System.out.println();
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return result;
    }

    private Connection connectByExplicitProperties(String url, boolean passColumnNames, boolean passCollumnTypes, boolean passColumnWidths, boolean passColumnBoundaries, String nameOfInputTable, String charset, String fileExtension, String separator, String suppressHeaders, int skipLines) throws Exception {
        Connection conn = null;
        Properties props = new Properties();
        nameOfInputTable = nameOfInputTable == null || nameOfInputTable.equals("") ? "anagrafica" : nameOfInputTable;
        charset = charset == null || charset.equals("") ? "ISOOA-8859-1" : charset;
        fileExtension = fileExtension == null || fileExtension.equals("") ? ".wsv" : fileExtension;
        separator = separator == null || separator.equals("") ? "fixed_length" : separator;
        suppressHeaders = suppressHeaders == null || suppressHeaders.equals("") ? "true" : suppressHeaders;
        props.setProperty("jdbc.sourceNameOfInputTable", nameOfInputTable);
        props.setProperty("jdbc.sourceDriver", "axs.jdbc.driver.WsvDriver");
        File f = new File(".");
        if (f.canRead()) {
            System.out.println("current working dir is " + f.getAbsoluteFile().toString());
        }
        String wd = f.getAbsoluteFile().toString();
        wd = wd.substring(0, wd.length() - 1) + "tests";
        props.setProperty("jdbc.sourceUrl", "jdbc:wsv:file://" + wd);
        props.setProperty("charset", charset);
        props.setProperty("fileExtension", fileExtension);
        props.setProperty("separator", separator);
        props.setProperty("suppressHeaders", suppressHeaders);
        props.setProperty("skipLines", String.valueOf(skipLines));
        if (passColumnNames) {
            props.setProperty("columnNames", "SOCIETA;CSAMPERS;COGNOME;NOME;HID;MAGNETICA;GRUPPO;STATUS;DIV;QUAL;SETTORE;DIPENDENZA;UBICAZIONE;TIPO_ABILITAZIONE;IDENTIFIER;EXP_TRANS;HID_ID;TYPEID");
        }
        if (passCollumnTypes) {
            props.setProperty("columnTypes", "4;12;12;12;12;12;12;12;4;12;4;4;12;12;12;12;4;4");
        }
        if (passColumnWidths) {
            props.setProperty("columnWidths", "null;255;255;255;255;255;255;255;null;255;null;null;255;255;255;1;null;null");
        }
        if (passColumnBoundaries) {
            props.setProperty("columnBoundaries", "1-3;4-6;7-16;17-22;23-23;24-33;34-52;53-60;61-61;62-62;63-64;65-68;69-74;75-79;80-80;81-81;82-82;83-83");
        }
        System.out.println("the DRIVER is: " + props.getProperty("jdbc.sourceDriver"));
        try {
            Class.forName(props.getProperty("jdbc.sourceDriver"));
            url = url == null ? props.getProperty("jdbc.sourceUrl") : url;
            System.out.println("the URL is: " + url);
            conn = DriverManager.getConnection(url, props);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return conn;
    }

    private boolean connectionAnsSelectWithNoExplicitDriverLoading() {
        boolean result = true;
        PrintWriter log = new PrintWriter(System.out, false);
        DriverManager.setLogWriter(log);
        try {
            Properties props = new java.util.Properties();
            props.put("separator", "\t");
            props.put("suppressHeaders", "false");
            props.put("fileExtension", ".wsv");
            props.put("charset", "ISO-8859-1");
            File f = new File(".");
            if (f.canRead()) {
                System.out.println("current working dir is " + f.getAbsoluteFile().toString());
            }
            String wd = f.getAbsoluteFile().toString();
            wd = wd.substring(0, wd.length() - 1) + "tests";
            props.setProperty("jdbc.sourceUrl", "jdbc:wsv:file://" + wd);
            System.out.println("properties:\n" + props);
            System.setProperty("jdbc.drivers", "axs.jdbc.driver.WsvDriver");
            Connection conn = DriverManager.getConnection("jdbc:wsv:file://./tests", props);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM anagraficaWithHeadersAndTabs");
            int i = 0;
            Object recCol;
            while (rs.next()) {
                i++;
                System.out.println("record number" + i + " has " + rs.getMetaData().getColumnCount() + " columns");
                for (int j = 1; j <= rs.getMetaData().getColumnCount(); j++) {
                    recCol = rs.getObject(j);
                    System.out.print(rs.getMetaData().getColumnLabel(j) + " = " + recCol + "\n");
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (RuntimeException e) {
            System.out.println("  --  " + log + "\n  --  " + e.getMessage() + "\n  --  " + e.getStackTrace());
            result = false;
        } catch (SQLException e) {
            System.out.println("  --  " + log + "\n  --  " + e.getMessage() + "\n  --  " + e.getStackTrace());
            result = false;
        }
        return result;
    }
}
