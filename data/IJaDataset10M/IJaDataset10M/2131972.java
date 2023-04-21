package com.aelitis.azureus.core.subs;

public interface SubscriptionScheduler {

    public void download(Subscription subs, boolean is_auto) throws SubscriptionException;

    public void downloadAsync(Subscription subs, boolean is_auto) throws SubscriptionException;

    public void download(Subscription subs, boolean is_auto, SubscriptionDownloadListener listener) throws SubscriptionException;

    public void download(Subscription subs, SubscriptionResult result);
}
