package russotto.zplet.screenmodel;

import java.util.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.applet.Applet;

public class ZStatus extends Panel {

    boolean timegame;

    boolean initialized;

    boolean chronograph;

    String location;

    int score;

    int turns;

    int hours;

    int minutes;

    Label Right;

    Label Left;

    public ZStatus() {
        setLayout(new BorderLayout());
        Right = new Label();
        add("East", Right);
        Left = new Label();
        add("West", Left);
        chronograph = false;
    }

    public void update_score_line(String location, int score, int turns) {
        this.timegame = false;
        this.location = location;
        this.score = score;
        this.turns = turns;
        Left.setText(location);
        Right.setText(score + "/" + turns);
        layout();
        repaint();
    }

    public void update_time_line(String location, int hours, int minutes) {
        String meridiem;
        this.timegame = true;
        this.location = location;
        this.hours = hours;
        this.minutes = minutes;
        Left.setText(location);
        if (chronograph) {
            Right.setText(hours + ":" + minutes);
        } else {
            if (hours < 12) meridiem = "AM"; else meridiem = "PM";
            hours %= 12;
            if (hours == 0) hours = 12;
            Right.setText(hours + ":" + minutes + meridiem);
        }
        layout();
        repaint();
    }

    public Dimension minimumSize() {
        return new Dimension(100, 10);
    }

    public Dimension preferredSize() {
        return new Dimension(500, 20);
    }
}
