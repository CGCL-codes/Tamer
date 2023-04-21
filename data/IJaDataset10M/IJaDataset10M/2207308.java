package it.paolomind.pwge.interfaces.bo;

import java.awt.Point;
import java.awt.Rectangle;
import it.paolomind.pwge.interfaces.bo.generic.IElementStatusBO;

public interface IMapStatusBO extends IElementStatusBO<IMapBO> {

    /**
     * Imposta la posizione di una risorsa sulla mappa. Se la posizione è già
     * occupata, non esegue il posizionamento e restituisce la risorsa in quella
     * coordinata
     * @param xy la posizione sulla mappa
     * @param res la risorsa da posizionare
     * @return restituisce la risorsa precedentemente nella posizione, null altrimenti
     */
    IResourceStatusBO setResource(Point xy, IResourceStatusBO res);

    /**
     * Rimuove la risorsa alle coordinate specificate.
     * @param xy le coordinate della risorsa
     * @return la risorsa rimossa
     */
    IResourceStatusBO removeResource(Point xy);

    /**
     * @return restituisce tutte le coordinate non vuote della mappa.
     */
    Point[] getResourcePoints();

    /**
     * @param xy la coordinata della risorsa
     * @return la risorsa in quel punto
     */
    IResourceStatusBO getResourceStatusBO(Point xy);

    /**
     * @return restituisce tutte le coordinate non vuote dell' area della mappa.
     */
    Point[] getResourcePoints(Rectangle area);
}
