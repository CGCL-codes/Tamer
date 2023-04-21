package shttp.config;

import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import shttp.core.FatalError;
import java.util.*;

/**
 * This is an XML based implementation of Config.
 *
 * It currently does not support the use of attributes. A future version could automatically detect if it is
 * an attribute or an Element the get/set functions are referring to.
 * 
 * @author  dmlarsson
 * @version 
 */
public class XMLConfig extends Config {

    private Element element;

    private SAXBuilder builder;

    private Document doc;

    /** Creates new Config with default values */
    public XMLConfig(File file) throws FatalError {
        super(file);
        try {
            builder = new SAXBuilder();
            doc = builder.build(file);
            element = doc.getRootElement();
        } catch (Exception e) {
            throw new FatalError("Config constructor: " + e);
        }
    }

    public XMLConfig(Element e) {
        this.element = e;
    }

    public void write(File file) throws IOException {
        XMLOutputter outputter = new XMLOutputter(" ", true);
        FileOutputStream outf = new FileOutputStream(file);
        outputter.output(doc, outf);
        outf.close();
    }

    public void write() throws java.io.IOException {
        write(this.file);
    }

    public int getInteger(String name, int def) {
        String res = getString(name, "" + def);
        return Integer.parseInt(res);
    }

    public void setInteger(String name, int value) {
        setString(name, "" + value);
    }

    public long getLong(String name, long def) {
        String res = getString(name, "" + def);
        return Long.parseLong(res);
    }

    public boolean getBoolean(String name, boolean def) {
        String res;
        if (def) res = getString(name, "true"); else res = getString(name, "false");
        if (res.equalsIgnoreCase("true")) return true; else return false;
    }

    public void setLong(String name, long value) {
        setString(name, "" + value);
    }

    public void setBoolean(String name, boolean value) {
        if (value) setString(name, "true"); else setString(name, "false");
    }

    public String getString(String name, String def) {
        Element e = element.getChild(name);
        if (e == null) setString(name, def);
        return element.getChildTextTrim(name);
    }

    public void setString(String name, String value) {
        Element e = this.element.getChild(name);
        if (e == null) {
            e = new Element(name);
            e.setText(value);
            element.addContent(e);
        } else {
            e.setText(value);
        }
    }

    /** Returns an array of Configs matching a certain name.
     */
    public Config[] getConfigs(String name) {
        List list = element.getChildren(name);
        if (list == null) return null;
        Config[] res = new Config[list.size()];
        int cnt = 0;
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            Element el = (Element) i.next();
            res[cnt] = new XMLConfig(el);
            cnt++;
        }
        return res;
    }

    /**
     * Returns the config associated with the specified name.
     */
    public Config getConfig(String name) {
        Element e = element.getChild(name);
        if (e == null) {
            e = new Element(name);
            element.addContent(e);
        }
        return new XMLConfig(e);
    }
}
