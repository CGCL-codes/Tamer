package org.monet.modelling.kernel.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "operation")
public class OperationDeclaration {

    public static class Label {

        @Attribute(name = "language")
        protected String language;

        @Text
        protected String content;

        public String getLanguage() {
            return language;
        }

        public String getContent() {
            return content;
        }
    }

    public static class Description {

        @Attribute(name = "language")
        protected String language;

        @Text
        protected String content;

        public String getLanguage() {
            return language;
        }

        public String getContent() {
            return content;
        }
    }

    @Attribute(name = "name")
    protected String name;

    @ElementMap(entry = "label", key = "language", attribute = true, inline = true, required = false)
    protected Map<String, String> labelMap = new HashMap<String, String>();

    @ElementMap(entry = "description", key = "language", attribute = true, inline = true, required = false)
    protected Map<String, String> descriptionMap = new HashMap<String, String>();

    public String getName() {
        return name;
    }

    public String getLabel(String language) {
        if (labelMap.get(language) == null) return "";
        return labelMap.get(language);
    }

    public Collection<String> getLabels() {
        return labelMap.values();
    }

    public String getDescription(String language) {
        if (descriptionMap.get(language) == null) return "";
        return descriptionMap.get(language);
    }

    public Collection<String> getDescriptions() {
        return descriptionMap.values();
    }
}
