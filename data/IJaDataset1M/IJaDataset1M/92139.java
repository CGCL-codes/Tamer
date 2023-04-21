package foa.layout.pages;

import java.util.*;
import org.w3c.dom.*;
import foa.layout.*;
import foa.Manager;
import foa.elements.Element;
import foa.elements.fo.SimplePageMaster;
import foa.elements.fo.RegionAfter;
import foa.elements.fo.RegionBefore;
import foa.elements.fo.RegionStart;
import foa.elements.fo.RegionEnd;
import foa.elements.fo.RegionBody;

public class PageManager extends Manager {

    protected Hashtable pages;

    private Hashtable regions;

    private PageFrame pageFrame;

    private LayoutDirector layoutDirector;

    private SimplePageMaster currentPage;

    public PageManager(LayoutDirector layoutDirector, double wFactor, double hFactor) {
        this.layoutDirector = layoutDirector;
        pages = new Hashtable();
        pageFrame = new PageFrame("Page Manager", this, layoutDirector, wFactor, hFactor);
    }

    public PageFrame getPageFrame() {
        return pageFrame;
    }

    public void setVisible(boolean visible) {
        pageFrame.setVisible(visible);
    }

    public void createPage(Hashtable attributes) {
        currentPage = new SimplePageMaster(attributes);
    }

    public void deletePage(String pageName) {
        this.pages.remove(pageName);
    }

    public void deleteRegion(String pageName, String region) {
        ((SimplePageMaster) this.pages.get(pageName)).removeRegion(region);
    }

    public void addRegion(String tag, Hashtable attributes) {
        if (tag.endsWith("before") || tag.equals("Header")) currentPage.addRegion("Header", new RegionBefore((Hashtable) attributes.clone())); else if (tag.endsWith("after") || tag.equals("Footer")) currentPage.addRegion("Footer", new RegionAfter((Hashtable) attributes.clone())); else if (tag.endsWith("body") || tag.equals("Body")) currentPage.addRegion("Body", new RegionBody((Hashtable) attributes.clone())); else if (tag.endsWith("start") || tag.equals("Left")) currentPage.addRegion("Left", new RegionStart((Hashtable) attributes.clone())); else if (tag.endsWith("end") || tag.equals("Right")) currentPage.addRegion("Right", new RegionEnd((Hashtable) attributes.clone()));
    }

    public void updateRegion(String pageName, String tag, Hashtable attributes) {
        currentPage = (SimplePageMaster) pages.get(pageName);
        if (tag.endsWith("Header")) currentPage.updateRegion("Header", new RegionBefore((Hashtable) attributes.clone())); else if (tag.endsWith("Footer")) currentPage.updateRegion("Footer", new RegionAfter((Hashtable) attributes.clone())); else if (tag.endsWith("Body")) currentPage.updateRegion("Body", new RegionBody((Hashtable) attributes.clone())); else if (tag.endsWith("Left")) currentPage.updateRegion("Left", new RegionStart((Hashtable) attributes.clone())); else if (tag.endsWith("Right")) currentPage.updateRegion("Right", new RegionEnd((Hashtable) attributes.clone()));
    }

    public void addPage() {
        pages.put((String) (currentPage.getAtts()).get("master-name"), currentPage);
    }

    public void updatePage(String pageName, Hashtable attributes) {
        ((SimplePageMaster) pages.get(pageName)).setAtts((Hashtable) attributes.clone());
    }

    public Hashtable getPages() {
        return pages;
    }

    public void setCurrentPage(SimplePageMaster page) {
        currentPage = page;
    }

    public void initializePages() {
        pages = new Hashtable();
        pageFrame.initializePages();
    }

    public void startPages() {
        pages = new Hashtable();
        pageFrame.refreshPages();
    }

    public void refreshPages() {
        pageFrame.refreshPages();
    }
}
