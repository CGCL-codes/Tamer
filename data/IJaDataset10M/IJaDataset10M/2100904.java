package net.sf.jmms.plugins.logger;

import net.sf.jmms.*;

public class LoggingPlugin implements Plugin {

    private PlayerListener playerListener = new PlayerEventPrinter();

    private jmms jmms;

    public LoggingPlugin() {
    }

    public void init(jmms jmms) throws JMMSException {
        this.jmms = jmms;
        jmms.player().addPlayerListener(playerListener);
    }

    public void deinit() {
        jmms.player().removePlayerListener(playerListener);
    }

    private class PlayerEventPrinter implements PlayerListener {

        public void playerEvent(PlayerEvent e) {
            System.out.println(e.toString());
        }
    }
}
