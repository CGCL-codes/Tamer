package net.sf.doolin.app.sc.game.contributor;

import net.sf.doolin.app.sc.game.SCGame;
import net.sf.doolin.app.sc.game.model.Player;
import net.sf.doolin.app.sc.game.state.SCGameState;
import org.springframework.stereotype.Service;

@Service
public class SCClientStateStatsContributor extends AbstractSCClientStateContributor {

    public SCClientStateStatsContributor() {
        super(SCClientStateContributor.STATS);
    }

    @Override
    public void contribute(SCGameState state, SCGame game) {
        Player player = getLocalPlayer(game, state);
        String export = player.getStats().getExport();
        state.getLocalPlayer().getStats().setExport(export);
    }
}
