package app.sgf.action;

import go.GoGame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import sgf.GameTree;

public class GoTopAction extends AbstractAction {

    private static final String TOP = java.util.ResourceBundle.getBundle("app/resource/Resource").getString("TopNode");

    private static final String ICON = "image/go-first.png";

    private GoGame goGame;

    public GoTopAction(GoGame goGame) {
        ClassLoader cl = this.getClass().getClassLoader();
        Icon icon = new ImageIcon(cl.getResource(ICON));
        putValue(Action.NAME, TOP);
        putValue(Action.SHORT_DESCRIPTION, TOP);
        putValue(Action.SMALL_ICON, icon);
        putValue(Action.LARGE_ICON_KEY, icon);
        this.goGame = goGame;
    }

    public void actionPerformed(ActionEvent e) {
        GameTree tree = goGame.getGameTree();
        tree.top();
    }
}
