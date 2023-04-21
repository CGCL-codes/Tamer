package org.fudaa.dodico.crue.io.common;

import java.util.List;
import org.fudaa.dodico.crue.config.CrueConfigMetier;
import org.fudaa.dodico.crue.config.PropertyDefinition;
import org.fudaa.dodico.crue.io.neuf.STOSequentialReader;
import org.fudaa.dodico.crue.metier.emh.Calc;
import org.fudaa.dodico.crue.metier.emh.CatEMHBranche;
import org.fudaa.dodico.crue.metier.emh.CatEMHCasier;
import org.fudaa.dodico.crue.metier.emh.CatEMHNoeud;
import org.fudaa.dodico.crue.metier.emh.CatEMHSection;
import org.fudaa.dodico.crue.metier.emh.DonCLimMScenario;
import org.fudaa.dodico.crue.metier.emh.DonFrt;
import org.fudaa.dodico.crue.metier.emh.DonFrtConteneur;
import org.fudaa.dodico.crue.metier.emh.DonLoiHYConteneur;
import org.fudaa.dodico.crue.metier.emh.DonPrtGeoBatiCasier;
import org.fudaa.dodico.crue.metier.emh.DonPrtGeoProfilCasier;
import org.fudaa.dodico.crue.metier.emh.DonPrtGeoProfilSection;
import org.fudaa.dodico.crue.metier.emh.EMH;
import org.fudaa.dodico.crue.metier.emh.EMHBrancheSaintVenant;
import org.fudaa.dodico.crue.metier.emh.EMHModeleBase;
import org.fudaa.dodico.crue.metier.emh.EMHNoeudNiveauContinu;
import org.fudaa.dodico.crue.metier.emh.EMHScenario;
import org.fudaa.dodico.crue.metier.emh.EMHSousModele;
import org.fudaa.dodico.crue.metier.emh.Loi;
import org.fudaa.dodico.crue.metier.emh.OrdCalcScenario;
import org.fudaa.dodico.crue.metier.emh.OrdPrtCIniModeleBase;
import org.fudaa.dodico.crue.metier.emh.OrdPrtGeoModeleBase;
import org.fudaa.dodico.crue.metier.emh.OrdPrtReseau;
import org.fudaa.dodico.crue.metier.emh.OrdResScenario;
import org.fudaa.dodico.crue.metier.emh.ParamCalcScenario;
import org.fudaa.dodico.crue.metier.emh.ParamNumModeleBase;

/**
 * @author deniger
 */
public interface CrueData {

    /**
   * Ajoute automatiquement l'objet emh dans la bonne structure.
   * 
   * @param object
   */
    void add(final EMH object);

    public EMHNoeudNiveauContinu createNode(String id);

    /**
   * @param nom le nom de la branche cherchee
   * @return la branche correspondante ou null si non trouvee
   */
    CatEMHBranche findBrancheByReference(String nom);

    public Calc findCalcByNom(String nom);

    /**
   * @param nom le nom du casier cherchee
   * @return le casier correspondant ou null si non trouve
   */
    CatEMHCasier findCasierByReference(String nom);

    /**
   * @param nom le nom de l'EMH a cherche
   * @return l'EMH portant le nom donnée ou null si non trouve.
   */
    EMH findEMHByReference(String ref);

    /**
   * @param nom le nom du noeud cherche
   * @return le noeuc correspondant ou null si non trouve
   */
    CatEMHNoeud findNoeudByReference(String nom);

    /**
   * @param nom le nom de la section cherchee
   * @return la section correspondante ou null si non trouve
   */
    CatEMHSection findSectionByReference(String nom);

    /**
   * Retourne tous les objets EMH
   * 
   * @return la liste des emh.
   */
    List<EMH> getAllSimpleEMH();

    List<CatEMHBranche> getBranches();

    /**
   * @return les branches de saint-venant
   */
    List<EMHBrancheSaintVenant> getBranchesSaintVenant();

    List<CatEMHCasier> getCasiers();

    DonCLimMScenario getConditionsLim();

    public CrueConfigMetier getCrueConfigMetier();

    DonFrtConteneur getFrottements();

    DonLoiHYConteneur getLoiConteneur();

    /**
   * @return the lois
   */
    List<Loi> getLois();

    EMHModeleBase getModele();

    List<CatEMHNoeud> getNoeuds();

    /**
   * @return the ordCalc
   */
    OrdCalcScenario getOCAL();

    OrdPrtGeoModeleBase getOPTG();

    void getOPTG(OrdPrtGeoModeleBase optg);

    /**
   * @return setter sur les methodes d'interpolation OPTI
   */
    public OrdPrtReseau getOPTR();

    /**
   * @return setter sur les methodes d'interpolation OPTI
   */
    public OrdPrtCIniModeleBase getOPTI();

    public void setOPTI(OrdPrtCIniModeleBase opti);

    /**
   * @return the ordRes
   */
    OrdResScenario getORES();

    /**
   * @param lois the lois to set
   */
    ParamCalcScenario getPCAL();

    ParamNumModeleBase getPNUM();

    public PropertyDefinition getProperty(String id);

    EMHScenario getScenarioData();

    List<CatEMHSection> getSections();

    /**
   * @return le sous-modele
   */
    EMHSousModele getSousModele();

    /**
   * @return the sto
   */
    public STOSequentialReader getSto();

    /**
   * A utiliser lors de la lecture uniquement.
   * 
   * @param id l'identifiant du profil
   * @return le DonPrtGeoProfilSection utilise
   */
    public DonPrtGeoBatiCasier getUsedBatiCasier(final String id);

    /**
   * A utiliser lors de la lecture uniquement.
   * 
   * @param id l'identifiant du profil
   * @return le DonPrtGeoProfilSection utilise
   */
    public DonPrtGeoProfilCasier getUsedProfilCasier(final String id);

    /**
   * A utiliser lors de la lecture uniquement.
   * 
   * @param id l'identifiant du profil
   * @return le DonPrtGeoProfilSection utilise
   */
    public DonPrtGeoProfilSection getUsedProfilSection(final String id);

    /**
   * boolean pour indiquer les fichiers crue 9 contiennent des cartes distmax. Dans ce cas, la lecture des fichiers de
   * resultats ne doivent pas se faire.
   * 
   * @return true si contient distmax
   */
    public boolean isCrue9ContientDistmax();

    /**
   * Ajoute des lectures successives de données crue data en une seule. Exemple, lecture de plusieurs fichiers DRSO.
   * 
   * @return
   */
    public boolean mergeWithAnotherCrueData(CrueData data);

    /**
   * @param batiCasier
   */
    public void registerDefinedBatiCasier(DonPrtGeoBatiCasier batiCasier);

    /**
   * @param casierProfil un casier définit dans le fichiert dptg en tant que bibliothèque
   */
    public void registerDefinedCasierProfil(DonPrtGeoProfilCasier casierProfil);

    /**
   * @param sectionProfil
   */
    public void registerDefinedSectionProfil(DonPrtGeoProfilSection sectionProfil);

    /**
   * Utiliser lors de la lecture: permet d'enregistrer les bati utilises par EMH.
   * 
   * @param emh
   * @param profilId
   */
    public void registerUseBatiCasier(final EMH emh, final String profilId);

    /**
   * Utiliser lors de la lecture: permet d'enregistrer les profils de casier utilises par EMH.
   * 
   * @param emh
   * @param profilId
   */
    public void registerUseCasierProfil(final EMH emh, final String profilId);

    /**
   * Utiliser lors de la lecture: permet d'enregistrer les profils utilises par EMH.
   * 
   * @param emh
   * @param profilId
   */
    public void registerUseSectionProfil(final EMH emh, final String profilId);

    void setConditionsLim(final DonCLimMScenario donCLimMScenario);

    /**
   * boolean pour indiquer les fichiers crue 9 contiennent des cartes distmax. Dans ce cas, la lecture des fichiers de
   * resultats ne doivent pas se faire.
   * 
   * @param crue9ContientDistmax true si contient distmax
   */
    public void setCrue9ContientDistmax(boolean crue9ContientDistmax);

    void setFrottements(final List<DonFrt> frottements);

    /**
   * @return getter sur les methodes d'interpolation OPTI
   */
    public void setMethodesInterpolation(OrdPrtCIniModeleBase methodesInterpolation);

    void setOPTR(OrdPrtReseau optr);

    /**
   * @param sto the sto to set
   */
    public void setSto(final STOSequentialReader sto);

    public void sort();

    OrdPrtGeoModeleBase getOrCreateOPTG();

    OrdPrtReseau getOrCreateOPTR();

    OrdCalcScenario getOrCreateOCAL();

    ParamCalcScenario getOrCreatePCAL();

    OrdPrtCIniModeleBase getOrCreateOPTI();

    OrdResScenario getOrCreateORES();

    ParamNumModeleBase getOrCreatePNUM();
}
