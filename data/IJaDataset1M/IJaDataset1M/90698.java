package restlet.description.convert;

import com.thoughtworks.xstream.XStream;

public class RESTConfigurationXMLConverter {

    XStream streamer = new XStream();

    public RESTConfigurationXMLConverter() {
        streamer.alias("configuration", RESTConfigurationDescription.class);
        streamer.omitField(RESTConfigurationDescription.class, "moduleDescriptions");
        streamer.omitField(RESTConfigurationDescription.class, "moduleNames");
        streamer.aliasField("modules", RESTConfigurationDescription.class, "moduleConfigurationFiles");
        streamer.omitField(RESTModuleDescription.class, "path");
        streamer.alias("module", RESTModuleDescription.class);
        streamer.alias("resource", RESTResourceDescription.class);
        streamer.alias("handler", Handler.class);
    }

    public String toXML(Object object) {
        return streamer.toXML(object);
    }

    public Object fromXML(String xml) {
        Object object = streamer.fromXML(xml);
        if (object instanceof RecoverFromSerialization) ((RecoverFromSerialization) object).recoverFromSerialization();
        return object;
    }
}
