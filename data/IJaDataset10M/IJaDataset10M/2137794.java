package org.apache.harmony.sql.internal.rowset;

import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import javax.sql.rowset.WebRowSet;
import javax.sql.rowset.spi.XmlWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlWriterImpl extends CachedRowSetWriter implements XmlWriter {

    public void writeXML(WebRowSet caller, Writer writer) throws SQLException {
        if (writer == null || caller == null || caller.getMetaData() == null) {
            throw new NullPointerException();
        }
        caller.beforeFirst();
        DocumentBuilder docBuidler = null;
        Document doc = null;
        try {
            docBuidler = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuidler.newDocument();
        } catch (ParserConfigurationException e) {
            SQLException ex = new SQLException();
            ex.initCause(e);
            throw ex;
        }
        Element rootElement = doc.createElement("webRowSet");
        rootElement.setAttribute("xmlns", "http://java.sun.com/xml/ns/jdbc");
        rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.setAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/jdbc http://java.sun.com/xml/ns/jdbc/webrowset.xsd");
        Element prop = doc.createElement("properties");
        rootElement.appendChild(prop);
        writeProperties(doc, prop, caller);
        ResultSetMetaData rsmd = caller.getMetaData();
        Element metadataEle = doc.createElement("metadata");
        int colCount = rsmd.getColumnCount();
        Element colCountEle = doc.createElement("column-count");
        colCountEle.setTextContent(Integer.toString(colCount));
        metadataEle.appendChild(colCountEle);
        for (int i = 1; i <= colCount; i++) {
            writeMetadataByCol(doc, metadataEle, rsmd, i);
        }
        rootElement.appendChild(metadataEle);
        Element data = doc.createElement("data");
        rootElement.appendChild(data);
        writeRowSetData(doc, data, caller);
        doc.appendChild(rootElement);
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource domSrc = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(writer);
            transformer.transform(domSrc, streamResult);
        } catch (TransformerConfigurationException e) {
            SQLException ex = new SQLException();
            ex.initCause(e);
            throw ex;
        } catch (TransformerFactoryConfigurationError e) {
            SQLException ex = new SQLException();
            ex.initCause(e);
            throw ex;
        } catch (TransformerException e) {
            SQLException ex = new SQLException();
            ex.initCause(e);
            throw ex;
        }
    }

    private void writeRowSetData(Document doc, Element data, WebRowSet caller) throws SQLException {
        boolean preShowDelted = caller.getShowDeleted();
        caller.setShowDeleted(true);
        caller.beforeFirst();
        while (caller.next()) {
            wirteRow(doc, data, caller);
        }
        caller.setShowDeleted(preShowDelted);
    }

    private void wirteRow(Document doc, Element data, WebRowSet caller) throws SQLException {
        Element row = null;
        if (caller.rowDeleted()) {
            row = doc.createElement("deleteRow");
        } else if (caller.rowInserted()) {
            row = doc.createElement("insertRow");
        } else if (caller.rowUpdated()) {
            row = doc.createElement("modifyRow");
        } else {
            row = doc.createElement("currentRow");
        }
        data.appendChild(row);
        for (int i = 1; i <= caller.getMetaData().getColumnCount(); ++i) {
            if (caller.columnUpdated(i)) {
                ResultSet result = caller.getOriginalRow();
                result.next();
                Object originalValue = result.getObject(i);
                Object value = caller.getObject(i);
                if (originalValue != null) {
                    int columnType = caller.getMetaData().getColumnType(i);
                    switch(columnType) {
                        case Types.DATE:
                            originalValue = Long.valueOf(result.getDate(i).getTime());
                            value = Long.valueOf(caller.getDate(i).getTime());
                            break;
                        case Types.TIME:
                            originalValue = Long.valueOf(result.getTime(i).getTime());
                            value = Long.valueOf(caller.getTime(i).getTime());
                            break;
                        case Types.TIMESTAMP:
                            originalValue = Long.valueOf(result.getTime(i).getTime());
                            value = Long.valueOf(caller.getTimestamp(i).getTime());
                            break;
                    }
                    if (columnType == Types.ARRAY || columnType == Types.BINARY || columnType == Types.BLOB || columnType == Types.CLOB || columnType == Types.REF || columnType == Types.STRUCT || columnType == Types.DISTINCT) {
                        originalValue = "";
                        value = "";
                    }
                }
                appendElement(row, doc, "columnValue", originalValue);
                appendElement(row, doc, "updateValue", value);
            } else {
                Object value = caller.getObject(i);
                if (value != null) {
                    int columnType = caller.getMetaData().getColumnType(i);
                    switch(columnType) {
                        case Types.DATE:
                            value = Long.valueOf(caller.getDate(i).getTime());
                            break;
                        case Types.TIME:
                            value = Long.valueOf(caller.getTime(i).getTime());
                            break;
                        case Types.TIMESTAMP:
                            value = Long.valueOf(caller.getTime(i).getTime());
                            break;
                    }
                    if (columnType == Types.ARRAY || columnType == Types.BINARY || columnType == Types.BLOB || columnType == Types.CLOB || columnType == Types.REF || columnType == Types.STRUCT || columnType == Types.DISTINCT) {
                        value = "";
                    }
                }
                appendElement(row, doc, "columnValue", value);
            }
        }
    }

    @SuppressWarnings("boxing")
    private void writeProperties(Document doc, Element prop, WebRowSet caller) throws SQLException {
        appendElement(prop, doc, "command", caller.getCommand());
        appendElement(prop, doc, "concurrency", caller.getConcurrency());
        appendElement(prop, doc, "datasource", caller.getDataSourceName());
        appendElement(prop, doc, "escape-processing", caller.getEscapeProcessing());
        appendElement(prop, doc, "fetch-direction", caller.getFetchDirection());
        appendElement(prop, doc, "fetch-size", caller.getFetchSize());
        appendElement(prop, doc, "isolation-level", caller.getTransactionIsolation());
        Element keyColumns = doc.createElement("key-columns");
        prop.appendChild(keyColumns);
        int[] indexes = caller.getKeyColumns();
        for (int i = 0; i < indexes.length; ++i) {
            appendElement(keyColumns, doc, "column", indexes[i]);
        }
        Element typeMap = doc.createElement("map");
        prop.appendChild(typeMap);
        if (caller.getTypeMap() != null) {
            for (Iterator<String> iter = caller.getTypeMap().keySet().iterator(); iter.hasNext(); ) {
                String key = iter.next();
                appendElement(typeMap, doc, "type", key);
                appendElement(typeMap, doc, "class", caller.getTypeMap().get(key).getName());
            }
        }
        appendElement(prop, doc, "max-field-size", caller.getMaxFieldSize());
        appendElement(prop, doc, "max-rows", caller.getMaxRows());
        appendElement(prop, doc, "query-timeout", caller.getQueryTimeout());
        appendElement(prop, doc, "read-only", caller.isReadOnly());
        String rowsetType = null;
        switch(caller.getType()) {
            case ResultSet.TYPE_FORWARD_ONLY:
                rowsetType = "ResultSet.TYPE_FORWARD_ONLY";
                break;
            case ResultSet.TYPE_SCROLL_INSENSITIVE:
                rowsetType = "ResultSet.TYPE_SCROLL_INSENSITIVE";
                break;
            case ResultSet.TYPE_SCROLL_SENSITIVE:
                rowsetType = "ResultSet.TYPE_SCROLL_SENSITIVE";
                break;
        }
        appendElement(prop, doc, "rowset-type", rowsetType);
        appendElement(prop, doc, "show-deleted", caller.getShowDeleted());
        appendElement(prop, doc, "table-name", caller.getTableName());
        appendElement(prop, doc, "url", caller.getUrl());
        Element provider = doc.createElement("sync-provider");
        prop.appendChild(provider);
        appendElement(provider, doc, "sync-provider-name", caller.getSyncProvider().getProviderID());
        appendElement(provider, doc, "sync-provider-vendor", caller.getSyncProvider().getVendor());
        appendElement(provider, doc, "sync-provider-version", caller.getSyncProvider().getVersion());
        appendElement(provider, doc, "sync-provider-grade", caller.getSyncProvider().getProviderGrade());
        appendElement(provider, doc, "data-source-lock", caller.getSyncProvider().getDataSourceLock());
    }

    private Element createElement(Document doc, String name, Object value) {
        Element element = doc.createElement(name);
        if (value == null) {
            element.appendChild(doc.createElement("null"));
        } else {
            element.setTextContent(value.toString());
        }
        return element;
    }

    private void appendElement(Element root, Document doc, String name, Object value) {
        Element child = createElement(doc, name, value);
        root.appendChild(child);
    }

    @SuppressWarnings("boxing")
    private void writeMetadataByCol(Document doc, Element ele, ResultSetMetaData rsmd, int colIndex) throws SQLException {
        Element colDefEle = doc.createElement("column-definition");
        appendElement(colDefEle, doc, "column-index", colIndex);
        appendElement(colDefEle, doc, "auto-increment", rsmd.isAutoIncrement(colIndex));
        appendElement(colDefEle, doc, "case-sensitive", rsmd.isCaseSensitive(colIndex));
        appendElement(colDefEle, doc, "currency", rsmd.isCurrency(colIndex));
        appendElement(colDefEle, doc, "nullable", rsmd.isNullable(colIndex));
        appendElement(colDefEle, doc, "signed", rsmd.isSigned(colIndex));
        appendElement(colDefEle, doc, "searchable", rsmd.isSearchable(colIndex));
        appendElement(colDefEle, doc, "column-display-size", rsmd.getColumnDisplaySize(colIndex));
        appendElement(colDefEle, doc, "column-label", rsmd.getColumnLabel(colIndex));
        appendElement(colDefEle, doc, "column-name", rsmd.getColumnName(colIndex));
        appendElement(colDefEle, doc, "schema-name", rsmd.getSchemaName(colIndex));
        appendElement(colDefEle, doc, "column-precision", rsmd.getPrecision(colIndex));
        appendElement(colDefEle, doc, "column-scale", rsmd.getScale(colIndex));
        appendElement(colDefEle, doc, "table-name", rsmd.getTableName(colIndex));
        appendElement(colDefEle, doc, "catalog-name", rsmd.getCatalogName(colIndex));
        appendElement(colDefEle, doc, "column-type", rsmd.getColumnType(colIndex));
        appendElement(colDefEle, doc, "column-type-name", rsmd.getColumnTypeName(colIndex));
        ele.appendChild(colDefEle);
    }
}
