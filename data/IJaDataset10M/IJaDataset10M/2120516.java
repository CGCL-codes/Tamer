package org.juicyapps.juicynews.gui;

import java.awt.Insets;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.juicyapps.app.JuicyUtil;
import org.juicyapps.gui.sidebar.SidebarPane;
import org.juicyapps.persistence.pojo.JuicyAccount;
import org.juicyapps.persistence.pojo.Rssfeed;

public class JuicyNewsSidebarPane extends SidebarPane {

    private static final long serialVersionUID = 9192955662553047047L;

    private JuicyAccount activeJuicyAccount = null;

    private Set<Rssfeed> feeds = null;

    private static final int MAX_RSS_VISIBLE = 5;

    public JuicyNewsSidebarPane(String app, JuicyAccount juicyaccount) {
        activeJuicyAccount = juicyaccount;
        name = "JuicyNewsPane";
        appName = app;
        feeds = activeJuicyAccount.getRssFeeds();
        initLayout("News", true, new ImageIcon("img" + File.separator + "juicynews" + File.separator + "icon.png"), true);
        fillRssList();
    }

    private void fillRssList() {
        JLabel lblNews = null;
        lblNews = new JLabel(feeds.size() + " Newsfeeds found");
        JuicyUtil.addComponent(contentPane, lblNews, 0, 0, 1, 1, 0, 0, new Insets(0, 0, 0, 0));
        Iterator<Rssfeed> feedIterator = feeds.iterator();
        int i = 1;
        while (feedIterator.hasNext() && i++ < MAX_RSS_VISIBLE + 1) {
            Rssfeed rssFeed = feedIterator.next();
            lblNews = new JLabel();
            lblNews.setText(rssFeed.getRfName());
            JuicyUtil.addComponent(contentPane, lblNews, 0, i, 1, 1, 0, 0, new Insets(0, 0, 0, 0));
        }
    }
}
