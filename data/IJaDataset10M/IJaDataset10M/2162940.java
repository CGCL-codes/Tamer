package redora.generator;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import redora.generator.Template.Destination;
import redora.generator.Template.DestinationType;
import redora.generator.Template.Input;
import redora.generator.Template.Type;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import static java.io.File.separatorChar;
import static javax.xml.xpath.XPathConstants.NODESET;

/**
 * The generator uses a bean-counterish template file located in /target/generated-resources/templates.xml
 * (look it up in the sources). There are all the templates in use. You can override this
 * template by adding your own templates.xml in your project's resources directory.
 * Keep in mind that you have to track any changes in templates.xml (simply do a diff
 * in Google Codes source browser). Check this diff whenever you upgrade a version of Redora.
 * <br>
 * By overriding this templates file, you can for example add newe templates that
 * will be used by the generator. For example to create an client that uses SmartGWT
 * or ExtJS, whatever you prefer of course.
 * <br>
 * This class handles this thing.
 * 
 * @author Nanjing RedOrange (http://www.red-orange.cn)
 */
public class GeneratorTemplate {

    final Document doc;

    final String templatesDir;

    /**
     * @param templateDir (Mandatory).
     * @throws ModelGenerationException If templates.xml cannot be found.
     */
    public GeneratorTemplate(@NotNull String templateDir) throws ModelGenerationException {
        this.templatesDir = templateDir;
        File templates = new File(templateDir + separatorChar + "templates.xml");
        InputStream in = null;
        try {
            in = new FileInputStream(templates);
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        } catch (Exception e) {
            if (in == null) {
                throw new ModelGenerationException("Failed to initialize templates.xml, i could not get the file " + templates.getAbsolutePath() + " (" + templates.exists() + ")", e);
            }
            throw new ModelGenerationException("Failed to initialize templates.xml. If you use a custom templates.xml make sure it is valid.", e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * @param input A value of the Input enum
     * @return List of all the templates that use given input.
     * @throws ModelGenerationException Wrapping XPath exceptions
     */
    @NotNull
    public List<Template> byInput(@NotNull Input input) throws ModelGenerationException {
        List<Template> retVal = new LinkedList<Template>();
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        NodeList templateNodes;
        try {
            templateNodes = (NodeList) xpath.evaluate("//template[@input='" + input + "']", doc, NODESET);
        } catch (XPathExpressionException e) {
            throw new ModelGenerationException("Filter on " + input + " failed", e);
        }
        for (int i = 0; i < templateNodes.getLength(); i++) {
            retVal.add(from(templateNodes.item(i)));
        }
        return retVal;
    }

    /**
     * Will get a Template object form the templates.xml document. This file is
     * the generator dispatcher, telling the generator what to do with which template
     * file (freemarker or xslt). You can create a custom templates.xml and change or
     * extend the generator's behavior.
     * @return Template class as holder of the information found in templates.xml
     */
    @NotNull
    public Template from(@NotNull Node node) {
        Template retVal = new Template(node.getAttributes().getNamedItem("name").getNodeValue(), Type.valueOf(node.getAttributes().getNamedItem("type").getNodeValue()), Destination.valueOf(node.getAttributes().getNamedItem("destination").getNodeValue()), templatesDir);
        if (node.getAttributes().getNamedItem("templateFileName") != null) {
            retVal.setTemplateFileName(node.getAttributes().getNamedItem("templateFileName").getNodeValue());
        }
        if (node.getAttributes().getNamedItem("ignoreProjects") != null) {
            retVal.setIgnoreProjects(node.getAttributes().getNamedItem("ignoreProjects").getNodeValue());
        }
        if (node.getAttributes().getNamedItem("destinationType") != null) {
            retVal.setDestinationType(DestinationType.valueOf(node.getAttributes().getNamedItem("destinationType").getNodeValue()));
        }
        if (node.getAttributes().getNamedItem("input") != null) {
            retVal.setInput(Input.valueOf(node.getAttributes().getNamedItem("input").getNodeValue()));
        }
        if (node.getAttributes().getNamedItem("package") != null) {
            retVal.setPackageSuffix(node.getAttributes().getNamedItem("package").getNodeValue());
        }
        if (node.getAttributes().getNamedItem("path") != null) {
            retVal.setPath(node.getAttributes().getNamedItem("path").getNodeValue());
        }
        return retVal;
    }
}
