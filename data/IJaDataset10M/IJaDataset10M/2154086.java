package ui.swing.actions;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import kits.TdKit;
import managers.TDManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import ui.MainFrame;
import ui.panels.KitsPanel;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class SaveAction extends FileAction implements ListDataListener {

    private final KitsPanel kitsPanel;

    public SaveAction(MainFrame mainFrame, KitsPanel kitsPanel) {
        this(mainFrame, kitsPanel, true);
    }

    public SaveAction(MainFrame mainFrame, KitsPanel kitsPanel, boolean withIcon) {
        super(mainFrame, "save", withIcon);
        this.kitsPanel = kitsPanel;
        this.kitsPanel.addListDataListener(this);
        setEnabledByKits();
    }

    @Override
    protected void handleAction(File file) throws InvalidMidiDataException, IOException {
        final TdKit[] kitsInList = kitsPanel.getKits();
        if (!FilenameUtils.isExtension(file.getName(), "syx")) {
            String name = file.getAbsolutePath() + ".syx";
            file = new File(name);
        }
        final SysexMessage messageFromManager = TDManager.kitsToSysexMessage(kitsInList);
        FileUtils.writeByteArrayToFile(file, messageFromManager.getMessage());
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        setEnabledByKits();
    }

    private void setEnabledByKits() {
        setEnabled(kitsPanel.numberOfKits() > 0);
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
    }
}
