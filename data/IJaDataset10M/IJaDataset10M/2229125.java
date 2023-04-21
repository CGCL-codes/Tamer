package com.yubarta.docman;

import java.util.Properties;
import org.openrdf.model.Graph;

public interface DocManConfig {

    public String getDocStoragePath();

    public String getBaseUrl();

    public String getBaseUser();

    public String getBaseGroup();

    /**
	 * Nombre de objeto de sesión en que se almacena docman
	 */
    public String getDocmanSessionLabel();

    /**
	 * El repositorio sesame se accede a través de un servicio java
	 * o a través de un servidor HTTP remoto.
	 */
    public boolean isSesameLocal();

    /**
	 * Se guardan los datos del repositorio en una cache o no
	 **/
    public boolean useCache();

    public Properties getCacheProperties() throws java.io.IOException;

    /***
	 * fichero de configuracion si el sesame es local
	 * @see #isSesameLocal()
	 */
    public String getSesameConfigPath();

    /***
	 * URL de sesame si el sesame es remoto
	 * @see #isSesameLocal()
	 */
    public String getSesameUrl();

    public String getMdRepName();

    public String getSesameUser();

    public String getSesamePass();

    public Graph getSchemaGraph() throws DocManFailure;

    /**
	 * devuelve el path absoluto del directorio a partir del que resolvemos
	 * referencias relativas (al esquema, los xforms, ...)
	 */
    public String getDocRoot();
}
