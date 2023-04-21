package com.jlz.beans.core;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.julewa.db.DB;
import com.julewa.db.Entity;

@DB.DYNAMIC
@Component
@Scope(Entity.SCOPE)
public class PriceBean extends Dynamic<Long> {

    @DB.COLUMN
    int idx;

    @DB.COLUMN
    int min;

    @DB.COLUMN
    int max;

    @DB.COLUMN
    int type;

    @DB.COLUMN
    int duration;

    @DB.COLUMN
    int timeunit;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTimeunit() {
        return timeunit;
    }

    public void setTimeunit(int timeunit) {
        this.timeunit = timeunit;
    }
}
