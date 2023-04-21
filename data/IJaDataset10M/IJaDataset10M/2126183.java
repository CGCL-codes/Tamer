package net.sipvip.client;

import net.sipvip.shared.CommonJSNI;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dev.jjs.ast.js.JsonObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Start {

    static final DecoratedTabPanel tp = new DecoratedTabPanel();

    private static class PopUpCommand implements Command, ClickHandler {

        private FlexTable fltable = new FlexTable();

        public PopUpCommand(FlexTable rssfeed) {
            this.fltable = rssfeed;
        }

        public void execute() {
            new PopUpPanelDialog(fltable).show();
        }

        @Override
        public void onClick(ClickEvent event) {
            new PopUpPanelDialog(fltable).show();
        }
    }

    private static class PopUpPanelDialog extends DecoratedPopupPanel {

        public PopUpPanelDialog(FlexTable flextable) {
            this.setGlassEnabled(true);
            this.setAnimationEnabled(true);
            this.setWidth("670px");
            FlexTable fltableinternal = flextable;
            Button closeBut = new Button("Close");
            closeBut.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    PopUpPanelDialog.this.hide();
                }
            });
            Frame frameInsideSmall = new Frame("/adserv_insidesmall");
            frameInsideSmall.setWidth("490px");
            frameInsideSmall.setHeight("90px");
            DOM.setElementProperty(frameInsideSmall.getElement(), "frameBorder", "0");
            VerticalPanel panel = new VerticalPanel();
            panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            panel.setSpacing(15);
            panel.add(closeBut);
            panel.add(frameInsideSmall);
            panel.add(fltableinternal);
            setWidget(panel);
            this.center();
        }
    }

    public static void StartRootPanel(final DockPanel dockPanel) {
        JsArray<LinkContextJSNI> arr = CommonJSNI.getJsonContext();
        JsArray<DomainObjJSNI> arrdomain = CommonJSNI.getJsonDomain();
        Document doc = Document.get();
        String domain = doc.getDomain();
        GWT.log("!!!COUNT l " + arrdomain.length());
        String facebookid = arrdomain.get(0).getFacebookid();
        GWT.log("!!!COUNT l2 " + facebookid);
        VerticalPanel leftVertPanel = new VerticalPanel();
        leftVertPanel.setSpacing(20);
        leftVertPanel.setBorderWidth(1);
        HTML facebookcomments = new HTML("<fb:comments xid='" + facebookid + "' css='http://" + domain + "/fb_comments.css' numposts='4' width='230px' />");
        leftVertPanel.add(facebookcomments);
        MenuBar mainLeft = new MenuBar(true);
        for (int i = 6; i < arr.length(); i++) {
            JsArray<RssContextObjJSNI> arrtree = arr.get(i).getRssContextJSNI();
            MenuBar upLeftMenu = new MenuBar(true);
            FlexTable flextabletree = new FlexTable();
            flextabletree.setCellPadding(10);
            flextabletree.setCellSpacing(10);
            flextabletree.setBorderWidth(1);
            for (int k = 0; k < arrtree.length(); k++) {
                flextabletree.setWidget(k, 0, new HTML("<b>" + arrtree.get(k).getTitle() + "</b>"));
                flextabletree.setWidget(k, 1, new HTML(arrtree.get(k).getDescription()));
                PopUpCommand lefttab = new PopUpCommand(flextabletree);
                upLeftMenu.addItem(arrtree.get(k).getTitle(), lefttab);
            }
            mainLeft.addItem(arr.get(i).getTab(), upLeftMenu);
            leftVertPanel.add(mainLeft);
        }
        tp.setAnimationEnabled(true);
        for (int i = 0; i < 6; i++) {
            FlexTable flextable = new FlexTable();
            flextable.setCellPadding(10);
            flextable.setCellSpacing(10);
            flextable.setBorderWidth(1);
            JsArray<RssContextObjJSNI> arr2 = arr.get(i).getRssContextJSNI();
            for (int k = 0; k < arr2.length(); k++) {
                flextable.setWidget(k, 0, new HTML("<b>" + arr2.get(k).getTitle() + "</b>"));
                flextable.setWidget(k, 1, new HTML(arr2.get(k).getDescription()));
            }
            tp.setTitle(arr.get(i).getTab());
            tp.add(flextable, arr.get(i).getTab());
        }
        Frame frameRight = new Frame("/adserv_right");
        frameRight.setWidth("200px");
        frameRight.setHeight("650px");
        DOM.setElementProperty(frameRight.getElement(), "frameBorder", "0");
        Frame frameUp = new Frame("/adserv_upimage");
        frameUp.setWidth("770px");
        frameUp.setHeight("125px");
        DOM.setElementProperty(frameUp.getElement(), "frameBorder", "0");
        dockPanel.setWidth("100%");
        dockPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        dockPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        dockPanel.setSpacing(30);
        tp.selectTab(0);
        dockPanel.add(tp, DockPanel.CENTER);
        dockPanel.add(frameRight, DockPanel.EAST);
        dockPanel.add(frameUp, DockPanel.NORTH);
        dockPanel.add(leftVertPanel, DockPanel.WEST);
    }
}
