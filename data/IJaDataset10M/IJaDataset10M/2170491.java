package br.com.dcjtravelme.servico;

/**
 * @author Marcelo de Castro / Sergio Watanabe
 */
public class CodificadorUrl {

    /**
     * Creates a new instance of CodificadorUrl
     */
    public CodificadorUrl() {
    }

    public static String getUrlCodificada(String url) {
        return url.replace(' ', '+');
    }
}
