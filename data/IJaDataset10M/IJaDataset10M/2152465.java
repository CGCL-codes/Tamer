package br.com.cinefilmes.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TipoCast implements Serializable {

    private static final long serialVersionUID = 1L;

    public Long codigo;

    public String descricao;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
        result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof TipoCast)) return false;
        final TipoCast other = (TipoCast) obj;
        if (codigo == null) {
            if (other.codigo != null) return false;
        } else if (!codigo.equals(other.codigo)) return false;
        if (descricao == null) {
            if (other.descricao != null) return false;
        } else if (!descricao.equals(other.descricao)) return false;
        return true;
    }
}
