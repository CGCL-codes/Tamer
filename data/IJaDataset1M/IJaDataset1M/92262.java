package org.snipsnap.graph;

import java.awt.*;

public class TreeDimCalculator {

    private Dim[] recDim;

    private Dim picDim;

    private PicInfo picInfo;

    private Tree tree;

    private int kind;

    private FontInfo fontInfo;

    public TreeDimCalculator(PicInfo picInfo, Tree tree, int kind, FontInfo fontInfo) {
        this.picInfo = picInfo;
        this.tree = tree;
        this.kind = kind;
        this.fontInfo = fontInfo;
    }

    public void setDim(Graphics2D g2) {
        switch(kind) {
            case 0:
                changeVLimit();
                break;
            case 1:
                changeXY();
                break;
            case 2:
                changeXY();
                break;
            case 3:
                changeXY();
                break;
            default:
                changeVLimit();
                break;
        }
        setRecDim(g2);
        setPicDim();
        if (picInfo.getAutoBreak() == true) decreaseVLimit(g2);
        harmonize(g2);
    }

    private void decreaseVLimit(Graphics2D g2) {
        if (picInfo.getMaxPicWid() < getPicDim().getWidth() && picInfo.getVLimit() == tree.getRowCounter()) {
            picInfo.setVLimit(picInfo.getVLimit() - 1);
            setRecDim(g2);
            setPicDim();
        }
    }

    private void setRecDim(Graphics2D g2) {
        RecDimCalculation dimRecCalc = new RecDimCalculation(picInfo, fontInfo, tree, kind);
        recDim = dimRecCalc.calculateRecDims(g2);
    }

    private void setPicDim() {
        PicDim picCalc;
        switch(kind) {
            case 0:
                picCalc = new PicDimVertical(recDim, tree, picInfo);
                break;
            case 1:
                picCalc = new PicDimMindMap(recDim, tree, picInfo);
                break;
            case 2:
                picCalc = new PicDimHorizontal(recDim, tree, picInfo);
                break;
            case 3:
                picCalc = new PicDimExplorer(recDim, tree, picInfo);
                break;
            default:
                picCalc = new PicDimVertical(recDim, tree, picInfo);
                break;
        }
        int picWidth = picCalc.getWidth() + 10;
        int picHeight = picCalc.getHeight() + 10;
        picDim = new Dim(picWidth, picHeight);
    }

    public Dim[] getRecDim() {
        return recDim;
    }

    public Dim getPicDim() {
        return picDim;
    }

    private void harmonize(Graphics2D g2) {
        int maxPicWid = picInfo.getMaxPicWid();
        int picWid = picDim.getWidth();
        if (maxPicWid > picWid) picDim.setWidth(maxPicWid); else picInfo.setMaxPicWid(picWid);
        Harmony harmony = new Harmony(picInfo, fontInfo, tree, kind, recDim);
        harmony.iterateForHarmony(g2, 1, recDim);
    }

    private void changeXY() {
        int temp = picInfo.getDistanceY();
        picInfo.setDistanceY(picInfo.getDistanceX());
        picInfo.setDistanceX(temp);
    }

    private void changeVLimit() {
        int vLimit = picInfo.getVLimit();
        int rowCounter = tree.getRowCounter();
        if (vLimit < rowCounter - 1) vLimit = rowCounter - 1;
        if (vLimit <= 1) vLimit = 2;
        if (rowCounter < vLimit) vLimit = rowCounter;
        picInfo.setVLimit(vLimit);
    }

    public int getMaxRecWid(int row) {
        int maxPicWid = picInfo.getMaxPicWid() - 10;
        int vLimit = picInfo.getVLimit();
        int rowCounter = tree.getRowCounter();
        int maxChildren[] = tree.getMaxChildren();
        int maxRecWid = maxPicWid;
        if (kind == 0) {
            maxRecWid = maxPicWid;
            if (vLimit < row) row = vLimit;
            for (int i = 1; i <= row; i++) maxRecWid = maxRecWid / maxChildren[i];
        }
        if (kind == 3) {
            int s = rowCounter;
            int dx = picInfo.getDistanceX();
            maxRecWid = (int) (1.25 * maxPicWid / s);
        }
        if (kind == 1 || kind == 2) {
            int s = rowCounter;
            int dx = picInfo.getDistanceX();
            maxRecWid = maxPicWid / s - dx;
        }
        return maxRecWid;
    }
}
