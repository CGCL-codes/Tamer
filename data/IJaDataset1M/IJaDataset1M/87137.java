package game;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;
import java.awt.Font;
import java.util.*;
import javax.swing.*;

/**
*Do not use this class directly, use the showMenu methods inherited on Presenters.
*/
public class MenuPresenter extends Presenter {

    private String[] choices;

    private String longest = "";

    private int selected = 0;

    private String choice;

    private int bottomx = 16 * 20, bottomy = 195;

    private Presenter behide, next;

    public String choice() {
        return choice;
    }

    public MenuPresenter(String[] choicess, Presenter b, Presenter n) {
        choices = choicess;
        behide = b;
        next = n;
        for (String x : choices) if (x.length() > longest.length()) longest = x;
        choice = choices[selected];
        int height = choices.length * 30;
        if (bottomy - height < 0) bottomy = height;
    }

    /**
	*
	*/
    public void drawOn(Graphics2D g) {
        behide.drawOn(g);
        int width = longest.length() * 18 + 20;
        int height = choices.length * 30;
        g.setColor(Color.WHITE);
        g.fillRect(bottomx - width - 1, bottomy - height - 1, width + 3, height + 3);
        g.setColor(Color.BLACK);
        g.fillRoundRect(bottomx - width, bottomy - height, width, height, 4, 4);
        g.setColor(Color.WHITE);
        g.fillRect(bottomx - width + 3, bottomy - height + 3, width - 6, height - 6);
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Courier New", java.awt.Font.BOLD, 25));
        int off = 0;
        if (choices.length > 9) {
            off = selected - 4;
        }
        for (int i = 0; i < choices.length && i < 10; i++) {
            int pull = i + off;
            if (pull < 0) pull = pull + choices.length;
            if (pull >= choices.length) pull = pull - choices.length;
            g.drawString(choices[pull], bottomx - width + 20, bottomy - height + 22 + 30 * i);
        }
        g.drawString(">", bottomx - width + 4, bottomy - height + 22 + 30 * (selected - off));
    }

    /**
	*
	*/
    public void buttonPressed(Button b) {
        if (b == Button.UP) {
            selected--;
            if (selected < 0) selected = choices.length - 1;
            choice = choices[selected];
        } else if (b == Button.DOWN) {
            selected++;
            if (selected >= choices.length) selected = 0;
            choice = choices[selected];
        } else if (b == Button.A) {
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
