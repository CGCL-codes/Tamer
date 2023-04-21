package com.limegroup.gnutella;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.limegroup.gnutella.settings.ApplicationSettings;
import com.limegroup.gnutella.util.LimeWireUtils;

@Singleton
public class ApplicationServicesImpl implements ApplicationServices {

    private final byte[] bittorrentGUID;

    private final byte[] limewireGUID;

    @Inject
    ApplicationServicesImpl() {
        byte[] myguid = null;
        try {
            myguid = GUID.fromHexString(ApplicationSettings.CLIENT_ID.getValue());
        } catch (IllegalArgumentException iae) {
            myguid = GUID.makeGuid();
            ApplicationSettings.CLIENT_ID.setValue((new GUID(myguid)).toHexString());
        }
        limewireGUID = myguid;
        byte[] mybtguid = new byte[20];
        mybtguid[0] = 0x2D;
        mybtguid[1] = 0x4C;
        mybtguid[2] = 0x57;
        System.arraycopy(LimeWireUtils.BT_REVISION.getBytes(), 0, mybtguid, 3, 4);
        mybtguid[7] = 0x2D;
        System.arraycopy(limewireGUID, 0, mybtguid, 8, 12);
        bittorrentGUID = mybtguid;
    }

    public byte[] getMyBTGUID() {
        return bittorrentGUID;
    }

    public byte[] getMyGUID() {
        return limewireGUID;
    }

    public void setFullPower(boolean newValue) {
    }
}
