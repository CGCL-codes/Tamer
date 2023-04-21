package nakayo.loginserver.network;

import com.aionemu.commons.network.NioServer;
import com.aionemu.commons.network.ServerCfg;
import nakayo.loginserver.configs.Config;
import nakayo.loginserver.network.aion.AionConnectionFactoryImpl;
import nakayo.loginserver.network.gameserver.GsConnectionFactoryImpl;
import nakayo.loginserver.utils.ThreadPoolManager;

/**
 * This class create NioServer and store its instance.
 *
 * @author -Nemesiss-
 */
public class IOServer {

    /**
     * NioServer instance that will handle io.
     */
    private static final NioServer instance;

    static {
        ServerCfg aion = new ServerCfg(Config.LOGIN_BIND_ADDRESS, Config.LOGIN_PORT, "Aion Connections", new AionConnectionFactoryImpl());
        ServerCfg gs = new ServerCfg(Config.GAME_BIND_ADDRESS, Config.GAME_PORT, "Gs Connections", new GsConnectionFactoryImpl());
        instance = new NioServer(Config.NIO_READ_THREADS, ThreadPoolManager.getInstance(), gs, aion);
    }

    /**
     * @return NioServer instance.
     */
    public static NioServer getInstance() {
        return instance;
    }
}
