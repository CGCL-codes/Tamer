package com.nepxion.swing.wizard;

import java.awt.Component;
import javax.swing.Icon;

public abstract class JStep {

    private String title;

    private Icon image;

    private Component contentPane;

    public JStep(String title) {
        this(title, null, null);
    }

    public JStep(String title, Component contentPane) {
        this(title, null, contentPane);
    }

    public JStep(String title, Icon image) {
        this(title, image, null);
    }

    public JStep(String title, Icon image, Component contentPane) {
        this.title = title;
        this.image = image;
        this.contentPane = contentPane;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Icon getImage() {
        return image;
    }

    public void setImage(Icon image) {
        this.image = image;
    }

    public Component getContentPane() {
        return contentPane;
    }

    public void setContentPane(Component contentPane) {
        this.contentPane = contentPane;
    }

    public abstract boolean confirm();

    public abstract boolean cancel();

    public abstract boolean previous();

    public abstract boolean next();

    public String toString() {
        return getTitle();
    }
}
