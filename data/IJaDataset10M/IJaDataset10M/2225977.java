package com.diegosilva.anydump;

import com.diegosilva.anydump.domain.Synchro;
import com.diegosilva.anydump.domain.Table;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author DSILVA
 */
public class Main {

    private Properties props;

    static final Logger logger = Logger.getLogger(Main.class.getName());

    static final String XML_TAG = "synchro";

    static final String TABLE_TAG = "table";

    static final String NAME_ATTR = "name";

    private static final String ROW_TAG = "row";

    private static final String COLUMN_TAG = "field";

    static final String WHERE_ATTR = "where";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main().start();
    }

    private NodeList batchElements;

    private int cuentaBatchElements;

    private int indexBatchElements;

    private Connection getConnection() {
        logger.log(Level.INFO, "Conectando a la base de datos");
        FileInputStream fis = null;
        try {
            props = new Properties();
            File file = new File(System.getProperty("user.dir"), "conf.ini");
            fis = new FileInputStream(file);
            props.load(fis);
            Class.forName(props.getProperty("driver"));
            conn = DriverManager.getConnection(props.getProperty("url"), props.getProperty("user"), props.getProperty("password"));
            logger.log(Level.INFO, "Se conecto a la base de datos satisfactoriamente");
            return conn;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    Document xmlScanDoc;

    private Synchro getXML() {
        NodeList tableElements;
        Synchro synchro = new Synchro();
        try {
            if (xmlScanDoc == null) {
                File file = new File(System.getProperty("user.dir"), "synchro.xml");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                try {
                    builder = factory.newDocumentBuilder();
                    xmlScanDoc = builder.parse(file);
                    NodeList rootElems = xmlScanDoc.getElementsByTagName("siv21synchro");
                    String sDelete = ((Element) rootElems.item(0)).getAttribute("deleteOnExit");
                    synchro.setDeleteOnExist("true".equals(sDelete));
                    batchElements = xmlScanDoc.getElementsByTagName("batch");
                    cuentaBatchElements = batchElements.getLength();
                } catch (ParserConfigurationException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            if (batchElements == null) {
                return null;
            }
            if (indexBatchElements < cuentaBatchElements) {
                Element batchElement = (Element) batchElements.item(indexBatchElements++);
                tableElements = batchElement.getElementsByTagName(TABLE_TAG);
                for (int i = 0; i < tableElements.getLength(); i++) {
                    if (tableElements.item(i) instanceof Element) {
                        Element tableElement = (Element) tableElements.item(i);
                        if (TABLE_TAG.equals(tableElement.getTagName())) {
                            Table table = new Table(tableElement.getAttribute(NAME_ATTR));
                            String whereAttr = tableElement.getAttribute(WHERE_ATTR);
                            if (whereAttr != null && !whereAttr.isEmpty()) {
                                table.setWhere(whereAttr);
                            }
                            synchro.addTable(table);
                        }
                    }
                }
                return synchro;
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Connection conn;

    private void start() {
        FileOutputStream fis = null;
        try {
            conn = getConnection();
            Synchro synchro = null;
            while ((synchro = getXML()) != null) {
                logger.info("Explorando la base de datos");
                logger.info("Creando  archivo");
                File file = File.createTempFile("dump", "sqlgz");
                if (synchro.isDeleteOnExist()) {
                    file.deleteOnExit();
                }
                fis = new FileOutputStream(file);
                GZIPOutputStream gzipout = new GZIPOutputStream(fis);
                PrintWriter pw = new PrintWriter(gzipout);
                for (Table table : synchro.getTables()) {
                    logger.info("Descargando la tabla:" + table.getName());
                    dumpTabla(table, pw);
                    pw.flush();
                }
                logger.info("Cerrando");
                gzipout.close();
                fis.close();
                logger.info("Terminado");
                logger.info("enviando el archivo");
                HttpClient client = new HttpClient();
                PostMethod method = new PostMethod(props.getProperty("url_synchro"));
                Part[] parts = { new FilePart("file", file) };
                method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
                int statusCode1 = client.executeMethod(method);
                logger.info("respuesta del servidor:(" + statusCode1 + ")" + method.getStatusLine());
                byte[] responseBody = method.getResponseBody();
                System.out.println(new String(responseBody));
                method.releaseConnection();
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    private void dumpTabla(Table tabla, PrintWriter pw) throws SQLException {
        pw.println("LOCK TABLES " + tabla.getName() + " WRITE;");
        pw.println("DELETE FROM " + tabla.getName() + " ;");
        String sql = "SELECT * FROM " + tabla.getName();
        if (tabla.getWhere() != null) {
            sql += " WHERE " + tabla.getWhere();
        }
        ResultSet res = conn.createStatement().executeQuery(sql);
        ResultSetMetaData metaData = res.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (res.next()) {
            pw.format("INSERT INTO %1s (", tabla.getName());
            for (int i = 1; i <= columnCount; i++) {
                String campo = metaData.getColumnName(i);
                if (i > 1) {
                    pw.print(',');
                }
                pw.print(campo);
            }
            pw.print(") VALUES (");
            for (int i = 1; i <= columnCount; i++) {
                if (i > 1) {
                    pw.print(',');
                }
                String value = res.getString(i);
                if (value != null) {
                    value = value.replace('\r', ' ').replace('\n', ' ').replace('\'', ' ');
                    pw.print(String.format("'%1s'", value));
                } else {
                    pw.print("null");
                }
            }
            pw.println(");");
            pw.flush();
        }
        pw.println("UNLOCK TABLES;");
    }
}
