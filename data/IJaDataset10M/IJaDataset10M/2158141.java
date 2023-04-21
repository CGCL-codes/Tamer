package edu.nus.iss.ejava.team4.util;

import java.io.Writer;
import org.apache.commons.lang.StringUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class ReflectionUtils {

    public static String toString(Object object) {
        if (object == null) return StringUtils.EMPTY;
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("root", Object.class);
        return (xstream.toXML(object));
    }
}
