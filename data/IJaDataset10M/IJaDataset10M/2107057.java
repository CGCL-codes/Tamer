package comportamientos_jugador;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jugador.AgJugador;
import tablero.EstadoPartida;
import tablero.ResumenJugador;
import utils.Filtros;
import acciones.DecirEstado;
import acciones.DestruirDistrito;
import acciones.PedirDistritoJugadores;
import conceptos.Distrito;

public class DestruirDistritoJugador extends Behaviour {

    private final AgJugador _agj;

    private final Behaviour beh;

    private final AID raid;

    public DestruirDistritoJugador(AgJugador agj, Behaviour ft, AID aid) {
        _agj = agj;
        beh = ft;
        raid = aid;
    }

    @Override
    public void action() {
        EstadoPartida ep = EstadoPartida.getInstance();
        ResumenJugador jugador = ep.getJugActual();
        DecirEstado de = new DecirEstado();
        de.setJugador(_agj.getJugador());
        _agj.sendMSG(ACLMessage.REQUEST, raid, de, Filtros.PEDIRRESUMENESJUGADORES);
        ACLMessage msg = _agj.reciveBlockingMessage(Filtros.DARRESUMENESJUGADORES, true);
        if (msg != null) {
            try {
                PedirDistritoJugadores pd = (PedirDistritoJugadores) _agj.getContentManager().extractContent(msg);
                System.out.println("Imprimimos pd");
                System.out.println("person 1 = " + pd.getPersonaje1().getNombre() + "; jug 1 = " + pd.getJugador1().getNombre());
                System.out.println("Imprimimos los distritos");
                for (int i = 0; i < pd.getDistritos1().size(); i++) System.out.print(": " + ((Distrito) (pd.getDistritos1().get(i))).getNombre());
                System.out.println("");
                System.out.println("---------------------------------------");
                System.out.println("person 2 = " + pd.getPersonaje2().getNombre() + "; jug 2 = " + pd.getJugador2().getNombre());
                System.out.println("Imprimimos los distritos");
                for (int i = 0; i < pd.getDistritos2().size(); i++) System.out.print(": " + ((Distrito) (pd.getDistritos2().get(i))).getNombre());
                System.out.println("");
                System.out.println("---------------------------------------");
                System.out.println("person 3 = " + pd.getPersonaje3().getNombre() + "; jug 3 = " + pd.getJugador3().getNombre());
                System.out.println("Imprimimos los distritos");
                for (int i = 0; i < pd.getDistritos3().size(); i++) System.out.print(": " + ((Distrito) (pd.getDistritos3().get(i))).getNombre());
                System.out.println("");
                System.out.println("---------------------------------------");
                DestruirDistrito dd = new DestruirDistrito();
                completarDetruirDistrito(pd, dd);
                _agj.sendMSG(ACLMessage.REQUEST, raid, dd, Filtros.DESTRUIRDISTRITO);
            } catch (UngroundedException e) {
                e.printStackTrace();
            } catch (CodecException e) {
                e.printStackTrace();
            } catch (OntologyException e) {
                e.printStackTrace();
            }
        }
    }

    private void completarDetruirDistrito(PedirDistritoJugadores pd, DestruirDistrito dd) {
        int num = (int) (Math.random() * 3);
        int aux = 0;
        int dinero = 0;
        switch(num) {
            case 0:
                dd.setJugador(pd.getJugador1());
                dinero = pd.getJugador1().getMonedas();
                aux = (int) (Math.random() * pd.getDistritos1().size());
                dd.setDistrito((Distrito) (pd.getDistritos1().get(aux)));
                break;
            case 1:
                dd.setJugador(pd.getJugador2());
                dinero = pd.getJugador2().getMonedas();
                aux = (int) (Math.random() * pd.getDistritos2().size());
                dd.setDistrito((Distrito) (pd.getDistritos2().get(aux)));
                break;
            case 2:
                dd.setJugador(pd.getJugador3());
                dinero = pd.getJugador3().getMonedas();
                aux = (int) (Math.random() * pd.getDistritos3().size());
                dd.setDistrito((Distrito) (pd.getDistritos3().get(aux)));
                break;
            default:
                break;
        }
        if (dinero >= (dd.getDistrito().getCoste() - 1)) {
            dd.setPago(dd.getDistrito().getCoste() - 1);
        } else {
            dd.setPago(-1);
        }
    }

    @Override
    public boolean done() {
        _agj.addBehaviour(beh);
        return true;
    }
}
