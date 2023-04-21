package br.com.gesclub.business.model;

import java.io.Serializable;

/**
 * Entitidade base para as demais entidades.
 * Possui os métodos comuns para entidades que a estendem.
 *
 */
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * Método getId() que todas as entidades primárias devem implementar.
	 * 
	 * @return Long
	 */
    public abstract Long getId();

    /**
	 * Implementação mais geral que determina se o id está preenchido ou não. 
	 * 
	 * @return boolean
	 */
    public boolean isIdPreenchido() {
        return getId() != null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        if (!(obj instanceof BaseEntity)) return false;
        BaseEntity other = (BaseEntity) obj;
        if (getId() == null) {
            if (other.getId() != null) return false;
        } else if (!getId().equals(other.getId())) return false;
        return true;
    }
}
