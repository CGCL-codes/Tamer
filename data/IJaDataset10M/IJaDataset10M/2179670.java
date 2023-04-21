package org.jazzteam.snipple.model;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.format.annotation.DateTimeFormat;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@XStreamAlias("article")
public class Article extends Identifiable {

    private String title;

    private int rating;

    @DateTimeFormat(pattern = "dd-MM-yyyy'T'hh:mm")
    private Date creationDate;

    @Lob
    private String content;

    @ManyToOne
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags;

    @OneToMany
    private List<Snippet> snippets;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    public List<Comment> comments;

    public Article() {
        creationDate = new Date();
    }

    public Article(String title, String content, User user, List<Tag> tags) {
        this.title = title;
        this.creationDate = new Date();
        this.content = content;
        this.user = user;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Snippet> getSnippets() {
        return snippets;
    }

    public void setSnippets(List<Snippet> snippets) {
        this.snippets = snippets;
    }
}
