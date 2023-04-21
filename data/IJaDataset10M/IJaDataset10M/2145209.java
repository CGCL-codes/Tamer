package org.openXpertya.process;

import org.openXpertya.util.Env;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class CacheReset extends SvrProcess {

    /**
     * Descripción de Método
     *
     */
    protected void prepare() {
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws java.lang.Exception
     */
    protected String doIt() throws java.lang.Exception {
        log.info("doIt");
        Env.reset(false);
        return "Memoria vaciada";
    }
}
