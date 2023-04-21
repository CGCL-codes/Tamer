package net.sf.gham.core.util;

import java.awt.Color;

/**
 * @author fabio
 *
 */
public class FixedSkill implements Skill, Comparable<FixedSkill> {

    private double value;

    public FixedSkill(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public double getFirstValue() {
        return value;
    }

    public Color getBackgroundColor() {
        return Color.WHITE;
    }

    public int compareTo(FixedSkill o) {
        double r = value - o.value;
        return r > 0 ? 1 : (r < 0 ? -1 : 0);
    }
}
