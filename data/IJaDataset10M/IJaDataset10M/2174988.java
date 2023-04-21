package net.sipvip.server.utils.impl;

import org.json.JSONException;
import org.json.JSONObject;
import net.sipvip.server.utils.inte.ParsJsonReplyServ;

public class ParsJsonReplyServImpl implements ParsJsonReplyServ {

    private String title;

    private String charset;

    private String locale;

    private String links;

    private String theme;

    private String facebookid;

    private String google;

    private String slot_r;

    private String slot_up;

    private String slot_s;

    private String slot_l;

    private String slot_bi;

    private JSONObject jSONObject;

    @Override
    public void getParsJsonReplyServ(JSONObject jSONObject) throws JSONException {
        this.jSONObject = jSONObject;
        JSONObject domaininfo = (JSONObject) jSONObject.get("domaininfo");
        this.title = domaininfo.get("title").toString();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getCharset() {
        return null;
    }

    @Override
    public String getLocale() {
        return null;
    }

    @Override
    public String getLinks() {
        return null;
    }

    @Override
    public String getTheme() {
        return null;
    }

    @Override
    public String getFacebookid() {
        return null;
    }

    @Override
    public String getGoogle() {
        return null;
    }

    @Override
    public String getSlot_r() {
        return null;
    }

    @Override
    public String getSlot_up() {
        return null;
    }

    @Override
    public String getSlot_s() {
        return null;
    }

    @Override
    public String getSlot_l() {
        return null;
    }

    @Override
    public String getSlot_bi() {
        return null;
    }
}
