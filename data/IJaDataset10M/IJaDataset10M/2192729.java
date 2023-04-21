package net.sf.imca.androidsync.platform;

import android.provider.ContactsContract.Data;

public interface SampleSyncAdapterColumns {

    /**
     * MIME-type used when storing a profile {@link Data} entry.
     */
    public static final String MIME_PROFILE = "vnd.android.cursor.item/net.sf.imca.androidsync.profile";

    public static final String DATA_PID = Data.DATA1;

    public static final String DATA_SUMMARY = Data.DATA2;

    public static final String DATA_DETAIL = Data.DATA3;
}
