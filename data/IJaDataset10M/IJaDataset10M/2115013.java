package org.allcolor.ywt.style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.allcolor.services.xml.BaseXMLSerializer;
import org.allcolor.services.xml.Choice;
import org.allcolor.services.xml.Required;
import org.allcolor.services.xml.XmlNS;

@XmlNS(xmlns = "http://www.allcolor.org/xmlns/style")
public class Style {

    private Style() {
        super();
    }

    private Style(String descriptionKey, String description, String[] depends) {
        super();
        this.descriptionKey = descriptionKey;
        this.description = description;
        if (depends != null) {
            this.depends.addAll(Arrays.asList(depends));
        }
    }

    public static void main(String[] args) {
        Style s = new Style("style.default.description", "This is the default style.", null);
        System.out.println(BaseXMLSerializer.toXML(s));
        s = new Style("style.alias.description", "Alias style", new String[] { "default" });
        System.out.println(BaseXMLSerializer.toXML(s));
    }

    private List<String> depends = new ArrayList<String>();

    @Choice(name = "description")
    @Required
    private String descriptionKey;

    @Choice(name = "description")
    @Required
    private String description;

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDepends() {
        return depends == null ? depends = new ArrayList<String>() : depends;
    }
}
