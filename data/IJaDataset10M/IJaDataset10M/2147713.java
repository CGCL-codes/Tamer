package org.dwgsoftware.raistlin.composition.model.impl;

import org.dwgsoftware.raistlin.composition.model.Commissionable;

public class UninterruptableCommissionable implements Commissionable {

    private boolean m_looping = true;

    public void commission() throws Exception {
        m_looping = true;
        while (m_looping) ;
    }

    public void decommission() {
    }

    ;

    void stop() {
        m_looping = false;
    }
}
