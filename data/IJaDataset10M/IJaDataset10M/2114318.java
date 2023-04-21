package com.htdsoft.noyau;

import com.htdsoft.exception.*;

public class Journal {

    private int idJournal;

    private String libelle;

    private String initiales;

    private String type;

    private boolean attribue;

    public Journal() {
    }

    public Journal(int idJournal, String libelle, String initiales, String type, boolean attribue) throws BusinessException {
        if (idJournal < 0) throw new BusinessException("idJournal " + idJournal + "Invalide");
        this.idJournal = idJournal;
        if (libelle == null || libelle.trim().equals("")) throw new BusinessException("libelle " + libelle + "Invalide");
        this.libelle = libelle;
        if (initiales == null || initiales.trim().equals("")) throw new BusinessException("initiales " + initiales + "Invalide");
        this.initiales = initiales;
        if (type == null || type.trim().equals("")) throw new BusinessException("type " + type + "Invalide");
        this.type = type;
        if (attribue != false && attribue != true) throw new BusinessException("attribue " + attribue + " Invalide");
        this.attribue = attribue;
    }

    public int getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(int idJournal) throws BusinessException {
        if (idJournal < 0) {
            throw new BusinessException("idJournal [" + idJournal + "]invalide");
        }
        this.idJournal = idJournal;
    }

    public String getInitiales() {
        return initiales;
    }

    public void setInitiales(String initiales) throws BusinessException {
        if (initiales == null || initiales.trim().equals("")) {
            throw new BusinessException("initiales[" + initiales + "]invalide");
        }
        this.initiales = initiales;
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

    public String getType() {
        return type;
    }

    public void setType(String type) throws BusinessException {
        if (type == null || type.trim().equals("")) {
            throw new BusinessException("type [" + type + "] invalide");
        }
        this.type = type;
    }

    public boolean isAttribue() {
        return attribue;
    }

    public void setAttribue(boolean attribue) throws BusinessException {
        if (attribue != true && attribue != false) {
            throw new BusinessException("attribue [" + attribue + "] invalide");
        }
        this.attribue = attribue;
    }

    @Override
    public String toString() {
        return this.libelle + " [" + type + "]";
    }
}
