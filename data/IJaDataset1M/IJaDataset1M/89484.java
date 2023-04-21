package org.spbu.plweb.diagram.util.projects.partsPLWeb;

import java.util.Collections;
import java.util.List;

public class Group extends GroupsAware implements TypeAware {

    public static final String PLWEB_TYPE = "plweb:Group";

    private final boolean groupXOR;

    private List<Page> pages;

    private List<SiteView> siteViews;

    private List<Area> areas;

    public Group(final boolean optional, final String title, final List<Page> pages, final List<SiteView> siteViews, final List<Area> areas, final List<Node> nodes, final List<Group> groups, final List<DocTopic> topics, final boolean groupXOR) {
        super(optional, title, topics, nodes, groups);
        this.pages = pages;
        this.siteViews = siteViews;
        this.areas = areas;
        this.groupXOR = groupXOR;
    }

    public List<Page> getPages() {
        return Collections.unmodifiableList(pages);
    }

    public List<SiteView> getSiteViews() {
        return Collections.unmodifiableList(siteViews);
    }

    public List<Area> getAreas() {
        return Collections.unmodifiableList(areas);
    }

    @Override
    public String toString() {
        String sAreas = "";
        String sPages = "";
        String sNodes = "";
        String sGroups = "";
        String sTopics = "";
        if (!getAreas().isEmpty()) {
            sAreas = ", areas: " + getAreas();
        }
        if (!getGroups().isEmpty()) {
            sGroups = ", groups: " + getGroups();
        }
        if (!getNodes().isEmpty()) {
            sNodes = ", nodes: " + getNodes();
        }
        if (!getPages().isEmpty()) {
            sPages = ", pages: " + pages;
        }
        if (!getTopics().isEmpty()) {
            sTopics = ", topics: " + getTopics();
        }
        return "Group {" + getTitle() + "}, siteViews: " + siteViews + sAreas + sPages + sNodes + sGroups + sTopics;
    }

    @Override
    public String getType() {
        return "Group";
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public void setSiteViews(List<SiteView> siteViews) {
        this.siteViews = siteViews;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public void addPage(Page page) {
        pages.add(page);
    }

    public void addArea(Area area) {
        areas.add(area);
    }

    public void addSiteView(SiteView siteView) {
        siteViews.add(siteView);
    }
}
