package apjava.snowman;

import java.awt.*;

public class LumpOfCoal extends Feature {

    private Color color;

    private int height, width;

    int lastHeight;

    public LumpOfCoal(int x, int y, Snowman p, int width, int height, java.awt.Color c, int xMax, int xMin, int yMax) {
        super(x, y, p, xMax, xMin, yMax);
        color = c;
        this.height = height;
        this.width = width;
        attatched = true;
        vx = 0;
        vy = 0;
        ay = 1. / 36;
    }

    public void draw(Graphics g, int x, int y) {
        int xpos, ypos;
        g.setColor(color);
        if (parent.getMelted() > .83 + Math.random() * .1 && attatched) {
            xpos = (int) Math.round(x + this.x);
            ypos = (int) Math.round(y + this.y + ((Snowman) parent).getExtraHeight());
            g.drawRoundRect(xpos, ypos, width, height, width * 4 / 5, width * 4 / 5);
            lastHeight = ((Snowman) parent).getExtraHeight();
        } else if (attatched) {
            attatched = false;
            this.x = x + this.x;
            this.y = y + this.y + ((Snowman) parent).getExtraHeight();
            vy = (((Snowman) parent).getExtraHeight() - lastHeight);
            g.drawRoundRect((int) Math.round(this.x), (int) Math.round(this.y), width, height, width * 3 / 5, width * 4 / 5);
        } else {
            move();
            if (parent.hitTest((int) Math.round(this.x), (int) Math.round(this.y), x, y) != -1) {
                int num = parent.hitTest((int) Math.round(this.x), (int) Math.round(this.y), x, y);
                double v = .9 * Math.sqrt(vx * vx + vy * vy);
                int yOffset = 0;
                for (int i = 0; i < num; i++) yOffset += parent.snowballs.get(i).getCurrHeight();
                yOffset += parent.snowballs.get(num).getCurrHeight() / 2;
                double xhitpos = this.x - parent.snowballs.get(parent.snowballs.size() - 1).getWidth() / 2 - x;
                double yhitpos = this.y - (parent.getExtraHeight() + yOffset) - y;
                double theta = Math.atan2(yhitpos, xhitpos);
                vx = v * Math.cos(theta);
                vy = v * Math.sin(theta);
            }
            vy += ay;
            this.x += vx;
            this.y += vy;
            g.drawRoundRect((int) Math.round(this.x), (int) Math.round(this.y), width, height, width * 3 / 5, width * 4 / 5);
        }
    }

    public void move() {
        if (this.y + height >= yMax) {
            if (vy < .01) vy = ay = 0;
            vy *= -.8;
            vx *= .8;
        }
        if (this.x < xMin || this.x > xMax - width) {
            vx *= -.9;
        }
    }
}
