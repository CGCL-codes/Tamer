package model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ricardol
 */
public class MedicalCondition implements Serializable {

    private String id;

    private String name;

    private List<Disease> diseases;

    public MedicalCondition() {
    }

    public MedicalCondition(String id, String name) {
        super();
        this.name = name;
        this.id = id;
    }

    public MedicalCondition(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }
}
