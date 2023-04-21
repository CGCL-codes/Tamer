package jwebdownloader.modelo.parsers.javascript;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jwebdownloader.control.Propiedades;
import jwebdownloader.modelo.parsers.AbstractParser;
import jwebdownloader.modelo.parsers.Utilidades;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class JavaScriptParser extends AbstractParser {

    private String codigoFuente;

    private String urlPagina;

    private static final int MAX_BUFFER_SIZE = 1024;

    private boolean modificarCodigo;

    public JavaScriptParser(InputStream stream, String sitioWeb, String urlPagina, int tamaño) {
        this.sitioWeb = sitioWeb;
        this.urlPagina = urlPagina;
        this.linksEncontrados = new HashSet<String>();
        log = Logger.getLogger("JavaScriptParser");
        StringBuffer codigo = new StringBuffer();
        try {
            int bytesDescargados = 0;
            while (true) {
                byte buffer[];
                if (tamaño - bytesDescargados > MAX_BUFFER_SIZE || tamaño == -1) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[tamaño - bytesDescargados];
                }
                int read = stream.read(buffer);
                if (read == -1) {
                    break;
                }
                bytesDescargados += read;
                codigo.append(new String(buffer));
            }
            codigoFuente = codigo.toString();
        } catch (Exception e) {
            log.error("Error al descargar el archivo " + urlPagina, e);
        }
    }

    public JavaScriptParser(String codigoFuente, String sitioWeb, String urlPagina) {
        this.codigoFuente = codigoFuente;
        this.sitioWeb = sitioWeb;
        this.urlPagina = urlPagina;
        this.linksEncontrados = new HashSet<String>();
    }

    public void parseaCodigo(boolean modificarCodigo) {
        this.modificarCodigo = modificarCodigo;
        log.debug(codigoFuente);
        Vector<String> patrones = Propiedades.getPatronesJS();
        for (String patron : patrones) {
            log.debug("Buscando links con el patron: " + patron);
            encuentraLinksPatron(patron);
        }
    }

    private void encuentraLinksPatron(String patron) {
        Matcher m = Pattern.compile(patron).matcher(codigoFuente);
        while (m.find()) {
            try {
                String enlace = m.group(1);
                log.debug("Encontrado enlace: " + enlace);
                linksEncontrados.add(enlace);
                if (modificarCodigo) {
                    String enlaceModificado = modificaAtributo(enlace);
                    codigoFuente = StringUtils.replace(codigoFuente, enlace, enlaceModificado);
                }
            } catch (Exception e) {
                log.error("Error en JavaScriptParser->", e);
            }
        }
    }

    private String modificaAtributo(String cadena) {
        if (cadena.startsWith("http://")) {
            return Utilidades.decodificaURI(makeEnlaceLocal(cadena, urlPagina));
        } else if (cadena.startsWith("/")) {
            return Utilidades.decodificaURI(makeEnlaceLocal(sitioWeb + cadena, urlPagina));
        } else {
            return Utilidades.sustituyeCaracteresNoValidos(Utilidades.decodificaURI(cadena));
        }
    }

    public String getCodigoModificado() {
        return codigoFuente;
    }
}
