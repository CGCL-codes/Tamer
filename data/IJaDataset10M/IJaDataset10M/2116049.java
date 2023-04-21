package com.emental.mindraider.rest.properties;

import javax.xml.namespace.QName;
import org.apache.log4j.Category;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
import com.emental.mindraider.utils.PullParsing;

/**
 * LabelProperty.java
 * 
 * @author Martin.Dvorak
 */
public class LabelProperty implements ResourceProperty {

    private static final Category cat = Category.getInstance("com.emental.mindraider.rest.properties.ResourceProperty");

    public static final String ELEMENT_LABEL = "labelProperty";

    public static String label;

    public static QName qname;

    static {
        qname = new QName("", ELEMENT_LABEL);
        label = "Label property";
    }

    public String labelContent;

    /**
     * Constructor.
     */
    public LabelProperty() {
        qname = new QName("", ELEMENT_LABEL);
        label = "Label property";
    }

    /**
     * Constructor.
     * 
     * @param labelContent
     */
    public LabelProperty(String labelContent) {
        this();
        this.labelContent = labelContent;
    }

    public String getLabel() {
        return label;
    }

    public QName getQName() {
        return qname;
    }

    public void fromXml(XmlPullParser xpp) throws Exception {
        labelContent = xpp.nextText();
        cat.debug("  Label property text: " + labelContent);
    }

    public void toXml(XmlSerializer xs) throws Exception {
        PullParsing.serializeTextElement(xs, ELEMENT_LABEL, labelContent);
    }
}
