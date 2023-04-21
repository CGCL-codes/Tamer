package org.gwtoolbox.sample.widget.client.misc;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.sample.widget.client.SamplePanel;
import org.gwtoolbox.widget.client.WidgetImages;
import org.gwtoolbox.commons.ui.client.event.MouseHoverHandler;
import org.gwtoolbox.widget.client.label.BasicLabel;
import org.gwtoolbox.widget.client.notification.DefaultNotifier;
import org.gwtoolbox.widget.client.notification.SimpleCallback;
import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;

/**
 * @author Uri Boness
 */
@Component
@MiscSample
public class BasicLabelSamplePane extends Composite implements SamplePanel {

    public BasicLabelSamplePane() {
        VerticalPanel labels = new VerticalPanel();
        BasicLabel label = createLabel("Simple", null);
        labels.add(label);
        labels.setCellHeight(label, "30px");
        addGap(labels, "10px");
        label = createLabel("Click Me", null);
        label.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                DefaultNotifier.getInstance().showInfo("Click", "You see, you can even register click listeners on a BasicLabel", new SimpleCallback() {

                    public void handle() {
                    }
                });
            }
        });
        labels.add(label);
        labels.setCellHeight(label, "30px");
        addGap(labels, "10px");
        label = createLabel("Icon Label", WidgetImages.Instance.get().icon_Printer().createImage());
        labels.add(label);
        labels.setCellHeight(label, "30px");
        addGap(labels, "10px");
        label = createLabel("Label Custom Width", WidgetImages.Instance.get().icon_Printer().createImage());
        label.setWidth("250px");
        labels.add(label);
        labels.setCellHeight(label, "30px");
        addGap(labels, "10px");
        label = createLabel("Align Center", WidgetImages.Instance.get().icon_CubeGreen().createImage());
        label.setWidth("250px");
        label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        labels.add(label);
        labels.setCellHeight(label, "30px");
        addGap(labels, "10px");
        label = createLabel("Align Right", WidgetImages.Instance.get().icon_CubeGreen().createImage());
        label.setWidth("250px");
        label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        labels.add(label);
        labels.setCellHeight(label, "30px");
        VerticalPanel main = new VerticalPanel();
        main.add(labels);
        main.setCellWidth(labels, "100%");
        Label gap = new Label();
        main.add(gap);
        main.setCellHeight(gap, "100%");
        initWidget(main);
    }

    public String getName() {
        return "Labels";
    }

    public Widget getContentWidget() {
        return this;
    }

    public void reset() {
    }

    private BasicLabel createLabel(String text, Image image) {
        final BasicLabel label = new BasicLabel(text);
        if (image != null) {
            label.setImage(image);
        }
        label.addMouseHoverHandler(new MouseHoverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                DOM.setStyleAttribute(label.getElement(), "border", "1px solid blue");
            }

            @Override
            public void onMouseOut(MouseOutEvent event) {
                DOM.setStyleAttribute(label.getElement(), "border", "none");
            }
        });
        return label;
    }
}
