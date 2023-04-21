package com.simconomy.twitter.client;

import java.util.List;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gadgets.client.Gadget;
import com.google.gwt.gadgets.client.NeedsSetPrefs;
import com.google.gwt.gadgets.client.SetPrefsFeature;
import com.google.gwt.gadgets.client.Gadget.ModulePrefs;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.simconomy.twitter.client.model.AccessToken;
import com.simconomy.twitter.client.model.Tweet;
import com.simconomy.twitter.client.oauth.OAuthPanel;
import com.simconomy.twitter.client.service.GreetingService;
import com.simconomy.twitter.client.service.GreetingServiceAsync;
import com.simconomy.twitter.client.service.ServiceUtils;
import com.simconomy.twitter.client.startup.StartupPanel;
import com.simconomy.twitter.client.tweetz.TweetLine;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
@ModulePrefs(title = "twitter", author = "Mark Bakker", author_email = "bakker.mark@gmail.com")
public class Twitter extends Gadget<GadgetRPCPreferences> implements ChangeListener, NeedsSetPrefs {

    /**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
    private GreetingServiceAsync greetingService = GreetingService.Util.getInstance();

    private GadgetRPCPreferences prefs = null;

    private SetPrefsFeature feature = null;

    private OAuthPanel authPanel = new OAuthPanel();

    private StartupPanel startupPanel = new StartupPanel();

    private Button reset = new Button("reset");

    private VerticalPanel tweets = new VerticalPanel();

    /**
	 * This is the entry point method.
	 */
    @Override
    protected void init(GadgetRPCPreferences preferences) {
        prefs = preferences;
        authPanel.addChangeListerner(this);
        authPanel.setVisible(false);
        startupPanel.setVisible(false);
        reset.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                feature.set(prefs.accessToken(), "");
                feature.set(prefs.accessTokenSecret(), "");
                showAuthPanel();
            }
        });
        RootPanel.get().add(authPanel);
        RootPanel.get().add(startupPanel);
        RootPanel.get().add(new Anchor("nu.nl", "http://www.nu.nl", "_blank"));
        RootPanel.get().add(reset);
        RootPanel.get().add(tweets);
        tweets.add(new Label("tweets"));
        if (prefs.accessToken().getValue().length() > 0 & prefs.accessTokenSecret().getValue().length() > 0) {
            RequestBuilder requestBuilder = greetingService.checkCredentials(prefs.accessToken().getValue(), prefs.accessTokenSecret().getValue(), new AsyncCallback<Void>() {

                public void onSuccess(Void result) {
                    showStartPanel();
                    addTweets();
                }

                public void onFailure(Throwable caught) {
                    showAuthPanel();
                }
            });
            ServiceUtils.makePostRequest(requestBuilder);
        } else {
            showAuthPanel();
        }
    }

    private void addTweets() {
        RequestBuilder requestBuilder = greetingService.getFriendsTimeline(prefs.accessToken().getValue(), prefs.accessTokenSecret().getValue(), new AsyncCallback<List<Tweet>>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(List<Tweet> result) {
                for (Tweet tweet : result) {
                    TweetLine tweetLine = new TweetLine(tweet);
                    tweets.add(tweetLine);
                }
            }
        });
        ServiceUtils.makePostRequest(requestBuilder);
    }

    private void showAuthPanel() {
        authPanel.setVisible(true);
        startupPanel.setVisible(false);
    }

    private void showStartPanel() {
        authPanel.setVisible(false);
        startupPanel.setVisible(true);
    }

    public void onChange(Widget sender) {
        OAuthPanel panel = (OAuthPanel) sender;
        AccessToken accessToken = panel.getValue();
        feature.set(prefs.accessToken(), accessToken.getAccessToken());
        feature.set(prefs.accessTokenSecret(), accessToken.getAccessTokenSecret());
        showStartPanel();
        addTweets();
    }

    public void initializeFeature(SetPrefsFeature feature) {
        this.feature = feature;
    }
}
