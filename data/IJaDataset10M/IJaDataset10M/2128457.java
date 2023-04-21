package uk.icat3.logging.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@NamedQueries({ @NamedQuery(name = "LogKeyword.findAll", query = "select o from LogKeyword o") })
@Table(name = "LOG_KEYWORD")
public class LogKeyword implements Serializable {

    @TableGenerator(name = "KEYWORD_GENERATOR", table = "ID_SEQUENCE_TABLE", pkColumnName = "NAME", valueColumnName = "VALUE", pkColumnValue = "KEYWORD_KEY", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "KEYWORD_GENERATOR")
    @Column(nullable = false)
    private Long id;

    private String keyword;

    @OneToMany(mappedBy = "logKeyword")
    private List<AdvKeyword> advKeywordList;

    public LogKeyword() {
    }

    public LogKeyword(Long id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<AdvKeyword> getAdvKeywordList() {
        return advKeywordList;
    }

    public void setAdvKeywordList(List<AdvKeyword> advKeywordList) {
        this.advKeywordList = advKeywordList;
    }

    public AdvKeyword addAdvKeyword(AdvKeyword advKeyword) {
        getAdvKeywordList().add(advKeyword);
        advKeyword.setLogKeyword(this);
        return advKeyword;
    }

    public AdvKeyword removeAdvKeyword(AdvKeyword advKeyword) {
        getAdvKeywordList().remove(advKeyword);
        advKeyword.setLogKeyword(null);
        return advKeyword;
    }
}
