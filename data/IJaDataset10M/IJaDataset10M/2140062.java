package de.d3s.alricg.data.charElement.auswahl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import de.d3s.alricg.data.charElement.CharElement;
import de.d3s.alricg.data.charElement.CharElementJpa;
import de.d3s.alricg.data.charElement.DsaElementJpa;
import de.d3s.alricg.data.charElement.auswahl.Auswahl;
import de.d3s.alricg.data.charElement.link.Link;
import de.d3s.alricg.data.charElement.link.LinkJpa;

/**
 * Implementierung des 'Auswahl'-Interfaces für JPA.
 * 
 * @see Auswahl
 * @author V. Strelow
 */
@Entity
public class AuswahlJpa extends DsaElementJpa implements Auswahl {

    @OneToOne(targetEntity = CharElementJpa.class)
    private CharElement quelle;

    @OneToMany(targetEntity = LinkJpa.class, mappedBy = "quelle")
    private List<Link> links;

    private String anmerkungFuerUser;

    /** 
	 * @see de.d3s.alricg.data.charElement.auswahl.Auswahl#getQuelle()
	 */
    @Override
    public CharElement getQuelle() {
        return quelle;
    }

    /**
	 * @see de.d3s.alricg.data.charElement.auswahl.Auswahl#getAnmerkungFuerUser()
	 */
    @Override
    public String getAnmerkungFuerUser() {
        return anmerkungFuerUser;
    }

    /**
	 * @see de.d3s.alricg.data.charElement.auswahl.Auswahl#getLinks()
	 */
    @Override
    public List<Link> getLinks() {
        return links;
    }

    /**
	 * @see de.d3s.alricg.data.charElement.auswahl.Auswahl#getName()
	 */
    @Override
    public String getName() {
        return null;
    }

    /**
	 * @see de.d3s.alricg.data.charElement.auswahl.Auswahl#getBeschreibung()
	 */
    @Override
    public String getBeschreibung() {
        return null;
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public void setBeschreibung(String beschreibung) {
    }

    @Override
    public void setQuelle(CharElement quelle) {
        this.quelle = quelle;
    }

    @Override
    public void addLink(Link links) {
        if (this.links == null) this.links = new ArrayList<Link>();
        this.links.add(links);
    }

    @Override
    public void removeLink(Link link) {
        if (this.links == null) return;
        this.links.remove(link);
    }

    @Override
    public void setAnmerkungFuerUser(String str) {
        this.anmerkungFuerUser = str;
    }

    @Override
    public String getUserText() {
        return null;
    }
}
