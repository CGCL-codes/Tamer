package simulador;

/**
 *
 * @author Fabián Guevara
 * @date 2-6-9
 * Representa una palabra de la máquina Mips, formada por NUM_BYTES_PALABRA bytes.
 * Si una palabra representa una instrucción, usa todos los bytes. Si
 * representa un dato en memoria, toda la información está en el byte menos significativo
 * (el byte cero).
 */
public class Palabra {

    /**
     * los cuatro bytes que conforman la palabra,
     * desde _byte[0] (más significativo)
     * hasta _byte[3] (menos significativo)
     */
    private int[] _byte;

    /**Número de bytes que conforman una palabra*/
    public static final int NUM_BYTES_PALABRA = 4;

    /**
     * ciclo de reloj en el que la instruccion entra a CPU
     */
    private int cicloInicial;

    /**
     * tiempo de ejecucion en CPU de la instruccion
     * incluye los posiblers retrasos que pueden existir
     * para una instruccion
     */
    private int tiempoCPU;

    /**
     * identificacion del ciclo que corre en el nucleo;
     */
    private int identificador;

    /** Instruccion que se ejecuto antes de la actual*/
    private Palabra instruccionAnterior;

    /** Instruccion que se ejecuto 2 ciclos de reloj antes que la actual*/
    private Palabra instruccionTrasanterior;

    /**
     * crea una palabra vacía que consta de 4 bytes
     */
    public Palabra() {
        _byte = new int[NUM_BYTES_PALABRA];
        for (int i = 0; i < NUM_BYTES_PALABRA; i++) {
            _byte[i] = -1;
        }
        this.instruccionAnterior = null;
        this.instruccionTrasanterior = null;
        this.tiempoCPU = 5;
    }

    public String toString() {
        String palabra = "" + this.toInt();
        return palabra;
    }

    /**
     * Sobrecarga del constructor
     * @para valor a guardar
     */
    public Palabra(int valor) {
        _byte = new int[NUM_BYTES_PALABRA];
        _byte[0] = valor;
        _byte[1] = 0;
        _byte[2] = 0;
        _byte[3] = 0;
        this.instruccionAnterior = null;
        this.instruccionTrasanterior = null;
        this.tiempoCPU = 5;
    }

    /**Constructor. Crea una palabra con un contenido dado
     * @param b0 primer byte de la palabra (el más significativo)
     * @param b1 segundo byte de la palabra
     * @param b2 tercer byte de la palabra
     * @param b3 cuarto byte de la palabra (el menos significativo)
     */
    public Palabra(int b0, int b1, int b2, int b3) {
        _byte = new int[4];
        _byte[0] = b0;
        _byte[1] = b1;
        _byte[2] = b2;
        _byte[3] = b3;
        this.instruccionAnterior = null;
        this.instruccionTrasanterior = null;
        this.tiempoCPU = 5;
    }

    public void imprimir() {
        for (int i = 0; i < NUM_BYTES_PALABRA; i++) {
            System.out.print(" " + _byte[i]);
        }
    }

    /**
     *Obtiene un byte determinado dentro de la palabra
     * @param indice número del byte a devolver. 3 es el byte menos
     * significativo y 0 el más.
     * @return el byte número <code>indice</code>
     * @exception ArrayIndexOutOfBoundsException si el índice es mayor o igual que
     * NUM_BYTES_PALABRA o menor que 0
     */
    public int getByte(int indice) {
        if (indice >= NUM_BYTES_PALABRA || indice < 0) {
            throw new ArrayIndexOutOfBoundsException("Indice fuera de la palabra");
        }
        return _byte[indice];
    }

    /**
     * Asigna un valor a un byte determinado dentro de la palabra
     * @param indice número del byte a modificar
     * @param valor valor que se asignará al byte
     * @exception ArrayIndexOutOfBoundsException si el índice es mayor o igual que
     * NUM_BYTES_PALABRA o menor que 0.
     */
    public void setByte(int indice, int valor) throws ArrayIndexOutOfBoundsException {
        if (indice >= NUM_BYTES_PALABRA || indice < 0) {
            throw new ArrayIndexOutOfBoundsException("Indice fuera de la palabra");
        }
        _byte[indice] = valor;
    }

    /**
     * Devuelve una representación como entero del objeto invocador
     * @return una representación como entero del objeto invocador
     */
    public int toInt() {
        int resultado = 0;
        for (int i = 0; i < NUM_BYTES_PALABRA; i++) {
            resultado += Math.pow(10, (NUM_BYTES_PALABRA - 1) - i) * _byte[i];
        }
        return resultado;
    }

    /**Suma dos palabras y devuelve una nueva palabra con el resultado.
     * Esta operación debe ser ejecutada solo para palabras que representan datos,
     * no instrucciones. La palabra resultante contiene todo en el byte más significativo.
     * @param p1 primer sumando
     * @param p2 segundo sumando
     * @return una palabra que representa la suma de p1 y p2
     */
    public static Palabra sumar(Palabra p1, Palabra p2) {
        Palabra resultado = new Palabra();
        resultado.setByte(0, p1.toInt() + p2.toInt());
        return resultado;
    }

    /**Resta una palabra a otra y devuelve una nueva palabra con el resultado.
     * Esta operación debe ser ejecutada solo para palabras que representan datos,
     * no instrucciones. La palabra resultante contiene todo en el byte más significativo.
     * @param p1 palabra a la que se le restará <code>p2</code>
     * @param p2 palabra que se le restará a <code>p1</code>
     * @return una palabra que representa el producto de p1 y p2
     */
    public static Palabra restar(Palabra p1, Palabra p2) {
        Palabra resultado = new Palabra();
        resultado.setByte(0, p1.toInt() - p2.toInt());
        return resultado;
    }

    public static Palabra suma(Palabra p1, int p2) {
        Palabra resultado = new Palabra();
        resultado.setByte(0, p1.toInt() + p2);
        return resultado;
    }

    /**Multiplica dos palabras y devuelve una nueva palabra con el resultado.
     * Esta operación debe ser ejecutada solo para palabras que representan datos,
     * no instrucciones. La palabra resultante contiene todo en el byte más significativo.
     * @param p1 primer factor
     * @param p2 segundo factor
     * @return una palabra que representa el producto de p1 y p2
     */
    public static Palabra multiplicar(Palabra p1, Palabra p2) {
        Palabra resultado = new Palabra();
        resultado.setByte(0, p1.toInt() * p2.toInt());
        return resultado;
    }

    /**Divide una palabra entre otra y devuelve una nueva palabra con el resultado.
     * Esta operación debe ser ejecutada solo para palabras que representan datos,
     * no instrucciones. La palabra resultante contiene todo en el byte más significativo.
     * @param p1 numerador
     * @param p2 denoninador
     * @return una palabra que representa la división de p1 entre p2
     * @throws DivideByZeroException si <code>p2</code> es igual a 0
     */
    public static Palabra dividir(Palabra p1, Palabra p2) {
        Palabra resultado = new Palabra();
        resultado.setByte(0, p1.toInt() / p2.toInt());
        return resultado;
    }

    public Palabra getInstruccionAnterior() {
        return instruccionAnterior;
    }

    public Palabra getInstruccionTransanterior() {
        return instruccionTrasanterior;
    }

    public int getCicloInicial() {
        return cicloInicial;
    }

    public int getTiempoCPU() {
        return tiempoCPU;
    }

    public void setInstruccionAnterior(Palabra instruccionAnterior) {
        this.instruccionAnterior = instruccionAnterior;
    }

    public void setInstruccionTransanterior(Palabra instruccionTransanterior) {
        this.instruccionTrasanterior = instruccionTransanterior;
    }

    public void setTiempoCPU(int tiempoCPU) {
        this.tiempoCPU = tiempoCPU;
    }

    public void asignarTiempos(int reloj, int retraso) {
        this.tiempoCPU = retraso + 5;
        this.cicloInicial = reloj;
    }
}
