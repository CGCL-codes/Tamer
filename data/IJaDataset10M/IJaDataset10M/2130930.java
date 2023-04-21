package com.jeantessier.dependencyfinder;

import com.jeantessier.classreader.*;

public class VerboseListenerBase extends LoadListenerBase {

    private String ratioIndicator = "";

    protected String getRatioIndicator() {
        return ratioIndicator;
    }

    private void setRatioIndicator(String ratioIndicator) {
        this.ratioIndicator = ratioIndicator;
    }

    private int computeCurrentRatio() {
        return getCurrentGroup().getCount() * 100 / getCurrentGroup().getSize();
    }

    public void beginFile(LoadEvent event) {
        int previousRatio = computeCurrentRatio();
        super.beginFile(event);
        if (getCurrentGroup().getSize() > 0) {
            int newRatio = computeCurrentRatio();
            if (previousRatio != newRatio) {
                StringBuffer buffer = new StringBuffer(4);
                if (newRatio < 10) {
                    buffer.append(" ");
                }
                if (newRatio < 100) {
                    buffer.append(" ");
                }
                buffer.append(newRatio).append("%");
                setRatioIndicator(buffer.toString());
            } else {
                setRatioIndicator("");
            }
        } else {
            setRatioIndicator("");
        }
    }
}
