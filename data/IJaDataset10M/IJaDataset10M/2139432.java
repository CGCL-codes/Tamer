package org.fudaa.dodico.refonde;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.fudaa.dodico.corba.refonde.IParametresRefonde;
import org.fudaa.dodico.corba.refonde.IParametresRefondeINP;
import org.fudaa.dodico.corba.refonde.IParametresRefondeOperations;
import org.fudaa.dodico.corba.refonde.SParametresRefondeDomainePoreux;
import org.fudaa.dodico.corba.refonde.SParametresRefondeINP;
import org.fudaa.dodico.corba.refonde.SParametresRefondeINPBase;
import org.fudaa.dodico.corba.refonde.SParametresRefondeSPE;
import org.fudaa.dodico.calcul.DParametres;
import org.fudaa.dodico.fortran.FortranWriter;
import org.fudaa.dodico.objet.CDodico;

/**
 * Les parametres Refonde.
 *
 * @version      $Revision: 1.15 $ $Date: 2006-09-19 14:45:59 $ by $Author: deniger $
 * @author       Philippe Sergent , Guillaume Desnoix, Bertrand Marchand
 */
public class DParametresRefonde extends DParametres implements IParametresRefonde, IParametresRefondeOperations {

    public void clearRefondeNewVersion() {
        parametresI_ = null;
    }

    /**
   * A voir si utile.
   */
    IParametresRefondeINP parametresI_;

    public IParametresRefondeINP parametresINPNewVersion() {
        return parametresI_;
    }

    public boolean isNewVersion() {
        return parametresI_ != null;
    }

    public void parametresINPNewVersion(final IParametresRefondeINP _newParametresINPNewVersion) {
        parametresI_ = _newParametresINPNewVersion;
    }

    /** Type calcul : Houle r�guli�re. */
    public static final int HOULE_REG = 0;

    /** Type calcul : Houle al�atoire. */
    public static final int HOULE_ALEA = 1;

    /** Type calcul : Houle al�atoire avec reprise des r�sultats houle reguli�re. */
    public static final int HOULE_ALEA_REPRISE = 2;

    /** Type calcul : Calcul de seiches. */
    public static final int MODULE_SEICHE = 3;

    private int typeCalcul_;

    private String racine_;

    private SParametresRefondeINP paramsINP_;

    private SParametresRefondeSPE paramsSPE_;

    public DParametresRefonde() {
        super();
        typeCalcul_ = HOULE_REG;
    }

    public Object clone() {
        return new DParametresRefonde();
    }

    /**
   * D�finit le type de calcul � effectuer.
   *
   * @param _type Type du calcul.
   * @see #HOULE_REG
   * @see #HOULE_ALEA
   * @see #HOULE_ALEA_REPRISE
   * @see #MODULE_SEICHE
   */
    public void typeCalcul(final int _type) {
        typeCalcul_ = _type;
    }

    /**
   * Retourne le type du calcul.
   * @return Le type.
   */
    public int typeCalcul() {
        return typeCalcul_;
    }

    /**
   * D�finit la racine des fichiers de calcul. La racine est le nom sans
   * le chemin ni l'extension d'un fichier.
   *
   * @param _rac Racine.
   */
    public void racine(final String _rac) {
        racine_ = _rac;
    }

    /**
   * Retourne la racine des fichiers de calcul.
   *
   * @return Racine.
   */
    public String racine() {
        return racine_;
    }

    /**
   * D�finit les param�tres du fichier <code>.spe</code> du spectre incident.
   *
   * @param _params Parametres.
   */
    public void parametresSPE(final SParametresRefondeSPE _params) {
        paramsSPE_ = _params;
    }

    /**
   * Retourne les param�tres du fichier <code>.spe</code> du spectre incident.
   *
   * @return Parametres.
   */
    public SParametresRefondeSPE parametresSPE() {
        return paramsSPE_;
    }

    public SParametresRefondeINP parametresINP() {
        return paramsINP_;
    }

    public void parametresINP(final SParametresRefondeINP _p) {
        paramsINP_ = _p;
    }

    /**
   * Ecriture des param�tres sur fichiers. En fonction du type de calcul, les
   * fichiers .INP, .SPE sont �crits ou pas.
   *
   * @param _fichiers Nom des fichiers.
   * @param _tpCalcul Type du calcul (HOULE_REG, HOULE_ALEA, HOULE_ALEA_REPRISE).
   * @param _params Parametres � �crire.
   */
    public static void ecritSurFichiers(final String _fichiers, final int _tpCalcul, final IParametresRefonde _params) {
        if (_params.isNewVersion()) {
            _params.parametresINP(createInpForInterface(_params.parametresINPNewVersion()));
            _params.clearRefondeNewVersion();
        }
        if (_tpCalcul != HOULE_ALEA_REPRISE) {
            ecritParametresRefondeINP(_fichiers, _tpCalcul, _params.parametresINP());
        }
        if (_tpCalcul == HOULE_ALEA || _tpCalcul == HOULE_ALEA_REPRISE) {
            ecritParametresRefondeSPE(_fichiers, _params.parametresSPE());
        }
    }

    public static SParametresRefondeINP createInpForInterface(final IParametresRefondeINP _s) {
        System.err.println("changement structure");
        final SParametresRefondeINP r = new SParametresRefondeINP();
        final SParametresRefondeINPBase base = _s.donneesBase();
        r.nbNoeuds = base.nbNoeuds;
        r.nbElements = base.nbElements;
        r.nbMaxNoeudsElement = base.nbMaxNoeudsElement;
        r.nbGroupesPE = base.nbGroupesPE;
        r.nbMaxPEGroupe = base.nbMaxPEGroupe;
        r.hauteurHoule = base.hauteurHoule;
        r.angleHoule = base.angleHoule;
        r.fondsPoreux = base.fondsPoreux;
        r.periodeHoule = base.periodeHoule;
        r.penteFonds = base.penteFonds;
        r.profondeurOrigine = base.profondeurOrigine;
        r.hauteurMer = base.hauteurMer;
        r.nbIterationAngleFrontiere = base.nbIterationAngleFrontiere;
        r.casBordOuvert = base.casBordOuvert;
        r.ordreMax = base.ordreMax;
        r.deferlement = base.deferlement;
        r.formuleDeferlement = base.formuleDeferlement;
        r.nbAnglesHoule = base.nbAnglesHoule;
        r.angleHouleMin = base.angleHouleMin;
        r.angleHouleMax = base.angleHouleMax;
        r.nbPeriodesHoule = base.nbPeriodesHoule;
        r.periodeHouleMin = base.periodeHouleMin;
        r.periodeHouleMax = base.periodeHouleMax;
        r.nbNoeudsSuperElement = base.nbNoeudsSuperElement;
        r.xyz = _s.xyz();
        r.connectivites = _s.connectivites();
        r.type = _s.type();
        r.groupePE = _s.groupePE();
        r.valeurPE = _s.valeurPE();
        r.domainePoreux = _s.domainePoreux();
        r.seiche = _s.seiche();
        return r;
    }

    /**
   * Suppression des fichiers de donn�es de nom sp�cifi�.
   *
   * @param _fichiers Nom des fichiers du projet sans l'extension.
   */
    static void clearFichiers(final String _fichiers) {
        new File(_fichiers + ".inp").delete();
        new File(_fichiers + ".spe").delete();
        new File(new File(_fichiers).getParent(), "refonde1.5").delete();
        new File(new File(_fichiers).getParent(), "refonde2.5").delete();
    }

    /**
   * Ecriture des param�tres sur fichier .inp.
   *
   * @param _fichier Nom du fichier .inp.
   * @param _typeCalcul Type du calcul (HOULE_REG, HOULE_ALEA,
   *                    HOULE_ALEA_REPRISE, MODULE_SEICHE).
   * @param _params Parametres � �crire.
   */
    private static void ecritParametresRefondeINP(final String _fichier, final int _typeCalcul, final SParametresRefondeINP _params) {
        System.out.println("Ecriture de " + _fichier + ".inp");
        try {
            final FortranWriter file = new FortranWriter(new FileWriter(_fichier + ".inp"));
            file.setStringQuoted(false);
            int[] fmt;
            int reste;
            int nbLignes;
            int i, j;
            int idefer;
            boolean hReg;
            nbLignes = _params.nbElements / 16;
            reste = _params.nbElements - 16 * nbLignes;
            hReg = _typeCalcul == HOULE_REG;
            String log;
            if (_typeCalcul == MODULE_SEICHE) {
                log = "SEICHE 1.0";
            } else {
                log = "REFONDE 2.1";
            }
            file.stringField(0, "COMT");
            file.writeFields();
            file.stringField(0, "* ======================================" + "============================= G.H.N. ===");
            file.writeFields();
            file.stringField(0, "* Description : Fichier d'entree de " + log);
            file.writeFields();
            file.stringField(0, "* Cree par    : RefondeServeur");
            file.writeFields();
            file.stringField(0, "* ======================================" + "========================================");
            file.writeFields();
            file.stringField(0, " ");
            file.writeFields();
            if (_typeCalcul != MODULE_SEICHE) {
                fmt = new int[] { 4 };
                file.stringField(0, "SPEC");
                file.writeFields(fmt);
                fmt = new int[] { 6 };
                idefer = _params.deferlement;
                if (idefer == 2) {
                    idefer += _params.formuleDeferlement;
                }
                if (idefer == 1 && !hReg) {
                    idefer = 6;
                }
                file.intField(0, idefer);
                file.writeFields(fmt);
                fmt = new int[] { 6 };
                file.intField(0, hReg ? 1 : _params.nbPeriodesHoule);
                file.writeFields(fmt);
                fmt = new int[] { 6 };
                file.doubleField(0, hReg ? _params.periodeHoule : _params.periodeHouleMin);
                file.writeFields(fmt);
                if (!hReg && _params.nbPeriodesHoule > 1) {
                    fmt = new int[] { 6 };
                    file.doubleField(0, _params.periodeHouleMax);
                    file.writeFields(fmt);
                }
                fmt = new int[] { 6 };
                file.intField(0, hReg ? 1 : _params.nbAnglesHoule);
                file.writeFields(fmt);
                fmt = new int[] { 6 };
                file.doubleField(0, hReg ? _params.angleHoule : _params.angleHouleMin);
                file.writeFields(fmt);
                if (!hReg && _params.nbAnglesHoule > 1) {
                    fmt = new int[] { 6 };
                    file.doubleField(0, _params.angleHouleMax);
                    file.writeFields(fmt);
                }
                file.stringField(0, " ");
                file.writeFields();
            }
            fmt = new int[] { 4 };
            file.stringField(0, "DATA");
            file.writeFields(fmt);
            fmt = new int[] { 5, 5, 5, 10, 5 };
            file.stringField(0, "");
            file.stringField(1, "COOR");
            file.intField(2, 2);
            file.intField(3, _params.nbNoeuds);
            file.intField(4, 1);
            file.writeFields(fmt);
            if (_typeCalcul != MODULE_SEICHE) {
                fmt = new int[] { 10, 10, 10 };
                file.doubleField(0, 1.);
                file.doubleField(1, 1.);
                file.doubleField(2, 1.);
                file.writeFields(fmt);
            }
            fmt = new int[] { 10, 10, 10 };
            for (i = 0; i < _params.nbNoeuds; i++) {
                file.doubleField(0, _params.xyz[i].x);
                file.doubleField(1, _params.xyz[i].y);
                file.doubleField(2, _params.xyz[i].z);
                file.writeFields(fmt);
            }
            fmt = new int[] { 5, 5, 10, 5, 5, 5, 5, 5, 5, 5, 5 };
            if (_typeCalcul == MODULE_SEICHE) {
                fmt = new int[] { 5, 5, 10, 5, 5, 5, 5, 5 };
            }
            file.stringField(0, "");
            file.stringField(1, "ELEM");
            file.intField(2, _params.nbElements);
            file.intField(3, _params.nbMaxNoeudsElement);
            file.intField(4, _params.nbGroupesPE);
            file.intField(5, _params.nbMaxPEGroupe);
            file.stringField(6, "    4");
            file.stringField(7, "    4");
            if (_typeCalcul != MODULE_SEICHE) {
                file.intField(8, _params.ordreMax);
                file.intField(9, _params.casBordOuvert);
                file.intField(10, _params.nbNoeudsSuperElement);
            }
            file.writeFields(fmt);
            fmt = new int[] { 10, 10, 10, 10, 10, 10, 10, 10 };
            for (i = 0; i < _params.nbElements; i++) {
                if (i == _params.nbElements - 1 && _params.casBordOuvert == 2) {
                    for (j = 0; j < _params.connectivites[i].noeuds.length; j++) {
                        file.intField(0, _params.connectivites[i].noeuds[j]);
                        file.writeFields(fmt);
                    }
                } else {
                    for (j = 0; j < _params.connectivites[i].noeuds.length; j++) {
                        file.intField(j, _params.connectivites[i].noeuds[j]);
                    }
                    file.writeFields(fmt);
                }
            }
            fmt = new int[] { 5, 5 };
            file.stringField(0, "");
            file.stringField(1, "TYPE");
            file.writeFields(fmt);
            fmt = new int[] { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
            for (i = 0; i < nbLignes; i++) {
                for (j = 0; j < 16; j++) {
                    file.intField(j, _params.type[i * 16 + j]);
                }
                file.writeFields(fmt);
            }
            if (reste != 0) {
                for (j = 0; j < reste; j++) {
                    file.intField(j, _params.type[nbLignes * 16 + j]);
                }
                file.writeFields(fmt);
            }
            fmt = new int[] { 5, 5 };
            file.stringField(0, "");
            file.stringField(1, "PREL");
            file.writeFields(fmt);
            fmt = new int[] { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
            for (i = 0; i < nbLignes; i++) {
                for (j = 0; j < 16; j++) {
                    file.intField(j, _params.groupePE[i * 16 + j]);
                }
                file.writeFields(fmt);
            }
            if (reste != 0) {
                for (j = 0; j < reste; j++) {
                    file.intField(j, _params.groupePE[nbLignes * 16 + j]);
                }
                file.writeFields(fmt);
            }
            fmt = new int[] { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 };
            for (i = 0; i < _params.nbGroupesPE; i++) {
                for (j = 0; j < _params.valeurPE[i].pes.length; j++) {
                    file.doubleField(j, _params.valeurPE[i].pes[j]);
                }
                file.writeFields(fmt);
            }
            if (_typeCalcul == MODULE_SEICHE) {
                fmt = new int[] { 5, 5 };
                file.stringField(0, "");
                file.stringField(1, "COND");
                file.writeFields(fmt);
                fmt = new int[] { 10, 10 };
                file.stringField(0, "1");
                file.intField(1, 0);
                file.writeFields(fmt);
                fmt = new int[] { 10, 10, 10, 10, 10, 10, 10, 10 };
                final int nbl = (_params.seiche.noeudsBord.length) / 8;
                final int rst = (_params.seiche.noeudsBord.length) % 8;
                for (i = 0; i < nbl; i++) {
                    for (j = 0; j < 8; j++) {
                        file.intField(j, _params.seiche.noeudsBord[i * 8 + j]);
                    }
                    file.writeFields(fmt);
                }
                if (rst != 0) {
                    for (j = 0; j < rst; j++) {
                        file.intField(j, _params.seiche.noeudsBord[nbl * 8 + j]);
                    }
                    file.writeFields(fmt);
                }
                file.stringField(0, "");
                file.writeFields();
            }
            if (_typeCalcul == MODULE_SEICHE) {
                fmt = new int[] { 5, 5 };
                file.stringField(0, "");
                file.stringField(1, "SOLC");
                file.writeFields(fmt);
            }
            file.stringField(0, "");
            file.writeFields();
            if (_typeCalcul != MODULE_SEICHE && _params.fondsPoreux) {
                fmt = new int[] { 5 };
                file.stringField(0, "PORO");
                file.writeFields(fmt);
                final SParametresRefondeDomainePoreux[] dms = _params.domainePoreux;
                for (i = 0; i < dms.length; i++) {
                    for (j = 0; j < dms[i].noeud.length; j++) {
                        fmt = new int[] { 10 };
                        file.intField(0, dms[i].noeud[j]);
                        file.writeFields(fmt);
                        fmt = new int[] { 10, 10, 10, 10, 10 };
                        file.doubleField(0, dms[i].epaisseurNodale[j]);
                        file.doubleField(1, dms[i].porosite);
                        file.doubleField(2, dms[i].coefFrottement);
                        file.doubleField(3, dms[i].permeabilite);
                        file.doubleField(4, dms[i].coefMasseVirtuelle);
                        file.writeFields(fmt);
                    }
                }
                fmt = new int[] { 10 };
                file.intField(0, -1);
                file.writeFields(fmt);
                file.stringField(0, "");
                file.writeFields();
            }
            fmt = new int[] { 5 };
            file.stringField(0, "ONDE");
            file.writeFields(fmt);
            fmt = new int[] { 10, 10, 10, 10, 10, 10 };
            if (_typeCalcul == MODULE_SEICHE) {
                fmt = new int[] { 10, 10, 10, 10 };
            }
            file.doubleField(0, _params.periodeHoule);
            file.doubleField(1, _params.penteFonds);
            file.doubleField(2, _params.profondeurOrigine);
            file.doubleField(3, _params.hauteurMer);
            if (_typeCalcul != MODULE_SEICHE) {
                file.doubleField(4, _params.angleHoule);
                file.doubleField(5, _params.hauteurHoule);
            }
            file.writeFields(fmt);
            fmt = new int[] { 5 };
            file.stringField(0, "");
            file.writeFields(fmt);
            if (_typeCalcul == MODULE_SEICHE) {
                fmt = new int[] { 5 };
                file.stringField(0, "VALP");
                file.writeFields(fmt);
                fmt = new int[] { 5, 5 };
                file.intField(0, _params.seiche.nbValPropres);
                file.intField(1, _params.seiche.nbMaxIterations);
                file.writeFields(fmt);
                fmt = new int[] { 10, 10 };
                file.doubleField(0, _params.seiche.decalageValPropres);
                file.doubleField(1, _params.seiche.precisionConvergence);
                file.writeFields(fmt);
            } else {
                fmt = new int[] { 5 };
                file.stringField(0, "LINT");
                file.writeFields(fmt);
                fmt = new int[] { 5, 5, 5, 10 };
                file.stringField(0, "    0");
                file.intField(1, _params.nbIterationAngleFrontiere);
                file.stringField(2, "    0");
                file.stringField(3, "---");
                file.writeFields(fmt);
            }
            fmt = new int[] { 5 };
            file.stringField(0, "STOP");
            file.writeFields(fmt);
            file.flush();
            file.close();
        } catch (final IOException ex) {
            CDodico.exception(DParametresRefonde.class, ex);
        }
    }

    /**
   * Ecriture des param�tres sur fichier .spe du spectre incident.
   *
   * @param _fichier Nom du fichier .spe
   * @param _params Parametres � �crire.
   */
    private static void ecritParametresRefondeSPE(final String _fichier, final SParametresRefondeSPE _params) {
        int[] fmt;
        System.out.println("Ecriture de " + _fichier + ".spe");
        try {
            final FortranWriter file = new FortranWriter(new FileWriter(_fichier + ".spe"));
            file.setStringQuoted(false);
            fmt = new int[] { 6 };
            file.doubleField(0, _params.periodePic);
            file.writeFields(fmt);
            file.doubleField(0, _params.rehaussementPic);
            file.writeFields(fmt);
            file.doubleField(0, _params.anglePrincipal);
            file.writeFields(fmt);
            file.doubleField(0, _params.repartitionAngulaire);
            file.writeFields(fmt);
            fmt = new int[] { 16 };
            file.stringField(0, new File(_fichier).getName() + ".don");
            file.writeFields(fmt);
            file.flush();
            file.close();
        } catch (final IOException ex) {
            CDodico.exception(DParametresRefonde.class, ex);
        }
    }
}
