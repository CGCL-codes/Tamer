package com.blindeye.model.blindeyes;

import static org.jboss.seam.ScopeType.EVENT;
import static org.jboss.seam.ScopeType.SESSION;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Role;
import org.jboss.seam.annotations.Scope;
import com.blindeye.util.IdGenerator;

@Entity
@Name("jobOutputTrigger")
@Scope(SESSION)
@Role(name = "tempJobOutputTrigger", scope = EVENT)
@Table(name = "jobOutputTriggers")
public class JobOutputTrigger implements Serializable {

    private Integer id;

    private int version;

    private String name;

    private String type;

    private String script;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Lob
    @Column(length = 2147483647)
    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Length(max = 25)
    @Column(length = 25)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: " + getId() + " Name: " + getName() + " Type: " + getType()).append("\n");
        sb.append("Script: " + getScript());
        return sb.toString();
    }
}
