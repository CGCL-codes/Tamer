package lv.webkursi.client;

import java.util.ArrayList;
import java.util.List;
import lv.webkursi.client.model.RssItem;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TopicTree implements EntryPoint {

    /**
	 * This is the entry point method.
	 */
    public void onModuleLoad() {
        List items1 = new ArrayList();
        items1.add(new RssItem(2, "http://www.delfi.lv", "This section explains which " + "classes and methods are available in GWT. " + "Check this list to make sure you're using stuff " + "that can be translated.", "Content2"));
        items1.add(new RssItem(3, "http://www.tvnet.lv", "TVNet abstract", "Content3"));
        items1.add(new RssItem(4, "http://www.apollo.lv", "Apollo abstract", "Content4"));
        RssWidget scienceRss = new RssWidget("Informātika", items1);
        RootPanel.get("leftColumn").add(scienceRss);
        List items2 = new ArrayList();
        items2.add(new RssItem(12, "http://www.google.com", "Google content", "Content12"));
        items2.add(new RssItem(13, "http://www.yahoo.com", "Yahoo abstract", "Content13"));
        items2.add(new RssItem(14, "http://www.ask.com", "Ask Jeeves abstract", "Content14"));
        RssWidget educationRss = new RssWidget("Izglītība", items2);
        RootPanel.get("rightColumn").add(educationRss);
        VerticalPanel vPanel = new VerticalPanel();
        LoginWidget loginWidget = new LoginWidget();
        vPanel.add(loginWidget);
        RootPanel.get("toolPanel").add(vPanel);
        HTML switch1 = new HTML("<br /><div class='btn'><a href='#'>Matemātikas uzdevumi</a></div>");
        HTML switch2 = new HTML("<br /><div class='btn'><a href='#'>Testi</a></div>");
        final Label label = new Label();
        HorizontalPanel hPanel = new HorizontalPanel();
        HTML homeLink = new HTML("<a href='#'>Uz sākumu</a>&nbsp;&nbsp;");
        HTML settingsLink = new HTML("<a href='#'>Iestatījumi</a>");
        hPanel.add(homeLink);
        hPanel.add(settingsLink);
        switch1.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                if (label.getText().equals("")) label.setText("Hello World!"); else label.setText("");
            }
        });
        switch2.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                Window.alert("Tests not implemented yet");
            }
        });
        vPanel.add(switch1);
        vPanel.add(switch2);
        vPanel.add(label);
        RootPanel.get("topMenu").add(hPanel);
    }
}
