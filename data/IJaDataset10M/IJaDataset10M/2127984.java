package jwebdownloader.modelo.parsers;

import java.io.IOException;
import java.util.HashSet;
import jwebdownloader.control.Constantes;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

public abstract class AbstractParser implements Parser {

    protected String sitioWeb;

    protected HashSet<String> linksEncontrados;

    protected static Logger log = null;

    protected HttpClient clienteHttp;

    public void setClienteHttp(HttpClient clienteHttp) {
        this.clienteHttp = clienteHttp;
    }

    public String getContentType(String link) throws ContentTypeException, IOException {
        MultiThreadedHttpConnectionManager manager = (MultiThreadedHttpConnectionManager) clienteHttp.getHttpConnectionManager();
        manager.closeIdleConnections(5000);
        manager.deleteClosedConnections();
        HeadMethod request = new HeadMethod();
        request.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
        try {
            request.setURI(new URI(link, true));
        } catch (URIException e) {
            log.info("La URI " + link + " .No está escapada. La escapamos");
            request.setURI(new URI(link, false));
        }
        int codigo;
        try {
            codigo = clienteHttp.executeMethod(request);
        } catch (Exception e) {
            request.releaseConnection();
            throw new ContentTypeException("Error en la conexión de " + link + ":" + e.getMessage());
        }
        request.releaseConnection();
        if (codigo != 200) {
            throw new ContentTypeException("No se ha podido comprobar el tipo de contenido de " + link + ". Código de error " + codigo);
        }
        String type = request.getResponseHeader("content-type").getValue();
        return type;
    }

    protected boolean isParseable(String link) {
        try {
            String type = getContentType(link);
            if (type != null && (type.startsWith(Constantes.MIME_HTML) || type.startsWith(Constantes.MIME_FLASH) || type.startsWith(Constantes.MIME_CSS) || type.startsWith(Constantes.MIME_JS))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("No se ha podido averiguar si " + link + " es un contenido parseable", e);
            return false;
        }
    }

    public String makeEnlaceLocal(String enlace, String current) {
        int i;
        int j;
        String ret;
        log.debug("Make enlace Local con estos parametros: Enlace: " + enlace + "  ; current: " + current + "  ; SitioWeb: " + sitioWeb);
        try {
            String hostEnlace = "";
            String hostProyecto = "";
            try {
                hostEnlace = new URI(enlace, true).getHost();
            } catch (URIException e) {
                hostEnlace = new URI(enlace, false).getHost();
            }
            try {
                hostProyecto = new URI(sitioWeb, true).getHost();
            } catch (URIException e) {
                hostProyecto = new URI(sitioWeb, false).getHost();
            }
            if (hostProyecto.contains(hostEnlace) || hostEnlace.contains(hostProyecto)) {
                enlace = enlace.replaceAll(hostEnlace, hostProyecto);
            }
        } catch (Exception e) {
        }
        if (enlace.equals(sitioWeb) || (!sitioWeb.endsWith("/") && enlace.equals(sitioWeb + "/"))) {
            ret = "index.html";
        } else if (enlace.startsWith(sitioWeb) && (enlace.length() > sitioWeb.length())) {
            ret = enlace.substring(sitioWeb.length() + 1);
        } else {
            ret = enlace;
        }
        try {
            if (ret.endsWith("/") && isParseable(enlace)) {
                ret = ret + "index.html";
            } else if (ret.indexOf(".") == -1 && isParseable(enlace)) {
                ret = ret + "/index.html";
            }
        } catch (Exception e) {
            log.info("Error al comprobar si el enlace " + enlace + " se trata de la página inicial de un directorio", e);
        }
        if ((null != current) && enlace.startsWith(sitioWeb) && (current.length() > sitioWeb.length())) {
            current = current.substring(sitioWeb.length() + 1);
            i = 0;
            while (-1 != (j = current.indexOf('/', i))) {
                ret = "../" + ret;
                i = j + 1;
            }
        }
        log.debug("MakeEnlaceLocal devuelve: " + Utilidades.sustituyeCaracteresNoValidos(ret));
        return (Utilidades.sustituyeCaracteresNoValidos(ret));
    }

    public HashSet<String> getLinksEncontrados() {
        return linksEncontrados;
    }

    public abstract void parseaCodigo(boolean modificarCodigo);

    public abstract Object getCodigoModificado();
}
