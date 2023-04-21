package org.weras.portal.clientes.domain.escola.simples;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.weras.portal.clientes.domain.ObjetoPersistente;
import org.weras.portal.clientes.domain.pessoa.fisica.PessoaFisica;

@Entity
@Table(name = "TurmaSimples")
public class TurmaSimples extends ObjetoPersistente {

    private static final long serialVersionUID = 1L;

    private DisciplinaSimples disciplina;

    private Integer ano;

    private Integer periodo;

    private SalaSimples sala;

    private PessoaFisica professor;

    private PessoaFisica monitor;

    @ManyToOne
    @JoinColumn(name = TurmasSimples.CAMPO_ID_DISCIPLINA)
    public DisciplinaSimples getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(DisciplinaSimples disciplina) {
        this.disciplina = disciplina;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    @ManyToOne
    @JoinColumn(name = TurmasSimples.CAMPO_ID_SALA)
    public SalaSimples getSala() {
        return sala;
    }

    public void setSala(SalaSimples sala) {
        this.sala = sala;
    }

    @ManyToOne
    @JoinColumn(name = TurmasSimples.CAMPO_ID_PROFESSOR)
    public PessoaFisica getProfessor() {
        return professor;
    }

    public void setProfessor(PessoaFisica professor) {
        this.professor = professor;
    }

    @ManyToOne
    @JoinColumn(name = TurmasSimples.CAMPO_ID_MONITOR)
    public PessoaFisica getMonitor() {
        return monitor;
    }

    public void setMonitor(PessoaFisica monitor) {
        this.monitor = monitor;
    }
}
