package client.game.task.gameState;

import client.game.task.ETask;
import client.game.task.ITask;
import client.game.task.ITaskFactory;

/**
 * <code>U3DDisplayConditionedMsgTaskFactory</code> 
 * <code>U3DDisplayConditionedMsgTask</code>. 
 * Implementa la interfaz ITaskFactory.
 */
public class U3DDisplayConditionedMsgTaskFactory implements ITaskFactory {

    /**
	 * Crea una nueva <code>U3DDisplayConditionedMsgTask</code>
	 * 
	 * @return <code>ITask</code> La tarea generada
	 */
    public ITask createTask() {
        return new U3DDisplayConditionedMsgTask();
    }

    /**
	 * Retorna el identificador unico de la factoria
	 * 
	 * @return <code>String</code> El identificador unico
	 */
    public ETask getId() {
        return ETask.DISPLAY_CONDITIONED_MESSAGE;
    }
}
