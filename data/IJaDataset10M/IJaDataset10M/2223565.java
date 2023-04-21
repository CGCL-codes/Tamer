package sjrd.tricktakinggame.rules;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;

/**
 * Contrat établi d'un jeu qui définit un atout
 * @author sjrd
 */
public class ContractWithTrump extends Contract {

    /**
     * Atout
     */
    private Suit trump;

    /**
     * Crée le contrat
     * @param aName {@inheritDoc}
     * @param aTrump Atout
     */
    public ContractWithTrump(String aName, Suit aTrump) {
        super(aName);
        trump = aTrump;
    }

    /**
     * Atout
     * @return Atout
     */
    public Suit getTrump() {
        return trump;
    }

    /**
     * Modifie l'atout
     * @param value Nouvel atout
     */
    protected void setTrump(Suit value) {
        trump = value;
    }
}
