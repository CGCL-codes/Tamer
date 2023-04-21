package newsatort.log;

import java.util.ArrayList;
import java.util.List;
import newsatort.exception.GeneralException;

public abstract class AbstractLogger implements ILogger {

    private final List<LogLevel> acceptedLogLevelList;

    private String name = null;

    public AbstractLogger() {
        acceptedLogLevelList = new ArrayList<LogLevel>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addAcceptedLogLevelList(LogLevel logLevel) {
        acceptedLogLevelList.add(logLevel);
    }

    @Override
    public List<LogLevel> getAcceptedLogLevelList() {
        return acceptedLogLevelList;
    }

    @Override
    public void start() throws GeneralException {
    }

    @Override
    public void stop() throws GeneralException {
    }

    protected String getLevelLabel(LogLevel level) {
        String label = null;
        switch(level) {
            case ERROR:
                label = "ERROR";
                break;
            case WARNING:
                label = "WARNING";
                break;
            case INFO:
                label = "INFO";
                break;
            case EVENT:
                label = "EVENT";
                break;
            case VERBOSE:
                label = "VERBOSE";
                break;
        }
        return label;
    }
}
