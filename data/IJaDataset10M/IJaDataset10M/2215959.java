package viewer.action;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viewer.config.TileConfig;
import viewer.core.MapWindowChangeListener;
import viewer.core.TileConfigListener;
import viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 *
 */
public class TileConfigAction extends ViewerAction implements MapWindowChangeListener, TileConfigListener {

    private static final long serialVersionUID = 2464732546694656245L;

    static final Logger logger = LoggerFactory.getLogger(TileConfigAction.class);

    private boolean enabled = true;

    private TileConfig config;

    /**
	 * Create a new action for choosing tileConfig.
	 * 
	 * @param viewer the viewer to use for.
	 * @param config the config to select.
	 */
    public TileConfigAction(Viewer viewer, TileConfig config) {
        super(viewer, null);
        this.config = config;
        viewer.addTileConfigListener(this);
        enabled = viewer.getTileConfig().getId() != config.getId();
    }

    @Override
    public Object getValue(String key) {
        if (key == Action.SMALL_ICON) {
            return null;
        } else if (key.equals(Action.NAME)) {
            return config.getName();
        } else if (key.equals(Action.SHORT_DESCRIPTION)) {
            return config.getName();
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getViewer().setTileConfig(config);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void changed() {
        firePropertyChange("enabled", null, null);
    }

    @Override
    public void tileConfigChanged() {
        boolean nowEnabled = getViewer().getTileConfig().getId() != config.getId();
        boolean emit = (enabled != nowEnabled);
        enabled = nowEnabled;
        if (emit) {
            changed();
        }
    }
}
