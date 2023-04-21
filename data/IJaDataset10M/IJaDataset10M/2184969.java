package com.hba.web.logger.server.jaxb;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Huynh Bao Anh
 *
 */
public class AppenderRef {

    @XmlAttribute(name = "ref")
    public String ref;
}
