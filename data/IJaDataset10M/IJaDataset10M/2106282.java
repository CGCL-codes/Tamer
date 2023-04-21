package brgm.mapreader.operations.operators;

import java.awt.Color;
import java.awt.image.BufferedImage;
import brgm.mapreader.Image;
import brgm.mapreader.operations.ParameteredBinaryOperation;
import brgm.mapreader.parameters.IntParameter;

public class AddOperator extends ParameteredBinaryOperation {

    public AddOperator() {
        super("Addition d'image");
        IntParameter alphaCompo = new IntParameter("Coefficient d'ajout", 0, 100, 50);
        params.add(0, alphaCompo);
    }

    public IntParameter getCompositionParameter() {
        return (IntParameter) params.get(0);
    }

    public void doExecute(Image img1, Image img2) {
        BufferedImage bi1 = img1.getBufferedImage();
        BufferedImage bi2 = img2.getBufferedImage();
        BufferedImage newImg = new BufferedImage(bi1.getWidth(), bi1.getHeight(), bi1.getType());
        float alpha = getCompositionParameter().getValue() / 100.0f;
        int x, y;
        Color c1, c2;
        int r1, g1, b1, r2, g2, b2;
        int r, g, b;
        int newRGB;
        for (x = 0; x < bi1.getWidth(); x++) {
            for (y = 0; y < bi1.getHeight(); y++) {
                c1 = new Color(bi1.getRGB(x, y));
                c2 = new Color(bi2.getRGB(x, y));
                r1 = c1.getRed();
                g1 = c1.getGreen();
                b1 = c1.getBlue();
                r2 = (int) (alpha * c2.getRed());
                g2 = (int) (c2.getGreen());
                b2 = (int) (c2.getBlue());
                r = Math.min(r1 + r2, 255);
                g = Math.min(g1 + g2, 255);
                b = Math.min(b1 + b2, 255);
                newRGB = (r << 16) | (g << 8) | b;
                newImg.setRGB(x, y, newRGB);
            }
        }
        this.setImage(new Image(newImg));
    }
}
