package de.berlin.fu.inf.gameai.go.kifoos;

import de.berlin.fu.inf.gameai.game.operation.Controller;
import de.berlin.fu.inf.gameai.game.operation.SimpleSearchController;
import de.berlin.fu.inf.gameai.game.search.ContinuesSearch;
import de.berlin.fu.inf.gameai.go.KifooFactory;
import de.berlin.fu.inf.gameai.go.goban.Goban;
import de.berlin.fu.inf.gameai.go.rules.TimeControlFactory;

public abstract class AbstractDefaultControllerFactory<G extends Goban> extends TimeControlFactory implements KifooFactory<G> {

    public abstract G createGoban(int size, float komi);

    public abstract ContinuesSearch<G> createSearch(G goban);

    public Controller<G> createController(final G goban) {
        final SimpleSearchController<G> control = new SimpleSearchController<G>(createSearch(goban));
        control.setup(goban);
        return control;
    }

    public G createGoban(final int size, final float komi, final int handicap) {
        final G goban = createGoban(size, komi);
        goban.reset();
        return goban;
    }
}
