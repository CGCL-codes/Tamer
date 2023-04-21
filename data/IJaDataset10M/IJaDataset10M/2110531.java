package org.identifylife.key.player.gwt.client.actions;

import org.identifylife.key.player.gwt.client.PlayerActions;
import org.identifylife.key.player.gwt.client.PlayerManager;
import org.identifylife.key.player.gwt.client.PlayerStates;
import org.identifylife.key.player.gwt.client.context.Context;
import com.allen_sauer.gwt.log.client.Log;

/**
 * @author dbarnier
 *
 */
public class NextFeaturesAction extends PlayerAction {

    public NextFeaturesAction(PlayerManager playManager) {
        super(playManager, PlayerActions.NEXT_FEATURES);
        playManager.getContextManager().registerListener(PlayerStates.FEATURE_COUNT, this);
        setEnabled(false);
    }

    @Override
    public void contextChanged(Context state, Object value) {
        if (state.hasName(PlayerStates.FEATURE_COUNT)) {
            int featureCount = state.getIntValue();
            setEnabled(featureCount < playManager.getTotalFeatureCount());
        }
    }

    @Override
    public void actionPerformed() {
        Log.debug("actionPerformed(): action: " + this);
        if (isEnabled()) {
            playManager.nextFeatures();
        }
    }
}
