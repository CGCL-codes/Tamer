package name.jelen.reqtop.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Martin Jelen
 */
@Entity
@Table(name = "T51_Post")
@NamedQueries({ @NamedQuery(name = "Post.findById", query = "SELECT p FROM Post p WHERE p.id = :id"), @NamedQuery(name = "Post.findByAuthor", query = "SELECT p FROM Post p WHERE p.author = :author"), @NamedQuery(name = "Post.findByParent", query = "SELECT p FROM Post p WHERE p.parent = :parent"), @NamedQuery(name = "Post.findCreatedAfter", query = "SELECT p FROM Post p WHERE p.created > :after") })
public class Post implements Serializable {

    /***************************************************************************
	 * Properties
	 **************************************************************************/
    private static final long serialVersionUID = 5159408210523618851L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @JoinColumn(name = "T51T50TOPIC", nullable = false)
    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.PERSIST }, fetch = FetchType.EAGER)
    private Topic topic;

    @JoinColumn(name = "T51T40AUTHOR", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @JoinColumn(name = "T51T51PARENT")
    @ManyToOne
    private Post parent;

    @OneToMany(mappedBy = "parent", cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @OrderBy(value = "created ASC")
    private List<Post> replies;

    @Column(name = "T51BODY")
    @Lob
    private String body;

    @Column(name = "T51CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "T51EDITED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date edited;

    @Column(name = "T51TITLE", nullable = false)
    private String title;

    /***************************************************************************
	 * Constructors
	 **************************************************************************/
    public Post() {
    }

    /***************************************************************************
	 * "Interesting" methods
	 **************************************************************************/
    @PrePersist
    protected void onPersist() {
        this.created = new Date();
    }

    /***************************************************************************
	 * Generated getters/setters
	 **************************************************************************/
    public List<Post> getReplies() {
        return replies;
    }

    public void setReplies(List<Post> replies) {
        this.replies = replies;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public Post getParent() {
        return parent;
    }

    public void setParent(Post parent) {
        if (parent != this) this.parent = parent;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEdited() {
        return edited;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }

    /***************************************************************************
	 * Generated Object methods
	 **************************************************************************/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
