package eu.maydu.gwtvlhibernate.server.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
public class ReferencedObject implements Serializable, IsSerializable {

    private Long id;

    private String value;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
