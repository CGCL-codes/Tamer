package clutrfree;

public class TreeGraphics {

    protected float stroke;

    protected int titleFontSize;

    protected int currentFontSize;

    protected int diameterx;

    protected int diametery;

    protected float correlationStroke;

    protected int xSpace;

    protected int ySpace;

    protected int xposorig;

    protected int yposorig;

    private LinkedTree tree;

    private Size2D s2d;

    public TreeGraphics(LinkedTree tree, int xSpace, int ySpace) {
        this.xSpace = xSpace;
        this.ySpace = ySpace;
        this.tree = tree;
        setAttributes();
        develop();
    }

    private void setAttributes() {
        diameterx = 2 * xSpace / 3;
        diametery = ySpace / 2;
        titleFontSize = xSpace / 2;
        currentFontSize = ySpace / 3;
        stroke = 2.0f;
        correlationStroke = ySpace / 7;
        xposorig = xSpace;
        yposorig = ySpace / 2;
    }

    public void update(int xSpace, int ySpace) {
        this.xSpace = xSpace;
        this.ySpace = ySpace;
        setAttributes();
        reset();
        develop();
    }

    private void reset() {
        TreeNode cNode1 = tree.getFirstNode();
        TreeNode cNode2 = cNode1;
        for (int i = 0; i < cNode1.npatterns; i++) {
            resetNode(cNode2);
            cNode2 = cNode2.next;
        }
    }

    private void resetNode(TreeNode cNode) {
        cNode.xpos = 0;
        cNode.ypos = 0;
        if (cNode.nSons != 0) {
            for (int i = 0; i < cNode.nSons; i++) {
                resetNode(cNode.son[i]);
            }
        }
    }

    private void develop() {
        s2d = new Size2D(0, 0, 0, 0);
        TreeNode cNode1 = tree.getFirstNode();
        TreeNode cNode2 = cNode1;
        int nLevelWidth = tree.getNumberOfLevels();
        float[] maxx = new float[nLevelWidth];
        int i;
        float xpos, ypos;
        cNode2 = cNode1;
        s2d.maxWidth = 0;
        for (i = 0; i < cNode1.npatterns; i++) {
            xpos = s2d.maxWidth + xposorig;
            ypos = yposorig;
            placeNode(cNode2, xpos, ypos, s2d, maxx);
            cNode2 = cNode2.next;
        }
    }

    /**
	 * placeNode -> find the optimal node placement on the panel
	 */
    private float placeNode(TreeNode cNode, float xpos, float ypos, Size2D s2d, float[] maxx) {
        boolean alreadyAssigned = false;
        if (cNode.ypos != 0) alreadyAssigned = true;
        if (cNode.nSons == 0) {
            if (alreadyAssigned == false) {
                cNode.xpos = xpos;
                cNode.ypos = ypos;
            }
        } else {
            float x, value;
            int i;
            int nSonsnn;
            if (cNode.nSons == 1) {
                placeNode(cNode.son[0], s2d.maxWidth + xSpace, ypos + ySpace, s2d, maxx);
                if (cNode.son[0].xpos > maxx[cNode.level] + xSpace) x = cNode.son[0].xpos; else x = maxx[cNode.level] + xSpace;
                if (alreadyAssigned == false) {
                    xpos = x;
                    cNode.xpos = xpos;
                    cNode.ypos = ypos;
                }
            } else {
                for (i = 0, x = 0, nSonsnn = 0; i < cNode.nSons; i++) {
                    value = placeNode(cNode.son[i], s2d.maxWidth + xSpace, ypos + ySpace, s2d, maxx);
                    x += value;
                    if (value != 0) nSonsnn++;
                }
                if (alreadyAssigned == false) {
                    if (nSonsnn != 0) x /= (float) nSonsnn; else {
                        x = Math.min(maxx[cNode.level] + xSpace, xpos);
                    }
                    xpos = x;
                    cNode.xpos = xpos;
                    cNode.ypos = ypos;
                }
            }
        }
        if (alreadyAssigned == false) {
            if (maxx[cNode.level] < cNode.xpos) maxx[cNode.level] = cNode.xpos;
            if (s2d.minHeight > cNode.ypos) s2d.minHeight = cNode.ypos;
            if (s2d.minWidth > cNode.xpos) s2d.minWidth = cNode.xpos;
            if (s2d.maxHeight < cNode.ypos) s2d.maxHeight = cNode.ypos;
            if (s2d.maxWidth < cNode.xpos) s2d.maxWidth = cNode.xpos;
        }
        if (alreadyAssigned) return 0; else return cNode.xpos;
    }

    /**
	 * return width
	 */
    public float getMaxWidth() {
        return s2d.maxWidth;
    }

    /**
	 * return height
	 */
    public float getMaxHeight() {
        return s2d.maxHeight;
    }
}
