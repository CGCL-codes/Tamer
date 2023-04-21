package com.prolix.editor.resourcemanager.zip;

import java.io.IOException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

public class TestLomMetadata {

    private static String str_title = "here is the title";

    private static String str_description = "and this is the description...";

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Element root = new Element("metadata");
        Element schema = new Element("schema");
        schema.addContent("IMS Content");
        root.addContent(schema);
        Element schemaversion = new Element("schemaversion");
        schemaversion.addContent("1.1");
        root.addContent(schemaversion);
        Element lom = new Element("lom");
        lom.setNamespace(Namespace.getNamespace("http://www.imsglobal.org/xsd/imsmd_v1p2"));
        root.addContent(lom);
        Element general = new Element("general");
        lom.addContent(general);
        Element title = new Element("title");
        general.addContent(title);
        Element langstring = new Element("langstring");
        langstring.addContent(str_title);
        title.addContent(langstring);
        Element description = new Element("description");
        general.addContent(description);
        Element langstring_desc = new Element("langstring");
        langstring_desc.addContent(str_description);
        description.addContent(langstring_desc);
        Document doc = new Document(root);
        XMLOutputter outputter = new XMLOutputter();
        try {
            outputter.setIndent("   ");
            outputter.setNewlines(true);
            outputter.output(doc, System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n\n" + doc.getRootElement().getChild("lom", Namespace.getNamespace("http://www.imsglobal.org/xsd/imsmd_v1p2")).getChild("general"));
    }
}
