package com.zspower.zp.model;

/** 2006-10-17
 * 简玮侠
 * V1
 * @pdOid cecdbaff-707e-4d4f-9983-edb332c37ba8 */
public class NewsItem implements java.io.Serializable {

    /** @pdOid 243e548b-dff0-4b5f-a580-a19093417bd9 */
    private java.lang.String id;

    /** @pdOid 255e6aaa-15d0-437a-b677-82d430b7e753 */
    private java.lang.String title;

    /** @pdOid 5c5e79b1-ae57-4fbd-bf03-8dafe6f6e71e */
    private java.lang.String content;

    /** @pdOid da83be30-900e-492e-8238-143aadfca4d7 */
    private java.util.Date pubishDate;

    /**
    * Empty constructor which is required by Hibernate
    *
    */
    public NewsItem() {
    }

    /** @pdOid e1662ddb-bfa9-4565-a46a-248d8526beac */
    public String getId() {
        return id;
    }

    /** @param newId
    * @pdOid 39fe41bd-2453-42ca-97f4-2b3c3a5a607b */
    public void setId(String newId) {
        id = newId;
    }

    /** @pdOid fb35ef1e-214f-46d9-9af5-33dde9c530a7 */
    public java.lang.String getTitle() {
        return title;
    }

    /** @param newTitle
    * @pdOid 139f9b90-497b-40a4-8ba3-87feaa6d45e8 */
    public void setTitle(java.lang.String newTitle) {
        title = newTitle;
    }

    /** @pdOid 8ea86033-1e52-47c5-9340-d0a48ccc63e2 */
    public java.lang.String getContent() {
        return content;
    }

    /** @param newContent
    * @pdOid 59f29e35-5c1f-49da-ac0f-c4c6ae8bcaa1 */
    public void setContent(java.lang.String newContent) {
        content = newContent;
    }

    /** @pdOid b06d7e72-1208-42c0-8756-2238f89d6546 */
    public java.util.Date getPubishDate() {
        return pubishDate;
    }

    /** @param newPubishDate
    * @pdOid 93d0c0e9-6a65-4829-9a5e-1e2f2cd1d8ee */
    public void setPubishDate(java.util.Date newPubishDate) {
        pubishDate = newPubishDate;
    }

    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof NewsItem)) return false;
        NewsItem cast = (NewsItem) other;
        if (this.id == null ? cast.getId() != this.id : !this.id.equals(cast.getId())) return false;
        if (this.title == null ? cast.getTitle() != this.title : !this.title.equals(cast.getTitle())) return false;
        if (this.content == null ? cast.getContent() != this.content : !this.content.equals(cast.getContent())) return false;
        if (this.pubishDate == null ? cast.getPubishDate() != this.pubishDate : !(com.zspower.zp.util.Util.compareDate(this.pubishDate, cast.getPubishDate(), java.util.Calendar.SECOND) == 0)) return false;
        return true;
    }

    public int hashCode() {
        int hashCode = 0;
        if (this.title != null) hashCode = 29 * hashCode + title.hashCode();
        if (this.content != null) hashCode = 29 * hashCode + content.hashCode();
        if (this.pubishDate != null) hashCode = 29 * hashCode + pubishDate.hashCode();
        return hashCode;
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("com.zspower.zp.model.NewsItem: ");
        ret.append("id='" + id + "'");
        ret.append("title='" + title + "'");
        ret.append("content='" + content + "'");
        ret.append("pubishDate='" + pubishDate + "'");
        return ret.toString();
    }
}
