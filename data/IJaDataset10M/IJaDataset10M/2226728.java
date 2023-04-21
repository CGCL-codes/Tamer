package com.google.code.sagetvaddons.sagetweet.server;

import gkusnick.sagetv.api.API;
import com.google.code.sagetvaddons.sagetweet.client.ServerSettings;
import com.google.code.sagetvaddons.sagetweet.client.TwitterSettings;

class MonitorDiskSpace extends TweetMonitor {

    long lastWarning;

    @Override
    public void run() {
        lastWarning = 0;
        while (keepAlive()) {
            System.out.println("SageTweet: Disk monitor started");
            ServerSettings props = AppSettings.getInstance().getServerSettings();
            if (props.isTweeting() && props.getTweetLowSpace() != null && props.getTweetLowSpace().length() > 0) {
                long freeSpace = API.apiNullUI.global.GetTotalDiskspaceAvailable();
                long minSpace = 1024L * 1024L * 1024L * props.getLowSpaceValue();
                TwitterSettings twitter = AppSettings.getInstance().getTwitterSettings(props.getTweetLowSpace());
                if (freeSpace >= minSpace) lastWarning = 0; else if (System.currentTimeMillis() - lastWarning > 14400000 && TweetQueue.getInstance().add(new QueuedTweet(twitter.getId(), twitter.getPwd(), "Less than " + props.getLowSpaceValue() + "GB of free space available on the server.", System.currentTimeMillis()))) lastWarning = System.currentTimeMillis();
            }
            System.out.println("SageTweet: Disk monitor sleeping");
            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("SageTweet: Disk monitor done");
    }
}
