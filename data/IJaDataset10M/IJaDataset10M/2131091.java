package com.untilov.gb.me.gui;

import java.io.IOException;
import javax.microedition.lcdui.Displayable;
import com.untilov.gb.FeedsBuilder;
import com.untilov.gb.URLs;
import com.untilov.gb.http.DataPumpIF;
import com.untilov.gb.http.DataPumpMEImpl;
import com.untilov.gb.http.auth.AuthenticatorIF;
import com.untilov.gb.http.auth.AuthenticatorMEImpl;
import com.untilov.gb.me.GoogleBookmarksMIDlet;

/**
 * This thread tries to login, get and prepare bookmarks.
 * 
 * @author iuntilov
 *
 */
public class LoginThread implements Runnable {

    private GoogleBookmarksMIDlet midlet;

    private AuthenticatorIF security;

    private String login;

    private String password;

    public LoginThread(GoogleBookmarksMIDlet midlet, String login, String password) {
        this.midlet = midlet;
        this.security = new AuthenticatorMEImpl(URLs.loginURL);
        this.login = login;
        this.password = password;
    }

    /**
     * Main work.
     */
    public void run() {
        try {
            if (security.authenticate(login, password)) {
                retrieveAndPrepareData();
                setDisplay(midlet.getCategoriesList().populateCategoriesList());
            } else {
                setDisplay(midlet.getLoginFailed(""));
            }
        } catch (Exception e) {
            e.printStackTrace();
            setDisplay(midlet.getLoginFailed(""));
        }
    }

    /**
     * Load bookmarks data from the web, set feedBuilder.
     * 
     * @throws IOException
     * @throws Exception
     */
    private void retrieveAndPrepareData() throws IOException, Exception {
        DataPumpIF data = new DataPumpMEImpl(security.getCookiesManager().getCookies(), URLs.bookmarksURL);
        FeedsBuilder feedsBuilder = new FeedsBuilder();
        feedsBuilder.createFeeds(data.getData());
        feedsBuilder.sortCategoriesByName();
        feedsBuilder.groupByCategories();
        midlet.setFeedsBuilder(feedsBuilder);
    }

    private void setDisplay(Displayable d) {
        midlet.getDisplay().setCurrent(d);
    }
}
