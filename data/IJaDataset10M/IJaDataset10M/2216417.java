package com.patientis.framework.controls;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Component;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import com.patientis.framework.controls.table.IRenderTableCell;
import com.patientis.framework.locale.FormatUtil;
import com.patientis.framework.logging.Log;
import com.patientis.framework.controls.IComponentPainter;
import com.patientis.model.common.Converter;

/**
 * TextRenderer provides rendering for a table cell.  Based on the DefaultCellRenderer.
 * Source is from DefaultTableCellRenderer pasted here to highlight how the rendering occurs.
 * <p>
 * <pre>
 * (#)DefaultTableCellRenderer.java	1.42 05/10/31
 *
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * </pre>
 * </p>
 * <br/>
 */
public class ISCellLabel extends JLabel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    /**
	 * 
	 */
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

    /**
	 * 
	 */
    private static final ISCheckBox checkBox = new ISCheckBox();

    /**
	 * Unselected color
	 */
    private Color unselectedForeground;

    /**
	 * Unselected color
	 */
    private Color unselectedBackground;

    /**
	 * Creates a default table cell renderer.
	 */
    public ISCellLabel() {
        super();
        setOpaque(true);
        setBorder(getNoFocusBorder());
        if (FormatUtil.hasDefaultFont()) setFont(FormatUtil.getDefaultFont());
    }

    /**
	 * Creates a default table cell renderer.
	 */
    public ISCellLabel(String text) {
        super(text);
        setOpaque(true);
        setBorder(getNoFocusBorder());
        if (FormatUtil.hasDefaultFont()) setFont(FormatUtil.getDefaultFont());
    }

    /**
	 * Border
	 * 
	 * @return
	 */
    private static Border getNoFocusBorder() {
        if (System.getSecurityManager() != null) {
            return SAFE_NO_FOCUS_BORDER;
        } else {
            return noFocusBorder;
        }
    }

    /**
	 * Overrides <code>JComponent.setForeground</code> to assign
	 * the unselected-foreground color to the specified color.
	 * 
	 * @param c set the foreground color to this value
	 */
    public void setForeground(Color c) {
        super.setForeground(c);
        unselectedForeground = c;
    }

    /**
	 * Overrides <code>JComponent.setBackground</code> to assign
	 * the unselected-background color to the specified color.
	 *
	 * @param c set the background color to this value
	 */
    public void setBackground(Color c) {
        super.setBackground(c);
        unselectedBackground = c;
    }

    /**
	 * Notification from the <code>UIManager</code> that the look and feel
	 * [L&F] has changed.
	 * Replaces the current UI object with the latest version from the 
	 * <code>UIManager</code>.
	 *
	 * @see JComponent#updateUI
	 */
    public void updateUI() {
        super.updateUI();
        setForeground(null);
        setBackground(null);
    }

    /**
	 *
	 * Returns the default table cell renderer.
	 *
	 * @param table  the <code>JTable</code>
	 * @param value  the value to assign to the cell at
	 *			<code>[row, column]</code>
	 * @param isSelected true if cell is selected
	 * @param hasFocus true if cell has focus
	 * @param row  the row of the cell to render
	 * @param column the column of the cell to render
	 * @return the default table cell renderer
	 */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            super.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
            super.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
        }
        setFont(table.getFont());
        if (hasFocus) {
            Border border = null;
            if (isSelected) {
                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("Table.focusCellHighlightBorder");
            }
            setBorder(border);
            if (!isSelected && table.isCellEditable(row, column)) {
                Color col;
                col = UIManager.getColor("Table.focusCellForeground");
                if (col != null) {
                    super.setForeground(col);
                }
                col = UIManager.getColor("Table.focusCellBackground");
                if (col != null) {
                    super.setBackground(col);
                }
            }
        } else {
            setBorder(getNoFocusBorder());
        }
        try {
            setValue(value);
        } catch (Exception ex) {
            Log.exception(ex);
        }
        return this;
    }

    /**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a> 
	 * for more information.
	 */
    public boolean isOpaque() {
        Color back = getBackground();
        Component p = getParent();
        if (p != null) {
            p = p.getParent();
        }
        boolean colorMatch = (back != null) && (p != null) && back.equals(p.getBackground()) && p.isOpaque();
        return !colorMatch && super.isOpaque();
    }

    /**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a> 
	 * for more information.
	 *
	 * @since 1.5
	 */
    public void invalidate() {
    }

    /**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a> 
	 * for more information.
	 */
    public void validate() {
    }

    /**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a> 
	 * for more information.
	 */
    public void revalidate() {
    }

    /**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a> 
	 * for more information.
	 */
    public void repaint(long tm, int x, int y, int width, int height) {
    }

    /**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a> 
	 * for more information.
	 */
    public void repaint(Rectangle r) {
    }

    /**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a> 
	 * for more information.
	 *
	 * @since 1.5
	 */
    public void repaint() {
    }

    /**
	 * Overridden for performance reasons.
	 * 
	 * See the <a href="#override">Implementation Note</a> 
	 * for more information.
	 */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    }

    /**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a> 
	 * for more information.
	 */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }

    /**
	 * Sets the <code>String</code> object for the cell being rendered to
	 * <code>value</code>.
	 * 
	 * @param value  the string value for this cell; if value is
	 *		<code>null</code> it sets the text value to an empty string
	 * @see JLabel#setText
	 * 
	 */
    public void setValue(Object value) throws Exception {
        setText((value == null) ? "" : value.toString());
    }

    /**
	 * A subclass of <code>DefaultTableCellRenderer</code> that
	 * implements <code>UIResource</code>.
	 * <code>DefaultTableCellRenderer</code> doesn't implement
	 * <code>UIResource</code>
	 * directly so that applications can safely override the
	 * <code>cellRenderer</code> property with
	 * <code>DefaultTableCellRenderer</code> subclasses.
	 * <p>
	 * <strong>Warning:</strong>
	 * Serialized objects of this class will not be compatible with
	 * future Swing releases. The current serialization support is
	 * appropriate for short term storage or RMI between applications running
	 * the same version of Swing.  As of 1.4, support for long term storage
	 * of all JavaBeans<sup><font size="-2">TM</font></sup>
	 * has been added to the <code>java.beans</code> package.
	 * Please see {@link java.beans.XMLEncoder}.
	 */
    public static class UIResource extends DefaultTableCellRenderer implements javax.swing.plaf.UIResource {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;
    }

    /**
	 * @see com.patientis.framework.controls.table.IRenderTableCell#prepareToBeCellRenderer()
	 */
    public void prepareToBeCellRenderer() {
    }

    /**
	 * Override painting
	 * 
	 */
    private IComponentPainter componentPainter = null;

    /**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
    @Override
    protected void paintComponent(Graphics g) {
        if (componentPainter != null) {
            componentPainter.prePaintComponent(g, this);
        }
        super.paintComponent(g);
        if (componentPainter != null) {
            componentPainter.postPaintComponent(g, this);
        }
    }

    /**
	 * @param componentPainter the componentPainter to set
	 */
    public void setComponentPainter(IComponentPainter componentPainter) {
        this.componentPainter = componentPainter;
    }
}
