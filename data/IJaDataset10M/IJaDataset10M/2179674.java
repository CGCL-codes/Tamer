package com.tesisutn.restsoft.dominio.proveedor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import com.tesisutn.restsoft.dominio.datos.CondicionIVA;
import com.tesisutn.restsoft.dominio.persona.Persona;

@Entity
public class Proveedor extends Persona {

    private String razonSocial;

    private Boolean esPersonaFisica;

    private String nombreFantasia;

    private String numeroCuit;

    @ManyToOne
    private CondicionIVA condicionIVA;

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreFantasia() {
        return nombreFantasia;
    }

    public void setNombreFantasia(String nombreFantasia) {
        this.nombreFantasia = nombreFantasia;
    }

    public String getNumeroCuit() {
        return numeroCuit;
    }

    public void setNumeroCuit(String numeroCuit) {
        this.numeroCuit = numeroCuit;
    }

    public CondicionIVA getCondicionIVA() {
        return condicionIVA;
    }

    public void setCondicionIVA(CondicionIVA condicionIVA) {
        this.condicionIVA = condicionIVA;
    }

    public void setPersonaFisica(Boolean esPersonaFisica) {
        this.setEsPersonaFisica(esPersonaFisica);
    }

    public Boolean esPersonaFisica() {
        return esPersonaFisica;
    }

    public void setEsPersonaFisica(Boolean esPersonaFisica) {
        this.esPersonaFisica = esPersonaFisica;
    }

    @Override
    public String toString() {
        return razonSocial;
    }
}
