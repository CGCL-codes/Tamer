package br.com.framework;

import java.util.Properties;

/**
 * 
 * @author Thiago Luiz Rodrigues
 *
 */
public class EtexProperties extends Properties {

    private static final long serialVersionUID = -7746309646392646309L;

    /**
	 * 
	 * 
	 * NOME DO ARQUIVO DE CONFIGURA��O PADR�O
	 *  
	 * 
	 */
    public static final String CONFIGURATION_PROPERTIES = "configuration.properties";

    public static final String WEB_INF_CONFIGURATION_PROPERTIES = "/WEB-INF/configuration.properties";

    public static final String ETEX_PACKAGE_PERSISTENT = "etex.package.persistent";

    public static final String META_INF = "META-INF";

    /**
	 * 
	 *  PROPIEDADES DE CONTROLE DO DESENVOLVIMENTO
	 *  
	 */
    public static final boolean PRODUCAO = false;
}
