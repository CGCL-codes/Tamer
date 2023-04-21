package com.astrientlabs.ui;

import javax.microedition.io.ConnectionNotFoundException;
import com.astrientlabs.colors.Colors;
import com.astrientlabs.i18n.TextMap;
import com.astrientlabs.jobs.FetchThumbnailJob;
import com.astrientlabs.jobs.SearchJob;
import com.astrientlabs.log.Logger;
import com.astrientlabs.midlet.Yipi;
import com.astrientlabs.search.CannedResult;
import com.astrientlabs.search.SearchQuery;
import com.astrientlabs.search.SearchResult;
import com.astrientlabs.text.Strings;
import com.astrientlabs.threads.Cron;
import com.astrientlabs.threads.JobRunner;
import com.astrientlabs.ui.util.TextInputEngine;
import com.astrientlabs.ui.util.TextInputListener;
import com.astrientlabs.util.Images;
import com.astrientlabs.util.ProgressListener;
import com.astrientlabs.util.Stats;
import com.astrientlabs.util.ThumbnailFetcher;

public class SearchWindow extends Window implements ProgressListener, TextInputListener {

    public static SearchWindow instance;

    private SearchQuery query;

    private ListWidget listWidget;

    private TextInputWidget textInputWidget;

    private LogWindow logs;

    private PopupWidget about;

    private ThumbnailFetcher thumbnailFetcher;

    private CannedSearchResultWidget moreResultsWidget;

    public SearchWindow() {
        super("search");
        moreResultsWidget = new CannedSearchResultWidget(this, Images.MORE_RESULTS, TextMap.instance.get("text.more"), "");
        thumbnailFetcher = new ThumbnailFetcher(2);
        logs = new LogWindow(this, (int) (getWidth() * .90), (int) (getHeight() * .60), Colors.POPUP_BG, 15);
        Logger.instance.setListener(logs);
        about = new PopupWidget(this, (int) (getWidth() * .90), (int) (getHeight() * .60), Colors.POPUP_BG);
        about.add(new LabelWidget(this, "version: " + Yipi.instance.getAppProperty("MIDlet-Version")));
        about.add(new LabelWidget(this, "platform: " + System.getProperty("microedition.platform")));
        about.add(new LabelWidget(this, "build date: " + Yipi.instance.getAppProperty("astrientlabs-build-date")));
        about.add(new LabelWidget(this, "total mem: " + Strings.getSizeString(Runtime.getRuntime().totalMemory())));
        about.add(new FreeMemoryWidget(this, "free mem: "));
        about.add(new ThreadActiveCountWidget(this, "threads: "));
        about.add(new CacheSizeWidget(this, "cache: "));
        about.add(new LabelWidget(this, "downloads: " + Stats.downloads));
        textInputWidget = new TextInputWidget(this, "ysh>", "", this);
        children.addElement(textInputWidget);
        this.listWidget = new ListWidget(this, getHeight() - footerHeight - headerHeight - textInputWidget.widgetHeight, Colors.BACKGROUND);
        this.listWidget.padding = 0;
        this.children.addElement(listWidget);
        focus = textInputWidget;
    }

    public void widgetSelected(Widget who, Object arg) {
        if (who == moreResultsWidget) {
            JobRunner.instance.run(new SearchJob(this, query));
        } else if (who instanceof CannedSearchResultWidget) {
            CannedResult cannedResult = (CannedResult) ((CannedSearchResultWidget) who).getValue();
            if (cannedResult.getType() == CannedResult.TYPE_POPUP) {
                if (cannedResult.getTitle().equals("about")) {
                    setPopup(about);
                } else if (cannedResult.getTitle().equals("logs")) {
                    setPopup(logs);
                }
            } else if (cannedResult.getType() == CannedResult.TYPE_SETTING) {
                boolean filtering = !Yipi.instance.systemPrefs.getBoolean("filter", false);
                Yipi.instance.systemPrefs.put("filter", String.valueOf(filtering));
                setMessage(TextMap.instance.get("text.filtering." + filtering));
            } else if (cannedResult.getType() == CannedResult.TYPE_LINK) {
                try {
                    Yipi.instance.platformRequest(cannedResult.getUrl());
                } catch (ConnectionNotFoundException e) {
                    setError(e);
                }
            }
        } else if (who instanceof SearchResultWidget) {
            SearchResultWidget searchResultWidget = (SearchResultWidget) who;
            ResultDisplayWindow.instance.setSearchResult(searchResultWidget.getSearchResult());
            JobRunner.instance.run(ResultDisplayWindow.instance);
        }
    }

    public void showNotify() {
        super.showNotify();
        thumbnailFetcher.setPaused(false);
        Cron.instance.register(textInputWidget);
    }

    public void hideNotify() {
        super.hideNotify();
        thumbnailFetcher.setPaused(true);
        Cron.instance.unregister(textInputWidget);
    }

    public void search(String queryString) {
        if (queryString != null) {
            if (queryString.trim().length() > 0) {
                if (query != null) {
                    if (query.getTerm().equals(queryString.trim())) {
                        return;
                    }
                    query.cancel();
                }
                query = new SearchQuery();
                query.setFilter(Yipi.instance.systemPrefs.getBoolean("filter", false));
                query.setTerm(queryString);
                query.setPageSize(Yipi.instance.systemPrefs.getInt("maxresults", 20));
                query.setProgressListener(this);
                thumbnailFetcher.clear();
                JobRunner.instance.run(new SearchJob(this, query));
            }
        }
    }

    public void update(Object who, long position) {
        if (who == query) {
            SearchResult tempResult = (SearchResult) ((SearchQuery) who).getResults().lastElement();
            if (position == 1) {
                listWidget.clear();
                thumbnailFetcher.clear();
            }
            if (tempResult instanceof CannedResult) {
                listWidget.add(new CannedSearchResultWidget(this, Images.CANNED_RESULT, tempResult.getTitle(), tempResult));
            } else {
                SearchResultWidget searchResultWidget = new SearchResultWidget(this, tempResult);
                listWidget.add(searchResultWidget);
                thumbnailFetcher.run(new FetchThumbnailJob(searchResultWidget, listWidget));
            }
            repaintChild(listWidget, false);
        }
    }

    public void completed(Object who) {
        if (who == query) {
            if (query.getResults().size() == query.getPageSize()) {
                listWidget.add(this.moreResultsWidget);
                repaintChild(listWidget, false);
            }
        }
    }

    public void update(TextInputEngine textInput, int event) {
        if (event == TextInputEngine.EVENT_APPEND) {
            textInputWidget.textChanged();
            repaintChild(textInputWidget, true);
        } else if (event == TextInputEngine.EVENT_CURSOR_BACK) {
            repaintChild(textInputWidget, true);
        } else if (event == TextInputEngine.EVENT_CURSOR_FLASH) {
            repaintChild(textInputWidget, false);
        } else if (event == TextInputEngine.EVENT_CURSOR_FORWARD) {
            repaintChild(textInputWidget, true);
        } else if (event == TextInputEngine.EVENT_DELETE) {
            textInputWidget.textChanged();
            repaintChild(textInputWidget, true);
        } else if (event == TextInputEngine.EVENT_IDLE) {
            search(textInputWidget.getText());
        }
    }
}
