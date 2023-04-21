package at.ac.tuwien.j3dvneclipse.editors;

import java.awt.Frame;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.vecmath.Color3f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import at.ac.tuwien.j3dvn.control.ConversionAddon;
import at.ac.tuwien.j3dvn.control.ConversionConnector;
import at.ac.tuwien.j3dvn.control.DataVisualMapping;
import at.ac.tuwien.j3dvn.control.AddonManager;
import at.ac.tuwien.j3dvn.control.InputAddon;
import at.ac.tuwien.j3dvn.control.InputConnector;
import at.ac.tuwien.j3dvn.control.LayoutConnector;
import at.ac.tuwien.j3dvn.control.AddonManager.Descriptor;
import at.ac.tuwien.j3dvn.control.DataVisualMapping.Entry;
import at.ac.tuwien.j3dvn.model.DataAddon;
import at.ac.tuwien.j3dvn.model.Model;
import at.ac.tuwien.j3dvn.view.GraphCanvas;
import at.ac.tuwien.j3dvn.view.LayoutAddon;
import at.ac.tuwien.j3dvn.view.VisualAddon;

/**
 *
 */
public class Graph3DEditor extends EditorPart {

    private static final String XML_ROOT = "j3dvn";

    private static final String XML_INPUT = "input";

    private static final String XML_MAPPING = "mapping";

    private static final String XML_LAYOUT = "layout";

    private static final String XML_VISUALIZATION = "visualization";

    private static final String XML_PROPERTY = "property";

    private static final String XML_MAP = "map";

    private static final String XML_PROPMAP = "propmap";

    private static final String XML_NAME_ATTRIBUTE = "name";

    private static final String XML_DATA_ATTRIBUTE = "data";

    private static final String XML_VISUAL_ATTRIBUTE = "visual";

    private static final String XML_CONVERSION_ATTRIBUTE = "conversion";

    private static final String XML_BACKGROUND = "background";

    private static final String XML_LIGHT = "light";

    private static final String XML_IMAGE = "image";

    private static final String XML_SCALING = "scaling";

    private static final String SCALE_ALL = "all";

    private static final String SCALE_MAX = "max";

    private static final String SCALE_MIN = "min";

    private static final String SCALE_NONE = "none";

    private static final String SCALE_CENTER = "center";

    private static final String SCALE_REPEAT = "repeat";

    private static final String XML_COLOR = "color";

    private static final String XML_RED = "r";

    private static final String XML_GREEN = "g";

    private static final String XML_BLUE = "b";

    private static final String XML_X = "x";

    private static final String XML_Y = "y";

    private static final String XML_Z = "z";

    private static final String XML_TYPE = "type";

    private static final String XML_AMBIENT = "ambient";

    private static final String XML_DIRECTIONAL = "directional";

    private static final String XML_DIRECTION = "direction";

    private static final String XML_ANTI_ALIAS = "antialias";

    private static final String XML_VALUE = "value";

    private IEditorInput editorInput;

    public GraphCanvas graphCanvas;

    private AddonManager addonManager = AddonManager.getInstance();

    private InputConnector inputConnector;

    private DataVisualMapping dvm;

    public Graph3DEditor() {
        super();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        editorInput = input;
        setPartName(editorInput.getName());
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void createPartControl(Composite parent) {
        InputStream input = null;
        String inputName = null;
        try {
            input = ((IFileEditorInput) editorInput).getFile().getContents();
            Element root = openInput(input);
            if (root != null) {
                inputName = getAttribute(root.getElementsByTagName(XML_INPUT).item(0), XML_NAME_ATTRIBUTE);
            }
            input = ((IFileEditorInput) editorInput).getFile().getContents();
        } catch (CoreException e) {
            System.err.println("Error reading input file.");
        }
        if ((inputName == null) || (inputName == "")) inputConnector = addonManager.createInputFor(((IFileEditorInput) editorInput).getFile().getName()); else {
            Descriptor<? extends InputAddon> inputDescriptor = addonManager.getAddonDescriptor(inputName, InputAddon.class);
            InputAddon inputAddon = inputDescriptor.newInstance();
            inputConnector = addonManager.createConnector(inputAddon);
        }
        inputConnector.open(input);
        Model m = inputConnector.getModel();
        graphCanvas = new GraphCanvas();
        dvm = new DataVisualMapping();
        dvm.initModel(m);
        LayoutConnector layout = null;
        try {
            input = ((IFileEditorInput) editorInput).getFile().getContents();
            Element root = openInput(input);
            if (root != null) {
                loadMapping(root.getElementsByTagName(XML_MAPPING).item(0), dvm);
                layout = loadLayout(root.getElementsByTagName(XML_LAYOUT).item(0), graphCanvas);
                loadVisualization(root.getElementsByTagName(XML_VISUALIZATION).item(0), graphCanvas);
            }
        } catch (CoreException e) {
            System.err.println("Error reading input file.");
        }
        graphCanvas.setDataVisualMapping(dvm);
        graphCanvas.setModel(m);
        graphCanvas.setLayoutConnector(layout);
        graphCanvas.layout();
        Composite composite = new Composite(parent, SWT.EMBEDDED);
        composite.setBounds(20, 20, 300, 200);
        Frame frame = SWT_AWT.new_Frame(composite);
        frame.add(graphCanvas.getCanvas3D());
    }

    /**
	 * @param node
	 * @param graphCanvas2
	 */
    private void loadVisualization(Node visualizationNode, GraphCanvas canvas) {
        if (visualizationNode == null) return;
        NodeList nodes = visualizationNode.getChildNodes();
        Node node;
        for (int i = 0; i < nodes.getLength(); i++) {
            node = nodes.item(i);
            if (XML_BACKGROUND.equals(node.getNodeName())) {
                setupBackground(node, canvas);
            } else if (XML_LIGHT.equals(node.getNodeName())) {
                createLight(node, canvas);
            } else if (XML_ANTI_ALIAS.equals(node.getNodeName())) {
                String sValue = getAttribute(node, XML_VALUE);
                boolean value = Boolean.parseBoolean(sValue);
                canvas.setAntiAlias(value);
            }
        }
    }

    /**
	 * @param node
	 * @param canvas
	 */
    private void createLight(Node node, GraphCanvas canvas) {
        String type = getAttribute(node, XML_TYPE);
        Tuple3f triple = getTriple(node, XML_COLOR, XML_RED, XML_GREEN, XML_BLUE);
        Color3f color;
        if (triple == null) color = new Color3f(1, 1, 1); else color = new Color3f(triple);
        Light light = null;
        if (XML_AMBIENT.equals(type)) {
            light = new AmbientLight();
        } else if (XML_DIRECTIONAL.equals(type)) {
            light = new DirectionalLight();
            Tuple3f dirTriple = getTriple(node, XML_DIRECTION, XML_X, XML_Y, XML_Z);
            Vector3f direction = (dirTriple == null) ? new Vector3f(1, -1, 0) : new Vector3f(dirTriple);
            ((DirectionalLight) light).setDirection(direction);
        }
        light.setColor(color);
        canvas.addLight(light);
    }

    /**
	 * @param node
	 * @param canvas
	 */
    private void setupBackground(Node node, GraphCanvas canvas) {
        String imageFile = getAttribute(node, XML_IMAGE);
        String scale = getAttribute(node, XML_SCALING);
        int scaleId = Background.SCALE_FIT_ALL;
        if (SCALE_ALL.equals(scale)) scaleId = Background.SCALE_FIT_ALL; else if (SCALE_MAX.equals(scale)) scaleId = Background.SCALE_FIT_MAX; else if (SCALE_MIN.equals(scale)) scaleId = Background.SCALE_FIT_MIN; else if (SCALE_NONE.equals(scale)) scaleId = Background.SCALE_NONE; else if (SCALE_CENTER.equals(scale)) scaleId = Background.SCALE_NONE_CENTER; else if (SCALE_REPEAT.equals(scale)) scaleId = Background.SCALE_REPEAT;
        if ((imageFile != null) && (imageFile != "")) canvas.setBackgroundImage(imageFile, scaleId);
        Tuple3f colorTriple = getTriple(node, XML_COLOR, XML_RED, XML_GREEN, XML_BLUE);
        if (colorTriple != null) {
            Color3f color = new Color3f(colorTriple);
            Background background = canvas.getBackground();
            background.setColor(color);
        }
    }

    private Tuple3f getTriple(Node parentNode, String nodeName, String attr1, String attr2, String attr3) {
        Node node = getChildNode(parentNode, nodeName);
        if (node == null) return null;
        String s1 = getAttribute(node, attr1);
        String s2 = getAttribute(node, attr2);
        String s3 = getAttribute(node, attr3);
        try {
            float f1 = Float.parseFloat(s1);
            float f2 = Float.parseFloat(s2);
            float f3 = Float.parseFloat(s3);
            return new Vector3f(f1, f2, f3);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Node getChildNode(Node parent, String childName) {
        if (parent == null) return null;
        NodeList nodeList = parent.getChildNodes();
        Node node;
        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);
            if (childName.equals(node.getNodeName())) {
                return node;
            }
        }
        return null;
    }

    /**
	 * @param input
	 * @param dvm2
	 */
    private void loadMapping(Node mappingNode, DataVisualMapping mapping) {
        if (mappingNode == null) return;
        NodeList nodes = mappingNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            checkMapNode(nodes.item(i), mapping);
        }
    }

    private LayoutConnector loadLayout(Node layoutNode, GraphCanvas canvas) {
        if (layoutNode == null) return null;
        String layoutName = getAttribute(layoutNode, XML_NAME_ATTRIBUTE);
        LayoutConnector layoutConnector;
        try {
            Descriptor<? extends LayoutAddon> layoutDescriptor = addonManager.getAddonDescriptor(layoutName, LayoutAddon.class);
            LayoutAddon layoutAddon = layoutDescriptor.newInstance();
            layoutConnector = addonManager.createConnector(layoutAddon);
        } catch (IllegalArgumentException e) {
            return null;
        }
        Map<String, Object> properties = getPropertyNodes(layoutNode.getChildNodes());
        try {
            Object value;
            for (String name : properties.keySet()) {
                value = properties.get(name);
                if (value instanceof String) layoutConnector.setPropertyCast(name, (String) value); else layoutConnector.setProperty(name, value);
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
        return layoutConnector;
    }

    private Element openInput(InputStream input) {
        DocumentBuilder db;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            db = dbf.newDocumentBuilder();
        } catch (FactoryConfigurationError e) {
            return null;
        } catch (ParserConfigurationException e) {
            return null;
        }
        Document doc;
        try {
            doc = db.parse(input);
        } catch (SAXException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        Element root = doc.getDocumentElement();
        return (XML_ROOT.equals(root.getTagName())) ? root : null;
    }

    /**
	 * @param node
	 */
    private void checkMapNode(Node node, DataVisualMapping mapping) {
        if (!XML_MAP.equals(node.getNodeName())) return;
        String dataValue = getAttribute(node, XML_DATA_ATTRIBUTE);
        String visualValue = getAttribute(node, XML_VISUAL_ATTRIBUTE);
        Descriptor<? extends DataAddon> dataDescriptor = addonManager.getAddonDescriptor(dataValue, DataAddon.class);
        Class<? extends DataAddon> dataClass = dataDescriptor.getAddonClass();
        Descriptor<? extends VisualAddon> visualDescriptor = addonManager.getAddonDescriptor(visualValue, VisualAddon.class);
        Entry entry = mapping.new Entry(dataDescriptor, visualDescriptor);
        mapping.set(entry);
        NodeList propMapNodes = node.getChildNodes();
        for (int i = 0; i < propMapNodes.getLength(); i++) checkPropMapNode(propMapNodes.item(i), dataClass, mapping);
    }

    /**
	 * Checks one propmap node 
	 */
    private void checkPropMapNode(Node node, Class<? extends DataAddon> dataClass, DataVisualMapping mapping) {
        if (!XML_PROPMAP.equals(node.getNodeName())) return;
        String dataName = getAttribute(node, XML_DATA_ATTRIBUTE);
        String visualName = getAttribute(node, XML_VISUAL_ATTRIBUTE);
        String conversionName = getAttribute(node, XML_CONVERSION_ATTRIBUTE);
        Descriptor<? extends ConversionAddon> conversionDescriptor = addonManager.getAddonDescriptor(conversionName, ConversionAddon.class);
        if (conversionDescriptor == null) return;
        ConversionAddon conversion = conversionDescriptor.newInstance();
        ConversionConnector conversionConnector = addonManager.createConnector(conversion);
        Map<String, Object> properties = getPropertyNodes(node.getChildNodes());
        Object value;
        for (String name : properties.keySet()) {
            value = properties.get(name);
            if (value instanceof String) conversionConnector.setPropertyCast(name, (String) value); else conversionConnector.setProperty(name, value);
        }
        mapping.setProperty(dataClass, dataName, visualName, conversion);
    }

    /**
	 * Takes a node list and returns a map with key-value pairs of properties in
	 * that node list. Properties are recognized through <code>property</code>
	 * nodes. The key is the property node's <code>name</code> attribute. The
	 * value is the property node's text content.
	 * 
	 * @param nodes
	 *            A list of nodes
	 * @return A map with key-value pairs of properties. If <code>nodes</code>
	 *         is null, an empty map is returned.
	 */
    private Map<String, Object> getPropertyNodes(NodeList nodes) {
        Node node;
        String name;
        Object content;
        Map<String, Object> result = new HashMap<String, Object>();
        if (nodes != null) {
            for (int i = 0; i < nodes.getLength(); i++) {
                node = nodes.item(i);
                if (XML_PROPERTY.equals(node.getNodeName())) {
                    name = getAttribute(node, XML_NAME_ATTRIBUTE);
                    Tuple3f triple = getTriple(node, XML_COLOR, XML_RED, XML_GREEN, XML_BLUE);
                    if (triple != null) content = new Color3f(triple); else content = node.getTextContent();
                    result.put(name, content);
                }
            }
        }
        return result;
    }

    /**
	 * Retrieves the value of an attribute of a node
	 * 
	 * @param node
	 *            The node, which contains attribute <code>attribute</code>.
	 * @param attribute
	 *            The name of the attribute, of which its value should be
	 *            returned.
	 * @return The value of attribute <code>attribute</code>. If any of the
	 *         parameters is null, null is returned. If <code>node</code>
	 *         doesn't have an attribute called <code>attribute</code>, an
	 *         empty string is returned.
	 */
    private String getAttribute(Node node, String attribute) {
        if ((node == null) || (attribute == null)) return null;
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) return "";
        Attr attrNode = (Attr) attributes.getNamedItem(attribute);
        if (attrNode == null) return "";
        return attrNode.getValue();
    }

    @Override
    public void setFocus() {
    }

    public InputConnector getInput() {
        return inputConnector;
    }

    public DataVisualMapping getDataVisualMapping() {
        return dvm;
    }
}
