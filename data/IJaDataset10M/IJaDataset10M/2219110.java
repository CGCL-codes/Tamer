package remixlab.remixvym.core.util;

import java.util.ArrayList;
import processing.core.PConstants;
import remixlab.remixvym.core.graphics.StyleGraph;
import remixlab.remixvym.core.graphics.node.NodeConstans;
import remixlab.remixvym.core.graphics.text.TextUtil;
import remixlab.remixvym.core.mindmap.Node;

/**
 * This class implements the NodeConstant's properties related to
 * let the nodes be scrolled (Hide and Show sections of the mind map)**/
public class ScrolledUtil implements NodeConstans {

    /**
	 * Implements the constants defined in the NodeConstants interface.
	 */
    public ScrolledUtil() {
        SPACE_X = defSpaceX;
        SPACE_Y = defSpaceY;
        LONG_EMOTICON = defLongEmoticon;
        SPACE_OBJECTS = defSpaceObjects;
        INIT_SPACE = defInitSpace;
        SPACE_ICONS = defSpaceIcons;
    }

    /**
	 * Returns a boolean for showing the scrolled node comparing the father node's position,
	 * the actual node's position and the arrow key value.
	 */
    public boolean show(Node node, int key) {
        if (node.children.size() == 0) return false;
        if (node.getFather().x1 < node.x1 && key == PConstants.RIGHT) return true;
        if (node.getFather().x1 > node.x1 && key == PConstants.LEFT) return true;
        return false;
    }

    /**
	 * Returns a boolean for hide the showed node comparing the father node's position,
	 * the actual node's position and the arrow key value.
	 */
    public boolean hide(Node node, int key) {
        if (node.children.size() == 0) return false;
        if (node.getFather().x1 < node.x1 && key == PConstants.LEFT) return true;
        if (node.getFather().x1 > node.x1 && key == PConstants.RIGHT) return true;
        return false;
    }

    /**
	 * Verifies the position of the current node and the node father, comparing their X coordinates.
	 * Depending the position, the method calls {@link #hideNodes(Node)} for hide all nodes in the
	 * branch and {@link #position(Node)} for reorganize the positions.
	 */
    public void hideLevel(Node node) {
        if (node.getFather().x1 < node.x1) dirRigth = true; else dirRigth = false;
        hideNodes(node);
        position(node);
    }

    /**
	 * Search into the XML tree and hides the nodes of the chosen branch.
	 */
    private void hideNodes(Node node) {
        if (node.children.isEmpty()) return;
        for (int i = 0; i < node.children.size(); i++) {
            node.children.get(i).hide = true;
            node.children.get(i).x1 = 0;
            node.children.get(i).y1 = 0;
            node.children.get(i).x2 = 0;
            node.children.get(i).y2 = 0;
            node.children.get(i).calculate();
            hideNodes(node.children.get(i));
        }
        node.scrolledNow = true;
    }

    /**
	 * Verifies the position of the current node and the node father, comparing their X cordinates.
	 * Depending the position, the method calls {@link #hideNodes(Node)} for hide all nodes in the
	 * branch and {@link #position(Node)} for reorganize the positions.
	 */
    public void showLevel(Node node, StyleGraph style) {
        if (node.getFather().x1 < node.x1) dirRigth = true; else dirRigth = false;
        showNodes(node, style);
        position(node);
    }

    private void showNodes(Node node, StyleGraph style) {
        if (node.children.isEmpty()) return;
        int i;
        int widthNode;
        int j;
        ArrayList<String> list;
        int wdText;
        int heightNode;
        for (i = 0; i < node.children.size(); i++) {
            list = TextUtil.getSize(style, style.fontNormal, node.children.get(i).getInfo());
            wdText = Integer.parseInt(list.get(list.size() - 1));
            widthNode = INIT_SPACE + wdText;
            for (j = 0; node.children.get(i).emoticones != null && j < node.children.get(i).emoticones.size(); j++) widthNode = widthNode + LONG_EMOTICON + SPACE_OBJECTS;
            widthNode = widthNode + INIT_SPACE + SPACE_ICONS;
            heightNode = (int) (style.heigthNormal + ((list.size() - 2) * (style.heigthNormal)));
            node.children.get(i).x1 = 0;
            node.children.get(i).y1 = 0;
            node.children.get(i).x2 = widthNode;
            node.children.get(i).y2 = heightNode;
            node.children.get(i).calculate();
            node.children.get(i).hide = false;
            if (!node.children.get(i).scrolled) showNodes(node.children.get(i), style);
        }
        node.scrolledNow = false;
    }

    /**Calculates the new position of the node and its children*/
    private void position(Node node) {
        Node idea = node;
        while (idea.getFather().getFather() != null) idea = idea.getFather();
        NodeTemp nodeHeigth = new NodeTemp();
        getHeigths(idea, nodeHeigth);
        if (dirRigth) organiceRigth(idea, nodeHeigth); else organiceLeft(idea, nodeHeigth);
    }

    /**Calculates the total height required to show all the node's children.*/
    private int getHeigths(Node node, NodeTemp nodeHeigth) {
        if (node.scrolledNow || node.children.isEmpty()) return (int) node.height;
        NodeTemp child;
        int totalHeigth = 0;
        int heigths[] = new int[node.children.size()];
        for (int i = 0; i < node.children.size(); i++) {
            child = new NodeTemp();
            nodeHeigth.children.add(child);
            heigths[i] = getHeigths(node.children.get(i), child);
            totalHeigth = totalHeigth + heigths[i] + SPACE_Y;
        }
        nodeHeigth.heigths = heigths;
        totalHeigth = totalHeigth - SPACE_Y;
        nodeHeigth.totalHeigth = totalHeigth;
        return totalHeigth;
    }

    /**Shows the children nodes towards the right direction*/
    private void organiceRigth(Node node, NodeTemp nodeHeigth) {
        if (node.scrolledNow) return;
        int i;
        int heigths[];
        int spaceNodeCont;
        int posY;
        int posX;
        Node nodeNow;
        spaceNodeCont = (node.y1 + node.height / 2) - (nodeHeigth.totalHeigth / 2);
        posX = node.x2 + SPACE_X;
        heigths = nodeHeigth.heigths;
        for (i = 0; i < heigths.length; i++) {
            nodeNow = node.children.get(i);
            posY = spaceNodeCont + (heigths[i] / 2) - (node.children.get(i).height / 2);
            nodeNow.x1 = posX;
            nodeNow.y1 = posY;
            nodeNow.x2 = posX + nodeNow.width;
            nodeNow.y2 = posY + nodeNow.height;
            nodeNow.calculate();
            spaceNodeCont = spaceNodeCont + heigths[i] + SPACE_Y;
            if (!nodeNow.children.isEmpty() && !nodeNow.scrolledNow) organiceRigth(nodeNow, nodeHeigth.children.get(i));
        }
    }

    /**Shows the children nodes towards the left direction*/
    private void organiceLeft(Node node, NodeTemp nodeHeigth) {
        if (node.scrolledNow) return;
        int i;
        int heigths[];
        int spaceNodeCont;
        int posY;
        int posX;
        Node nodeNow;
        spaceNodeCont = (node.y1 + node.height / 2) - (nodeHeigth.totalHeigth / 2);
        posX = node.x1 - SPACE_X;
        heigths = nodeHeigth.heigths;
        for (i = 0; i < heigths.length; i++) {
            nodeNow = node.children.get(i);
            posY = spaceNodeCont + (heigths[i] / 2) - (node.children.get(i).height / 2);
            nodeNow.x1 = posX - nodeNow.width;
            nodeNow.y1 = posY;
            nodeNow.x2 = posX;
            nodeNow.y2 = posY + nodeNow.height;
            nodeNow.calculate();
            spaceNodeCont = spaceNodeCont + heigths[i] + SPACE_Y;
            if (!nodeNow.children.isEmpty() && !nodeNow.scrolledNow) organiceLeft(nodeNow, nodeHeigth.children.get(i));
        }
    }

    /**
	 * Changes the node and emoticons scale.
	 */
    public void changeScale(double rate) {
        SPACE_X = (int) ((double) defSpaceX * rate);
        SPACE_Y = (int) ((double) defSpaceY * rate);
        LONG_EMOTICON = (int) ((double) defLongEmoticon * rate);
        SPACE_OBJECTS = (int) ((double) defSpaceObjects * rate);
        INIT_SPACE = (int) ((double) defInitSpace * rate);
        if (SPACE_X < 12) SPACE_X = 12;
        if (SPACE_Y < 8) SPACE_Y = 8;
        if (LONG_EMOTICON > defLongEmoticon) LONG_EMOTICON = defLongEmoticon;
        if (SPACE_OBJECTS > defSpaceObjects) SPACE_OBJECTS = defSpaceObjects;
        if (INIT_SPACE > defInitSpace) INIT_SPACE = defInitSpace;
        SPACE_ICONS = (int) ((double) defSpaceIcons * rate);
    }

    /**X-Space between two nodes*/
    private int SPACE_X;

    /**Y-Space between two nodes*/
    private int SPACE_Y;

    /**Longitude of an emoticon*/
    private int LONG_EMOTICON;

    /**Space between emoticons*/
    private int SPACE_OBJECTS;

    /**Space dedicated to icons*/
    private int SPACE_ICONS;

    /**The space between the left border and the begin of the text*/
    private int INIT_SPACE;

    /**Indicates if children is show towards right (true) or left (false)*/
    private boolean dirRigth;

    /**
	 * Save the temporal distance between nodes into the {@link #children} Array List.
	 */
    private class NodeTemp {

        public int heigths[];

        public int totalHeigth;

        public ArrayList<NodeTemp> children = new ArrayList<NodeTemp>();
    }
}
