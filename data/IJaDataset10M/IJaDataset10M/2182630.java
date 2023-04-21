package com.volantis.xml.pipeline.sax.drivers.web.conditioners;

import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConditionerFactory;
import com.volantis.xml.pipeline.sax.conditioners.ContentConditioner;
import org.xml.sax.XMLFilter;

/**
 * Implementation of {@link WebDriverConditionerFactory} to create a
 * {@link HTMLResponseConditioner}
 */
public class HTMLResponseConditionerFactory implements WebDriverConditionerFactory {

    public ContentConditioner createConditioner(XMLFilter filter) {
        return new HTMLResponseConditioner(filter);
    }
}
