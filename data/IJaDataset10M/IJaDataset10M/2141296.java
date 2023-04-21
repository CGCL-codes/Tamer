package org.gwm.samples.gwmdemo.client;

import org.gwm.client.GDesktopPane;
import org.gwm.client.GFrame;
import org.gwm.client.GInternalFrame;
import org.gwm.client.impl.DefaultGDesktopPane;
import org.gwm.client.impl.DefaultGFrame;
import org.gwm.client.impl.DefaultGInternalFrame;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class MultiDesktopsScenarii extends AbstractScenarii {

    public MultiDesktopsScenarii() {
        super();
    }

    public void runScenarii() {
        GFrame window = new DefaultGFrame("");
        window.setWidth(1200);
        window.setHeight(600);
        GDesktopPane desk1 = new DefaultGDesktopPane();
        desk1.setTheme("default");
        GDesktopPane desk2 = new DefaultGDesktopPane();
        desk2.setTheme("sky");
        GInternalFrame f1desk1 = new DefaultGInternalFrame("Google");
        f1desk1.setUrl("http://www.google.com");
        desk1.addFrame(f1desk1);
        f1desk1.setVisible(true);
        f1desk1.setSize(500, 400);
        GInternalFrame f2desk1 = new DefaultGInternalFrame("GWT Official Web Site");
        f2desk1.setUrl("http://code.google.com/webtoolkit/");
        desk1.addFrame(f2desk1);
        f2desk1.setVisible(true);
        f2desk1.setSize(500, 400);
        GInternalFrame f1desk2 = new DefaultGInternalFrame("Yahoo");
        f1desk2.setUrl("http://www.yahoo.com");
        desk2.addFrame(f1desk2);
        f1desk2.setVisible(true);
        f1desk2.setSize(500, 400);
        GInternalFrame f2desk2 = new DefaultGInternalFrame("MSN");
        f2desk2.setUrl("http://www.gwtwindowmanager.org");
        desk2.addFrame(f2desk2);
        f2desk2.setVisible(true);
        f2desk2.setSize(500, 400);
        FlexTable desktopsContainer = new FlexTable();
        desktopsContainer.setBorderWidth(1);
        desktopsContainer.setSize("100%", "100%");
        Button goBackBtn = new Button("Go Back to the demo menu");
        goBackBtn.addClickListener(new ClickListener() {

            public void onClick(Widget source) {
                GwmDemo.reset();
            }
        });
        Button resetScenarii = new Button("Reload Sample");
        resetScenarii.addClickListener(new ClickListener() {

            public void onClick(Widget source) {
                runScenarii();
            }
        });
        desktopsContainer.setWidget(0, 0, goBackBtn);
        desktopsContainer.setWidget(0, 1, resetScenarii);
        desktopsContainer.setWidget(1, 0, (Widget) desk1);
        desktopsContainer.setWidget(1, 1, (Widget) desk2);
        desktopsContainer.getFlexCellFormatter().setHeight(1, 0, "100%");
        desktopsContainer.getFlexCellFormatter().setWidth(1, 0, "50%");
        desktopsContainer.getFlexCellFormatter().setWidth(1, 1, "50%");
        window.setContent(desktopsContainer);
        window.setOutlineDragMode(true);
        window.setVisible(true);
        RootPanel.get().clear();
        RootPanel.get().add(desktopsContainer);
    }

    protected Hyperlink createLink() {
        Hyperlink simpleDemo = new Hyperlink("Mini Desktops", "");
        return simpleDemo;
    }
}
