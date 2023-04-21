package com.ohua.data.model.json;

import com.ohua.engine.data.model.daapi.DataUtils;
import com.ohua.engine.data.model.daapi.DataFormat;
import com.ohua.engine.data.model.daapi.DataPacket;
import com.ohua.engine.data.model.daapi.OhuaDataAccessor;

public class JSONDataFormat implements DataFormat {

    public OhuaDataAccessor createDataAccessor() {
        return new JSONDataAccessor();
    }

    public DataPacket createDataPacket() {
        return new JSONDataPacket();
    }

    public DataUtils getComparisonUtils() {
        return new JSONDataUtils();
    }
}
