package org.ebadat.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Primary Key class ProvinciaPK for entity class Provincia
 * 
 * @author DSILVA
 */
@Embeddable
public class ProvinciaPK implements Serializable {

    @Column(name = "COD_PROVINCIA", nullable = false)
    private String codProvincia;

    @Column(name = "COD_DEPARTAMENTO", nullable = false)
    private String codDepartamento;

    /** Creates a new instance of ProvinciaPK */
    public ProvinciaPK() {
    }

    /**
     * Creates a new instance of ProvinciaPK with the specified values.
     * @param codDepartamento the codDepartamento of the ProvinciaPK
     * @param codProvincia the codProvincia of the ProvinciaPK
     */
    public ProvinciaPK(String codDepartamento, String codProvincia) {
        this.codDepartamento = codDepartamento;
        this.codProvincia = codProvincia;
    }

    /**
     * Gets the codProvincia of this ProvinciaPK.
     * @return the codProvincia
     */
    public String getCodProvincia() {
        return this.codProvincia;
    }

    /**
     * Sets the codProvincia of this ProvinciaPK to the specified value.
     * @param codProvincia the new codProvincia
     */
    public void setCodProvincia(String codProvincia) {
        this.codProvincia = codProvincia;
    }

    /**
     * Gets the codDepartamento of this ProvinciaPK.
     * @return the codDepartamento
     */
    public String getCodDepartamento() {
        return this.codDepartamento;
    }

    /**
     * Sets the codDepartamento of this ProvinciaPK to the specified value.
     * @param codDepartamento the new codDepartamento
     */
    public void setCodDepartamento(String codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.codDepartamento != null ? this.codDepartamento.hashCode() : 0);
        hash += (this.codProvincia != null ? this.codProvincia.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this ProvinciaPK.  The result is 
     * <code>true</code> if and only if the argument is not null and is a ProvinciaPK object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProvinciaPK)) {
            return false;
        }
        ProvinciaPK other = (ProvinciaPK) object;
        if (this.codDepartamento != other.codDepartamento && (this.codDepartamento == null || !this.codDepartamento.equals(other.codDepartamento))) return false;
        if (this.codProvincia != other.codProvincia && (this.codProvincia == null || !this.codProvincia.equals(other.codProvincia))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ebadat.domain.ProvinciaPK[codDepartamento=" + codDepartamento + ", codProvincia=" + codProvincia + "]";
    }
}
