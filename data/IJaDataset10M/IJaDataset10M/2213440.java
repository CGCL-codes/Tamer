package de.d3s.alricg.data;

import java.util.List;
import de.d3s.alricg.data.charElement.RegelVerweis;
import de.d3s.alricg.data.charElement.auswahl.Voraussetzung;
import de.d3s.alricg.data.charElement.basic.Eigenschaft;
import de.d3s.alricg.data.charElement.link.Link;
import de.d3s.alricg.data.charElement.link.LinkAuszeichnung;
import de.d3s.alricg.data.charakter.Charakter;
import de.d3s.alricg.data.charakter.CharakterAllgemeineDaten;
import de.d3s.alricg.data.user.User;

/**
 * @author Vincent
 *
 */
public class CharakterFacade implements Charakter {

    private Charakter charakterData;

    protected CharakterFacade(Charakter charakterData) {
        this.charakterData = charakterData;
    }

    @Override
    public List<RegelVerweis> getAllRegeln() {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getBeschreibung() {
        return null;
    }

    @Override
    public User getOwningUser() {
        return null;
    }

    @Override
    public CharakterAllgemeineDaten getAllgemeineDaten() {
        return null;
    }

    @Override
    public List<List<Voraussetzung>> getPositivVoraussetzungen() {
        return null;
    }

    @Override
    public List<List<Voraussetzung>> getNegativVoraussetzungen() {
        return null;
    }

    @Override
    public List<Link> getDsaElementList(Class<? extends DsaElement> clazz) {
        return null;
    }

    @Override
    public int getEigenschaftWert(Eigenschaft eigenschaft) {
        return 0;
    }

    @Override
    public Link getEigenschaft(Eigenschaft eigenschaft) {
        return null;
    }

    @Override
    public boolean hasElementLink(DsaElement gesuchtElement) {
        return false;
    }

    @Override
    public boolean hasElementLink(Link gesuchtLink) {
        return false;
    }

    @Override
    public boolean hasElementLink(DsaElement gesuchtElement, List<LinkAuszeichnung> gesuchtLA) {
        return false;
    }

    @Override
    public void addElementLink(Link link) {
    }

    @Override
    public void updateElementLink(Link link) {
    }

    @Override
    public void removeElementLink(Link link) {
    }

    @Override
    public void addElementLink(List<Link> links) {
    }

    @Override
    public void updateElementLink(List<Link> links) {
    }

    @Override
    public void removeElementLink(List<Link> links) {
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public void setBeschreibung(String beschreibung) {
    }

    @Override
    public Link getElementLink(Link gesuchtLink) {
        return null;
    }

    @Override
    public Link getElementLink(DsaElement gesuchtElement, List<LinkAuszeichnung> gesuchtLA) {
        return null;
    }

    @Override
    public List<Link> getElementLink(DsaElement element) {
        return null;
    }
}
