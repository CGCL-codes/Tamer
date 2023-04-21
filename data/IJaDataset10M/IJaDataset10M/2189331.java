package org.gwanted.gwt.widget.grid.client.impl.view.bulk.formater.html;

import org.gwanted.gwt.core.client.util.GwantedEvent;
import org.gwanted.gwt.widget.grid.client.events.DetailListener;
import org.gwanted.gwt.widget.grid.client.view.ImageLocator;
import org.gwanted.gwt.widget.grid.client.view.StyleConstants;
import org.gwanted.gwt.widget.grid.client.view.cells.OpenerDetailCell;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Image to open/close disclosure.
 *
 * @author Joshua Hewitt aka Sposh
 */
public class DisclosureImage extends Image implements DetailListener {

    private static final AbstractImagePrototype OPEN = ImageLocator.getInstance().openDetail();

    private static final AbstractImagePrototype CLOSE = ImageLocator.getInstance().closeDetail();

    private static final AbstractImagePrototype CLOSEDISABLED = ImageLocator.getInstance().closeDetailDisabled();

    private static final Image OPEN_IMAGE = DisclosureImage.OPEN.createImage();

    public DisclosureImage(final OpenerDetailCell opener) {
        this(OPEN_IMAGE, opener);
    }

    public DisclosureImage(final AbstractImagePrototype imagePrototype, final OpenerDetailCell opener) {
        this(imagePrototype.createImage(), opener);
    }

    public DisclosureImage(final Image image, final OpenerDetailCell opener) {
        super(image.getUrl(), image.getOriginLeft(), image.getOriginTop(), image.getWidth(), image.getHeight());
        addStyleName(StyleConstants.STYLE_DISCLOSURE_CALLER_IMG);
        addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                opener.toggleRender(DisclosureImage.this);
            }
        });
        DOM.setEventListener(getElement(), this);
    }

    public boolean onDetailPreview(final GwantedEvent event) {
        return true;
    }

    public boolean onDetail(final GwantedEvent event) {
        if (DetailListener.EVENT_CLOSE.equalsIgnoreCase(event.getType())) {
            onDetailClose(this);
        } else if (DetailListener.EVENT_OPEN.equalsIgnoreCase(event.getType())) {
            onDetailOpen(this);
        } else if (DetailListener.EVENT_STICK.equalsIgnoreCase(event.getType())) {
            onDetailStick(this);
        } else if (DetailListener.EVENT_UNSTICK.equalsIgnoreCase(event.getType())) {
            onDetailUnstick(this);
        }
        return true;
    }

    private void onDetailStick(final UIObject sender) {
        changeImage(sender, DisclosureImage.CLOSEDISABLED);
    }

    private void onDetailUnstick(final UIObject sender) {
        onDetailOpen(sender);
    }

    private void onDetailOpen(final UIObject sender) {
        changeImage(sender, DisclosureImage.CLOSE);
    }

    private void onDetailClose(final UIObject sender) {
        changeImage(sender, DisclosureImage.OPEN);
    }

    private void changeImage(final UIObject sender, final AbstractImagePrototype imagePrototype) {
        imagePrototype.applyTo((Image) sender);
    }
}
