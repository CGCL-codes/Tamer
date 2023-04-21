package br.com.gestorescolar.to;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TB_AVALIACAO")
public class AvaliacaoTO {

    private Integer idAvaliacao;

    private SerieTO serie;

    private String nome;

    private Double notaMaxima;

    private DisciplinaTO disciplina;

    private String descricao;

    private List<NotaTO> clNotas;

    private ProfessorTO professor;

    private String bimestre;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(Integer idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    @OneToOne
    public SerieTO getSerie() {
        return serie;
    }

    public void setSerie(SerieTO serie) {
        this.serie = serie;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getNotaMaxima() {
        return notaMaxima;
    }

    public void setNotaMaxima(Double notaMaxima) {
        this.notaMaxima = notaMaxima;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public List<NotaTO> getClNotas() {
        if (clNotas == null) clNotas = new ArrayList<NotaTO>();
        return clNotas;
    }

    public void setClNotas(List<NotaTO> clNotas) {
        this.clNotas = clNotas;
    }

    public String getBimestre() {
        return bimestre;
    }

    public void setBimestre(String bimestre) {
        this.bimestre = bimestre;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @OneToOne
    public ProfessorTO getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorTO professor) {
        this.professor = professor;
    }

    @OneToOne
    public DisciplinaTO getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(DisciplinaTO disciplina) {
        this.disciplina = disciplina;
    }
}
