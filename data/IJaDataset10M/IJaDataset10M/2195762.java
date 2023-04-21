package com.google.code.sagetvaddons.sre3.shared;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author dbattams
 *
 */
public interface RecSchedServiceAsync {

    public void getRecSchedule(AsyncCallback<ArrayList<Recording>> cb);

    public void saveOverride(Recording r, AsyncCallback<Recording> cb);

    public void getRecording(int airingId, AsyncCallback<Recording> cb);

    public void rmOverride(int airingId, AsyncCallback<Boolean> cb);

    public void saveGlobalOverrides(HashMap<String, String> overrides, AsyncCallback<Boolean> cb);

    public void isRecSchedChanged(long ts, AsyncCallback<Long> cb);
}
