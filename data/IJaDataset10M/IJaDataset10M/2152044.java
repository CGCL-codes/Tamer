package piconode.visualpiconode;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Vector;

/**
 * This class provides neuron representation, graphics and attributes
 */
public class NNNode extends NNShape {

    public static final int _RAYON = 12;

    public static final int _UNKNOWN = 0;

    public static final int _INPUT = 1;

    public static final int _OUTPUT = 2;

    public static final int _HIDDEN = 3;

    public static final String[] types = { "Unknown", "Input", "Output", "Hidden" };

    public static final String[] functions = { "Sigmoide", "Creneau", "CustomThreshold", "Histerisis", "Id" };

    private static int nbNode = 0;

    private static int nbInputNode = 0, nbOutputNode = 0, nbHiddenNode = 0;

    private int positionX, positionY;

    private String type;

    private String function;

    private double valeur;

    private int myId;

    String underNodeDescription;

    private boolean undernode;

    Vector inputArcs;

    Vector outputArcs;

    /**
	 * Constructor from scratch
	 * 
	 * @param x
	 *            the x coordinate of the graphic representation
	 * @param y
	 *            the y coordinate
	 * @param gridActivated
	 *            indicates wether grid is set in NNCanvas.
	 */
    NNNode(int x, int y, boolean gridActivated) {
        positionX = x;
        positionY = y;
        inputArcs = new Vector();
        outputArcs = new Vector();
        nbNode++;
        myId = nbNode;
        setName("N " + nbNode);
        color = Color.WHITE;
        valeur = 1.0;
        function = functions[0];
        type = types[0];
        undernode = false;
        if (gridActivated) replaceForGrid();
    }

    /**
	 * Constructor, copy the node passed in argument and modify its position.
	 * 
	 * @param node_arg
	 *            the node to copy
	 */
    NNNode(NNNode node_arg) {
        positionX = node_arg.positionX + 35;
        positionY = node_arg.positionY + 10;
        inputArcs = new Vector();
        outputArcs = new Vector();
        undernode = node_arg.hasUnderNode();
        if (undernode) underNodeDescription = node_arg.getUnderNode();
        nbNode++;
        myId = nbNode;
        setName(node_arg.getName() + " copy");
        color = node_arg.getColor();
        valeur = node_arg.getValeur();
        function = node_arg.getFunction();
        type = node_arg.getType();
    }

    /**
	 * Constructor: used to convert an xml string into a Node
	 * 
	 * @param idnumber_arg
	 * @param name_arg
	 * @param type_arg
	 * @param function_arg
	 * @param x_arg
	 * @param y_arg
	 * @param red_arg
	 * @param green_arg
	 * @param blue_arg
	 */
    NNNode(String idnumber_arg, String name_arg, String type_arg, String function_arg, String x_arg, String y_arg, String red_arg, String green_arg, String blue_arg) {
        type = types[0];
        setType(type_arg);
        if (x_arg.equals("")) positionX = 100; else positionX = Integer.parseInt(x_arg);
        if (y_arg.equals("")) positionY = 100; else positionY = Integer.parseInt(y_arg);
        inputArcs = new Vector();
        outputArcs = new Vector();
        myId = Integer.parseInt(idnumber_arg);
        setName(name_arg);
        color = new Color(Integer.parseInt(red_arg), Integer.parseInt(green_arg), Integer.parseInt(blue_arg));
        valeur = 1.0;
        function = function_arg;
        if (myId >= nbNode) nbNode = myId + 1;
    }

    NNNode() {
        type = types[0];
        positionX = 100;
        positionY = 100;
        inputArcs = new Vector();
        outputArcs = new Vector();
        myId = 0;
        setName("");
        color = Color.WHITE;
        valeur = 0.0;
        function = functions[0];
    }

    /**
	 * Generate an Id if necessary
	 */
    void generateId() {
        if (myId == 0) {
            nbNode++;
            myId = nbNode;
        }
    }

    /**
	 * Reset the number of nodes in the program
	 * 
	 */
    static void resetNbNode() {
        System.err.println("reset nb node ");
        nbNode = nbInputNode = nbOutputNode = nbHiddenNode = 0;
    }

    /**
	 * delete a node
	 * 
	 * @param list
	 *            the list from wich the node has to be deleted
	 */
    public void delete(NNList list) {
        int i = 0;
        int vectorSize = inputArcs.size();
        NNArc nextArc;
        while (i < vectorSize) {
            nextArc = (NNArc) inputArcs.get(i);
            if (nextArc != null) {
                list.removeArc(nextArc);
            }
            i++;
        }
        i = 0;
        vectorSize = outputArcs.size();
        while (i < vectorSize) {
            nextArc = (NNArc) outputArcs.get(i);
            if (nextArc != null) {
                list.removeArc(nextArc);
            }
            i++;
        }
    }

    /**
	 * replace a node considering his type and number.
	 * 
	 * @param number :
	 *            number zero will be placed in bottom case,number two in the
	 *            third case etc...
	 */
    protected void replace(int number) {
        if (getType().equals(types[_INPUT])) {
            positionX = NNFrame.MIDGRIDWIDTH * 3;
            positionY = NNFrame.MIDGRIDWIDTH * (1 + 2 * number);
        } else if (getType().equals(types[_OUTPUT])) {
            positionX = NNFrame.MIDGRIDWIDTH * 19;
            positionY = NNFrame.MIDGRIDWIDTH * (1 + 2 * number);
        } else if (getType().equals(types[_HIDDEN])) {
            positionX = NNFrame.MIDGRIDWIDTH * 11;
            positionY = NNFrame.MIDGRIDWIDTH * (1 + 2 * number);
        }
    }

    /**
	 * return true if the center of the node is placed in the rect
	 * 
	 * @param rect
	 * @return
	 */
    boolean isInRect(Rectangle rect) {
        return (positionX >= rect.x && positionX <= rect.x + rect.width && positionY >= rect.y && positionY <= rect.y + rect.height);
    }

    /**
	 * return true if the point is included in the circle.
	 * 
	 * @param x_arg
	 * @param y_arg
	 * @return
	 */
    boolean contains(int x_arg, int y_arg) {
        if ((Math.pow(Math.abs(x_arg - positionX), 2) + Math.pow(Math.abs(y_arg - positionY), 2)) <= Math.pow(_RAYON, 2)) return true;
        return false;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    @Override
    void paint(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if ((selected != inverseTempSelection)) {
            g2.setColor(NNFrame._selectionColor);
        } else g2.setColor(color);
        g2.fillOval(positionX - _RAYON, positionY - _RAYON, _RAYON * 2, _RAYON * 2);
        g2.setColor(Color.black);
        g2.drawOval(positionX - _RAYON, positionY - _RAYON, _RAYON * 2, _RAYON * 2);
        g2.setColor(Color.black);
        FontMetrics fontMetrics = g2.getFontMetrics();
        String name = getName();
        int longueur = fontMetrics.stringWidth(name);
        int hauteur = fontMetrics.getHeight();
        int tx = positionX - longueur / 2;
        int ty = positionY + _RAYON + hauteur;
        g2.drawString(name, tx, ty);
        if (getType().equals(types[_INPUT])) {
            int largeurType = fontMetrics.stringWidth("I");
            g2.drawString("I", positionX - largeurType / 2, positionY + 2);
        } else if (getType().equals(types[_OUTPUT])) {
            int largeurType = fontMetrics.stringWidth("O");
            g2.drawString("O", positionX - largeurType / 2, positionY + 2);
        }
    }

    /**
	 * translate the neuron of dx in x and dy in y
	 * 
	 * @param dx
	 * @param dy
	 */
    public void translate(int dx, int dy) {
        setPositionX(positionX + dx);
        setPositionY(positionY + dy);
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setFunction(int function) {
        this.function = functions[function];
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (this.type.equalsIgnoreCase(types[1])) nbInputNode--; else if (this.type.equalsIgnoreCase(types[2])) nbOutputNode--; else if (this.type.equalsIgnoreCase(types[3])) nbHiddenNode--;
        if (type.equalsIgnoreCase(types[1])) this.type = types[1]; else if (type.equalsIgnoreCase(types[2])) this.type = types[2]; else if (type.equalsIgnoreCase(types[3])) this.type = types[3];
        if (this.type.equalsIgnoreCase(types[1])) nbInputNode++; else if (this.type.equalsIgnoreCase(types[2])) nbOutputNode++; else if (this.type.equalsIgnoreCase(types[3])) nbHiddenNode++;
    }

    public void setType(int type) {
        setType(types[type]);
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    /**
	 * Convert the node in an XML string
	 */
    @Override
    public String toXML() {
        String retour = "\t\t<node>";
        retour += "<name at=\"" + getName() + "\"/>";
        retour += "<id at=\"" + myId + "\"/>";
        retour += "<type at=\"" + getType() + "\"/>";
        retour += "<function at=\"" + getFunction() + "\"/>";
        retour += "<x at=\"" + positionX + "\"/>";
        retour += "<y at=\"" + positionY + "\"/>";
        retour += "<color" + " red=\"" + color.getRed() + "\"" + " green=\"" + color.getGreen() + "\"" + " blue=\"" + color.getBlue() + "\"/>";
        retour += "</node>";
        return retour;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    /**
	 * Recalculate x and y in function of the grid
	 * 
	 */
    public void replaceForGrid() {
        positionX = positionX - (positionX % (NNFrame.MIDGRIDWIDTH * 2)) + NNFrame.MIDGRIDWIDTH;
        positionY = positionY - (positionY % (NNFrame.MIDGRIDWIDTH * 2)) + NNFrame.MIDGRIDWIDTH;
    }

    protected Vector getInputArcs() {
        return inputArcs;
    }

    protected void setInputArcs(Vector inputArcs) {
        this.inputArcs = inputArcs;
    }

    protected Vector getOutputArcs() {
        return outputArcs;
    }

    protected void setOutputArcs(Vector outputArcs) {
        this.outputArcs = outputArcs;
    }

    public boolean hasUnderNode() {
        return undernode;
    }

    public String getUnderNode() {
        return underNodeDescription;
    }
}
