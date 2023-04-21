package org.equanda.tapestry5.base;

import org.apache.tapestry5.json.JSONObject;

/**
 * Configuration bean for {@link org.equanda.tapestry5.components.JSPager}.
 *
 * @author <a href="mailto:vladimir.tkachenko@gmail.com">Vladimir Tkachenko</a>
 */
public class PagerConfig {

    private int range;

    private int maxPages;

    private int rowsPerPage;

    private int lastIndex;

    private int currentPage;

    private int rowCount;

    private int addedRowCount;

    private int additionalRowCount;

    private String currentPageFieldName;

    private String addedRowFieldName;

    private String containerClass;

    private String rowClass;

    private String addLinkClass;

    private String addBlockClass;

    private String name;

    public PagerConfig() {
    }

    public PagerConfig(int range, int maxPages, int rowsPerPage, int lastIndex, int currentPage, int rowCount, int addedRowCount, int additionalRowCount, String currentPageFieldName, String addedRowFieldName, String containerClass, String rowClass, String addLinkClass, String addBlockClass, String name) {
        super();
        this.range = range;
        this.maxPages = maxPages;
        this.rowsPerPage = rowsPerPage;
        this.lastIndex = lastIndex;
        this.currentPage = currentPage;
        this.rowCount = rowCount;
        this.addedRowCount = addedRowCount;
        this.additionalRowCount = additionalRowCount;
        this.currentPageFieldName = currentPageFieldName;
        this.addedRowFieldName = addedRowFieldName;
        this.containerClass = containerClass;
        this.rowClass = rowClass;
        this.addLinkClass = addLinkClass;
        this.addBlockClass = addBlockClass;
        this.name = name;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getAddedRowCount() {
        return addedRowCount;
    }

    public void setAddedRowCount(int addedRowCount) {
        this.addedRowCount = addedRowCount;
    }

    public int getAdditionalRowCount() {
        return additionalRowCount;
    }

    public void setAdditionalRowCount(int additionalRowCount) {
        this.additionalRowCount = additionalRowCount;
    }

    public String getCurrentPageFieldName() {
        return currentPageFieldName;
    }

    public void setCurrentPageFieldName(String currentPageFieldName) {
        this.currentPageFieldName = currentPageFieldName;
    }

    public String getAddedRowFieldName() {
        return addedRowFieldName;
    }

    public void setAddedRowFieldName(String addedRowFieldName) {
        this.addedRowFieldName = addedRowFieldName;
    }

    public String getContainerClass() {
        return containerClass;
    }

    public void setContainerClass(String containerClass) {
        this.containerClass = containerClass;
    }

    public String getRowClass() {
        return rowClass;
    }

    public void setRowClass(String rowClass) {
        this.rowClass = rowClass;
    }

    public String getAddLinkClass() {
        return addLinkClass;
    }

    public void setAddLinkClass(String addLinkClass) {
        this.addLinkClass = addLinkClass;
    }

    public String getAddBlockClass() {
        return addBlockClass;
    }

    public void setAddBlockClass(String addBlockClass) {
        this.addBlockClass = addBlockClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("range", range);
        object.put("rowsPerPage", rowsPerPage);
        object.put("lastIndex", lastIndex);
        object.put("currentPage", currentPage);
        object.put("rowCount", rowCount);
        object.put("addedRowCount", addedRowCount);
        object.put("additionalRowCount", additionalRowCount);
        object.put("currentPageFieldName", currentPageFieldName);
        object.put("addedRowFieldName", addedRowFieldName);
        object.put("containerClass", containerClass);
        object.put("rowClass", rowClass);
        object.put("addLinkClass", addLinkClass);
        object.put("addBlockClass", addBlockClass);
        object.put("name", name);
        return object;
    }
}
