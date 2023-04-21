package ar.edu.unicen.exa.server.communication.tasks;

import java.util.logging.Logger;
import com.jme.math.Vector3f;
import com.sun.sgs.app.ClientSession;
import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;
import common.messages.notify.MsgChangeWorld;

/**
 * La tarea se ejecutará al recibir un mensaje por un canal desde un cliente,
 * el cual informa que cambiará al mundo indicado en el mensaje. Las acciones a
 * tomar son las siguientes:
 * <ol>
 * <li>Agregar el {@link Player} al mundo({@link IGridStructure}) deseado.</li>
 * <li>Suscribirlo a la celda({@link Cell}) por defecto del mundo y a las 
 * adyacentes según corresponda.</li>
 * <li>Quitarlo del mundo en el que estaba el {@link Player} previamente.</li>
 * <li>Desuscribirlo de las celdas en las que se encontraba en el mundo
 * anterior.</li>
 * <li>Enviar los mensajes correspondientes {@link MsgArrived} a las celdas del
 * mundo nuevo.</li>
 * <li>Enviar los mensajes correspondientes {@link MsgLeft} a las celdas del
 * mundo viejo.</li>
 * </ol>
 * 
 * @encoding UTF-8.
 * 
 */
public final class TEnterWorld extends TaskCommunication {

    /**  Para cumplir con la version de la clase {@Serializable}. */
    private static final long serialVersionUID = 1L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(TEnterWorld.class.getName());

    /**
	 * Constructor.
	 * 
	 * @param msg
	 *            El mensaje.
	 */
    public TEnterWorld(final IMessage msg) {
        super(msg);
    }

    /**
	 * Crea la tarea a travez del factory..
	 * 
	 * @param msg
	 *            El mensaje.
	 * 
	 * @return TEnterWorld.
	 */
    @Override
    public TaskCommunication factoryMethod(final IMessage msg) {
        return new TEnterWorld(msg);
    }

    /**
	 * Este metodo cambia de mundo({@link IGridStructure}) al {@link Player}. 
	 * Para ello, se obtiene el mundo actual y la celda({@link Cell}) donde se 
	 * encuentra. Luego se crea el mensaje {@link MsgLeft} para notificar al 
	 * los {@link Player}s que estan en la misma celda y en las visibles por
	 * otros, que el {@link Player} no se encuntra en la celda y finalmente se
	 * desuscribe del canal asociado a la celda. Para ingresar al nuevo mundo,
	 * se obtiene el id contenido en el mensaje y se asigna al {@link Player}
	 * la posicion y celda inicial suscribiendolo al canal de dicha celda. 
	 * Finalmente se crea el mensaje {@link MsgArrived} para 
	 * notificar a los demas {@link Player}s el ingreso de este {@link Player}.
	 * 
	 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/>
	 * @encoding UTF-8.
	 * 
	 */
    public void run() {
        Player player = getPlayerAssociated();
        Cell cell = getCellAssociated();
        IMessage msgLeft = null;
        try {
            msgLeft = MessageFactory.getInstance().createMessage(MsgTypes.MSG_LEFT_TYPE);
            ((MsgPlainText) msgLeft).setMsg(player.getIdEntity());
        } catch (UnsopportedMessageException e1) {
            e1.printStackTrace();
        }
        ClientSession session = player.getSession();
        cell.send(msgLeft, session);
        IGridStructure structure = cell.getStructure();
        LOGGER.info(player.getIdEntity() + " -> abandona el mundo " + structure.getIdWorld());
        Cell[] adyacentes = structure.getAdjacents(cell, player.getPosition());
        if (adyacentes != null) {
            for (int i = 0; i < adyacentes.length; i++) {
                adyacentes[i].send(msgLeft, null);
            }
        }
        cell.leaveFromChannel(session);
        MsgChangeWorld msg = (MsgChangeWorld) getMessage();
        String newWorldID = msg.getIdNewWorld();
        LOGGER.info(player.getIdEntity() + " -> entra al mundo " + newWorldID);
        player.setActualWorld(newWorldID);
        player.setAngle(new Vector3f(1, 1, 1));
        structure = GridManager.getInstance().getStructure(newWorldID);
        player.setPosition(msg.getSpownPosition());
        LOGGER.info(player.getIdEntity() + " -> posicion en el nuevo mundo " + player.getPosition());
        cell = structure.getCell(player.getPosition());
        cell.joinToChannel(session);
        IMessage msgArrived = null;
        try {
            msgArrived = MessageFactory.getInstance().createMessage(MsgTypes.MSG_ARRIVED_TYPE);
            ((MsgPlainText) msgArrived).setMsg(player.getIdEntity());
        } catch (UnsopportedMessageException e) {
            e.printStackTrace();
        }
        cell.send(msgArrived, session);
        adyacentes = structure.getAdjacents(cell, player.getPosition());
        if (adyacentes != null) {
            for (int i = 0; i < adyacentes.length; i++) {
                adyacentes[i].send(msgArrived, null);
            }
        }
    }
}
