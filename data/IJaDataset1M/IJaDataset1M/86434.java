package com.tensegrity.palowebviewer.modules.widgets.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.tensegrity.palowebviewer.modules.util.client.Assertions;

/**
 * Label that contains not only text but picture too. It also can reciev click events.
 *
 * CSS:
 * <ol> 
 * <li>tensegrity-gwt-widgets-labeledimage - label<li>
 * <li>tensegrity-gwt-widgets-labeledimage-icon - icon<li>
 * </ol>
 */
public class LabeledImage2 extends Composite implements SourcesClickEvents {

    public static final int RIGHT = 1;

    public static final int LEFT = 2;

    private HorizontalPanel panel;

    private FocusPanel focusPanel;

    private Image image;

    private HTML label;

    /**
     * {@inheritDoc}
     */
    public void addClickListener(ClickListener listener) {
        image.addClickListener(listener);
        label.addClickListener(listener);
    }

    /**
     * Sets the text for the label.
     * @param value label text.
     */
    public void setText(String value) {
        label.setText(value);
    }

    /**
     * Sets HTML as a label text.
     * @param value HTML label text.
     */
    public void setHTML(String value) {
        label.setHTML(value);
    }

    /**
     * {@inheritDoc}
     */
    public void removeClickListener(ClickListener listener) {
        image.removeClickListener(listener);
        label.removeClickListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void setStyleName(String name) {
        super.setStyleName(name);
        image.setStyleName(name + "-icon");
    }

    /**
     * {@inheritDoc}
     */
    public void addStyleName(String name) {
        super.addStyleName(name);
        image.addStyleName(name + "-icon");
    }

    /**
     * {@inheritDoc}
     */
    public void removeStyleName(String name) {
        super.removeStyleName(name);
        image.removeStyleName(name + "-icon");
    }

    /**
     * Constructs the widget.
     * @param style CSS style name
     * @param text label text.
     */
    public LabeledImage2(Image img, String text) {
        this(img, text, RIGHT);
    }

    /**
     * Constructs the widget.
     * @param style CSS style name
     * @param text label text.
     * @param textPosition text position {@link #RIGHT} or {@link #LEFT}
     */
    public LabeledImage2(Image img, String text, int textPosition) {
        Assertions.assertNotNull(img, "Image");
        this.panel = new HorizontalPanel();
        this.image = img;
        this.label = new HTML();
        setText(text);
        panel.setCellVerticalAlignment(image, HorizontalPanel.ALIGN_MIDDLE);
        panel.setSpacing(0);
        focusPanel = new FocusPanel(panel);
        this.initWidget(focusPanel);
        setStyleName("tensegrity-gwt-widgets-labeledimage");
        addImageAndLabel(textPosition);
    }

    private void addImageAndLabel(int textPosition) {
        switch(textPosition) {
            case RIGHT:
                {
                    panel.add(image);
                    panel.add(label);
                    break;
                }
            case LEFT:
                {
                    panel.add(label);
                    panel.add(image);
                    break;
                }
        }
    }
}
