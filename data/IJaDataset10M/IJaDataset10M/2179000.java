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
import acciones.Matar;
import conceptos.Personaje;

public class HabilidadAsesino extends Behaviour {

    private final AgTablero agt;

    private boolean fin = false;

    public HabilidadAsesino(AgTablero agTablero) {
        agt = agTablero;
    }

    @Override
    public void action() {
        EstadoPartida ep = EstadoPartida.getInstance();
        ResumenJugador jugador = ep.getJugActual();
        ACLMessage msg = agt.reciveBlockingMessageFrom(Filtros.MATAR, jugador, 100);
        if (msg != null) {
            fin = true;
            try {
                Matar contenido = (Matar) myAgent.getContentManager().extractContent(msg);
                Personaje personajeAsesinado = contenido.getPersonaje();
                ep.setNombreMuerto(personajeAsesinado);
                agt.sendMSG(ACLMessage.REQUEST, null, contenido, Filtros.NOTIFICARASESINADO);
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
        return fin;
    }
}
