package org.awelements.table.web;

import org.awelements.table.IHtmlString;
import org.awelements.table.web.tag.InfoBoxTag;

public class HtmlInfoBoxTrigger implements IHtmlString {

    private String mInfoBoxName;

    private String mInfoBoxParameter;

    private IHtmlString mInnerHtml;

    public HtmlInfoBoxTrigger() {
    }

    public HtmlInfoBoxTrigger(String infoBoxName, String infoBoxParameter, IHtmlString innerHtml) {
        mInfoBoxName = infoBoxName;
        mInfoBoxParameter = infoBoxParameter;
        mInnerHtml = innerHtml;
    }

    public String getInfoBoxName() {
        return mInfoBoxName;
    }

    public void setInfoBoxName(String infoBoxName) {
        mInfoBoxName = infoBoxName;
    }

    public String getInfoBoxParameter() {
        return mInfoBoxParameter;
    }

    public void setInfoBoxParameter(String infoBoxParameter) {
        mInfoBoxParameter = infoBoxParameter;
    }

    public IHtmlString getInnerHtml() {
        return mInnerHtml;
    }

    public void setInnerHtml(IHtmlString innerHtml) {
        mInnerHtml = innerHtml;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(InfoBoxTag.getStartTag(mInfoBoxName, mInfoBoxParameter));
        if (mInnerHtml != null) sb.append(mInnerHtml);
        sb.append(InfoBoxTag.getEndTag());
        return sb.toString();
    }
}
