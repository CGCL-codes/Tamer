package rothag.graphics;

import java.util.EnumMap;
import java.util.HashMap;
import rothag.enums.EtapeJeu;

/**
 * Classe contenant tous les paramètres de l'appli
 * @author Gaetan
 */
public class GraphicsParameters {

    public static final int mainWidth;

    public static final int mainHeight;

    public static final int infosJoueurWidth;

    public static final int infosJoueurHeight;

    public static final int partieWidth;

    public static final int partieHeight;

    public static final EnumMap<EtapeJeu, String> pathEtapeJeu;

    public static final String prefix = "/rothag/graphics/images/";

    public static final String pathFond = prefix + "fond.png";

    public static final String pathImgBois = prefix + "partiegauche/ressource/resBois.png";

    public static final String pathImgFerDeLance = prefix + "partiegauche/ressource/resFer.png";

    public static final String pathImgPierre = prefix + "partiegauche/ressource/resPierre.png";

    public static final String pathImgPoterie = prefix + "partiegauche/ressource/resPoterie.png";

    public static final String pathImgTissu = prefix + "partiegauche/ressource/resTissus.png";

    public static final String pathImgNourriture = prefix + "partiegauche/ressource/resFood.png";

    public static final String pathImgBateau = prefix + "partiegauche/ressource/resBoat.png";

    public static final String pathImgOuvrier = prefix + "partiegauche/ressource/resOuv.png";

    public static final String pathImgPiece = prefix + "partiegauche/ressource/resPiece.png";

    public static final String pathCadreDev = prefix + "partiegauche/cadre/cadreDev.png";

    public static final String pathCadreRes = prefix + "partiegauche/cadre/cadreRes.png";

    public static final String pathCadreScore = prefix + "partiegauche/cadre/cadreScore.png";

    public static final String pathCadreDroit = prefix + "cadreDroit.png";

    public static final String pathDesCranes = prefix + "etape1/imgDesCranes.png";

    public static final String pathDesFood = prefix + "etape1/imgDesFood.png";

    public static final String pathDesMonnaie = prefix + "etape1/imgDesMonnaie.png";

    public static final String pathDesOuvFood = prefix + "etape1/imgDesOuvFood.png";

    public static final String pathDesOuvriers = prefix + "etape1/imgDesOuvriers.png";

    public static final String pathDesRessource = prefix + "etape1/imgDesRessource.png";

    public static final String pathDesLegende = prefix + "etape1/imgLegende.png";

    public static final String pathResOuv = prefix + "etape2/resOuv.png";

    public static final String pathResFood = prefix + "etape2/resFood.png";

    public static final String pathChoixOuvFood = prefix + "etape2/imgDesOuvFood.png";

    public static final String pathDesastrePeste = prefix + "etape2/desastrePeste.png";

    public static final String pathDesastreInvasion = prefix + "etape2/desastreInvasion.png";

    public static final String pathDesastreSecheresse = prefix + "etape2/desastreSecheresse.png";

    public static final String pathDesastreRevolte = prefix + "etape2/desastreRevolte.png";

    public static final String pathDesastreVide = prefix + "etape2/desastreVide.png";

    public static final String pathDesastreFamine = prefix + "etape2/desastreFamine.png";

    public static final String pathTraitConstruire = prefix + "villes/trait.png";

    public static final String pathVilleDefaut = prefix + "villes/villeDepart.png";

    public static final HashMap<Integer, String> pathVilles = new HashMap<Integer, String>();

    public static final HashMap<Integer, String> pathVillesOn = new HashMap<Integer, String>();

    public static final HashMap<Integer, String> pathMonuments = new HashMap<Integer, String>();

    public static final String pathResCommerceTissu = prefix + "commerce/resCommerceTissu.png";

    public static final String pathResCommerceFer = prefix + "commerce/resCommerceFer.png";

    public static final String pathResCommercePierre = prefix + "commerce/resCommercePierre.png";

    public static final String pathResCommerceBois = prefix + "commerce/resCommerceBois.png";

    public static final String pathResCommercePoterie = prefix + "commerce/resCommercePoterie.png";

    public static final String pathResPieceDeveloppement = prefix + "developpement/resPiece.png";

    public static final String pathHelp = "/rothag/help/index.html";

    public static final int bigFrameBottomY = 420;

    public static final int smallFrameTopY = 460;

    public static String font = "Tahoma";

    public static final int nbBateaux = 5;

    /**
     * Constructeur static
     */
    static {
        mainWidth = 1024;
        mainHeight = 768;
        infosJoueurWidth = 265;
        infosJoueurHeight = mainHeight;
        partieWidth = mainWidth - infosJoueurWidth;
        partieHeight = mainHeight;
        pathEtapeJeu = new EnumMap<EtapeJeu, String>(EtapeJeu.class);
        pathEtapeJeu.put(EtapeJeu.LANCERDES, prefix + "partiegauche/tour/cadreTour1.png");
        pathEtapeJeu.put(EtapeJeu.NOURRIR_SELECTION, prefix + "partiegauche/tour/cadreTour2.png");
        pathEtapeJeu.put(EtapeJeu.NOURRIR_INFOS, prefix + "partiegauche/tour/cadreTour2.png");
        pathEtapeJeu.put(EtapeJeu.CONSTRUIRE, prefix + "partiegauche/tour/cadreTour3.png");
        pathEtapeJeu.put(EtapeJeu.COMMERCE, prefix + "partiegauche/tour/cadreTour4.png");
        pathEtapeJeu.put(EtapeJeu.DEVELOPPEMENT, prefix + "partiegauche/tour/cadreTour5.png");
        pathEtapeJeu.put(EtapeJeu.DEFAUSSE, prefix + "partiegauche/tour/cadreTour6.png");
        pathEtapeJeu.put(EtapeJeu.AUCUNE, prefix + "partiegauche/tour/cadreTourVide.png");
        pathEtapeJeu.put(EtapeJeu.FIN_PARTIE, prefix + "partiegauche/tour/cadreTourVide.png");
        pathVilles.put(3, prefix + "villes/ville3.png");
        pathVilles.put(4, prefix + "villes/ville4.png");
        pathVilles.put(5, prefix + "villes/ville5.png");
        pathVilles.put(6, prefix + "villes/ville6.png");
        pathVillesOn.put(3, prefix + "villes/ville3on.png");
        pathVillesOn.put(4, prefix + "villes/ville4on.png");
        pathVillesOn.put(5, prefix + "villes/ville5on.png");
        pathVillesOn.put(6, prefix + "villes/ville6on.png");
        pathMonuments.put(1, prefix + "villes/monument.png");
        pathMonuments.put(2, prefix + "villes/monument2j.png");
        pathMonuments.put(3, prefix + "villes/monument3j.png");
        pathMonuments.put(4, prefix + "villes/monument.png");
    }
}
