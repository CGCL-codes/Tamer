package com.generatescape.mediaminder;

import java.util.Date;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import com.generatescape.baseobjects.ArticleObject;
import com.generatescape.baseobjects.CONSTANTS;
import com.generatescape.newtreemodel.NewCanalNode;
import com.generatescape.views.BigBlogZooView;
import com.generatescape.views.SearchView;

/*******************************************************************************
 * Copyright (c) 2005, 2007 GenerateScape and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the GNU General Public License which accompanies this distribution, and is
 * available at http://www.gnu.org/copyleft/gpl.html
 * 
 * @author kentgibson : http://www.bigblogzoo.com
 * 
 ******************************************************************************/
public class SearchCatcher {

    private String catcherName;

    static Logger log = Logger.getLogger(SearchCatcher.class.getName());

    private static Hashtable urlToArticleVector = new Hashtable();

    /**
   * @param name
   */
    public SearchCatcher(String name) {
        this.catcherName = name;
    }

    /**
   * @param newsnose
   * @param foundArticle
   * @param node
   */
    public void add(final boolean newsnose, final ArticleObject foundArticle, NewCanalNode node) {
        urlToArticleVector.put(foundArticle.getCaption(), foundArticle);
        final NewCanalNode cn = new NewCanalNode(foundArticle.getCaption(), CONSTANTS.TYPE_HIT, foundArticle.getUrl());
        Date date = foundArticle.getDateFound();
        if (date != null) {
            cn.setDate(date.getTime());
        } else {
            cn.setDate(System.currentTimeMillis());
        }
        cn.setXmlsource(foundArticle.getChannelURL());
        String lineage = (String) BigBlogZooView.urlSet.get(foundArticle.getChannelURL());
        cn.setLineage(lineage);
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                SearchView.addNode(newsnose, cn);
            }
        });
    }

    /**
   * @param feedtitle
   * @param url
   * @param title
   * @param date
   * @param description
   */
    public static void addForAutoDetect(String feedtitle, String url, String title, Date date, String description) {
        ArticleObject ao = new ArticleObject();
        ao.setChannelTitle(feedtitle);
        ao.setUrl(url);
        ao.setDescription(description);
        ao.setDateFound(date);
        ao.setTitle(title);
        urlToArticleVector.put(url, ao);
    }

    /**
   * @return
   */
    public static Hashtable getUrlToArticleVector() {
        return urlToArticleVector;
    }
}
