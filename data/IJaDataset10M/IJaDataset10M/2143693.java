package com.miden2ena.mogeci.wfe.event;

import java.util.Date;

/**
 *
 * @author Work
 */
public class WFEvent_CVPrivacy extends WFEvent {

    private Date dataConfermaPrivacy;

    private boolean confermata;

    public boolean isConfermata() {
        return confermata;
    }

    public void setConfermata(boolean confermata) {
        this.confermata = confermata;
    }

    public void setDataConfermaPrivacy(Date dataConfermaPrivacy) {
        this.dataConfermaPrivacy = dataConfermaPrivacy;
    }

    public Date getDataConfermaPrivacy() {
        return dataConfermaPrivacy;
    }

    /** Creates a new instance of WFEvent_CVPrivacy */
    public WFEvent_CVPrivacy() {
    }
}
