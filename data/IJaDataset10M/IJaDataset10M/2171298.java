package com.limegroup.gnutella.gui;

import java.awt.IllegalComponentStateException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.limewire.service.ErrorService;

/**
 * @author jum
 *
 * Implement a generic error handler that catches all errors thrown
 * by ActionListeners in the AWT event dispatcher thread.
 */
public class DefaultErrorCatcher {

    static void install() {
        System.setProperty("sun.awt.exception.handler", DefaultErrorCatcher.class.getName());
    }

    public void handle(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        pw.flush();
        String bug = sw.toString();
        if (!isIgnorable(ex, bug)) ErrorService.error(ex, "Uncaught event-thread error."); else {
            System.err.println("Ignoring error:");
            ex.printStackTrace();
        }
    }

    /**
	 * Determines if the message can be ignored.
	 */
    private boolean isIgnorable(Throwable bug, String msg) {
        if (bug instanceof StackOverflowError) return true;
        if (bug instanceof OutOfMemoryError) return true;
        if (msg == null) return true;
        if (msg.indexOf("javax.swing.RepaintManager") != -1) return true;
        if (msg.indexOf("sun.awt.RepaintArea.paint") != -1) return true;
        if (bug instanceof ArrayIndexOutOfBoundsException) {
            if (msg.indexOf("apple.awt.CWindow.displayChanged") != -1) return true;
            if (msg.indexOf("javax.swing.plaf.basic.BasicTabbedPaneUI.getTabBounds") != -1) return true;
        }
        if (bug instanceof IllegalStateException) {
            if (msg.indexOf("cannot open system clipboard") != -1) return true;
        }
        if (bug instanceof IllegalComponentStateException) {
            if (msg.indexOf("component must be showing on the screen to determine its location") != -1) return true;
        }
        if (bug instanceof NullPointerException) {
            if (msg.indexOf("MetalFileChooserUI") != -1) return true;
            if (msg.indexOf("WindowsFileChooserUI") != -1) return true;
            if (msg.indexOf("AquaDirectoryModel") != -1) return true;
            if (msg.indexOf("SizeRequirements.calculateAlignedPositions") != -1) return true;
            if (msg.indexOf("BasicTextUI.damageRange") != -1) return true;
            if (msg.indexOf("null pData") != -1) return true;
            if (msg.indexOf("disposed component") != -1) return true;
            if (msg.indexOf("javax.swing.JComponent.repaint") != -1 && msg.indexOf("com.limegroup.gnutella.gui.FileChooserHandler.getSaveAsFile") != -1) {
                return true;
            }
            if (msg.indexOf("javax.swing.JComponent.repaint") != -1 && msg.indexOf("com.limegroup.gnutella.gui.FileChooserHandler.getInput") != -1) {
                return true;
            }
        }
        if (bug instanceof IndexOutOfBoundsException) {
            if (msg.indexOf("Invalid index") != -1 && msg.indexOf("com.limegroup.gnutella.gui.FileChooserHandler.getSaveAsFile") != -1) {
                return true;
            }
            if (msg.indexOf("Invalid index") != -1 && msg.indexOf("com.limegroup.gnutella.gui.FileChooserHandler.getInput") != -1) {
                return true;
            }
        }
        if (bug instanceof InternalError) {
            if (msg.indexOf("getGraphics not implemented for this component") != -1) return true;
        }
        if (msg.indexOf("com.limegroup.gnutella") == -1 && msg.indexOf("org.limewire") == -1) return true;
        if (intercepts(msg, "com.limegroup.gnutella.tables.MouseEventConsumptionChecker")) return true;
        if (intercepts(msg, "com.limegroup.gnutella.gui.tables.LimeJTable.processMouseEvent")) return true;
        return false;
    }

    /**
     * Determines if the given string is the only place where 'com.limegroup.gnutella' exists.
     */
    private boolean intercepts(String msg, String inter) {
        int i = msg.indexOf(inter);
        if (i == -1) return false;
        if (msg.lastIndexOf("com.limegroup.gnutella", i) != -1 && msg.lastIndexOf("org.limewire", i) != -1) return false;
        i += inter.length();
        if (i >= msg.length()) return false;
        if (msg.indexOf("com.limegroup.gnutella", i) != -1 && msg.lastIndexOf("org.limewire", i) != -1) return false;
        return true;
    }
}
