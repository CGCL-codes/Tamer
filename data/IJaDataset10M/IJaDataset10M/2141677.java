package se.trixon.nbmpc.core.actions;

import java.awt.event.ActionEvent;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDPlayerException;
import se.trixon.nbmpc.Monitor;
import se.trixon.nbmpc.Mpc;

/**
 *
 * @author Patrik Karlsson <patrik@trixon.se>
 */
public class ControlPrevAction extends ControlAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Mpc.getInstance().getMpdPlayer().playPrev();
            Monitor.outln("playPrev");
        } catch (MPDConnectionException ex) {
            Monitor.errln(ex.getMessage());
        } catch (MPDPlayerException ex) {
            Monitor.errln(ex.getMessage());
        }
    }
}
