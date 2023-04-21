package com.htdsoft.noyau;

import java.math.BigDecimal;
import java.sql.Date;
import com.htdsoft.exception.BusinessException;

class Echeance {

    private int idEcheance;

    private Date dateFinale;

    private BigDecimal montantEcheance;

    private boolean etat;

    public Echeance() {
    }

    public Echeance(Integer idEcheance, Date dateFinale, BigDecimal montantEcheance, boolean etat) throws BusinessException {
        if (idEcheance < 0) throw new BusinessException("idEcheance " + idEcheance + " Invalide");
        this.idEcheance = idEcheance;
        if (dateFinale == null) throw new BusinessException("dateFinale " + dateFinale + " Invalide");
        this.dateFinale = dateFinale;
        if (montantEcheance.signum() == 0) throw new BusinessException("montant Echeance " + montantEcheance + " Invalide");
        this.montantEcheance = montantEcheance;
        if (etat != true && etat != false) throw new BusinessException("etat " + etat + " Invalide");
        this.etat = etat;
    }

    public Date getDateFinale() {
        return dateFinale;
    }

    public void setDateFinale(Date dateFinale) throws BusinessException {
        if (dateFinale == null) {
            throw new BusinessException("dateFinale [" + dateFinale + "] est invalide");
        }
        this.dateFinale = dateFinale;
    }

    public int getIdEcheance() {
        return idEcheance;
    }

    public void setIdEcheance(int idEcheance) throws BusinessException {
        if (idEcheance < 0) {
            throw new BusinessException("idEcheance[" + idEcheance + "] invalide");
        }
        this.idEcheance = idEcheance;
    }

    public BigDecimal getMontantEcheance() {
        return montantEcheance;
    }

    public void setMontantEcheance(BigDecimal montantEcheance) throws BusinessException {
        if (montantEcheance.signum() == 0) {
            throw new BusinessException("montantEcheance [" + montantEcheance + "] invalide");
        }
        this.montantEcheance = montantEcheance;
    }

    public boolean isRegle() throws BusinessException {
        if (idEcheance < 0) {
            throw new BusinessException("Echeance N� " + idEcheance + " non valide");
        }
        return this.etat;
    }

    public void setEtat(boolean etat) throws BusinessException {
        if (etat != true && etat != false) {
            throw new BusinessException("Etat " + etat + " de l'�cheance non valide");
        }
        this.etat = etat;
    }
}
