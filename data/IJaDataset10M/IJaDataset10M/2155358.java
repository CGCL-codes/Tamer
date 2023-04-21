package org.monet.kernel.model.definition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "workplace")
public class WorkplaceDeclaration extends IndexedDeclaration {

    public enum TypeEnumeration {

        EVENT, GOAL, DEAD_END
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

    @Attribute(name = "type")
    protected TypeEnumeration type;

    @ElementMap(entry = "label", key = "language", attribute = true, inline = true, required = false)
    protected Map<String, String> labelMap = new HashMap<String, String>();

    public TypeEnumeration getType() {
        return type;
    }

    public String getLabel(String language) {
        if (labelMap.get(language) == null) return "";
        return labelMap.get(language);
    }

    public Collection<String> getLabels() {
        return labelMap.values();
    }
}
