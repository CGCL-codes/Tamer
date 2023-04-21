package entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Cliente {

    @Id
    @GeneratedValue
    private long OID;

    private String nomeFantasia;

    private String razaoSocial;

    private String cnpj;

    private String telefone;

    @OneToMany
    @PrimaryKeyJoinColumn
    private List<ClienteContato> contatos = new ArrayList<ClienteContato>();

    public long getOID() {
        return OID;
    }

    public void setOID(long oID) {
        OID = oID;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<ClienteContato> getContatos() {
        return contatos;
    }

    public void setContatos(List<ClienteContato> contato) {
        this.contatos = contato;
    }
}
