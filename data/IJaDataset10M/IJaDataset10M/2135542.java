package uk.ac.cisban.saint.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.Window;

/**
 * @author morgan on 30-Jul-2009, 17:43:22
 */
public class HeaderPanel extends VerticalPanel {

    public HeaderPanel(SaintImageBundle myImageBundle) {
        setWidth("100%");
        AbstractImagePrototype saintLogoPrototype = myImageBundle.saintLogo();
        Image image = saintLogoPrototype.createImage();
        image.addStyleName("centerMe");
        image.addStyleName("clickableImage");
        image.setTitle("Saint Model Annotator");
        image.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent clickEvent) {
                Window.open("http://www.cisban.ac.uk/saint", "_top", "");
            }
        });
        add(image);
        add(new HTML("<h3>SBML Model Annotator: Annotation via Data Integration</h3>"));
    }
}
