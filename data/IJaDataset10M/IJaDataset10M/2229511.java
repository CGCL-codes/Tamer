package py.edu.ucom.cadira.game.war;

import py.edu.ucom.cadira.net.User;

/**
 * 
 * @author largonet
 *
 */
public interface GameInterface {

    /**
     * 
     * @param user
     * @param type 0 si es player y 1 si es es watcher
     * @return
     * @throws GameLogicException
     */
    public WarUser userRegistration(User user, int type);

    /**
     * Parametro para que el usuario realice un movimiento
     * @param movement 
     * @param user 
     * @param m
     */
    public void moveTab(Movement movement, WarUser user);

    /**
     * Metodo para setear un tablero
     * Se usa cuando va a empezar el juego y los usaurios arman sus tableros
     * @param user User del tablero
     * @param tablero Board con las fichas ubicadas por el usuario
     */
    public void setBoard(WarUser user, Board tablero);

    /**
     * Metodo para iniciar el juego cuando los usuario que estan listos
     */
    public void startGame();

    /**
     *  Metodo para eliminar el registro de un usuario
     *  Esto en caso de que el usuario salga de una mesa o cierre su conexion
     * @param user User que se eliminara el registro
     */
    public void unregisterUser(WarUser user);

    public void addGameListener(GameListener listener);

    public WarUser getPlayer(int numPlayer);

    public Board getBoard(WarUser user);

    public boolean getStatusBoard();

    public WarUser getWarUser(User user);

    public Board getBoard();

    public void setStatusGame(boolean status, Object obj);

    public void setMesaId(Integer mesaId);
}
