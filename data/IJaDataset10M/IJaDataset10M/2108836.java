package org.formaria.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.View;
import org.formaria.debug.DebugLogger;
import org.formaria.aria.Project;
import org.formaria.aria.ProjectManager;
import org.formaria.aria.build.BuildProperties;
import org.formaria.aria.helper.Translator;
import org.formaria.aria.helper.AriaUtilities;

/**
 * A renderer for table headers that includes translation of the header text.
 *
 * <p> Copyright (c) Formaria Ltd., 2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * <p> $Revision: 1.12 $</p>
 */
public class TableHeaderRenderer extends DefaultTableCellRenderer implements MouseListener, MouseMotionListener {

    protected Translator translator;

    private int fontHeight = 0;

    private JTableHeader tableHeader;

    private JTable table;

    private int rolloverColumn;

    private boolean focused, enabled;

    private String keyText;

    private Color bkColor;

    private Color lightColor, darkColor;

    private Color lightColorDisabled, darkColorDisabled;

    private TableBadgePainter badgePainter;

    private int column;

    /**
   * @deprecated - use other constructor
   */
    public TableHeaderRenderer(Color back, Color fore, Font font) {
        translator = ProjectManager.getCurrentProject().getTranslator();
    }

    /**
   * Create a new header renderer
   * @param th the header being rendered
   */
    public TableHeaderRenderer(JTableHeader th) {
        tableHeader = th;
        rolloverColumn = -1;
        Project currentProject = ProjectManager.getCurrentProject();
        translator = currentProject.getTranslator();
        tableHeader.addMouseListener(this);
        tableHeader.addMouseMotionListener(this);
        setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        String painterName = currentProject.getStartupParam("TableBadgePainter");
        if (painterName != null) {
            try {
                badgePainter = (TableBadgePainter) Class.forName(painterName).newInstance();
            } catch (Exception instantiationException) {
                if (BuildProperties.DEBUG) DebugLogger.logWarning("Unable to load table badge painter: " + painterName);
            }
        }
    }

    /**
   * Repaints the component when the image has changed.
   * This <code>imageUpdate</code> method of an <code>ImageObserver</code>
   * is called when more information about an
   * image which had been previously requested using an asynchronous
   * routine such as the <code>drawImage</code> method of
   * <code>Graphics</code> becomes available.
   * See the definition of <code>imageUpdate</code> for
   * more information on this method and its arguments.
   * <p>
   * The <code>imageUpdate</code> method of <code>Component</code>
   * incrementally draws an image on the component as more of the bits
   * of the image are available.
   * <p>
   * If the system property <code>awt.image.incrementaldraw</code>
   * is missing or has the value <code>true</code>, the image is
   * incrementally drawn. If the system property has any other value,
   * then the image is not drawn until it has been completely loaded.
   * <p>
   * Also, if incremental drawing is in effect, the value of the
   * system property <code>awt.image.redrawrate</code> is interpreted
   * as an integer to give the maximum redraw rate, in milliseconds. If
   * the system property is missing or cannot be interpreted as an
   * integer, the redraw rate is once every 100ms.
   * <p>
   * The interpretation of the <code>x</code>, <code>y</code>,
   * <code>width</code>, and <code>height</code> arguments depends on
   * the value of the <code>infoflags</code> argument.
   *
   * @param     img   the image being observed
   * @param     infoFlags   see <code>imageUpdate</code> for more information
   * @param     x   the <i>x</i> coordinate
   * @param     y   the <i>y</i> coordinate
   * @param     width   the width
   * @param     height   the height
   * @return    <code>false</code> if the infoflags indicate that the
   *            image is completely loaded; <code>true</code> otherwise.
   * 
   * @see     java.awt.image.ImageObserver
   * @see     Graphics#drawImage(Image, int, int, Color, java.awt.image.ImageObserver)
   * @see     Graphics#drawImage(Image, int, int, java.awt.image.ImageObserver)
   * @see     Graphics#drawImage(Image, int, int, int, int, Color, java.awt.image.ImageObserver)
   * @see     Graphics#drawImage(Image, int, int, int, int, java.awt.image.ImageObserver)
   * @see     java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int, int)
   * @since   JDK1.0
   */
    public boolean imageUpdate(Image img, int infoFlags, int x, int y, int width, int height) {
        repaint();
        if ((infoFlags & ImageObserver.ALLBITS) != 0) return false;
        return true;
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
        this.table = table;
        this.column = column;
        setValue(column, value);
        if ((column >= 0) && ((hasFocus) || (rolloverColumn == column))) {
            super.setBackground(tableHeader.getBackground().brighter());
            focused = true;
        } else {
            super.setBackground(tableHeader.getBackground());
            focused = false;
        }
        enabled = table.isEnabled();
        if (!enabled) super.setBackground(AriaUtilities.unsaturateColor(tableHeader.getBackground(), 50));
        setForeground(tableHeader.getForeground());
        return this;
    }

    /**
   * Chnage the enabled state
   * @param state the new state
   */
    public void setEnabled(boolean state) {
        super.setEnabled(state);
        bkColor = null;
    }

    /**
   * The backgound color has changed
   * @param c the new color
   */
    public void setBackground(Color c) {
        bkColor = null;
        super.setBackground(c);
    }

    /**
   * Update the graphics
   */
    public void update(Graphics g) {
        paintComponent(g);
    }

    /**
   * Paint the component
   * @param g the graphics context
   */
    public void paintComponent(Graphics g) {
        Graphics2D g2d = ((Graphics2D) g);
        Rectangle rect = getBounds();
        String key = getText();
        if (bkColor == null) {
            bkColor = getBackground();
            lightColor = bkColor.brighter();
            darkColor = bkColor.darker();
            lightColorDisabled = AriaUtilities.unsaturateColor(lightColor, 50);
            darkColorDisabled = AriaUtilities.unsaturateColor(darkColor, 50);
        }
        bkColor = getBackground();
        g.setColor(bkColor);
        if (g instanceof Graphics2D) {
            Color color1 = enabled ? lightColor : lightColorDisabled;
            Color color2 = enabled ? darkColor : darkColorDisabled;
            GradientPaint gradient = new GradientPaint(0.0F, 0.0F, color1, (float) rect.height / 2, (float) rect.width / 2, color2, true);
            g2d.setPaint(gradient);
            g2d.fill(new Rectangle(0, 0, rect.width - 1, rect.height));
        } else g.fillRect(0, 0, rect.width, rect.height);
        if (enabled && focused) {
            g.setColor(Color.green);
            g.fillRect(1, rect.height - 2, rect.width - 1, 1);
        }
        g.setColor(getForeground());
        View v = (View) getClientProperty("html");
        if (v != null) {
            Dimension sz = getSize();
            Rectangle textRect = new Rectangle(6, 4, rect.width, rect.height);
            v.paint(g, textRect);
        } else if (key != null) g.drawString(key, 6, 4 + (rect.height - fontHeight) / 2);
        if (focused) {
            g.setColor(tableHeader.getBackground().brighter().brighter());
            g.fillRect(rect.width - 2, rect.y, 2, rect.height);
        }
        if (badgePainter != null) badgePainter.paint(g2d, table, this, column, rect.width, rect.height);
    }

    /**
   * Set the header value. Translates the value if possible 
   * @param column the column index
   * @param value the raw header value
   */
    public void setValue(int column, Object value) {
        if (value != null) {
            String text = ((AdaptedTable) table).getFieldName(column, value.toString());
            if ((keyText == null) || !keyText.equals(text)) {
                keyText = text;
                if (translator != null) text = translator.translate(text);
                setText(text);
            }
        } else setText("");
    }

    private void updateRolloverColumn(MouseEvent e) {
        int col = tableHeader.columnAtPoint(e.getPoint());
        if (col != rolloverColumn) {
            rolloverColumn = col;
            tableHeader.repaint();
        }
    }

    /**
   * Invoked when the mouse button has been clicked (pressed
   * and released) on a component.
   */
    public void mouseClicked(MouseEvent e) {
    }

    /**
   * Invoked when a mouse button has been pressed on a component.
   */
    public void mousePressed(MouseEvent e) {
        rolloverColumn = -1;
        tableHeader.repaint();
    }

    /**
   * Invoked when a mouse button has been released on a component.
   */
    public void mouseReleased(MouseEvent e) {
        updateRolloverColumn(e);
    }

    /**
   * Invoked when the mouse enters a component.
   */
    public void mouseEntered(MouseEvent e) {
        updateRolloverColumn(e);
    }

    /**
   * Invoked when the mouse exits a component.
   */
    public void mouseExited(MouseEvent e) {
        rolloverColumn = -1;
        tableHeader.repaint();
    }

    /**
   * Invoked when a mouse button is pressed on a component and then 
   * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be 
   * delivered to the component where the drag originated until the 
   * mouse button is released (regardless of whether the mouse position 
   * is within the bounds of the component).
   * <p> 
   * Due to platform-dependent Drag&Drop implementations, 
   * <code>MOUSE_DRAGGED</code> events may not be delivered during a native 
   * Drag&Drop operation.  
   */
    public void mouseDragged(MouseEvent e) {
    }

    /**
   * Invoked when the mouse cursor has been moved onto a component
   * but no buttons have been pushed.
   */
    public void mouseMoved(MouseEvent e) {
        updateRolloverColumn(e);
    }
}
