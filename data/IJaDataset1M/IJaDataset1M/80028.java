package ch.simas.jtoggl;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * 
 * @author Simon Martinelli
 */
public class Task {

    private Long id;

    private String name;

    private Long estimated_workhours;

    private Long estimated_seconds;

    private Boolean is_active;

    private Workspace workspace;

    private Project project;

    private User user;

    public Task() {
    }

    public Task(String jsonString) {
        JSONObject object = (JSONObject) JSONValue.parse(jsonString);
        this.id = (Long) object.get("id");
        this.name = (String) object.get("name");
        this.estimated_workhours = (Long) object.get("estimated_workhours");
        this.estimated_seconds = (Long) object.get("estimated_seconds");
        this.is_active = (Boolean) object.get("is_active");
        JSONObject workspaceObject = (JSONObject) object.get("workspace");
        if (workspaceObject != null) {
            this.workspace = new Workspace(workspaceObject.toJSONString());
        }
        JSONObject projectObject = (JSONObject) object.get("project");
        if (projectObject != null) {
            this.project = new Project(projectObject.toJSONString());
        }
        JSONObject userObject = (JSONObject) object.get("user");
        if (userObject != null) {
            this.user = new User(userObject.toJSONString());
        }
    }

    public Long getEstimated_seconds() {
        return estimated_seconds;
    }

    public void setEstimated_seconds(Long estimated_seconds) {
        this.estimated_seconds = estimated_seconds;
    }

    public Long getEstimated_workhours() {
        return estimated_workhours;
    }

    public void setEstimated_workhours(Long estimated_workhours) {
        this.estimated_workhours = estimated_workhours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public JSONObject toJSONObject() {
        JSONObject object = new JSONObject();
        if (id != null) {
            object.put("id", id);
        }
        if (name != null) {
            object.put("name", name);
        }
        if (estimated_workhours != null) {
            object.put("estimated_workhours", estimated_workhours);
        }
        if (estimated_seconds != null) {
            object.put("estimated_seconds", estimated_seconds);
        }
        if (is_active != null) {
            object.put("is_active", is_active);
        }
        if (workspace != null) {
            object.put("workspace", this.workspace.toJSONObject());
        }
        if (project != null) {
            object.put("project", this.project.toJSONObject());
        }
        if (user != null) {
            object.put("user", this.user.toJSONObject());
        }
        return object;
    }

    public String toJSONString() {
        return this.toJSONObject().toJSONString();
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", name=" + name + ", estimated_workhours=" + estimated_workhours + ", estimated_seconds=" + estimated_seconds + ", is_active=" + is_active + ", workspace=" + workspace + ", project=" + project + ", user=" + user + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
