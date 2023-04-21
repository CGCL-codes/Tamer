package org.itsnat.impl.core.browser;

/**
 * Soportado al menos la versi�n 4
 *
 * Derivamos de BrowserOpera9 porque el motor en el servidor es a partir de Opera 9
 *
 * Limitaciones:
 *  http://dev.opera.com/articles/view/javascript-support-in-opera-mini-4/
 *  http://dev.opera.com/articles/view/evolving-the-internet-on-your-phone-des/
 *
 * Limitaciones detectadas por mi:
 * - La propiedad "data" de los nodos Text es s�lo lectura aunque no da error al intentar cambiarla
 *
 * Depurar JavaScript con Opera Mini

   En Opera Mini el JavaScript se ejecuta en un servidor de Opera.
   Los alert() funcionan pero s�lo puede ejecutarse uno, pues los scripts si tardan mucho se paran,
   cuando hay un alert el servidor de Opera da por acabado el script.
   Por ello es mejor depurar sacando visualmente informaci�n a la pantalla, por ejemplo:

    document.body.appendChild(document.createTextNode(TEXTO + " "));

 * @author jmarranz
 */
public class BrowserOperaMini extends BrowserOpera9 {

    /** Creates a new instance of BrowserOperaMini */
    public BrowserOperaMini(String userAgent) {
        super(userAgent);
        this.browserSubType = OPERA_MINI;
    }

    public static boolean isOperaMini(String userAgent) {
        return (userAgent.indexOf("Opera Mini") != -1);
    }

    public boolean isMobile() {
        return true;
    }

    public boolean isCachedBackForwardExecutedScripts() {
        return false;
    }

    public boolean hasHTMLFormAutoFillPostLoadEvent() {
        return false;
    }
}
