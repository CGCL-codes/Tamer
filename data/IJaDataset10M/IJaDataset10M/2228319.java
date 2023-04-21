package tabla_de_simbolos.simbolo;

import java.util.ArrayList;
import semantico.TipoSemantico;
import tabla_de_simbolos.TablaDeSimbolos;

/**
 * Clase que gestiona la informacion relativa a un identificador de la TS que
 * representa un tipo ARRAY.
 * 
 * @author Ivan Munsuri Ibanez
 */
public class InfoSimboloArray extends InfoSimbolo {

    /**
     * Numero de dimensiones del array.
     */
    private int _numeroDimensiones;

    /**
     * Array con el numero de componentes por cada dimension del mismo.
     */
    private int[] _numeroComponentesPorDimension;

    private ArrayList<ArrayList<String>> _rango;

    /**
     * Constructor por defecto de la clase InfoSimboloArray.
     */
    public InfoSimboloArray(int numeroDimensiones, ArrayList<TipoSemantico> tipoSemantico) {
        super._tipoSemantico = tipoSemantico;
        _numeroDimensiones = numeroDimensiones;
        _numeroComponentesPorDimension = new int[0];
    }

    public InfoSimboloArray(int numeroDimensiones, ArrayList<ArrayList<String>> rango, ArrayList<TipoSemantico> tipoSemantico) {
        super._tipoSemantico = tipoSemantico;
        _numeroDimensiones = numeroDimensiones;
        _rango = rango;
    }

    /**
     * Devuelve el numero de dimensiones del array.
     * 
     * @return Devuelve un entero.
     */
    public int getNumeroDimensiones() {
        return _numeroDimensiones;
    }

    /**
     * Establece el numero de componentes por cada dimension del array.
     *
     * @param numeroComponentesPorDimension array de enteros.
     */
    public void setNumeroComponentesPorDimension(int[] numeroComponentesPorDimension) {
        _numeroComponentesPorDimension = numeroComponentesPorDimension;
    }

    /**
     * Devuelve el numero de componentes por cada dimension del array.
     * 
     * @return Devuelve un array de enteros.
     */
    public int[] getNumeroComponentesPorDimension() {
        return _numeroComponentesPorDimension;
    }

    public ArrayList<ArrayList<String>> getRangos() {
        return _rango;
    }

    /**
     * Devuelve el numero de componentes de cierta dimension del array.
     *
     * @return Devuelve un enteros.
     */
    public int getNumeroComponentesDimension(int index) {
        return _numeroComponentesPorDimension[index];
    }

    /**TODO:
     * @see Object.toString().
     */
    @Override
    public String toString() {
        String cadena = "";
        cadena = "\tARRAY:" + _tipoSemantico;
        cadena += "\tnDim: " + getNumeroDimensiones() + "\n";
        if (_rango.size() != 0) {
            for (int i = getNumeroDimensiones() - 1; i >= 0; i--) {
                cadena += "\ttipoDim: [" + _rango.get(i).get(0) + "]\t";
                cadena += "Inicio: " + _rango.get(i).get(2) + ", Final: " + _rango.get(i).get(1) + "\n";
            }
        }
        return cadena;
    }

    /**
     * Recupera el tipo de este simbolo, tipo de simbolo, <b>NO</b> semantico
     * @return Devuelve el tipo del simbolo.
     */
    public TipoSimbolo getTipoSimbolo() {
        return TipoSimbolo.ARRAY;
    }
}
