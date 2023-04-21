package com.chuidiang.ejemplos.poco_mazacote;

import static org.junit.Assert.assertEquals;
import com.chuidiang.ejemplos.semi_mazacote.IfzMuestraResultados;

/**
 * Una IfzMuestraResultados espec�fica para test. En este test espec�fico sabe
 * qu� debe mostrar la clase Sumador, as� que simplemente va comprobando que lo
 * que muestra la clase Sumador es lo esperado.
 * 
 * @author Chuidiang
 * 
 */
public class MockMuestraResultados implements IfzMuestraResultados {

    /** Para saber por qu� texto del array vamos */
    private int contador = 0;

    /** Textos que esperamos recibir */
    private final String[] textosEsperados = { "Sumando 1:", "Sumando 2:", "1.0+2.0=3.0" };

    /** Devuelve el n�mero de textos recibidos */
    public int getContador() {
        return contador;
    }

    /** Comprueba que el texto recibido coincide con el esperado */
    public void println(String textoRecibido) {
        assertEquals(textosEsperados[contador++], textoRecibido);
    }
}
