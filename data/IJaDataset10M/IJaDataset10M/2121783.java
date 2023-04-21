package fca.gui.lattice.element;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Vector;
import fca.core.rule.Rule;
import fca.core.util.BasicSet;
import fca.gui.util.constant.LMOptions;
import fca.messages.GUIMessages;

/**
 * D�finition d'une �tiquette pour un noeud d'un treillis
 * 
 * @author Genevi�ve Roberge
 * @author Yoann Montouchet
 * @version 1.1
 */
public class ConceptLabel {

    protected boolean showObjects;

    protected boolean showAttributes;

    protected boolean showRules;

    protected String objects;

    protected String attributes;

    protected Vector<String> rules;

    protected int objAmountType;

    protected int attAmountType;

    protected int rulesType;

    protected boolean hideOutOfFocusLabel;

    protected boolean isVisible;

    protected boolean isHighlighted;

    protected Rectangle2D shape;

    protected Point2D distanceFromNode;

    protected Point2D closestPointFromNode;

    protected Point2D closestNodePoint;

    /**
	 * inverse="label:fca.lattice.graphical.GraphicalConcept"
	 */
    protected GraphicalConcept parentConcept;

    protected BasicSet intent;

    protected BasicSet extent;

    protected BasicSet reducedIntent;

    protected BasicSet reducedExtent;

    protected int nbExtentObjects;

    protected int nbTotalObjects;

    protected int nbExtNodeObjects;

    protected int fontSize = 12;

    protected Vector<BasicSet> generators;

    protected int geneAmountType;

    protected String gene;

    protected boolean showGene;

    /**
	 * Constructeur
	 * 
	 * @param gc
	 *            Le GraphicalConcept pour lequel est cr�� cette �tiquette
	 * @param nt
	 *            Le int contenant le nombre d'objets du contexte global
	 * @param ne
	 *            Le int contenant le nombre d'objets du noeud externe au noeud
	 *            d'appartenance
	 */
    public ConceptLabel(GraphicalConcept gc, int nt, int ne) {
        parentConcept = gc;
        intent = parentConcept.getNestedConcept().getIntent();
        extent = parentConcept.getNestedConcept().getExtent();
        reducedIntent = parentConcept.getNestedConcept().getReducedIntent();
        reducedExtent = parentConcept.getNestedConcept().getReducedExtent();
        generators = parentConcept.getConcept().getGenerators();
        nbTotalObjects = nt;
        nbExtNodeObjects = ne;
        nbExtentObjects = extent.size();
        objAmountType = LMOptions.OBJECTS_PERC_NODE;
        attAmountType = LMOptions.ATTRIBUTES_REDUCED;
        rulesType = LMOptions.NO_LABEL;
        hideOutOfFocusLabel = false;
        isVisible = true;
        isHighlighted = false;
        showAttributes = true;
        showObjects = true;
        showRules = false;
        showGene = false;
        double perc;
        if (nbExtNodeObjects == 0) perc = 0; else perc = (Math.round(((double) nbExtentObjects / (double) nbExtNodeObjects) * 10000.0)) / 100.0;
        objects = " " + perc + "% ";
        attributes = setToSimpleString(reducedIntent);
        rules = new Vector<String>();
        gene = "";
        distanceFromNode = null;
        closestPointFromNode = null;
        closestNodePoint = null;
        shape = new Rectangle2D.Double(0, 0, 1, 1);
    }

    /**
	 * Construit une chaine simple � partir d'un ensemble, en les listant un �
	 * la suite de l'autre sans s�parateur, ni d�limiteurs de d�but et fin
	 * d'ensemble
	 * 
	 * @param set
	 *            Le SortedSet contenant l'ensemble pour lequel la chaine doit
	 *            �tre construite
	 * @return Une String contenant la chaine de caract�res cr�e
	 */
    protected String setToSimpleString(SortedSet<String> set) {
        String simpleString = " ";
        Iterator<String> it = set.iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Object currentElement = it.next();
                String currentString = currentElement.toString();
                if (currentString.contains(" ")) currentString = "'" + currentString + "'";
                simpleString = simpleString + currentString + " ";
            }
        } else simpleString = " {} ";
        return simpleString;
    }

    /**
	 * Construit un vecteur contenant les chaine des r�gles approximatives, �
	 * partir d'un ensemble de r�gles, en les listant une sous l'autre, avec un
	 * indicateur de confiance et un indicateur de support.
	 * 
	 * @param ruleSet
	 *            Le RuleSet contenant l'ensemble de r�gles pour lesquelles la
	 *            chaine doit �tre construite
	 * @return Un Vector contenant les chaines de caract�res cr�es
	 */
    protected Vector<String> setToApproxStrings(Vector<Rule> ruleSet) {
        Vector<String> approxStrings = new Vector<String>();
        if (ruleSet == null || ruleSet.size() == 0) {
            approxStrings.add(GUIMessages.getString("GUI.noRule"));
        } else {
            for (int i = 0; i < ruleSet.size(); i++) {
                Rule rule = ruleSet.elementAt(i);
                String ruleStr = " ";
                Iterator<String> it = rule.getAntecedent().iterator();
                while (it.hasNext()) ruleStr += (it.next()).toString() + " ";
                ruleStr += "=> ";
                it = rule.getConsequence().iterator();
                while (it.hasNext()) {
                    ruleStr += (it.next()).toString() + " ";
                }
                ruleStr += "(" + Double.toString(((double) ((int) (rule.getSupport() * 100.0))) / 100.0) + ", ";
                ruleStr += Double.toString(((double) ((int) (rule.getConfidence() * 100.0))) / 100.0) + ") ";
                approxStrings.add(ruleStr);
            }
        }
        return approxStrings;
    }

    /**
	 * Ajuste le type d'information affich�e en ce qui concerne les objets
	 * 
	 * @param t
	 *            Le int contenant le type d'information � afficher (NONE,
	 *            LIST_OBJ, REDUCED_OBJ, NBR_OBJ, PERC_CTX_OBJ, PERC_NODE_OBJ)
	 */
    public void setObjLabelType(int t) {
        if (t >= 0 && t <= 5) {
            this.objAmountType = t;
            showObjects = true;
            if (objAmountType == LMOptions.NO_LABEL) showObjects = false; else if (objAmountType == LMOptions.OBJECTS_ALL) objects = setToSimpleString(extent); else if (objAmountType == LMOptions.OBJECTS_REDUCED) objects = setToSimpleString(reducedExtent); else if (objAmountType == LMOptions.OBJECTS_NUMBER) objects = " " + extent.size() + " "; else if (objAmountType == LMOptions.OBJECTS_PERC_CTX) {
                double perc;
                if (nbTotalObjects == 0) perc = 0; else perc = (Math.round(((double) nbExtentObjects / (double) nbTotalObjects) * 10000.0)) / 100.0;
                objects = " " + perc + "% ";
            } else if (objAmountType == LMOptions.OBJECTS_PERC_NODE) {
                double perc;
                if (nbExtNodeObjects == 0) perc = 0; else perc = (Math.round(((double) nbExtentObjects / (double) nbExtNodeObjects) * 10000.0)) / 100.0;
                objects = " " + perc + "% ";
            }
        }
    }

    /**
	 * Ajuste le type d'information affich�e en ce qui concerne les attributs
	 * 
	 * @param t
	 *            Le int contenant le type d'information � afficher (NONE,
	 *            ALL_ATTRIBUTES, REDUCED_LABEL)
	 */
    public void setAttLabelType(int t) {
        if (t >= 0 && t <= 2) {
            this.attAmountType = t;
            showAttributes = true;
            if (attAmountType == LMOptions.NO_LABEL) showAttributes = false; else if (attAmountType == LMOptions.ATTRIBUTES_ALL) attributes = setToSimpleString(intent); else if (attAmountType == LMOptions.ATTRIBUTES_REDUCED) attributes = setToSimpleString(reducedIntent);
        }
    }

    /**
	 * Ajuste le type d'information affich�e en ce qui concerne les r�les
	 * 
	 * @param t
	 *            Le int contenant le type d'information � afficher (NONE,
	 *            EXACT_RULES, APPROX_RULES)
	 */
    public void setRulesLabelType(int t) {
        if (t >= 0 && t <= 2) {
            this.rulesType = t;
            showRules = true;
            if (rulesType == LMOptions.NO_LABEL) showRules = false; else if (rulesType == LMOptions.RULES_SHOW) rules = setToApproxStrings(parentConcept.getNestedConcept().getRuleSet());
        }
    }

    /**
	 * Permet de cacher l'�tiquette quand le concept parent est flou
	 * 
	 * @param hide
	 *            Le boolean indiquant si l'�tiquette doit �tre cach�e
	 */
    public void hideLabelWhenConceptIsOutOfFocus(boolean hide) {
        hideOutOfFocusLabel = hide;
    }

    /**
	 * Ajuste la position � laquelle l'�tiquette est affich�e
	 * 
	 * @param new_x
	 *            Le double contenant la nouvelle position en x
	 * @param new_y
	 *            Le double contenant la nouvelle position en y
	 */
    public void setPosition(double new_x, double new_y) {
        shape.setRect(new_x, new_y, shape.getWidth(), shape.getHeight());
        if (distanceFromNode == null) {
            if (!attributes.equals(" {} ") && !objects.equals(" {} ")) closestPointFromNode = new Point2D.Double(0, 25.0); else closestPointFromNode = new Point2D.Double(0, 11.0);
            closestNodePoint = new Point2D.Double(0, 0);
            distanceFromNode = new Point2D.Double(0, 0);
        }
        Ellipse2D parentShape = parentConcept.getShape();
        double scale = parentConcept.getParentLattice().getScale();
        double parentRadius = parentShape.getHeight() / 2.0;
        Point2D parentCenter = new Point2D.Double(parentShape.getX() + parentShape.getWidth() / 2.0, parentShape.getY() + parentShape.getHeight() / 2.0);
        Point2D topLeft = new Point2D.Double(new_x, new_y);
        Point2D topRight = new Point2D.Double(new_x + shape.getWidth(), new_y);
        Point2D bottomLeft = new Point2D.Double(new_x, new_y + shape.getHeight());
        Point2D bottomRight = new Point2D.Double(new_x + shape.getWidth(), new_y + shape.getHeight());
        double dist1 = (topLeft.getX() - parentCenter.getX()) * (topLeft.getX() - parentCenter.getX()) + (topLeft.getY() - parentCenter.getY()) * (topLeft.getY() - parentCenter.getY());
        double dist2 = (topRight.getX() - parentCenter.getX()) * (topRight.getX() - parentCenter.getX()) + (topRight.getY() - parentCenter.getY()) * (topRight.getY() - parentCenter.getY());
        double dist3 = (bottomLeft.getX() - parentCenter.getX()) * (bottomLeft.getX() - parentCenter.getX()) + (bottomLeft.getY() - parentCenter.getY()) * (bottomLeft.getY() - parentCenter.getY());
        double dist4 = (bottomRight.getX() - parentCenter.getX()) * (bottomRight.getX() - parentCenter.getX()) + (bottomRight.getY() - parentCenter.getY()) * (bottomRight.getY() - parentCenter.getY());
        double parentRadiusPow2 = parentRadius * parentRadius;
        if (dist1 < parentRadiusPow2 && dist2 < parentRadiusPow2 && dist3 < parentRadiusPow2 && dist4 < parentRadiusPow2) {
            double x = (shape.getX() + shape.getWidth() / 2.0 - parentShape.getX()) / scale;
            double y = (shape.getY() + shape.getHeight() / 2.0 - parentShape.getY()) / scale;
            closestNodePoint.setLocation(x, y);
            closestPointFromNode.setLocation(shape.getWidth() / 2.0, shape.getHeight() / 2.0);
            distanceFromNode.setLocation(0, 0);
        } else if ((dist1 > parentRadiusPow2 && dist2 > parentRadiusPow2 && dist3 < parentRadiusPow2 && dist4 < parentRadiusPow2) || (dist1 < parentRadiusPow2 && dist2 < parentRadiusPow2 && dist3 > parentRadiusPow2 && dist4 > parentRadiusPow2)) {
            Point2D topCorner = topLeft;
            Point2D bottomCorner = bottomLeft;
            if (dist1 >= parentRadiusPow2 && dist3 >= parentRadiusPow2) {
                topCorner = topRight;
                bottomCorner = bottomRight;
            }
            double a = parentCenter.getX();
            double b = parentCenter.getY();
            double r = parentRadius;
            double d = topCorner.getX();
            double newB = -2.0 * b;
            double newC = a * a + b * b - r * r + d * d - 2.0 * a * d;
            double y = (-newB + Math.sqrt(newB * newB - 4.0 * newC)) / 2.0;
            if (!(y >= topCorner.getY() && y <= bottomCorner.getY())) y = (-newB - Math.sqrt(newB * newB - 4.0 * newC)) / 2.0;
            double x = d;
            distanceFromNode.setLocation(0, 0);
            closestPointFromNode.setLocation(x - shape.getX(), y - shape.getY());
            closestNodePoint.setLocation((x - parentShape.getX()) / scale, (y - parentShape.getY()) / scale);
        } else if (dist1 < parentRadiusPow2 || dist2 < parentRadiusPow2 || dist3 < parentRadiusPow2 || dist4 < parentRadiusPow2) {
            Point2D leftCorner = topLeft;
            Point2D rightCorner = topRight;
            if (dist1 >= parentRadiusPow2 && dist2 >= parentRadiusPow2) {
                leftCorner = bottomLeft;
                rightCorner = bottomRight;
            }
            double a = parentCenter.getX();
            double b = parentCenter.getY();
            double r = parentRadius;
            double d = leftCorner.getY();
            double newB = -2.0 * a;
            double newC = a * a + b * b - r * r + d * d - 2.0 * b * d;
            double x = (-newB + Math.sqrt(newB * newB - 4.0 * newC)) / 2.0;
            if (!(x >= leftCorner.getX() && x <= rightCorner.getX())) x = (-newB - Math.sqrt(newB * newB - 4.0 * newC)) / 2.0;
            double y = d;
            distanceFromNode.setLocation(0, 0);
            closestPointFromNode.setLocation(x - shape.getX(), y - shape.getY());
            closestNodePoint.setLocation((x - parentShape.getX()) / scale, (y - parentShape.getY()) / scale);
        } else if (topLeft.getX() <= parentCenter.getX() && topRight.getX() >= parentCenter.getX()) {
            if (topLeft.getY() < parentCenter.getY()) {
                closestNodePoint.setLocation(parentRadius / scale, 0);
                closestPointFromNode.setLocation(parentCenter.getX() - bottomLeft.getX(), shape.getHeight());
                distanceFromNode.setLocation(0, bottomLeft.getY() - parentShape.getY());
            } else {
                closestNodePoint.setLocation(parentRadius / scale, parentShape.getHeight() / scale);
                closestPointFromNode.setLocation(parentCenter.getX() - bottomLeft.getX(), 0);
                distanceFromNode.setLocation(0, topLeft.getY() - (parentShape.getY() + parentShape.getHeight()));
            }
        } else if (topLeft.getY() <= parentCenter.getY() && bottomLeft.getY() >= parentCenter.getY()) {
            if (topRight.getX() < parentCenter.getX()) {
                closestNodePoint.setLocation(0, parentRadius / scale);
                closestPointFromNode.setLocation(shape.getWidth(), parentCenter.getY() - topRight.getY());
                distanceFromNode.setLocation(topRight.getX() - parentShape.getX(), 0);
            } else {
                closestNodePoint.setLocation(parentShape.getWidth() / scale, parentRadius / scale);
                closestPointFromNode.setLocation(0, parentCenter.getY() - topRight.getY());
                distanceFromNode.setLocation(bottomLeft.getX() - (parentShape.getX() + parentShape.getWidth()), 0);
            }
        } else {
            Point2D closestCorner = topLeft;
            double dist = dist1;
            if (dist2 < dist) {
                closestCorner = topRight;
                dist = dist2;
            }
            if (dist3 < dist) {
                closestCorner = bottomLeft;
                dist = dist3;
            }
            if (dist4 < dist) {
                closestCorner = bottomRight;
                dist = dist4;
            }
            closestPointFromNode.setLocation(closestCorner.getX() - topLeft.getX(), closestCorner.getY() - topLeft.getY());
            double a = parentCenter.getX();
            double b = parentCenter.getY();
            double r = parentRadius;
            double m = (closestCorner.getY() - b) / (closestCorner.getX() - a);
            double d = b - m * a;
            double newA = m * m + 1.0;
            double newB = 2.0 * d * m - 2.0 * a - 2.0 * b * m;
            double newC = a * a + b * b - r * r + d * d - 2.0 * b * d;
            double x = (-newB + Math.sqrt(newB * newB - 4.0 * newA * newC)) / (2.0 * newA);
            if (!(x >= closestCorner.getX() && x <= a) && !(x >= a && x <= closestCorner.getX())) x = (-newB - Math.sqrt(newB * newB - 4.0 * newA * newC)) / (2.0 * newA);
            double y = m * x + d;
            distanceFromNode.setLocation(closestCorner.getX() - x, closestCorner.getY() - y);
            closestNodePoint.setLocation((x - parentShape.getX()) / scale, (y - parentShape.getY()) / scale);
        }
    }

    /**
	 * Ajuste la position de l'�tiquette en fonction de sa distance avec son
	 * noeud d'appartenance
	 */
    public void refreshShape() {
        Ellipse2D parentShape = parentConcept.getShape();
        double scale = parentConcept.getParentLattice().getScale();
        double size = GraphicalConcept.LARGE_NODE_SIZE;
        if (parentConcept.getSizeType() == LMOptions.SMALL) size = GraphicalConcept.SMALL_NODE_SIZE;
        if (distanceFromNode == null) {
            if (!attributes.equals(" {} ") && !objects.equals(" {} ")) closestPointFromNode = new Point2D.Double(0, 25.0); else closestPointFromNode = new Point2D.Double(0, 11.0);
            closestNodePoint = new Point2D.Double(size / 2.0, 0);
            distanceFromNode = new Point2D.Double(0, 0);
        }
        double posX = parentShape.getX() + closestNodePoint.getX() * scale + distanceFromNode.getX() - closestPointFromNode.getX();
        double posY = parentShape.getY() + closestNodePoint.getY() * scale + distanceFromNode.getY() - closestPointFromNode.getY();
        shape.setRect(posX, posY, shape.getWidth(), shape.getHeight());
    }

    /**
	 * Permet d'obtenir la forme graphique de l'�tiquette
	 * 
	 * @return Un Rectangle2D contenant la forme graphique de l'�tiquette
	 */
    public Rectangle2D getShape() {
        return shape;
    }

    /**
	 * Permet de savoir si l'�tiquette est affich�e ou non
	 * 
	 * @return Un boolean indiquant si l'�tiquette est affich�e
	 */
    public boolean isVisible() {
        return isVisible;
    }

    /**
	 * Permet d'indiquer � l'�tiquette si elle est affich�e ou non
	 * 
	 * @param s
	 *            Le boolean indiquant � l'�tiquette si elle est affich�e
	 */
    public void setVisible(boolean s) {
        isVisible = s;
    }

    /**
	 * Permet de savoir si l'�tiquette est en surbrillance ou non
	 * 
	 * @return Un boolean indiquant si l'�tiquette est en surbrillance
	 */
    public boolean isHighlighted() {
        return isHighlighted;
    }

    /**
	 * Permet d'indiquer si l'�tiquette est en surbrillance ou non
	 * 
	 * @param s
	 *            Le boolean indiquant si l'�tiquette est en surbrillance
	 */
    public void setHighlighted(boolean s) {
        isHighlighted = s;
    }

    /**
	 * Permet d'afficher l'�tiquette
	 * 
	 * @param g2
	 *            Le composant Graphics2D qui doit afficher l'�tiquette
	 */
    public void paintLabel(Graphics2D g2) {
        if (hideOutOfFocusLabel && parentConcept.isOutOfFocus()) return;
        String firstRule = GUIMessages.getString("GUI.noRule");
        if (rules.size() > 0) firstRule = rules.elementAt(0);
        boolean displayObjects = showObjects && !objects.equals(" {} ");
        boolean displayAttributes = showAttributes && !attributes.equals(" {} ");
        boolean displayRules = showRules && !firstRule.equals(GUIMessages.getString("GUI.noRule"));
        boolean displayGenerators = showGene && !gene.equals("");
        if (!displayObjects && !displayAttributes && !displayRules && !displayGenerators) return;
        g2.setFont(new Font("Arial", Font.PLAIN, fontSize));
        g2.setStroke(new BasicStroke(1));
        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D intentBounds = fm.getStringBounds(attributes, g2);
        Rectangle2D extentBounds = fm.getStringBounds(objects, g2);
        Rectangle2D geneBounds = fm.getStringBounds(gene, g2);
        double width = 0;
        double height = 0;
        if (displayAttributes && displayObjects) {
            if (intentBounds.getWidth() > extentBounds.getWidth()) width = intentBounds.getWidth(); else width = extentBounds.getWidth();
            height = intentBounds.getHeight() + extentBounds.getHeight();
        } else if (displayObjects) {
            width = extentBounds.getWidth();
            height = extentBounds.getHeight();
        } else if (displayAttributes) {
            width = intentBounds.getWidth();
            height = intentBounds.getHeight();
        }
        if (displayGenerators) {
            if (width < geneBounds.getWidth()) width = geneBounds.getWidth();
            height += geneBounds.getHeight();
        }
        if (displayRules) {
            for (int i = 0; i < rules.size(); i++) {
                String ruleStr = rules.elementAt(i);
                Rectangle2D ruleBounds = fm.getStringBounds(ruleStr, g2);
                if (ruleBounds.getWidth() > width) width = ruleBounds.getWidth();
                height += ruleBounds.getHeight();
            }
        }
        if (width == 0 && height == 0) {
            width = 10;
            height = 10;
        }
        shape.setRect(shape.getX(), shape.getY(), width, height);
        Rectangle2D shadow = new Rectangle2D.Double(shape.getX() + 1, shape.getY() + 1, width, height);
        double x1 = shape.getX() + (shape.getWidth() / 2);
        double y1 = shape.getY() + (shape.getHeight() / 2);
        double x2 = parentConcept.getShape().getX() + (parentConcept.getShape().getWidth() / 2);
        double y2;
        if (parentConcept.getInternalLattice() != null && parentConcept.getInternalLattice().isVisible()) y2 = parentConcept.getShape().getY() + (3 * parentConcept.getScale()); else y2 = parentConcept.getShape().getY() - (8 * parentConcept.getScale());
        Line2D line = new Line2D.Double(x1, y1, x2, y2);
        g2.setPaint(Color.LIGHT_GRAY);
        g2.draw(line);
        g2.setPaint(Color.DARK_GRAY);
        g2.draw(shadow);
        g2.setPaint(Color.WHITE);
        g2.fill(shape);
        g2.setPaint(Color.LIGHT_GRAY);
        g2.draw(shape);
        float currentHeight = 0;
        if (displayAttributes) {
            g2.setPaint(Color.BLUE);
            currentHeight += (float) intentBounds.getHeight();
            g2.drawString(attributes, (float) shape.getX() + 1, (float) (shape.getY() + currentHeight) - 2);
        }
        if (displayObjects) {
            g2.setPaint(Color.RED);
            currentHeight += (float) extentBounds.getHeight();
            g2.drawString(objects, (float) shape.getX() + 1, (float) (shape.getY() + currentHeight) - 2);
        }
        if (displayRules) {
            g2.setPaint(Color.DARK_GRAY);
            for (int i = 0; i < rules.size(); i++) {
                String ruleStr = rules.elementAt(i);
                Rectangle2D ruleBounds = fm.getStringBounds(ruleStr, g2);
                currentHeight += ruleBounds.getHeight();
                g2.drawString(ruleStr, (float) shape.getX() + 1, (float) (shape.getY() + currentHeight) - 2);
            }
        }
        if (displayGenerators) {
            g2.setPaint(Color.GRAY);
            currentHeight += (float) geneBounds.getHeight();
            g2.drawString(gene, (float) shape.getX() + 1, (float) (shape.getY() + currentHeight) - 2);
        }
    }

    /**
	 * Fonction qui enregistre la nouvelle valeur de la taille du texte
	 * 
	 * @param newSize
	 *            la nouvelle taille
	 */
    public void setFontSize(int newSize) {
        fontSize = newSize;
    }

    /**
	 * Recupere la taille actuelle du texte
	 * 
	 * @return la taile du texte
	 */
    public int getFontSize() {
        return fontSize;
    }

    /**
	 * Ajuste le type d'information affich�e en ce qui concerne les attributs
	 * 
	 * @param t
	 *            Le int contenant le type d'information � afficher (NONE,
	 *            ALL_ATTRIBUTES, REDUCED_LABEL)
	 */
    public void setGeneLabelType(int t) {
        if (t >= 0 && t <= 1) {
            this.geneAmountType = t;
            showGene = true;
            if (geneAmountType == LMOptions.NO_LABEL) {
                showGene = false;
            } else if (geneAmountType == LMOptions.GENE_SHOW) {
                generators = parentConcept.getConcept().getGenerators();
                gene = generators.toString();
            }
        }
    }
}
