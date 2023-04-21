package com.eip.yost.dto;

import java.util.List;

public class ModuleDTO {

    private Integer idModule;

    private String nom;

    private Integer etat;

    private List<ModuleServeurDTO> moduleServeurList;

    private List<ConfigDTO> configList;

    public ModuleDTO() {
    }

    public ModuleDTO(Integer idModule, String nom, Integer etat, List<ModuleServeurDTO> moduleServeurList, List<ConfigDTO> configList) {
        super();
        this.idModule = idModule;
        this.nom = nom;
        this.etat = etat;
        this.moduleServeurList = moduleServeurList;
        this.configList = configList;
    }

    public Integer getIdModule() {
        return idModule;
    }

    public void setIdModule(Integer idModule) {
        this.idModule = idModule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getEtat() {
        return this.etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public List<ModuleServeurDTO> getModuleServeurList() {
        return moduleServeurList;
    }

    public void setModuleServeurList(List<ModuleServeurDTO> moduleServeurList) {
        this.moduleServeurList = moduleServeurList;
    }

    public List<ConfigDTO> getConfigList() {
        return configList;
    }

    public void setConfigList(List<ConfigDTO> configList) {
        this.configList = configList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((configList == null) ? 0 : configList.hashCode());
        result = prime * result + ((etat == null) ? 0 : etat.hashCode());
        result = prime * result + ((idModule == null) ? 0 : idModule.hashCode());
        result = prime * result + ((moduleServeurList == null) ? 0 : moduleServeurList.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ModuleDTO other = (ModuleDTO) obj;
        if (configList == null) {
            if (other.configList != null) return false;
        } else if (!configList.equals(other.configList)) return false;
        if (etat == null) {
            if (other.etat != null) return false;
        } else if (!etat.equals(other.etat)) return false;
        if (idModule == null) {
            if (other.idModule != null) return false;
        } else if (!idModule.equals(other.idModule)) return false;
        if (moduleServeurList == null) {
            if (other.moduleServeurList != null) return false;
        } else if (!moduleServeurList.equals(other.moduleServeurList)) return false;
        if (nom == null) {
            if (other.nom != null) return false;
        } else if (!nom.equals(other.nom)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ModuleDTO [idModule=" + idModule + ", nom=" + nom + ", etat=" + etat + ", moduleServeurList=" + moduleServeurList + ", configList=" + configList + "]";
    }
}
