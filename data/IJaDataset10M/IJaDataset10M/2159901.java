package com.openbravo.pos.printer.ticket;

import com.openbravo.pos.printer.printer.PrinterBook;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author jaroslawwozniak
 */
public class PrintItemImageForPrinter extends PrintItemImage {

    private int imageWidthForPrinter;

    private int imageHeightForPrinter;

    private final int V_GAP = 10;

    private final int H_GAP = 10;

    public PrintItemImageForPrinter(BufferedImage image) {
        super(image);
        imageWidthForPrinter = (int) (image.getWidth() / (3.0 / 2));
        imageHeightForPrinter = (int) (image.getHeight() / (3.0 / 2));
    }

    public void draw(Graphics2D g, int x, int y, int width) {
        if (PrinterBook.isReceiptPrinter) {
            g.drawImage(image, (width - imageWidthForPrinter) / 2 - H_GAP, y, imageWidthForPrinter, imageHeightForPrinter, null);
        } else {
            g.drawImage(image, 72 + (width - imageWidthForPrinter) / 2 - H_GAP, y, imageWidthForPrinter, imageHeightForPrinter, null);
        }
    }

    public int getHeight() {
        return imageHeightForPrinter + V_GAP;
    }
}
