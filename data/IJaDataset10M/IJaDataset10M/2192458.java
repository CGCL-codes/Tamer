package com.programiraj.database;

import java.io.StringWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.programiraj.database.firebird.FirebirdDatabaseConnector;
import com.programiraj.database.h2.H2DatabaseConnector;
import com.programiraj.database.mssql.MicrosoftSQLServerDatabaseConnector;
import com.programiraj.database.mysql.MySqlDatabaseConnector;
import com.programiraj.database.oracle.OracleDatabaseConnector;
import com.programiraj.database.postgresql.PostgresqlDatabaseConnector;
import com.programiraj.util.Base64;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;

public class Executor {

    private Connection conn = null;

    private DatabaseConnector connector = null;

    public Executor(String server, String username, String password) throws SQLException {
        if (server.contains("mysql")) {
            connector = new MySqlDatabaseConnector();
            conn = connector.connect(server, username, password);
        } else if (server.contains("mssql")) {
            connector = new MicrosoftSQLServerDatabaseConnector();
            conn = connector.connect(server, username, password);
        } else if (server.contains("h2")) {
            connector = new H2DatabaseConnector();
            conn = connector.connect(server, username, password);
        } else if (server.contains("firebirdsql")) {
            connector = new FirebirdDatabaseConnector();
            conn = connector.connect(server, username, password);
        } else if (server.contains("oracle")) {
            connector = new OracleDatabaseConnector();
            conn = connector.connect(server, username, password);
        } else if (server.contains("postgresql")) {
            connector = new PostgresqlDatabaseConnector();
            conn = connector.connect(server, username, password);
        }
    }

    public String executeSelect(String query, boolean html) throws SQLException {
        ResultSet resultSet = null;
        ResultSetMetaData metadata = null;
        String result = null;
        try {
            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            metadata = resultSet.getMetaData();
            if (html) result = compileResultHTML(metadata, resultSet); else result = compileResultXML(metadata, resultSet);
        } catch (SQLException e) {
            result = "<?xml version=\"1.0\"?>\n<e>" + e.getMessage() + "</e>";
        } finally {
            conn.close();
        }
        return result;
    }

    public String executeUpdate(String query) throws SQLException {
        int success = 0;
        String result = null;
        try {
            Statement statement = conn.createStatement();
            success = statement.executeUpdate(query);
            result = "<?xml version=\"1.0\"?>\n<i>" + success + "</i>";
        } catch (SQLException e) {
            result = "<?xml version=\"1.0\"?>\n<e>" + e.getMessage() + "</e>";
        } finally {
            conn.close();
        }
        return result;
    }

    public String executeInsert(String query) throws SQLException {
        int success = 0;
        String result = null;
        try {
            Statement statement = conn.createStatement();
            success = statement.executeUpdate(query);
            result = "<?xml version=\"1.0\"?>\n<m>" + success + "</m>";
        } catch (SQLException e) {
            result = "<?xml version=\"1.0\"?>\n<e>" + e.getMessage() + "</e>";
        } finally {
            conn.close();
        }
        return result;
    }

    public String executeAlterDatabase(String query) throws SQLException {
        String result = null;
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            result = "<?xml version=\"1.0\"?>\n<u>Query executed!</u>";
        } catch (SQLException e) {
            result = "<?xml version=\"1.0\"?>\n<e>" + e.getMessage() + "</e>";
        } finally {
            conn.close();
        }
        return result;
    }

    private String compileResultXML(ResultSetMetaData metadata, ResultSet resultSet) throws SQLException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootEle = document.createElement("q");
            document.appendChild(rootEle);
            Element columns = document.createElement("cs");
            rootEle.appendChild(columns);
            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                Element columnMetaElement = document.createElement("cm");
                columns.appendChild(columnMetaElement);
                String fullClassName = metadata.getColumnClassName(i);
                String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
                className = className.toLowerCase();
                String columnName = metadata.getColumnName(i);
                if ("[B".equals(fullClassName)) {
                    Blob blob = resultSet.getBlob(i);
                    if (blob != null) className = "base64";
                }
                columnMetaElement.setAttribute("t", className);
                columnMetaElement.appendChild(document.createTextNode(columnName));
            }
            int counter = 0;
            Element rows = document.createElement("rs");
            rootEle.appendChild(rows);
            while (resultSet.next()) {
                Element row = document.createElement("r");
                row.setAttribute("n", String.valueOf(counter++));
                rows.appendChild(row);
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    String columnName = metadata.getColumnName(i);
                    String fullClassName = metadata.getColumnClassName(i);
                    String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
                    className = className.toLowerCase();
                    String value = null;
                    if ("[B".equals(fullClassName)) {
                        Blob blob = resultSet.getBlob(i);
                        if (blob != null) value = Base64.encodeBytes(blob.getBytes(1, (int) blob.length()));
                        className = "base64";
                    } else {
                        value = resultSet.getString(i);
                    }
                    if (value == null) value = "";
                    Element column = document.createElement(columnName);
                    column.appendChild(document.createTextNode(value));
                    row.appendChild(column);
                }
            }
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);
            String xmlString = sw.toString();
            return xmlString;
        } catch (Exception e) {
            e.printStackTrace();
            return "<?xml version=\"1.0\"?>\n<e>" + e.getMessage() + "</e>";
        }
    }

    private String compileResultHTML(ResultSetMetaData metadata, ResultSet resultSet) throws SQLException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootEle = document.createElement("table");
            document.appendChild(rootEle);
            Element columns = document.createElement("tr");
            rootEle.appendChild(columns);
            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                Element columnMetaElement = document.createElement("th");
                columns.appendChild(columnMetaElement);
                String fullClassName = metadata.getColumnClassName(i);
                String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
                className = className.toLowerCase();
                String columnName = metadata.getColumnName(i);
                if ("[B".equals(fullClassName)) {
                    Blob blob = resultSet.getBlob(i);
                    if (blob != null) className = "base64";
                }
                columnMetaElement.appendChild(document.createTextNode(columnName));
            }
            while (resultSet.next()) {
                Element row = document.createElement("tr");
                rootEle.appendChild(row);
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    String fullClassName = metadata.getColumnClassName(i);
                    String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
                    className = className.toLowerCase();
                    String value = null;
                    if ("[B".equals(fullClassName)) {
                        Blob blob = resultSet.getBlob(i);
                        if (blob != null) value = Base64.encodeBytes(blob.getBytes(1, (int) blob.length()));
                        className = "base64";
                    } else {
                        value = resultSet.getString(i);
                    }
                    if (value == null) value = "";
                    Element column = document.createElement("td");
                    column.appendChild(document.createTextNode(value));
                    row.appendChild(column);
                }
            }
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);
            String xmlString = sw.toString();
            return xmlString;
        } catch (Exception e) {
            e.printStackTrace();
            return "<?xml version=\"1.0\"?>\n<e>" + e.getMessage() + "</e>";
        }
    }
}
