package org.fudaa.fudaa.modeleur.modeleur1d.controller;

import org.fudaa.ctulu.gis.GISAttributeConstants;
import org.fudaa.ctulu.gis.GISAttributeInterface;
import org.fudaa.ctulu.gis.GISAttributeModel;
import org.fudaa.ctulu.gis.GISAttributeModelDoubleArray;
import org.fudaa.ctulu.gis.GISAttributeModelDoubleInterface;
import org.fudaa.ctulu.gis.GISAttributeModelIntegerList;
import org.fudaa.ctulu.gis.GISCoordinateSequenceContainerInterface;
import org.fudaa.ctulu.gis.GISCoordinateSequenceFactory;
import org.fudaa.ctulu.gis.GISLib;
import org.fudaa.ctulu.gis.GISPolyligne;
import org.fudaa.ctulu.gis.GISReprojectInterpolateur1DDouble;
import org.fudaa.ctulu.gis.GISZoneCollection;
import org.fudaa.ctulu.gis.GISZoneCollectionGeometry;
import org.fudaa.ctulu.gis.GISZoneCollectionLigneBrisee;
import org.fudaa.ebli.calque.ZModeleLigneBrisee;
import org.fudaa.fudaa.commun.FudaaLib;
import org.fudaa.fudaa.modeleur.layer.MdlModel1dAxe;
import org.fudaa.fudaa.modeleur.layer.MdlModel1dBank;
import org.fudaa.fudaa.modeleur.layer.MdlModel1dLimiteStockage;
import org.fudaa.fudaa.modeleur.layer.MdlModel2dConstraintLine;
import org.fudaa.fudaa.modeleur.layer.MdlModel2dDirectionLine;
import org.fudaa.fudaa.modeleur.layer.MdlModel2dLine;
import org.fudaa.fudaa.modeleur.layer.MdlModel2dProfile;
import org.fudaa.fudaa.modeleur.modeleur1d.model.Bief;
import org.fudaa.fudaa.modeleur.modeleur1d.model.UtilsBief1d;
import org.fudaa.fudaa.modeleur.modeleur1d.model.UtilsProfil1d;
import org.fudaa.fudaa.modeleur.modeleur1d.view.OpenBiefPanel;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Import un bief a partir d'un tableau de modeles.
 * 
 * Cette classe est dans les controlleurs car elle peut instancier une vue pour
 * demander � l'utilisateur certaines informations suppl�mentaires.
 * 
 * @author Emmanuel MARTIN
 * @version $Id: BiefImporterFromModels.java 5102 2009-09-10 14:43:29Z bmarchan $
 */
public class BiefImporterFromModels {

    /** Le tableau de mod�le tel que donn� � la construction de l'instance. */
    private ZModeleLigneBrisee[] models_;

    private ZModeleLigneBrisee axeHydraulique_;

    private MdlModel2dLine profils_;

    private ZModeleLigneBrisee rives_;

    private ZModeleLigneBrisee limitesStockages_;

    private ZModeleLigneBrisee lignesDirectrices_;

    private ZModeleLigneBrisee lignesContraints_;

    public BiefImporterFromModels(ZModeleLigneBrisee[] _models) {
        if (_models == null) throw new IllegalArgumentException(FudaaLib.getS("_models ne peut pas �tre null."));
        models_ = _models;
    }

    /**
   * @return le bief r�sultant des mod�les.
   * @throws CancelException 
   */
    public Bief getBief() throws CancelException {
        buildBief();
        Bief bief = new Bief(axeHydraulique_, lignesContraints_, lignesDirectrices_, limitesStockages_, profils_, rives_);
        bief.enableSynchroniser();
        return bief;
    }

    /**
   * Construit les mod�les.
   * @throws CancelException 
   */
    private void buildBief() throws CancelException {
        testAndValuateModels();
        GISZoneCollection zone = profils_.getGeomData();
        UtilsBief1d.normalizeProfilAttributes(zone);
        normalizePKData();
        int idxAttRiveGauche = zone.getIndiceOf(GISAttributeConstants.INTERSECTION_RIVE_GAUCHE);
        int idxAttRiveDroite = zone.getIndiceOf(GISAttributeConstants.INTERSECTION_RIVE_DROITE);
        int idxAttlsGauche = zone.getIndiceOf(GISAttributeConstants.INTERSECTION_LIMITE_STOCKAGE_GAUCHE);
        int idxAttlsDroite = zone.getIndiceOf(GISAttributeConstants.INTERSECTION_LIMITE_STOCKAGE_DROITE);
        int idxAttLignesDirectrices = zone.getIndiceOf(GISAttributeConstants.INTERSECTIONS_LIGNES_DIRECTRICES);
        UtilsBief1d.orderProfils(profils_, -1, null);
        for (int k = 0; k < profils_.getNombre(); k++) {
            Geometry profil = zone.getGeometry(k);
            CoordinateSequence seqProfil = ((GISCoordinateSequenceContainerInterface) profil).getCoordinateSequence();
            Coordinate interAxeProfil = seqProfil.getCoordinate(seqProfil.size() / 2);
            Coordinate vAxeH = null;
            boolean noReorientation = false;
            if (k > 0) {
                CoordinateSequence profilPrevious = zone.getCoordinateSequence(k - 1);
                Coordinate pointProfilPrevious = profilPrevious.getCoordinate(profilPrevious.size() / 2);
                vAxeH = new Coordinate(interAxeProfil.x - pointProfilPrevious.x, interAxeProfil.y - pointProfilPrevious.y, 0);
            } else if (k < profils_.getNombre() - 1) {
                CoordinateSequence profilPrevious = zone.getCoordinateSequence(k + 1);
                Coordinate pointProfilNext = profilPrevious.getCoordinate(profilPrevious.size() / 2);
                vAxeH = new Coordinate(pointProfilNext.x - interAxeProfil.x, pointProfilNext.y - interAxeProfil.y, 0);
            } else {
                if (axeHydraulique_.getNombre() > 0) {
                    Geometry axeHydraulique = (Geometry) axeHydraulique_.getObject(0);
                    CoordinateSequence seqAxeHydraulique = ((GISCoordinateSequenceContainerInterface) axeHydraulique).getCoordinateSequence();
                    Coordinate interAxeProfil2 = profil.intersection(axeHydraulique).getCoordinate();
                    int idxPrevious = UtilsProfil1d.getPreviousIndex(seqAxeHydraulique, interAxeProfil2);
                    int idxNext = UtilsProfil1d.getNextIndex(seqAxeHydraulique, interAxeProfil2);
                    if (idxPrevious != -1) vAxeH = new Coordinate(interAxeProfil2.x - seqAxeHydraulique.getCoordinate(idxPrevious).x, interAxeProfil2.y - seqAxeHydraulique.getCoordinate(idxPrevious).y, 0); else vAxeH = new Coordinate(seqAxeHydraulique.getCoordinate(idxNext).x - interAxeProfil2.x, seqAxeHydraulique.getCoordinate(idxNext).y - interAxeProfil2.y, 0);
                } else noReorientation = true;
            }
            if (!noReorientation) {
                Coordinate vProfilH;
                int idxPrevious = UtilsProfil1d.getPreviousIndex(seqProfil, interAxeProfil);
                int idxNext = UtilsProfil1d.getNextIndex(seqProfil, interAxeProfil);
                if (idxPrevious != -1) vProfilH = new Coordinate(interAxeProfil.x - seqProfil.getCoordinate(idxPrevious).x, interAxeProfil.y - seqProfil.getCoordinate(idxPrevious).y, 0); else vProfilH = new Coordinate(seqProfil.getCoordinate(idxNext).x - interAxeProfil.x, seqProfil.getCoordinate(idxNext).y - interAxeProfil.y, 0);
                double produitVectorielCoordZ = vAxeH.x * vProfilH.y - vAxeH.y * vProfilH.x;
                if (produitVectorielCoordZ > 0) {
                    profils_.invertOrientation(k, null);
                    profil = (Geometry) profils_.getGeomData().getGeometry(k);
                    seqProfil = ((GISCoordinateSequenceContainerInterface) profil).getCoordinateSequence();
                }
            }
        }
        for (int k = 0; k < profils_.getNombre(); k++) {
            for (int l = 0; l < lignesContraints_.getNombre(); l++) createPointIfNeededKeepZ(k, l, lignesContraints_);
            for (int l = 0; l < rives_.getNombre(); l++) createPointIfNeeded(k, (GISPolyligne) rives_.getObject(l));
            for (int l = 0; l < limitesStockages_.getNombre(); l++) createPointIfNeeded(k, (GISPolyligne) limitesStockages_.getObject(l));
            for (int l = 0; l < lignesDirectrices_.getNombre(); l++) createPointIfNeeded(k, (GISPolyligne) lignesDirectrices_.getObject(l));
        }
        for (int k = 0; k < profils_.getNombre(); k++) {
            CoordinateSequence seqProfil = zone.getCoordinateSequence(k);
            double abscisseCurvIntersectionAxe;
            if (axeHydraulique_.getNombre() > 0) abscisseCurvIntersectionAxe = UtilsProfil1d.abscisseCurviligne(seqProfil, axeHydraulique_.getGeomData().getCoordinateSequence(0)); else abscisseCurvIntersectionAxe = UtilsProfil1d.abscisseCurviligne(seqProfil, seqProfil.getCoordinate(seqProfil.size() - 1)) / 2;
            zone.setAttributValue(idxAttRiveGauche, k, 0, null);
            zone.setAttributValue(idxAttRiveDroite, k, seqProfil.size() - 1, null);
            for (int l = 0; l < rives_.getNombre(); l++) valuateProfilIntersection(k, (Geometry) rives_.getObject(l), idxAttRiveGauche, idxAttRiveDroite, abscisseCurvIntersectionAxe);
            zone.setAttributValue(idxAttlsGauche, k, 0, null);
            zone.setAttributValue(idxAttlsDroite, k, seqProfil.size() - 1, null);
            for (int l = 0; l < limitesStockages_.getNombre(); l++) valuateProfilIntersection(k, (Geometry) limitesStockages_.getObject(l), idxAttlsGauche, idxAttlsDroite, abscisseCurvIntersectionAxe);
        }
        valuateProfilIntersection(idxAttLignesDirectrices);
        UtilsBief1d.normalizeAxeHydrauliqueAttributes(axeHydraulique_.getGeomData());
    }

    /**
   * Normalise les informations PK.
   * @throws CancelException si l'imortation est annul�e.
   */
    private void normalizePKData() throws CancelException {
        GISZoneCollection zone = profils_.getGeomData();
        int idxAttCommentaireHydraulique = zone.getIndiceOf(GISAttributeConstants.COMMENTAIRE_HYDRO);
        boolean attrIsCorrectlyValued = false;
        boolean attrMatchWithAxeHydraulique = false;
        boolean presenceAxeHydraulique = axeHydraulique_.getNombre() > 0;
        double decalAxe = 0.0;
        if (presenceAxeHydraulique) {
            int idxAttDecalAxe = axeHydraulique_.getGeomData().getIndiceOf(GISAttributeConstants.CURVILIGNE_DECALAGE);
            if (idxAttDecalAxe != -1) {
                decalAxe = ((Double) axeHydraulique_.getGeomData().getValue(idxAttDecalAxe, 0)).doubleValue();
            }
        }
        if (idxAttCommentaireHydraulique != -1) {
            attrIsCorrectlyValued = true;
            if (presenceAxeHydraulique) attrMatchWithAxeHydraulique = true;
            int i = -1;
            while (attrIsCorrectlyValued && ++i < profils_.getNombre()) {
                String CommHydrauValue = (String) profils_.getGeomData().getValue(idxAttCommentaireHydraulique, i);
                attrIsCorrectlyValued = GISLib.isHydroCommentValued(CommHydrauValue, "PK");
                if (presenceAxeHydraulique) {
                    double hydraoCommValue = GISLib.getHydroCommentDouble(CommHydrauValue, "PK");
                    double absCurvValue = UtilsProfil1d.abscisseCurviligne(axeHydraulique_.getGeomData().getCoordinateSequence(0), profils_.getGeomData().getCoordinateSequence(i));
                    if (absCurvValue == -1) throw new IllegalArgumentException(FudaaLib.getS("Au moins un des profils coupe plusieurs fois ou jamais l'axe hydraulique."));
                    attrMatchWithAxeHydraulique = attrIsCorrectlyValued && attrMatchWithAxeHydraulique && Math.abs(hydraoCommValue - (absCurvValue + decalAxe)) < 0.0001;
                }
            }
        } else {
            GISAttributeInterface[] atts = new GISAttributeInterface[zone.getNbAttributes() + 1];
            for (int k = 0; k < zone.getNbAttributes(); k++) atts[k] = zone.getAttribute(k);
            atts[atts.length - 1] = GISAttributeConstants.COMMENTAIRE_HYDRO;
            zone.setAttributes(atts, null);
        }
        if (!attrIsCorrectlyValued) {
            idxAttCommentaireHydraulique = zone.getIndiceOf(GISAttributeConstants.COMMENTAIRE_HYDRO);
            for (int k = 0; k < zone.getNumGeometries(); k++) {
                String comm = (String) profils_.getGeomData().getValue(idxAttCommentaireHydraulique, k);
                if (!GISLib.isHydroCommentValued(comm, "PK")) {
                    double pk;
                    if (presenceAxeHydraulique) {
                        pk = UtilsProfil1d.abscisseCurviligne(axeHydraulique_.getGeomData().getCoordinateSequence(0), profils_.getGeomData().getCoordinateSequence(k)) + decalAxe;
                    } else {
                        pk = decalAxe;
                    }
                    zone.setAttributValue(idxAttCommentaireHydraulique, k, GISLib.setHydroCommentDouble(comm, pk, "PK"), null);
                }
            }
            attrMatchWithAxeHydraulique = true;
        }
        if (!attrMatchWithAxeHydraulique && presenceAxeHydraulique) {
            OpenBiefPanel vImport = new OpenBiefPanel();
            if (!vImport.run()) throw new CancelException(FudaaLib.getS("L'importation a �t� annul�."));
            if (vImport.axeHydrauChosen()) {
                idxAttCommentaireHydraulique = zone.getIndiceOf(GISAttributeConstants.COMMENTAIRE_HYDRO);
                for (int k = 0; k < zone.getNumGeometries(); k++) {
                    String comm = (String) profils_.getGeomData().getValue(idxAttCommentaireHydraulique, k);
                    double pk = UtilsProfil1d.abscisseCurviligne(axeHydraulique_.getGeomData().getCoordinateSequence(0), profils_.getGeomData().getCoordinateSequence(k)) + decalAxe;
                    zone.setAttributValue(idxAttCommentaireHydraulique, k, GISLib.setHydroCommentDouble(comm, pk, "PK"), null);
                }
            } else {
                int[] idxAH = new int[axeHydraulique_.getNombre()];
                for (int i = 0; i < idxAH.length; i++) idxAH[i] = i;
                axeHydraulique_.getGeomData().removeGeometries(idxAH, null);
            }
        }
    }

    /**
   * Value les attributs priv�s a partir du tableau de ZModeleLigneBrisee.
   */
    private void testAndValuateModels() {
        for (int i = 0; i < models_.length; i++) {
            if (models_[i] == null) throw new IllegalArgumentException(FudaaLib.getS("Erreur programmation : _models ne doit pas contenir de valeurs null"));
            if (models_[i].getGeomData() == null) throw new IllegalArgumentException(FudaaLib.getS("Erreur programmation : Certain model n'ont pas de GSIZoneCollection."));
            if (!(models_[i].getGeomData() instanceof GISZoneCollectionLigneBrisee)) throw new IllegalArgumentException(FudaaLib.getS("Erreur programmation : Toutes les GISZone doivent �tre des GISZoneCollectionLigneBrisee."));
            String nature = (String) models_[i].getGeomData().getFixedAttributValue(GISAttributeConstants.NATURE);
            if (nature == null) throw new IllegalArgumentException(FudaaLib.getS("Un des modeles ne contient pas l'attribut NATURE."));
            if (nature == GISAttributeConstants.ATT_NATURE_AH) if (axeHydraulique_ != null) throw new IllegalArgumentException(FudaaLib.getS("Plusieurs models d'axe hydrauliques sont donn�es.")); else axeHydraulique_ = models_[i]; else if (nature == GISAttributeConstants.ATT_NATURE_LD) if (lignesDirectrices_ != null) throw new IllegalArgumentException(FudaaLib.getS("Plusieurs models de lignes directrices sont donn�es.")); else lignesDirectrices_ = models_[i]; else if (nature == GISAttributeConstants.ATT_NATURE_LS) if (limitesStockages_ != null) throw new IllegalArgumentException(FudaaLib.getS("Plusieurs models de limites de stockage sont donn�es.")); else if (models_[i].getGeomData().getNbGeometries() > 2) throw new IllegalArgumentException(FudaaLib.getS("Il ne peut pas y avoir plus de 2 limites de stockages.")); else limitesStockages_ = models_[i]; else if (nature == GISAttributeConstants.ATT_NATURE_PF) if (profils_ != null) throw new IllegalArgumentException(FudaaLib.getS("Plusieurs models de profils sont donn�es.")); else profils_ = (MdlModel2dLine) models_[i]; else if (nature == GISAttributeConstants.ATT_NATURE_RV) if (rives_ != null) throw new IllegalArgumentException(FudaaLib.getS("Plusieurs models de rives sont donn�es.")); else if (models_[i].getGeomData().getNbGeometries() > 2) throw new IllegalArgumentException(FudaaLib.getS("Il ne peut pas y avoir plus de 2 rives.")); else rives_ = models_[i]; else if (nature == GISAttributeConstants.ATT_NATURE_LC) if (lignesContraints_ != null) throw new IllegalArgumentException(FudaaLib.getS("Plusieurs models de lignes de contraintes sont donn�es.")); else lignesContraints_ = models_[i];
        }
        if (axeHydraulique_ == null) axeHydraulique_ = new MdlModel1dAxe(null);
        if (profils_ == null) profils_ = new MdlModel2dProfile(null, null);
        if (rives_ == null) rives_ = new MdlModel1dBank(null, null);
        if (limitesStockages_ == null) limitesStockages_ = new MdlModel1dLimiteStockage(null, null);
        if (lignesDirectrices_ == null) lignesDirectrices_ = new MdlModel2dDirectionLine(null, null);
        if (lignesContraints_ == null) lignesContraints_ = new MdlModel2dConstraintLine(null, null);
    }

    /**
   * Valuation des attributs *gauche et *droite des profils.
   * 
   * @param _idxProfil
   *          l'index du profil concern�.
   * @param _ligne
   *          la ligne qui est sens� crois� le profil.
   * @param _idxAttrGauche
   *          l'index de l'attribut gauche o� sera mit l'information en cas de
   *          croisement � gauche.
   * @param _idxAttrDroite
   *          l'index de l'attribut gauche o� sera mit l'information en cas de
   *          croisement � droite.
   * @param _absCurvAxeHydrau
   *          la valeur de l'abscisse curviligne de l'axe hydrau sur le profil.
   */
    private void valuateProfilIntersection(int _idxProfil, Geometry _ligne, int _idxAttrGauche, int _idxAttrDroite, double _absCurvAxeHydrau) {
        Geometry inter = _ligne.intersection((Geometry) profils_.getObject(_idxProfil));
        if (inter.getNumPoints() == 1) {
            Coordinate coord = inter.getCoordinate();
            int idxIntersection = UtilsProfil1d.getIndex(profils_.getGeomData().getCoordinateSequence(_idxProfil), coord);
            if (_absCurvAxeHydrau < UtilsProfil1d.abscisseCurviligne(profils_.getGeomData().getCoordinateSequence(_idxProfil), coord)) profils_.getGeomData().setAttributValue(_idxAttrDroite, _idxProfil, idxIntersection, null); else profils_.getGeomData().setAttributValue(_idxAttrGauche, _idxProfil, idxIntersection, null);
        }
    }

    /**
   * Valuation des attributs composites des profils, c'est � dire des attributs
   * contenant plusieurs informations d'intersection sous forme d'une liste.
   * 
   * @param _idxAttr
   *          l'index de l'attribut o� sera stock� les intersections
   */
    private void valuateProfilIntersection(int _idxAttr) {
        for (int k = 0; k < profils_.getNombre(); k++) {
            GISAttributeModelIntegerList attrModel = new GISAttributeModelIntegerList(0, GISAttributeConstants.INTERSECTIONS_LIGNES_DIRECTRICES);
            attrModel.setListener(profils_.getGeomData());
            profils_.getGeomData().setAttributValue(_idxAttr, k, attrModel, null);
        }
        int k = 0;
        int[] lstIntersectionTmp = new int[profils_.getNombre()];
        while (k < lignesDirectrices_.getNombre()) {
            Geometry ligneD = (Geometry) lignesDirectrices_.getObject(k);
            for (int l = 0; l < profils_.getNombre(); l++) {
                Geometry profil = profils_.getGeomData().getGeometry(l);
                CoordinateSequence seqProfil = ((GISCoordinateSequenceContainerInterface) profil).getCoordinateSequence();
                Geometry inter = ((Geometry) profils_.getObject(l)).intersection(ligneD);
                if (inter.getNumPoints() == 0) lstIntersectionTmp[l] = -1; else lstIntersectionTmp[l] = UtilsProfil1d.getIndex(seqProfil, inter.getCoordinate());
            }
            boolean ok = false;
            int m = -1;
            while (!ok && ++m < lstIntersectionTmp.length) ok = lstIntersectionTmp[m] != -1;
            if (!ok) lignesDirectrices_.getGeomData().removeGeometries(new int[] { k }, null); else {
                for (int l = 0; l < profils_.getNombre(); l++) {
                    GISAttributeModelIntegerList lst = (GISAttributeModelIntegerList) profils_.getGeomData().getValue(_idxAttr, l);
                    if (lstIntersectionTmp[l] != -1) lst.add(lstIntersectionTmp[l]); else {
                        int idxTest = l;
                        while (idxTest >= 0 && lstIntersectionTmp[idxTest] == -1) idxTest--;
                        if (idxTest < 0) {
                            idxTest = l;
                            while (idxTest < lstIntersectionTmp.length && lstIntersectionTmp[idxTest] == -1) idxTest++;
                        }
                        Geometry profil = profils_.getGeomData().getGeometry(idxTest);
                        CoordinateSequence seqProfil = ((GISCoordinateSequenceContainerInterface) profil).getCoordinateSequence();
                        int idxAxe;
                        if (axeHydraulique_.getNombre() == 0) idxAxe = seqProfil.size() / 2; else {
                            Coordinate intersection = profil.intersection((Geometry) axeHydraulique_.getObject(0)).getCoordinate();
                            idxAxe = UtilsProfil1d.getPreviousIndex(seqProfil, intersection);
                            if (idxAxe == -1) idxAxe = 0;
                        }
                        if (lstIntersectionTmp[idxTest] <= idxAxe) lst.add(0); else lst.add(profils_.getGeomData().getCoordinateSequence(l).size() - 1);
                    }
                }
                k++;
            }
        }
    }

    /**
   * Cr�e un point � l'intersection du profil indiqu� par _idxProfil et de
   * _ligne.
   */
    private void createPointIfNeeded(int _idxProfil, GISPolyligne _ligne) {
        Geometry inter = _ligne.intersection((Geometry) profils_.getObject(_idxProfil));
        CoordinateSequence seq = profils_.getGeomData().getCoordinateSequence(_idxProfil);
        if (inter.getNumPoints() == 1) {
            Coordinate coord = inter.getCoordinate();
            int previousIdx = UtilsProfil1d.getPreviousIndex(seq, coord);
            int nextIdx = UtilsProfil1d.getNextIndex(seq, coord);
            if (previousIdx != -2 && nextIdx != -2 && nextIdx != -1 && previousIdx != -1 && (nextIdx - previousIdx) < 2) ((GISZoneCollectionGeometry) profils_.getGeomData()).addAtomic(_idxProfil, previousIdx, coord.x, coord.y, null);
        }
    }

    /**
   * Cr�e un point � l'intersection du profil indiqu� par _idxProfil et de
   * _ligne. Le z du point cr�e prendra la valeur z du point correspondant dans
   * _ligne (interpol� si n�c�ssaire).
   */
    private void createPointIfNeededKeepZ(int _idxProfil, int _idxLigne, ZModeleLigneBrisee _modelLigne) {
        GISZoneCollection zoneLigne = _modelLigne.getGeomData();
        GISZoneCollection zoneProfil = profils_.getGeomData();
        GISPolyligne ligne = (GISPolyligne) zoneLigne.getGeometry(_idxLigne);
        GISPolyligne profil = (GISPolyligne) zoneProfil.getGeometry(_idxProfil);
        CoordinateSequence seqLigne = zoneLigne.getCoordinateSequence(_idxLigne);
        CoordinateSequence seqProfil = zoneProfil.getCoordinateSequence(_idxProfil);
        Geometry intersection = ligne.intersection(profil);
        if (intersection.getNumPoints() == 1) {
            Coordinate coordIntersection = intersection.getCoordinate();
            int previousIdx = UtilsProfil1d.getPreviousIndex(seqProfil, coordIntersection);
            int nextIdx = UtilsProfil1d.getNextIndex(seqProfil, coordIntersection);
            if (previousIdx != -2 && nextIdx != -2 && nextIdx != -1 && previousIdx != -1 && (nextIdx - previousIdx) < 2) {
                ((GISZoneCollectionGeometry) zoneProfil).addAtomic(_idxProfil, previousIdx, coordIntersection.x, coordIntersection.y, null);
                seqProfil = zoneProfil.getCoordinateSequence(_idxProfil);
            }
            if (zoneLigne.getAttributeIsZ() != null) {
                int idxPProfil = UtilsProfil1d.getIndex(seqProfil, coordIntersection);
                int idxPLigne = UtilsProfil1d.getIndex(seqLigne, coordIntersection);
                GISAttributeModel modelZLigne = zoneLigne.getModel(zoneLigne.getAttributeIsZ());
                double z;
                if (!zoneLigne.getAttributeIsZ().isAtomicValue()) z = (Double) modelZLigne.getObjectValueAt(_idxLigne); else if (idxPLigne != -1) z = (Double) ((GISAttributeModel) modelZLigne.getObjectValueAt(_idxLigne)).getObjectValueAt(idxPLigne); else {
                    int idxPrevious = UtilsProfil1d.getPreviousIndex(seqLigne, coordIntersection);
                    if (idxPrevious == -1) idxPrevious = 0;
                    Coordinate previous = seqLigne.getCoordinate(idxPrevious);
                    int idxNext = UtilsProfil1d.getNextIndex(seqLigne, coordIntersection);
                    if (idxNext == -1) idxNext = seqLigne.size() - 1;
                    Coordinate next = seqLigne.getCoordinate(UtilsProfil1d.getNextIndex(seqLigne, coordIntersection));
                    double valZ1 = (Double) ((GISAttributeModel) modelZLigne.getObjectValueAt(_idxLigne)).getObjectValueAt(idxPrevious);
                    double valZ2 = (Double) ((GISAttributeModel) modelZLigne.getObjectValueAt(_idxLigne)).getObjectValueAt(idxNext);
                    GISAttributeModelDoubleInterface zModel = new GISAttributeModelDoubleArray(new double[] { valZ1, valZ2 }, zoneLigne.getAttributeIsZ());
                    GISCoordinateSequenceFactory factory = new GISCoordinateSequenceFactory();
                    z = new GISReprojectInterpolateur1DDouble(factory.create(new Coordinate[] { previous, next }), factory.create(new Coordinate[] { previous, new Coordinate(coordIntersection.x, coordIntersection.y, 0), next }), zModel).interpol(1);
                }
                ((GISAttributeModel) zoneProfil.getModel(zoneProfil.getAttributeIsZ()).getObjectValueAt(_idxProfil)).setObject(idxPProfil, z, null);
            }
        }
    }
}
