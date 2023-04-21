package cn.jsprun.domain;

public class RsscachesId implements java.io.Serializable {

    private static final long serialVersionUID = -7689304748902118178L;

    private Integer lastupdate;

    private Short fid;

    private Integer tid;

    private Integer dateline;

    private String forum;

    private String author;

    private String subject;

    private String description;

    public RsscachesId() {
    }

    public RsscachesId(Integer lastupdate, Short fid, Integer tid, Integer dateline, String forum, String author, String subject, String description) {
        this.lastupdate = lastupdate;
        this.fid = fid;
        this.tid = tid;
        this.dateline = dateline;
        this.forum = forum;
        this.author = author;
        this.subject = subject;
        this.description = description;
    }

    public Integer getLastupdate() {
        return this.lastupdate;
    }

    public void setLastupdate(Integer lastupdate) {
        this.lastupdate = lastupdate;
    }

    public Short getFid() {
        return this.fid;
    }

    public void setFid(Short fid) {
        this.fid = fid;
    }

    public Integer getTid() {
        return this.tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getDateline() {
        return this.dateline;
    }

    public void setDateline(Integer dateline) {
        this.dateline = dateline;
    }

    public String getForum() {
        return this.forum;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof RsscachesId)) return false;
        RsscachesId castOther = (RsscachesId) other;
        return ((this.getLastupdate() == castOther.getLastupdate()) || (this.getLastupdate() != null && castOther.getLastupdate() != null && this.getLastupdate().equals(castOther.getLastupdate()))) && ((this.getFid() == castOther.getFid()) || (this.getFid() != null && castOther.getFid() != null && this.getFid().equals(castOther.getFid()))) && ((this.getTid() == castOther.getTid()) || (this.getTid() != null && castOther.getTid() != null && this.getTid().equals(castOther.getTid()))) && ((this.getDateline() == castOther.getDateline()) || (this.getDateline() != null && castOther.getDateline() != null && this.getDateline().equals(castOther.getDateline()))) && ((this.getForum() == castOther.getForum()) || (this.getForum() != null && castOther.getForum() != null && this.getForum().equals(castOther.getForum()))) && ((this.getAuthor() == castOther.getAuthor()) || (this.getAuthor() != null && castOther.getAuthor() != null && this.getAuthor().equals(castOther.getAuthor()))) && ((this.getSubject() == castOther.getSubject()) || (this.getSubject() != null && castOther.getSubject() != null && this.getSubject().equals(castOther.getSubject()))) && ((this.getDescription() == castOther.getDescription()) || (this.getDescription() != null && castOther.getDescription() != null && this.getDescription().equals(castOther.getDescription())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getLastupdate() == null ? 0 : this.getLastupdate().hashCode());
        result = 37 * result + (getFid() == null ? 0 : this.getFid().hashCode());
        result = 37 * result + (getTid() == null ? 0 : this.getTid().hashCode());
        result = 37 * result + (getDateline() == null ? 0 : this.getDateline().hashCode());
        result = 37 * result + (getForum() == null ? 0 : this.getForum().hashCode());
        result = 37 * result + (getAuthor() == null ? 0 : this.getAuthor().hashCode());
        result = 37 * result + (getSubject() == null ? 0 : this.getSubject().hashCode());
        result = 37 * result + (getDescription() == null ? 0 : this.getDescription().hashCode());
        return result;
    }
}
