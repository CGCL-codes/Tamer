package com.google.code.sagetvaddons.sagerss.client;

import java.util.Arrays;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

final class SearchPanel extends Grid {

    public static final String DEFAULT_MEDIA_MASK = "T";

    public static final String REGEX_SEPARATOR = ";";

    ListBox outlook;

    TextBox title;

    TextBox subtitle;

    TextBox desc;

    TextBox people;

    TextBox category;

    TextBox rated;

    TextBox extRatings;

    TextBox year;

    TextBox misc;

    TextBox airingUrl;

    ListBox isWatched;

    ListBox isFav;

    ListBox isHD;

    ListBox isPremiere;

    ListBox isFinale;

    ListBox isFirstRun;

    Button submit;

    public SearchPanel() {
        super(7, 3);
        outlook = initOutlookListBox(new ListBox());
        title = new TextBox();
        subtitle = new TextBox();
        desc = new TextBox();
        people = new TextBox();
        category = new TextBox();
        rated = new TextBox();
        extRatings = new TextBox();
        year = new TextBox();
        misc = new TextBox();
        airingUrl = new TextBox();
        isWatched = initTriOptListBox(new ListBox());
        isFav = initTriOptListBox(new ListBox());
        isHD = initTriOptListBox(new ListBox());
        isPremiere = initTriOptListBox(new ListBox());
        isFinale = initTriOptListBox(new ListBox());
        isFirstRun = initTriOptListBox(new ListBox());
        submit = new Button("Search");
        submit.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SearchShowsAsync srchSvc = GWT.create(SearchShows.class);
                srchSvc.search(generateQuery(), new AsyncCallback<ShowsQueryResponse>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("RPC failure: " + caught.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(ShowsQueryResponse result) {
                        ResultsPanel.getInstance().setContent(result);
                    }
                });
            }
        });
        setWidget(0, 0, initInputWidget(title, "Title"));
        setWidget(0, 1, initInputWidget(subtitle, "Subtitle"));
        setWidget(0, 2, initInputWidget(desc, "Description"));
        setWidget(1, 0, initInputWidget(people, "People (Credits)"));
        setWidget(1, 1, initInputWidget(category, "Category"));
        setWidget(1, 2, initInputWidget(year, "Year"));
        setWidget(2, 0, initInputWidget(rated, "Rating"));
        setWidget(2, 1, initInputWidget(extRatings, "Extended Rating"));
        setWidget(2, 2, initInputWidget(misc, "Miscellaneous Data"));
        setWidget(3, 0, initInputWidget(isWatched, "Watched Flag"));
        setWidget(3, 1, initInputWidget(isFav, "Favourites Only"));
        setWidget(3, 2, initInputWidget(isHD, "HD Flag"));
        setWidget(4, 0, initInputWidget(isPremiere, "Premieres Only"));
        setWidget(4, 1, initInputWidget(isFinale, "Finales Only"));
        setWidget(4, 2, initInputWidget(isFirstRun, "First Runs Only"));
        setWidget(5, 0, initInputWidget(outlook, "Look Ahead (days)"));
        setWidget(5, 2, initInputWidget(airingUrl, "Airing Link (%d = airing id)"));
        setWidget(6, 1, submit);
        setWidget(6, 2, new HTML("<b>SageRSS v" + Version.getFullVersion() + "</b>"));
    }

    private ShowsQueryRequest generateQuery() {
        ShowsQueryRequest req = new ShowsQueryRequest();
        req.setTitle(Arrays.asList(title.getText().split(REGEX_SEPARATOR)));
        req.setSubtitle(Arrays.asList(subtitle.getText().split(REGEX_SEPARATOR)));
        req.setDescription(Arrays.asList(desc.getText().split(REGEX_SEPARATOR)));
        req.setPeople(Arrays.asList(people.getText().split(REGEX_SEPARATOR)));
        req.setCategory(Arrays.asList(category.getText().split(REGEX_SEPARATOR)));
        req.setRated(Arrays.asList(rated.getText().split(REGEX_SEPARATOR)));
        req.setExtendedRatings(Arrays.asList(extRatings.getText().split(REGEX_SEPARATOR)));
        req.setYear(Arrays.asList(year.getText().split(REGEX_SEPARATOR)));
        req.setMiscellaneous(Arrays.asList(misc.getText().split(REGEX_SEPARATOR)));
        req.setIsFirstRun(getBooleanFromListBox(isFirstRun));
        req.setIsHD(getBooleanFromListBox(isHD));
        req.setIsPremiere(getBooleanFromListBox(isPremiere));
        req.setIsFinale(getBooleanFromListBox(isFinale));
        req.setIsFavourite(getBooleanFromListBox(isFav));
        req.setIsWatched(getBooleanFromListBox(isWatched));
        req.setOutlook(Integer.parseInt(outlook.getValue(outlook.getSelectedIndex())));
        req.setAiringUrl(airingUrl.getText());
        return req;
    }

    private static Boolean getBooleanFromListBox(ListBox b) {
        String val = b.getValue(b.getSelectedIndex());
        if (val.length() == 0) return null;
        return Boolean.parseBoolean(val);
    }

    private static Panel initInputWidget(Widget w, String lbl, Panel container) {
        if (container == null) container = new VerticalPanel();
        container.add(new HTML("<b>" + lbl + ":</b>"));
        container.add(w);
        return container;
    }

    private static Panel initInputWidget(Widget w, String lbl) {
        return initInputWidget(w, lbl, new VerticalPanel());
    }

    private static ListBox initOutlookListBox(ListBox l) {
        l.addItem("1");
        l.addItem("2");
        l.addItem("3");
        l.addItem("4");
        l.addItem("5");
        l.addItem("6");
        l.addItem("7");
        l.addItem("8");
        l.addItem("9");
        l.addItem("10");
        l.addItem("11");
        l.addItem("12");
        l.addItem("13");
        l.addItem("14");
        return l;
    }

    private static ListBox initTriOptListBox(ListBox l) {
        l.addItem("Any", "");
        l.addItem("Yes", "true");
        l.addItem("No", "false");
        return l;
    }
}
