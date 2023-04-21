package org.sam.jogl;

/**
 * Interface que indica que las clases lo implementan,
 * contienen los datos que respresentan la geometría de un objeto.
 */
public interface Geometria extends Dibujable {

    /**
	 * Constante que indica que sólo se almacenará la referencia
	 * de los vectores, cuando se asignen los valores de los distintos
	 * componentes.<br/>
	 * En caso de no hacer uso de esta constante los vectores de
	 * copiarán completamente.
	 */
    public static final int POR_REFERENCIA = 0x0010;

    /**
	 * Constante que indica que los valores de los distintos
	 * componentes serán almacenados en buffers.<br/>
	 * El uso de esta constante es <b>incompatible</b> con el uso
	 * de {@link #POR_REFERENCIA}.
	 */
    public static final int USAR_BUFFERS = 0x0020;

    public static final int COORDENADAS_TEXTURA = 0x0040;

    /**
	 * Constante que indica que la {@code Geometria} contendrá
	 * colores RGB.
	 */
    public static final int COLOR_3 = 0x0080;

    /**
	 * Constante que indica que la {@code Geometria} contendrá
	 * colores RGBA.
	 */
    public static final int COLOR_4 = 0x0081;

    /**
	 * Constante que indica que la {@code Geometria} contendrá
	 * normales.
	 */
    public static final int NORMALES = 0x0100;

    public static final int ATRIBUTOS_VERTICES = 0x0200;
}
