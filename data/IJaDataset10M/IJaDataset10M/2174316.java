package com.pcmsolutions.gui.desktop;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * User: paulmeehan
 * Date: 16-Feb-2004
 * Time: 17:04:37
 */
public class DesktopBranch implements Serializable {

    DesktopElement[] desktopElements;

    private String title;

    private static final String UNTITLED = "Untitled";

    public DesktopBranch(DesktopElement[] desktopElements) {
        this(desktopElements, UNTITLED);
    }

    public DesktopBranch(DesktopElement[] desktopElements, String title) {
        this.desktopElements = (DesktopElement[]) desktopElements.clone();
        this.title = title;
        if (!validateInOrder(desktopElements)) throw new IllegalArgumentException("DesktopBranch: DesktopElements must be in-order");
    }

    public static boolean validateInOrder(DesktopElement[] elems) {
        return true;
    }

    public List asList() {
        return Arrays.asList((Object[]) desktopElements.clone());
    }

    public String getTitle() {
        return title;
    }

    public DesktopElement[] getDesktopElements() {
        return (DesktopElement[]) desktopElements.clone();
    }

    public DesktopElement getRoot() {
        return desktopElements[0];
    }

    public int count() {
        return desktopElements.length;
    }
}
