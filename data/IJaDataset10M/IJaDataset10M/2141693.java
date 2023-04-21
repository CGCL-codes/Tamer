package net.diretrix.suporte.dominio.entidade.contato;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.Table;
import javax.persistence.Transient;
import net.diretrix.util.telefone.Telefone;
import net.diretrix.util.telefone.TelefoneFactory;
import net.diretrix.util.telefone.exception.TelefoneException;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Fabricio
 */
@Entity(name = "Telefone")
@Table(name = "telefone")
public class TelefoneDeContato implements AbstractTelefoneComContatos<Integer, Integer, Contato> {

    public static final Logger logger4j = Logger.getLogger(TelefoneDeContato.class);

    private int id;

    private Pessoa cadastro;

    private Telefone telefone;

    private Map<Integer, Contato> contatos;

    private Integer idPessoa;

    public TelefoneDeContato() throws TelefoneException {
        setContatos(new HashMap<Integer, Contato>());
    }

    public TelefoneDeContato(Telefone telefone) throws TelefoneException {
        setContatos(new HashMap<Integer, Contato>());
    }

    @Id
    @Column(name = "id_telefone")
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_pessoa")
    public Pessoa getCadastro() {
        return cadastro;
    }

    public void setCadastro(Pessoa cadastro) {
        this.cadastro = cadastro;
    }

    @Transient
    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    @Column(name = "nr_telefone")
    public String getNumero() {
        return getTelefone().getNumero();
    }

    public void setNumero(String numero) {
        TelefoneFactory factory = new TelefoneFactory();
        try {
            setTelefone(factory.criarTelefoneBrasileiro(numero));
        } catch (TelefoneException ex) {
            logger4j.error("setNumero - " + ex.getMessage());
        }
    }

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER, mappedBy = "telefones")
    @Fetch(FetchMode.SUBSELECT)
    @MapKey(name = "id")
    @Override
    public Map<Integer, Contato> getContatos() {
        return contatos;
    }

    @Override
    public void setContatos(Map<Integer, Contato> contatos) {
        this.contatos = contatos;
    }

    @Override
    public void adicionarContato(Contato contato) {
        if (!contatos.containsValue(contato)) {
            contatos.put(contato.getId(), contato);
        }
        if (!contato.getTelefones().contains(this)) {
            contato.adicionarTelefone(this);
        }
    }

    @Override
    public void removerContato(Contato contato) {
        if (contatos.containsValue(contato)) {
            contatos.remove(contato.getId());
        }
        if (contato.getTelefones().contains(this)) {
            contato.removerTelefone(this);
        }
    }

    @Override
    public void removerContatoPeloId(Integer id) {
        removerContato(getContatos().get(id));
    }

    @Override
    public void setContato(Integer id, Contato contato) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
