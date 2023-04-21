package com.matrixbi.adans.client.report.widget;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.matrixbi.adans.client.mvc.CustomController;
import com.matrixbi.adans.client.report.mvc.ReportEvents;
import com.matrixbi.adans.client.report.mvc.ReportView;

;

public class MeasureLink extends ReportView {

    private Hyperlink link;

    private MeasureController selector;

    public MeasureLink() {
        super();
    }

    @Override
    public void setController(CustomController controller) {
        super.setController(controller);
    }

    @Override
    public Widget asWidget() {
        return getLink();
    }

    protected Hyperlink getLink() {
        if (link == null) {
            link = new Hyperlink();
            link.addClickHandler(getClickHandler());
            link.addStyleName("widget-Hyperlink");
        }
        return link;
    }

    private MeasureController getSelector() {
        if (selector == null) {
            MeasureSelector view = new MeasureSelector();
            selector = new MeasureController(getController().getDispatcher(), view);
            getController().getDispatcher().addController(selector);
        }
        return selector;
    }

    @Override
    protected void handleEvent(AppEvent event) {
        EventType type = event.getType();
        if (type == ReportEvents.Init) {
            getLink();
        }
        if (type == ReportEvents.LoadReport) {
        }
        if (event.getType() == ReportEvents.ReportLoaded) {
            getLink().setText(getReport().getVariable1());
        }
    }

    private ClickHandler getClickHandler() {
        return new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ((Window) getSelector().getView().asWidget()).show();
            }
        };
    }
}
