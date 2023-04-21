package ru.adv.logger;

public class InfoLogWriter extends LogWriter {

    public InfoLogWriter(TLogger logger, int bufferSize) {
        super(logger, bufferSize);
    }

    @Override
    protected void logMessage(String message) {
        getLogger().info(message);
    }
}
