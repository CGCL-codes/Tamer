package com.agileprojectassistant.server;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.agileprojectassistant.client.UserStoryCondition;
import com.google.appengine.api.datastore.Key;

@Entity
public class UserStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    @ManyToOne
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userStory")
    private List<Task> tasks;

    private String title;

    private int points;

    private UserStoryCondition condition;

    public UserStory(String title, Project project) {
        super();
        this.title = title;
        this.project = project;
        this.tasks = new LinkedList<Task>();
        this.points = 0;
        this.condition = UserStoryCondition.USP;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public boolean removeTask(Task task) {
        return tasks.remove(task);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public UserStoryCondition getCondition() {
        return condition;
    }

    public void setCondition(UserStoryCondition condition) {
        this.condition = condition;
    }

    public Long getID() {
        return key.getId();
    }

    public Key getKey() {
        return key;
    }

    public Project getProject() {
        return project;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
