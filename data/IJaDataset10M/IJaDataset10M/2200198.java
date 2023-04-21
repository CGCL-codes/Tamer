package ligueBaseball;

import java.sql.SQLException;

public class GestionFaitPartie {

    /** Attributes */
    private Connexion cx;

    private FaitPartie faitPartie;

    private Joueur joueur;

    private Equipe equipe;

    GestionFaitPartie(Connexion cx, FaitPartie faitPartie, Joueur joueur, Equipe equipe) throws LigueException {
        if (joueur.getCx() != equipe.getCx() || faitPartie.getCx() != joueur.getCx()) {
            throw new LigueException("Les instances de joueur , de equipe et de faitPartie n'utilisent pas la m�me connexion au serveur");
        }
        this.cx = joueur.getCx();
        this.joueur = joueur;
        this.equipe = equipe;
        this.faitPartie = faitPartie;
    }

    /**
     * Operation
     *
     * @param idJoueur
     * @param idEquipe
     * @param numero
     * @param datedebut
     * @param datefin
     */
    public void creerFaitPartie(int joueurId, int equipeId, int numero, String dateDebut) throws SQLException, LigueException, Exception {
        try {
            TupleJoueur tupleJoueur = joueur.getJoueur(joueurId);
            if (tupleJoueur == null) {
                throw new LigueException("Joueur inexistant: " + joueurId);
            }
            TupleEquipe tupleEquipe = equipe.getEquipe(equipeId);
            if (tupleEquipe == null) {
                throw new LigueException("Equipe inexistant: " + equipeId);
            }
            if (faitPartie.existe(joueurId, equipeId)) {
                throw new LigueException("La relation joueur " + joueurId + " equipe " + equipeId + " existe deja");
            }
            faitPartie.inserer(joueurId, equipeId, numero, dateDebut);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }
}
