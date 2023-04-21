package edu.csula.coolstatela.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "story")
public class Story {

    private Long storyID;

    private Long projectID;

    private Long priority;

    private Long editorID;

    private Long producerID;

    private String slug;

    private String subslug;

    private Date rundate;

    private Date killdate;

    private Date creationDate;

    private Date lastupdateDate;

    private String source;

    private String note;

    private String runlist;

    private String comments;

    private String status;

    private List<Media> assets;

    private List<StoryInstantiation> instantiations;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getStoryID() {
        return storyID;
    }

    public void setStoryID(Long storyID) {
        this.storyID = storyID;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSubslug() {
        return subslug;
    }

    public void setSubslug(String subslug) {
        this.subslug = subslug;
    }

    public Date getRundate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date formatDate = null;
        try {
            formatDate = sdf.parse(sdf.format(rundate));
        } catch (ParseException pe) {
            return null;
        }
        return formatDate;
    }

    public void setRundate(Date rundate) {
        this.rundate = rundate;
    }

    public Date getKilldate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date formatDate = null;
        try {
            formatDate = sdf.parse(sdf.format(killdate));
        } catch (ParseException pe) {
            return null;
        }
        return formatDate;
    }

    public void setKilldate(Date killdate) {
        this.killdate = killdate;
    }

    public Date getCreationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date formatDate = null;
        try {
            formatDate = sdf.parse(sdf.format(creationDate));
        } catch (ParseException pe) {
            return null;
        }
        return formatDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastupdateDate() {
        return lastupdateDate;
    }

    public void setLastupdateDate(Date lastupdateDate) {
        this.lastupdateDate = lastupdateDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRunlist() {
        return runlist;
    }

    public void setRunlist(String runlist) {
        this.runlist = runlist;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @OneToMany
    @JoinColumn(name = "storyID")
    public List<Media> getAssets() {
        return assets;
    }

    public void setAssets(List<Media> assets) {
        this.assets = assets;
    }

    @OneToMany
    @JoinColumn(name = "storyID")
    public List<StoryInstantiation> getInstantiations() {
        return instantiations;
    }

    public void setInstantiations(List<StoryInstantiation> instantiations) {
        this.instantiations = instantiations;
    }

    public Long getEditorID() {
        return editorID;
    }

    public void setEditorID(Long editorID) {
        this.editorID = editorID;
    }

    public Long getProducerID() {
        return producerID;
    }

    public void setProducerID(Long producerID) {
        this.producerID = producerID;
    }
}
