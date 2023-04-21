package org.jlense.uiworks.widget.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.jlense.uiworks.internal.WorkbenchPlugin;
import org.jlense.uiworks.widget.WidgetUtils;

/**
*   Renders an indented cell value.
*   This renderer is typically used to render an indented value in the 
*   first column of a table that is displaying a hierarchial set of objects.  
*   The indentation produces a tree-like display that provides an indication of 
*   the hierarchy of the displayed objects.
*   
*   @author ted stockwell [emorning@sourceforge.net]
*/
public class TreeListCellRenderer extends BasicCellRenderer implements TableCellRenderer {

    private static WorkbenchPlugin PLUGIN = WorkbenchPlugin.getDefault();

    public static final TreeListCellRenderer sharedRenderer = new TreeListCellRenderer();

    private TreeListItemLabel _rendererComponent = new TreeListItemLabel();

    /**
     * @param value This method expects to be passed an TreeListItem instance.
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        TreeListItem item = (TreeListItem) value;
        _rendererComponent.setText((String) item.getValue());
        _rendererComponent.setIndentLevel(item.getDepth());
        return super.getListCellRendererComponent(list, _rendererComponent, index, isSelected, hasFocus);
    }

    /**
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
     * 
     * @param value This method expects to be passed an TreeListItem instance.
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        TreeListItem item = (TreeListItem) value;
        _rendererComponent.setText((String) item.getValue());
        _rendererComponent.setIndentLevel(item.getDepth());
        hasFocus = false;
        if (table.hasFocus()) if (table.getSelectedRow() == row) hasFocus = true;
        Color colorForeground;
        Color colorBackground;
        if (isSelected) {
            if (hasFocus) {
                colorBackground = table.getSelectionBackground();
                colorForeground = table.getSelectionForeground();
            } else {
                colorForeground = SystemColor.controlText;
                colorBackground = SystemColor.control;
            }
            setBackground(colorBackground);
            setForeground(colorForeground);
        } else {
            colorBackground = table.getBackground();
            colorForeground = table.getForeground();
        }
        _rendererComponent.setBackground(colorBackground);
        _rendererComponent.setForeground(colorForeground);
        _rendererComponent.setEnabled(table.isEnabled());
        _rendererComponent.setFont(table.getFont());
        _rendererComponent.setBorder(getBorder(hasFocus));
        return _rendererComponent;
    }

    static class TreeListItemLabel extends JPanel {

        static ImageIcon _imageIcon = null;

        GridBagConstraints _gbc = new GridBagConstraints();

        int _spacerWidth = Toolkit.getDefaultToolkit().getFontMetrics(new JLabel().getFont()).getMaxAdvance();

        JLabel _spacer = new JLabel();

        JLabel _iconLabel = new JLabel();

        JLabel _spaceLabel = new JLabel(" ");

        JLabel _textLabel = new JLabel();

        TreeListItemLabel() {
            super(new GridBagLayout());
            if (_imageIcon == null) {
                String iconName = PLUGIN.bind("org.jlense.uiworks.renderer.chiclet");
                try {
                    _imageIcon = WidgetUtils.getImageIconResource(PLUGIN.getDescriptor().getPluginClassLoader(), iconName);
                } catch (IOException x) {
                    PLUGIN.logError("Failed to load icon " + iconName + ".", x);
                }
            }
            if (_imageIcon != null) _iconLabel = new JLabel(_imageIcon);
            setIndentLevel(0);
        }

        void setText(String text) {
            _textLabel.setText(text);
        }

        void setIndentLevel(int indentLevel) {
            removeAll();
            _gbc.anchor = _gbc.WEST;
            _gbc.gridx = 0;
            _gbc.weightx = 0.0d;
            _gbc.ipadx += (_spacerWidth * indentLevel) / 2;
            add(_spacer, _gbc);
            if (_iconLabel != null) {
                _gbc.gridx = 1;
                _gbc.ipadx = 0;
                add(_iconLabel, _gbc);
            }
            _gbc.gridx = 3;
            _gbc.weightx = 1.0d;
            add(_textLabel, _gbc);
        }

        /**
         * @see java.awt.Component#setForeground(Color)
         */
        public void setForeground(Color c) {
            super.setForeground(c);
            if (_textLabel != null) _textLabel.setForeground(c);
        }
    }
}
