package net.sf.beanform.domain;

import java.util.Date;
import org.hibernate.validator.Length;

/**
 * Domain object for testing purposes.
 *
 * @author Daniel Gredler
 */
public class State {

    public static enum Color {

        RED, BLUE
    }

    @Length(min = 5, max = 10)
    private String name;

    private String abbreviation;

    private Date unreadable;

    private String[] cities;

    private Color color;

    public State() {
    }

    public State(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(max = 2000)
    public String getAbbreviation() {
        return this.abbreviation;
    }

    public void setUnreadable(Date unreadable) {
        this.unreadable = unreadable;
    }

    public String getCities(int index) {
        return this.cities[index];
    }

    public void setCities(int index, String value) {
        this.cities[index] = value;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "State[name=" + this.name + " unreadable=" + this.unreadable + "]";
    }
}
