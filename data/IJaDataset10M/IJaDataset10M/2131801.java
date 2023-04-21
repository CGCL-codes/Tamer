package ru.nsu.ccfit.pm.econ.controller.player;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.Inject;
import ru.nsu.ccfit.pm.econ.common.IGameEventHandler;
import ru.nsu.ccfit.pm.econ.common.controller.player.IGameEventConsumer;
import ru.nsu.ccfit.pm.econ.common.controller.player.IGameEventGateway;
import ru.nsu.ccfit.pm.econ.common.engine.events.IUGameEvent;
import ru.nsu.ccfit.pm.econ.modules.names.InPlayerController;

/**
 * Game event gateway.
 * @see IGameEventGateway
 * 
 * @author dragonfly
 */
public class GameEventGateway implements IGameEventGateway, IGameEventHandler {

    static final Logger logger = LoggerFactory.getLogger(GameEventGateway.class);

    private Set<IGameEventConsumer> consumers = new HashSet<IGameEventConsumer>();

    private IGameEventHandler eventHandler;

    @Override
    public void addGameEventConsumer(IGameEventConsumer consumer) {
        synchronized (consumers) {
            consumers.add(consumer);
        }
    }

    @Override
    public void removeGameEventConsumer(IGameEventConsumer consumer) {
        synchronized (consumers) {
            consumers.remove(consumer);
        }
    }

    @Override
    public void sendEvent(IUGameEvent event) {
        eventHandler.handleEvent(event);
    }

    @Override
    public void handleEvent(IUGameEvent event) {
        boolean processed = false;
        synchronized (consumers) {
            for (IGameEventConsumer consumer : consumers) {
                try {
                    processed |= consumer.processEvent(event);
                } catch (Exception e) {
                    logger.warn("Consumer {} failed with {}", consumer, e.getMessage());
                    if (logger.isDebugEnabled()) e.printStackTrace();
                }
            }
        }
        if (!processed) logger.warn("Event {} has not been processed by anyone", event);
    }

    @Inject
    public void setGameEventHandler(@InPlayerController IGameEventHandler handler) {
        if (eventHandler != null) logger.warn("redefining eventHandler");
        eventHandler = handler;
    }
}
