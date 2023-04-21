package org.snipsnap.graph;

import java.util.*;
import java.awt.*;

public class Harmony extends TreeDimCalculator {

    private PicInfo picInfo;

    private FontInfo fontInfo;

    private Tree tree;

    private int kind;

    private Dim recDim[];

    private boolean ready[];

    private int oldWids[];

    private boolean halvedPrev[];

    private boolean halvedNow[];

    private int oldRow = 1;

    private int rowCounter;

    public Harmony(PicInfo picInfo, FontInfo fontInfo, Tree tree, int kind, Dim recDim[]) {
        super(picInfo, tree, kind, fontInfo);
        this.picInfo = picInfo;
        this.fontInfo = fontInfo;
        this.tree = tree;
        this.kind = kind;
        this.recDim = recDim;
        this.rowCounter = tree.getRowCounter();
    }

    public void iterateForHarmony(Graphics2D g2, int row, Dim[] recDim) {
        ready = new boolean[tree.getRowCounter() + 1];
        halvedPrev = new boolean[rowCounter + 1];
        halvedNow = new boolean[rowCounter + 1];
        oldWids = new int[rowCounter + 1];
        int startFontSize = fontInfo.getFont().getSize();
        int startFontAttrSize = fontInfo.getFontAttributes().getSize();
        for (int i = 1; i <= rowCounter; i++) oldWids[i] = recDim[i].getWidth();
        iterate(tree.getRoot(), row, recDim, g2);
        FontMetrics fm = g2.getFontMetrics(fontInfo.getFont());
        for (int i = 1; i <= rowCounter; i++) {
            if (halvedPrev[i] == true && halvedNow[i] == false) {
                recDim[i].setHeight(recDim[i].getHeight() - fm.getAscent());
            }
        }
        fontInfo.setStartFontSizes(startFontSize, startFontAttrSize);
    }

    private int getMaxPlace(int row) {
        int maxRecWid = getMaxRecWid(row);
        if (kind == 2 || kind == 1) {
            for (int i = 1; i <= rowCounter; i++) {
                if (maxRecWid < recDim[i].getWidth()) {
                    int hangOver = recDim[i].getWidth() - maxRecWid;
                    maxRecWid = maxRecWid - hangOver / (rowCounter - 1);
                }
            }
        }
        return maxRecWid;
    }

    private void iterate(Node node, int row, Dim[] recDim, Graphics2D g2) {
        if (oldRow < row) fontInfo.changeFontSize("minus", row, oldRow);
        if (oldRow > row) fontInfo.changeFontSize("plus", row, oldRow);
        ArrayList nodelist = node.getChildrenList();
        Iterator it = nodelist.iterator();
        int maxChildren = tree.getMaxChildren()[row];
        int vLimit = picInfo.getVLimit();
        int sisters = 1;
        if (row > 1) sisters = node.getParent().getChildrenList().size();
        if (ready[row] == false && maxChildren == sisters) makeWider(row, recDim);
        oldRow = row;
        FontMetrics fm = g2.getFontMetrics(fontInfo.getFont());
        int nodeStringLength = fm.stringWidth(node.getName());
        if (nodeStringLength >= oldWids[row] - 8) halvedPrev[row] = true;
        if (nodeStringLength >= recDim[row].getWidth() - 8) halvedNow[row] = true;
        while (it.hasNext()) {
            node = (Node) (it.next());
            iterate(node, row + 1, recDim, g2);
        }
    }

    private void makeWider(int row, Dim[] recDim) {
        int tmp = recDim[row].getWidth();
        int recWid = recDim[row].getWidth();
        int recHei = recDim[row].getHeight();
        ready[row] = true;
        int maxPlace = getMaxPlace(row) - 8;
        if (maxPlace >= recWid && recWid < recHei * 1.7) {
            tmp = (int) (recHei * 1.7);
            while (maxPlace < tmp) tmp--;
        }
        recDim[row].setWidth(tmp);
    }
}
