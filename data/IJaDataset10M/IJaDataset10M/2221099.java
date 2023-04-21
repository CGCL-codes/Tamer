package com.unicont.cardio.impl;

import com.unicont.cardio.Lead;

public class CardiogramImpl extends AbstractCardiogramImpl {

    public CardiogramImpl(long time) {
        super(time);
    }

    @Override
    public Lead getLead(int nLead) {
        return leads[nLead];
    }

    public boolean isCompleted() {
        return isReceived();
    }

    public boolean isReceived() {
        for (Lead l : leads) {
            if (l == CardiogramImpl.NULL_LEAD) return false;
        }
        return true;
    }
}
