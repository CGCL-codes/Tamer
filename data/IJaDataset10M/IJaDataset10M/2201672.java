package edu.tfh.s2.ehkbot.gerete.aktoren.real;

import edu.tfh.s2.ehkbot.gerete.aktoren.BiStateAktor.BiStateAktorState;

/**
 * Implementiert den Zustand Aktiviert und Real für den BiStateAktor.
 * @author s2zehn
 *
 */
public class BiStateAktorReal implements BiStateAktorState {

    /**
	 * @see edu.tfh.s2.ehkbot.gerete.aktoren.BiStateAktor.BiStateAktorState#aktiviere()
	 */
    @Override
    public void aktiviere() {
    }

    /**
	 * @see edu.tfh.s2.ehkbot.gerete.aktoren.BiStateAktor.BiStateAktorState#deaktivier()
	 */
    @Override
    public void deaktivier() {
    }

    /**
	 * @see edu.tfh.s2.ehkbot.gerete.aktoren.BiStateAktor.BiStateAktorState#istAktiviert()
	 */
    @Override
    public boolean istAktiviert() {
        return false;
    }

    /**
	 * @see edu.tfh.s2.ehkbot.gerete.aktoren.BiStateAktor.BiStateAktorState#testeFunktion()
	 */
    @Override
    public boolean testeFunktion() {
        return false;
    }
}
