package org.sistema.cs.wp.cpaq;

/**
 * Constantes de la clase {@code CPEntrada}.
 *
 * @author	Juan D&iacute;az
 * @since	06-abr-2011
 * @version	1.1 01-ago-2011
 *
 * @see CPEntrada
 */
public interface CPEntradaC {

    /**
    * Atributos de la clase {@code CPEntrada}.
    *
    * @see CPEntrada
    */
    public interface CPEntryAttribute {

        public static String ID = "id";

        /**
		 * C&oacute;digo asignado a la entrada por Adminpaq.
		 */
        public static String ID_APAQ = "idAP";

        /**
		 * Raz&oacute;n social.
		 */
        public static String NAME = "name";

        /**
		 * Tipo (beneficiario, pagador o ambos).
		 */
        public static String TYPE = "type";
    }
}
