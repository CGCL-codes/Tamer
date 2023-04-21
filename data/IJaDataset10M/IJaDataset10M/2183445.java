package org.phylowidget.render;

import java.util.Collections;
import java.util.List;
import org.phylowidget.PhyloWidget;
import org.phylowidget.tree.PhyloNode;
import org.phylowidget.tree.RootedTree;
import processing.core.PGraphics;

public class LayoutDiagonal extends LayoutBase {

    int numLeaves;

    public void layoutImpl() {
        numLeaves = leaves.length;
        double index = 0;
        for (PhyloNode leaf : leaves) {
            leafPosition(leaf, index);
            index++;
        }
        branchPosition((PhyloNode) tree.getRoot());
    }

    @Override
    public void drawLine(PGraphics canvas, PhyloNode p, PhyloNode c) {
        canvas.line(c.getX(), c.getY(), p.getX(), p.getY());
    }

    protected float branchPosition(PhyloNode n) {
        setAngle(n, 0);
        if (tree.isLeaf(n)) return 0;
        List<PhyloNode> children = tree.getChildrenOf(n);
        for (int i = 0; i < children.size(); i++) {
            PhyloNode child = children.get(i);
        }
        for (int i = 0; i < children.size(); i++) {
            PhyloNode child = (PhyloNode) children.get(i);
            branchPosition(child);
        }
        Collections.sort(children);
        PhyloNode loChild = (PhyloNode) Collections.min(children);
        PhyloNode hiChild = (PhyloNode) Collections.max(children);
        float stepSize = 1f / (numLeaves);
        float loLeaves = tree.getNumEnclosedLeaves(loChild);
        float hiLeaves = tree.getNumEnclosedLeaves(hiChild);
        float mLeaves = Math.max(loLeaves, hiLeaves);
        float loChildNewY = loChild.getTargetY() + (mLeaves - loLeaves) * stepSize / 2;
        float hiChildNewY = hiChild.getTargetY() - (mLeaves - hiLeaves) * stepSize / 2;
        float unscaledY = (loChildNewY + hiChildNewY) / 2;
        float unscaledX = calcXPosition(n);
        setPosition(n, unscaledX, unscaledY);
        return 0;
    }

    protected int getRealEnclosedLeaves(PhyloNode n) {
        int numEnclosed = tree.getNumEnclosedLeaves(n);
        int orig = numEnclosed;
        List<PhyloNode> children = tree.getChildrenOf(n);
        return numEnclosed;
    }

    private void leafPosition(PhyloNode n, double index) {
        float yPos = ((float) (index + .5f) / (float) (numLeaves));
        float xPos = 1;
        xPos = calcXPosition(n);
        setPosition(n, xPos, yPos);
    }

    protected float calcXPosition(PhyloNode n) {
        float a = xPosForNumEnclosedLeaves(getRealEnclosedLeaves(n));
        return a / 2;
    }

    float xPosForNumEnclosedLeaves(int numLeaves) {
        float asdf = 1 - (float) (numLeaves - 1) / (float) (leaves.length);
        return asdf;
    }
}
