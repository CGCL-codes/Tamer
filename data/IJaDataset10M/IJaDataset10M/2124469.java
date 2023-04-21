package it.paolomind.pwge.interfaces.facade;

import it.paolomind.pwge.exceptions.ImageGameException;
import it.paolomind.pwge.interfaces.bo.IActionBO;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Interfaccia per gestire la visualizzazione dell' immagine delle risorse.
 * @author paolomind
 *
 */
public interface IActionImageFacade {

    /**
     * scrive su output l'immagine della risorsa ridimensionata.
     * @param res la risorsa di cui si vuole l'immagine
     * @param size la dimensione dell'immagine
     * @param out l'output su cui scrivere l'immagine
     * @param type tipo di immagine da visualizzare (dettaglio, anteprima, icona)
     * @param format formato dell'immagine(jpg, png, gif)
     * @throws IOException eccezione
     * @throws ImageGameException eccezione
     */
    void writeImage(IActionBO res, java.awt.Dimension size, String type, String format, OutputStream out) throws IOException, ImageGameException;
}
