package com.dcivision.dms.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
  HtmlFormText.java

  This class send text through http

    @author          Rollo Chan
    @company         DCIVision Limited
    @creation date   24/06/2003
    @version         $Revision: 1.3.32.1 $
    */
public class HtmlFormText implements HtmlFormElement {

    public static final String REVISION = "$Revision: 1.3.32.1 $";

    private String name;

    private String value;

    private static final Log log = LogFactory.getLog(HtmlFormText.class);

    public HtmlFormText(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public byte[] getTranslated() {
        try {
            StringBuffer content = new StringBuffer();
            content.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(HttpSender.returnChar);
            content.append(HttpSender.returnChar);
            content.append(value).append(HttpSender.returnChar);
            return content.toString().getBytes("UTF-8");
        } catch (Exception e) {
            log.error(e, e);
            return (null);
        }
    }
}
