package org.fudaa.dodico.crue.io.etu;

import java.util.List;
import org.fudaa.dodico.crue.io.common.CrueVersionType;
import org.fudaa.dodico.crue.io.dao.AbstractCrueDao;
import org.fudaa.dodico.crue.io.etu.CrueDaoStructureETU.Modele;
import org.fudaa.dodico.crue.io.etu.CrueDaoStructureETU.Repertoire;
import org.fudaa.dodico.crue.io.etu.CrueDaoStructureETU.Scenario;
import org.fudaa.dodico.crue.io.etu.CrueDaoStructureETU.ScenarioCourant;
import org.fudaa.dodico.crue.io.etu.CrueDaoStructureETU.SousModele;
import org.fudaa.dodico.crue.io.etu.CrueDaoStructureETU.TypeFichierDispo;

/**
 * Persistence du fichier XML ETU, le seul pour les gouverner tous.
 * 
 * @author Adrien Hadoux
 */
public class CrueDaoETU extends AbstractCrueDao implements CrueEtuInfosContainer {

    protected String AuteurCreation;

    protected String DateCreation;

    protected String AuteurDerniereModif;

    protected String DateDerniereModif;

    protected ScenarioCourant ScenarioCourant;

    protected List<Repertoire> Repertoires;

    protected List<TypeFichierDispo> FichEtudes;

    protected List<SousModele> SousModeles;

    protected List<Modele> Modeles;

    protected String Rapports;

    protected List<Scenario> Scenarios;

    @Override
    public String getAuteurDerniereModif() {
        return AuteurDerniereModif;
    }

    @Override
    public String getDateCreation() {
        return DateCreation;
    }

    @Override
    public String getDateDerniereModif() {
        return DateDerniereModif;
    }

    @Override
    public String getAuteurCreation() {
        return AuteurCreation;
    }

    @Override
    public String getType() {
        return null;
    }

    public void setAuteurCreation(String auteurCreation) {
        AuteurCreation = auteurCreation;
    }

    public void setDateCreation(String dateCreation) {
        DateCreation = dateCreation;
    }

    public void setAuteurDerniereModif(String auteurDerniereModif) {
        AuteurDerniereModif = auteurDerniereModif;
    }

    public void setDateDerniereModif(String dateDerniereModif) {
        DateDerniereModif = dateDerniereModif;
    }

    public void setCrueVersion(CrueVersionType version) {
    }
}
