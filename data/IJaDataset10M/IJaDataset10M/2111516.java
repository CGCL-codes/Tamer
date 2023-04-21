package com.ibm.richtext.awtui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import com.ibm.richtext.textpanel.MTextPanel;
import com.ibm.richtext.uiimpl.TabRulerImpl;
import com.ibm.richtext.styledtext.MTabRuler;

/**
 * TabRuler is an implementation of MTabRulerComponent in an AWT component.
 */
public final class TabRuler extends Component implements MTabRulerComponent {

    static final String COPYRIGHT = "(C) Copyright IBM Corp. 1998-1999 - All Rights Reserved";

    private TabRulerImpl fImpl;

    /**
     * Create a new TabRuler.
     * @param baseline the y-coordinate of the ruler's baseline
     * @param origin the x-coordinate in this Component where
     *     the left margin appears
     * @param textPanel the MTextPanel to listen to.  This TabRuler
     *     will reflect the MTextPanel's paragraph styles, and update
     *     the paragraph styles when manipulated.
     */
    public TabRuler(int baseline, int origin, MTextPanel textPanel) {
        fImpl = new TabRulerImpl(baseline, origin, textPanel, this);
    }

    /**
     * Listen to the given MTextPanel and reflect its changes,
     * and update its paragraph styles when TabRuler is
     * manipulated.
     * @param textPanel the MTextPanel to listen to
     */
    public void listenToTextPanel(MTextPanel textPanel) {
        fImpl.listenToTextPanel(textPanel);
    }

    /**
     * Return the background color of this TabRuler.
     * @return the background color of this TabRuler
     */
    public Color getBackColor() {
        return fImpl.getBackColor();
    }

    /**
     * Set the background color of this TabRuler.
     * @param backColor the new background color of this TabRuler
     */
    public void setBackColor(Color backColor) {
        fImpl.setBackColor(backColor);
    }

    /**
     * Return the MTabRuler represented by this TabRuler.
     * @return the MTabRuler represented by this TabRuler
     */
    public MTabRuler getRuler() {
        return fImpl.getRuler();
    }

    /**
     * Return the leading margin of this TabRuler.
     * @return the leading margin of this TabRuler
     */
    public int getLeadingMargin() {
        return fImpl.getLeadingMargin();
    }

    /**
     * Return the first line indent of this TabRuler.
     * @return the first line indent of this TabRuler
     */
    public int getFirstLineIndent() {
        return fImpl.getFirstLineIndent();
    }

    /**
     * Return the trailing margin of this TabRuler.
     * @return the trailing margin of this TabRuler
     */
    public final int getTrailingMargin() {
        return fImpl.getTrailingMargin();
    }

    public void paint(Graphics g) {
        fImpl.paint(g);
    }

    public Dimension getPreferredSize() {
        return fImpl.getPreferredSize();
    }

    public Dimension getMinimumSize() {
        return fImpl.getMinimumSize();
    }
}
