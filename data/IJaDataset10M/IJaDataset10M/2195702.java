package sourceforge.pebblesframewor.gwt.client.windows;

import sourceforge.pebblesframewor.gwt.client.InterfaceConstants;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
* DimensionWindow
* @author JunSun Whang
* @version $Id: DimensionWindow.java 105 2009-02-21 06:02:27Z junsunwhang $
*/
public class DimensionWindow extends WindowPanel {

    public DimensionWindow(final WindowController windowController, Label headerWidget, Image infoImage) {
        super(windowController, headerWidget, infoImage);
        initialize();
    }

    public DimensionWindow(final WindowController windowController, Label headerWidget, Image infoImage, boolean scrollRegion) {
        super(windowController, headerWidget, infoImage, scrollRegion);
        initialize();
    }

    private void initialize() {
        infoImage.setStyleName(InterfaceConstants.CSS_PEBBLES_WIDGET_HEADER);
        infoLabel.setStyleName(InterfaceConstants.CSS_PEBBLES_WIDGET_HEADER);
        headerPanel.setStyleName(InterfaceConstants.CSS_RESIZE_PANEL_HEADER_ACTIVE);
        this.setStyleName(InterfaceConstants.CSS_PEBBLES_WIDGET);
        this.setContentSize(InterfaceConstants.INT_DEFAULT_WIDGET_SIZE_WIDTH, InterfaceConstants.INT_DEFAULT_WIDGET_SIZE_HEIGHT);
        contentWidget = getWidgetPanel(180, 180);
        contentWrapper = new ScrollPanel(contentWidget);
        masterVerticalContent.add(contentWrapper);
    }

    public WindowPanel cloneWidget() {
        return null;
    }

    public void instantiateAggregatedComponents() {
    }

    public void adjustInternalPane(int width, int height) {
        if (width > InterfaceConstants.INT_DEFAULT_CONTENT_SIZE_WIDTH) {
            contentWidget.setWidth(String.valueOf(width) + "px");
        }
        if (height > InterfaceConstants.INT_DEFAULT_CONTENT_SIZE_HEIGHT) {
            contentWidget.setHeight(String.valueOf(height) + "px");
        }
        contentWrapper.setPixelSize(contentWidth, contentHeight);
    }

    private Panel getWidgetPanel(int width, int height) {
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setPixelSize(width, height);
        verticalPanel.setSize("100%", "100%");
        Label widgetLabel = new Label("Widget");
        widgetLabel.setStyleName(InterfaceConstants.CSS_PEBBLES_WIDGET_BANDING);
        verticalPanel.add(widgetLabel);
        VerticalPanel verticalPanel_8 = new VerticalPanel();
        verticalPanel.add(verticalPanel_8);
        Label test8 = new Label("Product Family");
        verticalPanel_8.add(test8);
        VerticalPanel verticalPanel_10 = new VerticalPanel();
        verticalPanel.add(verticalPanel_10);
        Label test10 = new Label("Sales");
        verticalPanel_10.add(test10);
        Label columnsLabel = new Label("Columns");
        columnsLabel.setStyleName(InterfaceConstants.CSS_PEBBLES_WIDGET_BANDING);
        verticalPanel.add(columnsLabel);
        VerticalPanel verticalPanel_9 = new VerticalPanel();
        verticalPanel.add(verticalPanel_9);
        Label test9 = new Label("Gender");
        verticalPanel_9.add(test9);
        Label rowsLabel = new Label("Rows");
        rowsLabel.setStyleName(InterfaceConstants.CSS_PEBBLES_WIDGET_BANDING);
        verticalPanel.add(rowsLabel);
        VerticalPanel verticalPanel_6 = new VerticalPanel();
        verticalPanel.add(verticalPanel_6);
        Label test6 = new Label("Country");
        verticalPanel_6.add(test6);
        VerticalPanel verticalPanel_7 = new VerticalPanel();
        verticalPanel.add(verticalPanel_7);
        Label test7 = new Label("State");
        verticalPanel_7.add(test7);
        Label pagesLabel = new Label("Pages");
        pagesLabel.setStyleName(InterfaceConstants.CSS_PEBBLES_WIDGET_BANDING);
        verticalPanel.add(pagesLabel);
        Label dimensionsLabel = new Label("Dimensions");
        dimensionsLabel.setStyleName(InterfaceConstants.CSS_PEBBLES_WIDGET_BANDING);
        verticalPanel.add(dimensionsLabel);
        Label measuresLabel = new Label("Measures");
        measuresLabel.setStyleName(InterfaceConstants.CSS_PEBBLES_WIDGET_BANDING);
        verticalPanel.add(measuresLabel);
        VerticalPanel verticalPanel_11 = new VerticalPanel();
        verticalPanel.add(verticalPanel_11);
        Label test11 = new Label("Returns");
        verticalPanel_11.add(test11);
        return verticalPanel;
    }
}
