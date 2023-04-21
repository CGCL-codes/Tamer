package br.com.jnfe.core;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Transient;
import org.helianto.inventory.Picking;

/**
 * Volumes transportados.
 * 
 * @author Mauricio Fernandes de Castro
 */
@javax.persistence.Entity
@DiscriminatorValue("V")
public class Volume extends Picking {

    private static final long serialVersionUID = 1L;

    private String marca;

    private String nVol;

    /**
     * Construtor.
     */
    public Volume() {
        super();
    }

    /**
     * <<Transient>> Quantidade de volumes transportados.
     */
    @Transient
    public BigDecimal getQVol() {
        return super.getQuantity();
    }

    /**
     * <<Transient>> Esp�cie de volumes transportados.
     */
    @Transient
    public String getEsp() {
        return super.getPackaging();
    }

    /**
     * Marca dos volumes transportados.
     */
    @Column(length = 60)
    public String getMarca() {
        return marca;
    }

    public void setMarca(String value) {
        this.marca = value;
    }

    /**
     * Numera��o dos volumes transportados.
     */
    @Column(length = 60)
    public String getNVol() {
        return nVol;
    }

    public void setNVol(String value) {
        this.nVol = value;
    }

    /**
     * Peso L�quido (em kg).
     */
    @Transient
    public BigDecimal getPesoL() {
        return super.getNetWeight();
    }

    /**
     * Peso Bruto (em kg).
     */
    @Transient
    public BigDecimal getPesoB() {
        return super.getGrossWeight();
    }

    /**
    * equals
    */
    public boolean equals(Object other) {
        if (other instanceof Volume) {
            return super.equals(other);
        }
        return false;
    }
}
