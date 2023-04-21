package sjrd.tricktakinggame.rules.whist.contracts;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.whist.*;

/**
 * Contrat utilisé lorsqu'aucun contrat n'a pu être établi
 * @author sjrd
 */
public class NoContract extends WhistContract {

    /**
     * Crée le contrat
     * @param aRules Règles associées
     */
    public NoContract(WhistRules aRules, Player aPlayer) {
        super(aRules, "Aucun contrat", Suit.None, aPlayer);
    }

    /**
     * {@inheritDoc}
     */
    public void playTricks() throws CardGameException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeScores() throws CardGameException {
    }
}
