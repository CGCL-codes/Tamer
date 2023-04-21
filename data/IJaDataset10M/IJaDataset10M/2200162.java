package ui.swing.actions;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import kits.TdKit;
import managers.TDManager;
import org.apache.commons.io.FileUtils;
import ui.MainFrame;
import ui.panels.KitsPanel;
import exceptions.VdrumException;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class BrowseAction extends FileAction {

    private final KitsPanel kitsPanel;

    public BrowseAction(MainFrame mainFrame, KitsPanel kitsPanel) {
        this(mainFrame, kitsPanel, true);
    }

    public BrowseAction(MainFrame mainFrame, KitsPanel kitsPanel, boolean withIcon) {
        super(mainFrame, "browse", withIcon);
        this.kitsPanel = kitsPanel;
        config.get("browse").read(this);
        if (!withIcon) {
            setSmallIcon(null);
        }
    }

    @Override
    protected void handleAction(final File file) throws IOException, InvalidMidiDataException, VdrumException {
        byte[] bytes = FileUtils.readFileToByteArray(file);
        TdKit[] kits = TDManager.bytesToKits(bytes);
        for (TdKit kit : kits) {
            if (kit != null) {
                kitsPanel.addKit(kit);
            }
        }
    }
}
