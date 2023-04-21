package org.sodeja.swing.layout;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GridBag {

    private static final GridBagConstraints GBG = new GridBagConstraints();

    private static final int DEFAULT_ANCHOR = GridBagConstraints.CENTER;

    private static final int DEFAULT_FILL = GridBagConstraints.BOTH;

    private static final Insets DEFAULT_INSETS = new Insets(2, 2, 2, 2);

    private static final int DEFAULT_IPADX = 0;

    private static final int DEFAULT_IPADY = 0;

    public static GridBagConstraints create(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty) {
        return create(gridx, gridy, gridwidth, gridheight, weightx, weighty, DEFAULT_ANCHOR, DEFAULT_FILL);
    }

    public static GridBagConstraints create(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill) {
        return create(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, DEFAULT_INSETS);
    }

    private static GridBagConstraints create(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets) {
        return create(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, DEFAULT_IPADX, DEFAULT_IPADY);
    }

    public static GridBagConstraints bigPanel() {
        return bigPanel(0, 1);
    }

    public static GridBagConstraints bigPanelWidth(int gridwidth) {
        return bigPanel(0, gridwidth);
    }

    public static GridBagConstraints bigPanel(int gridy, int gridwidth) {
        return bigPanel(0, gridy, gridwidth, 1);
    }

    public static GridBagConstraints bigPanelHeight(int height) {
        return bigPanel(0, 0, 1, height);
    }

    private static GridBagConstraints bigPanel(int gridx, int gridy, int gridwidth, int gridheight) {
        return create(gridx, gridy, gridwidth, gridheight, 1.0, 1.0);
    }

    public static GridBagConstraints halfPanel(int gridy) {
        return create(0, gridy, 1, 1, 1.0, 0.5);
    }

    public static GridBagConstraints line(int gridy) {
        return line(0, gridy);
    }

    public static GridBagConstraints separatorLine(int gridy, int gridWidth) {
        return separatorLine(0, gridy, gridWidth);
    }

    public static GridBagConstraints separatorLine(int gridx, int gridy, int gridWidth) {
        return create(gridx, gridy, gridWidth, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
    }

    public static GridBagConstraints separatorLineFixed(int gridx, int gridy, int gridWidth) {
        return create(gridx, gridy, gridWidth, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
    }

    public static GridBagConstraints lineLabel(int gridy) {
        return line(0, gridy, 0.0);
    }

    public static GridBagConstraints lineLabelNorth(int gridy) {
        return create(0, gridy, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL);
    }

    public static GridBagConstraints lineField(int gridy) {
        return line(1, gridy);
    }

    public static GridBagConstraints lineField(int gridy, int gridWidth) {
        return create(1, gridy, gridWidth, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL);
    }

    private static GridBagConstraints line(int gridx, int gridy) {
        return line(gridx, gridy, 1.0);
    }

    private static GridBagConstraints line(int gridx, int gridy, double weightx) {
        return create(gridx, gridy, 1, 1, weightx, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL);
    }

    public static GridBagConstraints buttonLine() {
        return buttonLine(1);
    }

    public static GridBagConstraints buttonLine(int gridy) {
        return buttonLine(gridy, 1);
    }

    public static GridBagConstraints buttonLine(int gridy, int gridwidth) {
        return buttonLine(0, gridy, gridwidth, 1.0);
    }

    public static GridBagConstraints buttonLine(int gridx, int gridy, int gridwidth) {
        return buttonLine(gridx, gridy, gridwidth, 0.0);
    }

    private static GridBagConstraints buttonLine(int gridx, int gridy, int gridwidth, double weightx) {
        return create(gridx, gridy, gridwidth, 1, weightx, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE);
    }

    public static GridBagConstraints leftButtonLine(int gridy) {
        return leftButtonLine(gridy, 1);
    }

    public static GridBagConstraints leftButtonLine(int gridy, int gridwidth) {
        return leftButtonLine(0, gridy, gridwidth, 1.0);
    }

    public static GridBagConstraints leftButtonLine(int gridx, int gridy, int gridwidth) {
        return leftButtonLine(gridx, gridy, gridwidth, 0.0);
    }

    private static GridBagConstraints leftButtonLine(int gridx, int gridy, int gridwidth, double weightx) {
        return create(gridx, gridy, gridwidth, 1, weightx, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
    }

    public static GridBagConstraints buttonColumn(int gridx, int gridheight) {
        return buttonColumn(gridx, 0, gridheight);
    }

    public static GridBagConstraints buttonColumnFixed(int gridx, int gridheight) {
        return create(gridx, 0, 1, gridheight, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE);
    }

    public static GridBagConstraints buttonColumn(int gridx, int gridy, int gridheight) {
        return create(gridx, gridy, 1, gridheight, 0.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE);
    }

    public static GridBagConstraints fillingButtonLine(int gridy, int gridwidth) {
        return create(0, gridy, gridwidth, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE);
    }

    private static GridBagConstraints create(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady) {
        GBG.gridx = gridx;
        GBG.gridy = gridy;
        GBG.gridwidth = gridwidth;
        GBG.gridheight = gridheight;
        GBG.weightx = weightx;
        GBG.weighty = weighty;
        GBG.anchor = anchor;
        GBG.fill = fill;
        GBG.insets = insets;
        GBG.ipadx = ipadx;
        GBG.ipady = ipady;
        return GridBag.GBG;
    }
}
