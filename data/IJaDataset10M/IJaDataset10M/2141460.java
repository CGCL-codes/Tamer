package comportamientos_jugador;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jugador.AgJugador;
import utils.Filtros;
import acciones.Monedas;

public class CobrarRobo extends Behaviour {

    private final AgJugador _agj;

    private final AID raid;

    public CobrarRobo(AgJugador agj, AID aid) {
        _agj = agj;
        raid = aid;
    }

    @Override
    public void action() {
        ACLMessage msg = _agj.reciveBlockingMessageFrom(Filtros.COBRARROBO, raid, 100);
        if (msg != null) {
            System.out.println("ejecuta el CobrarRobo");
            Monedas m = new Monedas();
            ;
            try {
                m = (Monedas) _agj.getContentManager().extractContent(msg);
            } catch (UngroundedException e) {
                e.printStackTrace();
            } catch (CodecException e) {
                e.printStackTrace();
            } catch (OntologyException e) {
                e.printStackTrace();
            }
            _agj.setMonedas(m.getDinero() + _agj.getMonedas());
        }
    }

    @Override
    public boolean done() {
        return true;
    }
}
