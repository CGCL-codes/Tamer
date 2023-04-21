package org.eclipsetrader.repository.local.internal.types;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.eclipsetrader.core.feed.IOHLC;

public class OHLCAdapter extends XmlAdapter<OHLCType, IOHLC> {

    public OHLCAdapter() {
    }

    @Override
    public OHLCType marshal(IOHLC v) throws Exception {
        return v != null ? new OHLCType(v) : null;
    }

    @Override
    public IOHLC unmarshal(OHLCType v) throws Exception {
        return v != null ? v.getOHLC() : null;
    }
}
