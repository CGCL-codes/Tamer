package ch.drystayle.jghost.game;

import org.apache.log4j.Logger;
import ch.drystayle.jghost.JGhost;
import ch.drystayle.jghost.common.Constants;
import ch.drystayle.jghost.i18n.MessageFactory;
import ch.drystayle.jghost.i18n.Messages;
import ch.drystayle.jghost.map.Map;
import ch.drystayle.jghost.protocol.IncomingJoinPlayer;
import ch.drystayle.jghost.util.TimeUtil;

public class AdminGame extends Game {

    /** The Logger to use in this class. */
    private static final Logger LOG = Logger.getLogger(BaseGame.class);

    private String password;

    public AdminGame(JGhost host, Map map, SaveGame saveGame, short hostPort, char gameState, String gameName, String password) {
        super(host, map, saveGame, hostPort, gameState, gameName, "", "", "");
        m_VirtualHostName = "|cFFC04040Admin";
        m_MuteLobby = true;
        this.password = password;
    }

    public boolean Update(Object fd) {
        m_LastReservedSeen = TimeUtil.getTime();
        return super.Update(fd);
    }

    public void SendWelcomeMessage(GamePlayer player) {
        SendChat(player, MessageFactory.create("GHost++ Admin Game                    http://forum.codelain.com/"));
        SendChat(player, MessageFactory.create("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
        SendChat(player, MessageFactory.create("Commands: addadmin, autohost, checkadmin, countadmins, deladmin"));
        SendChat(player, MessageFactory.create("Commands: disable, enable, end, exit, getgame, getgames"));
        SendChat(player, MessageFactory.create("Commands: hostsg, load, loadsg, map, password, priv, privby"));
        SendChat(player, MessageFactory.create("Commands: pub, pubby, quit, saygame, saygames, unhost"));
    }

    public void EventPlayerJoined(PotentialPlayer potential, IncomingJoinPlayer joinPlayer) {
        super.EventPlayerJoined(potential, joinPlayer);
    }

    public void EventPlayerBotCommand(GamePlayer player, String command, String payload) {
        String User = player.GetName();
        if (player.GetLoggedIn() || this.password.isEmpty()) {
            LOG.info("[ADMINGAME] admin [" + User + "] sent command [" + command + "] with payload [" + payload + "]");
            if ("priv".equals(command) && !payload.isEmpty()) this.getGhost().CreateGame(Constants.GAME_PRIVATE, false, payload, User, User, "", false);
        } else {
            LOG.info("User [" + User + "] sent command [" + command + "] with payload [" + payload + "]");
        }
        if ("password".equals(command) && !player.GetLoggedIn()) {
            if (!this.password.isEmpty() && payload == this.password) {
                LOG.info("User [" + User + "] logged in");
                SendChat(player, MessageFactory.create(Messages.ADMIN_LOGGED_IN));
                player.SetLoggedIn(true);
            } else {
                int LoginAttempts = player.GetLoginAttempts() + 1;
                player.SetLoginAttempts(LoginAttempts);
                LOG.info("User [" + User + "] login attempt failed");
                SendChat(player, MessageFactory.create(Messages.ADMIN_INVALID_PASSWORD, LoginAttempts));
                if (LoginAttempts >= 1) {
                    player.SetDeleteMe(true);
                    player.SetLeftReason("was kicked for too many failed login attempts");
                    OpenSlot(GetSIDFromPID(player.GetPID()), false);
                }
            }
        }
    }
}
