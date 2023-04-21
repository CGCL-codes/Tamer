package net.sourceforge.ondex.workflow.model;

import net.sourceforge.ondex.init.ArgumentBean;
import net.sourceforge.ondex.init.PluginBean;
import net.sourceforge.ondex.init.PluginRegistry;
import net.sourceforge.ondex.init.PluginType;
import net.sourceforge.ondex.workflow.engine.Engine;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.Format.TextMode;
import org.jdom.output.XMLOutputter;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for saving and reading Ondex workflow files in a variety of formats
 *
 * @author lysenkoa
 */
public class TaskDescriptionIO {

    private TaskDescriptionIO() {
    }

    public static TaskDescription readFile(File file, PluginRegistry pr) throws IOException, JDOMException, XMLStreamException, PluginType.UnknownPluginTypeException {
        String version = Engine.getVersion(file.getCanonicalPath());
        TaskDescription result = null;
        if (version.startsWith("2")) {
            result = parseVersion_2(file, pr);
        } else if (version.startsWith("3")) {
            result = parseVersion_3(file, pr);
        } else {
            result = parseVersion_1(file, pr);
        }
        return result;
    }

    private static TaskDescription parseVersion_1(File file, PluginRegistry pr) throws IOException, JDOMException, XMLStreamException, PluginType.UnknownPluginTypeException {
        TaskDescription result = new TaskDescription();
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);
        Element root = doc.getRootElement();
        List<Element> es = root.getChildren();
        String graph = "default";
        for (Element e : es) {
            if (e.getName().equals("DefaultGraph")) {
                PluginBean pb = pr.getPluginBean(PluginType.GRAPH, "newgraph");
                List<ArgDefValuePair> args = new ArrayList<ArgDefValuePair>();
                ArgumentBean ab = getArgumentBeanByName(pb, "Type");
                args.add(new ArgDefValuePair(ab, e.getAttributeValue("type")));
                ab = getArgumentBeanByName(pb, "GraphId");
                args.add(new ArgDefValuePair(ab, e.getAttributeValue("name")));
                ab = getArgumentBeanByName(pb, "storageFolder");
                String dir = null;
                List children = e.getChildren("Parameter");
                for (Object child : children) {
                    if (((Element) child).getAttribute("name").getValue().equals("StorageDir")) {
                        dir = ((Element) child).getTextTrim();
                    }
                }
                args.add(new ArgDefValuePair(ab, dir));
                graph = e.getAttributeValue("name");
                result.addPlugin(new PluginConfiguration(pb, args));
            } else {
                PluginBean pb = pr.getPluginBean(PluginType.getType(e.getName()), e.getAttributeValue("name"));
                if (pb == null) {
                    throw new RuntimeException(e.getAttributeValue("name") + " of type " + PluginType.getType(e.getName()) + " is not registered in plugin registry");
                }
                List<ArgDefValuePair> args = new ArrayList<ArgDefValuePair>();
                List<Element> atts = e.getChildren();
                ArgumentBean graphId = getArgumentBeanByName(pb, "graphId");
                args.add(new ArgDefValuePair(graphId, graph));
                for (Element att : atts) {
                    String paramName = att.getAttributeValue("name");
                    if (paramName == null) {
                        throw new RuntimeException("No required name attribute found in element " + att.getName() + " for plugin " + pb.getName());
                    }
                    ArgumentBean ab = getArgumentBeanByName(pb, paramName);
                    if (ab == null) {
                        throw new RuntimeException(paramName + " is not a valid argument for plugin " + pb.getName());
                    }
                    args.add(new ArgDefValuePair(ab, att.getText()));
                }
                result.addPlugin(new PluginConfiguration(pb, args));
            }
        }
        return result;
    }

    private static TaskDescription parseVersion_2(File file, PluginRegistry pr) throws IOException, JDOMException, XMLStreamException, PluginType.UnknownPluginTypeException {
        final Map<String, PluginBean> clsToPluginBean = new HashMap<String, PluginBean>();
        for (PluginBean pb : pr.getAllPlugins()) {
            clsToPluginBean.put(pb.getCls(), pb);
        }
        TaskDescription result = new TaskDescription();
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);
        Element root = doc.getRootElement();
        Map<String, Element> resources = new HashMap<String, Element>();
        List<Element> res = root.getChild("Resources").getChildren();
        for (Element r : res) {
            resources.put(r.getChildText("GlobalId"), r.getChild("Value"));
        }
        List<Element> components = root.getChild("Components").getChildren();
        Map<String, String> map = new HashMap<String, String>();
        for (Element c : components) {
            if (c.getAttributeValue("type").equals("function") && c.getAttributeValue("method").equals("getNewGraph")) {
                PluginBean pb = pr.getPluginBean(PluginType.GRAPH, "newgraph");
                List<ArgDefValuePair> args = new ArrayList<ArgDefValuePair>();
                List<Element> inputs = c.getChild("InputList").getChildren();
                String graphID = "default";
                for (Element e : inputs) {
                    String id = e.getChildText("LocalId").trim();
                    if (id.equals("0")) {
                        ArgumentBean ab = getArgumentBeanByName(pb, "Type");
                        String value = resources.get(e.getChildText("GlobalId")).getText();
                        args.add(new ArgDefValuePair(ab, value));
                    } else if (id.equals("1")) {
                        ArgumentBean ab = getArgumentBeanByName(pb, "GraphId");
                        String value = resources.get(e.getChildText("GlobalId")).getText();
                        graphID = value;
                        args.add(new ArgDefValuePair(ab, value));
                    } else if (id.equals("2")) {
                        ArgumentBean ab = getArgumentBeanByName(pb, "storageFolder");
                        String value = resources.get(e.getChildText("GlobalId")).getText();
                        args.add(new ArgDefValuePair(ab, value));
                    }
                }
                List<Element> outputs = c.getChild("OutputList").getChildren();
                map.put(String.valueOf(outputs.get(0).getChildText("GlobalId")), graphID);
                result.addPlugin(new PluginConfiguration(pb, args));
            } else if (c.getAttributeValue("type").equals("plugin")) {
                PluginBean pb = clsToPluginBean.get(c.getAttributeValue("class"));
                List<ArgDefValuePair> args = new ArrayList<ArgDefValuePair>();
                String graphName = "default";
                if (pb == null) {
                    String[] ids = c.getChildText("UniqueID").split("\\.");
                    pb = pr.getPluginBean(PluginType.getType(firstUpper(ids[1])), ids[0]);
                    if (pb == null) {
                        pb = pr.getPluginBean(PluginType.getType(ids[1]), ids[0]);
                    }
                    if (pb == null) {
                        System.err.println("Missing: " + ids[0] + " - " + c.getAttributeValue("class"));
                        continue;
                    }
                }
                List<Element> inputs = c.getChild("InputList").getChildren();
                for (Element e : inputs) {
                    String id = e.getChildText("LocalId").trim();
                    if (id.equals("0")) {
                        Element value = resources.get(e.getChildText("GlobalId"));
                        List<Element> internal = value.getChildren();
                        for (Element v : internal) {
                            ArgumentBean ab = getArgumentBeanByName(pb, v.getName());
                            if (ab != null) {
                                String val = v.getTextTrim();
                                if (val != null && val.length() > 0) args.add(new ArgDefValuePair(ab, val));
                            }
                        }
                    } else if (id.equals("1")) {
                        graphName = map.get(String.valueOf(e.getChildText("GlobalId").trim()));
                        ArgumentBean ab = getArgumentBeanByName(pb, "graphId");
                        args.add(new ArgDefValuePair(ab, graphName));
                    }
                }
                List<Element> outputs = c.getChild("OutputList").getChildren();
                for (Element e : outputs) {
                    String id = e.getChildText("LocalId").trim();
                    if (id.equals("0")) {
                        map.put(String.valueOf(e.getChildText("GlobalId").trim()), graphName);
                    } else if (id.equals("1") && pb.getOndexType().getName().equals("Filter")) {
                        map.put(String.valueOf(e.getChildText("GlobalId").trim()), e.getChildText("GlobalId").trim());
                        ArgumentBean ab = getArgumentBeanByName(pb, "secondaryGraphId");
                        args.add(new ArgDefValuePair(ab, graphName));
                    }
                }
                result.addPlugin(new PluginConfiguration(pb, args));
            }
        }
        return result;
    }

    private static TaskDescription parseVersion_3(File file, PluginRegistry pr) throws IOException, JDOMException, XMLStreamException, PluginType.UnknownPluginTypeException {
        TaskDescription result = new TaskDescription();
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);
        Element root = doc.getRootElement();
        List<Element> components = root.getChild("Workflow").getChildren();
        String id = "";
        for (Element c : components) {
            List<ArgDefValuePair> ads = new ArrayList<ArgDefValuePair>();
            PluginBean pb = pr.getPluginBean(PluginType.getType(c.getName()), c.getAttributeValue("name"));
            if (pb == null) {
                System.err.println("Missing plugin " + c.getName());
                continue;
            }
            List<Element> args = c.getChildren();
            for (Element arg : args) {
                String argumentName = arg.getAttributeValue("name");
                ArgumentBean argBean = getArgumentBeanByName(pb, argumentName);
                if (argBean == null) {
                    System.err.println("Missing plugin argument" + argumentName);
                    continue;
                }
                ads.add(new ArgDefValuePair(argBean, arg.getText()));
            }
            PluginConfiguration p = new PluginConfiguration(pb, ads);
            Element commentElement = c.getChild("Comment");
            if (commentElement != null) {
                String comment = commentElement.getText();
                if (comment != null && comment.length() > 0) p.setComment(comment);
            }
            result.addPlugin(p);
        }
        return result;
    }

    public static void saveToFile(File file, TaskDescription td) throws IOException {
        Format format = Format.getPrettyFormat();
        format.setTextMode(TextMode.PRESERVE);
        XMLOutputter outputter = new XMLOutputter(format);
        Element rootElement = new Element("Ondex");
        rootElement.setAttribute("version", "3.0");
        Element workflow = new Element("Workflow");
        for (PluginConfiguration pc : td.getComponents()) {
            Element comp = new Element(pc.getPluginBean().getOndexType().getName());
            comp.setAttribute("name", pc.getPluginBean().getOndexId());
            if (pc.getComment() != null) {
                String comment = pc.getComment().trim();
                if (comment != null && comment.length() > 0) {
                    Element commentElement = new Element("Comment");
                    commentElement.setContent(new CDATA(comment));
                    comp.addContent(commentElement);
                }
            }
            for (ArgDefValuePair a : pc.getArgs()) {
                Element arg = new Element("Arg");
                if (a.getArg().getInteranlName() == null) {
                    System.err.println(pc.getPluginBean().getOndexId() + " - " + a.getArg().getName());
                    continue;
                }
                arg.setAttribute("name", a.getArg().getInteranlName());
                if (a.getValue() == null) {
                    if (!a.getArg().getIsRequired()) continue;
                    arg.addContent("");
                } else {
                    arg.addContent(a.getValue());
                }
                comp.addContent(arg);
            }
            workflow.addContent(comp);
        }
        rootElement.addContent(workflow);
        Document doc = new Document(rootElement);
        FileOutputStream fos = new FileOutputStream(file);
        outputter.output(doc, fos);
        fos.close();
    }

    private static String firstUpper(String in) {
        return in.substring(0, 1).toUpperCase() + in.substring(1);
    }

    public static ArgumentBean getArgumentBeanByName(PluginBean pb, String name) {
        for (ArgumentBean ab : pb.getInputArgDef()) {
            if (ab.getInteranlName().equalsIgnoreCase(name)) {
                return ab;
            }
        }
        return null;
    }

    private static class ComponentDelegate {

        private final List<ArgDefValuePair> args = new ArrayList<ArgDefValuePair>();

        private final PluginBean pb;

        public ComponentDelegate(PluginBean pb) {
            this.pb = pb;
        }

        public void addArgument(String internalId, String value) {
            ArgumentBean ab = getArgumentBeanByName(pb, internalId);
            if (ab != null && (value != null && value.length() > 0)) args.add(new ArgDefValuePair(ab, value));
        }

        public PluginConfiguration getResult() {
            return new PluginConfiguration(pb, args);
        }
    }
}
