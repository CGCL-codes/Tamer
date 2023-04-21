package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "topic")
public class Topic implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "topic_id")
    private int topicId;

    private String title;

    private boolean sticky = false;

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    List<TopicPost> posts = new ArrayList<TopicPost>();

    public Topic() {
    }

    public Topic(String title, boolean sticky, Course course) {
        this.title = title;
        this.sticky = sticky;
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<TopicPost> getPosts() {
        return posts;
    }

    public void setPosts(List<TopicPost> posts) {
        this.posts = posts;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
}
