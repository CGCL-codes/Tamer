package jp.eisbahn.eclipse.plugins.twitterclipse;

import jp.eisbahn.eclipse.plugins.twitterclipse.internal.TwitterclipsePlugin;

public class TwitterclipseCore {

    public static void addTwitterclipseListener(TwitterclipseListener listener) {
        TwitterclipsePlugin plugin = TwitterclipsePlugin.getDefault();
        plugin.addTwitterclipseListener(listener);
    }

    public static void removeTwitterclipseListener(TwitterclipseListener listener) {
        TwitterclipsePlugin plugin = TwitterclipsePlugin.getDefault();
        plugin.removeTwitterclipseListener(listener);
    }

    public static void updateStatus(String status) throws HttpRequestTimeoutException, TwitterCommunicationException {
        TwitterclipsePlugin plugin = TwitterclipsePlugin.getDefault();
        plugin.updateStatus(status);
    }

    public static void refresh() {
        TwitterclipsePlugin plugin = TwitterclipsePlugin.getDefault();
        plugin.refresh();
    }
}
