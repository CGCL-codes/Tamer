package life.domain_model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Discussion {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "discussion")
    @OrderBy("creationDate ASC")
    private List<Post> posts;

    @Enumerated(EnumType.STRING)
    private ForumCategory category;

    private boolean sticked;

    private boolean closed;

    public Discussion() {
    }

    public Discussion(String title, ForumCategory category) {
        this.title = title;
        this.category = category;
        this.posts = new ArrayList<Post>();
        this.sticked = false;
        this.closed = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public ForumCategory getCategory() {
        return category;
    }

    public void setCategory(ForumCategory category) {
        this.category = category;
    }

    public boolean isSticked() {
        return sticked;
    }

    public void setSticked(boolean sticked) {
        this.sticked = sticked;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
