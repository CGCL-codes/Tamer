package com.zhiyun.estore.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;

public class AuthCodeGenerator {

    private char mapTable[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    public String draw(int width, int height, OutputStream os) {
        if (width <= 0) width = 60;
        if (height <= 0) height = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xFCF000));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        String strEnsure = "";
        for (int i = 0; i < 4; ++i) {
            strEnsure += mapTable[(int) (mapTable.length * Math.random())];
        }
        g.setColor(Color.black);
        g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
        String str = strEnsure.substring(0, 1);
        g.drawString(str, 8, 17);
        str = strEnsure.substring(1, 2);
        g.drawString(str, 20, 15);
        str = strEnsure.substring(2, 3);
        g.drawString(str, 35, 18);
        str = strEnsure.substring(3, 4);
        g.drawString(str, 45, 15);
        Random rand = new Random();
        for (int i = 0; i < 50; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            g.drawOval(x, y, 1, 1);
        }
        g.dispose();
        try {
            ImageIO.write(image, "JPEG", os);
        } catch (IOException e) {
            return "";
        }
        return strEnsure;
    }
}
