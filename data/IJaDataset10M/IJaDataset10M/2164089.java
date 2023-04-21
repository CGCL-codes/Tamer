package in980.rgms.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "linha_pesquisa_seq", sequenceName = "linha_pesquisa_seq")
public class LinhaPesquisa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "linha_pesquisa_seq")
    private Integer ID;

    private String titulo;

    private String descricao;

    private String descricaoDetalhada;

    private String links;

    private String financiadores;

    @ManyToMany(targetEntity = Membro.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "linhasPesquisa", fetch = FetchType.LAZY)
    private List<Membro> membros;

    @ManyToMany(targetEntity = Publicacao.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "linhasPesquisa", fetch = FetchType.LAZY)
    private List<Publicacao> publicacoes;

    public void validateToPersist() throws Exception {
        if (getTitulo().equals(null)) throw new Exception("Título inválido");
        if (getDescricao() == null) throw new Exception("Descrição inválida");
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer iD) {
        ID = iD;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricaoDetalhada() {
        return descricaoDetalhada;
    }

    public void setDescricaoDetalhada(String descricaoDetalhada) {
        this.descricaoDetalhada = descricaoDetalhada;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getFinanciadores() {
        return financiadores;
    }

    public void setFinanciadores(String financiadores) {
        this.financiadores = financiadores;
    }

    public List<Membro> getMembros() {
        return membros;
    }

    public void setMembros(List<Membro> membros) {
        this.membros = membros;
    }

    public List<Publicacao> getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(List<Publicacao> publicacoes) {
        this.publicacoes = publicacoes;
    }
}
