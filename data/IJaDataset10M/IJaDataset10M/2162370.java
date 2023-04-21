package lps.grupo05.pr1.jugador;

import java.util.Random;
import java.util.Vector;
import lps.grupo05.pr1.logica.Posicion;
import lps.grupo05.pr1.logica.Tablero;
import lps.grupo05.pr1.logica.Tablero.Estado;

/**
 * Implementacion en modo IA del interface. 
 *  Busca una nueva posicion a disparar teniendo en cuenta 
 *  los barcos tocados anteriormente
 * @author Natalia Lopez y Alfredo Benito GRUPO 05
 * 
 */
public class JugadorAleatorio implements Jugador {

    private Boolean tocadoAnterior;

    private Vector<Posicion> casillasTocadas;

    Posicion ultimaPosicion;

    private Vector<Posicion> casillasDisparadas;

    /**
	 * Funcion que calcula una posicion aleatoria dentro de los 
	 * limites del tablero
	 * @param t tablero 
	 * @return una posicion dentro de los limites del tablero
	 */
    private Posicion calculaPosAleatoria(Tablero t) {
        Random r = new Random();
        return new Posicion(r.nextInt(t.getAlto()), r.nextInt(t.getAncho()));
    }

    public JugadorAleatorio() {
        super();
        this.tocadoAnterior = false;
        this.casillasTocadas = new Vector<Posicion>();
        this.ultimaPosicion = null;
        this.casillasDisparadas = new Vector<Posicion>();
    }

    /**
	 * Metodo que busca una posicion "aleatoria" dentro del tablero.
	 * Si en otra jugada hemos tocado un barco intentar� hundirlo buscando sus adyacentes.
	 * 
	 * 	@param t Tablero de juego
	 * 	@param resultAnterior resultado de la jugada anterior (AGUA|TOCADO|HUNDIDO)
	 * 	@return una Posicion dentro del tablero y que no ha sido disparada antes
	 */
    @Override
    public Posicion escogeMovimiento(Tablero t, Estado resultAnterior) {
        Posicion posicionAux = null;
        if ((resultAnterior == Estado.TOCADO) || (resultAnterior == Estado.HUNDIDO)) {
            Posicion posEsquina = null;
            int[] desp = { -1, 1 };
            for (int i = 0; i < desp.length; i++) {
                for (int j = 0; j < desp.length; j++) {
                    posEsquina = new Posicion(ultimaPosicion.getFila() + desp[i], ultimaPosicion.getCol() + desp[j]);
                    if (t.perteneceAlTablero(posEsquina)) casillasDisparadas.add(posEsquina);
                }
            }
        }
        if (resultAnterior == Estado.TOCADO) {
            tocadoAnterior = true;
            casillasTocadas.add(ultimaPosicion);
        } else {
            if (resultAnterior == Estado.HUNDIDO) {
                casillasTocadas.removeAllElements();
                tocadoAnterior = false;
            }
        }
        if (!tocadoAnterior) {
            boolean valida = false;
            while (!valida) {
                posicionAux = calculaPosAleatoria(t);
                valida = !casillasDisparadas.contains(posicionAux);
            }
        } else {
            int numeroAciertos = casillasTocadas.size();
            if (numeroAciertos == 1) {
                posicionAux = calculaPosAdyacente(t);
            } else {
                Posicion posPrimero = casillasTocadas.firstElement();
                Posicion posSegundo = casillasTocadas.elementAt(1);
                if (posPrimero.getFila() == posSegundo.getFila()) {
                    posicionAux = calculaPosAdyacenteHorizontal(t);
                } else {
                    posicionAux = calculaPosAdyacenteVertical(t);
                }
            }
        }
        ultimaPosicion = posicionAux;
        casillasDisparadas.add(posicionAux);
        return posicionAux;
    }

    /**
	 * Metodo auxiliar para calculaPosAdyacente que calcula una posicion 
	 * en la misma columna de las posiciones
	 * del vector de casillas tocadas
	 * @param t tablero de juego
	 * @return una posicion en la misma columna de las tocadas
	 */
    private Posicion calculaPosAdyacenteVertical(Tablero t) {
        Posicion posCalculada = new Posicion();
        if (!calculaPosDesp(t, posCalculada, 1, 0)) {
            calculaPosDesp(t, posCalculada, -1, 0);
        }
        return posCalculada;
    }

    /**
	 * metodo auxiliar para calculaPosAdyacente que calcula una posicion
	 * en la misma fila que las posiciones tocadas
	 * @param t
	 * @return
	 */
    private Posicion calculaPosAdyacenteHorizontal(Tablero t) {
        Posicion posCalculada = new Posicion();
        if (!calculaPosDesp(t, posCalculada, 0, 1)) {
            calculaPosDesp(t, posCalculada, 0, -1);
        }
        return posCalculada;
    }

    /**
	 * metoco que calcula una posicon adyacente a las posiciones tocadas.
	 * Posicones adyacentes:
	 * 		HORIZONTAL DERECHA: despFila = 0; despCol = +1
	 * 		HORIZONTAL IZQUIERDA: despFila = 0; despCol = -1
	 * 		VERTICAL ARRIBA: despFila = -1; despCol = 0;
	 * 		VERTICAL ABAJO: despFila = +1; despCol = 0;
	 * 		DIAGONAL ARRIBA DERECHA: despFila = -1; despCol = +1;
	 * 		DIAGONAL ARRIBA IZQUIERDA: despFila = -1, despCol = -1;
	 * 		DIAGONAL ABAJO DERECHA: despFila = +1; despCol = +1;
	 * 		DIAGONAL ABAJO IZQUIERDA: despFila = +1; despCol = -1;
	 * @param t tablero de juego
	 * @param posCalculada posicion adyacente a las pos tocadas
	 * @param despFila 
	 * @param despCol
	 * @return un booleano que indica si la posicion calculada es valida
	 */
    private boolean calculaPosDesp(Tablero t, Posicion posCalculada, int despFila, int despCol) {
        Posicion posAux = casillasTocadas.firstElement();
        boolean valida = false;
        boolean acabado = false;
        Posicion posAux2 = null;
        while (!acabado) {
            posAux2 = new Posicion(posAux.getFila() + despFila, posAux.getCol() + despCol);
            if ((!t.perteneceAlTablero(posAux2)) || ((casillasDisparadas.contains(posAux2) && !casillasTocadas.contains(posAux2)))) {
                acabado = true;
                valida = false;
            } else {
                if (casillasTocadas.contains(posAux2)) {
                    posAux = posAux2;
                    valida = false;
                } else {
                    acabado = true;
                    valida = true;
                }
            }
        }
        posCalculada.setFila(posAux2.getFila());
        posCalculada.setCol(posAux2.getCol());
        return valida;
    }

    /**
	 * metodo que calcula una posicion adyacente (vertical u horizontal) 
	 * de las casillas tocadas
	 * @param t tablero de juego
	 * @return una posicion adyacente a las tocadas
	 */
    private Posicion calculaPosAdyacente(Tablero t) {
        Posicion posCalculada = new Posicion();
        if (!calculaPosDesp(t, posCalculada, 0, 1)) {
            if (!calculaPosDesp(t, posCalculada, 0, -1)) {
                if (!calculaPosDesp(t, posCalculada, 1, 0)) {
                    calculaPosDesp(t, posCalculada, -1, 0);
                }
            }
        }
        return posCalculada;
    }
}
