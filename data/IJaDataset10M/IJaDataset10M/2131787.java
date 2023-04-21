package casdadm.domain.simulados;

import casdadm.core.Clonavel;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import casdadm.domain.*;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Imada
 */
@Entity
@Table(name = "curso_vestibular")
@NamedQueries({ @NamedQuery(name = "CursoVestibular.findById", query = "SELECT f FROM CursoVestibular f WHERE f.id = :id"), @NamedQuery(name = "CursoVestibular.findAll", query = "SELECT f FROM CursoVestibular f") })
public class CursoVestibular extends Clonavel implements Serializable {

    @Id
    @GeneratedValue()
    @Column(name = "id_curso_vestibular")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @OneToMany(mappedBy = "cursoVestibularPrimeiraOpcao", cascade = CascadeType.ALL)
    private List<VestibularAluno> vestibularAlunoPrimeiraOpcao;

    @OneToMany(mappedBy = "cursoVestibularSegundaOpcao", cascade = CascadeType.ALL)
    private List<VestibularAluno> vestibularAlunoSegundaOpcao;

    @OneToMany(mappedBy = "cursoVestibularTerceiraOpcao", cascade = CascadeType.ALL)
    private List<VestibularAluno> vestibularAlunoTerceiraOpcao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<VestibularAluno> getVestibularAlunoPrimeiraOpcao() {
        return vestibularAlunoPrimeiraOpcao;
    }

    public void setVestibularAlunoPrimeiraOpcao(List<VestibularAluno> vestibularAlunoPrimeiraOpcao) {
        this.vestibularAlunoPrimeiraOpcao = vestibularAlunoPrimeiraOpcao;
    }

    public List<VestibularAluno> getVestibularAlunoSegundaOpcao() {
        return vestibularAlunoSegundaOpcao;
    }

    public void setVestibularAlunoSegundaOpcao(List<VestibularAluno> vestibularAlunoSegundaOpcao) {
        this.vestibularAlunoSegundaOpcao = vestibularAlunoSegundaOpcao;
    }

    public List<VestibularAluno> getVestibularAlunoTerceiraOpcao() {
        return vestibularAlunoTerceiraOpcao;
    }

    public void setVestibularAlunoTerceiraOpcao(List<VestibularAluno> vestibularAlunoTerceiraOpcao) {
        this.vestibularAlunoTerceiraOpcao = vestibularAlunoTerceiraOpcao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CursoVestibular)) {
            return false;
        }
        CursoVestibular other = (CursoVestibular) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }
}
