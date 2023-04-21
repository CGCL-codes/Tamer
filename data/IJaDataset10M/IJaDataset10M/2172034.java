package org.sistema.cs.wp.wpaq;

/**
 * Constantes de la clase {@code WPConcepto}.
 *
 * @author	Juan D&iacute;az
 * @since	29-mar-2011
 * @version 1.1 01-ago-2011
 *
 * @see WPConcepto
 */
public interface WPConceptoConstante {

    /**
    * Atributos de la clase {@code WPConcept}.
    *
    * @see WPConcept
    */
    public interface WPConceptAttribute {

        public static String CODE = "wpcCode";

        /**
       * Indica si el concepto es exento de I.V.A.
       *
       * <ul>
       * <li>
       * 0 - no exento
       * </li>
       * <li>
       * 1 - exento
       * </li>
       * </ul>
       */
        public static String EXENTO_IVA = "wpcExentoIVA";

        public static String ID = "idConcepto";

        /**
       * Descripci&oacute;n del concepto
       */
        public static String NOMBRE = "nombre";

        /**
       * Tipo del concepto
       */
        public static String TIPO = "tipo";
    }

    /**
    * Constantes con conceptos predefinidos.
    * 
    * @todo Estos valores tend&aacute;n que definirse en el archivo de configuraci&oacute;n del m&oacute;dulo,
    *       con sus respectivas constantes y leerse durante la carga del m&oacute;dulo.
    */
    public interface WPConceptValue {

        public static String CODE_NON_IVA_TAXABLE = "2";

        /**
       * Prevalidaci&oacute;n
       */
        public static int PRV = 82;

        /**
       * Derecho de Tr&aacute;mite Aduanero
       */
        public static int DTA = 83;

        public static byte EXEMPT = 1;

        /**
       * Producto
       */
        public static byte TYPE_PRODUCT = 1;

        /**
       * Servicio
       */
        public static byte TYPE_SERVICE = 3;
    }
}
