package de.d3s.alricg.data.charElement;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import de.d3s.alricg.data.Datensammlung;
import de.d3s.alricg.data.DatensammlungJpa;
import de.d3s.alricg.data.charElement.CharElement;
import de.d3s.alricg.data.charElement.RegelVerweis;
import de.d3s.alricg.data.charElement.auswahl.Auswahl;
import de.d3s.alricg.data.charElement.auswahl.AuswahlJpa;
import de.d3s.alricg.data.charElement.auswahl.Voraussetzung;

/**
 * Implementierung des 'CharElement'-Interfaces für JPA.
 * 
 * @see CharElement
 * @author V. Strelow
 */
@Entity
public abstract class CharElementJpa extends DsaElementJpa implements CharElement {

    private String name;

    private String beschreibung;

    private String sammelbegriff;

    private boolean waehlbar;

    @ManyToOne(targetEntity = DatensammlungJpa.class)
    private Datensammlung datensammlung;

    @OneToMany(targetEntity = RegelVerweisJpa.class)
    private List<RegelVerweis> regeln;

    @OneToMany(targetEntity = AuswahlJpa.class, mappedBy = "quelle")
    private List<Voraussetzung> positivVoraussetzungen;

    @OneToMany(targetEntity = AuswahlJpa.class, mappedBy = "quelle")
    private List<Voraussetzung> negativVoraussetzungen;

    /**
	 * @see de.d3s.alricg.data.charElement.link.Voraussetzung#getPositivVoraussetzungen()
	 */
    @Override
    public List<Voraussetzung> getPositivVoraussetzungen() {
        return positivVoraussetzungen;
    }

    /**
	 * @see de.d3s.alricg.data.charElement.link.Voraussetzung#getNegativVoraussetzungen()
	 */
    @Override
    public List<Voraussetzung> getNegativVoraussetzungen() {
        return negativVoraussetzungen;
    }

    /**
	 * @see de.d3s.alricg.data.charElement.link.Voraussetzung#getName()
	 */
    @Override
    public String getName() {
        return name;
    }

    /**
	 * @see de.d3s.alricg.data.charElement.link.Voraussetzung#getBeschreibung()
	 */
    @Override
    public String getBeschreibung() {
        return beschreibung;
    }

    /**
	 * @see de.d3s.alricg.data.charElement.link.Voraussetzung#getSammelbegriff()
	 */
    @Override
    public String getSammelbegriff() {
        return sammelbegriff;
    }

    /**
	 * @see de.d3s.alricg.data.charElement.link.Voraussetzung#isWaehlbar()
	 */
    @Override
    public boolean isWaehlbar() {
        return waehlbar;
    }

    /**
	 * @see de.d3s.alricg.data.charElement.link.Voraussetzung#getDatensatz()
	 */
    @Override
    public Datensammlung getDatensammlung() {
        return datensammlung;
    }

    /**
	 * @see de.d3s.alricg.data.charElement.link.Voraussetzung#getRegeln()
	 */
    @Override
    public List<RegelVerweis> getRegeln() {
        return regeln;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public void setSammelbegriff(String sammelbegriff) {
        this.sammelbegriff = sammelbegriff;
    }

    @Override
    public void setWaehlbar(boolean waehlbar) {
        this.waehlbar = waehlbar;
    }

    @Override
    public void setDatensammlung(Datensammlung datensammlung) {
        this.datensammlung = datensammlung;
    }

    @Override
    @Transient
    public void addRegel(RegelVerweis regel) {
        if (this.regeln == null) this.regeln = new ArrayList<RegelVerweis>();
        this.regeln.add(regel);
    }

    @Override
    @Transient
    public void removeRegel(RegelVerweis regel) {
        if (this.regeln == null) return;
        this.regeln.remove(regel);
    }

    @Override
    @Transient
    public void addPositivVoraussetzung(Voraussetzung positivVoraussetzung) {
        if (this.positivVoraussetzungen == null) this.positivVoraussetzungen = new ArrayList<Voraussetzung>();
        this.positivVoraussetzungen.add(positivVoraussetzung);
    }

    @Override
    @Transient
    public void removePositivVoraussetzung(Voraussetzung positivVoraussetzung) {
        if (this.positivVoraussetzungen == null) return;
        this.positivVoraussetzungen.remove(positivVoraussetzung);
    }

    @Override
    @Transient
    public void addNegativVoraussetzung(Voraussetzung negativVoraussetzung) {
        if (this.negativVoraussetzungen == null) this.negativVoraussetzungen = new ArrayList<Voraussetzung>();
        this.negativVoraussetzungen.add(negativVoraussetzung);
    }

    @Override
    @Transient
    public void removeNegativVoraussetzung(Voraussetzung negativVoraussetzung) {
        if (this.negativVoraussetzungen == null) return;
        this.negativVoraussetzungen.remove(negativVoraussetzung);
    }
}
