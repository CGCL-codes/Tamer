package net.sf.oreka.persistent;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "recport")
public class RecPort implements Serializable {

    private int id;

    @Id(generate = GeneratorType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
