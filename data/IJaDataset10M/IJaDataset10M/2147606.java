package org.wings;

import java.io.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision: 1759 $
 */
public class SFont implements SConstants, Serializable {

    private static final boolean DEBUG = true;

    /**
     * TODO: documentation
     */
    protected int type = FONT;

    /**
     * TODO: documentation
     */
    protected int style = PLAIN;

    /**
     * TODO: documentation
     */
    protected String face = null;

    /**
     * TODO: documentation
     */
    protected int size = Integer.MIN_VALUE;

    /**
     * TODO: documentation
     *
     */
    public SFont() {
    }

    public SFont(String face, int style, int size) {
        setFace(face);
        setStyle(style);
        setSize(size);
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setType(int t) {
        type = t;
    }

    /**
     * TODO: documentation
     *
     * @param f
     */
    public void setFace(String f) {
        face = f;
        if (face != null && face.trim().length() == 0) face = null;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getFace() {
        return face;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setStyle(int s) {
        style = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getStyle() {
        return style;
    }

    /**
     * TODO: documentation
     *
     * @param s
     */
    public void setSize(int s) {
        size = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getSize() {
        return size;
    }
}
