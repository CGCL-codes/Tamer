package client.game.state;

import java.util.ArrayList;
import java.util.Hashtable;
import client.communication.DynamicEntitysSolicitations;
import client.communication.PositionsTranslator;
import client.communication.WorldsMaper;
import client.communication.tasks.TaskCommFactory;
import client.game.hud.HudStateType;
import client.game.hud.listener.AccessPointListener;
import client.game.hud.listener.TaskListener;
import client.game.task.ETask;
import client.game.task.ITask;
import client.game.task.gameState.AccessPointAction;
import client.game.task.gameState.U3DChangeDeskStateTask;
import client.game.task.gameState.U3DDisplayConditionedMsgTask;
import client.game.task.gameState.U3DDisplayWarningMsgTask;
import client.manager.TaskManager;
import com.jme.math.Vector3f;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgTypes;
import common.messages.notify.MsgChangeWorld;

public class WorldStatesMachine {

    /**
	 * el String con el nombre del estado actual.
	 */
    public static String activeState;

    /**
	 * La instancia unica del <code>StateMachineManager</code>.
	 */
    protected static WorldStatesMachine instance;

    /**
	 * Obtiene la instancia unica del <code>StateMachineManager</code>.
	 * 
	 * @return <code>StateMachineManager</code> La instancia unica.
	 */
    public static WorldStatesMachine getInstace() {
        if (WorldStatesMachine.instance == null) {
            WorldStatesMachine.instance = new WorldStatesMachine();
        }
        return WorldStatesMachine.instance;
    }

    /**
	 * Este m�todo sirve para limpiar los estados activos, llamando a los
	 * respectivos metodos cleanup de cada uno de ellos
	 */
    public void endState(String myState) {
        ArrayList<GameState> States = GameStateManager.getInstance().getChildren();
        for (int i = 0; i < States.size(); i++) if ((States.get(i).isActive()) && (!States.get(i).getName().equals(myState))) States.get(i).cleanup();
    }

    /**
	 * Encargado de llevar a ala maquina a un nuevo estado
	 * "raiz de la accion de transicion de estado"
	 * 
	 * */
    public void changeToState(String newState) {
        this.endState(newState);
        GameStateManager.getInstance().deactivateAllChildren();
        GameStateManager.getInstance().activateChildNamed(newState);
        GameStateManager.getInstance().activateChildNamed(U3DDesktopState.stateName);
        WorldStatesMachine.activeState = newState;
        ((U3dState) GameStateManager.getInstance().getChild(newState)).initialize();
        U3DChangeDeskStateTask taskCargando = (U3DChangeDeskStateTask) TaskManager.getInstance().createTask(ETask.CHANGE_DESK_STATE);
        taskCargando.setNewState(HudStateType.WALKING_HUD);
        TaskManager.getInstance().enqueue(taskCargando);
    }

    /**
	 * Encargao de la ejecucicon de estados transicionales
	 * "Estados q no afectan al estado anterior" Digase: Juegos y Spots
	 * 
	 * @param newState
	 */
    public void showState(String newState, Hashtable<String, Object> configuration) {
        ((U3dState) GameStateManager.getInstance().getChild(newState)).setConfiguration(configuration);
        ((U3dState) GameStateManager.getInstance().getChild(newState)).initialize();
    }

    /**
	 * Devuelva el estado en proceso
	 * 
	 * */
    public U3dState getActiveState() {
        return ((U3dState) GameStateManager.getInstance().getChild(activeState));
    }

    /**
	 * utilizado ara avisar al serrvidor q se esta cambiando de mundo --->
	 * cambio de accion.
	 */
    public void sendChangePlaceMsg(String proxEstado, Vector3f newPosition) {
        try {
            MsgChangeWorld msg = (MsgChangeWorld) MessageFactory.getInstance().createMessage(MsgTypes.MSG_CHANGE_WORLD_TYPE);
            msg.setIdNewWorld(WorldsMaper.CLIENT_TO_SERVER.get(proxEstado));
            msg.setSpownPosition(PositionsTranslator.serverPosition(proxEstado, newPosition));
            ITask task = TaskCommFactory.getInstance().createComTask(msg);
            TaskManager.getInstance().submit(task);
            DynamicEntitysSolicitations.DYNAMIC_ENTITYS_STATES.clear();
        } catch (UnsopportedMessageException e) {
            e.printStackTrace();
        }
    }

    /**
	 * certifica si el pj desea ingresar al access Point.
	 * */
    public void changeStateDecision(AccessPoint ap) {
        U3DDisplayConditionedMsgTask taskDisplayMessage = (U3DDisplayConditionedMsgTask) TaskManager.getInstance().createTask(ETask.DISPLAY_CONDITIONED_MESSAGE);
        taskDisplayMessage.setParameters("Cambio de Mundo", "�Desea ingresar?", new AccessPointListener(ap));
        TaskManager.getInstance().enqueue(taskDisplayMessage);
    }

    /**
	 * Una vez q a sio tomada la decision de cambiar de mundo, y q el servidor a
	 * respondido a nuestro msj's estye metdo se encarga de realizar las
	 * acciones necesarias para llevar a cabo lo decidido.
	 * */
    public void performStoreAction(IMessage action) {
        getActiveState().performAction(action);
    }

    public void changeStateDialog() {
        U3DDisplayWarningMsgTask taskDisplayMessage = (U3DDisplayWarningMsgTask) TaskManager.getInstance().createTask(ETask.DISPLAY_WARNING_MESSAGE);
        taskDisplayMessage.setParameters("Bienvenido", this.getActiveState().getDialogText(), "Continuar");
        TaskManager.getInstance().enqueue(taskDisplayMessage);
    }

    public void changeToConditionedStateDialog(final AccessPointAction taskAccessPntAction) {
        U3DDisplayConditionedMsgTask taskDisplayMessage = (U3DDisplayConditionedMsgTask) TaskManager.getInstance().createTask(ETask.DISPLAY_CONDITIONED_MESSAGE);
        taskDisplayMessage.setParameters("Acceso Condicionado", "Para ingresar a este edificio \n" + " antes deber�s superar un desaf�o. \n" + "En �l, tu puntaje deber� ser superior  \n" + " a: " + taskAccessPntAction.getConfiguration().get("minScore") + " puntos.", new TaskListener(taskAccessPntAction));
        TaskManager.getInstance().enqueue(taskDisplayMessage);
    }

    public GameState getState(String stateName) {
        return GameStateManager.getInstance().getChild(stateName);
    }
}
