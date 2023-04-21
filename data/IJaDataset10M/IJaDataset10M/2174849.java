package de.fu_berlin.inf.gmanda.startup;

import java.io.File;
import de.fu_berlin.inf.gmanda.gui.MainFrame;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.manager.UndoManagement;
import de.fu_berlin.inf.gmanda.util.Configuration;

public class Starter {

    MainFrame frame;

    Configuration configuration;

    UndoManagement manager;

    CommonService commonService;

    CommandLineOptions cmd;

    public Starter(MainFrame frame, Configuration configuration, UndoManagement manager, CommonService commonService, CommandLineOptions cmd) {
        this.manager = manager;
        this.frame = frame;
        this.configuration = configuration;
        this.commonService = commonService;
        this.cmd = cmd;
    }

    public void start() {
        frame.setVisible(true);
        String file = cmd.fileToOpen;
        if (file == null || !new File(file).exists()) {
            file = configuration.getProperty("lastProject", null);
        }
        if (file != null && new File(file).exists()) {
            final String fileToOpen = file;
            commonService.run(new Runnable() {

                public void run() {
                    manager.load(new File(fileToOpen));
                }
            }, "Could not load project");
        }
    }
}
