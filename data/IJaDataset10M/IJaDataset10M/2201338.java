package game;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;
import java.awt.Font;
import java.util.*;
import javax.swing.*;

/**
*Do not use this class directly, use the showMessage method inherited on Presenters.
*/
public class MessagePresenter extends Presenter {

    private String message = "";

    private Presenter behide;

    private Presenter next;

    private List<String> lines = new ArrayList<String>();

    private int top = 0;

    private int nlines;

    public MessagePresenter(String m, Presenter b, Presenter n) {
        message = m;
        behide = b;
        next = n;
        while (message.length() > 0) {
            int nn = 20;
            if (nn > message.length()) nn = message.length();
            lines.add(message.substring(0, nn));
            message = message.substring(nn);
        }
        nlines = lines.size();
    }

    private Image bottomFrame = new ImageIcon("./resources/battle/bottomframe.png").getImage();

    /**
	*
	*/
    public void drawOn(Graphics2D g) {
        behide.drawOn(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 195, 16 * 20 + 2, 16 * 18);
        g.drawImage(bottomFrame, 0, 195, null);
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Courier New", java.awt.Font.BOLD, 25));
        for (int i = 0; i < 2 && (i + top) < nlines; i++) {
            g.drawString(lines.get(i + top), 10, 230 + 30 * i);
        }
        if (top + 2 < nlines) g.drawString("...", 16 * 16 + 8, 272);
    }

    /**
	*
	*/
    public void buttonPressed(Button b) {
        if (b != Button.A) return;
        if (top + 2 < nlines) top++; else {
            if (next != null) enterPresenter(next);
            synchronized (this) {
                notify();
            }
        }
    }

    /**
	*
	*/
    public void step(int ms) {
    }
}
