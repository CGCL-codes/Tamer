package org.achup.generador.project.writer.datasource;

import java.io.File;
import java.util.List;
import org.achup.generador.datasource.jdbc.JDBCDataSource;
import org.achup.generador.datasource.jdbc.JDBCProperties;
import org.achup.generador.project.writer.ElementWriter;
import org.achup.generador.project.writer.ElementWriterException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Marco Bassaletti Olivos.
 */
public class JDBCDataSourceWriter implements ElementWriter<JDBCDataSource> {

    public Element createXML(Document document, JDBCDataSource object) throws ElementWriterException {
        Element element = document.createElement("jdbc");
        Element childElement = null;
        Element driverElement = document.createElement("driver");
        JDBCProperties properties = object.getProperties();
        if (properties != null) {
            List<File> jarFiles = properties.getJarFiles();
            for (int i = 0; i < jarFiles.size(); i++) {
                childElement = document.createElement("jar");
                File file = jarFiles.get(i);
                childElement.setTextContent(file.getAbsolutePath());
                driverElement.appendChild(childElement);
            }
            List<File> dirFiles = properties.getNativeDirs();
            for (int i = 0; i < dirFiles.size(); i++) {
                childElement = document.createElement("native");
                File file = dirFiles.get(i);
                childElement.setTextContent(file.getAbsolutePath());
                driverElement.appendChild(childElement);
            }
            String clazz = properties.getDriverClass();
            if (clazz != null && clazz.trim().length() > 0) {
                clazz = clazz.trim();
                childElement = document.createElement("class");
                childElement.setTextContent(clazz);
                driverElement.appendChild(childElement);
            }
            element.appendChild(driverElement);
            Element connElement = document.createElement("connection");
            String catalog = properties.getCatalog();
            String schema = properties.getSchema();
            String url = properties.getUrl();
            String username = properties.getUsername();
            String password = properties.getPassword();
            if (catalog != null && catalog.trim().length() > 0) {
                catalog = catalog.trim();
                childElement = document.createElement("catalog");
                childElement.setTextContent(catalog);
                connElement.appendChild(childElement);
            }
            if (schema != null && schema.trim().length() > 0) {
                schema = schema.trim();
                childElement = document.createElement("schema");
                childElement.setTextContent(schema);
                connElement.appendChild(childElement);
            }
            if (url != null && url.trim().length() > 0) {
                url = url.trim();
                childElement = document.createElement("url");
                childElement.setTextContent(url);
                connElement.appendChild(childElement);
            }
            if (username != null && username.trim().length() > 0) {
                username = username.trim();
                childElement = document.createElement("username");
                childElement.setTextContent(username);
                connElement.appendChild(childElement);
            }
            if (password != null && password.trim().length() > 0) {
                password = catalog.trim();
                childElement = document.createElement("password");
                childElement.setTextContent(password);
                connElement.appendChild(childElement);
            }
            element.appendChild(connElement);
        }
        return element;
    }
}
