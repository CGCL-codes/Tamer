package client.game.scrum.panels;

import javax.swing.JButton;
import client.game.scrum.ui.AItemFrame;

public interface FramesDesktop {

    public abstract void delete(AItemFrame bif);

    public abstract void attachNewButton(JButton btnNew);

    public abstract void add(AItemFrame frame);
}
