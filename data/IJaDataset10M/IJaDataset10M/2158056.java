package fr.soleil.mambo.options.sub;

import fr.soleil.mambo.components.OperatorsList;
import fr.soleil.mambo.containers.sub.dialogs.options.OptionsLogsTab;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.options.PushPullOptionBook;
import fr.soleil.mambo.options.ReadWriteOptionBook;

public class LogsOptions extends ReadWriteOptionBook implements PushPullOptionBook {

    public static final String LEVEL = "LEVEL";

    public static final String KEY = "logs";

    /**
     * 
     */
    public LogsOptions() {
        super(KEY);
    }

    public void fillFromOptionsDialog() {
        OptionsLogsTab logsTab = OptionsLogsTab.getInstance();
        String logLevel_s = logsTab.getLogLevel();
        this.content.put(LEVEL, logLevel_s);
    }

    public void push() {
        ILogger logger = LoggerFactory.getCurrentImpl();
        String level_s = (String) this.content.get(LEVEL);
        int lvl;
        if (level_s != null) {
            lvl = logger.getTraceLevel(level_s);
        } else {
            lvl = ILogger.LEVEL_DEBUG;
            level_s = ILogger.DEBUG;
        }
        logger.setTraceLevel(lvl);
        OptionsLogsTab logsTab = OptionsLogsTab.getInstance();
        OperatorsList comboBox = logsTab.getComboBox();
        comboBox.setSelectedItem(level_s);
    }
}
