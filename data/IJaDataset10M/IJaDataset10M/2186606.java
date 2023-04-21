package sqba.jamper.map;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sqba.jamper.map.object.ChooseBox;
import sqba.jamper.map.object.DragableMapObject;
import sqba.jamper.map.object.FunctionBox;
import sqba.jamper.map.object.IMapObject;
import sqba.jamper.map.object.IfBox;
import sqba.jamper.map.object.LinkLine;
import sqba.jamper.map.object.VariableBox;
import sqba.jamper.map.object.WhenBox;
import sqba.jamper.map.tree.MapperTree;
import sqba.jamper.map.object.DefaultMapObject;
import sqba.jamper.map.object.ExpressionBox;
import sqba.jamper.util.DOMUtils;
import sqba.jamper.util.XPathUtils;
import sqba.jamper.map.Objects;
import sqba.jamper.util.PathList;
import sqba.jamper.util.XMLMapperInit;
import sqba.jamper.JamperFrame;
import java.net.MalformedURLException;

public class Map extends DefaultMapObject {

    private Objects _objects = new Objects(this);

    private MapperTree _srcTree = null;

    private MapperTree _dstTree = null;

    private JPanel _panel = null;

    private boolean _bLoaded = true;

    private String _uri;

    private class MyTreeSelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            Map.this.repaint();
        }
    }

    private class MyTreeExpansionListener implements TreeExpansionListener {

        public void treeCollapsed(TreeExpansionEvent event) {
            Map.this.repaint();
        }

        public void treeExpanded(TreeExpansionEvent event) {
            Map.this.repaint();
        }
    }

    public Map() {
        _bLoaded = true;
    }

    public Map(JPanel panel, MapperTree lTree, MapperTree rTree) {
        _panel = panel;
        _srcTree = lTree;
        _dstTree = rTree;
        MyTreeSelectionListener tsv = new MyTreeSelectionListener();
        lTree.addTreeSelectionListener(tsv);
        rTree.addTreeSelectionListener(tsv);
        MyTreeExpansionListener tev = new MyTreeExpansionListener();
        lTree.addTreeExpansionListener(tev);
        rTree.addTreeExpansionListener(tev);
    }

    public Collection getChildObjects() {
        return _objects;
    }

    public MapperTree getSource() {
        return _srcTree;
    }

    public MapperTree getDestination() {
        return _dstTree;
    }

    public JPanel getPanel() {
        return _panel;
    }

    public void removeChild(IMapObject obj) {
        _objects.remove(obj);
        repaint();
    }

    public void clear() {
        _objects.clear();
        repaint();
    }

    public void draw(Graphics g, JComponent parent) {
        if (!_bLoaded) return;
        _objects.draw(g, parent);
    }

    public boolean hitTest(Point pt) {
        return _objects.hitTest(pt);
    }

    public IMapObject getChildAt(Point pt) {
        return _objects.get(pt);
    }

    public LinkLine addLink(ILinkable src, ILinkable dst) {
        LinkLine line = _objects.addLink(src, dst);
        _panel.repaint();
        return line;
    }

    public IMapObject getSelectedObject() {
        return _objects.getSelectedObject();
    }

    public void deselectAll() {
        _objects.deselectAll();
        if (null != _srcTree) ((MapperTree) _srcTree).deselectAll(null);
        if (null != _dstTree) ((MapperTree) _dstTree).deselectAll(null);
        _panel.repaint();
    }

    public void setPanel(JPanel panel) {
        _panel = panel;
    }

    public void addDragableObject(DragableMapObject obj) {
        _panel.addMouseMotionListener(obj);
        _objects.add(obj);
        _panel.repaint();
    }

    public FunctionBox addNewFunction(Point pt) {
        int i = _objects.getFunctionCount();
        String name = "New function " + i;
        FunctionBox func = new FunctionBox(this, name, pt);
        addDragableObject(func);
        func.chooseFunction();
        return func;
    }

    public VariableBox addNewVariable(Point pt) {
        int i = _objects.getVariableCount();
        String name = "New Variable " + i;
        VariableBox vbox = new VariableBox(this, name, pt);
        addDragableObject(vbox);
        return vbox;
    }

    public IfBox addNewIfBox(Point pt) {
        IfBox ifbox = new IfBox(this, pt);
        addDragableObject(ifbox);
        return ifbox;
    }

    public WhenBox addNewWhenBox(Point pt) {
        WhenBox whenBox = new WhenBox(this, pt);
        addDragableObject(whenBox);
        return whenBox;
    }

    public ChooseBox addNewChooseBox(Point pt) {
        ChooseBox choosebox = new ChooseBox(this, pt);
        addDragableObject(choosebox);
        return choosebox;
    }

    public ExpressionBox addNewComplexExpression(Point pt) {
        ExpressionBox expbox = new ExpressionBox(this, pt);
        addDragableObject(expbox);
        return expbox;
    }

    public void objectSelected(IMapObject sel) {
        if (null != _srcTree) ((MapperTree) _srcTree).deselectAll(null);
        if (null != _dstTree) ((MapperTree) _dstTree).deselectAll(null);
        _objects.objectSelected(sel);
        repaint();
    }

    public void clearLinks() {
        _objects.clearLinks();
        repaint();
    }

    public IMapObject getChild(String uniqueName) {
        return _objects.get(uniqueName);
    }

    public ILinkable getLinkable(String uniqueName) {
        return _objects.getLinkable(uniqueName);
    }

    public void repaint() {
        if (null != _srcTree) _srcTree.repaint();
        if (null != _dstTree) _dstTree.repaint();
        if (null != _panel) _panel.repaint();
    }

    public String getDisplayName() {
        return "Map";
    }

    public String getFullPath() {
        return "Map";
    }

    public int getID() {
        return hashCode();
    }

    public void setID(int ID) {
    }

    public String getName() {
        return "Map";
    }

    public Point getOrigin() {
        return null;
    }

    public IMapObject getParentObject() {
        return null;
    }

    public String getUniqueName() {
        return getName();
    }

    public boolean isSelected() {
        return false;
    }

    public void select(boolean sel) {
        deselectAll();
    }

    public Element save(Element node) {
        Document doc = node.getOwnerDocument();
        if (null != _srcTree) {
            Element srcElement = doc.createElement("src");
            srcElement.setTextContent(_srcTree.getURI());
            srcElement.setAttribute("root", _srcTree.getRootNode().getFullPath());
            node.appendChild(srcElement);
        }
        if (null != _dstTree) {
            Element dstElement = doc.createElement("dst");
            dstElement.setTextContent(_dstTree.getURI());
            dstElement.setAttribute("root", _dstTree.getRootNode().getFullPath());
            node.appendChild(dstElement);
        }
        _objects.save(node);
        return node;
    }

    public boolean loadNoClear(Element node) {
        _bLoaded = false;
        _panel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Node srcNode = XPathUtils.getNode(node, "src");
        if (null != srcNode) loadTree(_srcTree, (Element) srcNode);
        Node dstNode = XPathUtils.getNode(node, "dst");
        if (null != dstNode) loadTree(_dstTree, (Element) dstNode);
        _objects.load(node);
        _bLoaded = true;
        repaint();
        _panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        return false;
    }

    public boolean load(Element node) {
        _bLoaded = false;
        _panel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        clear();
        Node srcNode = XPathUtils.getNode(node, "src");
        if (null != srcNode) loadTree(_srcTree, (Element) srcNode);
        Node dstNode = XPathUtils.getNode(node, "dst");
        if (null != dstNode) loadTree(_dstTree, (Element) dstNode);
        _objects.load(node);
        _bLoaded = true;
        repaint();
        _panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        return false;
    }

    private String getURL(String filename) {
        String s = filename;
        File f1 = new File(_uri);
        File f2 = new File(filename);
        if (null == JamperFrame._documentBase && !f2.isAbsolute()) {
            s = XMLMapperInit.getPathOnly(f1.getAbsolutePath()) + s;
        } else if (null != JamperFrame._documentBase) {
            try {
                URL url = new URL(JamperFrame._documentBase, s);
                s = url.toString();
            } catch (MalformedURLException e) {
                System.out.println(e);
            }
        }
        return s;
    }

    private boolean loadTree(MapperTree tree, Element node) {
        String path = XPathUtils.getNodeText(node, "text()");
        if ("" != path.trim()) {
            path = getURL(path);
            System.out.print("Loading " + path + "\n");
            tree.load(path);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String rootPath = XPathUtils.getNodeText(node, "@root/text()");
            if ("" != rootPath.trim()) tree.setRoot(rootPath);
            return true;
        }
        return false;
    }

    public void load(String uri) {
        _uri = uri;
        Document doc = DOMUtils.loadDocument(uri);
        if (null != doc) {
            Node root = XPathUtils.getNode(doc, "jamper");
            if (null != root) {
                load((Element) root);
            }
        }
    }

    public void save(String uri) {
        Document doc = DOMUtils.newDocument();
        Element mapElement = doc.createElement("jamper");
        save(mapElement);
        doc.appendChild(mapElement);
        DOMUtils.renderXMLString(doc, new File(uri));
    }

    public void renderGlobalDeclarations(PathList context, Element parent) {
        Node firstChild = parent.getFirstChild();
        Document doc = parent.getOwnerDocument();
        Iterator iter = _objects.iterator();
        while (iter.hasNext()) {
            IMapObject obj = (IMapObject) iter.next();
            Element decl = null;
            if (obj instanceof VariableBox) {
                VariableBox var = (VariableBox) obj;
                if (var.isGlobal()) decl = var.renderDeclaration(context, doc);
            } else if (obj instanceof FunctionBox) {
                FunctionBox func = (FunctionBox) obj;
                if (func.isGlobal()) decl = func.renderDeclaration(context, doc);
            } else if (obj instanceof ExpressionBox) {
                ExpressionBox exp = (ExpressionBox) obj;
                if (exp.isTemplate()) decl = exp.renderDeclaration(context, doc);
            }
            if (null != decl) {
                String name = decl.getAttribute("name");
                Element var = getVariableThatUses(name, parent);
                if (null == var) var = (Element) firstChild;
                parent.insertBefore(decl, var);
            }
            decl = null;
        }
    }

    private Element getVariableThatUses(String name, Element parent) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element child = (Element) node;
                if (child instanceof Element) {
                    String nodename = child.getNodeName();
                    if (null != nodename && nodename.equals("xsl:variable")) {
                        String sel = child.getAttribute("select");
                        if (sel.contains(name)) return child;
                    }
                }
            }
        }
        return null;
    }

    public void removeLinksConnectedTo(IMapObject obj) {
        _objects.removeLinksConnectedTo(obj);
    }
}
