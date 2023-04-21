package org.rsn.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.CardLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.portal.Portal;
import com.gwtext.client.widgets.portal.PortalColumn;
import com.gwtext.client.widgets.portal.Portlet;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Recommendation implements EntryPoint {

    /**
	 * This is the entry point method.
	 */
    private RecommendationServiceAsync recommendationService;

    private RecommendationModel recommendationModel;

    public void onModuleLoad() {
        recommendationService = (RecommendationServiceAsync) GWT.create(RecommendationService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) recommendationService;
        String moduleRelativeUrl = GWT.getModuleBaseURL() + "rpc";
        endpoint.setServiceEntryPoint(moduleRelativeUrl);
        recommendationModel = new RecommendationModel(this);
        recommendationModel.init();
        Panel panel = new Panel();
        panel.setBorder(false);
        panel.setLayout(new BorderLayout());
        panel.setWidth("100%");
        Panel northPanel = new Panel();
        northPanel.setBorder(false);
        northPanel.setCls("northPanel");
        Toolbar northTopToolbar = new Toolbar();
        northPanel.setTopToolbar(northTopToolbar);
        ToolbarTextItem loginItem = new ToolbarTextItem("Welcome:" + Utils.getLoggedInUserName());
        northTopToolbar.addItem(loginItem);
        Panel northInnerPanel = new Panel();
        northInnerPanel.setBorder(false);
        northInnerPanel.setCls("northInnerPanel");
        TextBox searchTb = new TextBox();
        searchTb.setWidth("200px");
        Button searchBtn = new Button("search");
        searchBtn.setStyleName("searchButton");
        northInnerPanel.add(searchTb);
        northInnerPanel.add(searchBtn);
        northPanel.add(northInnerPanel);
        panel.add(northPanel, new BorderLayoutData(RegionPosition.NORTH));
        Panel centerPanel = new Panel();
        centerPanel.setWidth("100%");
        centerPanel.setHeight(800);
        centerPanel.setBorder(false);
        centerPanel.setPaddings(15);
        centerPanel.setLayout(new FitLayout());
        BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
        panel.add(centerPanel, centerData);
        Portal portal = new Portal();
        PortalColumn firstCol = new PortalColumn();
        firstCol.setPaddings(10, 10, 0, 10);
        Portlet calendarPortlet = new Portlet();
        calendarPortlet.setTitle("Schedual");
        Panel horizontalPanel = new Panel();
        horizontalPanel.setBorder(false);
        horizontalPanel.setLayout(new HorizontalLayout(10));
        Panel calendarPanel = new Panel();
        final CalendarWidget calendar = new CalendarWidget();
        calendarPanel.add(calendar);
        final Panel todoPanel = new Panel();
        todoPanel.setLayout(new CardLayout());
        todoPanel.add(new HTML("<p>Today's Todo List</p><ul style='font-size: x-small;'><li>10:00-11:00 Student tutorial</li><li>11:00-12:00 Meeting</li><li>12:30-13:30 Lunch</li><li>15:00-16:30 Lecture</li><ul>"));
        todoPanel.setActiveItem(0);
        horizontalPanel.add(calendarPanel);
        horizontalPanel.add(todoPanel);
        calendarPortlet.add(horizontalPanel);
        firstCol.add(calendarPortlet);
        Portlet wallPortlet = new Portlet();
        wallPortlet.setTitle("The Wall");
        Panel tableWrapper = new Panel();
        tableWrapper.setStyleName("tableWrapper");
        tableWrapper.add(addWallMessage("Yang", "Please be aware of the works below which may have an affect on UCAS access tomorrow and Thursday 19th Feb. If access into B53 via the revolving doors is restricted, all UCAS delegates will need to be directed towards the doors on the North (banks) side of the Atrium. Signs and door access arrangements will be put in place for these two days.", 1));
        tableWrapper.add(addWallMessage("Rabbit", "Hi, Gary. Do you want to go to lunch together today. Coz I found a pretty good pub around univeristy.", 2));
        tableWrapper.add(addWallMessage("Pei", "Professor Don Nutbeam, new VC and Peter Staniczenko, Head of Corporate Planning will be visiting the School on 19 October 2009, 10.00 to 12.30, venue TBC.  This meeting is open to all levels of staff and the agenda will be confirmed in due course.", 3));
        tableWrapper.add(addWallMessage("Priyanka", "Here are Jiri's thoughts on the list of issues you developed As he says, ask him as many questions as you can :-)", 4));
        wallPortlet.add(tableWrapper);
        firstCol.add(wallPortlet);
        portal.add(firstCol, new ColumnLayoutData(.30));
        PortalColumn secondCol = new PortalColumn();
        secondCol.setPaddings(10, 10, 0, 10);
        PublicationPortlet publicationPortlet = new PublicationPortlet(this, "Daily Publication Recommendation");
        publicationPortlet.init();
        secondCol.add(publicationPortlet);
        portal.add(secondCol, new ColumnLayoutData(.40));
        PortalColumn thirdCol = new PortalColumn();
        thirdCol.setPaddings(10, 10, 0, 10);
        ScholarPortlet scholarPortlet = new ScholarPortlet(this, "Collaboration Recommendation");
        scholarPortlet.init();
        thirdCol.add(scholarPortlet);
        Portlet emailPort = new Portlet();
        emailPort.setTitle("Email");
        emailPort.setLayout(new FitLayout());
        GridPanel emailGrid = getEmailGrid();
        emailGrid.setFrame(false);
        emailPort.add(emailGrid);
        thirdCol.add(emailPort);
        portal.add(thirdCol, new ColumnLayoutData(.30));
        centerPanel.add(portal);
        Viewport viewport = new Viewport(panel);
        recommendationModel.refresh();
    }

    public RecommendationServiceAsync getService() {
        return this.recommendationService;
    }

    public RecommendationModel getModel() {
        return this.recommendationModel;
    }

    private FlexTable addWallMessage(String name, String message, int index) {
        FlexTable t = new FlexTable();
        FlexCellFormatter cellFormatter = t.getFlexCellFormatter();
        t.setHTML(0, 0, "<img src=\"" + Utils.getImageBaseURL() + "/recommendation/face" + String.valueOf(index) + ".jpg\" />");
        t.setHTML(0, 1, "<b>" + name + " wrote:</b>");
        t.setHTML(1, 0, "<p>" + message + "</p>");
        cellFormatter.setHeight(0, 1, "20%");
        cellFormatter.setWidth(0, 1, "100%");
        cellFormatter.setAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
        t.setWidth("100%");
        t.setStyleName("WallTable");
        cellFormatter.setRowSpan(0, 0, 2);
        cellFormatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        cellFormatter.setWidth(0, 0, "50px");
        cellFormatter.setHeight(0, 0, "50px");
        return t;
    }

    private Object[][] getEmailData() {
        return new Object[][] { new Object[] { "Rabbit", "Hello1", "I say hello once", "28/12/2008" }, new Object[] { "Yang", "Hello2", "I say hello twice", "29/12/2008" }, new Object[] { "Pei", "Hello3", "I say hello for the third time", "30/12/2008" }, new Object[] { "Mike", "Hello4", "I say hello for the fourth time", "31/12/2008" }, new Object[] { "David", "Hello5", "I say hello for the fifth time", "1/1/2009" } };
    }

    private GridPanel getEmailGrid() {
        RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("from"), new StringFieldDef("title"), new StringFieldDef("content"), new DateFieldDef("date", "j/n/Y") });
        GridPanel grid = new GridPanel();
        Object[][] data = getEmailData();
        MemoryProxy proxy = new MemoryProxy(data);
        ArrayReader reader = new ArrayReader(recordDef);
        Store store = new Store(proxy, reader);
        store.load();
        grid.setStore(store);
        ColumnConfig[] columns = new ColumnConfig[] { new ColumnConfig("From", "from", 40, true, null, "from"), new ColumnConfig("Title", "title", 80), new ColumnConfig("Content", "content", 40), new ColumnConfig("Date", "date", 100) };
        ColumnModel columnModel = new ColumnModel(columns);
        grid.setColumnModel(columnModel);
        grid.setFrame(false);
        grid.setStripeRows(false);
        grid.setAutoExpandColumn("from");
        grid.setWidth("100%");
        grid.setBorder(false);
        return grid;
    }
}
