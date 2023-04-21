package com.visitrend.ndvis.sql.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.visitrend.ndvis.io.XMLTags;
import com.visitrend.ndvis.io.XMLWritable;

public class DBDriverProfile implements XMLWritable {

    private String driverName = "";

    private String jarFile = "";

    private String jarFilePath = "";

    private String className = "";

    private String urlPrefix = "";

    public DBDriverProfile() {
    }

    public DBDriverProfile(String driverName, String jarFile, String jarPath, String className, String urlPrefix) {
        this.setDriverName(driverName);
        this.setJarFile(jarFile);
        this.setJarFilePath(jarPath);
        this.setClassName(className);
        this.setUrlPrefix(urlPrefix);
    }

    public DBDriverProfile(DBDriverProfile that) {
        this.setDriverName(that.getDriverName());
        this.setJarFile(that.getJarFile());
        this.setJarFilePath(that.getJarFilePath());
        this.setClassName(that.getClassName());
        this.setUrlPrefix(that.getUrlPrefix());
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setJarFile(String jarFile) {
        this.jarFile = jarFile;
    }

    public String getJarFile() {
        return jarFile;
    }

    public void setJarFilePath(String jarFilePath) {
        this.jarFilePath = jarFilePath;
    }

    public String getJarFilePath() {
        return jarFilePath;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void reset() {
        driverName = "";
        jarFile = "";
        jarFilePath = "";
        className = "";
        urlPrefix = "";
    }

    public String toString() {
        return driverName;
    }

    @Override
    public void writeToXML(Node n) {
        Document doc;
        if (n instanceof Document) {
            doc = (Document) n;
        } else {
            doc = n.getOwnerDocument();
        }
        Element e0 = doc.createElement(XMLTags.DB_DRIVER_PROFILE);
        Element driverNameElement = doc.createElement(DBDriverData.DRIVER_NAME.getName());
        driverNameElement.appendChild(doc.createTextNode(driverName));
        e0.appendChild(driverNameElement);
        Element jarFileElement = doc.createElement(DBDriverData.JAR_FILE.getName());
        jarFileElement.appendChild(doc.createTextNode(jarFile));
        e0.appendChild(jarFileElement);
        Element jarFilePathElement = doc.createElement(DBDriverData.JAR_FILE_PATH.getName());
        jarFilePathElement.appendChild(doc.createTextNode(jarFilePath));
        e0.appendChild(jarFilePathElement);
        Element classNameElement = doc.createElement(DBDriverData.CLASS_NAME.getName());
        classNameElement.appendChild(doc.createTextNode(className));
        e0.appendChild(classNameElement);
        Element urlPrefixElement = doc.createElement(DBDriverData.URL_PREFIX.getName());
        urlPrefixElement.appendChild(doc.createTextNode(urlPrefix));
        e0.appendChild(urlPrefixElement);
        n.appendChild(e0);
    }

    public static DBDriverProfile readFromXML(Element e) {
        if (!e.getTagName().equals(XMLTags.DB_DRIVER_PROFILE)) {
            return null;
        }
        NodeList nodes = e.getElementsByTagName(DBDriverData.DRIVER_NAME.getName());
        String driverName = nodes.item(0).getTextContent();
        nodes = e.getElementsByTagName(DBDriverData.JAR_FILE.getName());
        String jarFile = nodes.item(0).getTextContent();
        nodes = e.getElementsByTagName(DBDriverData.JAR_FILE_PATH.getName());
        String jarFilePath = nodes.item(0).getTextContent();
        nodes = e.getElementsByTagName(DBDriverData.CLASS_NAME.getName());
        String className = nodes.item(0).getTextContent();
        nodes = e.getElementsByTagName(DBDriverData.URL_PREFIX.getName());
        String urlPrefix = nodes.item(0).getTextContent();
        return (new DBDriverProfile(driverName, jarFile, jarFilePath, className, urlPrefix));
    }
}
