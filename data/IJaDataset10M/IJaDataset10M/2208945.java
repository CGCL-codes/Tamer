package frost.pluginmanager;

import java.io.File;
import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;
import frost.MainFrame;
import frost.SettingsClass;
import frost.fcp.FcpHandler;
import frost.fcp.NodeAddress;
import frost.gui.MessageFrame;

/**
 * @author saces
 *
 */
public class PluginRespinator {

    private final SettingsClass frostSettings;

    private final MainFrame mainFrame;

    private final PluginInfo pluginInfo;

    private Storage storage = null;

    protected PluginRespinator(PluginInfo plugininfo, SettingsClass frostsettings, MainFrame mainframe) {
        frostSettings = frostsettings;
        mainFrame = mainframe;
        pluginInfo = plugininfo;
    }

    public void makeNewMessage(String boardname, String subject, String text) {
        MessageFrame newMessageFrame = new MessageFrame(frostSettings, mainFrame);
        newMessageFrame.composeNewMessage(mainFrame.getTofTreeModel().getBoardByName(boardname), subject, "");
        newMessageFrame.addText(text);
    }

    public Storage getDataStorage(String storagename) {
        int pagePoolSize = 1 * 1024 * 1024;
        File sdir = new File("store/plugins");
        sdir.mkdirs();
        String databaseFilePath = sdir.getAbsolutePath() + '/' + storagename + ".dbs";
        storage = StorageFactory.getInstance().createStorage();
        storage.setProperty("perst.serialize.transient.objects", Boolean.TRUE);
        storage.setProperty("perst.concurrent.iterator", Boolean.TRUE);
        storage.setClassLoader(pluginInfo.getClassLoader());
        storage.open(databaseFilePath, pagePoolSize);
        return storage;
    }

    public String getFCPAdress() {
        NodeAddress na = FcpHandler.inst().getNodes().get(0);
        return na.hostName + ':' + na.port;
    }
}
