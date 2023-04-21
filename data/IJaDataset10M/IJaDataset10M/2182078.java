package jmplayer.ui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import jmplayer.ui.PlayerFrame;

/**
 * 
 * @author lois
 * @version $Revision: 1.2 $
 *
 */
public class PlayerForwardAction extends AbstractAction {

    private static final long serialVersionUID = 3222156473663604132L;

    public PlayerForwardAction() {
        super(">>");
    }

    public void actionPerformed(ActionEvent e) {
        PlayerFrame.getInstance().getPlayer().fwd();
    }
}
