package org.stummi.swpb.querys;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class getDefaultPage extends DefaultPagePropQuery {

    @Override
    public int getLimit() {
        return 0;
    }

    @Override
    public ArrayList<NameValuePair> getUserParams() {
        ArrayList<NameValuePair> userparams = new ArrayList<NameValuePair>();
        userparams.add(new BasicNameValuePair("prop", "ids|flags|timestamp|user|size|comment|content"));
        return userparams;
    }

    @Override
    public String getPrefix() {
        return "rv";
    }

    @Override
    public String getType() {
        return "revisions";
    }
}
