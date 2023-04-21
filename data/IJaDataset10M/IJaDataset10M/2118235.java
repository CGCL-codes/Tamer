package net.sf.jawp.game.server.gui;

import java.util.Collection;
import javax.security.auth.login.LoginException;
import net.sf.jawp.api.service.JAWPGameService;
import net.sf.jawp.game.service.JAWPRMIHelper;
import net.sf.jawp.game.system.JAWPGameController;
import net.sf.jawp.gf.api.domain.GameWorld;
import net.sf.jawp.gf.api.domain.Player;
import net.sf.jawp.gf.api.domain.User;
import net.sf.jawp.gf.api.services.LoginService;
import net.sf.jawp.gf.api.services.SessionService;
import net.sf.jawp.gf.api.services.UserService;
import net.sf.jawp.gf.persistence.PersistenceController;
import net.sf.jawp.util.Log;
import net.sf.jawp.util.PasswordUtil;

/**
 * Server GUI data.
 * All server windows should exchange information using this class instance.
 * @author jarek
 */
public final class ServerDataManager {

    public static final String DEFAULT_ADMIN_LOGIN = "admin";

    public static final String DEFAULT_ADMIN_PASSWORD = "foobar";

    public static final String DEFAULT_GAMEWORLD_NAME = "test";

    private static final Log LOG = Log.getLog(ServerDataManager.class);

    private static final String DEFAULT_STORAGE_FOLDER = "games";

    private static final String DEFAULT_STORAGE_GAME_NAME = "test";

    private JAWPGameController gameController;

    private ServerDataManager() {
    }

    public static ServerDataManager create() {
        return new ServerDataManager();
    }

    public boolean isStoredSystemAvailable() {
        return PersistenceController.isStoredSystemAvailable(getStorageName());
    }

    /**
	 * loads game latest snapshot
	 */
    public void loadGame() {
        assert getGameController() == null;
        final JAWPGameController game = new JAWPGameController(getStorageName(), true);
        setGameController(game);
        FramesManager.getInstance().fireGameServerReady();
    }

    /**
	 * creates new game - erases old one!!!
	 */
    public void newGame() {
        assert getGameController() == null;
        final JAWPGameController game = new JAWPGameController(getStorageName(), false);
        final String adminPassEnc = PasswordUtil.encrypt(DEFAULT_ADMIN_PASSWORD);
        final User admin = game.getUserService().registerUser(DEFAULT_ADMIN_LOGIN, adminPassEnc);
        final LoginService<JAWPGameService> logSrv = game.getUserService().initLogin(admin.getLogin());
        setGameController(game);
        FramesManager.getInstance().fireGameServerReady();
    }

    private String getStorageName() {
        return DEFAULT_STORAGE_FOLDER + "/" + DEFAULT_STORAGE_GAME_NAME;
    }

    /**
	 * saves game snapshot
	 */
    public void saveGame() {
        assert getGameController() != null;
    }

    public JAWPGameController getGameController() {
        return gameController;
    }

    private void setGameController(final JAWPGameController gameController) {
        this.gameController = gameController;
        if (this.gameController != null) {
            startRMI();
        }
    }

    public void stop() {
        assert getGameController() != null;
        getGameController().stopTimer();
    }

    public void start() {
        assert getGameController() != null;
        getGameController().startTimer();
    }

    private void startRMI() {
        JAWPRMIHelper.getInstance().startRMI(getUserService());
    }

    private void stopRMI() {
        JAWPRMIHelper.getInstance().stopRMI();
    }

    public boolean isRunning() {
        assert getGameController() != null;
        return getGameController().isRunning();
    }

    public boolean isGame() {
        return getGameController() != null;
    }

    public void destroyGame() {
        assert getGameController() != null;
        getGameController().dispose();
        setGameController(null);
    }

    public JAWPGameService getTestService() {
        final UserService<JAWPGameService> usrSrv = getGameController().getUserService();
        final LoginService<JAWPGameService> logSrv = usrSrv.initLogin(ServerDataManager.DEFAULT_ADMIN_LOGIN);
        try {
            final SessionService<JAWPGameService> sesSrv = logSrv.login(PasswordUtil.encryptMix(PasswordUtil.encrypt(ServerDataManager.DEFAULT_ADMIN_PASSWORD), logSrv.getServerSeed()));
            final Collection<GameWorld> games = sesSrv.getGames();
            for (final GameWorld gw : games) {
                if (gw.getName().equals(ServerDataManager.DEFAULT_GAMEWORLD_NAME)) {
                    final Collection<Player> players = sesSrv.getUserPlayersForGame(gw.getKey());
                    for (final Player pl : players) {
                        return sesSrv.getGameService(gw.getKey(), pl.getKey());
                    }
                }
            }
        } catch (final LoginException le) {
            LOG.error(le, le);
            throw new RuntimeException(le);
        }
        return null;
    }

    public UserService<JAWPGameService> getUserService() {
        assert this.gameController != null;
        return getGameController().getUserService();
    }

    public void dispose() {
        stopRMI();
    }
}
