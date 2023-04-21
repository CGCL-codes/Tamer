package fr.insee.rome.bean;

public class Metier implements Comparable<Metier> {

    private String code, libelle;

    public Metier() {
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int compareTo(Metier other) {
        if (this == other) {
            return 0;
        }
        if (other == null) {
            return 1;
        }
        if (this.code == null) {
            if (this.libelle == null) {
                return -1;
            } else {
                return this.libelle.compareTo(other.libelle);
            }
        } else {
            if (this.libelle == null) {
                return this.code.compareTo(other.code);
            } else {
                if (this.code.compareTo(other.code) == 0) {
                    return this.libelle.compareTo(other.libelle);
                } else {
                    return this.code.compareTo(other.code);
                }
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 1;
        hash = prime * hash + ((code == null) ? 0 : code.hashCode());
        hash = prime * hash + ((libelle == null) ? 0 : libelle.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        Metier other = (Metier) object;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        if (libelle == null) {
            if (other.libelle != null) {
                return false;
            }
        } else if (!libelle.equals(other.libelle)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return code + " : " + libelle;
    }
}
