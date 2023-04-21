package jwikidump.parser.procesadores.filtro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jwikidump.entidad.Pagina;

/**
 * Filtra si la condicion indicada (Expresion Regular) concuerda con el Titulo de la Pagina
 * @author Gonzalo <contacto@vagobit.com.ar>
 */
public class FiltrarPorTitulo extends Filtro {

    /**
     * Constante que indica si es Sensible al Caso: en este caso se distinguen el uso de mayusculas y minusculas
     * @see #FiltrarPorTitulo(java.lang.String, boolean, boolean)
     */
    public static final boolean SENSIBLE_AL_CASO = true;

    /**
     * Constante que indica que NO es Sensible al Caso.
     * @see #FiltrarPorTitulo(java.lang.String, boolean, boolean) 
     */
    public static final boolean NO_SENSIBLE_AL_CASO = false;

    private String cadena;

    private Pattern patron;

    /**
     * Crea un Filtro Default el se setea en base al Modo Filtra y la Condicion (ER) indicada. Por default es Sensible al caso
     * @param condicion Expresion Regular indicada
     * @see #SENSIBLE_AL_CASO
     */
    public FiltrarPorTitulo(String condicion) {
        super();
        inicializar(condicion, true);
    }

    /**
     * Crea un Filtro en base a la Condicion indicada (ER), y el Modo de filtrado. Nuevamente por Default es Sensible al Caso
     * @param condicion Expresion Regular indicada
     * @param modo {@link #MODO_FILTRA Filtra} o bien {@link #MODO_NO_FILTRA No Filtra}
     * @see #SENSIBLE_AL_CASO
     */
    public FiltrarPorTitulo(String condicion, boolean modo) {
        super(modo);
        inicializar(condicion, true);
    }

    /**
     * Crea un Filtro en base a la Condicion indicada (ER),  el Modo de filtrado y si es Sencible al Caso, osea al uso de Mayusculas y Minusculas
     * @param condicion Expresion Regular indicada
     * @param modo {@link #MODO_FILTRA Filtra} o bien {@link #MODO_NO_FILTRA No Filtra}
     * @param sensibleACaso true: lo convierte sensible al caso, false todo lo contrario. Se recomienda usar las Constantes {@link #SENSIBLE_AL_CASO SENSIBLE_AL_CASO} y {@link #NO_SENSIBLE_AL_CASO NO_SENSIBLE_AL_CASO}
     */
    public FiltrarPorTitulo(String condicion, boolean modo, boolean sensibleAlCaso) {
        super(modo);
        inicializar(condicion, sensibleAlCaso);
    }

    @Override
    protected boolean condicionDeFiltrado(Pagina pagina) {
        Matcher m = this.patron.matcher(pagina.getTitulo());
        return m.find();
    }

    private void inicializar(String condicion, boolean sencibleAlCaso) {
        this.cadena = condicion;
        if (!sencibleAlCaso) {
            this.patron = Pattern.compile(condicion, Pattern.CASE_INSENSITIVE);
        } else {
            this.patron = Pattern.compile(condicion);
        }
    }
}
