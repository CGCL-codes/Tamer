package net.sf.doolin.app.sc.client.controller;

import net.sf.doolin.app.sc.client.connector.ClientConnector;
import net.sf.doolin.app.sc.client.connector.ConnectorStartType;
import net.sf.doolin.app.sc.client.util.SCPreferences;
import net.sf.doolin.app.sc.engine.EngineClient;
import net.sf.doolin.app.sc.engine.EngineController;
import net.sf.doolin.app.sc.engine.EngineFactory;
import net.sf.doolin.app.sc.engine.InstanceID;
import net.sf.doolin.app.sc.game.SCClientResponse;
import net.sf.doolin.app.sc.game.SCClientState;
import net.sf.doolin.app.sc.game.SCGameGenerator;
import net.sf.doolin.gui.app.GUIApplication;
import net.sf.doolin.gui.service.GUIPreferences;

public abstract class AbstractHostingGameController<C extends AbstractHostingGameControllerConfig> extends AbstractGameController<C> {

    public static final String QUALIFIER_SHARED_ENGINE_FACTORY = "sharedEngineFactory";

    private final EngineFactory<SCGameGenerator, SCClientState, SCClientResponse> engineFactory;

    protected EngineController<SCGameGenerator, SCClientState, SCClientResponse> engine;

    protected InstanceID gameID;

    public AbstractHostingGameController(GUIApplication application, C config, ClientConnector clientConnector, EngineFactory<SCGameGenerator, SCClientState, SCClientResponse> engineFactory) {
        super(application, config, clientConnector);
        this.engineFactory = engineFactory;
    }

    @Override
    public void dispose() {
        try {
            super.dispose();
            if (this.engine != null && this.gameID != null) {
                this.engine.unloadInstance(this.gameID);
            }
            if (this.engine != null && this.engine.isStarted()) {
                this.engine.stop();
            }
        } finally {
            this.gameID = null;
            this.engine = null;
        }
    }

    @Override
    protected EngineClient<SCClientState, SCClientResponse> getEngineClient() {
        return this.engine;
    }

    @Override
    protected InstanceID getGameID() {
        return this.gameID;
    }

    @Override
    protected ConnectorStartType getStartType() {
        return ConnectorStartType.CAN_START;
    }

    @Override
    public void init() {
        super.init();
        this.engine = (EngineController<SCGameGenerator, SCClientState, SCClientResponse>) this.engineFactory.createEngine("Shared");
    }

    @Override
    public void start() {
        GUIPreferences preferences = getPreferences();
        SCPreferences.setGameID(preferences, SCPreferences.LAST_SHARED_GAME_ID, this.gameID);
        preferences.saveMemento(getConfig());
        preferences.save();
        if (!this.engine.isStarted()) {
            this.engine.start();
        }
        this.engine.loadInstance(this.gameID);
        super.start();
    }
}
