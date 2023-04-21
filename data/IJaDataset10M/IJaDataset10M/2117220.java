package ch.jester.ui.tournament.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.navigator.CommonNavigator;
import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.model.Tournament;
import ch.jester.ui.tournament.cnf.TournamentNavigator;

/**
 * Handler für den Turnier-Editor
 *
 */
public class EditTournamentHandler extends AbstractCommandHandler implements IHandler {

    private Tournament selectedTournament;

    @Override
    public Object executeInternal(ExecutionEvent event) {
        selectedTournament = getFirstSelectedAs(Tournament.class);
        if (selectedTournament != null) {
            openEditor(selectedTournament);
            CommonNavigator cn = (CommonNavigator) getView(TournamentNavigator.ID);
            cn.getCommonViewer().refresh();
        }
        return null;
    }
}
