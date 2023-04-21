package at.neckam.rezepte.bo;

import java.util.HashSet;
import java.util.Set;

public class Rezept {

    private Long id;

    private String titel;

    private String beschreibung;

    private Bewertung bewertung;

    private Set<Kategorie> kategorien;

    private Set<ZutatenZusammenstellung> zutatenZusammenstellung;

    private Foto foto;

    public Rezept() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getTitel() {
        return titel;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBewertung(Bewertung bewertung) {
        this.bewertung = bewertung;
    }

    public Bewertung getBewertung() {
        return bewertung;
    }

    public void setZutatenZusammenstellung(Set<ZutatenZusammenstellung> zutatenZusammenstellung) {
        this.zutatenZusammenstellung = zutatenZusammenstellung;
    }

    public Set<ZutatenZusammenstellung> getZutatenZusammenstellung() {
        return zutatenZusammenstellung;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public Foto getFoto() {
        return foto;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Rezept)) {
            return false;
        }
        final Rezept other = (Rezept) object;
        if (!(titel == null ? other.titel == null : titel.equals(other.titel))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((titel == null) ? 0 : titel.hashCode());
        return result;
    }

    public void setKategorien(Set<Kategorie> kategorien) {
        this.kategorien = kategorien;
    }

    public Set<Kategorie> getKategorien() {
        return kategorien;
    }

    public void addKategorie(final Kategorie kategorie) {
        if (getKategorien() == null) {
            setKategorien(new HashSet<Kategorie>());
        }
        getKategorien().add(kategorie);
    }

    public void addZutatenZusammenstellung(final ZutatenZusammenstellung zutatenzusammenstellung) {
        if (getZutatenZusammenstellung() == null) {
            setZutatenZusammenstellung(new HashSet<ZutatenZusammenstellung>());
        }
        getZutatenZusammenstellung().add(zutatenzusammenstellung);
    }
}
