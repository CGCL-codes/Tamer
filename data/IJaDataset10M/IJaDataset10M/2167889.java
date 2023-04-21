package projecttree;

public class ProjectTreeListener implements IProjectTreeListener {

    protected static ProjectTreeListener instance = new ProjectTreeListener();

    public static ProjectTreeListener getInstance() {
        return instance;
    }

    public void add(ProjectTreeEvent event) {
    }

    public void remove(ProjectTreeEvent event) {
    }
}
