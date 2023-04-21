package poker;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author jga87y
 */
public class PartidaPoker {

    ArrayList<JugadorPoker> jugadores;

    BarajaFrancesa baraja;

    Bote bote;

    /** Creates a new instance of PartidaPoker */
    public PartidaPoker(ArrayList<JugadorPoker> j) {
        jugadores.addAll(j);
        baraja = new BarajaFrancesa();
        bote = new Bote();
    }

    public void comenzar() {
        Iterator it = jugadores.iterator();
        while (it.hasNext()) {
            JugadorPoker j = (JugadorPoker) it.next();
            j.asignarCartas(baraja.sacarCarta(), baraja.sacarCarta());
        }
        it = jugadores.iterator();
        while (it.hasNext()) {
            JugadorPoker j = (JugadorPoker) it.next();
        }
    }
}
