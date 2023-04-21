package prc.bubulina.forum.dataclasses;

public class ForumThread {

    public int thread_id;

    public String name;

    public int topic_count;

    public ForumThread(int thread_id, String name, int topic_count) {
        this.thread_id = thread_id;
        this.name = name;
        this.topic_count = topic_count;
    }

    public String toString() {
        return "ID: " + thread_id + " name: " + name + " Topics: " + topic_count;
    }
}
