package treemap;

import java.awt.*;
import javax.swing.*;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.Vector;

/**
 * The TMAlgorithmSquarified class implements a squarified treemap drawing algorithm.
 * See Bruls, Huizing and van Wijk.
 */
public class TMAlgorithmSquarified extends TMAlgorithm {

    /**
     * Draws the children of a node, by setting their drawing area first,
     * dependant of the algorithm used.
     *
     * @param g        the graphic context
     * @param node     the node whose children should be drawn
     * @param axis     the axis of separation
     * @param level    the level of deep
     */
    protected void drawChildren(Graphics2D g, TMNodeModelComposite node, short axis, int level) {
        float pSize = node.getSize();
        Rectangle pArea = node.getArea();
        int x = pArea.x;
        int y = pArea.y;
        int width = pArea.width;
        int height = pArea.height;
        TMNodeModel child = null;
        Vector sortedChilds = new Vector();
        if (pSize == 0.0f) {
            return;
        }
        if ((width > borderLimit) && (height > borderLimit)) {
            x += borderSize;
            y += borderSize;
            width -= borderSize * 2;
            height -= borderSize * 2;
        }
        boolean isFirst = true;
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            child = (TMNodeModel) e.nextElement();
            float cSize = child.getSize();
            if (isFirst) {
                sortedChilds.add(child);
                isFirst = false;
            } else {
                boolean childSorted = false;
                TMNodeModel candidate = null;
                for (int index = 0; index < sortedChilds.size(); index++) {
                    candidate = (TMNodeModel) sortedChilds.get(index);
                    float candidateSize = candidate.getSize();
                    if (candidateSize < cSize) {
                        sortedChilds.add(index, child);
                        childSorted = true;
                        break;
                    }
                }
                if (!childSorted) {
                    sortedChilds.add(child);
                }
            }
        }
        while (!sortedChilds.isEmpty()) {
            child = (TMNodeModel) sortedChilds.remove(0);
            Vector block = new Vector();
            block.add(child);
            float blockSize = child.getSize();
            short blockAxis = HORIZONTAL;
            if (width < height) {
                blockAxis = VERTICAL;
            }
            float w = 0.0f;
            float h = 0.0f;
            if (blockAxis == HORIZONTAL) {
                w = (blockSize / pSize) * width;
                h = height;
            } else {
                w = width;
                h = (blockSize / pSize) * height;
            }
            float ratio = ratio(w, h);
            boolean blockDone = false;
            while ((!sortedChilds.isEmpty()) && (!blockDone)) {
                TMNodeModel candidate = (TMNodeModel) sortedChilds.firstElement();
                float newSize = candidate.getSize();
                float newBlockSize = blockSize + newSize;
                float newW = 0.0f;
                float newH = 0.0f;
                if (blockAxis == HORIZONTAL) {
                    newW = (newBlockSize / pSize) * width;
                    newH = (newSize / newBlockSize) * height;
                } else {
                    newW = (newSize / newBlockSize) * width;
                    newH = (newBlockSize / pSize) * height;
                }
                float newRatio = ratio(newW, newH);
                if (newRatio > ratio) {
                    blockDone = true;
                } else {
                    sortedChilds.remove(0);
                    block.add(candidate);
                    ratio = newRatio;
                    blockSize = newBlockSize;
                }
            }
            int childWidth = 0;
            int childHeight = 0;
            int childX = x;
            int childY = y;
            int maxX = x + width - 1;
            int maxY = y + height - 1;
            if (blockAxis == HORIZONTAL) {
                childWidth = Math.round((blockSize / pSize) * width);
            } else {
                childHeight = Math.round((blockSize / pSize) * height);
            }
            float proportion = 0.0f;
            float remaining = 0.0f;
            for (Enumeration e = block.elements(); e.hasMoreElements(); ) {
                child = (TMNodeModel) e.nextElement();
                Rectangle cArea = child.getArea();
                cArea.x = childX;
                cArea.y = childY;
                proportion = (child.getSize()) / blockSize;
                if (e.hasMoreElements()) {
                    if (blockAxis == HORIZONTAL) {
                        float fHeight = proportion * height;
                        childHeight = Math.round(fHeight);
                        remaining += fHeight - childHeight;
                        if (remaining >= 1) {
                            childHeight += 1;
                            remaining -= 1;
                        } else if (remaining <= -1) {
                            childHeight -= 1;
                            remaining += 1;
                        }
                        childY += childHeight;
                    } else {
                        float fWidth = proportion * width;
                        childWidth = Math.round(fWidth);
                        remaining += fWidth - childWidth;
                        if (remaining >= 1) {
                            childWidth += 1;
                            remaining -= 1;
                        } else if (remaining <= -1) {
                            childWidth -= 1;
                            remaining += 1;
                        }
                        childX += childWidth;
                    }
                } else {
                    if (blockAxis == HORIZONTAL) {
                        childHeight = (maxY - childY) + 1;
                    } else {
                        childWidth = (maxX - childX) + 1;
                    }
                }
                cArea.width = childWidth;
                cArea.height = childHeight;
                drawNodes(g, child, switchAxis(axis), (level + 1));
            }
            pSize -= blockSize;
            if (blockAxis == HORIZONTAL) {
                x += childWidth;
                width -= childWidth;
            } else {
                y += childHeight;
                height -= childHeight;
            }
        }
    }

    private float ratio(float w, float h) {
        return Math.max((w / h), (h / w));
    }
}
