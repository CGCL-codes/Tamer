package fr.amille.animebrowser.view.buttons;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import fr.amille.animebrowser.model.util.ViewColorConstant;

/**
 * @author amille
 * 
 */
@SuppressWarnings("serial")
public class LanguageComboBoxRender extends JLabel implements ListCellRenderer {

    private Color mycolor;

    private int myindex;

    private boolean isselected;

    public LanguageComboBoxRender() {
        this.setOpaque(true);
        this.mycolor = null;
    }

    @Override
    public Color getBackground() {
        if (this.myindex >= 0) {
            if (this.isselected) {
                return ViewColorConstant.GRAY;
            } else {
                return Color.lightGray;
            }
        } else {
            return Color.lightGray;
        }
    }

    @Override
    public Color getForeground() {
        if (this.myindex >= 0) {
            if (this.isselected) {
                return Color.white;
            } else {
                return Color.black;
            }
        } else {
            return Color.black;
        }
    }

    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        if (value instanceof String) {
            this.setText((String) value);
        }
        this.myindex = index;
        this.isselected = isSelected;
        this.setForeground(this.mycolor);
        this.setBorder(null);
        return this;
    }

    @Override
    public void setBackground(final Color col) {
        super.setBackground(col);
    }

    @Override
    public void setForeground(final Color col) {
        super.setForeground(col);
        if (this.mycolor == null) {
            this.mycolor = col;
        }
    }
}
