package entidades.atividade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Atividade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Date dtCriacao;

    @Column
    private Date dtRealizacao;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "atividade")
    private List<AlunoTurmaAtividade> alunoTurmaAtividade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtRealizacao() {
        return dtRealizacao;
    }

    public void setDtRealizacao(Date dtRealizacao) {
        this.dtRealizacao = dtRealizacao;
    }

    public List<AlunoTurmaAtividade> getAlunoTurmaAtividade() {
        return alunoTurmaAtividade;
    }

    public void setAlunoTurmaAtividade(List<AlunoTurmaAtividade> alunoTurmaAtividade) {
        this.alunoTurmaAtividade = alunoTurmaAtividade;
    }
}
