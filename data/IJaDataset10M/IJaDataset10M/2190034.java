package com.codeforces.graygoose.page.web;

import com.codeforces.graygoose.frame.SiteEditOrAddFrame;
import com.google.inject.Inject;
import org.nocturne.link.Link;

@Link("addSite")
public class SiteAddPage extends WebPage {

    @Inject
    private SiteEditOrAddFrame siteEditOrAddFrame;

    @Override
    public String getTitle() {
        return $("Add site");
    }

    @Override
    public void action() {
        siteEditOrAddFrame.setup(SitesPage.class);
        parse("siteAddFrame", siteEditOrAddFrame);
    }
}
