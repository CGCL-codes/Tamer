package org.gwt.advanced.client.ui.widget.cell;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.ExpandCellEventProducer;
import org.gwt.advanced.client.ui.widget.theme.ThemeImage;

/**
 * This is a basic expandable cell implementation.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class ExpandableCellImpl extends AbstractCell implements ExpandableCell {

    /** a dock panel to place an original cell and a node image */
    private HorizontalPanel panel;

    /** a node image */
    private Image image;

    /** image click handler registration */
    private HandlerRegistration imageClickHandlerRegistration;

    /** expanded flag */
    private boolean expanded;

    /** leaf flag */
    private boolean leaf;

    /**
     * Creates an instance of this class.
     */
    public ExpandableCellImpl() {
        setStyleName("expandable-cell");
    }

    /** {@inheritDoc} */
    public void displayActive(boolean active) {
        Widget widget = createInactive();
        prepare(widget);
    }

    /** {@inheritDoc} */
    protected Widget createActive() {
        return createInactive();
    }

    /** {@inheritDoc} */
    protected Widget createInactive() {
        HorizontalPanel panel = getPanel();
        Image image = getImage();
        Widget child = (Widget) getValue();
        if (panel.getWidgetIndex(image) == -1 && !isLeaf()) {
            panel.add(image);
            panel.setCellWidth(image, "1%");
        } else {
            Label label = new Label();
            panel.add(label);
            panel.setCellWidth(label, "1%");
        }
        if (panel.getWidgetIndex(child) == -1) panel.add(child);
        panel.setCellWidth(child, "99%");
        return panel;
    }

    /** {@inheritDoc} */
    public void setFocus(boolean focus) {
    }

    /** {@inheritDoc} */
    protected void addListeners(Widget widget) {
        if (imageClickHandlerRegistration != null) {
            imageClickHandlerRegistration.removeHandler();
        }
        final ExpandableCell cell = this;
        imageClickHandlerRegistration = getImage().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent clickEvent) {
                setExpanded(!isExpanded());
                HorizontalPanel panel = getPanel();
                panel.remove(getImage());
                createImage();
                panel.insert(getImage(), 0);
                panel.setCellWidth(getImage(), "1%");
                if (!isLeaf()) ((ExpandCellEventProducer) getGrid()).fireExpandCell(cell);
            }
        });
    }

    /** {@inheritDoc} */
    public Object getNewValue() {
        return getValue();
    }

    /** {@inheritDoc} */
    public boolean isExpanded() {
        return expanded;
    }

    /** {@inheritDoc} */
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /** {@inheritDoc} */
    public boolean isLeaf() {
        return leaf;
    }

    /** {@inheritDoc} */
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
        if (leaf) setExpanded(false);
    }

    /**
     * Getter for property 'image'.
     *
     * @return Value for property 'image'.
     */
    protected Image getImage() {
        if (image == null) createImage();
        return image;
    }

    /**
     * This method creates the node image and adds the listener.
     */
    protected void createImage() {
        if (isLeaf()) {
            return;
        }
        if (isExpanded()) setImage(new ThemeImage("expanded.gif")); else if (!isLeaf()) setImage(new ThemeImage("collapsed.gif"));
        addListeners(getImage());
    }

    /**
     * Setter for property 'image'.
     *
     * @param image Value to set for property 'image'.
     */
    protected void setImage(Image image) {
        this.image = image;
    }

    /**
     * Getter for property 'panel'.
     *
     * @return Value for property 'panel'.
     */
    protected HorizontalPanel getPanel() {
        if (panel == null) {
            panel = new HorizontalPanel();
            panel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        }
        return panel;
    }
}
