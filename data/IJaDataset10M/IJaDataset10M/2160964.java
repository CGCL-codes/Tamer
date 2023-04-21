package com.mscg.asf.impl.header.sub;

import java.io.InputStream;
import com.mscg.asf.exception.InvalidObjectDataException;
import com.mscg.asf.guid.ASFObjectGUID;
import com.mscg.asf.impl.ASFGenericObject;

public class ASFFilePropertiesObject extends ASFGenericObject {

    public ASFFilePropertiesObject(ASFObjectGUID guid, long length, InputStream data) throws InvalidObjectDataException {
        super(guid, length, data);
    }
}
