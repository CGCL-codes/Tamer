package ui.swing.actions;

import java.awt.event.ActionEvent;
import midi.MidiHandler;
import ui.MainFrame;
import ui.event.ConnectionEvent;
import ui.event.ConnectionListener;
import ui.panels.MidiSourcePanel;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class MidiSourceAction extends BaseAction implements ConnectionListener {

    private final MainFrame mainFrame;

    private final MidiHandler midiHandler;

    public MidiSourceAction(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.midiHandler = this.mainFrame.getMidiHandler();
        this.midiHandler.addConnectionListener(this);
        config.get("midisource").read(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MidiSourcePanel.showDialog(mainFrame, midiHandler.getMidiDeviceInfo());
    }

    @Override
    public void connected(ConnectionEvent connectionEvent) {
        setEnabled(false);
    }

    @Override
    public void disconnected() {
        setEnabled(true);
    }
}
