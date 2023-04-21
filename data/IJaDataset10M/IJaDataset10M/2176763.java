package com.wxz.sanguo.edit.map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MapSplitTool extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public MapSplitTool(String beSplitFile, String outputFile) {
        setPreferredSize(new Dimension(1400, 860));
        add(new SplitPane(beSplitFile, outputFile));
        setResizable(true);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new MapSplitTool("image/war/29-1.png", "E:/code/bwdl/image/split");
    }
}

class SplitPane extends JPanel implements MouseListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private BufferedImage splitImage;

    private String outputFile;

    public SplitPane(String imageName, String outputFile) {
        try {
            this.splitImage = ImageIO.read(new File(imageName));
            this.outputFile = outputFile;
            addMouseListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.splitImage, 0, 0, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / 48;
        int y = e.getY() / 48;
        try {
            BufferedImage image = this.splitImage.getSubimage(48 * x, 48 * y, 48, 48);
            if (image == null) {
                System.out.println("null");
            }
            ImageIO.write(image, "png", new File(this.outputFile + "/" + x + "-" + y + ".png"));
            System.out.println("ok!");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
