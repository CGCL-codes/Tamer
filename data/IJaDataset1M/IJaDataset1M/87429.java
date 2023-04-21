package org.fudaa.fudaa.taucomac;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuInformationsDocument;
import com.memoire.bu.BuInternalFrame;
import com.memoire.bu.BuPrintable;
import com.memoire.bu.BuPrinter;
import com.memoire.bu.BuResource;
import org.fudaa.dodico.corba.taucomac.*;
import org.fudaa.fudaa.commun.projet.FudaaParamEvent;
import org.fudaa.fudaa.commun.projet.FudaaParamEventProxy;
import org.fudaa.fudaa.commun.projet.FudaaParamListener;
import org.fudaa.fudaa.commun.projet.FudaaProjet;
import org.fudaa.fudaa.commun.projet.FudaaProjetEvent;
import org.fudaa.fudaa.commun.projet.FudaaProjetListener;

/**
 * Une fenetre fille pour entrer les parametres.
 *
 * @version      $Revision: 1.8 $ $Date: 2006-09-19 15:08:56 $ by $Author: deniger $
 * @author       Jean-Yves Riou 
 */
public class TaucomacFilleParametres extends BuInternalFrame implements FudaaProjetListener, FudaaParamListener, ActionListener, ChangeListener, PropertyChangeListener, BuPrintable, FocusListener {

    int VALIDER = 0;

    int MODIFIER = 1;

    int VUEPARTRAV = 1;

    int nptmax = 80;

    int mode;

    int mode1;

    int mode2;

    public static final int EDITER = 0x01;

    public static final int VOIR = 0x02;

    public static final int PROFILS = 1;

    public static final int CALAGE = 2;

    public static final int AMONT = 1;

    public static final int AVAL = 2;

    FudaaProjet project_;

    BuCommonInterface appli_;

    TaucomacImplementation imp_;

    SParametresTAU paramTaucoFille = new SParametresTAU();

    JComponent content_;

    JTabbedPane tpMain;

    int ongletCourant;

    TaucomacDonGen pnDonGen;

    TaucomacCotesPrinc pnCotesPrinc;

    TaucomacEssai pnEssai;

    TaucomacTest pnTest;

    TaucomacCasCharge pnCasCharge;

    TaucomacTubes pnTubes;

    TaucomacSolRupt pnSolRupt;

    TaucomacPressio pnPressio;

    TaucomacDefense pnDefense;

    TaucomacAccostage pnAccostage;

    TaucomacAmarrage pnAmarrage;

    int iStyle_ = 1;

    int iPhase_ = 2;

    int iUnit_ = 0;

    int nTubes_ = 0;

    String nomfont1_ = "TimesRoman";

    String nomfont2_ = "Courier";

    String nomfont3_ = "Helvetica";

    String nomfont4_ = "Dialog";

    Font txfont_ = new Font(nomfont4_, Font.PLAIN, 12);

    Color txback_ = Color.white;

    Color txfore_ = Color.blue;

    Font lafont1_ = new Font(nomfont4_, Font.PLAIN, 12);

    Color lafore1_ = Color.black;

    Font lafont2_ = new Font(nomfont4_, Font.PLAIN, 12);

    Color lafore2_ = Color.blue;

    JButton btFermer_, btCalculer_;

    JButton btEffacerAm_, btEditerAm_;

    JButton btEffacerAv_, btEditerAv_;

    JButton btDessinner_;

    protected static TaucomacDessin fen_dessin;

    public TaucomacFilleParametres(final BuCommonInterface _appli, final FudaaProjet projet, final TaucomacImplementation _imp) {
        super("", false, true, false, true);
        appli_ = _appli;
        project_ = projet;
        imp_ = _imp;
        iStyle_ = 0;
        iPhase_ = 2;
        iUnit_ = 1;
        FudaaParamEventProxy.FUDAA_PARAM.addFudaaParamListener(this);
        project_.addFudaaProjetListener(this);
        paramTaucoFille.donGen = new SDonGeneral();
        paramTaucoFille.cotes = new SCotesPrinc();
        paramTaucoFille.casCharg = new SCasCharges();
        paramTaucoFille.pressio = new SPressio();
        paramTaucoFille.defens = new SDefenses();
        paramTaucoFille.accost = new SAccost();
        paramTaucoFille.rupture = new SRupture();
        paramTaucoFille.amarr = new SAmarr();
        paramTaucoFille.tubes = new STubes();
        pnDonGen = new TaucomacDonGen(appli_);
        pnCotesPrinc = new TaucomacCotesPrinc(appli_);
        pnTubes = new TaucomacTubes(appli_);
        pnSolRupt = new TaucomacSolRupt(appli_);
        pnPressio = new TaucomacPressio(appli_);
        pnCasCharge = new TaucomacCasCharge(appli_);
        pnDefense = new TaucomacDefense(appli_);
        pnAccostage = new TaucomacAccostage(appli_);
        pnAmarrage = new TaucomacAmarrage(appli_);
        final JPanel pnValider = new JPanel();
        btFermer_ = new BuButton("Fermer");
        btFermer_.addActionListener(this);
        pnValider.add(btFermer_);
        btCalculer_ = new BuButton("Calculer");
        btCalculer_.addActionListener(this);
        pnValider.add(btCalculer_);
        btDessinner_ = new BuButton("Dessiner");
        btDessinner_.addActionListener(this);
        pnValider.add(btDessinner_);
        tpMain = new JTabbedPane();
        tpMain.addFocusListener(this);
        tpMain.addTab(" Donn�es G�n�rales", null, pnDonGen, "Donn�es G�n�rales");
        tpMain.addTab(" Cotes principales", null, pnCotesPrinc, "Cotes Principales");
        tpMain.addTab(" Tubes", null, pnTubes, "Tubes");
        tpMain.addTab(" Cas de Charges", null, pnCasCharge, "Cas de Charges");
        tpMain.addTab(" Sol Rupture", null, pnSolRupt, "Sol � la rupture");
        tpMain.addTab(" Pressio", null, pnPressio, "Proprietes pressiometriques du sol");
        tpMain.addTab(" D�fenses", null, pnDefense, "D�fenses");
        tpMain.addTab(" Accostages", null, pnAccostage, "Accostages");
        tpMain.addTab(" Amarrage", null, pnAmarrage, "Amarrage");
        tpMain.addChangeListener(this);
        content_ = (JComponent) getContentPane();
        content_.setLayout(new BorderLayout());
        content_.setBorder(new EmptyBorder(5, 5, 5, 5));
        content_.add(BorderLayout.CENTER, tpMain);
        content_.add(BorderLayout.SOUTH, pnValider);
        setTitle("Param�tres de calcul");
        setFrameIcon(BuResource.BU.getIcon("parametre"));
        setLocation(0, 0);
        tpMain.setEnabledAt(0, true);
        updatePanels();
        pack();
    }

    public void setProjet(final FudaaProjet project) {
        System.out.println("action : setProjet   ");
        project_ = project;
        updatePanels();
    }

    public void print(final PrintJob _job, final Graphics _g) {
        BuPrinter.INFO_DOC = new BuInformationsDocument();
        BuPrinter.INFO_DOC.name = getTitle();
        BuPrinter.INFO_DOC.logo = null;
        BuPrinter.printComponent(_job, _g, content_);
    }

    /**
    * Enregistre les donn�es de l'onglet courant dans la structure locale.
    *  Pour cela on g�n�re un faux changement d'onglet.
    */
    public void enregistreOngletCourant() {
        stateChanged(new ChangeEvent(this));
    }

    public void stateChanged(final ChangeEvent e) {
        switch(ongletCourant) {
            case 0:
                System.err.println("Quitte l'onglet pnDonGen ");
                paramTaucoFille.donGen = pnDonGen.getParametres();
                System.out.println("titreEtude   :" + paramTaucoFille.donGen.titreEtude);
                System.out.println("comentEtude  :" + paramTaucoFille.donGen.comentEtude);
                System.out.println("typeSysUnit  :" + paramTaucoFille.donGen.typeSysUnit);
                System.out.println("typeChoixCal :" + paramTaucoFille.donGen.typeChoixCal);
                System.out.println("typeStyle    :" + paramTaucoFille.donGen.typeStyle);
                iStyle_ = paramTaucoFille.donGen.typeStyle;
                iPhase_ = paramTaucoFille.donGen.typeChoixCal;
                iUnit_ = paramTaucoFille.donGen.typeSysUnit;
                changeLook(iStyle_, iPhase_, iUnit_, txfont_, txback_, txfore_, lafont1_, lafore1_, lafont2_, lafore2_);
                pnTubes.majlook(iStyle_, iPhase_, iUnit_, nTubes_);
                break;
            case 1:
                System.err.println("Quitte l'onglet Cotes Principales");
                paramTaucoFille.cotes = pnCotesPrinc.getParametres();
                nTubes_ = paramTaucoFille.cotes.nbTubes;
                pnTubes.majlook(iStyle_, iPhase_, iUnit_, nTubes_);
                System.out.println("Cotes zTete  :" + paramTaucoFille.cotes.zTete);
                System.out.println("Cotes zPied  :" + paramTaucoFille.cotes.zPied);
                System.out.println("Cotes zToitSup  :" + paramTaucoFille.cotes.zToitSup);
                System.out.println("Cotes zToitInf  :" + paramTaucoFille.cotes.zToitInf);
                System.out.println("Cotes nbTubes  :" + paramTaucoFille.cotes.nbTubes);
                System.out.println("Cotes nbTronconsTub  :" + paramTaucoFille.cotes.nbTronconsTub);
                for (int j = 0; j < paramTaucoFille.cotes.nbTronconsTub; j++) {
                    System.out.println("Cotes  carTronc[j].longueur  " + j + " " + paramTaucoFille.cotes.carTronc[j].longueur);
                    System.out.println("Cotes  carTronc[j].epaisseur " + j + " " + paramTaucoFille.cotes.carTronc[j].epaisseur);
                    System.out.println("Cotes  carTronc[j].limElast  " + j + " " + paramTaucoFille.cotes.carTronc[j].limElast);
                }
                break;
            case 2:
                System.err.println("Quitte l'onglet Tubes");
                paramTaucoFille.tubes = pnTubes.getParametres();
                System.out.println("Tubes  	diametre :" + paramTaucoFille.tubes.diametre);
                System.out.println("Tubes  young :" + paramTaucoFille.tubes.young);
                System.out.println("Tubes  distAction:" + paramTaucoFille.tubes.distAction);
                System.out.println("Tubes  distPerpend:" + paramTaucoFille.tubes.distPerpend);
                System.out.println("Tubes   epCorrTet :" + paramTaucoFille.tubes.epCorrTet);
                System.out.println("Tubes  epCorrPied :" + paramTaucoFille.tubes.epCorrPied);
                System.out.println("Tubes   nbCorrod:" + paramTaucoFille.tubes.nbCorrod);
                for (int j = 0; j < 3; j++) {
                    System.out.println("Tubes  corrTubes z  : " + j + " " + paramTaucoFille.tubes.corrTubes[j].z);
                    System.out.println("Tubes  corrTubes ep : " + j + " " + paramTaucoFille.tubes.corrTubes[j].ep);
                }
                break;
            case 3:
                System.err.println("Quitte l'onglet Cas de charges");
                paramTaucoFille.casCharg = pnCasCharge.getParametres();
                System.out.println("CasCharg duraAcost  :" + paramTaucoFille.casCharg.duraAcost);
                System.out.println("CasCharg duraAcostTet  :" + paramTaucoFille.casCharg.duraAcostTet);
                System.out.println("CasCharg duraAmarHoriz:" + paramTaucoFille.casCharg.duraAmarHoriz);
                System.out.println("CasCharg duraAmarHorizVert :" + paramTaucoFille.casCharg.duraAmarHorizVert);
                System.out.println("CasCharg acciAcost   :" + paramTaucoFille.casCharg.acciAcost);
                System.out.println("CasCharg acciAcostTet  :" + paramTaucoFille.casCharg.acciAcostTet);
                System.out.println("CasCharg acciAmarHoriz :" + paramTaucoFille.casCharg.acciAmarHoriz);
                System.out.println("CasCharg acciAmarHorizVert  :" + paramTaucoFille.casCharg.acciAmarHorizVert);
                break;
            case 4:
                System.err.println("Quitte l'onglet  Sol a la rupture");
                paramTaucoFille.rupture = pnSolRupt.getParametres();
                System.out.println("rupture nbCouchesRupt:" + paramTaucoFille.rupture.nbCouchesRupt);
                for (int j = 0; j < paramTaucoFille.rupture.nbCouchesRupt; j++) {
                    System.out.println("couchesRupt[j].zToit          " + j + " " + paramTaucoFille.rupture.couchesRupt[j].zToit);
                    System.out.println("couchesRupt[j].poidsPropre    " + j + " " + paramTaucoFille.rupture.couchesRupt[j].poidsPropre);
                    System.out.println("couchesRupt[j].angleFrot      " + j + " " + paramTaucoFille.rupture.couchesRupt[j].angleFrot);
                    System.out.println("couchesRupt[j].cohesion       " + j + " " + paramTaucoFille.rupture.couchesRupt[j].cohesion);
                    System.out.println("couchesRuptDr[j].zToit        " + j + " " + paramTaucoFille.rupture.couchesRuptDr[j].zToit);
                    System.out.println("couchesRuptDr[j].poidsPropre  " + j + " " + paramTaucoFille.rupture.couchesRuptDr[j].poidsPropre);
                    System.out.println("couchesRuptDr[j].angleFrot    " + j + " " + paramTaucoFille.rupture.couchesRuptDr[j].angleFrot);
                    System.out.println("couchesRuptDr[j].cohesion     " + j + " " + paramTaucoFille.rupture.couchesRuptDr[j].cohesion);
                }
                System.out.println("rupture coefPartButFav:   " + paramTaucoFille.rupture.coefPartButFav);
                System.out.println("rupture coefPartButDefav: " + paramTaucoFille.rupture.coefPartButDefav);
                System.out.println("rupture coefPartCohetFav: " + paramTaucoFille.rupture.coefPartCohetFav);
                System.out.println("rupture coefPartCoheDefav:" + paramTaucoFille.rupture.coefPartCoheDefav);
                break;
            case 5:
                System.err.println("Quitte l'onglet Sol plastique Pressio ");
                paramTaucoFille.pressio = pnPressio.getParametres();
                System.out.println("pressio  nbCouchesPlast:" + paramTaucoFille.pressio.nbCouchesPlast);
                System.out.println("pressio     comportAmar:" + paramTaucoFille.pressio.comportAmar);
                System.out.println("pressio   coefPartInter:" + paramTaucoFille.pressio.coefPartInter);
                System.out.println("pressio nbCouchesPlast= " + paramTaucoFille.pressio.nbCouchesPlast);
                for (int j = 0; j < paramTaucoFille.pressio.nbCouchesPlast; j++) {
                    System.out.println("pressio couche  j = " + j + " ====");
                    System.out.println("pressio modeSaisCourb = " + paramTaucoFille.pressio.couchesPlast[j].modeSaisCourb);
                    if (paramTaucoFille.pressio.couchesPlast[j].modeSaisCourb == 1) {
                        System.out.println("pressio nbPtCourb   = " + paramTaucoFille.pressio.couchesPlast[j].nbPtCourb);
                        for (int i = 0; i < paramTaucoFille.pressio.couchesPlast[j].nbPtCourb; i++) {
                            System.out.println("Point " + (i + 1) + " : def : " + paramTaucoFille.pressio.couchesPlast[j].ptCourb[i].def);
                            System.out.println("      " + (i + 1) + " : eff : " + paramTaucoFille.pressio.couchesPlast[j].ptCourb[i].eff);
                        }
                    }
                }
                break;
            case 6:
                System.err.println("Quitte l'onglet Defenses");
                break;
            case 7:
                System.err.println("Quitte l'onglet Accostages");
                break;
            case 8:
                System.err.println("Quitte l'onglet Amarrage");
                break;
            default:
                System.err.println("Onglet inconnu ");
        }
        ongletCourant = tpMain.getSelectedIndex();
        System.out.println(" ongletCourant = " + ongletCourant);
        switch(ongletCourant) {
            case 0:
                System.err.println("entre  l'onglet 0");
                break;
            case 1:
                System.err.println("entre  l'onglet 1");
                break;
            case 2:
                System.err.println("entre l'onglet  2");
                break;
            case 3:
                System.err.println("entre l'onglet 3");
                break;
            case 4:
                System.err.println("entre l'onglet sol rupt");
                break;
            case 5:
                System.err.println("entre l'onglet pressio");
                break;
            case 6:
                System.err.println("entre l'onglet defenses");
                break;
            case 7:
                System.err.println("entre l'onglet amarrage");
                break;
            default:
                System.err.println("entre inconnu ");
        }
    }

    public void changeLook(final int is, final int ip, final int iu, final Font txf, final Color txb, final Color txfo, final Font laf1, final Color lac1, final Font laf2, final Color lac2) {
        pnDonGen.relook(is, ip, iu, txf, txb, txfo, laf1, lac1, laf2, lac2);
        pnCasCharge.relook(is, ip, iu, txf, txb, txfo, laf1, lac1, laf2, lac2);
        pnCotesPrinc.relook(is, ip, iu, txf, txb, txfo, laf1, lac1, laf2, lac2);
        pnCotesPrinc.majlook(is, ip, iu, txf, txb, txfo, laf1, lac1, laf2, lac2);
        pnTubes.relook(is, ip, iu, txf, txb, txfo, laf1, lac1, laf2, lac2);
        pnTubes.majlook(is, ip, iu, nTubes_);
        pnSolRupt.relook(is, ip, iu, txf, txb, txfo, laf1, lac1, laf2, lac2);
        pnSolRupt.majlook(is, ip, iu);
        pnPressio.relook(is, ip, iu, txf, txb, txfo, laf1, lac1, laf2, lac2);
        pnPressio.majlook(is, ip, iu);
        pnDefense.relook(is, ip, iu, txf, txb, txfo, laf1, lac1, laf2, lac2);
        pnDefense.majlook(is, ip, iu);
        pnAccostage.relook(is, ip, iu, txf, txb, txfo, laf1, lac1, laf2, lac2);
        pnAccostage.majlook(ip, iu);
        pnAmarrage.relook(is, ip, iu, txf, txb, txfo, laf1, lac1, laf2, lac2);
        pnAmarrage.majlook(is, ip, iu);
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        System.out.println("filleparam  PropertyChange  = ");
    }

    public void focusGained(final FocusEvent e) {
    }

    public void focusLost(final FocusEvent e) {
        final Object src = e.getSource();
        System.out.println("filleparam focusLost ");
        if (src instanceof JTextField) {
            updatePanels();
        }
    }

    public void actionListener(final ActionEvent e) {
        final Object src = e.getSource();
        System.out.println("filleparam src actionListener = " + src);
        if (e.getActionCommand().equals("TATA")) {
        }
    }

    public void actionPerformed(final ActionEvent e) {
        System.out.println("filleparam actionperformed ");
        final Object src = e.getSource();
        System.out.println("src actionPerformed = " + src);
        if (src == btFermer_) {
            fermer();
        }
        if (src == btDessinner_) {
            dessinner();
        }
    }

    public void dessinner() {
        System.out.println("filleparam dessinner() ");
        paramTaucoFille.donGen = pnDonGen.getParametres();
        paramTaucoFille.casCharg = pnCasCharge.getParametres();
        paramTaucoFille.cotes = pnCotesPrinc.getParametres();
        paramTaucoFille.tubes = pnTubes.getParametres();
        paramTaucoFille.rupture = pnSolRupt.getParametres();
        paramTaucoFille.pressio = pnPressio.getParametres();
        paramTaucoFille.defens = pnDefense.getParametres();
        paramTaucoFille.accost = pnAccostage.getParametres();
        paramTaucoFille.amarr = pnAmarrage.getParametres();
        final TaucomacDessinContent content = new TaucomacDessinContent(paramTaucoFille);
        final TaucomacDessinNew fen = new TaucomacDessinNew(appli_, content);
        System.out.println("full");
        content.fullRepaint();
        fen.pack();
        fen.show();
    }

    public void valider() {
        System.out.println("filleparam valider() ");
        try {
        } catch (final IllegalArgumentException e) {
            return;
        }
        try {
            setClosed(true);
            setSelected(false);
        } catch (final PropertyVetoException e) {
        }
    }

    public void fermer() {
        System.out.println("filleparam fermer() ");
        try {
        } catch (final IllegalArgumentException e) {
            return;
        }
        try {
            setClosed(true);
            setSelected(false);
        } catch (final PropertyVetoException e) {
        }
    }

    public void calculer() {
        System.out.println("filleparam calculer() ");
    }

    public void delete() {
        System.out.println("filleparam delete ");
        FudaaParamEventProxy.FUDAA_PARAM.removeFudaaParamListener(this);
        if (project_ != null) {
            project_.removeFudaaProjetListener(this);
        }
        project_ = null;
    }

    public void paramStructCreated(final FudaaParamEvent e) {
        System.out.println("filleparam paramsStrucCreated ");
        if (e.getSource() != this) {
            updatePanels();
        }
    }

    public void paramStructDeleted(final FudaaParamEvent e) {
        System.out.println("filleparam paramsStrucDeleted ");
        if (e.getSource() != this) {
            updatePanels();
        }
    }

    public void paramStructModified(final FudaaParamEvent e) {
        System.out.println("filleparam StrucModified ");
    }

    public void dataChanged(final FudaaProjetEvent e) {
        System.out.println("filleparam dataChanged ");
        switch(e.getID()) {
            case FudaaProjetEvent.PARAM_ADDED:
            case FudaaProjetEvent.PARAM_IMPORTED:
                {
                    if (e.getSource() != this) {
                        updatePanels();
                    }
                    break;
                }
        }
    }

    public void statusChanged(final FudaaProjetEvent e) {
        System.out.println("filleparam statusChanged");
        switch(e.getID()) {
            case FudaaProjetEvent.PROJECT_OPENED:
            case FudaaProjetEvent.PROJECT_CLOSED:
                {
                    if (e.getSource() != this) {
                        updatePanels();
                    }
                    break;
                }
        }
    }

    public SParametresTAU getParametresTauco() {
        paramTaucoFille.donGen = pnDonGen.getParametres();
        paramTaucoFille.casCharg = pnCasCharge.getParametres();
        paramTaucoFille.cotes = pnCotesPrinc.getParametres();
        paramTaucoFille.tubes = pnTubes.getParametres();
        paramTaucoFille.rupture = pnSolRupt.getParametres();
        paramTaucoFille.pressio = pnPressio.getParametres();
        paramTaucoFille.defens = pnDefense.getParametres();
        paramTaucoFille.accost = pnAccostage.getParametres();
        paramTaucoFille.amarr = pnAmarrage.getParametres();
        return paramTaucoFille;
    }

    public void setParametresTaucomac(final SParametresTAU params) {
        paramTaucoFille = params;
        if (params != null) {
            pnDonGen.setParametres(params.donGen);
            pnCasCharge.setParametres(params.casCharg);
            pnCotesPrinc.setParametres(params.cotes);
            pnTubes.setParametres(params.tubes);
            pnSolRupt.setParametres(params.rupture);
            pnPressio.setParametres(params.pressio);
            pnDefense.setParametres(params.defens);
            pnAccostage.setParametres(params.accost);
            pnAmarrage.setParametres(params.amarr);
        }
    }

    public int getNTubes() {
        return (nTubes_);
    }

    private void updatePanels() {
        System.out.println("updatePanels");
        pnDonGen.getParametres();
        pnCotesPrinc.getParametres();
        pnTubes.getParametres();
        pnSolRupt.getParametres();
        pnPressio.getParametres();
        pnDefense.getParametres();
        pnAccostage.getParametres();
        pnAmarrage.getParametres();
    }
}
