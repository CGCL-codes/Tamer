package net.jgf.view;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.UnsupportedOperationException;
import net.jgf.core.service.BaseService;
import net.jgf.core.state.State;

/**
 * <p>Void view manager. This view manager can be used when no view needs to be
 * used, for example when defining an application that runs in dedicated
 * server mode.</p>
 * @version 1.0
 */
@Configurable
public final class VoidViewManager extends BaseService implements ViewManager {

    private ViewState rootState = null;

    /**
     * Constructor.
     */
    public VoidViewManager() {
        this.rootState = new ViewStateNode();
        this.rootState.setId("!voidViewManagerRootNode");
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }

    @Override
    public State getRootState() {
        return rootState;
    }

    @Override
    public void input(float tpf) {
    }

    @Override
    public void render(float tpf) {
    }

    @Override
    public void setRootState(ViewState rootState) {
        throw new UnsupportedOperationException("Can't set the root ViewState of a VoidViewManager");
    }

    @Override
    public void update(float tpf) {
    }
}
