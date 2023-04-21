package der.ponto;

import entities.annotations.EntityDescriptor;
import entities.annotations.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author 39361
 */
@Entity
@EntityDescriptor(displayName = "Lista de funcionários por Lotação", pluralDisplayName = "Lista de funcionários por Lotação")
public class FuncionarioReport implements Serializable {

    private static final long serialVersionUID = 1L;

    private static long ID;

    @Id
    private Long id;

    @Column(length = 100)
    @PropertyDescriptor(index = 1)
    private String descricaoLotacao;

    @Column(length = 10)
    @PropertyDescriptor(index = 2)
    private String matricula;

    @Column(length = 40)
    @PropertyDescriptor(index = 3)
    private String nomeFuncionario;

    @Column(length = 10)
    @PropertyDescriptor(hidden = true)
    private String codLotacao;

    private String matriculaCompleta;

    private String nomeCompleto;

    private String numeroFuncionario;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicio;

    private String batePonto;

    public FuncionarioReport() {
    }

    public FuncionarioReport(String matriculaCompleta, String nomeCompleto, Integer numeroFuncionario, Lotacao lotacao, Date dataInicio, Short batePonto) {
        this.id = Long.valueOf(++ID);
        this.descricaoLotacao = lotacao.getDescricao();
        this.nomeCompleto = nomeCompleto;
        this.matriculaCompleta = matriculaCompleta;
        this.numeroFuncionario = numeroFuncionario.toString();
        this.dataInicio = dataInicio;
        this.batePonto = batePonto.toString();
    }

    public FuncionarioReport(String nomeFuncionario, String matricula, String codLotacao, String descricaoLotacao) {
        this.id = Long.valueOf(++ID);
        this.nomeFuncionario = nomeFuncionario;
        this.matricula = matricula;
        this.codLotacao = codLotacao;
        this.descricaoLotacao = descricaoLotacao;
    }

    public String getCodLotacao() {
        return codLotacao;
    }

    public void setCodLotacao(String codLotacao) {
        this.codLotacao = codLotacao;
    }

    public String getDescricaoLotacao() {
        return descricaoLotacao;
    }

    public void setDescricaoLotacao(String descricaoLotacao) {
        this.descricaoLotacao = descricaoLotacao;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatePonto() {
        return batePonto;
    }

    public void setBatePonto(String batePonto) {
        this.batePonto = batePonto;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getMatriculaCompleta() {
        return matriculaCompleta;
    }

    public void setMatriculaCompleta(String matriculaCompleta) {
        this.matriculaCompleta = matriculaCompleta;
    }

    public String getNumeroFuncionario() {
        return numeroFuncionario;
    }

    public void setNumeroFuncionario(String numeroFuncionario) {
        this.numeroFuncionario = numeroFuncionario;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FuncionarioReport other = (FuncionarioReport) obj;
        if ((this.nomeFuncionario == null) ? (other.nomeFuncionario != null) : !this.nomeFuncionario.equals(other.nomeFuncionario)) {
            return false;
        }
        if ((this.matriculaCompleta == null) ? (other.matriculaCompleta != null) : !this.matriculaCompleta.equals(other.matriculaCompleta)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.nomeFuncionario != null ? this.nomeFuncionario.hashCode() : 0);
        hash = 83 * hash + (this.matriculaCompleta != null ? this.matriculaCompleta.hashCode() : 0);
        return hash;
    }
}
