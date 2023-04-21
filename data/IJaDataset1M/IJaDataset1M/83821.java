package br.com.mcampos.ejb.cloudsystem.user.attribute.companytype;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = CompanyType.getAll, query = "select o from CompanyType o"), @NamedQuery(name = CompanyType.nextId, query = "select max(o.id) from CompanyType o") })
@Table(name = "company_type")
public class CompanyType implements Serializable {

    public static final String getAll = "CompanyType.findAll";

    public static final String nextId = "CompanyType.nextId";

    @Column(name = "ctp_description_ch")
    private String description;

    @Id
    @Column(name = "ctp_id_in", nullable = false)
    private Integer id;

    public CompanyType() {
    }

    public CompanyType(Integer id) {
        setId(id);
    }

    public CompanyType(Integer id, String description) {
        setDescription(description);
        setId(id);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String id) {
        this.description = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer od) {
        this.id = od;
    }
}
