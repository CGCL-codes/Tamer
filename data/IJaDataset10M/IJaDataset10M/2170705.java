package org.adapit.wctoolkit.fomda.features.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.adapit.wctoolkit.fomda.diagram.featuresdiagram.FeaturesDiagramPaintPanel;
import org.adapit.wctoolkit.fomda.diagram.featuresdiagram.FeaturesDiagramPainterInternalFrame;
import org.adapit.wctoolkit.fomda.diagram.transformers.TransformerDiagramPainterInternalFrame;
import org.adapit.wctoolkit.fomda.features.util.Exportable;
import org.adapit.wctoolkit.fomda.features.view.popup.FeatureViewPopupMenu;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.models.config.InnerXMLExporter;
import org.adapit.wctoolkit.models.diagram.AbstractGraphicComponent;
import org.adapit.wctoolkit.models.util.AllElements;
import org.adapit.wctoolkit.models.util.ObserverMutableTreeNode;
import org.adapit.wctoolkit.models.util.ITreeDisplayable;
import org.adapit.wctoolkit.uml.ext.core.ElementImpl;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.ExcludesFeatureRelation;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.FeatureElement;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.FeatureRelation;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.MandatoryFeatureRelation;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.MultipleFeatureRelation;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.OptionalFeatureRelation;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.RequiresFeatureRelation;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import examples.gui.Resizable;

@SuppressWarnings({ "serial", "unchecked" })
public class FeatureViewComponent extends AbstractGraphicComponent implements Drawable, Exportable, MouseListener, MouseMotionListener, ITreeDisplayable, InnerXMLExporter {

    private static org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile messages = org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile.getInstance();

    protected int x = 100, y = 200;

    protected Point center;

    protected Point rigthDot, leftDot;

    protected Rectangle2D rectangle;

    public final float rasao = 5.22f;

    protected FeatureViewPopupMenu popup;

    protected JPanel drawingPanel;

    protected FeaturesDiagramPainterInternalFrame diagram;

    protected HashSet<ComponentMotionListener> componentMotionListeners = new HashSet<ComponentMotionListener>();

    protected boolean linked = false;

    protected HashSet<String> idLinkedFeaturesDiagrams = new HashSet<String>();

    protected HashSet<FeaturesDiagramPainterInternalFrame> linkedFeaturesDiagrams = new HashSet<FeaturesDiagramPainterInternalFrame>();

    protected HashSet<String> idTransformers = new HashSet<String>();

    protected HashSet<TransformerDiagramPainterInternalFrame> linkedTransformers = new HashSet<TransformerDiagramPainterInternalFrame>();

    protected float width = 20;

    protected float height = 20;

    protected float nameWidth = 0;

    protected float stereotypesWidth = 0;

    protected float stereotypesHeight = 0;

    protected int stereotypePixelNumber = 10;

    protected int namePixelNumber = 25;

    protected Point nameLabelLocation = new Point();

    private FeatureViewComponent parentFeatureComponent;

    double multiply = 5.5;

    protected int xLabelPosition = 0, yLabelPosition = 0;

    public int getXLabelPosition() {
        return xLabelPosition;
    }

    public void setXLabelPosition(int xLabelPosition) {
        this.xLabelPosition = xLabelPosition;
    }

    public void setBorders() throws Exception {
        if (width == 20) autoResize(); else {
            if (element.getAssignedStereotypes() != null && element.getAssignedStereotypes().size() > 0) {
                if ((namePixelNumber + stereotypesHeight) > height) height = namePixelNumber + stereotypesHeight;
            } else {
                if ((namePixelNumber) > height) height = namePixelNumber;
            }
            if (width < 10) width = 20;
            if (height < 10) height = 20;
            nameLabelLocation.x = (int) (center.x - (nameWidth / 2) + 5);
            nameLabelLocation.y = (int) (((center.y + (height / 2)) - 10));
            leftDot.x = (int) (center.x - (width / 2));
            leftDot.y = (int) (center.y - (height / 2));
            rigthDot.x = (int) (center.x + (width / 2));
            rigthDot.y = (int) (center.y + (height / 2));
            rectangle.setRect(center.x - (width / 2), center.y - (height / 2), width, height);
        }
    }

    protected int countCharsUpperCase(String val) {
        int m = 0;
        if (val == null || val.trim().equals("")) return 0;
        char[] chars = val.toCharArray();
        if (chars != null && chars.length > 0) {
            for (char c : chars) {
                if (Character.isUpperCase(c)) m++;
            }
        }
        return m;
    }

    public void autoResize() throws Exception {
        try {
            float maxWidth = width;
            if (element == null) return;
            if (element.getName().length() <= 3) multiply = 10.5; else if (element.getName().length() <= 6) multiply = 8.2; else if (((FeatureElement) element).getName().length() <= 10) multiply = 7.2; else if (((FeatureElement) element).getName().length() <= 15) multiply = 6.2; else if (((FeatureElement) element).getName().length() <= 18) multiply = 6.0; else if (((FeatureElement) element).getName().length() <= 20) multiply = 5.9; else if (((FeatureElement) element).getName().length() <= 25) multiply = 5.8; else if (((FeatureElement) element).getName().length() <= 30) multiply = 5.75; else multiply = 5.6888;
            nameWidth = (float) (multiply * (float) (((FeatureElement) element).getName().length()));
            if (nameWidth <= 10f) nameWidth = 15f;
            if (nameWidth > width) maxWidth = nameWidth;
            width = maxWidth;
            if (((FeatureElement) element).getAssignedStereotypes() != null && ((FeatureElement) element).getAssignedStereotypes().size() > 0) {
                if ((namePixelNumber + stereotypesHeight) > height) height = namePixelNumber + stereotypesHeight;
            } else {
                if ((namePixelNumber) > height) height = namePixelNumber;
            }
            if (width < 10) width = 20;
            if (height < 10) height = 20;
            nameLabelLocation.x = (int) (center.x - (nameWidth / 2) + 5);
            nameLabelLocation.y = (int) (((center.y + (height / 2)) - 10));
            leftDot.x = (int) (center.x - (width / 2));
            leftDot.y = (int) (center.y - (height / 2));
            rigthDot.x = (int) (center.x + (width / 2));
            rigthDot.y = (int) (center.y + (height / 2));
            rectangle.setRect(center.x - (width / 2), center.y - (height / 2), width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isRequiresOrExcludes() {
        return getFeature().getParentRelation() != null && (getFeature().getParentRelation() instanceof RequiresFeatureRelation || getFeature().getParentRelation() instanceof ExcludesFeatureRelation);
    }

    /**
	 * @return Returns the rectangle.
	 */
    public Rectangle2D getRectangle() {
        return rectangle;
    }

    public static final String ADD_OPTIONAL = messages.getMessage("Optional_Feature_0__1");

    public static final String ADD_MANDATORY = messages.getMessage("Mandatory_Feature_1");

    public static final String ADD_OPTIONAL_XOR = messages.getMessage("Optional_Exclusive_Features_0__1");

    public static final String ADD_MANDATORY_XOR = messages.getMessage("Mandatory_Exclusive_Features_1__1");

    public static final String ADD_OR = messages.getMessage("Mandatory_Features_1__*");

    public static final String ADD_OPTIONAL_OR = messages.getMessage("Optional_Features_0__*");

    public static final String ADD_REQUIRES = messages.getMessage("Requires_Relation");

    public static final String ADD_EXCLUDES = messages.getMessage("Excludes_Relation");

    public static final String ADD_TRANSFORMATION_DESCRIPTOR = messages.getMessage("Transformation_Descriptor");

    public FeatureViewComponent(FeatureElement feature) throws Exception {
        super();
        this.element = feature;
        center = new Point(-1, -1);
        rectangle = new Rectangle2D.Float();
        rigthDot = new Point(0, 0);
        leftDot = new Point(0, 0);
        setIcon("icons/feature.gif");
    }

    public FeatureViewComponent() throws Exception {
        super();
        center = new Point(-1, -1);
        rectangle = new Rectangle2D.Float();
        rigthDot = new Point(0, 0);
        leftDot = new Point(0, 0);
        setIcon("icons/feature.gif");
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        center.x = x;
        center.y = y;
        if (element != null) try {
            this.setBorders();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isInArea(int cordx, int cordy) {
        return this.isInBorders(cordx, cordy);
    }

    public void draw(Graphics g, JFrame frame) {
        try {
            Graphics2D g2 = (Graphics2D) g;
            Color bk = new Color(230, 230, 230);
            if (((FeatureElement) element).isSelected()) {
                g2.setColor(bk);
                g2.fill(this.rectangle);
                g2.setColor(Color.BLACK);
            }
            if (!isEditing) g2.drawString(this.getFeature().getName(), nameLabelLocation.x + xLabelPosition, nameLabelLocation.y + yLabelPosition);
            int i = 15;
            if (((FeatureElement) element).getRationals() != null && ((FeatureElement) element).getRationals().size() > 0) {
                Iterator it = ((FeatureElement) element).getRationals().iterator();
                while (it.hasNext()) {
                    String rat = (String) it.next();
                    if (rat == null) continue;
                    int length2 = rat.trim().length();
                    i += 10;
                    g2.setColor(Color.RED);
                    g2.drawString(rat, (int) (center.x - (3 * length2)), (center.y + (height / 2)) + i);
                    g2.drawLine((int) rectangle.getCenterX(), (int) rectangle.getMaxY(), (int) rectangle.getCenterX(), (int) rectangle.getMaxY() + 15);
                    g2.setColor(Color.BLACK);
                }
            }
            Stroke oldStroke = g2.getStroke();
            drawBorders(g2);
            g2.setStroke(oldStroke);
            if (linked) {
                g2.drawOval(center.x - 8, center.y + 5, 8, 6);
                g2.drawOval(center.x + 5, center.y + 3, 8, 6);
                g2.drawLine(center.x - 1, center.y + 8, center.x + 4, center.y + 7);
            }
            if (linkedTransformers != null && linkedTransformers.size() > 0) {
                g2.drawImage(cog_image, (int) rectangle.getMaxX(), center.y + 3, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Image cog_image = DefaultApplicationFrame.getIcon("/img/cog.png", 12, 12);

    private void drawBorders(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawRect((int) this.rectangle.getX(), (int) this.rectangle.getY(), (int) this.rectangle.getWidth(), (int) this.rectangle.getHeight());
    }

    private boolean isInBorders(int x, int y) {
        return rectangle.contains(x, y);
    }

    public void move(MouseEvent e) {
        Point p = getDifferenceFromPointer(e);
        this.center.x = this.center.x + p.x;
        this.center.y = this.center.y + p.y;
        try {
            this.setBorders();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public boolean isDraged() {
        return true;
    }

    @Override
    public void paintSelect(Graphics g, JFrame frame) {
        try {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(10, 10, 200));
            leftTop.setFrame(leftDot.x - (elipseRadius / 2), leftDot.y - (elipseRadius / 2), elipseRadius, elipseRadius);
            rightTop.setFrame(rigthDot.x - (elipseRadius / 2), rigthDot.y - (elipseRadius / 2), elipseRadius, elipseRadius);
            leftBotton.setFrame(leftDot.x - (elipseRadius / 2), rigthDot.y - (elipseRadius / 2), elipseRadius, elipseRadius);
            rightBotton.setFrame(rigthDot.x - (elipseRadius / 2), leftDot.y - (elipseRadius / 2), elipseRadius, elipseRadius);
            leftCenter.setFrame(leftDot.x - (elipseRadius / 2), center.y, elipseRadius, elipseRadius);
            rightCenter.setFrame(rigthDot.x - (elipseRadius / 2), center.y, elipseRadius, elipseRadius);
            topCenter.setFrame(center.x - (elipseRadius / 2), leftDot.y - (elipseRadius / 2), elipseRadius, elipseRadius);
            bottonCenter.setFrame(center.x - (elipseRadius / 2), rigthDot.y - (elipseRadius / 2), elipseRadius, elipseRadius);
            g2.fill(leftTop);
            g2.fill(leftBotton);
            g2.fill(rightTop);
            g2.fill(rightBotton);
            g2.fill(leftCenter);
            g2.fill(rightCenter);
            g2.fill(topCenter);
            g2.fill(bottonCenter);
            g2.setColor(new Color(0, 0, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Resizable resizer;

    public Resizable getResizer() {
        if (resizer == null) {
            JTextPane area = new JTextPane();
            SimpleAttributeSet sa = new SimpleAttributeSet();
            StyleConstants.setAlignment(sa, StyleConstants.ALIGN_JUSTIFIED);
            area.setText(this.element.getName());
            area.getStyledDocument().setParagraphAttributes(0, area.getText().split(" ").length, sa, false);
            area.setEditable(false);
            area.setEnabled(false);
            area.setBackground(Color.white);
            resizer = new Resizable(area);
            resizer.setBounds(rectangle.getBounds().x, rectangle.getBounds().y, rectangle.getBounds().width, rectangle.getBounds().height);
        }
        return resizer;
    }

    /**
	 * @param popup
	 *            The popup to set.
	 */
    public void setPopup(FeatureViewPopupMenu popup) {
        this.popup = popup;
    }

    protected int dx, dy;

    public void makeDifferenceFromCenter(int dx, int dy) {
        this.dx = x - dx;
        this.dy = y - dy;
    }

    public Point getDifferenceFromCenter(int dx, int dy) {
        return new Point(x - dx, y - dy);
    }

    public float getRasao() {
        return rasao;
    }

    public void createNodes(ObserverMutableTreeNode root, ITreeDisplayable obj) {
        ObserverMutableTreeNode re = new ObserverMutableTreeNode(this);
        root.add(re);
        Iterator it1 = ((FeatureElement) element).getRelations().iterator();
        while (it1.hasNext()) {
            ITreeDisplayable o = (ITreeDisplayable) it1.next();
            o.createNodes(re, null);
        }
    }

    /**
	 * @return Returns the dx.
	 */
    public int getDx() {
        return dx;
    }

    /**
	 * @return Returns the dy.
	 */
    public int getDy() {
        return dy;
    }

    public void setRectangle(Rectangle2D rectangle) {
        this.rectangle = rectangle;
    }

    private int difx, dify;

    public void mouseClicked(MouseEvent e) {
        if (rectangle.contains(e.getX(), e.getY())) {
            x = e.getX();
            y = e.getY();
            difx = center.x - x;
            dify = center.y - y;
            if (e.getClickCount() == 2) {
                Rectangle r = new Rectangle();
                r.setRect(nameLabelLocation.x - 5, nameLabelLocation.y - 15, nameWidth, 20);
                if (r.contains(x, y)) {
                    addFieldEditor();
                }
            }
            DefaultApplicationFrame.getInstance().getDefaultContentPane().setSelectedElement(element);
            DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController().notifyElementChanged();
        }
    }

    private boolean isEditing = false;

    public JTextField addFieldEditor() {
        isEditing = true;
        JTextField nameTField = new JTextField();
        nameTField.setLocation(nameLabelLocation.x - 5, (int) nameLabelLocation.y - 15);
        int w = (int) nameWidth;
        if (w < 30) w = 50;
        nameTField.setSize(w, 20);
        nameTField.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    String name = ((JTextField) evt.getSource()).getText();
                    ((FeatureElement) element).setName(name);
                    try {
                        autoResize();
                        setBorders();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                }
            }
        });
        nameTField.setText(((FeatureElement) element).getName());
        nameTField.setFocusable(true);
        nameTField.requestFocus();
        return nameTField;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    private int elipseRadius = 4;

    private java.awt.geom.Ellipse2D leftTop = new java.awt.geom.Ellipse2D.Double(), leftBotton = new java.awt.geom.Ellipse2D.Double(), rightTop = new java.awt.geom.Ellipse2D.Double(), rightBotton = new java.awt.geom.Ellipse2D.Double(), leftCenter = new java.awt.geom.Ellipse2D.Double(), rightCenter = new java.awt.geom.Ellipse2D.Double(), topCenter = new java.awt.geom.Ellipse2D.Double(), bottonCenter = new java.awt.geom.Ellipse2D.Double();

    public void resing(MouseEvent e) {
        if (leftCenter.contains(e.getX(), e.getY())) {
            Point p = getDifferenceFromPointer(e);
            if ((center.x - width) < e.getX()) {
                width = width - (Math.abs(e.getX() - x) - elipseRadius);
                getCenter().x = center.x - (p.x / 2);
            } else {
                width = width + (Math.abs(e.getX() - x) - elipseRadius);
                getCenter().x = center.x + (p.x / 2);
            }
            try {
                this.setBorders();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else if (rightCenter.contains(e.getX(), e.getY())) {
            Point p = getDifferenceFromPointer(e);
            if ((center.x - width) < e.getX()) {
                width = width + (Math.abs(e.getX() - x) - elipseRadius);
                getCenter().x = center.x + (p.x / 2);
            } else {
                width = width - (Math.abs(e.getX() - x) - elipseRadius);
                getCenter().x = center.x - (p.x / 2);
            }
            try {
                this.setBorders();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (rectangle.contains(e.getX(), e.getY()) && active) {
            getCenter().x = e.getX();
            getCenter().y = e.getY();
            try {
                setBorders();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (rectangle.contains(e.getX(), e.getY())) {
            x = e.getX();
            y = e.getY();
        }
    }

    private Point getDifferenceFromPointer(MouseEvent e) {
        int pointerX = e.getX();
        int pointerY = e.getY();
        Point p = new Point();
        p.x = (int) Math.abs(getCenter().getX() - pointerX) - difx;
        p.y = (int) Math.abs(getCenter().getY() - pointerY) - dify;
        return p;
    }

    public Point getCenter() {
        return center;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getNameWidth() {
        return nameWidth;
    }

    public void setNameWidth(float nameWidth) {
        this.nameWidth = nameWidth;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public Point getWidAndHei() {
        return new Point((int) width, (int) height);
    }

    public void setActiveElement(boolean b) {
        active = b;
    }

    private boolean active;

    public ObserverMutableTreeNode createNodes() {
        return null;
    }

    public Icon getIcon() {
        return null;
    }

    public String getName() {
        return element.getName();
    }

    public ObserverMutableTreeNode getNode() {
        return null;
    }

    public void setIcon(String name) {
    }

    public void setNode(ObserverMutableTreeNode node) {
    }

    public String toXMLBeanFactory() {
        return null;
    }

    public FeatureElement getFeature() {
        return ((FeatureElement) element);
    }

    public void setFeature(FeatureElement feature) {
        this.element = feature;
    }

    public void move(int mx, int my) {
        x = mx + difx;
        center.x = (x);
        y = my + dify;
        center.y = (y);
        try {
            setBorders();
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyComponentMotionListeners();
    }

    public void notifyComponentMotionListeners() {
        if (componentMotionListeners != null && componentMotionListeners.size() > 0) {
            Iterator<ComponentMotionListener> it = componentMotionListeners.iterator();
            while (it.hasNext()) {
                ComponentMotionListener c = it.next();
                c.componentMoved(x, y);
            }
        }
    }

    public HashSet<ComponentMotionListener> getComponentMotionListeners() {
        return componentMotionListeners;
    }

    public void setComponentMotionListeners(HashSet<ComponentMotionListener> componentMotionListeners) {
        this.componentMotionListeners = componentMotionListeners;
    }

    public void addComponentMotionListener(ComponentMotionListener component) {
        componentMotionListeners.add(component);
        component.addNotifyRemovableFeatureViewComponent(this);
    }

    public void removeComponentMotionListener(ComponentMotionListener component) {
        componentMotionListeners.remove(component);
    }

    public boolean isLinked() {
        return linked;
    }

    public boolean isLinkedTransformers() {
        return linkedTransformers != null && linkedTransformers.size() > 0;
    }

    public String getInnerXml(int tab) {
        if (linked || (linkedTransformers != null && linkedTransformers.size() > 0)) {
            String str = "";
            str += "\n";
            for (int i = 0; i < tab + 1; i++) {
                str += '\t';
            }
            str += "<LabelPosition x=\"" + xLabelPosition + "\" y=\"" + yLabelPosition + "\" >";
            str += "\n";
            for (int i = 0; i < tab + 1; i++) {
                str += '\t';
            }
            str += "<Link value=\"true\" >";
            Iterator<FeaturesDiagramPainterInternalFrame> it = linkedFeaturesDiagrams.iterator();
            while (it.hasNext()) {
                FeaturesDiagramPainterInternalFrame diagram = (FeaturesDiagramPainterInternalFrame) it.next();
                for (int i = 0; i < tab + 2; i++) {
                    str += '\t';
                }
                str += "<Diagram xmi.idref=\"" + diagram.getId() + "\"/>";
            }
            Iterator<TransformerDiagramPainterInternalFrame> it2 = linkedTransformers.iterator();
            while (it2.hasNext()) {
                TransformerDiagramPainterInternalFrame diagram = (TransformerDiagramPainterInternalFrame) it2.next();
                for (int i = 0; i < tab + 2; i++) {
                    str += '\t';
                }
                str += "<TransformerDiagram xmi.idref=\"" + diagram.getId() + "\"/>";
            }
            str += "\n";
            for (int i = 0; i < tab + 1; i++) {
                str += '\t';
            }
            str += "</Link>";
            return str;
        }
        return "";
    }

    public void importXMLForm(Node element) {
        super.importXMLForm(element);
        try {
            if (element.getAttributes().getNamedItem("center_width") != null) try {
                autoResize();
                width = Float.parseFloat(element.getAttributes().getNamedItem("center_width").getNodeValue());
                height = Float.parseFloat(element.getAttributes().getNamedItem("center_height").getNodeValue());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (element.getAttributes().getNamedItem("nameLabelLocation.x") != null) try {
                int x = Integer.parseInt(element.getAttributes().getNamedItem("nameLabelLocation.x").getNodeValue());
                int y = Integer.parseInt(element.getAttributes().getNamedItem("nameLabelLocation.y").getNodeValue());
                nameLabelLocation = new Point((int) x, (int) y);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (element.getAttributes().getNamedItem("xLabelPosition") != null) try {
                xLabelPosition = Integer.parseInt(element.getAttributes().getNamedItem("xLabelPosition").getNodeValue());
                yLabelPosition = Integer.parseInt(element.getAttributes().getNamedItem("yLabelPosition").getNodeValue());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            NodeList nl = element.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                if (n.getNodeName().equalsIgnoreCase("LabelPosition")) {
                    try {
                        String sx = n.getAttributes().getNamedItem("x").getNodeValue();
                        String sy = n.getAttributes().getNamedItem("y").getNodeValue();
                        xLabelPosition = Integer.parseInt(sx);
                        yLabelPosition = Integer.parseInt(sy);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (DOMException e) {
                        e.printStackTrace();
                    }
                }
                if (n.getNodeName().equalsIgnoreCase("Link")) {
                    linked = Boolean.parseBoolean(n.getAttributes().getNamedItem("value").getNodeValue());
                    NodeList nl2 = n.getChildNodes();
                    for (int j = 0; j < nl2.getLength(); j++) {
                        Node nodej = nl2.item(j);
                        if (nodej.getNodeName().equalsIgnoreCase("Diagram")) {
                            String idref = nodej.getAttributes().getNamedItem("xmi.idref").getNodeValue();
                            this.idLinkedFeaturesDiagrams.add(idref);
                        } else if (nodej.getNodeName().equalsIgnoreCase("TransformerDiagram")) {
                            String idref = nodej.getAttributes().getNamedItem("xmi.idref").getNodeValue();
                            this.idTransformers.add(idref);
                        }
                    }
                }
            }
        } catch (DOMException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterImport() {
        super.afterImport();
        try {
            if (idLinkedFeaturesDiagrams != null && idLinkedFeaturesDiagrams.size() > 0) {
                Iterator it = idLinkedFeaturesDiagrams.iterator();
                while (it.hasNext()) {
                    String idref = (String) it.next();
                    FeaturesDiagramPainterInternalFrame fd = (FeaturesDiagramPainterInternalFrame) AllElements.getInstance().getElements().get(idref);
                    if (fd != null) linkedFeaturesDiagrams.add(fd);
                }
            }
            if (idTransformers != null && idTransformers.size() > 0) {
                Iterator it = idTransformers.iterator();
                while (it.hasNext()) {
                    String idref = (String) it.next();
                    TransformerDiagramPainterInternalFrame fd = (TransformerDiagramPainterInternalFrame) AllElements.getInstance().getElements().get(idref);
                    if (fd != null) linkedTransformers.add(fd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
    }

    @Override
    public void paintSelected() {
    }

    public void addSingleRelation(FeatureRelation relation, FeatureElement feature) {
        try {
            FeatureViewComponent fc = null;
            try {
                fc = new FeatureViewComponent(feature);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            fc.move(getCenter().x, getCenter().y + 60);
            fc.setDrawingPanel(drawingPanel);
            fc.setDiagram(diagram);
            fc.setParentFeatureComponent(this);
            FeatureRelationViewComponent s = null;
            try {
                s = new FeatureRelationViewComponent(relation, FeatureViewComponent.this, fc);
                s.setDiagram(diagram);
                s.setDrawingPanel(drawingPanel);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            fc.addComponentMotionListener(s);
            FeatureViewComponent.this.addComponentMotionListener(s);
            relation.setParentFeature((FeatureElement) FeatureViewComponent.this.getFeature());
            diagram.getFeatureRelationViewComponents().add(s);
            diagram.getFeatureViewComponents().add(fc);
            if (relation instanceof OptionalFeatureRelation || relation instanceof MandatoryFeatureRelation) {
                ((FeaturesDiagramPaintPanel) drawingPanel).startEditing(fc);
                try {
                    fc.setWidth(0);
                    fc.autoResize();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ((FeatureElement) FeatureViewComponent.this.getFeature()).addElement(relation);
            ((FeatureElement) FeatureViewComponent.this.getFeature()).createNodes();
            s.componentMoved(fc.getCenter().x, fc.getCenter().y);
            ((FeaturesDiagramPaintPanel) drawingPanel).repaint();
            fc.setDiagram(diagram);
            fc.setDrawingPanel(drawingPanel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMultipleRelation(MultipleFeatureRelation relation) {
        try {
            FeatureElement opt1 = new FeatureElement(relation);
            opt1.setName(getMessages().getMessage("Feature") + " 1");
            relation.addChildFeature(opt1);
            opt1.setParentRelation(relation);
            FeatureElement opt2 = new FeatureElement(relation);
            opt2.setName(getMessages().getMessage("Feature") + " 2");
            relation.addChildFeature(opt2);
            opt2.setParentRelation(relation);
            FeatureViewComponent fc1 = null;
            try {
                fc1 = new FeatureViewComponent(opt1);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            fc1.move(getCenter().x - 30, getCenter().y + 60);
            fc1.setDrawingPanel(drawingPanel);
            fc1.setDiagram(diagram);
            fc1.setParentFeatureComponent(this);
            fc1.setDiagram(diagram);
            fc1.setDrawingPanel(drawingPanel);
            FeatureViewComponent fc2 = null;
            try {
                fc2 = new FeatureViewComponent(opt2);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            fc2.move(getCenter().x + 30, getCenter().y + 60);
            fc2.setDrawingPanel(drawingPanel);
            fc2.setDiagram(diagram);
            fc2.setParentFeatureComponent(this);
            fc2.setDiagram(diagram);
            fc2.setDrawingPanel(drawingPanel);
            FeatureRelationViewComponent s1 = null;
            try {
                HashSet<FeatureViewComponent> arr = new HashSet<FeatureViewComponent>();
                arr.add(fc1);
                arr.add(fc2);
                s1 = new FeatureRelationViewComponent(relation, FeatureViewComponent.this, arr);
                s1.setDiagram(diagram);
                s1.setDrawingPanel(drawingPanel);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            fc1.addComponentMotionListener(s1);
            fc2.addComponentMotionListener(s1);
            FeatureViewComponent.this.addComponentMotionListener(s1);
            relation.setParentFeature((FeatureElement) FeatureViewComponent.this.getFeature());
            diagram.getFeatureRelationViewComponents().add(s1);
            diagram.getFeatureViewComponents().add(fc1);
            diagram.getFeatureViewComponents().add(fc2);
            ((FeatureElement) FeatureViewComponent.this.getFeature()).addElement(relation);
            ((FeatureElement) FeatureViewComponent.this.getFeature()).createNodes();
            s1.componentMoved(getCenter().x, getCenter().y);
            ((FeaturesDiagramPaintPanel) drawingPanel).repaint();
            JTextField jtf = ((FeaturesDiagramPaintPanel) drawingPanel).startEditing(fc1);
            try {
                fc1.setWidth(0);
                fc1.autoResize();
            } catch (Exception e) {
            }
            jtf.addFocusListener(new RenameTextFocusListener(fc2));
            try {
                fc2.setWidth(0);
                fc2.autoResize();
            } catch (Exception e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class RenameTextFocusListener extends FocusAdapter {

        private FeatureViewComponent fvc;

        public RenameTextFocusListener(FeatureViewComponent fvc) {
            super();
            this.fvc = fvc;
        }

        public void focusLost(FocusEvent evt) {
            try {
                ((FeaturesDiagramPaintPanel) drawingPanel).startEditing(fvc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JPopupMenu getPopup() {
        popup = new FeatureViewPopupMenu(this);
        return popup;
    }

    public JPanel getDrawingPanel() {
        return drawingPanel;
    }

    public void setDrawingPanel(JPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    public FeaturesDiagramPainterInternalFrame getDiagram() {
        return diagram;
    }

    public void setDiagram(FeaturesDiagramPainterInternalFrame diagram) {
        this.diagram = diagram;
    }

    public HashSet<FeaturesDiagramPainterInternalFrame> getLinkedFeaturesDiagrams() {
        return linkedFeaturesDiagrams;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }

    @Override
    public void setElement(ElementImpl element) {
        super.setElement(element);
        if (element != null) try {
            this.setBorders();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String exportXMLForm(int tab) {
        String str = "";
        try {
            str += '\n';
            for (int i = 0; i < tab; i++) {
                str += '\t';
            }
            str += "<GraphicComponent xmi.id=\"" + getId() + "\" " + " width=\"" + getSize().width + "\" " + " height=\"" + getSize().height + "\" " + " x=\"" + center.x + "\" " + " y=\"" + center.y + "\" " + " center_width=\"" + width + "\" " + " center_height=\"" + height + "\" " + " nameLabelLocation.x=\"" + nameLabelLocation.x + "\" " + " xLabelPosition=\"" + xLabelPosition + "\" " + " nameLabelLocation.y=\"" + nameLabelLocation.y + "\" " + " yLabelPosition=\"" + yLabelPosition + "\" " + (element != null ? " elementId=\"" + element.getId() + "\" " : "") + " type=\"" + getClass().getName() + "\" ";
            if (!linked) str += "/>"; else {
                str += ">";
                str += "\n";
                for (int i = 0; i < tab + 1; i++) {
                    str += '\t';
                }
                str += "<Link value=\"true\" >";
                {
                    Iterator<FeaturesDiagramPainterInternalFrame> it = linkedFeaturesDiagrams.iterator();
                    while (it.hasNext()) {
                        FeaturesDiagramPainterInternalFrame diagram = (FeaturesDiagramPainterInternalFrame) it.next();
                        str += "\n";
                        for (int i = 0; i < tab + 2; i++) {
                            str += '\t';
                        }
                        str += "<Diagram xmi.idref=\"" + diagram.getId() + "\"/>";
                    }
                }
                {
                    Iterator<TransformerDiagramPainterInternalFrame> it2 = linkedTransformers.iterator();
                    while (it2.hasNext()) {
                        TransformerDiagramPainterInternalFrame diagram = (TransformerDiagramPainterInternalFrame) it2.next();
                        str += "\n";
                        for (int i = 0; i < tab + 2; i++) {
                            str += '\t';
                        }
                        str += "<TransformerDiagram xmi.idref=\"" + diagram.getId() + "\"/>";
                    }
                }
                str += "\n";
                for (int i = 0; i < tab + 1; i++) {
                    str += '\t';
                }
                str += "</Link>";
                str += '\n';
                for (int i = 0; i < tab; i++) {
                    str += '\t';
                }
                str += "</GraphicComponent>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public FeatureViewComponent getParentFeatureComponent() {
        return parentFeatureComponent;
    }

    public void setParentFeatureComponent(FeatureViewComponent parentFeatureComponent) {
        this.parentFeatureComponent = parentFeatureComponent;
    }

    public static void setMessages(org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile messages) {
        FeatureViewComponent.messages = messages;
    }

    public org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile getMessages() {
        return messages;
    }

    public HashSet<TransformerDiagramPainterInternalFrame> getLinkedTransformers() {
        return linkedTransformers;
    }

    public void setLinkedTransformers(HashSet<TransformerDiagramPainterInternalFrame> linkedTransformers) {
        this.linkedTransformers = linkedTransformers;
    }

    public void addTransformerDiagram(TransformerDiagramPainterInternalFrame diagram2) {
        if (linkedTransformers == null) linkedTransformers = new HashSet<TransformerDiagramPainterInternalFrame>();
        linkedTransformers.add(diagram2);
    }
}
