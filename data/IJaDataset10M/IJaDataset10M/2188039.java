package org.jdna.bmt.web.client.ui.prefs;

import org.jdna.bmt.web.client.ui.toast.Toaster;
import org.jdna.bmt.web.client.ui.util.HorizontalButtonBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ImageViewer extends Composite {

    private static ImageViewerUiBinder uiBinder = GWT.create(ImageViewerUiBinder.class);

    interface ImageViewerUiBinder extends UiBinder<Widget, ImageViewer> {
    }

    @UiField
    HorizontalButtonBar buttonBar;

    @UiField
    Image image;

    Toaster toast = new Toaster();

    private String[] images;

    private int cur = 0;

    public ImageViewer(String images[]) {
        initWidget(uiBinder.createAndBindUi(this));
        this.images = images;
        Button b = new Button("Previous");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setImage(cur - 1);
            }
        });
        buttonBar.add(b);
        b = new Button("Next");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setImage(cur + 1);
            }
        });
        buttonBar.add(b);
        if (images != null) {
            for (String img : images) {
                new Image(img);
            }
        }
        setImage(cur);
    }

    private void setImage(int n) {
        if (images == null || images.length == 0) {
            toast.addMessage("No images");
            return;
        }
        if (n < 0) {
            toast.addMessage("No previous image");
            return;
        }
        if (n > (images.length - 1)) {
            toast.addMessage("No next image");
            return;
        }
        cur = n;
        image.setUrl(images[n]);
    }
}
