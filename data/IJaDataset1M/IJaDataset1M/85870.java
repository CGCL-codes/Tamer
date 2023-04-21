package org.fudaa.ebli.commun;

import java.util.EventListener;

/**
 *
 * @author deniger
 * @version $Id: EbliListeSelectionListener.java,v 1.6 2004-09-06 13:00:24 deniger Exp $
 */
public interface EbliListeSelectionListener extends EventListener {

    public void listeSelectionChanged(EbliListeSelectionEvent _e);
}
