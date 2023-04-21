package galaxiia.jeu.unite;

import galaxiia.jeu.Outils;
import galaxiia.jeu.intelligence.Intelligence;
import galaxiia.jeu.terrain.InformateurTerrain;
import galaxiia.noyau.GestionnaireSecurite;

public class ControleurUniteReseau extends UniteVirtuelle implements ControleurUnite {

    private static final long serialVersionUID = 1;

    private final long idIntelligence;

    private ActionUnite actionEnCours;

    private Intelligence nouvelleIntelligence;

    public boolean activationBouclierRepulsif(int typeUnite) {
        GestionnaireSecurite.estThreadBloque();
        if ((ConstantesUnite.BOUCLIER_ASTRAL == typeUnite) || (ConstantesUnite.BOUCLIER_REPULSIF == typeUnite)) {
            if (!peutCreer(typeUnite)) {
                return false;
            } else {
                if ((energie() > Unite.creationUniteAbstraite(typeUnite).coutEnergetiqueCreation()) && (typeActionEnCours() == ACTION_AUCUNE)) {
                    actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_LANCEMENT, new double[] { 0, 0, 1 }, typeUnite, ActionUnite.AUCUNE);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            throw new IllegalArgumentException("L'unite " + typeUnite + " n'est pas un bouclier répulsif.");
        }
    }

    public boolean lancementBombe(int typeUnite, double[] vitesseInitiale, int nombreTourAvantExplosion) {
        if (nombreTourAvantExplosion < 0) {
            throw new IllegalArgumentException("Le nombre de tour avant la destruction ne doit pas être négatif.");
        }
        if (Double.isNaN(vitesseInitiale[0]) || Double.isNaN(vitesseInitiale[1])) {
            throw new IllegalArgumentException("Une des composantes de la vitesse données est NaN.");
        }
        if (Double.isInfinite(vitesseInitiale[0]) || Double.isInfinite(vitesseInitiale[1])) {
            throw new IllegalArgumentException("Une des composantes de la vitesse données est infinie.");
        }
        if (!(ClasseurUnite.estBombe(typeUnite))) {
            throw new IllegalArgumentException("L'unite " + ClasseurUnite.conversionTypeNom(typeUnite) + " n'est pas une bombe.");
        }
        if (Outils.normeCarre(vitesseInitiale) > ClasseurUnite.creationUniteAbstraite(typeUnite).vitesseCarreMaximumLancement(type())) {
            vitesseInitiale = Outils.normalisation(vitesseInitiale, ClasseurUnite.creationUniteAbstraite(typeUnite).vitesseCarreMaximumLancement(type()));
        }
        if (!peutCreer(typeUnite)) {
            return false;
        }
        if ((energie() > Unite.creationUniteAbstraite(typeUnite).coutEnergetiqueCreation()) && (typeActionEnCours() == ACTION_AUCUNE)) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_LANCEMENT, new double[] { vitesseInitiale[0], vitesseInitiale[1], 1, Double.longBitsToDouble(nombreTourAvantExplosion) }, typeUnite, ActionUnite.INTELLIGENCE_BOMBE);
            return true;
        } else {
            return false;
        }
    }

    public boolean lancementBombeVersPoint(int typeUnite, double[] pointCible, int nombreTourAvantExplosion) {
        if (nombreTourAvantExplosion < 0) {
            throw new IllegalArgumentException("Le nombre de tour avant la destruction ne doit pas être négatif.");
        }
        if (Double.isNaN(pointCible[0]) || Double.isNaN(pointCible[1])) {
            throw new IllegalArgumentException("Une des composantes du point cible donné est NaN.");
        }
        if (Double.isInfinite(pointCible[0]) || Double.isInfinite(pointCible[1])) {
            throw new IllegalArgumentException("Une des composantes du point cible donné est infinie.");
        }
        if (!(ClasseurUnite.estBombe(typeUnite))) {
            throw new IllegalArgumentException("L'unite " + ClasseurUnite.conversionTypeNom(typeUnite) + " n'est pas une bombe.");
        }
        if (!peutCreer(typeUnite)) {
            return false;
        }
        if ((energie() > Unite.creationUniteAbstraite(typeUnite).coutEnergetiqueCreation()) && (typeActionEnCours() == ACTION_AUCUNE)) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_LANCEMENT, new double[] { pointCible[0], pointCible[1], 0, Double.longBitsToDouble(nombreTourAvantExplosion) }, typeUnite, ActionUnite.INTELLIGENCE_BOMBE);
            return true;
        } else {
            return false;
        }
    }

    public boolean lancementMissileAutoguide(int typeUnite, double[] vitesseInitiale, double[] pointCibleMissile) {
        if (Double.isNaN(vitesseInitiale[0]) || Double.isNaN(vitesseInitiale[1])) {
            throw new IllegalArgumentException("Une des composantes de la vitesse données est NaN.");
        }
        if (Double.isInfinite(vitesseInitiale[0]) || Double.isInfinite(vitesseInitiale[1])) {
            throw new IllegalArgumentException("Une des composantes de la vitesse données est infinie.");
        }
        if (!(ClasseurUnite.estAutoguide(typeUnite))) {
            throw new IllegalArgumentException("L'unite " + ClasseurUnite.conversionTypeNom(typeUnite) + " n'est pas autoguidee.");
        }
        if (Outils.normeCarre(vitesseInitiale) > ClasseurUnite.creationUniteAbstraite(typeUnite).vitesseCarreMaximumLancement(type())) {
            vitesseInitiale = Outils.normalisation(vitesseInitiale, ClasseurUnite.creationUniteAbstraite(typeUnite).vitesseCarreMaximumLancement(type()));
        }
        if (!peutCreer(typeUnite)) {
            return false;
        }
        if ((energie() > Unite.creationUniteAbstraite(typeUnite).coutEnergetiqueCreation()) && (typeActionEnCours() == ACTION_AUCUNE)) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_LANCEMENT, new double[] { vitesseInitiale[0], vitesseInitiale[1], 1, pointCibleMissile[0], pointCibleMissile[1] }, typeUnite, ActionUnite.INTELLIGENCE_AUTOGUIDE);
            return true;
        } else {
            return false;
        }
    }

    public boolean lancementMissileAutoguideVersPoint(int typeUnite, double[] pointCible, double[] pointCibleMissile) {
        if (Double.isNaN(pointCible[0]) || Double.isNaN(pointCible[1])) {
            throw new IllegalArgumentException("Une des composantes du point cible donné est NaN.");
        }
        if (Double.isInfinite(pointCible[0]) || Double.isInfinite(pointCible[1])) {
            throw new IllegalArgumentException("Une des composantes du point cible donné est infinie.");
        }
        if (!(ClasseurUnite.estAutoguide(typeUnite))) {
            throw new IllegalArgumentException("L'unite " + ClasseurUnite.conversionTypeNom(typeUnite) + " n'est pas autoguidee.");
        }
        if (!peutCreer(typeUnite)) {
            return false;
        }
        if ((energie() > Unite.creationUniteAbstraite(typeUnite).coutEnergetiqueCreation()) && (typeActionEnCours() == ACTION_AUCUNE)) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_LANCEMENT, new double[] { pointCible[0], pointCible[1], 0, pointCibleMissile[0], pointCibleMissile[1] }, typeUnite, ActionUnite.INTELLIGENCE_AUTOGUIDE);
            return true;
        } else {
            return false;
        }
    }

    public boolean lancementMissileTeleguide(int typeUnite, double[] vitesseInitiale, long idUnite) {
        if (Double.isNaN(vitesseInitiale[0]) || Double.isNaN(vitesseInitiale[1])) {
            throw new IllegalArgumentException("Une des composantes de la vitesse données est NaN.");
        }
        if (Double.isInfinite(vitesseInitiale[0]) || Double.isInfinite(vitesseInitiale[1])) {
            throw new IllegalArgumentException("Une des composantes de la vitesse données est infinie.");
        }
        if (!(ClasseurUnite.estTeleguide(typeUnite))) {
            throw new IllegalArgumentException("L'unite " + ClasseurUnite.conversionTypeNom(typeUnite) + " n'est pas teleguidee.");
        }
        if (Outils.normeCarre(vitesseInitiale) > ClasseurUnite.creationUniteAbstraite(typeUnite).vitesseCarreMaximumLancement(type())) {
            vitesseInitiale = Outils.normalisation(vitesseInitiale, ClasseurUnite.creationUniteAbstraite(typeUnite).vitesseCarreMaximumLancement(type()));
        }
        if (!peutCreer(typeUnite)) {
            return false;
        }
        if ((energie() > Unite.creationUniteAbstraite(typeUnite).coutEnergetiqueCreation()) && (typeActionEnCours() == ACTION_AUCUNE)) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_LANCEMENT, new double[] { vitesseInitiale[0], vitesseInitiale[1], 1, Double.longBitsToDouble(idUnite) }, typeUnite, ActionUnite.INTELLIGENCE_TELEGUIDE);
            return true;
        } else {
            return false;
        }
    }

    public boolean lancementMissileTeleguideVersPoint(int typeUnite, double[] pointCible, long idUnite) {
        if (Double.isNaN(pointCible[0]) || Double.isNaN(pointCible[1])) {
            throw new IllegalArgumentException("Une des composantes du point cible donné est NaN.");
        }
        if (Double.isInfinite(pointCible[0]) || Double.isInfinite(pointCible[1])) {
            throw new IllegalArgumentException("Une des composantes du point cible donné est infinie.");
        }
        if (!(ClasseurUnite.estTeleguide(typeUnite))) {
            throw new IllegalArgumentException("L'unite " + ClasseurUnite.conversionTypeNom(typeUnite) + " n'est pas teleguidee.");
        }
        if (!peutCreer(typeUnite)) {
            return false;
        }
        if ((energie() > Unite.creationUniteAbstraite(typeUnite).coutEnergetiqueCreation()) && (typeActionEnCours() == ACTION_AUCUNE)) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_LANCEMENT, new double[] { pointCible[0], pointCible[1], 0, Double.longBitsToDouble(idUnite) }, typeUnite, ActionUnite.INTELLIGENCE_TELEGUIDE);
            return true;
        } else {
            return false;
        }
    }

    public boolean lancementProjectile(int typeUnite, double[] vitesseInitiale) {
        if (Double.isNaN(vitesseInitiale[0]) || Double.isNaN(vitesseInitiale[1])) {
            throw new IllegalArgumentException("Une des composantes de la vitesse données est NaN.");
        }
        if (Double.isInfinite(vitesseInitiale[0]) || Double.isInfinite(vitesseInitiale[1])) {
            throw new IllegalArgumentException("Une des composantes de la vitesse données est infinie.");
        }
        if (Outils.normeCarre(vitesseInitiale) > ClasseurUnite.creationUniteAbstraite(typeUnite).vitesseCarreMaximumLancement(type())) {
            vitesseInitiale = Outils.normalisation(vitesseInitiale, ClasseurUnite.creationUniteAbstraite(typeUnite).vitesseCarreMaximumLancement(type()));
        }
        if (!peutCreer(typeUnite)) {
            return false;
        }
        if ((energie() > Unite.creationUniteAbstraite(typeUnite).coutEnergetiqueCreation()) && (typeActionEnCours() == ACTION_AUCUNE)) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_LANCEMENT, new double[] { vitesseInitiale[0], vitesseInitiale[1], 1 }, typeUnite, ActionUnite.AUCUNE);
            return true;
        } else {
            return false;
        }
    }

    public boolean lancementProjectileVersPoint(int typeUnite, double[] pointCible) {
        if (Double.isNaN(pointCible[0]) || Double.isNaN(pointCible[1])) {
            throw new IllegalArgumentException("Une des composantes du point cible donné est NaN.");
        }
        if (Double.isInfinite(pointCible[0]) || Double.isInfinite(pointCible[1])) {
            throw new IllegalArgumentException("Une des composantes du point cible donné est infinie.");
        }
        if (!peutCreer(typeUnite)) {
            return false;
        }
        if ((energie() > Unite.creationUniteAbstraite(typeUnite).coutEnergetiqueCreation()) && (typeActionEnCours() == ACTION_AUCUNE)) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_LANCEMENT, new double[] { pointCible[0], pointCible[1], 0 }, typeUnite, ActionUnite.AUCUNE);
            return true;
        } else {
            return false;
        }
    }

    public boolean lancementUniteIntelligente(int typeUnite, double[] vitesseInitiale, Intelligence intelligence) {
        if (Double.isNaN(vitesseInitiale[0]) || Double.isNaN(vitesseInitiale[1])) {
            throw new IllegalArgumentException("Une des composantes de la vitesse données est NaN.");
        }
        if (Double.isInfinite(vitesseInitiale[0]) || Double.isInfinite(vitesseInitiale[1])) {
            throw new IllegalArgumentException("Une des composantes de la vitesse données est infinie.");
        }
        if (ClasseurUnite.estProjectile(typeUnite)) {
            throw new IllegalArgumentException("L'unite " + ClasseurUnite.conversionTypeNom(typeUnite) + " ne peut pas posseder d'intelligence.");
        }
        if (Outils.normeCarre(vitesseInitiale) > ClasseurUnite.creationUniteAbstraite(typeUnite).vitesseCarreMaximumLancement(type())) {
            vitesseInitiale = Outils.normalisation(vitesseInitiale, ClasseurUnite.creationUniteAbstraite(typeUnite).vitesseCarreMaximumLancement(type()));
        }
        if (!peutCreer(typeUnite)) {
            return false;
        }
        if ((energie() > Unite.creationUniteAbstraite(typeUnite).coutEnergetiqueCreation()) && (typeActionEnCours() == ACTION_AUCUNE)) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_LANCEMENT, new double[] { vitesseInitiale[0], vitesseInitiale[1], 1 }, typeUnite, ActionUnite.INTELLIGENCE_EXTERNE);
            nouvelleIntelligence = intelligence;
            return true;
        } else {
            return false;
        }
    }

    public boolean lancementUniteIntelligenteVersPoint(int typeUnite, double[] pointCible, Intelligence intelligence) {
        if (Double.isNaN(pointCible[0]) || Double.isNaN(pointCible[1])) {
            throw new IllegalArgumentException("Une des composantes du point cible donné est NaN.");
        }
        if (Double.isInfinite(pointCible[0]) || Double.isInfinite(pointCible[1])) {
            throw new IllegalArgumentException("Une des composantes du point cible donné est infinie.");
        }
        if (ClasseurUnite.estProjectile(typeUnite)) {
            throw new IllegalArgumentException("L'unite " + ClasseurUnite.conversionTypeNom(typeUnite) + " ne peut pas posseder d'intelligence.");
        }
        if (!peutCreer(typeUnite)) {
            return false;
        }
        if ((energie() > Unite.creationUniteAbstraite(typeUnite).coutEnergetiqueCreation()) && (typeActionEnCours() == ACTION_AUCUNE)) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_LANCEMENT, new double[] { pointCible[0], pointCible[1], 0 }, typeUnite, ActionUnite.INTELLIGENCE_EXTERNE);
            nouvelleIntelligence = intelligence;
            return true;
        } else {
            return false;
        }
    }

    public ControleurUniteReseau(InformateurUnite unite, InformateurTerrain informateurTerrain, long idIntelligence) {
        super(unite.equipe(), unite.pointsVie(), unite.energie(), unite.position(), unite.vitesse(), unite.id(), unite.type(), unite.actionEnCours(), unite.numeroObjectifEnCours(), unite.tousObjectifs(), informateurTerrain);
        actionEnCours = null;
        this.idIntelligence = idIntelligence;
    }

    public ActionUnite donneActionEnCours() {
        return actionEnCours;
    }

    /**
	 * Affiche le message <code>str</code> à l'écran. Les messages ne sont pas
	 * forcément affichés en directs, ils sont enregistrés lorsque la partie est
	 * sauvegardée. Lorsqu'un message est sauvegardé, l'IA émettrice et le
	 * numéro de tour sont enregistrés.
	 * 
	 * @param str
	 *            les message à afficher.
	 */
    public final void afficheMessage(String str) {
        GestionnaireSecurite.estThreadBloque();
    }

    /**
	 * Affiche l'objet <code>obj</code> à l'écran.
	 * 
	 * @param obj
	 *            l'objet à afficher.
	 * @see #afficheMessage(String)
	 */
    public final void afficheMessage(Object obj) {
        GestionnaireSecurite.estThreadBloque();
    }

    /**
	 * Affiche le nombre <code>number</code> à l'écran.
	 * 
	 * @param number
	 *            le nombre à afficher.
	 * @see #afficheMessage(String)
	 */
    public final void afficheMessage(Number number) {
        GestionnaireSecurite.estThreadBloque();
        afficheMessage(number);
    }

    /**
	 * Affiche le tableau <code>tab</code> à l'écran.
	 * 
	 * @param tab
	 *            le tableau à afficher.
	 * @see #afficheMessage(String)
	 */
    public final void afficheMessage(double[] tab) {
        GestionnaireSecurite.estThreadBloque();
        afficheMessage(java.util.Arrays.toString(tab));
    }

    public int typeActionEnCours() {
        if (actionEnCours == null) {
            return ACTION_AUCUNE;
        } else {
            return actionEnCours.typeAction();
        }
    }

    public Intelligence nouvelleIntelligence() {
        return nouvelleIntelligence;
    }

    /**
	 * Active le bouclier de l'unité.
	 * 
	 * @return <code>true</code> si le bouclier va être activé pour ce tour.
	 *         <code>flase</code> si le bouclier ne peut pas être activé, soit
	 *         parce qu'une autre action est déjà en cours, soit parce que
	 *         l'unité contrôlée n'a pas de bouclier.
	 */
    public boolean activationBouclier() {
        GestionnaireSecurite.estThreadBloque();
        if ((typeActionEnCours() == ACTION_AUCUNE) && (puissanceBouclier() != 0)) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_BOUCLIER, new double[] { 0, 0 }, -1, ActionUnite.AUCUNE);
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Freine ou arrête l'unité.
	 * 
	 * @return <code>true</code> si l'unité va effectivement être freinée.
	 *         <code>false</code> si l'unité ne peut pas être ralentie, soit
	 *         parce qu'une action est déjà en cours, soit parce qu'elle est
	 *         fixe, soit parce que freiné est sans effet sur l'unité.
	 */
    public boolean freinage() {
        GestionnaireSecurite.estThreadBloque();
        if ((typeActionEnCours() == ACTION_AUCUNE) && (!estFixe()) && (!Outils.estVecteurNul(vitesse()))) {
            if (Outils.normeCarre(vitesse()) <= puissanceArret()) {
                actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_FREIN, new double[] { 0, 0 }, -1, ActionUnite.AUCUNE);
                return true;
            } else {
                if (puissanceRalentissement() == 0) {
                    return false;
                } else {
                    actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_FREIN, new double[] { 0, 0 }, -1, ActionUnite.AUCUNE);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    /**
	 * Déplace l'unité vers ce point, en ligne droite, en utilisant
	 * l'accélération maximale.
	 * 
	 * @param destination
	 *            Le point de destination.
	 * @return <code>true</code> si le déplacement est possible.
	 *         <code>false</code> si le déplacement est impossible, soit parce
	 *         qu'une autre action est en cours, soit parce que l'unité est
	 *         fixe, soit parce que l'unité ne peut pas accélérée.
	 */
    public boolean accelerationVersPoint(double[] destination) {
        GestionnaireSecurite.estThreadBloque();
        return acceleration(Outils.normalisation(Outils.vecteur(position(), destination), puissanceAcceleration()));
    }

    /**
	 * Applique une accélération à l'unité. Si cette accélération est trop forte
	 * pour être supportée par l'unité, elle est remplacée par une accélération
	 * de norme maximale et de sens et direction identiques à ceux demandés.
	 * 
	 * @param acceleration
	 *            L'accélération demandée.
	 * @return <code>true</code> si le déplacement est possible.
	 *         <code>false</code> si le déplacement est impossible, soit parce
	 *         qu'une autre action est en cours, soit parce que l'unité est
	 *         fixe, soit parce que l'unité ne peut pas accélérée.
	 */
    public boolean acceleration(double[] acceleration) {
        if ((Double.isNaN(acceleration[0])) || (Double.isNaN(acceleration[1]))) {
            throw new IllegalStateException();
        }
        GestionnaireSecurite.estThreadBloque();
        if ((typeActionEnCours() == ACTION_AUCUNE) && (!estFixe()) && (puissanceAcceleration() != 0)) {
            if (Outils.normeCarre(acceleration) > puissanceAcceleration()) {
                acceleration = Outils.normalisation(acceleration, puissanceAcceleration());
            }
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_DEPLACEMENT, new double[] { acceleration[0], acceleration[1] }, -1, ActionUnite.AUCUNE);
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Détruit cette unité.
	 */
    public boolean autodestruction() {
        GestionnaireSecurite.estThreadBloque();
        if (typeActionEnCours() == ACTION_AUCUNE) {
            actionEnCours = new ActionUnite(id(), idIntelligence, ACTION_AUTODESTRUCTION, new double[] { 0, 0 }, -1, ActionUnite.AUCUNE);
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Annule la dernière action demandée.
	 */
    public void annuleActionEnregistree() {
        GestionnaireSecurite.estThreadBloque();
        actionEnCours = null;
    }
}
