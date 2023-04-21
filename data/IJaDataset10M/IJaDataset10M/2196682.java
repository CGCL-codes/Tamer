package javaframework.capadeaplicación.utilidades;

import base.InterfazEnumeración;

/**
 * Miembros públicos utilizables directamente por el programador al trabajar con la clase <code>Herrero</code>.
 *
 * <br/><br/>
 *
 * <b><u>Notas de diseño</u></b><br/>
 * <b>· Fecha de creación:</b><br/>
 * <b>· Revisiones:</b><br/><br/>
 * <b><u>Estado</u></b><br/>
 * <b>· Depurado:</b> -<br/>
 * <b>· Pruebas estructurales:</b> -<br/>
 * <b>· Pruebas funcionales:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw@yahoo.es) (c) 2011
 * @version JavaFramework.0.0.1.desktop-web.es
 * @version InterfazHerrero.0.0.1
 * @since JavaFramework.0.0.1.desktop-web.es
 * @see <a href=””></a>
 *
 */
public interface InterfazHerrero {

    /**
	 * Codificaciones de caracteres admitidas por la clase <code>Herrero</code>
	 */
    public static enum CodificacionesDeCaracteres implements InterfazEnumeración {

        ASCII_7BITS("US-ASCII"), ISO_8859_1("ISO-8859-1"), UTF_8("UTF-8"), UTF_16_BIG_ENDIAN("UTF-16BE"), UTF_16_LITTLE_ENDIAN("UTF-16LE"), UTF_16("UTF-16");

        private String codificación;

        private void setValor(String codificación) {
            this.codificación = codificación;
        }

        /**
		 * {@inheritDoc}
		 */
        public String getValor() {
            return this.codificación;
        }

        /**
		 * {@inheritDoc}
		 */
        public String getNombre() {
            return this.name();
        }

        /**
		 * A partir de un valor de constante enumerada, devuelve el nombre correspondiente.
		 *
		 * @param valor Valor de constante enumerada para el cual se quiere conocer su nombre asociado.
		 * @return Devuelve un objeto <code>CodificacionesDeCaracteres</code> que representa la constante asociada al valor introducido como parámetro
		 */
        public static CodificacionesDeCaracteres getNombre(final String valor) {
            for (CodificacionesDeCaracteres cc : CodificacionesDeCaracteres.values()) {
                if (cc.getValor().equals(valor)) {
                    return cc;
                }
            }
            return null;
        }

        private CodificacionesDeCaracteres(final String codificación) {
            this.setValor(codificación);
        }
    }

    /**
	 * @param cadena		Cadena a convetir a bytes.
	 * @param codificación	Codificación en la que se quiere obtener el array de bytes.
	 * @return				Devuelve un array de bytes equivalentes a la cadena pasada como parámetro.
	 *						Este array se encuentra	codificado de acuerdo con la codificación de
	 *						caracteres que se pasa como parámetro.
	 *
	 */
    byte[] getBytesDeString(final String cadena, final CodificacionesDeCaracteres codificación);

    /**
	 *
	 * @param plantilla		Cadena de texto original en la que se encuentran insertadas máscaras de sustitución.
	 * @param máscaras		Array de cadenas que contiene las máscaras utilizadas en el parámetro <code>plantilla</code>
	 * @param valores		Array de valores que sustituirán a las máscaras.
	 * @return				Devuelve una cadena igual al parámetro <code>plantilla</code> en la que se han sustituido
	 *						las cadenas identificadas como máscaras por sus valores correspondientes. Cada posición del
	 *						array de máscaras se corresponde con cada una de las posiciones del array de valores.
	 */
    String sustituirMáscaras(final String plantilla, final String[] máscaras, final String[] valores);
}
