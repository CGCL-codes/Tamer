package com.sts.webmeet.content.client.appshare;

public class BandwidthController {

    public static final float DEFAULT_KILO_BITS_PER_SECOND = 80.0f;

    private float fBitrate = DEFAULT_KILO_BITS_PER_SECOND;

    private int iLastSendAmount = 0;

    public BandwidthController(float fBitrate) {
        setBitratekbps(fBitrate);
    }

    public void setBitratekbps(float fBitrate) {
        this.fBitrate = fBitrate;
    }

    public int getWaitPeriod(int iBytes) {
        int iWait = 0;
        if (0 != iLastSendAmount) {
            iWait = (int) ((this.iLastSendAmount * 8) / fBitrate);
        }
        this.iLastSendAmount = iBytes;
        iWait = Math.max(0, iWait);
        return iWait;
    }

    public void reset() {
        this.iLastSendAmount = 0;
    }
}
