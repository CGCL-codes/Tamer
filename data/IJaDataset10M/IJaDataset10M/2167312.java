package edu.isi.div2.metadesk.gui;

import java.awt.Component;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import edu.isi.div2.metadesk.action.ColorizeContexts;
import edu.isi.div2.metadesk.datamodel.*;
import edu.isi.div2.metadesk.quad.Quad;

/**
 * Custom Renderer for the Attributes-Value Table.
 * 
 * @author Sameer Maggon
 * @version $Id: AttrValueTableCellRenderer.java,v 1.1 2005/05/24 16:34:18 maggon Exp $
 */
public class AttrValueTableCellRenderer extends DefaultTableCellRenderer {

    /** font size for the table cells */
    private int fontSize = 12;

    private JButton rowHeaderButton = new JButton();

    private JButton editorButton = new JButton("...");

    /**
	 *  
	 */
    public AttrValueTableCellRenderer() {
        super();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setOpaque(true);
        this.setForeground(Color.BLACK);
        this.setFont(this.getFont().deriveFont(Font.PLAIN, fontSize));
        if (column == 0 && null != value) {
            rowHeaderButton.setText(value.toString());
            rowHeaderButton.setFont(UIManager.getFont("TableHeader.font"));
            rowHeaderButton.setForeground(UIManager.getColor("TableHeader.foreground"));
            rowHeaderButton.setBackground(UIManager.getColor("TableHeader.background"));
            rowHeaderButton.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            rowHeaderButton.setMargin(new Insets(0, 0, 0, 0));
            return rowHeaderButton;
        } else if (column == 3 && null != value) {
            editorButton.setToolTipText("Open Editor");
            return editorButton;
        }
        this.setBackground(Color.WHITE);
        this.setHorizontalAlignment(JTextField.LEFT);
        this.setVerticalAlignment(JLabel.TOP);
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        Object o = AttrValueTableModel.friendlyRendering(value);
        String label = "";
        if (o != null) {
            this.setToolTipText(o.toString());
            if (o instanceof String) {
                label = (String) o;
            } else if (o instanceof Term) {
                String temp = o.toString();
                if (null == temp || "null".equalsIgnoreCase(temp)) {
                    label = "";
                } else {
                    label = iw.getFriendlyLabel(((Term) o).getURI(), true);
                    if (column == 2) {
                        if (ColorizeContexts.COLORIZE_CONTEXTS) label = "<html><u>" + label + "</u></html>"; else label = "<html><u><font color='blue'>" + label + "</font></u></html>";
                    }
                }
            }
            this.setText(label);
        } else {
            this.setText("");
        }
        if ((column == 1) && !ColorizeContexts.COLORIZE_CONTEXTS && RDFPropertyLookup.world().getPropertyFromLabel((String) table.getValueAt(row, 1)) != null) {
            this.setBackground(new Color(255, 250, 204));
        } else if ((column == 2) && !ColorizeContexts.COLORIZE_CONTEXTS) {
            Object instance = table.getValueAt(row, 2);
            if (instance instanceof Term) this.setBackground(new Color(244, 238, 224));
        }
        AttrValueTableModel model = (AttrValueTableModel) table.getModel();
        if ((column == 1) && ColorizeContexts.COLORIZE_CONTEXTS && (row < model.getAttributeQuads().size())) {
            Quad quad = (Quad) model.getAttributeQuads().get(row);
            String context = quad.getContext();
            this.setText("<html><font color='" + ColorizeContexts.getContextHTMLColor(context) + "'>" + label + "</font></html>");
        }
        {
            AttrValueTableModel model2 = (AttrValueTableModel) table.getModel();
            if (row < model.getAttributeQuads().size()) {
                String uri = ((Term) model2.getInstances().get(0)).getURI();
                Quad quad = (Quad) model.getAttributeQuads().get(row);
                String sub = quad.getSubject();
                if (!uri.equals(sub)) this.setBackground(new Color(240, 240, 240));
            }
        }
        if (isSelected || hasFocus) {
            this.setForeground(table.getSelectionForeground());
            this.setBackground(table.getSelectionBackground());
        }
        if (this.getPreferredSize().height > table.getRowHeight(row)) {
            table.setRowHeight(row, this.getPreferredSize().height);
        }
        return this;
    }

    /**
	 * sets the font size of the table cells
	 * 
	 * @param size
	 *            size of the font
	 */
    public void setFontSize(int size) {
        fontSize = size;
    }

    /**
	 * Returns the current font size of the table cells
	 * 
	 * @return current size of the font
	 */
    public int getFontSize() {
        return fontSize;
    }
}
