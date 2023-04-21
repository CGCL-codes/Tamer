package br.furb.furbot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import br.furb.furbot.suporte.LoadImage;

/**
 * 
 * @author Adilson Vahldick
 */
public class Tesouro extends Numero {

    public Tesouro() {
    }

    public ImageIcon buildImage() {
        ImageIcon image = LoadImage.getInstance().getIcon("imagens/tesouro.jpg");
        BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 50, 50);
        g.drawImage(image.getImage(), 2, 2, null);
        g.fillRect(30, 30, 20, 20);
        g.setColor(Color.black);
        g.drawRect(30, 30, 19, 19);
        g.setFont(fonte);
        g.setColor(Color.red);
        g.drawString(String.valueOf(getValor()), 32, 45);
        return new ImageIcon(img);
    }

    private static Font fonte = new Font("Arial", Font.PLAIN, 14);
}
