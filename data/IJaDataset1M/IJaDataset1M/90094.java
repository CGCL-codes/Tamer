package com.memoire.bu;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 * A tool bar with simplified adding methods.
 */
public class BuSpecificBar extends JToolBar {

    int w_;

    int h_;

    boolean toolFocusable_;

    public BuSpecificBar() {
        super();
        setName("tbSPECIFIC_TOOLBAR");
        addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent _evt) {
                if ("ancestor".equals(_evt.getPropertyName())) {
                    w_ = 0;
                    h_ = 0;
                }
            }
        });
    }

    public void updateUI() {
        w_ = h_ = 0;
        super.updateUI();
    }

    public boolean isFocusCycleRoot() {
        return true;
    }

    /**
   * Definit que les tools sont focusable. Dans le cas d'une combobox,
   * ceci permet de naviguer d'item en item directement depuis le clavier.
   * @param _b True : Les tools peuvent obtenir le focus.
   */
    public void setToolsFocusable(boolean _b) {
        toolFocusable_ = _b;
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i) instanceof JComponent) ((JComponent) getComponent(i)).setRequestFocusEnabled(_b);
        }
    }

    public void addTool(JComponent _tool) {
        if (_tool == null) addSeparator(); else {
            _tool.setVisible(true);
            _tool.setRequestFocusEnabled(toolFocusable_);
            if (_tool instanceof JButton) {
                ((JButton) _tool).setMargin(BuInsets.INSETS1111);
                ((JButton) _tool).setRolloverEnabled(true);
            }
            if (_tool instanceof JToggleButton) {
                ((JToggleButton) _tool).setMargin(BuInsets.INSETS1111);
                ((JToggleButton) _tool).setRolloverEnabled(true);
            }
            add(_tool);
        }
    }

    public void addTools(JComponent[] _tools) {
        if ((_tools == null) || (_tools.length == 0)) return;
        boolean sep = false;
        for (int i = 0; i < _tools.length; i++) {
            if (_tools[i] == null) {
                if (sep) continue;
                sep = true;
            } else sep = false;
            addTool(_tools[i]);
        }
        revalidate();
        repaint();
    }

    public void removeTool(JComponent _tool) {
        if (_tool == null) {
            Component c = getComponentAtIndex(0);
            if (c instanceof JToolBar.Separator) remove(c);
        } else {
            _tool.setVisible(false);
            remove(_tool);
        }
    }

    public void removeTools(JComponent[] _tools) {
        if ((_tools == null) || (_tools.length == 0)) return;
        boolean sep = false;
        for (int i = 0; i < _tools.length; i++) {
            if (_tools[i] == null) {
                if (sep) continue;
                sep = true;
            } else sep = false;
            removeTool(_tools[i]);
        }
        revalidate();
    }

    public Dimension getPreferredSize() {
        Dimension d = null;
        try {
            d = super.getPreferredSize();
            if (d.height > d.width) {
                if (d.width < w_) d.width = w_; else w_ = d.width;
            } else {
                if (d.height < h_) d.height = h_; else h_ = d.height;
            }
        } catch (NullPointerException ex) {
        }
        return d;
    }
}
