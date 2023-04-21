package ru.nsu.ccfit.pm.econ.common.view.client;

import java.util.List;
import javax.annotation.Nullable;
import ru.nsu.ccfit.pm.econ.common.controller.clientnet.IClientNetworkController;
import ru.nsu.ccfit.pm.econ.common.net.IUServerProperties;

/**
 * Interface with callbacks for network events.
 * <p>Components that may use this interface:
 * <ul><li>ClientNetworkingController</li></ul>
 * </p>
 * <p>This interface should be implemented by client view.</p>
 * @see IClientNetworkController
 * @author dragonfly
 */
public interface INetworkEvents {

    /**
	 * Called when automatic server lookup is started.
	 */
    void onServerLookupStart();

    /**
	 * Called when automatic server lookup is stopped.
	 */
    void onServerLookupStop();

    /**
	 * Called when list of visible servers, retrieved via automatic
	 * server lookup, changes. 
	 * @param serverList New list of currently visible servers.
	 */
    void onServerLookupUpdate(List<? extends IUServerProperties> serverList);

    /**
	 * Called upon successful connect to the server. 
	 * <p>Note that only connect itself is considered, no additional 
	 * data transfer is required before this event can be fired.</p>
	 */
    void onConnect();

    /**
	 * Called upon disconnect, when automatic reconnect has failed or when 
	 * it is not applicable.
	 * <p>Upon actual disconnect only one of following four events is fired:
	 * <ol>
	 * <li>{@link #onDisconnect(Exception)};</li>
	 * <li>{@link #onReconnectAttempt(int, Exception)};</li>
	 * <li>{@link #onKick(String)};</li>
	 * <li>{@link #onBan(String)};</li>
	 * </ol>
	 * Handlers of these events should be able to successfully call any method 
	 * of {@link IClientNetworkController} interface.
	 * </p>
	 * @param cause Exception that caused the event, if available. Otherwise
	 * 				<tt>null</tt>.
	 */
    void onDisconnect(@Nullable Exception cause);

    /**
	 * Called before reconnect attempt, i.e. just after disconnect happened,
	 * but before another attempt to connect has commenced.
	 * @param numberOfAttempts Number of previously failed attempts to 
	 * 							reconnect (in a row). When connection succeeds 
	 * 							this counter is zeroed.
	 * @param cause Exception that caused the disconnect, if available. 
	 * 				Otherwise <tt>null</tt>.
	 * @see #onDisconnect(Exception)
	 */
    void onReconnectAttempt(int numberOfAttempts, @Nullable Exception cause);

    /**
	 * Called after client was kicked from server. Note that this event is 
	 * fired after client has received kick event from server, and <i>after</i> 
	 * it resulted in a disconnect, but before any successive connect attempt.
	 * <p>Controller will not automatically attempt to reconnect after kick.</p> 
	 * @param reason Kick reason as reported by kick event from server.
	 * @see #onDisconnect(Exception)
	 */
    void onKick(String reason);

    /**
	 * Called after client was banned on server. Note that this event is 
	 * fired after client has received ban event from server, and <i>after</i> 
	 * it resulted in a disconnect, but before any successive connect attempt.
	 * <p>Controller will not automatically attempt to reconnect after client
	 * has been banned. Depending on server implementation any further 
	 * reconnect attempts (including manual) might result either in failure or
	 * in similar ban events.</p> 
	 * @param reason Kick reason as reported by kick event from server.
	 * @param reason
	 */
    void onBan(String reason);
}
