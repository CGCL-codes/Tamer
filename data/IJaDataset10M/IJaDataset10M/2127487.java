package Model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

public class Arena {

    Graphics g;

    List<Einheit> einheiten;

    List<Faehigkeit> faehigkeiten;

    public Arena(Graphics g) {
        this.g = g;
    }

    public void zeichneDich() {
        g.setColor(Color.black);
        g.fillRect(1000 / 7, 750 / 7, (1000 / 7) * 5, (750 / 7) * 5);
    }
}
