package com.team1160.scouting.frontend.elements;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * A scouting element meant to take its input in a single line.
 * @author Saketh Kasibatla
 */
public class SingleLineInputElement extends ScoutingElement {

    /**
     * the label that displays the name of the scouting element
     */
    private JLabel label;

    /**
     * the input text field
     */
    private JTextField input;

    /**
     * the type of the the input. Can be INTEGER, STRING, or DOUBLE
     */
    private int type;

    /**
     * the name that is displayed in the jlabel
     */
    private String name;

    public static final int INTEGER = 1, STRING = 2, DOUBLE = 3;

    protected boolean inComments;

    protected boolean isNegative;

    protected boolean isTime;

    protected int maxTime;

    /**
     * creates a new SingleLineInputElement with the specified name and type.
     * if there is no : at the end of the name, one is added.
     * @param name the name displayed in the label.
     * @param type the type of input this element recieves.
     */
    public SingleLineInputElement(String name, int type, boolean isNegative, boolean isTime, int maxTime) {
        super();
        this.isNegative = isNegative;
        this.isTime = isTime;
        this.maxTime = maxTime;
        this.inComments = false;
        if (!(name.endsWith(":"))) {
            label = new JLabel(name + ":");
            this.name = name + ":";
        } else {
            label = new JLabel(name);
            this.name = name;
        }
        this.input = new JTextField();
        this.input.setColumns(10);
        this.label.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.input.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(label);
        this.add(input);
    }

    public SingleLineInputElement(String name, int type, boolean isNegative) {
        this(name, type, isNegative, false, 0);
    }

    public SingleLineInputElement(String name, int type) {
        this(name, type, false, false, 0);
    }

    /**
     *
     * @return the text of the label
     */
    public String getText() {
        return this.label.getText().substring(0, this.label.getText().length());
    }

    /**
     *
     * @return the input in the input field
     */
    public String getInput() {
        String text = this.input.getText();
        if (this.isNegative) {
            if (text.startsWith("-")) {
                return text;
            } else {
                return "-" + text;
            }
        } else if (this.isTime) {
            int value = Math.abs(Integer.parseInt(text));
            return Integer.toString(this.maxTime - value);
        } else {
            return text;
        }
    }

    /**
     *
     * @return the type of input that this element takes
     */
    public int getType() {
        return type;
    }

    public boolean isNegative() {
        return this.isNegative;
    }

    public boolean isTime() {
        return this.isTime;
    }

    public int getMaxTime() {
        return this.maxTime;
    }

    public void setInComments(boolean b) {
        this.inComments = b;
    }

    public boolean isInComments() {
        return this.inComments;
    }

    /**
     * tests if this element is equal to another object.
     * @param obj the object to test equality with
     * @return wether or not this equals obj
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SingleLineInputElement other = (SingleLineInputElement) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * hash code method. used for identification purposes.
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    /**
     * resets this element.
     */
    @Override
    public void clear() {
        this.input.setText("");
    }
}
