package br.ufmg.lcc.arangi.commons;

import java.util.Date;

public class ProgressBarTracker {

    private float percent = 1;

    private Date remainingTime = new Date(10000);

    private long total = 1000000;

    private long currentQuantity = 1;

    public float getPercent() {
        if (percent == 0) {
            percent = 0.001f;
        }
        return percent;
    }

    public Date getRemainingTime() {
        return remainingTime;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public void setRemainingTime(Date remainingTime) {
        this.remainingTime = remainingTime;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(long currentQuatity) {
        this.currentQuantity = currentQuatity;
        percent = (int) ((currentQuantity * 100.0) / (total * 1.0));
    }
}
