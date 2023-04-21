package es.org.chemi.games.sokoban.actions;

import org.eclipse.jface.action.Action;
import es.org.chemi.games.sokoban.SokobanPlugin;
import es.org.chemi.games.sokoban.ui.ChangeLevelDialog;
import es.org.chemi.games.sokoban.ui.MainView;

public class ChangeLevelAction extends Action {

    private MainView view = null;

    public ChangeLevelAction(String label, MainView view) {
        super(label);
        this.view = view;
    }

    public void run() {
        SokobanPlugin.trace(this.getClass().getName(), "Change level solicited.");
        ChangeLevelDialog dialog = new ChangeLevelDialog(view.getViewSite().getShell(), view.getMap());
        dialog.open();
        view.setFocus();
    }
}
