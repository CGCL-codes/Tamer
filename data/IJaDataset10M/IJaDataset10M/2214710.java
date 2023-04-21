package org.mypomodoro.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.mypomodoro.Main;
import org.mypomodoro.gui.todo.Pomodoro;

/**
 *  Mute sound button
 * 
 * @author Phil Karoo
 */
public class MuteButton extends JButton {

    private static final long serialVersionUID = 20110814L;

    private final ImageIcon muteIcon = new ImageIcon(Main.class.getResource("/images/mute.png"));

    private final ImageIcon soundIcon = new ImageIcon(Main.class.getResource("/images/sound.png"));

    private boolean isMuteIcon = true;

    public MuteButton(final Pomodoro pomodoro) {
        setMuteIcon();
        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMuteIcon) {
                    setSoundIcon();
                    pomodoro.mute();
                    isMuteIcon = false;
                } else {
                    setMuteIcon();
                    pomodoro.unmute();
                    isMuteIcon = true;
                }
            }
        });
    }

    private void setSoundIcon() {
        setIcon(soundIcon);
    }

    private void setMuteIcon() {
        setIcon(muteIcon);
    }
}
