package net.sipvip.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class DownloadTimerEvent extends GwtEvent<DownloadTimerEventHandler> {

    public static Type<DownloadTimerEventHandler> TYPE = new Type<DownloadTimerEventHandler>();

    private int countvideos;

    public DownloadTimerEvent(int countvideos) {
        this.countvideos = countvideos;
    }

    @Override
    protected void dispatch(DownloadTimerEventHandler handler) {
        handler.onDownloadTimer(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DownloadTimerEventHandler> getAssociatedType() {
        return TYPE;
    }

    public int getCountvideos() {
        return countvideos;
    }
}
