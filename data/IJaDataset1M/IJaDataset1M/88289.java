package ar.com.jwa.services;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import ar.com.jwa.domain.Game;
import ar.com.jwa.domain.User;

public class GameServiceTestCase extends ServiceTestCase {

    @Autowired
    public GameLocalService gameLocalService;

    @Autowired
    public UserLocalService userLocalService;

    public void testAddPlayersGame() {
        User player = new User("Nicolas", "Gonzalez", "NIKO", "123456", "nicolas.hernan.gonzalez@gmail.com", "15559302769");
        User player2 = new User("Nicolas", "Gonzalez", "NIKO", "123456", "nicolas.hernan.gonzalez@gmail.com", "15559302769");
        Game game = new Game(new Date());
        game.addPlayer(player2);
        game.addPlayer(player);
        this.gameLocalService.saveGame(game);
    }

    public void testGetUnplayedGames() {
        User user = userLocalService.getUserById(new Long(1));
        this.getGameLocalService().hasToConfirmSomething(user);
    }

    /**
	 * @return the gameLocalService
	 */
    public GameLocalService getGameLocalService() {
        return gameLocalService;
    }

    /**
	 * @param gameLocalService the gameLocalService to set
	 */
    public void setGameLocalService(GameLocalService gameLocalService) {
        this.gameLocalService = gameLocalService;
    }

    /**
	 * @return the userLocalService
	 */
    public UserLocalService getUserLocalService() {
        return userLocalService;
    }

    /**
	 * @param userLocalService the userLocalService to set
	 */
    public void setUserLocalService(UserLocalService userLocalService) {
        this.userLocalService = userLocalService;
    }
}
