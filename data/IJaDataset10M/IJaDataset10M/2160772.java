package com.oktiva.mogno.additional;

import com.oktiva.mogno.Component;
import com.oktiva.mogno.html.A;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 */
public class PageNavigator extends com.oktiva.mogno.html.Span {

    public boolean compact = true;

    public boolean displayPrevNext = true;

    public int maxDisplayedPages = 10;

    public long numRows = 0;

    public int rowsPerPage = 25;

    public long currentPage = 1;

    public String evOnClick = "";

    public String applicationUrl = "";

    public String prevLinkLabel = "<<";

    public String nextLinkLabel = ">>";

    public String origin = "";

    public String formSubmit = null;

    /** Creates a new instance of PageNavigator */
    public PageNavigator() {
    }

    public String show() throws Exception {
        StringBuffer links = new StringBuffer(4096);
        long numPages = getNumRows() / getRowsPerPage();
        long displayedLinks = 0;
        if ((getNumRows() % getRowsPerPage()) > 0) {
            numPages++;
        }
        if (isDisplayPrevNext() && getCurrentPage() > 1) {
            links.append(buildPageLink(getCurrentPage() - 1, getPrevLinkLabel()));
            links.append(" ");
        }
        long i = 1;
        long end = numPages;
        if (isCompact()) {
            long pagesBefore = getMaxDisplayedPages() / 2;
            if ((getCurrentPage() - pagesBefore) <= 1) {
                i = 1;
            } else {
                i = getCurrentPage() - pagesBefore;
                links.append("<b>...</b> ");
            }
            end = i + getMaxDisplayedPages();
            if (end > numPages) {
                end = numPages;
                end = numPages + 1;
            }
        }
        if (formSubmit != null && !"".equals(formSubmit)) {
            links.append("<SCRIPT>\nfunction pageNavigatorGoTo(page) {\ndocument." + formSubmit + "." + getName() + ".value=page;\n");
            links.append("document." + formSubmit + ".submit();\n}\n</SCRIPT>\n");
            links.append("<INPUT type='hidden' name='" + getName() + "' value=''>");
        }
        for (; i < end; i++) {
            if (i == getCurrentPage()) {
                links.append("<b>");
                links.append(String.valueOf(i));
                links.append("</b>");
            } else {
                links.append(buildPageLink(i, null));
            }
            links.append(" ");
        }
        if (end < numPages) {
            links.append("<b>...</b> ");
        }
        if (isDisplayPrevNext() && (getCurrentPage() < numPages)) {
            links.append(buildPageLink(getCurrentPage() + 1, getNextLinkLabel()));
        }
        setContent(links.toString());
        return super.show();
    }

    private String buildPageLink(long pageNum, String label) throws Exception {
        String ret = "";
        A alink = new A();
        alink.setName(getName() + "_link_" + pageNum + "_" + label);
        if (formSubmit != null && !"".equals(formSubmit)) {
            alink.setHref("javascript:pageNavigatorGoTo(" + pageNum + ")");
        } else {
            String href = getApplicationUrl() + "?mognoOrigin=" + getOrigin() + "&" + getName() + "=" + pageNum;
            alink.setHref(href);
        }
        if (label == null || label.equals("")) {
            alink.setContent(String.valueOf(pageNum));
        } else {
            alink.setContent(label);
        }
        alink.setParent(name);
        ret += alink.show();
        return ret;
    }

    public void receiveRequest(HttpServletRequest request) {
        String page = null;
        if ((page = request.getParameter(getName())) != null) {
            if (!"".equals(page)) {
                Long curPage = new Long(page);
                setCurrentPage(curPage.longValue());
            }
            queue("evOnClick");
        }
    }

    /** If this component should display no more than maxDisplayedPages.
	 * or all links.
	 * @return Value of property compact.
	 */
    public boolean isCompact() {
        return compact;
    }

    /** Set if this component should display no more than maxDisplayedPages.
	 * or all links.
	 * @param compact New value of property compact.
	 */
    public void setCompact(boolean compact) {
        this.compact = compact;
    }

    /** If this component should display a next and previus page link.
	 * @return Value of property displayPrevNext.
	 */
    public boolean isDisplayPrevNext() {
        return displayPrevNext;
    }

    /** Sets if this component should display a next and previus page link.
	 * @param displayPrevNext New value of property displayPrevNext.
	 *
	 */
    public void setDisplayPrevNext(boolean displayPrevNext) {
        this.displayPrevNext = displayPrevNext;
    }

    /** The maximum number of displayed page links if isCompact()
	 * @return Value of property maxDisplayedPages.
	 */
    public int getMaxDisplayedPages() {
        return maxDisplayedPages;
    }

    /** Setter for property maxDisplayedPages.
	 * @param maxDisplayedPages New value of property maxDisplayedPages.
	 */
    public void setMaxDisplayedPages(int maxDisplayedPages) {
        this.maxDisplayedPages = maxDisplayedPages;
    }

    /** Number of rows I should try do paginate.
	 * @return Value of property numRows.
	 */
    public long getNumRows() {
        if (isDesigning()) {
            return (getRowsPerPage() * getMaxDisplayedPages()) + 5;
        }
        return numRows;
    }

    /** Setter for property numRows.
	 * @param numRows New value of property numRows.
	 */
    public void setNumRows(long numRows) {
        this.numRows = numRows;
    }

    /** Getter for property rowsPerPage.
	 * @return Value of property rowsPerPage.
	 *
	 */
    public int getRowsPerPage() {
        return rowsPerPage;
    }

    /** Setter for property rowsPerPage.
	 * @param rowsPerPage New value of property rowsPerPage.
	 */
    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    /** Getter for property currentPage.
	 * @return Value of property currentPage.
	 */
    public long getCurrentPage() {
        return currentPage;
    }

    /** Setter for property currentPage.
	 * @param currentPage New value of property currentPage.
	 */
    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getCurrentPageOffset() {
        long page = getCurrentPage();
        if (page > 0) {
            return ((page - 1) * getRowsPerPage());
        }
        return 0;
    }

    /** Getter for property evOnClick.
	 * @return Value of property evOnClick.
	 *
	 */
    public java.lang.String getEvOnClick() {
        return evOnClick;
    }

    /** Setter for property evOnClick.
	 * @param evOnClick New value of property evOnClick.
	 *
	 */
    public void setEvOnClick(java.lang.String evOnClick) {
        this.evOnClick = evOnClick;
    }

    /** Getter for property applicationUrl.
	 * @return Value of property applicationUrl.
	 *
	 */
    public java.lang.String getApplicationUrl() {
        if ("".equals(applicationUrl) && !isDesigning()) {
            HttpServletRequest request = getApplication().getServletRequest();
            return request.getRequestURI();
        }
        return applicationUrl;
    }

    /** Setter for property applicationUrl.
	 * @param applicationUrl New value of property applicationUrl.
	 *
	 */
    public void setApplicationUrl(java.lang.String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    /** Getter for property prevLinkLabel.
	 * @return Value of property prevLinkLabel.
	 *
	 */
    public java.lang.String getPrevLinkLabel() {
        return prevLinkLabel;
    }

    /** Setter for property prevLinkLabel.
	 * @param prevLinkLabel New value of property prevLinkLabel.
	 *
	 */
    public void setPrevLinkLabel(java.lang.String prevLinkLabel) {
        this.prevLinkLabel = prevLinkLabel;
    }

    /** Getter for property nextLinkLabel.
	 * @return Value of property nextLinkLabel.
	 *
	 */
    public java.lang.String getNextLinkLabel() {
        return nextLinkLabel;
    }

    /** Setter for property nextLinkLabel.
	 * @param nextLinkLabel New value of property nextLinkLabel.
	 *
	 */
    public void setNextLinkLabel(java.lang.String nextLinkLabel) {
        this.nextLinkLabel = nextLinkLabel;
    }

    /** Getter for property origin.
	 * @return Value of property origin.
	 *
	 */
    public java.lang.String getOrigin() {
        if (this.origin == null || "".equals(this.origin)) {
            Component owner = getOwner();
            if (owner == null) {
                owner = this;
            }
            return owner.getName();
        }
        return this.origin;
    }

    /** Setter for property origin.
	 * @param origin New value of property origin.
	 */
    public void setOrigin(java.lang.String origin) {
        this.origin = origin;
    }

    public Vector nonAttributeGetters() {
        Vector v = super.nonAttributeGetters();
        v.add("getCurrentPageOffset");
        v.add("getCurrentPage");
        v.add("getNumRows");
        return v;
    }

    /** Getter for property formSubmit.
	 * @return Value of property formSubmit.
	 *
	 */
    public String getFormSubmit() {
        return formSubmit;
    }

    /** If submitForm is set, the links will submit the form with the specified name when clicked.
	 * @param formSubmit New value of property formSubmit.
	 *
	 */
    public void setFormSubmit(String formSubmit) {
        this.formSubmit = formSubmit;
    }
}
