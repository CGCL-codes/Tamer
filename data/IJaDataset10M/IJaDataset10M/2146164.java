package uk.ac.soton.horserace.webscraping.betgenius;

import java.io.IOException;
import uk.ac.soton.horserace.webscraping.AdaptiveRetryPolicy;

public class RaceScrapingPolicy extends AdaptiveRetryPolicy {

    public RaceScrapingPolicy(long period, long delayOnRetry) {
        super(period, delayOnRetry);
    }

    @Override
    protected void onUnblock() {
        delayOnBlock = delayOnBlock * 3 / 4;
    }

    @Override
    public boolean needRetry(Exception e) {
        if (e instanceof IOException && e.getLocalizedMessage() != null && e.getLocalizedMessage().indexOf("Server returned HTTP response code: 500") >= 0) return true; else return false;
    }
}
