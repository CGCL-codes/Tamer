package apacheJiraMiner.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author kojy
 */
@Entity
@Table(name = "ISSUE")
public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String nome;

    private String assignee;

    private String reporter;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataCriada;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataResolvida;

    @OneToMany(mappedBy = "issue", orphanRemoval = true, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ISSUE_ID")
    private List<Comentario> comentarios;

    @Column(columnDefinition = "LONGTEXT")
    private String versoesAfetadas;

    @Column(columnDefinition = "LONGTEXT")
    private String componentes;

    @Column(columnDefinition = "LONGTEXT")
    private String versoesFixadas;

    @Column(length = 50)
    private String tipo;

    @Column(length = 50)
    private String status;

    @Column(length = 50)
    private String prioridade;

    @Column(length = 50)
    private String resolucao;

    @Column(columnDefinition = "LONGTEXT")
    private String descricao;

    private int numeroIssue;

    @ManyToOne
    private Projeto projeto;

    @Column(columnDefinition = "TEXT")
    private String ambiente;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.REFRESH, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "ISSUE_ID")
    private List<Commits> commits;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "ISSUE_ID")
    private List<Label> labels;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "ISSUE_ID")
    private List<TargetVersion> targetVersions;

    public Issue() {
        comentarios = new ArrayList<Comentario>();
        commits = new ArrayList<Commits>();
        labels = new ArrayList<Label>();
        targetVersions = new ArrayList<TargetVersion>();
    }

    public Issue(Projeto projeto) {
        this();
        projeto.addIssue(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getDataCriada() {
        return dataCriada;
    }

    public void setDataCriada(Date dataCriada) {
        this.dataCriada = dataCriada;
    }

    public Date getDataResolvida() {
        return dataResolvida;
    }

    public void setDataResolvida(Date dataResolvida) {
        this.dataResolvida = dataResolvida;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> coments) {
        if (coments != null) {
            for (Comentario cmt : coments) {
                this.addComentario(cmt);
            }
        }
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getResolucao() {
        return resolucao;
    }

    public void setResolucao(String resolucao) {
        this.resolucao = resolucao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNumeroIssue() {
        return numeroIssue;
    }

    public void setNumeroIssue(int numeroIssue) {
        this.numeroIssue = numeroIssue;
    }

    public String getComponentes() {
        return componentes;
    }

    public void setComponentes(String componentes) {
        this.componentes = componentes;
    }

    public String getVersoesAfetadas() {
        return versoesAfetadas;
    }

    public void setVersoesAfetadas(String versoesAfetadas) {
        this.versoesAfetadas = versoesAfetadas;
    }

    public String getVersoesFixadas() {
        return versoesFixadas;
    }

    public void setVersoesFixadas(String versoesFixadas) {
        this.versoesFixadas = versoesFixadas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public void addComentario(Comentario comentario) {
        if (!getComentarios().contains(comentario)) {
            getComentarios().add(comentario);
        }
        comentario.setIssue(this);
    }

    public List<Commits> getCommits() {
        return commits;
    }

    public void setCommits(List<Commits> commits) {
        this.commits = commits;
    }

    public void addCommit(Commits commit) {
        if (!getCommits().contains(commit) || commit.getId() == 0) {
            getCommits().add(commit);
        }
        commit.setIssue(this);
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public void addLabel(Label label) {
        if (!getLabels().contains(label) || label.getId() == 0) {
            getLabels().add(label);
        }
        label.setIssue(this);
    }

    public List<TargetVersion> getTargetVersions() {
        return targetVersions;
    }

    public void setTargetVersions(List<TargetVersion> targetVersions) {
        this.targetVersions = targetVersions;
    }

    public void addTargetVersion(TargetVersion targetVersion) {
        if (!getTargetVersions().contains(targetVersion) || targetVersion.getId() == 0) {
            getTargetVersions().add(targetVersion);
        }
        targetVersion.setIssue(this);
    }

    @Override
    public String toString() {
        if (this.getPrioridade() == null) {
            return null;
        } else {
            return "Issue{" + "num=" + getNumeroIssue() + ", autor=" + comentarios + ", tipo=" + tipo + ", status=" + status + ", prioridade=" + prioridade + ", resolvido=" + resolucao + '}';
        }
    }

    public void removeComentario(Comentario comentario) {
        if (getComentarios().contains(comentario)) {
            getComentarios().remove(comentario);
        }
        comentario.setIssue(null);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Issue)) {
            return false;
        }
        Issue other = (Issue) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
