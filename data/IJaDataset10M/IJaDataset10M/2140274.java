package simulador;

/**
 *
 * @author astrid
 */
public class Instruccion {

    private int[] _byte = new int[4];

    /**
     * crea una palabra de 4 bytes con un contenido determinado
     * @param uno valor del primer byte de la palabra (el menos significativo)
     * @param dos valor del segundo byte de la palabra
     * @param tres valor del tercer byte de la palabra
     * @param cuatro valor del cuarto byte de la palabra (el más significativo)
     */
    public Instruccion(int uno, int dos, int tres, int cuatro) {
        _byte[0] = uno;
        _byte[1] = dos;
        _byte[2] = tres;
        _byte[3] = cuatro;
    }

    /**
     *Obtiene un byte determinado dentro de la palabra
     * @param indice número del byte a devolver. 0 es el byte menos
     * significativo y 3 el más.
     * @return el byte número <code>indice</code>
     * @exception ArrayIndexOutOfBoundsException imprime un mensaje
     * en caso de que <code>indice</code> no esté en el rango [0, 4] y asume
     * <code>indice</code> igual a cero.
     */
    public int getByte(int indice) {
        try {
            return _byte[indice];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Indice fuera del arreglo. Se asume 0");
            return _byte[0];
        }
    }

    /**
     * Asigna un valor a un byte determinado dentro de la palabra
     * @param indice número del byte a modificar
     * @param valor valor que se asignará al byte
     * @exception ArrayIndexOutOfBoundsException imprime un mensaje
     * en caso de que <code>indice</code> no esté en el rango [0, 4] y asume
     * <code>indice</code> igual a cero.
     */
    public void setByte(int indice, int valor) {
        try {
            _byte[indice] = valor;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Indice fuera del arreglo. Se asume 0");
            _byte[0] = valor;
        }
    }

    /**
     * Devuelve una representación como entero del objeto invocador
     * @return una representación como entero del objeto invocador
     */
    public int toInt() {
        int resultado = 0;
        resultado = 1000 * _byte[3];
        resultado += 100 * _byte[2];
        resultado += 10 * _byte[1];
        resultado += _byte[0];
        return resultado;
    }
}
