package org.aeg.spaceclient;

import org.openspaces.core.GigaSpace;

/**
 * La classe SpaceClient e' una classe che rappresenta un servizio che
 * necessita di accedere ad uno spazio di Gigaspaces.
 * Contiene solamente un riferimento ad uno spazio, ed i metodi get e set
 * per tale proprieta'
 */
public class SpaceClient {

    GigaSpace space;

    /**
	 * Metodo accessore della proprieta' "space"
	 * 
	 * @return valore della proprieta' "space"
	 */
    public GigaSpace getSpace() {
        return space;
    }

    /**
	 * Metodo modificatore della proprieta' "space"
	 * 
	 * @param valore della proprieta' "space"
	 */
    public void setSpace(GigaSpace space) {
        this.space = space;
    }
}
