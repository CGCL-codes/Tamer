package br.ita.doacoes.domain.cadastrodoacoes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import br.ita.doacoes.domain.campanha.Campanha;

/**
 * @author Helder
 */
@Entity
@Table(name = "movimentacao_pacote")
public class Pacote {

    @Id
    @Column(name = "id_pacote")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_contraparte")
    private Contraparte contraparte;

    @ManyToOne
    @JoinColumn(name = "id_atendente")
    private Atendente atendente;

    @Temporal(TemporalType.DATE)
    @Column(name = "data")
    private Date data;

    @ManyToOne
    @JoinColumn(name = "id_campanha")
    private Campanha campanha;

    @OneToMany(mappedBy = "pacote", cascade = CascadeType.ALL)
    private Collection<Item> conteudo;

    public Atendente getAtendente() {
        return atendente;
    }

    public void setAtendente(Atendente atendente) {
        this.atendente = atendente;
    }

    public Campanha getCampanha() {
        return campanha;
    }

    public void setCampanha(Campanha campanha) {
        this.campanha = campanha;
    }

    public Collection<Item> getConteudo() {
        return conteudo;
    }

    public void setConteudo(Collection<Item> conteudo) {
        this.conteudo = conteudo;
    }

    public Contraparte getContraparte() {
        return contraparte;
    }

    public void setContraparte(Contraparte contraparte) {
        this.contraparte = contraparte;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Item> getItemsList() {
        List<Item> itemsList = new ArrayList<Item>(conteudo);
        return itemsList;
    }

    public void setItemsList(List<Item> itemsList) {
        Collection<Item> aux = new ArrayList<Item>();
        if (conteudo == null) conteudo = new ArrayList<Item>();
        for (Item t : conteudo) if (!itemsList.contains(t)) {
            aux.add(t);
        }
        for (Item t : aux) {
            conteudo.remove(t);
        }
        for (Item t : itemsList) if (!conteudo.contains(t)) {
            t.setPacote(this);
            conteudo.add(t);
        }
    }
}
