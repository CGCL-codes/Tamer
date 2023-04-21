package net.jgf.logic.action;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.system.Jgf;

@Configurable
public class QuitAction extends BaseLogicAction {

    /**
	 * Configures this object from Config.
	 */
    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }

    @Override
    public void perform(Object arg) {
        Jgf.getApp().dispose();
    }
}
