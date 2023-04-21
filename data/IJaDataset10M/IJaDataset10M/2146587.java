package android.content.cts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * This class is used for testing android.content.BroadcastReceiver.
 *
 * @see BroadcastReceiver
 */
public class MockReceiver extends BroadcastReceiver {

    public static final int RESULT_CODE = 4;

    public static final String RESULT_DATA = "add";

    public static final String RESULT_EXTRAS_INVARIABLE_KEY = "invariable";

    public static final String RESULT_EXTRAS_INVARIABLE_VALUE = "invariable value";

    public static final String RESULT_EXTRAS_REMOVE_KEY = "remove";

    public static final String RESULT_EXTRAS_REMOVE_VALUE = "remove value";

    public static final String RESULT_EXTRAS_ADD_KEY = "add";

    public static final String RESULT_EXTRAS_ADD_VALUE = "add value";

    public void onReceive(Context context, Intent intent) {
        Bundle map = getResultExtras(false);
        map.remove(RESULT_EXTRAS_REMOVE_KEY);
        map.putString(RESULT_EXTRAS_ADD_KEY, RESULT_EXTRAS_ADD_VALUE);
        setResult(RESULT_CODE, RESULT_DATA, map);
    }
}
