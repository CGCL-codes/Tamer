package com.iver.utiles.xmlViewer;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Interfaz que surte de datos al control XMLViewer
 *
 * @author Fernando Gonz�lez Cort�s
 */
public interface XMLContent {

    /**
     * Mediante este m�todo el control se registra como handler de los eventos
     * SAX disparados en el evento parse
     *
     * @param handler Handler de los eventos del m�todo parse que meter� toda
     *        la informaci�n en el control
     */
    public void setContentHandler(ContentHandler handler);

    /**
     * Debe de lanzar los eventos SAX del contenido XML que representa
     *
     * @throws SAXException Si se produce alg�n error relacionado con los
     *         eventos
     */
    public void parse() throws SAXException;
}
