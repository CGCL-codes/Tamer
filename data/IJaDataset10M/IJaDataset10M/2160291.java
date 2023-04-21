package com.tilents.client.panel;

import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.tilents.client.PanelFactory;
import com.tilents.client.data.Userinfo;
import com.tilents.client.mywidget.TButton;
import com.tilents.client.mywidget.Canvas.SearchForm;
import com.tilents.client.mywidget.callback.LoginRequestCallback;
import com.tilents.client.parent.ParentShowWindows;
import com.tilents.client.util.PubFnClient;

public class Memo extends ParentShowWindows {

    private static final String DESCRIPTION = "" + "Welcome";

    public static class Factory implements PanelFactory {

        private static Memo instance = null;

        private String id = "Memo";

        public Canvas create() {
            if (instance == null) {
                instance = new Memo();
            } else {
                instance.destroy();
                instance = new Memo();
            }
            return instance;
        }

        public String getID() {
            return id;
        }

        public String getDescription() {
            return DESCRIPTION;
        }
    }

    public Memo() {
        super();
    }

    @Override
    public Canvas getViewPanel() {
        HLayout mainLayOut = new HLayout();
        mainLayOut.setBackgroundColor("green");
        TButton bt = new TButton();
        final String login = "?defaultdomain=" + Userinfo.isDefaultdomain() + "&secure=" + Userinfo.isSecure();
        bt.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Window.open("/LoginServlet" + login, "_self", "");
            }
        });
        mainLayOut.setWidth100();
        mainLayOut.setHeight100();
        SearchForm sf = new SearchForm();
        Label label = new Label();
        label.setHeight(30);
        label.setPadding(10);
        label.setAlign(Alignment.CENTER);
        label.setValign(VerticalAlignment.CENTER);
        label.setWrap(false);
        label.setShowEdges(true);
        label.setContents("<FORM NAME=index METHOD=POST ACTION=\"/LoginServlet\">\n" + "<div class=\"rightcontent\">\n" + "<p class=title>请登陆 </p>\n" + "<div class=\"content\">\n" + "<input type=\"hidden\" name=\"defaultdomain\" value=\"true\">" + "<input type=\"hidden\" name=\"secure\" value=\"false\">" + "  <p><input type=submit value=\"用google账号登陆\">\n" + "</p>\n" + "</div>\n" + "</div>\n" + "</FORM>");
        mainLayOut.addMember(label);
        return mainLayOut;
    }

    public String getIntro() {
        return DESCRIPTION;
    }
}
