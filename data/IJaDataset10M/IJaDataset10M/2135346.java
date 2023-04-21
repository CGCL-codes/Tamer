package com.dukesoftware.software.sort.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import com.dukesoftware.software.sort.data.template.ElementArray;
import com.dukesoftware.utils.thread.ILoopTaskAdv;

/**
 * 
 * 
 * 
 * 
 *
 *
 */
public class Canvas extends JPanel implements ILoopTaskAdv {

    private static final long serialVersionUID = 1L;

    private static final long INTERVAL = 100;

    private final ElementArray data;

    /**
	 * RXgN^
	 * @param xsize LoX̃TCYX
	 * @param ysize LoX̃TCYY
	 */
    public Canvas(int xsize, int ysize, ElementArray data) {
        super();
        this.data = data;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(xsize, ysize));
        setOpaque(true);
    }

    public void executeInLoop() throws InterruptedException {
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        try {
            super.paint(g);
            g.setColor(Color.WHITE);
            data.paint(g);
            Thread.sleep(INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initOutOfLoop() {
    }

    @Override
    public void endOutOfLoop() {
    }
}
