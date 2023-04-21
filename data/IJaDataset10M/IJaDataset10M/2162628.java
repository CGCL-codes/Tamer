package org.pojosoft.ria.gwt.client.ui;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The default implementation of {@link Container}.
 *
 * @author POJO Software
 */
public class GenericContainer extends Container {

    Grid grid;

    String cssKey;

    public GenericContainer(String cssKey) {
        if (cssKey == null || cssKey.trim().equals("")) cssKey = "Generic";
        this.cssKey = cssKey;
        createContainer();
        initWidget(grid);
    }

    public GenericContainer() {
        this("Generic");
    }

    void createContainer() {
        grid = new Grid(5, 3);
        grid.setCellSpacing(0);
        grid.setCellPadding(0);
        grid.addStyleName("pojo-GenericContainer");
        Label imgHeaderTopLeft = new Label("");
        Label imgHeaderTop = new Label("");
        Label imgHeaderTopRight = new Label("");
        grid.setWidget(0, 0, imgHeaderTopLeft);
        grid.getCellFormatter().setStyleName(0, 0, "HeaderOutline-tl");
        grid.setWidget(0, 1, imgHeaderTop);
        grid.getCellFormatter().setStyleName(0, 1, "HeaderOutline-t");
        grid.setWidget(0, 2, imgHeaderTopRight);
        grid.getCellFormatter().setStyleName(0, 2, "HeaderOutline-tr");
        Label imgHeaderMiddleLeft = new Label("");
        Label imgHeaderMiddle = new Label("");
        Label imgHeaderMiddleRight = new Label("");
        grid.setWidget(1, 0, imgHeaderMiddleLeft);
        grid.getCellFormatter().setStyleName(1, 0, "HeaderOutline-l");
        grid.setWidget(1, 1, imgHeaderMiddle);
        grid.setWidget(1, 2, imgHeaderMiddleRight);
        grid.getCellFormatter().setStyleName(1, 2, "HeaderOutline-r");
        Label imgHeaderBottomLeft = new Label("");
        Label imgHeaderBottom = new Label("");
        Label imgHeaderBottomRight = new Label("");
        grid.setWidget(2, 0, imgHeaderBottomLeft);
        grid.getCellFormatter().setStyleName(2, 0, "HeaderOutline-bl");
        grid.setWidget(2, 1, imgHeaderBottom);
        grid.getCellFormatter().setStyleName(2, 1, "HeaderOutline-b");
        grid.setWidget(2, 2, imgHeaderBottomRight);
        grid.getCellFormatter().setStyleName(2, 2, "HeaderOutline-br");
        Label imgContentMiddleLeft = new Label("");
        Label imgContentMiddle = new Label("");
        Label imgContentMiddleRight = new Label("");
        grid.setWidget(3, 0, imgContentMiddleLeft);
        grid.getCellFormatter().setStyleName(3, 0, "ContentOutline-l");
        grid.setWidget(3, 1, imgContentMiddle);
        grid.getCellFormatter().addStyleName(3, 1, "pojo-GenericContainer-Content");
        grid.setWidget(3, 2, imgContentMiddleRight);
        grid.getCellFormatter().setStyleName(3, 2, "ContentOutline-r");
        Label imgBottomLeft = new Label("");
        Label imgBottom = new Label("");
        Label imgBottomRight = new Label("");
        grid.setWidget(4, 0, imgBottomLeft);
        grid.getCellFormatter().setStyleName(4, 0, "ContentOutline-bl");
        grid.setWidget(4, 1, imgBottom);
        grid.getCellFormatter().setStyleName(4, 1, "ContentOutline-b");
        grid.setWidget(4, 2, imgBottomRight);
        grid.getCellFormatter().setStyleName(4, 2, "ContentOutline-br");
    }

    String getItemStyle(String cssKey, String item) {
        return "pojo-" + cssKey + item;
    }

    public Widget getHeader() {
        return grid.getWidget(1, 1);
    }

    public void setHeader(Widget header) {
        header.addStyleName("pojo-GenericContainer-Header");
        grid.setWidget(1, 1, header);
    }

    public void setContent(Widget content) {
        grid.clearCell(3, 1);
        grid.setWidget(3, 1, content);
    }

    public void setContent(Widget content, String style) {
        grid.clearCell(3, 1);
        grid.getCellFormatter().addStyleName(3, 1, style);
        grid.setWidget(3, 1, content);
    }

    public Widget getContent() {
        return grid.getWidget(3, 1);
    }
}
