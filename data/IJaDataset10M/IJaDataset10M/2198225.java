package org.acs.elated.ui.itembinding;

import java.io.InputStream;
import java.util.*;
import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;

public class DSInputParser {

    private static Logger logger = Logger.getLogger(DSInputParser.class);

    private List bindMaps = new ArrayList();

    public List parse(InputStream inStream) {
        logger.debug("entering parse");
        Digester digester = new Digester();
        digester.push(this);
        digester.addObjectCreate("fbs:DSInputSpec/fbs:DSInput", BindMap.class);
        digester.addSetProperties("fbs:DSInputSpec/fbs:DSInput", "wsdlMsgPartName", "wsdlMsgPartName");
        digester.addBeanPropertySetter("fbs:DSInputSpec/fbs:DSInput/fbs:DSInputLabel", "label");
        digester.addBeanPropertySetter("fbs:DSInputSpec/fbs:DSInput/fbs:DSMIME", "mime");
        digester.addSetNext("fbs:DSInputSpec/fbs:DSInput", "addBindMap");
        try {
            digester.parse(inStream);
        } catch (Exception e) {
            logger.error("parsing error", e);
        }
        logger.debug("exiting parse");
        return bindMaps;
    }

    public void addBindMap(BindMap bindMap) {
        this.bindMaps.add(bindMap);
    }
}
