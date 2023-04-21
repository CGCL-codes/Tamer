package com.eip.yost.dto;

import java.util.Date;
import java.util.List;

public class ModuleServeurDTO {

    private Integer idmoduleServeur;

    private ModuleDTO module;

    private ServeurDTO serveur;

    private Date derniereMaj;

    private String jeton;

    private List<AffectationDTO> affectationList;

    public ModuleServeurDTO() {
    }

    public ModuleServeurDTO(Integer idmoduleServeur, ModuleDTO module, ServeurDTO serveur, Date derniereMaj, String jeton, List<AffectationDTO> affectionList) {
        super();
        this.idmoduleServeur = idmoduleServeur;
        this.module = module;
        this.serveur = serveur;
        this.derniereMaj = derniereMaj;
        this.jeton = jeton;
        this.affectationList = affectionList;
    }

    public Integer getIdmoduleServeur() {
        return idmoduleServeur;
    }

    public void setIdmoduleServeur(Integer idmoduleServeur) {
        this.idmoduleServeur = idmoduleServeur;
    }

    public ModuleDTO getModule() {
        return module;
    }

    public void setModule(ModuleDTO module) {
        this.module = module;
    }

    public ServeurDTO getServeur() {
        return serveur;
    }

    public void setServeur(ServeurDTO serveur) {
        this.serveur = serveur;
    }

    public Date getDerniereMaj() {
        return derniereMaj;
    }

    public void setDerniereMaj(Date derniereMaj) {
        this.derniereMaj = derniereMaj;
    }

    public String getJeton() {
        return jeton;
    }

    public void setJeton(String jeton) {
        this.jeton = jeton;
    }

    public List<AffectationDTO> getAffectationList() {
        return affectationList;
    }

    public void setAffectationList(List<AffectationDTO> affectationList) {
        this.affectationList = affectationList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((affectationList == null) ? 0 : affectationList.hashCode());
        result = prime * result + ((derniereMaj == null) ? 0 : derniereMaj.hashCode());
        result = prime * result + ((idmoduleServeur == null) ? 0 : idmoduleServeur.hashCode());
        result = prime * result + ((jeton == null) ? 0 : jeton.hashCode());
        result = prime * result + ((module == null) ? 0 : module.hashCode());
        result = prime * result + ((serveur == null) ? 0 : serveur.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ModuleServeurDTO other = (ModuleServeurDTO) obj;
        if (affectationList == null) {
            if (other.affectationList != null) return false;
        } else if (!affectationList.equals(other.affectationList)) return false;
        if (derniereMaj == null) {
            if (other.derniereMaj != null) return false;
        } else if (!derniereMaj.equals(other.derniereMaj)) return false;
        if (idmoduleServeur == null) {
            if (other.idmoduleServeur != null) return false;
        } else if (!idmoduleServeur.equals(other.idmoduleServeur)) return false;
        if (jeton == null) {
            if (other.jeton != null) return false;
        } else if (!jeton.equals(other.jeton)) return false;
        if (module == null) {
            if (other.module != null) return false;
        } else if (!module.equals(other.module)) return false;
        if (serveur == null) {
            if (other.serveur != null) return false;
        } else if (!serveur.equals(other.serveur)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ModuleServeurDTO [idmoduleServeur=" + idmoduleServeur + ", module=" + module + ", serveur=" + serveur + ", derniereMaj=" + derniereMaj + ", jeton=" + jeton + ", affectationList=" + affectationList + "]";
    }
}
