package org.waveprotocol.wave.client.editor.examples.img;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * A simple captioned image widget.
 *
 * @author danilatos@google.com (Daniel Danilatos)
 */
public class CaptionedImageWidget extends Composite {

    public interface Listener {

        void onClickImage();
    }

    /** UiBinder */
    interface Binder extends UiBinder<HTMLPanel, CaptionedImageWidget> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);

    @UiField
    Element container;

    @UiField
    Image image;

    private Listener listener;

    public CaptionedImageWidget() {
        initWidget(BINDER.createAndBindUi(this));
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Element getContainer() {
        return container;
    }

    public void setImageSrc(String src) {
        image.setUrl(src);
    }

    @UiHandler("image")
    void handleClick(ClickEvent e) {
        if (listener != null) {
            listener.onClickImage();
        }
    }
}
