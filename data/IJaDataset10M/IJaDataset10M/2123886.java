package com.wizzer.m3g.lcdui;

/**
 * This class emulates the J2ME <code>javax.microedition.lcdui.Graphics</code>
 * class.
 * 
 * @author Mark Millard
 */
public class Graphics {

    public static Graphics getGraphics(Object img) {
        return new Graphics();
    }
}
