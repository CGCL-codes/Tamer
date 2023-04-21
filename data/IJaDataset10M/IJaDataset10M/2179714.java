package com.google.gwt.ricordo.client.view;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TitlePanel extends VerticalPanel {

    public static String ANNOT_TITLE = ("RICORDO Annotation Editor");

    public static String COMP_TITLE = ("RICORDO Composite Editor");

    public static String QUERY_TITLE = ("RICORDO Query Service");

    public static String MAIN_TITLE = ("RICORDO Toolset");

    public static String MAN_TITLE = ("RICORDO Manchester Query Creator");

    public TitlePanel(String title) {
        setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        setWidth("100%");
        setStyleName("pannel-Border");
        createTitle(title);
    }

    private void createTitle(String title) {
        Label titleLabel = new Label(title);
        titleLabel.setStyleName("query-Title");
        this.add(titleLabel);
    }
}
