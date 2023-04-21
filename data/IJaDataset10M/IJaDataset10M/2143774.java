package it.f2.juboxplayer;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Classe Eccezione lettura file XML
 * @author F. Ferraiuolo
 */
public class XMLErrorHandler implements ErrorHandler {

    /**
   * Metodo definizione Warning.
   * @throws org.xml.sax.SAXException
   * @param exception
   */
    public void warning(SAXParseException exception) throws SAXException {
        System.out.println("**Parsing warning**\n" + "  Linea:     " + exception.getLineNumber() + "\n" + "  URI:       " + exception.getSystemId() + "\n" + "  Messaggio: " + exception.getMessage());
        throw new SAXException(exception);
    }

    /**
   * Metodo definizione Error.
   * @throws org.xml.sax.SAXException
   * @param exception
   */
    public void error(SAXParseException exception) throws SAXException {
        System.out.println("**Parsing error**\n" + "  Linea:     " + exception.getLineNumber() + "\n" + "  URI:       " + exception.getSystemId() + "\n" + "  Messaggio: " + exception.getMessage());
        throw new SAXException(exception);
    }

    /**
   * Metodo definizione fatal error.
   * @throws org.xml.sax.SAXException
   * @param exception
   */
    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println("**Parsing fatal error**\n" + "  Linea:     " + exception.getLineNumber() + "\n" + "  URI:       " + exception.getSystemId() + "\n" + "  Messaggio: " + exception.getMessage());
        throw new SAXException(exception);
    }
}
