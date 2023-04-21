package no.ugland.utransprod.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentListener;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameListener;

/**
 * Klasse som brukes sammen med JInternalFrameAdapter for � kunne brukes om
 * hverandre. Stort sett alle metoder her er metoder som kj�res direkte p� den
 * dialogen som blir adoptert
 * @author atle.brekka
 */
public class JDialogAdapter implements WindowInterface {

    /**
     * 
     */
    private JDialog dialog;

    /**
     * @param dialog
     *            dialogen som skal adapteres
     */
    public JDialogAdapter(JDialog dialog) {
        this.dialog = dialog;
        this.dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#dispose()
     */
    public void dispose() {
        dialog.dispose();
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#setCursor(java.awt.Cursor)
     */
    public void setCursor(Cursor cursor) {
        dialog.setCursor(cursor);
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#add(java.awt.Component)
     */
    public Component add(Component component) {
        return dialog.add(component);
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#pack()
     */
    public void pack() {
        dialog.pack();
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#getSize()
     */
    public Dimension getSize() {
        return dialog.getSize();
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#getToolkit()
     */
    public Toolkit getToolkit() {
        return dialog.getToolkit();
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#setLocation(int, int)
     */
    public void setLocation(int x, int y) {
        dialog.setLocation(x, y);
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#setVisible(boolean)
     */
    public void setVisible(boolean aFlag) {
        dialog.setVisible(aFlag);
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#getComponent()
     */
    public Component getComponent() {
        return dialog;
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#add(java.awt.Component,
     *      java.lang.Object)
     */
    public void add(Component comp, Object constraints) {
        dialog.add(comp, constraints);
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#setLocation(java.awt.Point)
     */
    public void setLocation(Point point) {
        dialog.setLocation(point);
    }

    /**
     * Denne gjelder ikke for JDialog s� her blir ingenting kj�rt
     * @see no.ugland.utransprod.gui.WindowInterface#setSelected(boolean)
     */
    public void setSelected(boolean selected) {
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#setName(java.lang.String)
     */
    public void setName(String name) {
        dialog.setName(name);
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#validate()
     */
    public void validate() {
        dialog.validate();
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#getRootPane()
     */
    public JRootPane getRootPane() {
        return dialog.getRootPane();
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#getTitle()
     */
    public String getTitle() {
        return dialog.getTitle();
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#addInternalFrameListener(javax.swing.event.InternalFrameListener)
     */
    public void addInternalFrameListener(InternalFrameListener internalFrameListener) {
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#setSize(java.awt.Dimension)
     */
    public void setSize(Dimension size) {
        dialog.setSize(size);
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#isAdded()
     */
    public boolean isAdded() {
        return false;
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#setAdded(boolean)
     */
    public void setAdded(boolean added) {
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#addComponentListener(java.awt.event.ComponentListener)
     */
    public void addComponentListener(ComponentListener componentListener) {
        dialog.addComponentListener(componentListener);
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#cleanUp()
     */
    public void cleanUp() {
        dialog.dispose();
    }

    /**
     * @see no.ugland.utransprod.gui.WindowInterface#getJDialog()
     */
    public JDialog getJDialog() {
        return dialog;
    }

    public void remove(Component comp) {
        dialog.remove(comp);
    }

    public void setTitle(String title) {
        dialog.setTitle(title);
    }
}
