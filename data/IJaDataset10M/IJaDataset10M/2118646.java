package view.acims.diagrams;

import javax.swing.*;
import view.acims.Graphics.*;
import view.acims.Math.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Diagram is the main component of the Diagrams Framework. It 
 * is useful in the rapid development of interactive 
 * two dimensional graphical displays.
 * A diagram contains a name, zero to many 
 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a>, 
 * and optional 
 * <a href="{@docRoot}/com/acims/Diagrams/Diagram.Relationships.html">
 * Relationships</a> . 
 * <p>
 * The specific specializations, interactions, and graphical display 
 * characteristics of the 
 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelations</a> 
 * and 
 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a>  
 * of a Diagram are defined by the implementation of the 
 * <a href="{@docRoot}/com/acims/Diagrams/DiagramAttributeSet.html">
 * DiagramAttributeSet</a> abstract class. 
 * <p>
 * See 
 * <a href="../../../Diagrams Tutorial.html">Diagram Tutorial</a>
 * 
 * @see com.acims.diagrams.DComponent
 * @see com.acims.diagrams.DRelation
 * @see com.acims.diagrams.DiagramAttributeSet
 * @author Robert Flasher
 *
 */
public class Diagram extends JLabel {

    /**
	 * The Relationship represents the connection of a 
	 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a> 
	 * to a 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a> 
	 * 
	 * @see com.acims.diagrams.Diagram
	 * @see com.acims.diagrams.DComponent
	 * @see com.acims.diagrams.DRelation
	 * @author Robert Flasher
	 * @version 1.1
	 */
    public class Relationship {

        private DRelation relation;

        private String endDComponent = "";

        private String startDComponent = "";

        /**
		 * Initializes a Relationship object with the input 
		 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a> 
		 * between the  
		 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a> 
		 * named start and end.
		 * <p>
		 * @param line <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a> 
		 * that connects the 
		 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a>. 
		 * @param start Name of the 
		 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a> 
		 * where the 
		 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a> 
		 * begins. 
		 * @param end Name of the 
		 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a> 
		 * where the 
		 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a> 
		 * ends. 
		 */
        protected Relationship(DRelation line, String start, String end) {
            startDComponent = start;
            endDComponent = end;
            relation = line;
        }

        /**
		 * Returns the 
		 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a> 
		 * of the Connection.
		 * <p>
		 * @return The 
		 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a>. 
		 */
        public DRelation getRelationship() {
            return relation;
        }

        /**
		 * Returns the name of the 
		 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a> 
		 * connected at the ends of the Relationship's 
		 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a>. 
		 * <p>
		 * @return Name of the end 
		 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a>. 
		 */
        public String getEndDComponent() {
            return endDComponent;
        }

        /**
		 * Returns the name of the 
		 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a> 
		 * connected at the  start of the Relationship's 
		 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a>. 
		 * <p>
		 * @return Name of the beginning 
		 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a>. 
		 */
        public String getStartDComponent() {
            return startDComponent;
        }

        /**
		 * Sets the name of the 
		 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a> 
		 * at the end of the Relationship's 
		 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a>. 
		 * <p>
		 */
        public void setEndDComponent(String lbl) {
            endDComponent = lbl;
        }

        /**
		 * Returns the name of the 
		 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a> 
		 * at the  start of the Relationship's 
		 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a>. 
		 * <p>
		 * */
        public void setStartDComponent(String lbl) {
            startDComponent = lbl;
        }
    }

    private DiagramAttributeSet atSet;

    private Color backgroundColor = Color.WHITE;

    private int charWidth = 6;

    private Vector relationships = new Vector(10, 1);

    private Font diagramFont = new Font("Arial", Font.BOLD, 12);

    private String diagramName = "";

    private Graphics2D g;

    private BufferedImage img;

    private DComponent bg = null;

    private int imgHt = 50;

    private int imgLen = 10;

    private int nameLen = 5;

    private Point namePos = new Point(5, 30);

    private int numRel = 0;

    private int numDComp = 0;

    private Vector dComponents = new Vector(10, 1);

    private Color textColor = Color.BLACK;

    private int xBuffer = 50;

    private int yBuffer = 50;

    /**
	 * Initialzes a Diagram with its name
	 * in the upper left corner, a white background, and black text.
	 * <p>
	 * @param name Name of the diagram to be displayed
	 * @param das Instance of a specialization of the 
	 * DiagramAttributeSet class
	 */
    public Diagram(String name, DiagramAttributeSet das) {
        setDiagramName(name);
        atSet = das;
        setHorizontalAlignment(JLabel.LEFT);
        setVerticalAlignment(JLabel.TOP);
        drawDiagram();
    }

    /**
	 * Initialzes a Diagram with its name
	 * in the upper left corner, a bgColor background, and black text.
	 * <p>
	 * @param name Name of the diagram to be displayed
	 * @param das Instance of a specialization of the 
	 * DiagramAttributeSet class
	 * @param bgColor Background color of the Diagram
	 */
    public Diagram(String name, DiagramAttributeSet das, Color bgColor) {
        setDiagramName(name);
        atSet = das;
        setHorizontalAlignment(JLabel.LEFT);
        setVerticalAlignment(JLabel.TOP);
        backgroundColor = bgColor;
        drawDiagram();
    }

    /**
	 * Initialzes a Diagram with its name
	 * in the upper left corner, a bgColor background, and txColor text.
	 * <p>
	 * @param name Name of the diagram to be displayed
	 * @param das Instance of a specialization of the 
	 * DiagramAttributeSet class
	 * @param bgColor Background color of the Diagram
	 * @param txColor Text color of the displayed Diagram name
	 */
    public Diagram(String name, DiagramAttributeSet das, Color bgColor, Color txColor) {
        setDiagramName(name);
        atSet = das;
        setHorizontalAlignment(JLabel.LEFT);
        setVerticalAlignment(JLabel.TOP);
        backgroundColor = bgColor;
        textColor = txColor;
        drawDiagram();
    }

    /**
	 * Add a 
	 * <a href="{@docRoot}/com/acims/Diagrams/Diagram.Relationship.html">Relationship</a> 
	 * with a 
	 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a> 
	 * named lbl from 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a> 
	 * named stShp to 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a> 
	 * named eshp.
	 * <p>
	 * @param lbl Name of the  
	 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html"> DRelation</a>
	 * @param stShp Name of the start 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a>.
	 * @param eShp Name of the end 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a>
	 * @return True if the 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a> 
	 * stShp and eShp exist, false otherwise
	 */
    public boolean addRelationship(String lbl, String stShp, String eShp) {
        if ((getDComponent(stShp) != null) && (getDComponent(eShp) != null)) {
            DRelation ln = atSet.getDRelationContext(lbl);
            Relationship c = new Relationship(ln, stShp, eShp);
            atSet.setDRelation(c, relationships, dComponents, "add");
            relationships.add(c);
            ++numRel;
            return true;
        }
        return false;
    }

    /**
	 * Adds a 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a> 
	 * with the unique name lbl with its upper left corner at position 
	 * pos.
	 * <p>
	 * @param lbl Name of the new 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a>
	 * @param pos <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/awt/Point.html">Point</a> 
	 * indicating its upper left corner position.
	 * @return False if the name lbl is not unique, true otherwise
	 */
    public boolean addDComponent(String lbl, Point pos) {
        if (getDComponent(lbl) == null) {
            DComponent sh = atSet.getDComponentContext(lbl);
            atSet.setDComponent(sh, lbl, pos);
            dComponents.add(sh);
            ++numDComp;
            return true;
        }
        return false;
    }

    public void setBackgroundComponent(String typ, String name) {
        atSet.setComponentType(typ);
        bg = atSet.getDComponentContext(name);
        atSet.setDComponent(bg, name, new Point(0, 0));
    }

    /**
	 * Removes all 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a> 
	 * and 
	 * <a href="{@docRoot}/com/acims/Diagrams/Diagram.Relationship.html">Relationships</a> 
	 * from the Diagram.
	 */
    public void clearDiagram() {
        dComponents = new Vector();
        numDComp = 0;
        relationships = new Vector();
        numRel = 0;
        imgLen = 10;
        imgHt = 50;
        nameLen = 5;
        setDiagramName(diagramName);
        drawDiagram();
    }

    private void drawRelationships(String XLabel) {
        for (int i = 0; i < relationships.size(); ++i) {
            Relationship conn;
            DComponent st = getDComponent((conn = (Relationship) (relationships.get(i))).getStartDComponent());
            DComponent ed = getDComponent(conn.getEndDComponent());
            DRelation line = conn.getRelationship();
            if (st.getLabel().equals(ed.getLabel())) {
                line.drawDRelation(Math2D.addVectors(st.getPosition(), st.getCenter()), Math2D.addVectors(ed.getPosition(), ed.getCenter()), g, XLabel);
            } else {
                Point startP = Math2D.addVectors(st.getCenter(), st.getPosition());
                line.drawDRelation(startP, ed.getClosestPointOnDComp(startP), g, XLabel);
            }
        }
    }

    private void drawRelationships() {
        for (int i = 0; i < relationships.size(); ++i) {
            Relationship conn;
            DComponent st = getDComponent((conn = (Relationship) (relationships.get(i))).getStartDComponent());
            DComponent ed = getDComponent(conn.getEndDComponent());
            DRelation line = conn.getRelationship();
            if (st.getLabel().equals(ed.getLabel())) {
                line.drawDRelation(Math2D.addVectors(st.getPosition(), st.getCenter()), Math2D.addVectors(ed.getPosition(), ed.getCenter()), g, "");
            } else {
                Point startP = Math2D.addVectors(st.getCenter(), st.getPosition());
                line.drawDRelation(startP, ed.getClosestPointOnDComp(startP), g, "");
            }
        }
    }

    /**
	 * Causes the diagram to be displayed.
	 */
    public void drawDiagram(String XLabel) {
        getCanvasDimensions();
        img = new BufferedImage(imgLen + xBuffer, imgHt + yBuffer, BufferedImage.TYPE_INT_RGB);
        g = img.createGraphics();
        g.setStroke(new BasicStroke(1.1f));
        g.setColor(backgroundColor);
        if (bg == null) {
            g.fillRect(0, 0, imgLen + xBuffer, imgHt + yBuffer);
        } else {
            bg.setDimensions(new Point(imgLen + xBuffer - 1, imgHt + yBuffer - 1));
            bg.drawDComponent(g);
        }
        g.setColor(textColor);
        g.drawRect(0, 0, imgLen + xBuffer - 1, imgHt + yBuffer - 1);
        g.setFont(diagramFont);
        g.drawString(diagramName, (int) namePos.getX(), (int) namePos.getY());
        drawRelationships(XLabel);
        drawDComponents();
        setIcon(new ImageIcon(img));
    }

    public void drawDiagram() {
        getCanvasDimensions();
        img = new BufferedImage(imgLen + xBuffer, imgHt + yBuffer, BufferedImage.TYPE_INT_RGB);
        g = img.createGraphics();
        g.setStroke(new BasicStroke(1.1f));
        g.setColor(backgroundColor);
        if (bg == null) {
            g.fillRect(0, 0, imgLen + xBuffer, imgHt + yBuffer);
        } else {
            bg.setDimensions(new Point(imgLen + xBuffer - 1, imgHt + yBuffer - 1));
            bg.drawDComponent(g);
        }
        g.setColor(textColor);
        g.drawRect(0, 0, imgLen + xBuffer - 1, imgHt + yBuffer - 1);
        g.setFont(diagramFont);
        g.drawString(diagramName, (int) namePos.getX(), (int) namePos.getY());
        drawRelationships();
        drawDComponents();
        setIcon(new ImageIcon(img));
    }

    private void drawDComponents() {
        for (int i = 0; i < numDComp; ++i) {
            ((DComponent) (dComponents.get(i))).drawDComponent(g);
        }
    }

    public Graphics2D getGraphic() {
        return g;
    }

    ;

    private void getCanvasDimensions() {
        for (int i = 0; i < numDComp; ++i) {
            DComponent sh = (DComponent) dComponents.get(i);
            if ((sh.getPosition().getX() + sh.getDimensions().getX()) > imgLen) {
                imgLen = (int) (sh.getPosition().getX() + sh.getDimensions().getX());
            }
            if ((sh.getPosition().getY() + sh.getDimensions().getY()) > imgHt) {
                imgHt = (int) (sh.getPosition().getY() + sh.getDimensions().getY());
            }
        }
    }

    /**
	 * Returns the 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a> 
	 * or 
	 * <a href="{@docRoot}/com/acims/Diagrams/Diagram.Relationship.html">Relationship</a> 
	 * located at 
	 * <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/awt/Point.html">Point</a> 
	 * p.
	 * @param p <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/awt/Point.html">Point</a> 
	 * location of interest.
	 * @return Array of two objects. Index 0 contains a reference to the 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a> 
	 * or the 
	 * <a href="{@docRoot}/com/acims/Diagrams/DRelation.html">DRelation</a> 
	 * at the location p. Index 1 contains its description. 
	 * If white space exists at position p null is returned.
	 */
    public Object[] getComponent(Point p) {
        Object[] ret = null;
        DComponent sh;
        for (int i = 0; i < numDComp; ++i) {
            sh = (DComponent) dComponents.get(i);
            if (sh.isOnDComp(p)) {
                ret = new Object[2];
                ret[0] = sh;
                ret[1] = "Component";
            }
        }
        if (ret != null) {
            return ret;
        }
        DRelation ln;
        for (int i = 0; i < numRel; ++i) {
            ln = ((Relationship) relationships.get(i)).getRelationship();
            if (ln.isOnDRelation(p)) {
                ret = new Object[2];
                ret[0] = (Relationship) relationships.get(i);
                ret[1] = "Relation";
            }
        }
        return ret;
    }

    /**
	 * Return the DComponent named text.
	 * @param text DComponent name
	 * @return The DComponent named text.
	 */
    public DComponent getDComponent(String text) {
        DComponent shp;
        for (int i = 0; i < numDComp; ++i) {
            if ((shp = ((DComponent) (dComponents.get(i)))).getLabel().equals(text)) {
                return shp;
            }
        }
        return null;
    }

    /**
	 * This returns a vector of all Relationships with a 
	 * start DComponent st and the end DComponent ed.
	 * @param st Name of start DComponent
	 * @param ed Name of end DComponent
	 * @return A Vector with all Relationships from st to ed.
	 */
    public Vector getRelationships(String st, String ed) {
        Vector results = new Vector(10, 1);
        Relationship rel;
        for (int i = 0; i < relationships.size(); ++i) {
            rel = (Relationship) (relationships.get(i));
            if (rel.getStartDComponent().equals(st)) {
                if (rel.getEndDComponent().equals(ed)) {
                    results.add(rel);
                }
            }
        }
        return results;
    }

    /**
	 * This returns a vector of all Relationships that
	 * contain the DComponent name dComp or a DRelation
	 * with the type dComp.
	 * @param dComp Name of search DComponent or type of DRelation
	 * @param type true if dComp and false if dComp
	 * @return All relationships containing the DComponent
	 * named dComp.
	 */
    public Vector getRelationships(String dComp, boolean type) {
        Vector results = new Vector(10, 1);
        Relationship rel;
        for (int i = 0; (i < relationships.size()) && !type; ++i) {
            rel = (Relationship) (relationships.get(i));
            if (rel.getStartDComponent().equals(dComp) || rel.getEndDComponent().equals(dComp)) {
                results.add(rel);
            }
        }
        for (int i = 0; (i < relationships.size()) && type; ++i) {
            rel = (Relationship) (relationships.get(i));
            if (((DRelation) rel.getRelationship()).getType().equals(dComp)) {
                results.add(rel);
            }
        }
        return results;
    }

    /**
	 * Relocates the 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a>  
	 * named label by 
	 * <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/awt/Point.html">Point</a> p.
	 * <p>
	 * p indicates the new position of the upper left corner of lbl. 
	 * <p>
	 * @param lbl <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a> 
	 * to be relocated.
	 * @param p <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/awt/Point.html">Point</a> 
	 * where the 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a> 
	 * is to be relocated. 
	 */
    public void moveDComponent(String lbl, Point p, String XLabel) {
        DComponent sh = getDComponent(lbl);
        atSet.relocateDComponent(sh, Math2D.addVectors(p, sh.getPosition()));
        drawDiagram();
    }

    /**
	 * Removes the 
	 * <a href="{@docRoot}/com/acims/Diagrams/Diagram.Relationship.html">Relationship</a> 
	 * c from the Diagram.
	 * <p>
	 * @param c <a href="{@docRoot}/com/acims/Diagrams/Diagram.Relationship.html">Relationship</a> 
	 * to be removed from the Diagram
	 */
    public void removeRelationship(Relationship c, String XLabel) {
        for (int i = 0; i < numRel; ++i) {
            if (((Relationship) relationships.get(i)).equals(c)) {
                relationships.remove(i);
                --numRel;
                drawDiagram();
                break;
            }
        }
    }

    /**
	 * Removes the 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a>  
	 * shp from the Diagram.
	 * <p>
	 * It, also, removes all 
	 * <a href="{@docRoot}/com/acims/Diagrams/Diagram.Relationship.html">Relationships</a> 
	 * that include the 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a>. 
	 * <p>
	 * @param shp <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a> 
	 * to be removed from the Diagram.
	 */
    public void removeDComponent(DComponent shp, String XLabel) {
        Relationship c;
        for (int i = 0; i < numRel; ++i) {
            c = (Relationship) relationships.get(i);
            if ((c.getStartDComponent().equals(shp.getLabel())) || (c.getEndDComponent().equals(shp.getLabel()))) {
                atSet.setDRelation((Relationship) relationships.get(i), relationships, dComponents, "remove");
                relationships.remove(i);
                --numRel;
                --i;
            }
        }
        for (int i = 0; i < numDComp; ++i) {
            if (((DComponent) dComponents.get(i)).equals(shp)) {
                dComponents.remove(i);
                --numDComp;
                break;
            }
        }
        drawDiagram();
    }

    /**
	 * Renames the 
	 * <a href="{@docRoot}/com/acims/Diagrams/Diagram.Relationship.html">Relationship</a> 
	 * c to lbl.
	 * <p>
	 * @param c <a href="{@docRoot}/com/acims/Diagrams/Diagram.Relationship.html">Relationship</a> 
	 * to be renamed.
	 * @param lbl New name of the 
	 * <a href="{@docRoot}/com/acims/Diagrams/Diagram.Relationship.html">Relationship</a>.
	 */
    public void renameRelationship(Relationship c, String lbl, String XLabel) {
        c.getRelationship().setLabel(lbl);
        drawDiagram();
    }

    /**
	 * Renames the 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a> 
	 * sh to lbl.
	 * <p>
	 * It, also, updates the name within the 
	 * <a href="{@docRoot}/com/acims/Diagrams/Diagram.Relationship.html">Relationships</a> 
	 * that include the  
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a>.
	 * <p>
	 * @param sh <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a> 
	 * to be renamed.
	 * @param lbl New name of the 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponent</a>.
	 * @return True if lbl is a unique 
	 * <a href="{@docRoot}/com/acims/Diagrams/DComponent.html">DComponents</a> 
	 * name, otherwise false.
	 */
    public boolean renameDComponent(DComponent sh, String lbl, String XLabel) {
        String old = sh.getLabel();
        Relationship c;
        if (getDComponent(lbl) == null) {
            atSet.setDComponent(sh, lbl, sh.getPosition());
            for (int i = 0; i < numRel; ++i) {
                c = (Relationship) relationships.get(i);
                if (c.getStartDComponent().equals(old)) {
                    c.setStartDComponent(lbl);
                }
                if (c.getEndDComponent().equals(old)) {
                    c.setEndDComponent(lbl);
                }
            }
            drawDiagram();
            return true;
        }
        return false;
    }

    /**
	 * Resets the diagrams name to the String name.
	 * @param name The new name of the diagram.
	 */
    public void resetDiagramName(String name) {
        if (!diagramName.equals("")) {
            nameLen -= (diagramName.length() * charWidth);
        }
        setDiagramName(name);
    }

    private void setDiagramName(String name) {
        diagramName = name;
        if (!diagramName.equals("")) {
            nameLen += (diagramName.length() * charWidth);
        }
        imgLen = nameLen;
    }
}
