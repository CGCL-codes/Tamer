package reproductor;

/**
 * This class implements player events. 
 */
public class ReproductorEvento {

    public static final int UNKNOWN = -1;

    public static final int ABRIENDO = 0;

    public static final int ABIERTO = 1;

    public static final int REPRODUCIENDO = 2;

    public static final int PARADO = 3;

    public static final int PAUSADO = 4;

    public static final int RESUMIDO = 5;

    public static final int SALTANDO = 6;

    public static final int SALTADO = 7;

    public static final int EOM = 8;

    public static final int PAN = 9;

    public static final int GAIN = 10;

    private int codigo = UNKNOWN;

    private int posicion = -1;

    private double valor = -1.0;

    private Object source = null;

    private Object descripcion = null;

    /**
     * Constructor
     * @param source of the event
     * @param code of the envent
     * @param position optional stream position
     * @param value opitional control value
     * @param desc optional description
     */
    public ReproductorEvento(Object source, int code, int position, double value, Object desc) {
        this.valor = value;
        this.posicion = position;
        this.source = source;
        this.codigo = code;
        this.descripcion = desc;
    }

    /**
     * Return code of the event triggered.
     * @return
     */
    public int getCode() {
        return codigo;
    }

    /**
     * Return position in the stream when event occured.
     * @return
     */
    public int getPosition() {
        return posicion;
    }

    /**
     * Return value related to event triggered. 
     * @return
     */
    public double getValue() {
        return valor;
    }

    /**
     * Return description.
     * @return
     */
    public Object getDescription() {
        return descripcion;
    }

    public Object getSource() {
        return source;
    }

    public String toString() {
        if (codigo == ABIERTO) return "ABIERTO:" + posicion; else if (codigo == ABRIENDO) return "ABRIENDO:" + posicion + ":" + descripcion; else if (codigo == REPRODUCIENDO) return "REPRODUCIENDO:" + posicion; else if (codigo == PARADO) return "PARADO:" + posicion; else if (codigo == PAUSADO) return "PAUSADO:" + posicion; else if (codigo == RESUMIDO) return "RESUMIDO:" + posicion; else if (codigo == SALTANDO) return "SALTANDO:" + posicion; else if (codigo == SALTADO) return "SALTADO:" + posicion; else if (codigo == EOM) return "EOM:" + posicion; else if (codigo == PAN) return "PAN:" + valor; else if (codigo == GAIN) return "GAIN:" + valor; else return "DESCONOCIDO:" + posicion;
    }
}
