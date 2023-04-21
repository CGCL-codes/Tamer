package bugcrawler.runtime.data;

import java.util.ArrayList;

public class BugContainer {

    private Priority priority;

    private Project project;

    private ArrayList<Bug> bugs = new ArrayList<Bug>();

    public BugContainer(Priority priority, Project project) {
        this.priority = priority;
        this.project = project;
    }

    public Priority getPriority() {
        return priority;
    }

    public void addBug(Bug bug) {
        bugs.add(bug);
    }

    public Object[] getBugs() {
        return bugs.toArray();
    }

    public Project getProject() {
        return project;
    }
}
