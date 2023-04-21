package com.stars.cgt.cfg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;

public class ProcessorParser {

    private static List processors = new ArrayList();

    private static Map processClass = new HashMap();

    private static Map vms = new HashMap();

    private static Map paths = new HashMap();

    private static Document doc = null;

    public static void parser() throws Exception {
        processors.clear();
        processClass.clear();
        vms.clear();
        paths.clear();
        org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
        doc = reader.read(new File("conf/processor.xml"));
        Element root = doc.getRootElement();
        List dbs = root.elements();
        for (int i = 0; i < dbs.size(); ++i) {
            Element elm = (Element) dbs.get(i);
            String name = elm.attributeValue("name");
            String cls = elm.attributeValue("class");
            processors.add(name);
            processClass.put(name, cls);
            vms.put(name, elm.attributeValue("vm"));
            paths.put(name, elm.attributeValue("path"));
        }
    }

    public static List getProcessors() {
        return processors;
    }

    public static String getProcessor(String name) {
        return (String) processClass.get(name);
    }

    public static String getVm(String name) {
        return (String) vms.get(name);
    }

    public static String getPath(String name) {
        return (String) paths.get(name);
    }
}
