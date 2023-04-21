package ejb;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Metodologia entity. @author MyEclipse Persistence Tools
 */
@Entity
@SequenceGenerator(name = "generator14", sequenceName = "metodologia_id_metodologia_seq")
@Table(name = "metodologia", schema = "public")
public class Metodologia implements java.io.Serializable {

    private Integer idMetodologia;

    private String nomeMetodologia;

    private String descricaoMetodologia;

    private Set<Projeto> projetos = new HashSet<Projeto>(0);

    /** default constructor */
    public Metodologia() {
    }

    /** minimal constructor */
    public Metodologia(Integer idMetodologia, String nomeMetodologia, String descricaoMetodologia) {
        this.idMetodologia = idMetodologia;
        this.nomeMetodologia = nomeMetodologia;
        this.descricaoMetodologia = descricaoMetodologia;
    }

    /** full constructor */
    public Metodologia(Integer idMetodologia, String nomeMetodologia, String descricaoMetodologia, Set<Projeto> projetos) {
        this.idMetodologia = idMetodologia;
        this.nomeMetodologia = nomeMetodologia;
        this.descricaoMetodologia = descricaoMetodologia;
        this.projetos = projetos;
    }

    @Id
    @GeneratedValue(generator = "generator14", strategy = GenerationType.AUTO)
    @Column(name = "id_metodologia", unique = true, nullable = false)
    public Integer getIdMetodologia() {
        return this.idMetodologia;
    }

    public void setIdMetodologia(Integer idMetodologia) {
        this.idMetodologia = idMetodologia;
    }

    @Column(name = "nome_metodologia", nullable = false, length = 50)
    public String getNomeMetodologia() {
        return this.nomeMetodologia;
    }

    public void setNomeMetodologia(String nomeMetodologia) {
        this.nomeMetodologia = nomeMetodologia;
    }

    @Column(name = "descricao_metodologia", nullable = false, length = 1000)
    public String getDescricaoMetodologia() {
        return this.descricaoMetodologia;
    }

    public void setDescricaoMetodologia(String descricaoMetodologia) {
        this.descricaoMetodologia = descricaoMetodologia;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "metodologia")
    public Set<Projeto> getProjetos() {
        return this.projetos;
    }

    public void setProjetos(Set<Projeto> projetos) {
        this.projetos = projetos;
    }
}
