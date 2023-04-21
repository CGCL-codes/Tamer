package edu.jrous.protocol;

import edu.jrous.core.AbstractConstantes;

/**
 * <p>Valores que se van a utilizar </p>
 * <p>especificamente para este package</p>
 * 
 * @author Manuel Sako
 * @see AbstractConstantes
 * @since version 1.0
 */
public class Constantes extends AbstractConstantes {

    /**
	 * <p>Numero de serie a representar</p>
	 * <p></p>
	 * <p></p>
	 */
    private static final long serialVersionUID = -4841925075884935007L;

    /**
	 * <p>Valores Minimo que puede ser una IP o Mask</p>
	 * 
	 * 
	 * @since version 1.0
	 */
    public static final int limitMinGeneral = 0;

    /**
	 * <p>Represents ...</p>
	 * 
	 */
    public static final int limitMaxGeneral = 255;

    /**
	 * <p>Represents ...</p>
	 * 
	 */
    public static final int redClaseA_begin = 1;

    /**
	 * <p>Represents ...</p>
	 * 
	 */
    public static final int redClaseA_end = 126;

    /**
	 * <p>Represents ...</p>
	 * 
	 */
    public static final int redClaseB_begin = 128;

    /**
	 * <p>Represents ...</p>
	 * 
	 */
    public static final int redClaseB_end = 191;

    /**
	 * <p>Represents ...</p>
	 * 
	 */
    public static final int redClaseC_begin = 192;

    /**
	 * <p>Represents ...</p>
	 * 
	 */
    public static final int redClaseC_end = 223;

    /**
     * <p>Represents ...</p>
     */
    public static final String IP_DEFAULT = "0.0.0.0";

    /**
     * <p>Represents ...</p>
     */
    public static final String MASK_DEFAULT = "255.255.255.255";

    /**
     * Las mascaras valiidas para el proceso
     * @return
     */
    public static final int[] getMASK() {
        int[] vec = new int[] { 0, 128, 192, 224, 240, 248, 252, 254, 255 };
        return vec;
    }

    public static final String ERROR_IP_REDCLASE = "   Direcci�n IP no pertenece a una red valida\n \n ";

    public static final String ERROR_IP_REDCLASE_EJ = "   Explicacion:\n              Red Clase A: 1.x.x.x - 126.x.x.x\n              Red Clase B: 128.x.x.x - 191.x.x.x\n              Red Clase C: 192.x.x.x - 223.x.x.x\n              Otras clases uso exclusivo para internet\n\n";

    public static final String ERROR_IP_0_255 = "   Direcci�n IP no valido �ltimo d�gito reservado\n \n";

    public static final String ERROR_IP_0_255_EJ = "   Explicaci�n:\n              El �ltimo d�gito reservado para broadcast o network\n \n";

    public static final String ERROR_ADDRESS_NUMBER = "   Direcci�n d�gitos alfanum�ricos no validos\n  \n";

    public static final String ERROR_ADDRESS_NUMBER_EJ = "   Explicaci�n:\n              Ha usado caracteres no num�ricos en el �ltimo d�gito\n \n";

    public static final String ERROR_ADDRESS_NUMBER_EJ2 = "   Explicaci�n:\n              Ha usado caracteres no num�ricos para esta direcci�n\n \n";

    public static final String ERROR_IP_FUERA0_255 = "   Direcci�n IP no valido ultimo d�gito fuera de rango\n  \n";

    public static final String ERROR_IP_FUERA0_255_EJ = "   Explicaci�n:\n              Valores fuera del rango permitido entre 0 a 255\n \n";

    public static final String ERROR_MASK_NODIGITO = "   Mascara contiene d�gitos alfanum�ricos no soportados \n  \n";

    public static final String ERROR_MASK_NODIGITO_EJ = "   Explicaci�n:\n              No a usado valores tales como 0, 128, 192, 224, 240, 248, 252, 254, 255 \n";

    public static final String ERROR_MASK_DIGITO = "   Mascara contiene d�gitos alfanum�ricos no soportados \n  \n";

    public static final String ERROR_MASK_1DIGITO_EJ = "   Explicacion:\n              El primer par de d�gitos no debe de ser igual a 0 \n \n";

    public static final String ERROR_MASK_3DIGITO_EJ = "   Explicacion:\n              El ultimo par de d�gitos no debe de ser igual a 255 \n \n";

    public static final String ERROR_MASK_AFTER = "   Mascara contiene d�gitos no validos en su composici�n\n \n";

    public static final String ERROR_MASK_AFTER_EJ = "   Explicaci�n:\n              Valores menores de 255 obliga que d�gitos siguientes sean igual a 0 \n \n";

    public static final String ERROR_ADDRSS_DIGITOS = "   Falta d�gitos para ser Valido \n  \n";

    public static final String ERROR_ADDRSS_DIGITOS_EJ = "   Explicaci�n:\n              N�mero faltantes para formar una direcci�n IP (x.x.x.x) \n \n";
}
