package com.acervera.afw.utils.idfiscales;

import java.io.Serializable;

/**
 * Value Object que contendrá un Identificador Fiscal.
 * @author Angel Cervera Claudio (angel@acervera.com)
 *
 */
public class IdentificadorFiscalVO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5137391354764237670L;

    /**
	 * Identificador fiscal
	 */
    protected String idFiscal;

    /**
	 * Uno de los tipos de identificados fiscales permitido
	 * @see com.acervera.afw.utils.idfiscales.idfiscales.TiposIdFiscalesCte
	 */
    protected String tipoIdFiscal;
}
