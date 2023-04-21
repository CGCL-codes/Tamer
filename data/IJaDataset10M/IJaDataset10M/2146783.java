package test.fullTests.recolorTest;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author Stefan
 * @since 2.0
 */
public class PropertyRecolorPanel extends JPanel {

    private static int IMG_COLS = 2;

    private List<List<BufferedImage>> propertyImages;

    protected void paintComponent(Graphics g) {
        if (propertyImages == null) return;
        Graphics2D copy = (Graphics2D) g.create();
        paintAllUnitsInoneRow(copy);
    }

    public void paintAllproperties(Graphics2D g) {
        int colCount = 0;
        for (List<BufferedImage> imgRow : propertyImages) {
            for (BufferedImage img : imgRow) {
                g.drawImage(img, 0, 0, this);
                if (++colCount >= IMG_COLS) {
                    g.translate(-IMG_COLS * 32, 32);
                    colCount = 0;
                }
                g.translate(32, 0);
            }
        }
    }

    private void paintAllUnitsInoneRow(Graphics2D g) {
        List<BufferedImage> imgRow = tools.MatrixUtil.grabCol(propertyImages, 1);
        for (BufferedImage img : imgRow) {
            g.drawImage(img, 0, 0, this);
            g.translate(32, 0);
        }
    }

    public void setpropertyImages(List<List<BufferedImage>> propertyImages) {
        this.propertyImages = propertyImages;
    }

    public Dimension getPreferredSize() {
        return new Dimension(100, 500);
    }
}
