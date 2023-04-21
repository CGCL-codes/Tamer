package cn.vlabs.clb.api.search;

import cn.cnic.esac.clb.util.Tag;

public class SearchResultVO implements Tagable {

    private String comment = null;

    private int docId = 0;

    private String summary = null;

    private String keywords = null;

    private String title = null;

    private String verNo = null;

    private String filename = null;

    private String updateDate = null;

    private Tag[] tags = null;

    private String createBy = null;

    private String size = null;

    private boolean removed = false;

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return this.size;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Tag[] getTags() {
        return tags;
    }

    public void setTags(Tag[] tags) {
        this.tags = tags;
    }

    public void setFilename(String fname) {
        this.filename = fname;
    }

    public String getFilename() {
        return filename;
    }

    public void setUpdateTime(String time) {
        this.updateDate = time;
    }

    public String getUpdateTime() {
        return updateDate;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVerNo() {
        return verNo;
    }

    public void setVerNo(String verNo) {
        this.verNo = verNo;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public String getResourceId() {
        return Integer.toString(docId);
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }
}
