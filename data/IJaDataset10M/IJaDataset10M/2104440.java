package compresor;

/**
 * Representa el codigo Huffman de un signo del alfabeto de entrada.
 * 
 * @author Ing. Valerio Frittelli
 * @version Octubre de 2004
 */
public class CodigoHuffman {

    public static final int MAXBITS = 175;

    private byte bits[];

    private int startPos;

    /**
     *  Constructor por defecto. Ajusta startPos en MAXBITS y crea el vector de tamao MAXBITS
     */
    public CodigoHuffman() {
        startPos = MAXBITS;
        bits = new byte[MAXBITS];
    }

    /**
     *  Constructor. Inicializa el objeto con los datos tomados de otro, que viene como parmetro
     */
    public CodigoHuffman(CodigoHuffman c) {
        for (int i = c.startPos; i < MAXBITS; i++) {
            bits[i] = c.bits[i];
        }
        startPos = c.startPos;
    }

    /**
     *  Acceso al valor de startPos
     *  @return el valor de startPos
     */
    public int getStartPos() {
        return startPos;
    }

    /**
     * Ajusta el valor de un bit especfico en la posicin startPos -1
     * @param bit el bit a almacenar en la posicin que corresponde en el momento de la invocacin (startPos - 1)
     */
    public void setBit(byte bit) {
        if (startPos == 0) {
            System.exit(1);
        }
        startPos--;
        bits[startPos] = bit;
    }

    /**
     *  Accede al vector que contiene al cdigo Huffman del signo
     *  @return una referencia al vector que contiene al cdigo
     */
    public byte[] getCodigo() {
        return bits;
    }

    /**
     *  Obtiene una cadena con la respesentacin del objeto
     *  @return una cadena con todos los bits presentes en el vector bits
     */
    public String toString() {
        StringBuffer res = new StringBuffer("");
        for (int i = startPos; i < MAXBITS; i++) {
            res.append(bits[i]);
        }
        return res.toString();
    }
}
