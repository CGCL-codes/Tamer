package com.odontosis.entidade;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import com.odontosis.OdontosisObject;

/**
 * Business Object Geral do sistema Odontosis.
 * @author Oto
 * @version
 * ltima modificao: $Author$<br>
 * Verso: $Revision$ $Date$
 */
@MappedSuperclass
public abstract class OdontosisBusinessObject extends OdontosisObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ativo")
    private boolean ativo = true;

    /**
	 * @return the id
	 */
    public final Long getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public final void setId(Long id) {
        this.id = id;
    }

    /**
	 * Hashcode do business object
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
	 * Verifica igualdade de dois objetos.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final OdontosisBusinessObject other = (OdontosisBusinessObject) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
