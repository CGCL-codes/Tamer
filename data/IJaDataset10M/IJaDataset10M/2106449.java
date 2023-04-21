package org.pocui.example.composites.stammdaten;

import java.util.List;
import org.pocui.core.selections.ISelection;
import org.pocui.example.components.Kunde;

/**
 * Selection for {@link VisualisiereStammdatenComposite}
 * 
 * @author Kai Benjamin Joneleit
 * @author Jan Christian Krause
 * 
 */
public class VisualisiereStammdatenSelectionInOut implements ISelection {

    private Long vertragsbeginn;

    private Long vertragsende;

    private String selektiertesProdukt;

    private List<String> produkte;

    private String strasse;

    private String hausnummer;

    private Integer postleitzahl;

    private String wohnort;

    private String selektiertesBundesland;

    private List<String> bundeslaender;

    private String zusatzInfos;

    private Long kundennummer;

    private String kundenname;

    private Kunde selektierterKunde;

    private List<Kunde> kunden;

    public Long getVertragsbeginn() {
        return vertragsbeginn;
    }

    public void setVertragsbeginn(Long vertragsbeginn) {
        this.vertragsbeginn = vertragsbeginn;
    }

    public Long getVertragsende() {
        return vertragsende;
    }

    public void setVertragsende(Long vertragsende) {
        this.vertragsende = vertragsende;
    }

    public String getSelektiertesProdukt() {
        return selektiertesProdukt;
    }

    public void setSelektiertesProdukt(String selektiertesProdukt) {
        this.selektiertesProdukt = selektiertesProdukt;
    }

    public List<String> getProdukte() {
        return produkte;
    }

    public void setProdukte(List<String> produkte) {
        this.produkte = produkte;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public Integer getPostleitzahl() {
        return postleitzahl;
    }

    public void setPostleitzahl(Integer postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public String getWohnort() {
        return wohnort;
    }

    public void setWohnort(String wohnort) {
        this.wohnort = wohnort;
    }

    public String getSelektiertesBundesland() {
        return selektiertesBundesland;
    }

    public void setSelektiertesBundesland(String selektiertesBundesland) {
        this.selektiertesBundesland = selektiertesBundesland;
    }

    public List<String> getBundeslaender() {
        return bundeslaender;
    }

    public void setBundeslaender(List<String> bundeslaender) {
        this.bundeslaender = bundeslaender;
    }

    public String getZusatzInfos() {
        return zusatzInfos;
    }

    public void setZusatzInfos(String zusatzInfos) {
        this.zusatzInfos = zusatzInfos;
    }

    public Long getKundennummer() {
        return kundennummer;
    }

    public void setKundennummer(Long kundennummer) {
        this.kundennummer = kundennummer;
    }

    public String getKundenname() {
        return kundenname;
    }

    public void setKundenname(String kundenname) {
        this.kundenname = kundenname;
    }

    public Kunde getSelektierterKunde() {
        return selektierterKunde;
    }

    public void setSelektierterKunde(Kunde selektierterKunde) {
        this.selektierterKunde = selektierterKunde;
    }

    public List<Kunde> getKunden() {
        return kunden;
    }

    public void setKunden(List<Kunde> kunden) {
        this.kunden = kunden;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bundeslaender == null) ? 0 : bundeslaender.hashCode());
        result = prime * result + ((hausnummer == null) ? 0 : hausnummer.hashCode());
        result = prime * result + ((kunden == null) ? 0 : kunden.hashCode());
        result = prime * result + ((kundenname == null) ? 0 : kundenname.hashCode());
        result = prime * result + ((kundennummer == null) ? 0 : kundennummer.hashCode());
        result = prime * result + ((postleitzahl == null) ? 0 : postleitzahl.hashCode());
        result = prime * result + ((produkte == null) ? 0 : produkte.hashCode());
        result = prime * result + ((selektierterKunde == null) ? 0 : selektierterKunde.hashCode());
        result = prime * result + ((selektiertesBundesland == null) ? 0 : selektiertesBundesland.hashCode());
        result = prime * result + ((selektiertesProdukt == null) ? 0 : selektiertesProdukt.hashCode());
        result = prime * result + ((strasse == null) ? 0 : strasse.hashCode());
        result = prime * result + ((vertragsbeginn == null) ? 0 : vertragsbeginn.hashCode());
        result = prime * result + ((vertragsende == null) ? 0 : vertragsende.hashCode());
        result = prime * result + ((wohnort == null) ? 0 : wohnort.hashCode());
        result = prime * result + ((zusatzInfos == null) ? 0 : zusatzInfos.hashCode());
        return result;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        VisualisiereStammdatenSelectionInOut other = (VisualisiereStammdatenSelectionInOut) obj;
        if (bundeslaender == null) {
            if (other.bundeslaender != null) return false;
        } else if (!bundeslaender.equals(other.bundeslaender)) return false;
        if (hausnummer == null) {
            if (other.hausnummer != null) return false;
        } else if (!hausnummer.equals(other.hausnummer)) return false;
        if (kunden == null) {
            if (other.kunden != null) return false;
        } else if (!kunden.equals(other.kunden)) return false;
        if (kundenname == null) {
            if (other.kundenname != null) return false;
        } else if (!kundenname.equals(other.kundenname)) return false;
        if (kundennummer == null) {
            if (other.kundennummer != null) return false;
        } else if (!kundennummer.equals(other.kundennummer)) return false;
        if (postleitzahl == null) {
            if (other.postleitzahl != null) return false;
        } else if (!postleitzahl.equals(other.postleitzahl)) return false;
        if (produkte == null) {
            if (other.produkte != null) return false;
        } else if (!produkte.equals(other.produkte)) return false;
        if (selektierterKunde == null) {
            if (other.selektierterKunde != null) return false;
        } else if (!selektierterKunde.equals(other.selektierterKunde)) return false;
        if (selektiertesBundesland == null) {
            if (other.selektiertesBundesland != null) return false;
        } else if (!selektiertesBundesland.equals(other.selektiertesBundesland)) return false;
        if (selektiertesProdukt == null) {
            if (other.selektiertesProdukt != null) return false;
        } else if (!selektiertesProdukt.equals(other.selektiertesProdukt)) return false;
        if (strasse == null) {
            if (other.strasse != null) return false;
        } else if (!strasse.equals(other.strasse)) return false;
        if (vertragsbeginn == null) {
            if (other.vertragsbeginn != null) return false;
        } else if (!vertragsbeginn.equals(other.vertragsbeginn)) return false;
        if (vertragsende == null) {
            if (other.vertragsende != null) return false;
        } else if (!vertragsende.equals(other.vertragsende)) return false;
        if (wohnort == null) {
            if (other.wohnort != null) return false;
        } else if (!wohnort.equals(other.wohnort)) return false;
        if (zusatzInfos == null) {
            if (other.zusatzInfos != null) return false;
        } else if (!zusatzInfos.equals(other.zusatzInfos)) return false;
        return true;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName());
        buffer.append(" [Vertragsbeginn: ");
        buffer.append(vertragsbeginn);
        buffer.append(", Vertragsende: ");
        buffer.append(vertragsende);
        buffer.append(", selektiertes Produkt: ");
        buffer.append(selektiertesProdukt);
        buffer.append(", Produkte: ");
        buffer.append(produkte);
        buffer.append(", Straße: ");
        buffer.append(strasse);
        buffer.append(", Hausnummer: ");
        buffer.append(hausnummer);
        buffer.append(", PLZ: ");
        buffer.append(postleitzahl);
        buffer.append(", Wohnort: ");
        buffer.append(wohnort);
        buffer.append(", selektiertes Bundesland: ");
        buffer.append(selektiertesBundesland);
        buffer.append(", Bundesländer: ");
        buffer.append(bundeslaender);
        buffer.append(", Zusatzinfos: ");
        buffer.append(zusatzInfos);
        buffer.append(", Kunden-Nr.: ");
        buffer.append(kundennummer);
        buffer.append(", Kundenname: ");
        buffer.append(kundenname);
        buffer.append(", Kunde: ");
        buffer.append(selektierterKunde);
        buffer.append(", Kunden: ");
        buffer.append(kunden);
        buffer.append("]");
        return buffer.toString();
    }
}
