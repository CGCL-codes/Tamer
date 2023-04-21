package pogo.gene;

public class ServerDefinitions {

    public String name;

    public String inherited_from = null;

    public String inherited_class = null;

    public String description;

    public String project_title;

    public ServerDefinitions() {
        this.name = "";
        this.description = "";
        this.project_title = "";
    }

    public ServerDefinitions(String name, String inherited_from, String description, String project_title) {
        this.name = name;
        this.inherited_from = inherited_from;
        this.description = description;
        this.project_title = project_title;
        if (inherited_from == null) inherited_class = "Device_2Impl"; else {
            int idx = inherited_from.lastIndexOf('/');
            if (idx < 0) inherited_class = inherited_from; else inherited_class = inherited_from.substring(idx + 1);
        }
    }

    public String toString() {
        return name;
    }
}
