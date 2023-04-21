package org.kablink.teaming.gwt.client.lpe;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * The DragProxy class is used to create the object that will be dragged from the palette.
 */
public class DragProxy extends PopupPanel {

    /**
	 * 
	 */
    public DragProxy(ImageResource imageResource, String text) {
        super(false);
        FlowPanel panel;
        InlineLabel label;
        Image img;
        addStyleName("lpeDragProxyPopup");
        panel = new FlowPanel();
        panel.addStyleName("lpeDragProxy");
        img = new Image(imageResource);
        img.addStyleName("lpeDragProxyImg");
        panel.add(img);
        label = new InlineLabel(text);
        panel.add(label);
        setWidget(panel);
    }
}
