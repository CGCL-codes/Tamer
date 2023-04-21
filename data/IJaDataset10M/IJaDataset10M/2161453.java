package uk.icat3.logging.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "IdSequenceTable.findAll", query = "select o from IdSequenceTable o") })
@Table(name = "ID_SEQUENCE_TABLE")
public class IdSequenceTable implements Serializable {

    @Id
    @Column(nullable = false)
    private String name;

    private Long value;

    public IdSequenceTable() {
    }

    public IdSequenceTable(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
