package net.adrianromero.tpv.ticket;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import net.adrianromero.tpv.util.ThumbNailBuilder;
import net.adrianromero.format.Formats;

public class ProductRenderer extends DefaultListCellRenderer {

    ThumbNailBuilder tnbprod;

    /** Creates a new instance of ProductRenderer */
    public ProductRenderer() {
        Image defimg;
        try {
            defimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("net/adrianromero/images/colorize.png"));
        } catch (Exception fnfe) {
            defimg = null;
        }
        tnbprod = new ThumbNailBuilder(64, 32, defimg);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
        ProductInfoExt prod = (ProductInfoExt) value;
        if (prod != null) {
            setText("<html>" + prod.getReference() + " - " + prod.getCode() + " - " + prod.getName() + "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + prod.getTaxName() + " " + Formats.CURRENCY.formatValue(new Double(prod.getPriceSell())));
            Image img = tnbprod.getThumbNail(prod.getImage());
            setIcon(img == null ? null : new ImageIcon(img));
        }
        return this;
    }
}
