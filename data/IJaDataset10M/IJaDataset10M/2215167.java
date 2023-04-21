package com.seitenbau.testing.testgenerator.partgenerator.velocity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResource;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Basic Abstract class for using Velocity Tempaltes
 *
 * This abstracts the usage of velocity so the templates aren't stored
 * by file. They are read from a XML file which can contain multiple
 * templates. <br>
 * <code>
 * &lt;template id="virtualResourceName"&gt;<br>
 * f<br>
 * &lt;/template&gt;
 * </code>
 *
 */
public abstract class VelocityTemplate {

    private static final String NODE_TEMPLATE_ID = "id";

    private static final String NODE_TEMPLATE_NAME = "template";

    private static final String RESOURCE_TEMPLATE_ROOTFOLDER = "/templates/";

    private StringResourceRepository myRepository;

    private Context myContext;

    protected abstract String getTemplateFileName();

    public void initTemplateStore() {
        try {
            Properties props = new Properties();
            props.setProperty("resource.loader", "str");
            props.setProperty("str.resource.loader.class", StringResourceLoader.class.getCanonicalName());
            Velocity.init(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myRepository = StringResourceLoader.getRepository();
        loadTemplate(getTemplateFileName());
        myContext = new VelocityContext();
        EventCartridge ec = new EventCartridge();
        ec.attachToContext(myContext);
        prepareContext(myContext);
    }

    protected void prepareContext(Context context) {
    }

    public StringBuffer getContent(String contentID, String newLineCharacters, Object... params) {
        Template t;
        StringWriter sw = new StringWriter();
        try {
            String vid = makeID(contentID);
            StringResource sr = myRepository.getStringResource(vid);
            if (sr != null) {
                t = Velocity.getTemplate(vid);
                t.merge(myContext, sw);
            }
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String value = sw.toString();
        if (value != null && value.length() > 2) {
            return parseContent(value);
        }
        return new StringBuffer();
    }

    private String makeID(String contentID) {
        return getClass().getSimpleName() + ":" + contentID;
    }

    private StringBuffer parseContent(String value) {
        if (value.charAt(0) == '\n') {
            value = value.substring(1);
        }
        if (value.charAt(value.length() - 1) == '\n') {
            value = value.substring(0, value.length() - 1);
        }
        value = runImports(value);
        return new StringBuffer(value);
    }

    private String runImports(String value) {
        int pos = value.indexOf("$include{");
        while (pos != -1) {
            int end = value.indexOf("}", pos);
            if (end != -1) {
                String id = value.substring(pos + "$include{".length(), end);
                value = value.replace("$include{" + id + "}", getImport(id));
            }
            pos = value.indexOf("$include{", pos + 1);
        }
        return value;
    }

    private String getImport(String id) {
        return "";
    }

    protected void loadTemplate(String templateFileName) {
        InputStream stream = AbstractTemplatePartGenerator.class.getResourceAsStream(RESOURCE_TEMPLATE_ROOTFOLDER + templateFileName);
        if (stream == null) {
            throw new IllegalArgumentException("Template not found : " + RESOURCE_TEMPLATE_ROOTFOLDER + templateFileName);
        }
        try {
            System.out.println("parsing : " + RESOURCE_TEMPLATE_ROOTFOLDER + templateFileName);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(stream);
            NodeList elemets = doc.getElementsByTagName(NODE_TEMPLATE_NAME);
            loadParts(elemets);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void loadParts(NodeList elemets) {
        for (int i = 0; i < elemets.getLength(); ++i) {
            Node n = elemets.item(i);
            loadPart(n);
        }
    }

    protected void loadPart(Node node) {
        String id = node.getAttributes().getNamedItem(NODE_TEMPLATE_ID).getNodeValue();
        String value = node.getTextContent();
        myRepository.putStringResource(makeID(id), value);
    }
}
