package br.com.cinefilmes.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class AgenciaAtores implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long codigo;

    private String nome;

    private String telefone;

    private Contato contato;

    private Usuario usuarioAlteracao;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    @OneToOne
    public Contato getContato() {
        return contato;
    }

    @ManyToOne
    public Usuario getUsuarioAlteracao() {
        return usuarioAlteracao;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
        this.usuarioAlteracao = usuarioAlteracao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof AgenciaAtores)) return false;
        final AgenciaAtores other = (AgenciaAtores) obj;
        if (codigo == null) {
            if (other.codigo != null) return false;
        } else if (!codigo.equals(other.codigo)) return false;
        if (nome == null) {
            if (other.nome != null) return false;
        } else if (!nome.equals(other.nome)) return false;
        return true;
    }
}
