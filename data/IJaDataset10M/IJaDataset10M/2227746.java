package com.siasal.contabilidad.commons;

import java.util.Date;
import java.util.Set;
import com.siasal.bibliotecarios.commons.AdministradorTO;

/** @modelguid {47970545-3A57-4053-A397-FCCA46622C1C} */
public class PeriodoTO {

    /** @modelguid {115AEA4A-0445-48FE-B57C-0D8600D0D472} */
    private Integer id;

    /** @modelguid {613AB94F-E0B7-47FA-A629-C1048A6FB9F4} */
    private Date inicio;

    /** @modelguid {B3295BDB-E18E-464D-B456-AA65D870B8EF} */
    private Date fin;

    /** @modelguid {26AC1036-23F7-4C44-8EF7-160D367E20B1} */
    private Integer estado;

    /** @modelguid {2BC01F07-6614-4FCB-B96A-F63B9A1C4C88} */
    private java.util.Set EgresosTO;

    /** @modelguid {66198E09-4506-4967-99C5-926F709F1D60} */
    private java.util.Set IngresosTO;

    private AdministradorTO administrador;

    private Set saldosmoneda;

    public PeriodoTO(AdministradorTO administrador, Set egresosto, Integer estado, Date fin, Integer id, Set ingresosto, Date inicio, Set saldosmoneda) {
        super();
        this.administrador = administrador;
        EgresosTO = egresosto;
        this.estado = estado;
        this.fin = fin;
        this.id = id;
        IngresosTO = ingresosto;
        this.inicio = inicio;
        this.saldosmoneda = saldosmoneda;
    }

    public AdministradorTO getAdministrador() {
        return administrador;
    }

    public void setAdministrador(AdministradorTO administrador) {
        this.administrador = administrador;
    }

    public Set getSaldosmoneda() {
        return saldosmoneda;
    }

    public void setSaldosmoneda(Set saldosmoneda) {
        this.saldosmoneda = saldosmoneda;
    }

    /** @modelguid {FFA68246-D1B0-4E63-B9A7-9D53AD59E60F} */
    public PeriodoTO() {
    }

    /** @modelguid {71507A22-43E3-40E8-BDC0-2F3839FC3BFD} */
    public Integer getEstado() {
        return estado;
    }

    /** @modelguid {6BDD17FF-F71D-412C-9925-EA3947787B49} */
    public void setEstado(Integer aEstado) {
        estado = aEstado;
    }

    /** @modelguid {D218EE7B-E672-478A-A477-55395DC89881} */
    public Date getFin() {
        return fin;
    }

    /** @modelguid {A97ACC4A-C1C3-422D-A1A4-4EF8167B8EC4} */
    public void setFin(Date aFin) {
        fin = aFin;
    }

    /** @modelguid {13084B2F-DC52-456A-B95C-AFF8BC04D507} */
    public Integer getId() {
        return id;
    }

    /** @modelguid {DACA5F4A-A5DD-4576-85E7-F26C72973B33} */
    public void setId(Integer aId) {
        id = aId;
    }

    /** @modelguid {4F4C0075-A805-40AC-B423-42F42BEE7B13} */
    public Date getInicio() {
        return inicio;
    }

    /** @modelguid {D8A59AD3-57B9-4767-99C4-2C1015D5B803} */
    public void setInicio(Date aInicio) {
        inicio = aInicio;
    }

    /** @modelguid {4A319AA7-1BAC-4DDA-8C3D-B9C3C349B937} */
    public java.util.Set getEgresosTO() {
        return EgresosTO;
    }

    /** @modelguid {0D7C2918-808B-4B3B-BCBF-DF3B58BE88BF} */
    public void setEgresosTO(java.util.Set aEgresosTO) {
        EgresosTO = aEgresosTO;
    }

    /** @modelguid {35714A97-7413-4206-A7C0-36F9A9D9A388} */
    public java.util.Set getIngresosTO() {
        return IngresosTO;
    }

    /** @modelguid {0276779B-C4B7-4E6D-ABDB-9A173E42B843} */
    public void setIngresosTO(java.util.Set aIngresosTO) {
        IngresosTO = aIngresosTO;
    }
}
