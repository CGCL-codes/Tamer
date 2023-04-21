package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePicker extends AbstractWidget {

    public static final String TAG_NAME = "DatePicker";

    public DatePicker() {
        super(TAG_NAME);
        apply();
    }

    @Override
    protected int getContentHeight() {
        return 160;
    }

    @Override
    protected int getContentWidth() {
        return 140;
    }

    public void paint(Graphics g) {
        drawBackground(g);
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int month = c.get(Calendar.MONTH);
        g.setColor(Color.black);
        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy");
        g.drawString(df.format(d), getX() + 2, getY() + 14);
        int day = c.get(Calendar.DATE);
        c.add(Calendar.DATE, -day + 1);
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) c.add(Calendar.DATE, -1);
        g.setColor(Color.darkGray);
        g.drawString("S", getX() + 2, getY() + 34);
        g.drawString("M", getX() + 22, getY() + 34);
        g.drawString("T", getX() + 42, getY() + 34);
        g.drawString("W", getX() + 62, getY() + 34);
        g.drawString("T", getX() + 82, getY() + 34);
        g.drawString("F", getX() + 102, getY() + 34);
        g.drawString("S", getX() + 122, getY() + 34);
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 7; i++) {
                if (c.get(Calendar.MONTH) == month) g.setColor(Color.black); else g.setColor(Color.lightGray);
                g.drawString("" + c.get(Calendar.DATE), getX() + 2 + i * 20, getY() + 54 + j * 20);
                c.add(Calendar.DATE, 1);
            }
        }
    }
}
