package org.jcvi.vics.web.gwt.test.client;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import org.jcvi.vics.model.common.SortArgument;
import org.jcvi.vics.web.gwt.common.client.BaseEntryPoint;
import org.jcvi.vics.web.gwt.common.client.Constants;
import org.jcvi.vics.web.gwt.common.client.panel.RoundedTabPanel;
import org.jcvi.vics.web.gwt.common.client.panel.TitledBox;
import org.jcvi.vics.web.gwt.common.client.popup.BasePopupPanel;
import org.jcvi.vics.web.gwt.common.client.popup.WidgetBasePopupPanelController;
import org.jcvi.vics.web.gwt.common.client.popup.WidgetPopupController;
import org.jcvi.vics.web.gwt.common.client.service.log.Logger;
import org.jcvi.vics.web.gwt.common.client.ui.imagebundles.ImageBundleFactory;
import org.jcvi.vics.web.gwt.common.client.ui.link.ActionLink;
import org.jcvi.vics.web.gwt.common.client.ui.link.HelpActionLink;
import org.jcvi.vics.web.gwt.common.client.ui.link.InfoActionLink;
import org.jcvi.vics.web.gwt.common.client.ui.table.SortableTable;
import org.jcvi.vics.web.gwt.common.client.ui.table.TableCell;
import org.jcvi.vics.web.gwt.common.client.ui.table.TableRow;
import org.jcvi.vics.web.gwt.common.client.ui.table.columns.TextColumn;
import org.jcvi.vics.web.gwt.common.client.ui.table.paging.DataRetrievedListener;
import org.jcvi.vics.web.gwt.common.client.ui.table.paging.PagedDataRetriever;
import org.jcvi.vics.web.gwt.common.client.ui.table.paging.RemotePagingPanel;
import org.jcvi.vics.web.gwt.common.client.ui.table.paging.RemotingPaginator;
import org.jcvi.vics.web.gwt.common.client.util.HtmlUtils;
import org.jcvi.vics.web.gwt.common.client.util.StringUtils;
import org.jcvi.vics.web.gwt.common.google.user.client.ui.TabBar;
import org.jcvi.vics.web.gwt.common.google.user.client.ui.TabPanel;
import java.util.ArrayList;
import java.util.List;

public class Test extends BaseEntryPoint {

    private static Logger _logger = Logger.getLogger("test");

    public void onModuleLoad() {
        clear();
        setBreadcrumb(new Breadcrumb(Constants.RESEARCH_HOME_LABEL), Constants.ROOT_PANEL_NAME);
        VerticalPanel mainHomePanel = new VerticalPanel();
        mainHomePanel.add(getTableTestPanel());
        mainHomePanel.add(HtmlUtils.getHtml("&nbsp;", "spacer"));
        mainHomePanel.add(getIconTestPanel());
        RootPanel.get(Constants.ROOT_PANEL_NAME).add(mainHomePanel);
        show();
    }

    private Panel getTableTestPanel() {
        SortableTable table = new SortableTable();
        table.addColumn(new TextColumn("Column 1"));
        table.addColumn(new TextColumn("Column 2"));
        table.addColumn(new TextColumn("Column 3"));
        String[][] sortConstants = new String[][] { { "stuff", "Column 1" } };
        final RemotingPaginator paginator = new RemotingPaginator(table, new DataRetriever(), sortConstants, "TableTest");
        RemotePagingPanel pagingPanel = new RemotePagingPanel(table, new String[] { "5", "10", "20" }, false, true, paginator, false, RemotePagingPanel.ADVANCEDSORTLINK_IN_THE_HEADER, 10, "TableTest");
        new Timer() {

            public void run() {
                paginator.first();
            }
        }.schedule(1000);
        TitledBox panel = new TitledBox("table test");
        panel.add(pagingPanel);
        return panel;
    }

    private class DataRetriever implements PagedDataRetriever {

        private Integer _numRows = 58;

        public void retrieveTotalNumberOfDataRows(DataRetrievedListener listener) {
            listener.onSuccess(_numRows);
        }

        public void retrieveDataRows(int startIndex, int numRows, SortArgument[] sortArgument, DataRetrievedListener listener) {
            List<TableRow> rows = new ArrayList();
            for (int i = startIndex + 1; i < _numRows; i++) rows.add(getRow(i));
            listener.onSuccess(rows);
        }

        private TableRow getRow(int index) {
            TableRow row = new TableRow();
            row.setValue(0, new TableCell(String.valueOf(index)));
            row.setValue(1, new TableCell("---" + String.valueOf(index) + "---"));
            row.setValue(2, new TableCell("really really long text goes here to make the table wider and wider"));
            return row;
        }
    }

    private Panel getIconTestPanel() {
        HorizontalPanel panel = new HorizontalPanel();
        panel.add(getIconTestPanel1());
        panel.add(HtmlUtils.getHtml("&nbsp;", "spacer"));
        panel.add(getIconTestPanel2());
        return panel;
    }

    private Panel getIconTestPanel2() {
        final String text = "This is very long text to test the wrapping func that will wrap very long text at 50 or so characters, so hopefully this line is long enough to generate at least 3 rows of text.";
        BasePopupPanel popup = new BasePopupPanel("title") {

            protected void populateContent() {
                add(HtmlUtils.getHtml(StringUtils.wrapTextAsHTML(text, 50), "text"));
            }
        };
        InfoActionLink infoLink = new InfoActionLink("", popup);
        FlexTable grid = new FlexTable();
        TabBar bar = new TabBar();
        bar.addTab("foo");
        bar.addTab("myTabPanel");
        bar.addTab("baz");
        grid.setWidget(0, 0, (HtmlUtils.getHtml("TabBar:", "prompt")));
        grid.setWidget(0, 1, bar);
        com.google.gwt.user.client.ui.TabPanel gwtTabPanel = new com.google.gwt.user.client.ui.TabPanel();
        gwtTabPanel.add(new HTML("foo contents"), "foo");
        gwtTabPanel.add(new HTML("bar contents"), "bar");
        gwtTabPanel.add(new HTML("baz contents"), "baz");
        gwtTabPanel.selectTab(1);
        grid.setWidget(1, 0, (HtmlUtils.getHtml("GWT TabPanel:", "prompt")));
        grid.setWidget(1, 1, gwtTabPanel);
        TabPanel myTabPanel = new TabPanel();
        myTabPanel.add(new HTML("foo contents"), "foo");
        myTabPanel.add(new HTML("bar contents"), "bar");
        myTabPanel.add(new HTML("baz contents"), "baz");
        myTabPanel.selectTab(1);
        grid.setWidget(2, 0, (HtmlUtils.getHtml("My TabPanel:", "prompt")));
        grid.setWidget(2, 1, myTabPanel);
        RoundedTabPanel roundedTabPanel = new RoundedTabPanel();
        roundedTabPanel.add(new HTML("foo contents"), "foo");
        roundedTabPanel.add(new HTML("bar contents"), "bar");
        roundedTabPanel.add(new HTML("baz contents"), "baz");
        roundedTabPanel.selectTab(1);
        grid.setWidget(3, 0, (HtmlUtils.getHtml("My TabPanel:", "prompt")));
        grid.setWidget(3, 1, roundedTabPanel);
        TitledBox panel = new TitledBox("tab test");
        panel.removeActionLinks();
        panel.addActionLink(new ActionLink("stuff"));
        panel.add(grid);
        panel.add(infoLink);
        return panel;
    }

    private Widget getIconTestPanel1() {
        TitledBox panel = new TitledBox("icon test");
        panel.addActionLink(new ActionLink("stuff"));
        panel.addActionLink(new HelpActionLink("help"));
        panel.add(HtmlUtils.getHtml("fixed BasePopupPanel show():", "prompt"));
        Image image0 = ImageBundleFactory.getControlImageBundle().getHelpImage().createImage();
        final BasePopupPanel popup0 = new BasePopupPanel("title", true) {

            protected void populateContent() {
                add(HtmlUtils.getHtml("stuff", "text"));
            }
        };
        image0.addMouseListener(new MouseListenerAdapter() {

            public void onMouseEnter(Widget sender) {
                popup0.show();
            }
        });
        panel.add(image0);
        panel.add(HtmlUtils.getHtml("--", "spacer"));
        panel.add(HtmlUtils.getHtml("InfoActionLink:", "prompt"));
        BasePopupPanel popup = new BasePopupPanel("title") {

            protected void populateContent() {
                add(HtmlUtils.getHtml("contents", "text"));
                add(HtmlUtils.getHtml("more contents", "text"));
                add(HtmlUtils.getHtml("more contents", "text"));
            }
        };
        InfoActionLink infoLink = new InfoActionLink("", popup);
        panel.add(infoLink);
        panel.add(HtmlUtils.getHtml("--", "spacer"));
        panel.add(HtmlUtils.getHtml("fixed DialogBox show()", "prompt"));
        final DialogBox popup2 = new DialogBox();
        popup2.setText("draggable title");
        popup2.add(HtmlUtils.getHtml("contents", "text"));
        Image image2 = ImageBundleFactory.getControlImageBundle().getHelpImage().createImage();
        image2.addMouseListener(new MouseListenerAdapter() {

            public void onMouseEnter(Widget sender) {
                popup2.show();
            }
        });
        panel.add(image2);
        panel.add(HtmlUtils.getHtml("--", "spacer"));
        panel.add(HtmlUtils.getHtml("WidgetPopupController", "prompt"));
        Image image3 = ImageBundleFactory.getControlImageBundle().getHelpImage().createImage();
        DialogBox popup3 = new DialogBox() {

            public void onBrowserEvent(Event event) {
                super.onBrowserEvent(event);
            }
        };
        popup3.setText("draggable title 3");
        popup3.add(HtmlUtils.getHtml("contents", "text"));
        new WidgetPopupController(image3, popup3);
        panel.add(image3);
        panel.add(HtmlUtils.getHtml("--", "spacer"));
        panel.add(HtmlUtils.getHtml("WidgetBasePopupPanelController", "prompt"));
        Image image4 = ImageBundleFactory.getControlImageBundle().getInfoImage().createImage();
        BasePopupPanel popup4 = new BasePopupPanel("draggable title 4", true) {

            protected void populateContent() {
                add(HtmlUtils.getHtml("contents", "text"));
            }
        };
        new WidgetBasePopupPanelController(image4, popup4);
        _logger.debug("image4...");
        popup4.addMouseListener(new MouseListenerAdapter() {

            public void onMouseEnter(Widget sender) {
                _logger.debug("mouse entered popup");
            }

            public void onMouseLeave(Widget sender) {
                _logger.debug("mouse left popup");
            }
        });
        panel.add(image4);
        return panel;
    }
}
