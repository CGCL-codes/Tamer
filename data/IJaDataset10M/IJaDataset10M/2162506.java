package com.k_int.util.Repository;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Observer;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import org.apache.xerces.parsers.SAXParser;
import org.apache.log4j.Category;

public class XMLDataSource extends DefaultHandler implements DataSource {

    private boolean loaded = false;

    private String sourceURL;

    private Hashtable collections = new Hashtable();

    private Hashtable repositories = new Hashtable();

    private Hashtable coll_instances = new Hashtable();

    private RepositoryInformation current_ri = null;

    private CollectionDirectory d = null;

    private static Category cat = Category.getInstance("CollectionDirectory/XMLDataSouce");

    public void setProperties(Properties p) {
        sourceURL = p.getProperty("RepositoryDataSourceURL");
        this.d = d;
    }

    private void loadConfigFile() {
        cat.debug("Loading config from URL : " + sourceURL);
        synchronized (this) {
            loaded = true;
            SAXParser _parser;
            try {
                _parser = new SAXParser();
                _parser.setContentHandler(this);
                _parser.parse(sourceURL);
            } catch (org.xml.sax.SAXException saxe) {
                saxe.printStackTrace();
            } catch (java.io.IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private void checkConfig() {
        if (!loaded) loadConfigFile();
    }

    public CollectionInformation lookupCollectionInformation(String collection_dn) {
        checkConfig();
        return (CollectionInformation) collections.get(collection_dn);
    }

    public RepositoryInformation lookupRepositoryInformation(String repository_dn) {
        checkConfig();
        return (RepositoryInformation) repositories.get(repository_dn);
    }

    public CollectionInstance lookupCollectionInstance(String collection_instance_dn) {
        checkConfig();
        return (CollectionInstance) coll_instances.get(collection_instance_dn);
    }

    public Enumeration collections() {
        checkConfig();
        return collections.elements();
    }

    public void startDocument() {
    }

    public void endDocument() {
    }

    public void startElement(String the_uri, String the_local_name, String the_q_name, Attributes attributes) throws SAXException {
        if (the_local_name.equals("Repository")) {
            cat.debug("Loading repository");
            current_ri = new RepositoryInformation(attributes.getValue("repository_dn"), attributes.getValue("name"), attributes.getValue("type"), new Properties());
            repositories.put(attributes.getValue("repository_dn"), current_ri);
        } else if (the_local_name.equals("Collection")) {
            cat.debug("Loading Collection");
            collections.put(attributes.getValue("collection_dn"), new CollectionInformation(attributes.getValue("collection_dn"), attributes.getValue("collection_name")));
        } else if (the_local_name.equals("Instance")) {
            cat.debug("Loading Instance");
            CollectionInstance inst = new CollectionInstance(attributes.getValue("instance_dn"), attributes.getValue("collection_dn"), attributes.getValue("repository_dn"), attributes.getValue("local_name"));
            coll_instances.put(attributes.getValue("instance_dn"), inst);
            CollectionInformation ci = lookupCollectionInformation(attributes.getValue("collection_dn"));
            if (ci != null) ci.addInstance(inst);
        } else if (the_local_name.equals("RepositoryProperty")) {
            cat.debug("Adding repository property " + attributes.getValue("name") + "=" + attributes.getValue("value"));
            current_ri.getProps().setProperty(attributes.getValue("name"), attributes.getValue("value"));
        }
    }

    public void endElement(String the_uri, String the_local_name, String the_q_name) {
    }

    public void characters(char[] ch, int start, int length) {
    }
}
