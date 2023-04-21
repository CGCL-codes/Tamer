package client.game.task.player;

import client.game.entity.Player;
import client.game.task.Task;
import com.jme.math.Vector3f;

/**
 * <Code>U3DMoveCharacterTask</code> es responsable de setear
 * en el view del player los nuevos parámetros de movimiento.
 * Para este proposito hace uso de la clase <code>ViewManager</code>.
 * Además se encarga de manejar las colisiones mediante el uso
 * de la clase <code>CollisionManager</code> y actualiza la posición
 * actual del mapa utilizando la clase <code>HudManager</code>.
 * También crea una tarea de movimiento que luego es subscripta al
 * <code>TaskManager</code> y es enviada al servidor. 
 * <Code>U3DMoveCharacterTask</code> es invocada cada vez que se
 * produce un movimiento (tipicamente cuando el usuario apreta una
 * tecla de movimiento). <code>U3DMoveCharacterTask</code> extiende
 * la funcionalidad de <code>Task</code>.
 * 
 * @author Sebastian Sampaoli (Javadoc)
 *
 */
public class U3DMoveCharacterTask extends Task {

    public U3DMoveCharacterTask() {
        super();
    }

    /**
	 * The <code>CharacterEntity</code> to be set.
	 */
    private Player character;

    private boolean adelante;

    private float movement = 0;

    @Override
    public boolean equals(Object o) {
        if (o instanceof U3DMoveCharacterTask) {
            U3DMoveCharacterTask given = (U3DMoveCharacterTask) o;
            if (given.character != null) return given.character.equals(this.character); else return this.character == null;
        }
        return false;
    }

    @Override
    public void execute() {
        if (this.character == null) return;
        Vector3f origin = this.character.getPosition();
        Vector3f destination = this.character.moveAction(adelante, movement);
        if (destination != null) {
            character.sendMoveMsg(origin, destination);
            character.updateHUD(0, destination);
        }
    }

    public void initTask(Player theCharacter, boolean isLocal, boolean adelante, float movement) {
        this.movement = movement;
        character = theCharacter;
        this.adelante = adelante;
    }
}
