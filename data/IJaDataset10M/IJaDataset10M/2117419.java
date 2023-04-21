package macaw.plugins;

import macaw.MacawMessages;
import macaw.system.SessionProperties;
import macaw.system.UserInterfaceFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JFileChooser;

public class DDI3FileWriterPlugin extends AbstractMacawPlugin implements WorkBenchPlugin {

    public DDI3FileWriterPlugin() {
        String pluginTitle = MacawMessages.getMessage("ddi3WriterPlugin.title");
        setDisplayName(pluginTitle);
        String pluginDescription = MacawMessages.getMessage("ddi3WriterPlugin.description");
        setDescription(pluginDescription);
    }

    public void run() throws Exception {
        SessionProperties sessionProperties = getSessionProperties();
        UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
        DDI3FileWriter ddi3FileWriter = new DDI3FileWriter(sessionProperties);
        JFileChooser fileChooser = userInterfaceFactory.createFileChooser();
        DDI3FileFilter ddiFileFilter = new DDI3FileFilter();
        fileChooser.addChoosableFileFilter(ddiFileFilter);
        int result = fileChooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File selectedFile = fileChooser.getSelectedFile();
        selectedFile = ddiFileFilter.getFileWithExtension(selectedFile);
        FileWriter fileWriter = new FileWriter(selectedFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        DDI3FileWriter ddi3Writer = new DDI3FileWriter(sessionProperties);
        ddi3Writer.writeVariables(printWriter);
        printWriter.flush();
        printWriter.close();
    }
}
