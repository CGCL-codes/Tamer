package net.sourceforge.ondex.xten.workflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;
import net.sourceforge.ondex.AbstractArguments;
import net.sourceforge.ondex.workflow.Parameters.AbstractONDEXPluginInit;
import net.sourceforge.ondex.xten.workflow.support.ComponentMaker;
import net.sourceforge.ondex.xten.workflow.support.InputProcessor;
import net.sourceforge.ondex.xten.workflow.support.OutputProcessor;
import net.sourceforge.ondex.xten.workflow.support.WElement;
import net.sourceforge.ondex.xten.workflow.tools.ValueTuple;

/**
 * 
 * @author lysenkoa
 *
 */
@SuppressWarnings("unused")
public class ProcessingJob {

    private static final Class<?>[] STR_ARG = new Class<?>[] { String.class };

    private static final Class<?>[] NO_ARGS = new Class<?>[0];

    private static final Object[] NO_IN = new Object[0];

    private static final String INIT = "#init";

    private static Map<String, InputProcessor> inputProcessors = new HashMap<String, InputProcessor>();

    private static Map<String, OutputProcessor> outputProcessors = new HashMap<String, OutputProcessor>();

    private static Map<String, ComponentMaker> componentProcessors = new HashMap<String, ComponentMaker>();

    private List<WElement> wes = new ArrayList<WElement>();

    private Map<Integer, Resource> resources = new HashMap<Integer, Resource>();

    private Map<Integer, List<WElement>> structure = new HashMap<Integer, List<WElement>>();

    private Map<String, Integer> rForwardLookup = new HashMap<String, Integer>();

    private Map<Integer, String> rReverseLookup = new HashMap<Integer, String>();

    private Set<Integer> ids = new HashSet<Integer>();

    private Set<Integer> defaultResources = new HashSet<Integer>();

    private Map<WElement, String> pluginTypes = new HashMap<WElement, String>();

    static {
        componentProcessors.put("function", new FunctionProcessor());
        componentProcessors.put("plugin", new PluginProcessor());
        outputProcessors.put("net.sourceforge.ondex.workflow.Parameters.AbstractONDEXPluginInit", new PluginArgOutProcessor());
        inputProcessors.put("net.sourceforge.ondex.workflow.Parameters.AbstractONDEXPluginInit", new PluginArgParser());
    }

    public ProcessingJob(String[] names, Object[] resources) {
        if (names == null) return;
        for (int i = 0; i < names.length; i++) {
            addInternalResource(names[i], resources[i]);
        }
    }

    public ProcessingJob(String fileName) {
        load(new File(fileName));
    }

    @SuppressWarnings("unchecked")
    public void load(File file) {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(file);
            Element root = doc.getRootElement();
            Element eCom = root.getChild("Components");
            List<Element> components = eCom.getChildren();
            for (Element component : components) {
                ComponentMaker cm = componentProcessors.get(component.getAttributeValue("type"));
                WElement we = cm.makeComponent(component);
                pluginTypes.put(we, component.getAttributeValue("class"));
                this.addFunctionElement(we, makeList(component.getChild("InputList")), makeList(component.getChild("OutputList")));
            }
            Element eRes = root.getChild("Resources");
            List<Element> resources = eRes.getChildren();
            for (Element resource : resources) {
                this.addResource(resource.getChildText("GlobalId"), convert(resource));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Resource> getResources() {
        return resources;
    }

    public Map<Integer, List<WElement>> getAllElements() {
        return structure;
    }

    public Map<Integer, List<WElement>> getEnabledElements() {
        Map<Integer, List<WElement>> result = new HashMap<Integer, List<WElement>>();
        for (Entry<Integer, List<WElement>> ent : structure.entrySet()) {
            List<WElement> list = new ArrayList<WElement>();
            for (WElement e : ent.getValue()) {
                if (e.isEnabled()) list.add(e);
            }
            if (list.size() > 0) result.put(ent.getKey(), list);
        }
        return result;
    }

    public void addResource(String name, Object resource) {
        if (name == null) return;
        Integer id = getId(name);
        resources.put(id, new Resource(id, resource));
    }

    public void removeResource(String name) {
        Integer id = rForwardLookup.remove(name);
        rReverseLookup.remove(id);
        resources.remove(id);
        ids.add(id);
    }

    public Integer getId(String strId) {
        Integer id = rForwardLookup.get(strId);
        if (id != null) return id;
        if (ids.size() > 0) {
            id = ids.iterator().next();
            ids.remove(id);
        } else {
            id = rForwardLookup.size();
        }
        rForwardLookup.put(strId, id);
        rReverseLookup.put(id, strId);
        return id;
    }

    public void addFunctionElement(WElement we, List<String> inputs, List<String> outputs) {
        try {
            Integer[] in;
            if (inputs == null || inputs.size() == 0) {
                addResource(INIT, null);
                in = new Integer[] { getId(INIT) };
            } else {
                in = convertStrToInt(inputs);
            }
            we.setIncomingAddresses(in);
            for (Integer i : in) {
                if (i == null) continue;
                List<WElement> list = structure.get(i);
                if (list == null) {
                    list = new ArrayList<WElement>();
                    structure.put(i, list);
                }
                list.add(we);
            }
            if (outputs != null && outputs.size() != 0) {
                we.setOutgoingAddresses(convertStrToInt(outputs));
            }
            wes.add(we);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer[] convertStrToInt(List<String> strIds) {
        Integer[] result = new Integer[strIds.size()];
        for (int i = 0; i < strIds.size(); i++) {
            String strID = strIds.get(i);
            result[i] = strID == null ? null : getId(strID);
        }
        return result;
    }

    private Object convert(Element e) {
        Element value = e.getChild("Value");
        String clas = getConsumerClass(e.getChild("GlobalId").getText());
        String type = value.getAttribute("type").getValue();
        InputProcessor ip = inputProcessors.get(type);
        if (ip != null) {
            return ip.convert(value, clas);
        } else if (type.equals("java.lang.String")) {
            return value.getText();
        } else {
            try {
                Class<?> cls = Class.forName(type);
                return cls.getMethod("valueOf", STR_ARG).invoke(null, new Object[] { value.getText() });
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> makeList(Element e) {
        List<Element> items = e.getChildren();
        int size = 0;
        for (Element item : items) {
            int candidateMax = Integer.valueOf(item.getChildText("LocalId"));
            if (size < candidateMax) size = candidateMax;
        }
        List<String> result = Arrays.asList(new String[size + 1]);
        for (Element item : items) {
            result.set(Integer.valueOf(item.getChildText("LocalId")), item.getChildText("GlobalId"));
        }
        return result;
    }

    public void save(File file) {
        Format format = Format.getPrettyFormat();
        format.setTextMode(TextMode.PRESERVE);
        XMLOutputter outputter = new XMLOutputter(format);
        Element rootElement = new Element("Ondex");
        rootElement.setAttribute("version", "2.0");
        Element main = new Element("Resources");
        Element value;
        Object content;
        for (Entry<Integer, Resource> ent : resources.entrySet()) {
            if (defaultResources.contains(ent.getKey())) continue;
            Resource r = ent.getValue();
            Element resource = new Element("Resource");
            Element globalid = new Element("GlobalId");
            globalid.setText(rReverseLookup.get(r.getId()));
            content = r.accessValue();
            if (content == null) {
                value = new Element("Value");
                value.setAttribute("type", "java.lang.Object");
            } else {
                String type = content.getClass().getCanonicalName();
                if (outputProcessors.containsKey(type)) {
                    value = outputProcessors.get(type).convert(content);
                } else {
                    value = new Element("Value");
                    value.setText(content.toString());
                }
                value.setAttribute("type", type);
            }
            resource.addContent(globalid);
            resource.addContent(value);
            main.addContent(resource);
        }
        rootElement.addContent(main);
        main = new Element("Components");
        for (WElement we : wes) {
            Element component = new Element("Component");
            component.setAttribute("type", we.getType());
            for (Entry<String, String> ent : we.getDefinition().entrySet()) {
                component.setAttribute(ent.getKey(), ent.getValue());
            }
            Element id = new Element("UniqueID");
            id.setText(we.getUniqueTypeId());
            Element priority = new Element("Priority");
            priority.setText(String.valueOf(we.getPriority()));
            Element enabled = new Element("Enabled");
            enabled.setText(String.valueOf(we.isEnabled()));
            component.addContent(id);
            component.addContent(priority);
            component.addContent(enabled);
            component.addContent(processList("Input", we.getIncoming()));
            component.addContent(processList("Output", we.getOutgoing()));
            main.addContent(component);
        }
        rootElement.addContent(main);
        Document doc = new Document(rootElement);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            outputter.output(doc, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Element processList(String type, Integer[] list) {
        Element result = new Element(type + "List");
        for (int i = 0; i < list.length; i++) {
            if (list[i] == null) continue;
            Element tuple = new Element(type);
            Element globalId = new Element("GlobalId");
            globalId.setText(rReverseLookup.get(list[i]));
            Element localId = new Element("LocalId");
            localId.setText(String.valueOf(i));
            tuple.addContent(globalId);
            tuple.addContent(localId);
            result.addContent(tuple);
        }
        return result;
    }

    private static class FunctionProcessor implements ComponentMaker {

        public FunctionProcessor() {
        }

        public WElement makeComponent(Element e) {
            try {
                String id = e.getChildText("UniqueID");
                Class<?> cls = Class.forName(e.getAttributeValue("class"));
                StandartElement se;
                if (e.getAttributeValue("argumentTypes") != null) {
                    String[] strArgs = e.getAttributeValue("argumentTypes").split(", *");
                    Class<?>[] args = new Class<?>[strArgs.length];
                    for (int i = 0; i < strArgs.length; i++) {
                        args[i] = Class.forName(strArgs[i]);
                    }
                    Method m = cls.getMethod(e.getAttributeValue("method"), args);
                    se = new StandartElement(m, id);
                } else {
                    se = new StandartElement(cls, e.getAttributeValue("method"), id);
                }
                return se;
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }

    private static class PluginProcessor implements ComponentMaker {

        public PluginProcessor() {
        }

        public WElement makeComponent(Element e) {
            try {
                String id = e.getChildText("UniqueID");
                Class<?> cls = Class.forName(e.getAttributeValue("class"));
                PluginWElement se = new PluginWElement(cls, id);
                return se;
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }

    private static class PluginArgOutProcessor implements OutputProcessor {

        public PluginArgOutProcessor() {
        }

        public Element convert(Object o) {
            Element value = new Element("Value");
            AbstractArguments aa = ((AbstractONDEXPluginInit) o).getArguments();
            addElement(value, "definitionsFrom", ((AbstractONDEXPluginInit) o).getArguments().getClass().getCanonicalName());
            for (Entry<String, List<Object>> ent : (aa.getOptions().entrySet())) {
                for (Object z : ent.getValue()) {
                    if (z != null) {
                        addElement(value, ent.getKey(), z.toString());
                    }
                }
            }
            String dir = null;
            try {
                dir = (String) aa.getClass().getMethod("getInputDir", NO_ARGS).invoke(aa, NO_IN);
            } catch (Exception e) {
            }
            if (dir != null) {
                addElement(value, "datadir", dir);
            }
            String file = null;
            try {
                file = (String) aa.getClass().getMethod("getInputFile", NO_ARGS).invoke(aa, NO_IN);
            } catch (Exception e) {
            }
            if (file != null) {
                addElement(value, "importfile", file);
            }
            try {
                file = (String) aa.getClass().getMethod("getExportFile", NO_ARGS).invoke(aa, NO_IN);
            } catch (Exception e) {
            }
            if (file != null) {
                addElement(value, "exportfile", file);
            }
            return value;
        }
    }

    private static class PluginArgParser implements InputProcessor {

        public Object convert(Element e, String clas) {
            List<ValueTuple<String, String>> toParse = new ArrayList<ValueTuple<String, String>>();
            String cls = null;
            for (Object o : e.getChildren()) {
                Element child = (Element) o;
                if (child.getName().equals("definitionsFrom")) cls = child.getText(); else {
                    toParse.add(new ValueTuple<String, String>(child.getName(), child.getText()));
                }
            }
            return WrappedEngine.process(toParse, cls, clas);
        }
    }

    public static Element addElement(Element parent, String name, String text) {
        Element e = new Element(name);
        if (text != null) {
            e.setText(text);
        }
        parent.addContent(e);
        return e;
    }

    public static void addInputProcessor(String type, InputProcessor ip) {
        inputProcessors.put(type, ip);
    }

    public static void addOutputProcessor(String type, OutputProcessor op) {
        outputProcessors.put(type, op);
    }

    public static void addComponentProcessor(String type, ComponentMaker cm) {
        componentProcessors.put(type, cm);
    }

    public void addInternalResource(String name, Object resource) {
        Integer id = getId(name);
        defaultResources.add(id);
        Resource r = new Resource(id, resource);
        r.setLocked(true);
        resources.put(id, r);
    }

    public boolean isRequired(String resourceName) {
        return rForwardLookup.containsKey(resourceName);
    }

    public String getConsumerClass(String strId) {
        if (structure.get(this.getId(strId)).size() != 1) return null;
        return pluginTypes.get(structure.get(this.getId(strId)).get(0));
    }

    public String enshureIdIsUnique(String id) {
        Random generator = new Random();
        while (rForwardLookup.containsKey(id)) {
            id = id + Character.toString((char) generator.nextInt(26));
        }
        return id;
    }

    public void printDebugInfo() {
        System.err.println("Resource reginstry: ");
        for (Resource r : this.getResources().values()) {
            System.err.println(r.getId() + " : " + r.accessValue() + "|");
        }
        System.err.println("Component registry: ");
        for (Entry<Integer, List<WElement>> ent : this.getAllElements().entrySet()) {
            System.err.print(ent.getKey());
            for (WElement w : ent.getValue()) {
                System.err.print(" : " + w.getUniqueTypeId() + ":" + w.toString());
            }
            System.err.print("|\n");
        }
    }
}
