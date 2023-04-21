package com.htdsoft.noyau;

import java.math.BigDecimal;
import com.htdsoft.exception.*;

public class Tva {

    private int idTva;

    private String libelle;

    private BigDecimal tauxTva;

    public Tva() {
    }

    public Tva(int idTva, String libelle, BigDecimal tauxTva) throws BusinessException {
        if (idTva < 0) throw new BusinessException("idTva " + idTva + " Invalide");
        this.idTva = idTva;
        if (libelle == null || libelle.trim().equals("")) throw new BusinessException("libelle " + libelle + "Invalide");
        this.libelle = libelle;
        if (tauxTva.signum() < 0) throw new BusinessException("tauxTva " + tauxTva + "Invalide");
        this.tauxTva = tauxTva;
    }

    public int getIdTva() {
        return idTva;
    }

    public void setIdTva(int idTva) throws BusinessException {
        if (idTva < 0) {
            throw new BusinessException("idTva[" + idTva + "] invalide");
        }
        this.idTva = idTva;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) throws BusinessException {
        if (libelle == null || libelle.trim().equals("")) {
            throw new BusinessException("libelle[" + libelle + "] invalide");
        }
        this.libelle = libelle;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) throws BusinessException {
        if (tauxTva.signum() < 0) {
            throw new BusinessException("tauxTva [" + tauxTva + "] invalide");
        }
        this.tauxTva = tauxTva;
    }
}
