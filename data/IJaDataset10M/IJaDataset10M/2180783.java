package com.google.code.javascribd.connection.jaxbadapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import com.google.code.javascribd.type.ShowAds;

public class ShowAdsAdapter extends XmlAdapter<String, ShowAds> {

    @Override
    public String marshal(ShowAds showAds) throws Exception {
        if (showAds == null) {
            return null;
        }
        return showAds.toString();
    }

    @Override
    public ShowAds unmarshal(String showAdsString) throws Exception {
        if (showAdsString == null) {
            return null;
        }
        return ShowAds.fromValue(new CDATAStringAdapter().unmarshal(showAdsString));
    }
}
