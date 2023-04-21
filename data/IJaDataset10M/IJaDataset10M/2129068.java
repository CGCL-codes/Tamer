package siac.com.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the cad_medico database table.
 * 
 */
@Entity
@Table(name = "CadMedico")
@NamedQueries({ @NamedQuery(name = "CadMedico.findAll", query = "SELECT e FROM CadMedico e"), @NamedQuery(name = "CadMedico.findById", query = "SELECT e FROM CadMedico e WHERE e.id = :idTabela"), @NamedQuery(name = "CadMedico.findByCode", query = "SELECT e FROM CadMedico e WHERE e.cedula = :code"), @NamedQuery(name = "CadMedico.findByStatus", query = "SELECT e FROM CadMedico e WHERE e.activo = :status"), @NamedQuery(name = "CadMedico.findByEspecialidade", query = "SELECT e FROM CadMedico e WHERE e.especialidade = :especialidade"), @NamedQuery(name = "CadMedico.findByPessoa", query = "SELECT e FROM CadMedico e WHERE e.pessoa = :pessoa") })
public class CadMedico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @org.hibernate.annotations.NaturalId
    @Column(nullable = false, unique = true, length = 20)
    private String cedula;

    private boolean activo;

    @JoinColumn(name = "idEspecialidade", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CadEspecialidade especialidade = new CadEspecialidade();

    @JoinColumn(name = "idPessoa", referencedColumnName = "id", nullable = false)
    @OneToOne(optional = false)
    private CadPessoa pessoa = new CadPessoa();

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRegisto;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracao;

    @ManyToOne(optional = false)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private AuthUtilizador operadorRegisto = new AuthUtilizador();

    @ManyToOne(optional = false)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private AuthUtilizador operadorAlteracao = new AuthUtilizador();

    public CadMedico() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public CadEspecialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(CadEspecialidade especialidade) {
        this.especialidade = especialidade;
    }

    public CadPessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(CadPessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getDataRegisto() {
        return dataRegisto;
    }

    public void setDataRegisto(Date dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

    public AuthUtilizador getOperadorRegisto() {
        return operadorRegisto;
    }

    public void setOperadorRegisto(AuthUtilizador operadorRegisto) {
        this.operadorRegisto = operadorRegisto;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public AuthUtilizador getOperadorAlteracao() {
        return operadorAlteracao;
    }

    public void setOperadorAlteracao(AuthUtilizador operadorAlteracao) {
        this.operadorAlteracao = operadorAlteracao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CadMedico other = (CadMedico) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return cedula + " - " + especialidade + " --> " + pessoa.toString();
    }
}
