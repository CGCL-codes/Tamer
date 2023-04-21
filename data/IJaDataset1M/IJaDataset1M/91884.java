package poweria.guia.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import poweria.joueur.Equipe;

/**
 *
 * @author Cody Stoutenburg
 */
public class CellRendererEquipe extends JLabel implements ListCellRenderer {

    private int _iconWidth;

    private int _iconHeigth;

    public CellRendererEquipe() {
        _iconWidth = 20;
        _iconHeigth = 20;
    }

    public CellRendererEquipe(int width, int heigth) {
        _iconWidth = width;
        _iconHeigth = heigth;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Equipe item = (Equipe) value;
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        BufferedImage bi = new BufferedImage(_iconWidth, _iconHeigth, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                bi.setRGB(i, j, Color.BLACK.getRGB());
            }
        }
        for (int i = 2; i < _iconWidth - 2; i++) {
            for (int j = 2; j < _iconHeigth - 2; j++) {
                bi.setRGB(i, j, item.getCouleur().getRGB());
            }
        }
        for (int i = _iconWidth - 2; i < _iconWidth; i++) {
            for (int j = _iconHeigth - 2; j < _iconHeigth; j++) {
                bi.setRGB(i, j, Color.BLACK.getRGB());
            }
        }
        ImageIcon icon = new ImageIcon(bi);
        String color = item.toString();
        setIcon(icon);
        setText(color);
        setFont(list.getFont());
        return this;
    }
}
