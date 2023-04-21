package ajedrezLogica;

import java.util.*;
import javax.swing.*;

/**
 *
 * @author Billy
 */
public class Peon extends Pieza {

    /** Creates a new instance of Peon */
    public Peon(String nombre, String posicion, JLabel laFigura) {
        super(nombre, posicion, laFigura);
    }

    public Peon() {
        super("", "", null);
    }

    private void comerPeon(manejadorPiezas adminPiezas, char f, char c) {
        char valor = f;
        if (valor > 'a') {
            --valor;
            if (adminPiezas.HayPiezaenCasilla(aString(valor, c)) == true) {
                agregarListaxComer(adminPiezas, valor, c);
            }
        }
        valor = f;
        if (valor < 'h') {
            ++valor;
            if (adminPiezas.HayPiezaenCasilla(aString(valor, c)) == true) {
                agregarListaxComer(adminPiezas, valor, c);
            }
        }
    }

    private boolean buscarListaPeonPasado(Evento evt, char elColor) {
        if (elColor == 'B' && evt.EnviarMarcacionesdePeonesBlancas() != null) {
            for (int k = 0; k < evt.EnviarMarcacionesdePeonesBlancas().size(); k++) {
                if (evt.EnviarMarcacionesdePeonesBlancas().get(k).posicionPeon.equals(posicion)) {
                    String pos = evt.EnviarMarcacionesdePeonesBlancas().get(k).damePuntoToma();
                    movimientosposibles.add(pos);
                    return true;
                }
            }
        }
        if (elColor == 'N' && evt.EnviarMarcacionesdePeonesNegras() != null) {
            for (int k = 0; k < evt.EnviarMarcacionesdePeonesNegras().size(); k++) {
                if (evt.EnviarMarcacionesdePeonesNegras().get(k).posicionPeon.equals(posicion)) {
                    String pos = evt.EnviarMarcacionesdePeonesNegras().get(k).damePuntoToma();
                    movimientosposibles.add(pos);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean buscarListaPeonPasado(EventoJuego evt, char elColor) {
        if (elColor == 'B' && evt.EnviarMarcacionesdePeonesBlancas() != null) {
            for (int k = 0; k < evt.EnviarMarcacionesdePeonesBlancas().size(); k++) {
                if (evt.EnviarMarcacionesdePeonesBlancas().get(k).posicionPeon.equals(posicion)) {
                    String pos = evt.EnviarMarcacionesdePeonesBlancas().get(k).damePuntoToma();
                    movimientosposibles.add(pos);
                    return true;
                }
            }
        }
        if (elColor == 'N' && evt.EnviarMarcacionesdePeonesNegras() != null) {
            for (int k = 0; k < evt.EnviarMarcacionesdePeonesNegras().size(); k++) {
                if (evt.EnviarMarcacionesdePeonesNegras().get(k).posicionPeon.equals(posicion)) {
                    String pos = evt.EnviarMarcacionesdePeonesNegras().get(k).damePuntoToma();
                    movimientosposibles.add(pos);
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> CalcularMovimientos(manejadorPiezas adminPiezas) {
        char f, c;
        movimientosposibles = new ArrayList<String>();
        char colorPieza = this.getNombre().charAt(0);
        switch(colorPieza) {
            case 'B':
                f = posicion.charAt(0);
                c = posicion.charAt(1);
                if (c < '8' && c >= '2') {
                    ++c;
                    if (adminPiezas.HayPiezaenCasilla(aString(f, c)) == false) movimientosposibles.add(aString(f, c));
                    comerPeon(adminPiezas, f, c);
                    c = posicion.charAt(1);
                    if (c == '2' && adminPiezas.HayPiezaenCasilla(aString(f, ++c)) == false) {
                        ++c;
                        if (adminPiezas.HayPiezaenCasilla(aString(f, c)) == false) movimientosposibles.add(aString(f, c));
                    }
                }
                if (adminPiezas.getEstoyenLectura() == false && adminPiezas.getEstoyJugando() == false) if (adminPiezas.dameEvento().dameContadorJugadas() > 0) buscarListaPeonPasado(adminPiezas.dameEvento(), 'B');
                if (adminPiezas.getEstoyenLectura() == false && adminPiezas.getEstoyJugando() == true) if (adminPiezas.dameEventoJuego().dameContadorJugadas() > 0) buscarListaPeonPasado(adminPiezas.dameEventoJuego(), 'B');
                break;
            case 'N':
                f = posicion.charAt(0);
                c = posicion.charAt(1);
                if (c > '1' && c <= '7') {
                    --c;
                    if (adminPiezas.HayPiezaenCasilla(aString(f, c)) == false) movimientosposibles.add(aString(f, c));
                    comerPeon(adminPiezas, f, c);
                    c = posicion.charAt(1);
                    if (c == '7' && adminPiezas.HayPiezaenCasilla(aString(f, --c)) == false) {
                        --c;
                        if (adminPiezas.HayPiezaenCasilla(aString(f, c)) == false) movimientosposibles.add(aString(f, c));
                    }
                }
                if (adminPiezas.getEstoyenLectura() == false && adminPiezas.getEstoyJugando() == false) if (adminPiezas.dameEvento().dameContadorJugadas() > 0) buscarListaPeonPasado(adminPiezas.dameEvento(), 'B');
                if (adminPiezas.getEstoyenLectura() == false && adminPiezas.getEstoyJugando() == true) if (adminPiezas.dameEventoJuego().dameContadorJugadas() > 0) buscarListaPeonPasado(adminPiezas.dameEventoJuego(), 'B');
                break;
        }
        return movimientosposibles;
    }
}
