package org.fudaa.ebli.calque;

/**
 * Un �v�nement <I>ZSelectionEvent</I>. Cet �venement est d�clench� par un calque de
 * s�lection et contient la liste des GrContour.
 *
 * @version      $Id: ZSelectionEvent.java,v 1.8 2006-07-13 13:35:45 deniger Exp $
 * @author       Bertrand Marchand
 */
public class ZSelectionEvent {

    private final ZCalqueAffichageDonneesInterface source_;

    public ZSelectionEvent(final ZCalqueAffichageDonneesInterface _source) {
        source_ = _source;
    }

    public ZCalqueAffichageDonneesInterface getSource() {
        return source_;
    }
}
