package com.afaker.rss.gui.table;

import com.afaker.rss.context.ApplicationContext;
import com.sun.syndication.feed.module.DCModule;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import java.util.ArrayList;
import java.util.Date;
import com.afaker.rss.feed.*;

/**
 *
 * @author bruce
 */
public class ExtractEntryInfoTemplate {

    private FeedEntry entry;

    private Object obj[];

    private ArrayList<String> list = new ArrayList<String>();

    private ArrayList<Class> types = new ArrayList<Class>();

    private ApplicationContext appContext = ApplicationContext.getApplicationContext();

    /** Creates a new instance of ExtractEntryInfoTemplate */
    public ExtractEntryInfoTemplate() {
    }

    final Object[] extract(FeedEntry entry, boolean isAll) {
        DCModule dc;
        int j = 1;
        obj = new Object[8];
        obj[j] = entry.getTitle();
        if (isAll) {
            obj[++j] = entry.getAuthor();
            if (obj[j] != null && obj[j] != "") {
                if (!list.contains(appContext.getProperty("author.col.name"))) {
                    list.add(appContext.getProperty("author.col.name"));
                    types.add(String.class);
                }
            } else {
                j--;
            }
            if (entry.getModules().size() > 0) {
                dc = (DCModule) entry.getModules().get(0);
                obj[++j] = dc.getPublisher();
                if (obj[j] != null) {
                    list.add(appContext.getProperty("publisher.col.name"));
                    types.add(String.class);
                } else {
                    j--;
                }
            }
            if (entry.getCategories().size() > 0) {
                SyndCategoryImpl catagory = (SyndCategoryImpl) entry.getCategories().get(0);
                obj[++j] = catagory.getName();
                if (obj[j] != null) {
                    if (!list.contains(appContext.getProperty("category.col.name"))) {
                        list.add(appContext.getProperty("category.col.name"));
                        types.add(String.class);
                    }
                } else {
                    j--;
                }
            }
        }
        if (entry.getPublishedDate() != null) {
            obj[++j] = entry.getPublishedDate();
            if (obj[j] != null && obj[j] != "") {
                if (!list.contains(appContext.getProperty("publishdate.col.name"))) {
                    list.add(appContext.getProperty("publishdate.col.name"));
                    types.add(Date.class);
                }
            }
        }
        hook();
        return obj;
    }

    public ArrayList<Class> getTypes() {
        return types;
    }

    public ArrayList<String> getColumnNames() {
        return list;
    }

    private void hook() {
    }
}
