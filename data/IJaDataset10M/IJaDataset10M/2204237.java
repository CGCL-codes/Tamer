package android.content.cts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class is used for testing android.content.ContentWrapper.
 *
 * @see ContextWrapperTest
 */
public class ResultReceiver extends BroadcastReceiver {

    public static final String MOCK_ACTION = "android.content.cts.ContextWrapperTest.BROADCAST_RESULT";

    private boolean mReceivedBroadCast;

    public void onReceive(Context context, Intent intent) {
        mReceivedBroadCast = MOCK_ACTION.equals(intent.getAction());
    }

    public boolean hasReceivedBroadCast() {
        return mReceivedBroadCast;
    }

    public void reset() {
        mReceivedBroadCast = false;
    }
}
