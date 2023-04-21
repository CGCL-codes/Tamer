package com.marketfarm.client.footer;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.marketfarm.client.Header;

public class Ajuda extends Composite {

    public Ajuda() {
        FlowPanel flowPanel = new FlowPanel();
        initWidget(flowPanel);
        flowPanel.setSize("846px", "");
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        flowPanel.add(verticalPanel);
        verticalPanel.setSize("", "");
        Header header = new Header();
        verticalPanel.add(header);
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setSpacing(10);
        verticalPanel.add(horizontalPanel);
        DecoratorPanel decoratorPanel = new DecoratorPanel();
        horizontalPanel.add(decoratorPanel);
        VerticalPanel verticalPanel_1 = new VerticalPanel();
        verticalPanel_1.setSpacing(10);
        decoratorPanel.setWidget(verticalPanel_1);
        verticalPanel_1.setWidth("798px");
        Label lblEssaA = new Label("Essa é a ajuda do sistema.");
        verticalPanel_1.add(lblEssaA);
        Footer footer = new Footer();
        verticalPanel.add(footer);
    }
}
