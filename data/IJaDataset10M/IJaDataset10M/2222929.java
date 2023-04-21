package jwikidump.parser.procesadores.filtro;

import jwikidump.entidad.Pagina;

/**
 * Super Clase Abstracta que define los Metodos Basicos de cualquier filtro. De Esta super clase se desprende los Filtros Atomicos y los Compuestos, cuya diferencia es que los segundos pueden contener ilimitados Filtros Atomicos.
 *
 * @see Filtro Filtro (Atomico)
 * @see FiltroCompuesto
 * @author Gonzalo <contacto@vagobit.com.ar>
 */
public abstract class FiltroAbstract {

    /**
     * Constante que indica que si la Condicion que Evalua el Filtro da Verdadera, Retorna el valor que indica que NO debe procesarse dicha Pagina
     */
    public static final boolean MODO_FILTRA = true;

    /**
     * Constante que indica que si la Condicion que Evalua el Filtro da Verdadera, Retorna el valor que indica que DEBE procesarse dicha Pagina
     */
    public static final boolean MODO_NO_FILTRA = false;

    public abstract boolean filtrarPagina(Pagina pagina);

    public abstract void setModo(boolean modo);

    public abstract void cambiarModo();
}
