package comportamientos_jugador;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jugador.AgJugador;
import utils.Filtros;
import acciones.CobrarDistritosObispo;
import acciones.DarMonedas;

public class PedirCobrarObispo extends Behaviour {

    private final AgJugador _agj;

    private final Behaviour beh;

    private final AID raid;

    public PedirCobrarObispo(AgJugador agj, Behaviour ft, AID aid) {
        _agj = agj;
        beh = ft;
        raid = aid;
    }

    @Override
    public void action() {
        CobrarDistritosObispo cobrarObispo = new CobrarDistritosObispo();
        cobrarObispo.setJugador(_agj.getJugador());
        _agj.sendMSG(ACLMessage.REQUEST, raid, cobrarObispo, Filtros.COBRARDISTRITOSOBISPO);
        int monedas = 0;
        ACLMessage msg = _agj.reciveBlockingMessage(Filtros.DARMONEDAS, false);
        try {
            DarMonedas contenido = (DarMonedas) _agj.getContentManager().extractContent(msg);
            monedas += contenido.getMonedas().intValue();
            _agj.addMonedas(monedas);
        } catch (UngroundedException e) {
            e.printStackTrace();
        } catch (CodecException e) {
            e.printStackTrace();
        } catch (OntologyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean done() {
        _agj.addBehaviour(beh);
        return true;
    }
}
