package app.sgf.action;

import go.GoGame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import sgf.GameTree;
import sgf.GoNode;
import sgf.property.SetupProperty;

public class AddSetupPropertyAction extends AbstractAction {

    private static final String ADD_SETUP = java.util.ResourceBundle.getBundle("app/resource/Resource").getString("Add");

    private GoGame goGame;

    public AddSetupPropertyAction(GoGame goGame) {
        putValue(Action.NAME, ADD_SETUP);
        this.goGame = goGame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GoNode node = goGame.getGameTree().getCurrentNode();
        if (node.hasSetupProperty()) {
            return;
        } else if (node.hasMoveProperty()) {
            return;
        } else {
            SetupProperty sp = new SetupProperty();
            node.setSetupProperty(sp);
            GameTree tree = goGame.getGameTree();
            tree.fireNodeStateChanged();
        }
    }
}
