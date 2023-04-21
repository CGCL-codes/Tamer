package dr.moteur;

import javax.vecmath.Vector2f;

/**
 * Classe de creation d'un projectile de type BouleGlace pour la tour de glace
 *
 */
public class BouleGlace extends ProjectileAbstrait {

    public BouleGlace(int idJoueur, Vector2f position, AgentAbstrait destination, int puissance, GameEngine gameEngine, float aoe) {
        super(idJoueur, position, destination, puissance, gameEngine, aoe);
        this.vitesse = 150;
        AmeliorationAbstraite effet = new EffetStun();
        this.effet = effet;
    }
}
