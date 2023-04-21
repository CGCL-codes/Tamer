package org.adapit.wctoolkit.fomda.features.view.transformation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.adapit.wctoolkit.fomda.diagram.fomdaextensions.TransformationCompositionDiagram;
import org.adapit.wctoolkit.fomda.features.util.Exportable;
import org.adapit.wctoolkit.fomda.features.view.Drawable;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.TransformationFeatureElement;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.Clause;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.KeyAndValue;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationDescriptor;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.Transformer;
import org.adapit.wctoolkit.utils.IdGenerator;

@SuppressWarnings({ "serial", "unchecked", "unused" })
public class TransformationDescriptorViewComponentOld extends TransformationDescriptor implements Drawable, Exportable, ActionListener, MouseListener {

    protected Point center;

    protected Point iconCenter = new Point(10, 10);

    protected int length;

    protected Polygon area;

    protected Polygon iconArea;

    protected Point p1, p2, p3, p4, p5, p6, p7;

    protected Frame frame;

    public int x, y;

    protected JPopupMenu popup;

    protected Rectangle2D rectangle;

    public static org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile messages = org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile.getInstance();

    /**
	 * @param observer
	 */
    public TransformationDescriptorViewComponentOld(TransformationFeatureElement observer) {
        super(observer);
        center = new Point(35, 35);
        area = new Polygon();
        iconArea = new Polygon();
        area.addPoint(10, 10);
        area.addPoint(60, 10);
        area.addPoint(50, 40);
        area.addPoint(60, 40);
        area.addPoint(35, 60);
        area.addPoint(10, 40);
        area.addPoint(20, 40);
        popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(messages.getMessage("Execute"));
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem(messages.getMessage("Delete"));
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem(messages.getMessage("Edit"));
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem(messages.getMessage("Make_it_a_new_transformation_descriptor"));
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem(messages.getMessage("Add_Transformation_Dependence"));
        menuItem.addActionListener(this);
        popup.add(menuItem);
        JMenu item = new JMenu(messages.getMessage("Add_Clausule"));
        menuItem = new JMenuItem(messages.getMessage("If_Clausule"));
        menuItem.addActionListener(this);
        item.add(menuItem);
        popup.add(item);
        popup.addMouseListener(this);
    }

    public TransformationDescriptorViewComponentOld() {
        super(null);
        center = new Point(35, 35);
        area = new Polygon();
        iconArea = new Polygon();
        area.addPoint(10, 10);
        area.addPoint(60, 10);
        area.addPoint(50, 40);
        area.addPoint(60, 40);
        area.addPoint(35, 60);
        area.addPoint(10, 40);
        area.addPoint(20, 40);
    }

    public void translate(int x, int y) {
        center.x = x;
        center.y = y;
    }

    public void moveIcon(int mx, int my) {
        iconCenter.x = mx;
        iconCenter.y = my;
    }

    public void draw(Graphics g, JFrame frame) {
        if (frame != null) this.frame = frame;
        Graphics2D g2 = (Graphics2D) g;
        String label = this.getName();
        length = label.length();
        area = new Polygon();
        area.addPoint(center.x - 25, center.y - 25);
        area.addPoint(center.x + 25, center.y - 25);
        area.addPoint(center.x + 16, center.y);
        area.addPoint(center.x + 25, center.y);
        area.addPoint(center.x, center.y + 20);
        area.addPoint(center.x - 25, center.y);
        area.addPoint(center.x - 16, center.y);
        try {
            Iterator it = (this.getParameters()).values().iterator();
            while (it.hasNext()) {
                Drawable t = (Drawable) it.next();
                t.draw(g, frame);
                g2.drawLine((int) center.getX(), (int) center.getY() - 26, (int) t.getCenter().getX(), (int) t.getCenter().getY() + 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Iterator it = (this.getSharedVariables()).values().iterator();
            while (it.hasNext()) {
                SharedVarViewComponent t = (SharedVarViewComponent) it.next();
                t.draw(g, frame);
                g2.drawLine((int) center.getX(), (int) center.getY() - 26, (int) t.getCenter().getX(), (int) t.getCenter().getY() + 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Iterator it = (this.getClauses()).iterator();
            while (it.hasNext()) {
                Drawable d = (Drawable) it.next();
                d.draw(g, frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (transformer.getTransformationDescriptor().hasKey(this.getName())) {
                Transformer t = ((KeyAndValue) ((LinkedHashMap) transformer.getTransformationDescriptor().getTransformationDependences()).get(getName())).getTransformer();
                TransformationDescriptorViewComponentOld tv = (TransformationDescriptorViewComponentOld) t.getTransformationDescriptor();
                Stroke oldStroke = g2.getStroke();
                float dash[] = { 3.0f };
                g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));
                g2.drawLine(this.getCenter().x, this.getCenter().y - 30, (int) tv.getCenter().getX(), (int) tv.getCenter().getY() + 45);
                g2.drawLine((int) tv.getCenter().getX() - 5, (int) tv.getCenter().getY() + 40, (int) tv.getCenter().getX(), (int) tv.getCenter().getY() + 30);
                g2.drawLine((int) tv.getCenter().getX() + 5, (int) tv.getCenter().getY() + 40, (int) tv.getCenter().getX(), (int) tv.getCenter().getY() + 30);
                g2.drawLine((int) tv.getCenter().getX(), (int) tv.getCenter().getY() + 45, (int) tv.getCenter().getX(), (int) tv.getCenter().getY() + 30);
                g2.setStroke(oldStroke);
            }
        } catch (Exception e) {
        }
        String label2 = this.getReturnType();
        if (label2 != null) {
            length = label2.trim().length();
            g2.drawString(label2, (int) (center.getX() - (3.4 * length)), (float) center.getY() + 30);
        }
        g2.setColor(new Color(253, 253, 253));
        g2.fillPolygon(area);
        g2.setColor(new Color(0, 0, 0));
        g2.drawPolygon(area);
        g2.drawString(label, (int) (center.getX() - (3.4 * length)), (float) center.getY() - 5);
    }

    public void drawIcon(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        String label = this.getName();
        length = label.trim().length();
        iconArea = new Polygon();
        iconArea.addPoint(iconCenter.x - 15, iconCenter.y - 15);
        iconArea.addPoint(iconCenter.x + 15, iconCenter.y - 15);
        iconArea.addPoint(iconCenter.x + 8, iconCenter.y);
        iconArea.addPoint(iconCenter.x + 15, iconCenter.y);
        iconArea.addPoint(iconCenter.x, iconCenter.y + 12);
        iconArea.addPoint(iconCenter.x - 15, iconCenter.y);
        iconArea.addPoint(iconCenter.x - 8, iconCenter.y);
        g2.setColor(new Color(253, 253, 253));
        g2.fillPolygon(iconArea);
        g2.setColor(new Color(0, 0, 0));
        g2.drawPolygon(iconArea);
    }

    public boolean isDraged() {
        return true;
    }

    public void move(int mx, int my) {
        translate(mx, my);
    }

    public boolean isInArea(int cordx, int cordy) {
        return area.contains(cordx, cordy);
    }

    public boolean isIconInArea(int cordx, int cordy) {
        return iconArea.contains(cordx, cordy);
    }

    public void paintSelect(Graphics g, JFrame frame) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(253, 10, 10));
        g2.drawPolygon(area);
        g2.setColor(new Color(0, 0, 0));
    }

    public void paintIconSelect(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(253, 10, 10));
        g2.drawPolygon(iconArea);
        g2.setColor(new Color(0, 0, 0));
    }

    public String toXMLBeanFactory() {
        String str = "";
        str += "<bean id=\"" + "paramValue" + this.id + "\" class=\"org.adapit.wctoolkit.fomda.uml.ext.fomda.metamodel.features.view.transformationdescriptor.TransformationDescriptorViewComponent\" singleton=\"true\">";
        str += '\n';
        str += '\t';
        if (!observer.isRootNode()) str += "<constructor-arg><ref bean=\"paramValue" + this.observer.getId() + "\"/></constructor-arg>"; else str += "<constructor-arg><ref bean=\"root\"/></constructor-arg>";
        str += '\n';
        str += '\t';
        str += "<property name=\"name\">";
        str += '\n';
        str += '\t';
        str += '\t';
        str += "<value>" + this.getName() + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"id\">";
        str += '\n';
        str += '\t';
        str += '\t';
        str += "<value>" + this.getId() + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"description\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String desc = "";
        if (this.getDescription() != null) desc += this.getDescription();
        str += "<value>" + desc + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        {
            Iterator enumer = (this.getParameters()).values().iterator();
            if (this.parameters.size() > 0) {
                str += '\n';
                str += '\t';
                str += "<property name=\"parameters\">";
                str += '\n';
                str += '\t';
                str += '\t';
                str += "<map>";
                str += '\n';
                str += '\t';
                str += '\t';
                while (enumer.hasNext()) {
                    str += "<entry>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    TransformationParameterViewComponent s = (TransformationParameterViewComponent) enumer.next();
                    str += "<key><value>" + s.getName() + "</value></key>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += "<ref bean=\"" + "paramValue" + s.getId() + "\"/>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += "</entry>";
                }
                str += '\n';
                str += '\t';
                str += '\t';
                str += '\t';
                str += "</map>";
                str += '\n';
                str += '\t';
                str += '\t';
                str += "</property>";
            }
        }
        str += '\n';
        str += '\t';
        {
            Iterator enumer = (this.clauses).iterator();
            if (this.clauses.size() > 0) {
                str += '\n';
                str += '\t';
                str += "<property name=\"clauses\">";
                str += '\n';
                str += '\t';
                str += '\t';
                str += "<list>";
                str += '\n';
                str += '\t';
                str += '\t';
                while (enumer.hasNext()) {
                    Clause c = (Clause) enumer.next();
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += "<ref bean=\"" + "paramValue" + c.getId() + "\"/>";
                }
                str += '\n';
                str += '\t';
                str += "</list>";
                str += '\n';
                str += '\t';
                str += "</property>";
            }
        }
        str += '\n';
        str += '\t';
        {
            Iterator enumer = (this.sharedVariables).values().iterator();
            if (this.sharedVariables.size() > 0) {
                str += '\n';
                str += '\t';
                str += "<property name=\"sharedVars\">";
                str += '\n';
                str += '\t';
                str += '\t';
                str += "<map>";
                str += '\n';
                str += '\t';
                str += '\t';
                while (enumer.hasNext()) {
                    str += "<entry>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    SharedVarViewComponent s = (SharedVarViewComponent) enumer.next();
                    str += "<key><value>" + s.getName() + "</value></key>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += "<ref bean=\"" + "paramValue" + s.getId() + "\"/>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += "</entry>";
                }
                str += '\n';
                str += '\t';
                str += '\t';
                str += '\t';
                str += "</map>";
                str += '\n';
                str += '\t';
                str += '\t';
                str += "</property>";
            }
        }
        str += '\n';
        str += '\t';
        str += "<property name=\"className\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String cn = this.getClassName();
        str += "<value>" + cn + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"returnType\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String rt = this.getReturnType();
        str += "<value>" + rt + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"expression\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String exp = this.getLanguage().name().toUpperCase();
        str += "<value>" + exp + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += "</bean>";
        {
            Iterator ite = this.sharedVariables.values().iterator();
            while (ite.hasNext()) {
                SharedVarViewComponent s = (SharedVarViewComponent) ite.next();
                str += s.toXMLBeanFactory();
            }
        }
        return str;
    }

    public Object execute() throws Exception {
        Object o = super.execute();
        Iterator it = destinations.iterator();
        while (it.hasNext()) {
        }
        return o;
    }

    public Object clone() {
        TransformationDescriptorViewComponentOld t = new TransformationDescriptorViewComponentOld(super.getObserver());
        t.setName(this.name);
        t.setReturnType(returnType);
        t.setDescription(this.description);
        t.setLanguage(this.language);
        t.setId(IdGenerator.getInstance().generateId() + "_" + IdGenerator.getInstance().generateId());
        t.setLanguageVersion(this.languageVersion);
        t.setClassName(className);
        {
            Iterator it1 = (this.getParameters()).values().iterator();
            while (it1.hasNext()) {
                TransformationParameterViewComponent tp = (TransformationParameterViewComponent) it1.next();
            }
        }
        t.setSrc(src);
        Transformer trans = null;
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        t.setTransformer(trans);
        t.setJarUrl(jarUrl);
        t.setVersion(version);
        t.setWriter(writer);
        t.center = (Point) center.clone();
        t.length = length;
        t.area = new Polygon();
        for (int i = 0; i < area.npoints; i++) {
            t.area.addPoint(area.xpoints[i], area.ypoints[i]);
        }
        return t;
    }

    /**
	 * @return Returns the area.
	 */
    public Polygon getArea() {
        return area;
    }

    /**
	 * @param area
	 *            The area to set.
	 */
    public void setArea(Polygon area) {
        this.area = area;
    }

    /**
	 * @return Returns the center.
	 */
    public Point getCenter() {
        return center;
    }

    /**
	 * @param center
	 *            The center to set.
	 */
    public void setCenter(Point center) {
        this.center = center;
    }

    /**
	 * @return Returns the length.
	 */
    public int getLength() {
        return length;
    }

    /**
	 * @param length
	 *            The length to set.
	 */
    public void setLength(int length) {
        this.length = length;
    }

    public void makeDifferenceFromCenter(int dx, int dy) {
    }

    /**
	 * @return Returns the popup.
	 */
    public JPopupMenu getPopup() {
        return popup;
    }

    /**
	 * @param popup
	 *            The popup to set.
	 */
    public void setPopup(JPopupMenu popup) {
        this.popup = popup;
    }

    protected TransformationCompositionDiagram transformationDescriptorObserver;

    public void setTransformationCompositionDiagram(TransformationCompositionDiagram td) {
        transformationDescriptorObserver = td;
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        if (source.getText().equalsIgnoreCase(messages.getMessage("Execute"))) {
            try {
                TransformationDescriptorViewComponentOld.this.execute();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else if (source.getText().equalsIgnoreCase(messages.getMessage("Delete"))) {
        } else if (source.getText().equalsIgnoreCase(messages.getMessage("Edit"))) {
        } else if (source.getText().equalsIgnoreCase(messages.getMessage("Make_it_a_new_transformation_descriptor"))) {
        } else if (source.getText().equalsIgnoreCase(messages.getMessage("Add_Transformation_Dependence"))) {
            this.getTDDobserver().setOperation(messages.getMessage("Add_Transformation_Dependence"));
        } else if (source.getText().equalsIgnoreCase(messages.getMessage("If_Clausule"))) {
            IfViewComponent ic = new IfViewComponent(this);
            ic.setObserver(this);
            getClauses().add(ic);
        }
    }

    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    /**
	 * @return Returns the tDDobserver.
	 */
    public TransformationCompositionDiagram getTDDobserver() {
        return transformationDescriptorObserver;
    }

    /**
	 * @return
	 */
    public String toRootXMLBeanFactory() {
        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        str += '\n';
        str += "<!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\" \"http://www.springframework.org/dtd/spring-beans.dtd\">";
        str += '\n';
        str += '\n';
        str += "<beans default-autowire=\"byName\" default-lazy-init=\"false\">";
        str += '\n';
        str += '\t';
        str += "<bean id=\"root\" class=\"" + this.getClassName() + "\" singleton=\"true\">";
        str += '\n';
        str += '\t';
        str += "<property name=\"name\">";
        str += '\n';
        str += '\t';
        str += '\t';
        str += "<value>" + this.getName() + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"id\">";
        str += '\n';
        str += '\t';
        str += '\t';
        str += "<value>" + this.getId() + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"description\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String desc = "";
        if (this.getDescription() != null) desc += this.getDescription();
        str += "<value>" + desc + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        {
            Iterator enumer = (this.getParameters()).values().iterator();
            if (this.parameters.size() > 0) {
                str += '\n';
                str += '\t';
                str += "<property name=\"parameters\">";
                str += '\n';
                str += '\t';
                str += '\t';
                str += "<map>";
                str += '\n';
                str += '\t';
                str += '\t';
                while (enumer.hasNext()) {
                    str += "<entry>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    TransformationParameterViewComponent s = (TransformationParameterViewComponent) enumer.next();
                    str += "<key><value>" + s.getName() + "</value></key>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += "<ref bean=\"" + "paramValue" + s.getId() + "\"/>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += "</entry>";
                }
                str += '\n';
                str += '\t';
                str += '\t';
                str += '\t';
                str += "</map>";
                str += '\n';
                str += '\t';
                str += '\t';
                str += "</property>";
            }
        }
        str += '\n';
        str += '\t';
        {
            Iterator enumer = (this.clauses).iterator();
            if (this.clauses.size() > 0) {
                str += '\n';
                str += '\t';
                str += "<property name=\"clauses\">";
                str += '\n';
                str += '\t';
                str += '\t';
                str += "<list>";
                str += '\n';
                str += '\t';
                str += '\t';
                while (enumer.hasNext()) {
                    Clause c = (Clause) enumer.next();
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += "<ref bean=\"" + "paramValue" + c.getId() + "\"/>";
                }
                str += '\n';
                str += '\t';
                str += "</list>";
                str += '\n';
                str += '\t';
                str += "</property>";
            }
        }
        str += '\n';
        str += '\t';
        {
            Iterator enumer = (this.sharedVariables).values().iterator();
            if (this.sharedVariables.size() > 0) {
                str += '\n';
                str += '\t';
                str += "<property name=\"sharedVars\">";
                str += '\n';
                str += '\t';
                str += '\t';
                str += "<map>";
                str += '\n';
                str += '\t';
                str += '\t';
                while (enumer.hasNext()) {
                    str += "<entry>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    SharedVarViewComponent s = (SharedVarViewComponent) enumer.next();
                    str += "<key><value>" + s.getName() + "</value></key>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += "<ref bean=\"" + "paramValue" + s.getId() + "\"/>";
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += '\t';
                    str += "</entry>";
                }
                str += '\n';
                str += '\t';
                str += '\t';
                str += '\t';
                str += "</map>";
                str += '\n';
                str += '\t';
                str += '\t';
                str += "</property>";
            }
        }
        str += '\n';
        str += '\t';
        str += "<property name=\"className\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String cn = this.getClassName();
        str += "<value>" + cn + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"returnType\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String rt = this.getReturnType();
        str += "<value>" + rt + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"expression\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String exp = this.getLanguage().name().toUpperCase();
        str += "<value>" + exp + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += "</bean>";
        {
            Iterator ite = this.sharedVariables.values().iterator();
            while (ite.hasNext()) {
                SharedVarViewComponent s = (SharedVarViewComponent) ite.next();
                str += '\n';
                str += '\n';
                str += '\t';
                str += '\n';
            }
        }
        {
            Iterator ite = this.parameters.values().iterator();
            while (ite.hasNext()) {
                TransformationParameterViewComponent s = (TransformationParameterViewComponent) ite.next();
                str += '\n';
                str += '\n';
                str += '\t';
                str += s.toRootXMLBeanFactory();
                str += '\n';
            }
        }
        {
            Iterator ite = this.clauses.iterator();
            while (ite.hasNext()) {
                IfViewComponent s = (IfViewComponent) ite.next();
                str += '\n';
                str += '\n';
                str += '\t';
                str += s.toXMLBeanFactory();
                str += '\n';
            }
        }
        str += '\n';
        str += "</beans>";
        return str;
    }

    protected void reportStatus(Object obj) {
        if (obj instanceof java.lang.String) {
            JOptionPane.showMessageDialog(transformationDescriptorObserver, messages.getMessage("Success_on_the_transformations"), messages.getMessage("Transformation"), JOptionPane.INFORMATION_MESSAGE);
            Iterator it = TransformationDescriptorViewComponentOld.this.getDestinations().iterator();
            while (it.hasNext()) {
            }
        } else if (obj instanceof Exception) {
            JOptionPane.showMessageDialog(transformationDescriptorObserver, messages.getMessage("Something_wrong_on_the_transformations") + ((Exception) obj).getMessage(), messages.getMessage("Error_in_executing_transformation"), JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setTransformationDescriptorObserver(TransformationCompositionDiagram dobserver) {
        transformationDescriptorObserver = dobserver;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public Polygon getIconArea() {
        return iconArea;
    }

    public void setIconArea(Polygon iconArea) {
        this.iconArea = iconArea;
    }

    public Point getIconCenter() {
        return iconCenter;
    }

    public void setIconCenter(Point iconCenter) {
        this.iconCenter = iconCenter;
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public Point getP3() {
        return p3;
    }

    public void setP3(Point p3) {
        this.p3 = p3;
    }

    public Point getP4() {
        return p4;
    }

    public void setP4(Point p4) {
        this.p4 = p4;
    }

    public Point getP5() {
        return p5;
    }

    public void setP5(Point p5) {
        this.p5 = p5;
    }

    public Point getP6() {
        return p6;
    }

    public void setP6(Point p6) {
        this.p6 = p6;
    }

    public Point getP7() {
        return p7;
    }

    public void setP7(Point p7) {
        this.p7 = p7;
    }

    public Rectangle2D getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle2D rectangle) {
        this.rectangle = rectangle;
    }
}
