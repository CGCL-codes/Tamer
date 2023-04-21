package net.sourceforge.ondex.workflow2.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import net.sourceforge.ondex.workflow2.definition.ArgDefValuePair;
import net.sourceforge.ondex.workflow2.definition.ArgumentBean;
import net.sourceforge.ondex.workflow2.definition.PluginBean;
import net.sourceforge.ondex.workflow2.definition.PluginConfiguration;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;

/**
 * Utility class for saving and reading Ondex workflow files in a variety of formats
 * 
 * @author lysenkoa
 *
 */
public class TaskDescriptionIO {

    private TaskDescriptionIO() {
    }

    public static TaskDescription readFile(File file, PluginRegistry pr) throws IOException, JDOMException, XMLStreamException {
        String version = net.sourceforge.ondex.workflow2.base.WrappedEngine.getVersion(file.getCanonicalPath());
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

    private static TaskDescription parseVersion_1(File file, PluginRegistry pr) throws IOException, JDOMException, XMLStreamException {
        TaskDescription result = new TaskDescription();
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);
        Element root = doc.getRootElement();
        List<Element> es = root.getChildren();
        for (Element e : es) {
            String graph = "my_graph";
            if (e.getName().equals("DefaultGraph")) {
                PluginBean pb = pr.getPluginBean("General", "newgraph");
                List<ArgDefValuePair> args = new ArrayList<ArgDefValuePair>();
                ArgumentBean ab = getArgumentBeanByName(pb, "Type");
                args.add(new ArgDefValuePair(ab, e.getAttributeValue("type")));
                ab = getArgumentBeanByName(pb, "GraphId");
                args.add(new ArgDefValuePair(ab, e.getAttributeValue("name")));
                graph = e.getAttributeValue("name");
                result.addPlugin(new PluginConfiguration(pb, args));
            } else {
                PluginBean pb = pr.getPluginBean(e.getName(), e.getAttributeValue("name"));
                List<ArgDefValuePair> args = new ArrayList<ArgDefValuePair>();
                List<Element> atts = e.getChildren();
                ArgumentBean graphId = getArgumentBeanByName(pb, "graphId");
                args.add(new ArgDefValuePair(graphId, graph));
                for (Element att : atts) {
                    ArgumentBean ab = getArgumentBeanByName(pb, att.getAttributeValue("name"));
                    args.add(new ArgDefValuePair(ab, att.getText()));
                }
                Attribute datadir = e.getAttribute("datadir");
                if (datadir != null && datadir.getValue().length() > 0) {
                    ArgumentBean ab = getArgumentBeanByName(pb, "datadir");
                    args.add(new ArgDefValuePair(ab, datadir.getValue()));
                }
                Attribute datafile = e.getAttribute("datafile");
                if (datafile != null && datafile.getValue().length() > 0) {
                    if (e.getName().equals("Export")) {
                        ArgumentBean ab = getArgumentBeanByName(pb, "exportfile");
                        args.add(new ArgDefValuePair(ab, datadir.getValue()));
                    } else {
                        ArgumentBean ab = getArgumentBeanByName(pb, "importfile");
                        args.add(new ArgDefValuePair(ab, datadir.getValue()));
                    }
                }
                result.addPlugin(new PluginConfiguration(pb, args));
            }
        }
        return result;
    }

    private static TaskDescription parseVersion_2(File file, PluginRegistry pr) throws IOException, JDOMException, XMLStreamException {
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
                PluginBean pb = pr.getPluginBean("General", "newgraph");
                List<ArgDefValuePair> args = new ArrayList<ArgDefValuePair>();
                List<Element> inputs = c.getChild("InputList").getChildren();
                String graphID = "my_graph";
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
                String graphName = "my_graph";
                if (pb == null) {
                    String[] ids = c.getChildText("UniqueID").split("\\.");
                    pb = pr.getPluginBean(firstUpper(ids[1]), ids[0]);
                    if (pb == null) {
                        pb = pr.getPluginBean(ids[1], ids[0]);
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
                    } else if (id.equals("1") && pb.getOndexType().equals("Filter")) {
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

    private static TaskDescription parseVersion_3(File file, PluginRegistry pr) throws IOException, JDOMException, XMLStreamException {
        TaskDescription result = new TaskDescription();
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);
        Element root = doc.getRootElement();
        List<Element> components = root.getChild("Workflow").getChildren();
        for (Element c : components) {
            PluginBean pb = pr.getPluginBean(c.getName(), c.getAttributeValue("name"));
            if (pb == null) {
                System.err.println("Warning: could not resolve " + c.getName() + " -- " + c.getAttributeValue("name"));
                continue;
            }
            List<ArgDefValuePair> ads = new ArrayList<ArgDefValuePair>();
            List<Element> args = c.getChildren();
            for (Element arg : args) {
                ads.add(new ArgDefValuePair(getArgumentBeanByName(pb, arg.getAttributeValue("name")), arg.getText()));
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
            Element comp = new Element(pc.getPluginBean().getOndexType());
            comp.setAttribute("name", pc.getPluginBean().getOnexId());
            String comment = pc.getComment();
            if (comment != null && comment.length() > 0) {
                Element commentElement = new Element("Comment");
                commentElement.setContent(new CDATA(comment.trim()));
                comp.addContent(commentElement);
            }
            for (ArgDefValuePair a : pc.getArgs()) {
                Element arg = new Element("Arg");
                if (a.getArg().getInteranlName() == null) {
                    System.err.println(pc.getPluginBean().getOnexId() + " - " + a.getArg().getName());
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
        try {
            for (ArgumentBean ab : pb.getInputArgDef()) {
                if (ab.getInteranlName().equalsIgnoreCase(name)) {
                    return ab;
                }
            }
        } catch (java.lang.NullPointerException e) {
            if (pb == null) {
                System.err.println("Plugin is null");
            }
            System.err.println("Plugin " + pb.getName() + " does not have " + name);
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
