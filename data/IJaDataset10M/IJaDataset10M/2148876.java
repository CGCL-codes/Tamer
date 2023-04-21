package ru.goldenforests.forum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ForumThread implements Comparable {

    private long id;

    private ForumInfo forumInfo;

    private ForumPost root;

    private Set posts = new HashSet();

    private Map threadsSettings = new HashMap();

    public long getId() {
        return root.getId();
    }

    private void setId(long id) {
    }

    public ForumInfo getForumInfo() {
        return forumInfo;
    }

    public void setForumInfo(ForumInfo forumInfo) {
        this.forumInfo = forumInfo;
    }

    public ForumPost getRoot() {
        return root;
    }

    public void setRoot(ForumPost root) {
        this.root = root;
    }

    public Set getPosts() {
        return this.posts;
    }

    public void setPosts(Set posts) {
        this.posts = posts;
    }

    public Map getThreadsSettings() {
        return this.threadsSettings;
    }

    public void setThreadsSettings(Map s) {
        this.threadsSettings = s;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ForumThread)) return false;
        ForumThread other = (ForumThread) o;
        return this.getRoot().equals(other.getRoot());
    }

    public int hashCode() {
        return this.getRoot().hashCode();
    }

    public int compareTo(Object other) {
        return -(this.getRoot().compareTo(((ForumThread) other).getRoot()));
    }

    public String toString() {
        return "[thread id=" + getId() + "]";
    }
}
