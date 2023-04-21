package project.entities.authorization;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dictroles", schema = "\"authorization\"")
public class DictRoles implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String type;

    public DictRoles(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public DictRoles() {
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "Name", nullable = false, unique = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Type", nullable = false, unique = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DictRoles [name=" + name + "]";
    }
}
