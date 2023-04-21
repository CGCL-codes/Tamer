package chat.common;

public class Logger {

    public static final int LEVEL_INFO = 1;

    public static final int LEVEL_WARN = 2;

    public static final int LEVEL_ERR = 3;

    public static final int LEVEL_DEBUG = 4;

    private static Logger instance;

    private int level;

    private String levelPrefix;

    private String application;

    private Logger(int level) {
    }

    public static void setLevel(int level) {
        Logger log = getInstance();
        String levelPrefix = null;
        log.level = level;
        switch(level) {
            case LEVEL_INFO:
                {
                    levelPrefix = "INFO";
                    break;
                }
            case LEVEL_WARN:
                {
                    levelPrefix = "WARN";
                    break;
                }
            case LEVEL_ERR:
                {
                    levelPrefix = "ERR";
                    break;
                }
            case LEVEL_DEBUG:
                {
                    levelPrefix = "DEBUG";
                    break;
                }
        }
        log.levelPrefix = "[" + levelPrefix + " - " + log.application + "] ";
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger(LEVEL_INFO);
            setApplication("");
            setLevel(LEVEL_INFO);
        }
        return instance;
    }

    public static void setApplication(String s) {
        Logger log = getInstance();
        log.application = (s != null ? s : "");
        setLevel(log.level);
    }

    private static void addLog(String s, int level) {
        Logger log = getInstance();
        if (level <= log.level) {
            System.err.println(log.levelPrefix + s);
        }
    }

    public static void info(String s) {
        addLog(s, LEVEL_INFO);
    }

    public static void warn(String s) {
        addLog(s, LEVEL_WARN);
    }

    public static void err(String s) {
        addLog(s, LEVEL_ERR);
    }

    public static void debug(String s) {
        addLog(s, LEVEL_DEBUG);
    }
}
