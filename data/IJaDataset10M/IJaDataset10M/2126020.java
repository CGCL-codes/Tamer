package comportamientos;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import tablero.AgTablero;
import tablero.EstadoPartida;
import tablero.ResumenJugador;
import utils.Filtros;
import utils.Personajes;
import acciones.CobrarDistritos;
import acciones.PedirConstruirDistrito;

public class ConstruirDistrito extends Behaviour {

    private final AgTablero agt;

    private int construidoTurno;

    private boolean fin = false;

    private Personajes pj;

    private EstadoPartida ep;

    private ResumenJugador jugador;

    public ConstruirDistrito(AgTablero agTablero) {
        agt = agTablero;
        construidoTurno = 0;
    }

    @Override
    public void action() {
        ep = EstadoPartida.getInstance();
        jugador = ep.getJugActual();
        ACLMessage msg = agt.reciveBlockingMessageFrom(Filtros.PEDIRCONSTRUIRDISTRITO, jugador, 100);
        if (msg != null) {
            try {
                PedirConstruirDistrito contenido = (PedirConstruirDistrito) agt.getContentManager().extractContent(msg);
                int ret = -1;
                if (jugador.esJugador(contenido.getJugador()) && jugador.getPersonaje().compareTo(contenido.getPersonaje()) == 0 && jugador.puedeConstruir(contenido.getDistrito())) {
                    ret = contenido.getDistrito().getCoste();
                    jugador.construir(contenido.getDistrito());
                    pj = Personajes.getPersonajeByPJ(jugador.getPersonaje());
                    fin = true;
                }
                CobrarDistritos obj = new CobrarDistritos();
                obj.setCantidad(ret);
                obj.setDistrito(contenido.getDistrito());
                obj.setJugador(jugador.getJugador());
                agt.sendMSG(ACLMessage.REQUEST, jugador, obj, Filtros.COBRARDISTRITOS);
            } catch (UngroundedException e) {
                e.printStackTrace();
            } catch (CodecException e) {
                e.printStackTrace();
            } catch (OntologyException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean done() {
        if (fin) {
            incDistConstTurno();
            ep.comprobarFinPartida();
            if (pj.equals(Personajes.ARQUITECTO) && construidoTurno < 3) {
                fin = false;
            }
        }
        return fin;
    }

    private void incDistConstTurno() {
        this.construidoTurno++;
    }
}
