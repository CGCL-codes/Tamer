package seabattle.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import seabattle.client.dto.UserDto;
import seabattle.client.dto.FieldDto;
import java.util.List;

public interface BattleService extends RemoteService {

    /**
     * system method to check if server alive.
     */
    public String ping();

    /**
     * Login
     * @return return false if user with the name already exists
     */
    boolean login(String name);

    /**
     * Method to create serverside field model.
     * @param shipDtoList
     * @return true if Navy was build successfully.
     * false if shipDtoList didn't pass validation rules
     * like number, size of ships and so on.
     */
    boolean buildNavy(List shipDtoList);

    /**
     * Gets all ysers who already built navy and ready to fight.
     * @return List of UserDto;
     */
    List getUsersReadyToFight();

    /**
     * Choses enemy to fight.
     * @param user
     * @return true if user accepted your challenge.
     */
    boolean chooseEnemy(UserDto user);

    /**
     * Display if someone challenged you to fight
     * @return user who challenged you to fight.
     */
    UserDto showIfSomeoneChoseMe();

    /**
     * Starts fight.
     * @return List of CellDto to display your field
     */
    List startFight();

    /**
     * This method is called when you click enemy field
     * @param x - x coordinate of you shoot
     * @param y - y coordinate of you shoot
     * @return FieldDto - data to display enemy field and metadata with result
     * of your shoot to decide what to do next: shoot again, or wait for enemy shoot,
     * or display end of the game message.
     */
    FieldDto shoot(int x, int y);

    /**
     * Displays the result of your enemy shoot.
     * Returns FieldDto: data to display your field and metadata with result
     * of your enemy shoot to decide what to do next: shoot, or wait for enemy to
     * shoot again, or display end of the game message.
     */
    FieldDto showEnemyShoot();
}
