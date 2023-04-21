package net.cepra.core.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import net.cepra.core.common.model.Model;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
public class Division extends Model {

    private static final long serialVersionUID = -8277649700052049931L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    @Column(nullable = false)
    private Integer vers;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = true)
    private Date closingDate;

    public Division() {
    }

    public Division(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public Integer getVers() {
        return vers;
    }
}
