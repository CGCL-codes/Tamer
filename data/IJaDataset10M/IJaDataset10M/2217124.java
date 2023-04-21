package com.javable.dataview.cursors;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.javable.dataview.DataView;
import com.javable.dataview.ResourceManager;

/**
 * Class that puts up data cursors
 * 
 * @see DataView
 */
public class CursorProxy implements javax.swing.event.TreeModelListener, javax.swing.event.ChangeListener {

    private DataView view;

    private CursorSlider cursors;

    private JMenu popup;

    /**
     * Default constructor
     * 
     * @param v data view
     */
    public CursorProxy(DataView v) {
        view = v;
        view.getScrollPane().getViewport().addChangeListener(this);
        view.getStorage().addTreeModelListener(this);
        cursors = new CursorSlider(4, view);
        cursors.addMouseListener(view);
        cursors.addMouseMotionListener(view);
        view.getScrollPane().getViewport().addChangeListener(((MetalCursorSliderUI) cursors.getUI()).cursorChangeListener);
    }

    /**
     * Adds a pair of cursors
     */
    public void addCursorsPair() {
        cursors.addThumb();
        cursors.addThumb();
    }

    /**
     * Returns current cursors
     * 
     * @return cursors
     */
    public CursorSlider getCursorSlider() {
        return cursors;
    }

    /**
     * Changes the limits when size of the component changes
     */
    public void changeLimits() {
        if ((view.getStorage() == null) || (view.getStorage().getGroupsSize() == 0)) return;
        java.awt.Dimension size = cursors.getPreferredSize();
        java.awt.Dimension thumb = ((CursorSliderAdditional) cursors.getUI()).getThumbSize();
        int width = view.getPreferredSize().width + thumb.width;
        int height = view.getPreferredSize().height;
        if ((size.width != width) || (size.height != height)) cursors.setPreferredSize(new Dimension(width, height));
        int min = (int) Math.min(view.getStorage().getLimit("MinX"), view.getScale().getX());
        int max = (int) Math.max(view.getStorage().getLimit("MaxX"), view.getScale().getX() + view.getScale().getWidth());
        cursors.setMinimum(min);
        cursors.setMaximum(max);
    }

    private void changeRange() {
        changeLimits();
        cursors.distributeThumbs(cursors.getMinimum(), cursors.getMaximum());
    }

    /**
     * Distributes cursors in the current view
     */
    public void bringCursorsHere() {
        cursors.distributeThumbs((int) (view.getScale().getX()), (int) (view.getScale().getX() + view.getScale().getWidth()));
    }

    /**
     * Creates cursor-related popup menu
     * 
     * @return popup menu
     */
    public JMenu createPopup() {
        JCheckBoxMenuItem c_menu = null;
        JMenuItem m_menu = null;
        if (popup == null) {
            try {
                popup = new JMenu(ResourceManager.getResource("Cursors"));
                c_menu = new JCheckBoxMenuItem(ResourceManager.getResource("Show"));
                c_menu.setSelected(cursors.isVisible());
                c_menu.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        cursors.setVisible(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    }
                });
                popup.add(c_menu);
                popup.addSeparator();
                m_menu = new JMenuItem(ResourceManager.getResource("Bring_here"));
                m_menu.setAccelerator(KeyStroke.getKeyStroke(' ', 1));
                m_menu.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        bringCursorsHere();
                    }
                });
                popup.add(m_menu);
                c_menu = new JCheckBoxMenuItem(ResourceManager.getResource("Lock_to_data"));
                c_menu.setSelected(cursors.isLocked());
                c_menu.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        cursors.setLocked(((JCheckBoxMenuItem) e.getSource()).isSelected());
                        cursors.repaint();
                    }
                });
                popup.add(c_menu);
            } catch (Exception e) {
            }
        }
        return popup;
    }

    /**
     * @param evt
     */
    public void stateChanged(javax.swing.event.ChangeEvent evt) {
        if (evt.getSource() == view.getScrollPane().getViewport()) changeLimits();
    }

    /**
     * @param evt
     */
    public void treeStructureChanged(javax.swing.event.TreeModelEvent evt) {
    }

    /**
     * @param evt
     */
    public void treeNodesInserted(javax.swing.event.TreeModelEvent evt) {
        if (evt.getTreePath().getPathCount() <= 2) changeRange();
    }

    /**
     * @param evt
     */
    public void treeNodesRemoved(javax.swing.event.TreeModelEvent evt) {
        if (evt.getTreePath().getPathCount() <= 2) changeRange();
    }

    /**
     * @param evt
     */
    public void treeNodesChanged(javax.swing.event.TreeModelEvent evt) {
    }
}
