package megamek.common.xml;

import gd.xml.tiny.ParsedXML;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import megamek.common.Board;
import megamek.common.Building;
import megamek.common.Coords;
import megamek.common.IBoard;
import megamek.common.IGame;
import megamek.common.IHex;
import megamek.common.InfernoTracker;

/**
 * Objects of this class can encode a <code>Board</code> object as XML into an
 * output writer and decode one from a parsed XML node. It is used when saving
 * games into a version- neutral format.
 * 
 * @author James Damour <suvarov454@users.sourceforge.net>
 */
public class BoardEncoder {

    /**
     * Encode a <code>Board</code> object to an output writer.
     * 
     * @param board - the <code>Board</code> to be encoded. This value must
     *            not be <code>null</code>.
     * @param out - the <code>Writer</code> that will receive the XML. This
     *            value must not be <code>null</code>.
     * @throws <code>IllegalArgumentException</code> if the node is
     *             <code>null</code>.
     * @throws <code>IOException</code> if there's any error on write.
     */
    public static void encode(IBoard board, Writer out) throws IOException {
        Enumeration<?> iter;
        Coords coords;
        int x;
        int y;
        int turns;
        if (null == board) {
            throw new IllegalArgumentException("The board is null.");
        }
        if (null == out) {
            throw new IllegalArgumentException("The writer is null.");
        }
        out.write("<board version=\"1.0\" >");
        out.write("<boardData width=\"");
        out.write(Integer.toString(board.getWidth()));
        out.write("\" height=\"");
        out.write(Integer.toString(board.getHeight()));
        out.write("\" roadsAutoExit=\"");
        out.write(board.getRoadsAutoExit() ? "true" : "false");
        out.write("\" >");
        for (y = 0; y < board.getHeight(); y++) {
            for (x = 0; x < board.getWidth(); x++) {
                HexEncoder.encode(board.getHex(x, y), out);
            }
        }
        out.write("</boardData>");
        iter = board.getBuildings();
        if (iter.hasMoreElements()) {
            out.write("<buildings>");
            while (iter.hasMoreElements()) {
                BuildingEncoder.encode((Building) iter.nextElement(), out);
            }
            out.write("</buildings>");
        }
        iter = board.getInfernoBurningCoords();
        if (iter.hasMoreElements()) {
            out.write("<infernos>");
            while (iter.hasMoreElements()) {
                coords = (Coords) iter.nextElement();
                out.write("<inferno>");
                CoordsEncoder.encode(coords, out);
                turns = board.getInfernoIVBurnTurns(coords);
                if (turns > 0) {
                    out.write("<arrowiv turns=\"");
                    out.write(Integer.toString(turns));
                    out.write("\" />");
                }
                turns -= board.getInfernoBurnTurns(coords);
                turns = -turns;
                if (turns > 0) {
                    out.write("<standard turns=\"");
                    out.write(Integer.toString(turns));
                    out.write("\" />");
                }
                out.write("</inferno>");
            }
            out.write("</infernos>");
        }
        out.write("</board>");
    }

    /**
     * Decode a <code>Board</code> object from the passed node.
     * 
     * @param node - the <code>ParsedXML</code> node for this object. This
     *            value must not be <code>null</code>.
     * @param game - the <code>IGame</code> the decoded object belongs to.
     * @return the <code>Board</code> object based on the node.
     * @throws <code>IllegalArgumentException</code> if the node is
     *             <code>null</code>.
     * @throws <code>IllegalStateException</code> if the node does not contain
     *             a valid <code>Board</code>.
     */
    public static IBoard decode(ParsedXML node, IGame game) {
        String attrStr = null;
        int attrVal = 0;
        Board retVal = null;
        Vector<Building> buildings = new Vector<Building>();
        Hashtable<Coords, InfernoTracker> infernos = new Hashtable<Coords, InfernoTracker>();
        int height = 0;
        int width = 0;
        IHex[] hexes = null;
        Coords coords = null;
        Enumeration<?> subnodes = null;
        ParsedXML subnode = null;
        if (null == node) {
            throw new IllegalArgumentException("The board is null.");
        }
        if (!node.getName().equals("board")) {
            throw new IllegalStateException("Not passed a board node.");
        }
        Enumeration<?> children = node.elements();
        while (children.hasMoreElements()) {
            ParsedXML child = (ParsedXML) children.nextElement();
            String childName = child.getName();
            if (null == childName) {
            } else if (childName.equals("boardData")) {
                if (null != hexes) {
                    throw new IllegalStateException("More than one 'boardData' node in a board node.");
                }
                attrStr = child.getAttribute("height");
                if (null == attrStr) {
                    throw new IllegalStateException("Couldn't decode the boardData for a board node.");
                }
                try {
                    attrVal = Integer.parseInt(attrStr);
                } catch (NumberFormatException exp) {
                    throw new IllegalStateException("Couldn't get an integer from " + attrStr);
                }
                height = attrVal;
                if (height < 0 || height > Board.BOARD_MAX_HEIGHT) {
                    throw new IllegalStateException("Illegal value for height: " + attrStr);
                }
                attrStr = child.getAttribute("width");
                if (null == attrStr) {
                    throw new IllegalStateException("Couldn't decode the boardData for a board node.");
                }
                try {
                    attrVal = Integer.parseInt(attrStr);
                } catch (NumberFormatException exp) {
                    throw new IllegalStateException("Couldn't get an integer from " + attrStr);
                }
                width = attrVal;
                if (width < 0 || width > Board.BOARD_MAX_WIDTH) {
                    throw new IllegalStateException("Illegal value for width: " + attrStr);
                }
                hexes = new IHex[height * width];
                int numHexes = 0;
                subnodes = child.elements();
                while (subnodes.hasMoreElements()) {
                    subnode = (ParsedXML) subnodes.nextElement();
                    if (subnode.getName().equals("hex")) {
                        if (hexes.length == numHexes) {
                            throw new IllegalStateException("Too many hexes in a board node.");
                        }
                        hexes[numHexes] = HexEncoder.decode(subnode, game);
                        numHexes++;
                    }
                }
                if (numHexes < hexes.length) {
                    throw new IllegalStateException("Not enough hexes in a board node.");
                }
            } else if (childName.equals("infernos")) {
                subnodes = child.elements();
                while (subnodes.hasMoreElements()) {
                    subnode = (ParsedXML) subnodes.nextElement();
                    if (subnode.getName().equals("inferno")) {
                        coords = null;
                        InfernoTracker tracker = new InfernoTracker();
                        Enumeration<?> details = subnode.elements();
                        while (details.hasMoreElements()) {
                            ParsedXML detail = (ParsedXML) details.nextElement();
                            if (detail.getName().equals("coords")) {
                                coords = CoordsEncoder.decode(detail, game);
                            } else if (detail.getName().equals("arrowiv")) {
                                attrStr = detail.getAttribute("turns");
                                if (null == attrStr) {
                                    throw new IllegalStateException("Couldn't decode the burn turns for an Arrow IV inferno round.");
                                }
                                try {
                                    attrVal = Integer.parseInt(attrStr);
                                } catch (NumberFormatException exp) {
                                    throw new IllegalStateException("Couldn't get an integer from " + attrStr);
                                }
                                tracker.add(InfernoTracker.INFERNO_IV_TURN, attrVal);
                            } else if (detail.getName().equals("standard")) {
                                attrStr = detail.getAttribute("turns");
                                if (null == attrStr) {
                                    throw new IllegalStateException("Couldn't decode the burn turns for a standard inferno round.");
                                }
                                try {
                                    attrVal = Integer.parseInt(attrStr);
                                } catch (NumberFormatException exp) {
                                    throw new IllegalStateException("Couldn't get an integer from " + attrStr);
                                }
                                tracker.add(InfernoTracker.STANDARD_TURN, attrVal);
                            }
                        }
                        if (null == coords) {
                            throw new IllegalStateException("Couldn't decode the coordinates for an inferno round.");
                        }
                        infernos.put(coords, tracker);
                    }
                }
            } else if (childName.equals("buildings")) {
                subnodes = child.elements();
                while (subnodes.hasMoreElements()) {
                    subnode = (ParsedXML) subnodes.nextElement();
                    if (subnode.getName().equals("building")) {
                        Building bldg = BuildingEncoder.decode(subnode, game);
                        if (null != bldg) {
                            buildings.addElement(bldg);
                        }
                    }
                }
            }
        }
        if (null == hexes) {
            throw new IllegalStateException("Couldn't locate the boardData for a board node.");
        }
        retVal = new Board(width, height, hexes, buildings, infernos);
        return retVal;
    }
}
