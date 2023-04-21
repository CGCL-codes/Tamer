package de.mse.mogwai.formmaker.component;

import java.awt.Component;

/**
 * Alias class for supporting Separators.
 * 
 * This class extends Component, but the only property that is 
 * the user able to edit is text.
 * 
 * @author Mirko Seric
 */
public class Separator extends Component {

    private String m_text;

    public Separator() {
    }

    public String getText() {
        return m_text;
    }

    public void setText(String text) {
        this.m_text = text;
    }
}
