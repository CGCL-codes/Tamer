package br.com.projeto.pojo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author ALUNO
 */
@Entity
public class Disciplina implements Serializable {

    @Id
    @Column(name = "ID_DISCIPLINA")
    private Long codigoDaDisciplina;

    @Column(name = "NOME")
    private String nomeDaDisciplina;

    @Column(name = "PERIODO")
    private int periodo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aluno", insertable = true, updatable = true)
    @Fetch(FetchMode.JOIN)
    @Cascade(CascadeType.SAVE_UPDATE)
    private Aluno aluno;

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Long getCodigoDaDisciplina() {
        return codigoDaDisciplina;
    }

    public void setCodigoDaDisciplina(Long codigoDaDisciplina) {
        this.codigoDaDisciplina = codigoDaDisciplina;
    }

    public String getNomeDaDisciplina() {
        return nomeDaDisciplina;
    }

    public void setNomeDaDisciplina(String nomeDaDisciplina) {
        this.nomeDaDisciplina = nomeDaDisciplina;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }
}
