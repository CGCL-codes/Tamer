package com.esri.gpt.control.webharvest.client.csw;

import com.esri.gpt.framework.isodate.IsoDateFormat;
import com.esri.gpt.framework.resource.api.SourceUri;
import com.esri.gpt.framework.resource.common.CommonPublishable;
import com.esri.gpt.framework.resource.common.StringUri;
import com.esri.gpt.framework.util.Val;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * CSW publishable adapter.
 */
class CswPublishableAdapter extends CommonPublishable {

    private static final IsoDateFormat ISODF = new IsoDateFormat();

    private CswProxy proxy;

    private SourceUri sourceUri;

    private String updateDate;

    public CswPublishableAdapter(CswProxy proxy, com.esri.gpt.server.csw.client.CswRecord record) {
        this.proxy = proxy;
        this.sourceUri = new StringUri(record.getId());
        this.updateDate = Val.chkStr(record.getModifiedDate());
    }

    public SourceUri getSourceUri() {
        return sourceUri;
    }

    public String getContent() throws IOException {
        return proxy.read(sourceUri.asString());
    }

    @Override
    public Date getUpdateDate() {
        if (updateDate.length() == 0) return null;
        try {
            return ISODF.parseObject(updateDate);
        } catch (ParseException ex) {
            return null;
        }
    }
}
