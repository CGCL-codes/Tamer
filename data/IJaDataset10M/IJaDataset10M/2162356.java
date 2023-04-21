package fr.amille.animebrowser.control.controlview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import fr.amille.animebrowser.control.InitControl;
import fr.amille.animebrowser.view.central.sites.SiteListView;

public class SiteListViewControl implements InitControl, ActionListener {

    private SiteListView siteListView;

    public SiteListViewControl() {
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
    }

    public SiteListView getSiteList() {
        return this.siteListView;
    }

    @Override
    public void initControl() {
        this.siteListView = new SiteListView();
    }

    public void setSiteList(final SiteListView siteListView) {
        this.siteListView = siteListView;
    }
}
