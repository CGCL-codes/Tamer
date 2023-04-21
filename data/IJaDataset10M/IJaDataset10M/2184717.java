package org.monet.kernel.model.definition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Text;

public abstract class FieldDeclarationBase extends IndexedDeclaration {

    public static class IsRequired {
    }

    public static class IsMultiple {
    }

    public static class IsExtended {
    }

    public static class IsSuper {
    }

    public static class DefaultValue {

        @Text
        protected String content;

        public String getContent() {
            return content;
        }
    }

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

    public static class Message {

        public enum TypeEnumeration {

            IF_EDITING, IF_EMPTY, IF_REQUIRED
        }

        @Attribute(name = "type", required = false)
        protected TypeEnumeration type;

        @Attribute(name = "language")
        protected String language;

        @Text
        protected String content;

        public TypeEnumeration getType() {
            return type;
        }

        public String getLanguage() {
            return language;
        }

        public String getContent() {
            return content;
        }
    }

    @Element(name = "is-required", required = false)
    protected IsRequired isRequired;

    @Element(name = "is-multiple", required = false)
    protected IsMultiple isMultiple;

    @Element(name = "is-extended", required = false)
    protected IsExtended isExtended;

    @Element(name = "is-super", required = false)
    protected IsSuper isSuper;

    @Element(name = "default-value", required = false)
    protected DefaultValue defaultValue;

    @ElementMap(entry = "label", key = "language", attribute = true, inline = true, required = false)
    protected Map<String, String> labelMap = new HashMap<String, String>();

    @ElementMap(entry = "description", key = "language", attribute = true, inline = true, required = false)
    protected Map<String, String> descriptionMap = new HashMap<String, String>();

    @ElementMap(entry = "message", key = "language", attribute = true, inline = true, required = false)
    protected Map<String, String> messageMap = new HashMap<String, String>();

    public boolean isRequired() {
        return (isRequired != null);
    }

    public boolean isMultiple() {
        return (isMultiple != null);
    }

    public boolean isExtended() {
        return (isExtended != null);
    }

    public boolean isSuper() {
        return (isSuper != null);
    }

    public DefaultValue getDefaultValue() {
        return defaultValue;
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

    public String getMessage(String language) {
        if (messageMap.get(language) == null) return "";
        return messageMap.get(language);
    }

    public Collection<String> getMessages() {
        return messageMap.values();
    }
}
