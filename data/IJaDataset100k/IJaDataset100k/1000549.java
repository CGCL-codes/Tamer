package com.augcampos.core;

/**
 * <b>Class description:</b><br>
 * This class implements the abstract representation of Tecnico.
 *
 * @author Augusto Campos <b>augcampos@augcampos.pt</b>
 */
public class n501070324_Tecnico extends n501070324_Pessoa {

    /** Constant of Tipo Tecnico. **/
    public static char COD_TIPO = 'T';

    private static final long serialVersionUID = 1000L;

    /**
     * Constructs a new Tecnico handler.
     */
    public n501070324_Tecnico() {
        this.setTipo(COD_TIPO);
    }
}
