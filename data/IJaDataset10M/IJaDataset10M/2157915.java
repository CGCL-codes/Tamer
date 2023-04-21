package it.paolomind.pwge.interfaces.bo;

import it.paolomind.pwge.interfaces.bo.generic.IVisualElementBO;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Ogetto che rappresenta una mappa del gioco. Una mappa è un insieme di celle,
 * ogni cella può essere a sua volta una mappa a cui accedere
 */
public interface IMapBO extends IVisualElementBO {

    /**
     * restituisce tutti i punti in cui sono presenti delle risorse.
     * @return insieme di coordinate
     */
    Point[] getResourcePoints();

    /**
     * restituisce le risorse contenute nel punto specificato.
     * @param point la coordinata della mappa
     * @return insieme di risorse
     */
    IResourceBO getResource(Point point);

    /**
     * @return la dimensione totale della mappa in pixel
     */
    Dimension getMapDimension();

    /**
     * restituisce tutti i punti in cui sono presenti delle risorse.
     * @param id identificativo della mappa
     * @param area l'area in cui prelevare le risorse
     * @return insieme di coordinate
     */
    Point[] getResourcePoints(Rectangle area);

    /**
     * @param xy coordinata della risorsa
     * @return true: se è una risorsa statica, ovvero non modificabile
     * false: se la risorsa può essere modificata durante il gioco.
     */
    boolean isStaticResource(Point xy);
}
