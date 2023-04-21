package easytc.service;

/**
 *
 * @author Alain Sellerin <alain.sellerin@easytc.org>
 */
public class NoteCreation {

    private String value;

    private String title;

    private String id;

    private String technoId;

    public String getTechnoId() {
        return technoId;
    }

    public void setTechnoId(String technoId) {
        this.technoId = technoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
