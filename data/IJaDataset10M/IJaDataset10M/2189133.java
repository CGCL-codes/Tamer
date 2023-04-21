package oneiro.server;

import java.util.*;

/**
 * Collection of server variables grouped together for easy configuration.
 * Although all are not declared final, they are not to be fiddled with
 * without proper precautions. Configuration changes should by made in the
 * file etc/OneiroConfig.properties. This file mearly holds the defaults.
 *
 * @author Markus Mårtnsson
 */
public interface Config {

    ResourceBundle configBundle = ResourceBundle.getBundle("OneiroConfig");

    /**
     * The URL of the logo to show when logging on via the JavaClient(tm).
     */
    public String LOGO_URL = ResourceUtils.getString(configBundle, "logo_url", "http://oneiro.sourceforge.net/images/oneiro_logo.png");

    /**
     * The logo presented by clients unable to show graphics.
     */
    public String[] TEXT_LOGIN_MSG = { "     xxxxxx    xxxx    xxxx xxxxxxxxxx   xxxxx xxxxxxxxx       xxxxxx      ", "    xx    xx     xxx     x    xx     xx   xx    xx     xxx    xx    xx     ", "   xx      xx    x xx    x    xx      x   xx    xx      xx   xx      xx    ", "  xx        xx   x xx    x    xx   x      xx    xx      xx  xx        xx   ", "  xx        xx   x  xx   x    xx  xx      xx    xx     xx   xx        xx   ", "xxxx xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxx", "  xx        xx   x   xx  x    xx  xx      xx    xx   xx     xx        xx   ", "  xx        xx   x    xx x    xx   x   x  xx    xx    xx    xx        xx   ", "   xx      xx    x     xxx    xx      xx  xx    xx     xx    xx      xx    ", "    xx    xx     x      xx    xx     xx   xx    xx      xx    xx    xx     ", "     xxxxxx    xxxxx    xx  xxxxxxxxxxx  xxxxx xxxxx    xxxx   xxxxxx      ", "", "                     Welcome to this test server!", "" };

    /** 
     * Version number. 
     */
    public final String VERSION = ResourceUtils.getString(configBundle, "version", "Unknown version");

    /**
     * The port number used by the mother socket.
     */
    public final int PORT_NUMBER = ResourceUtils.getInt(configBundle, "port_number", 5555);

    /**
     * The world script from which the world will be reconstructed
     * after a normal reset.
     */
    public final String WORLD_SCRIPT = ResourceUtils.getString(configBundle, "world_script", "world.script");

    /**
     * The factory which should be used to create the world if there is
     * none to load.
     */
    public final String WORLD_FACTORY = ResourceUtils.getString(configBundle, "world_factory", "oneiro:city");

    /** 
     * The logfile.
     */
    public final String LOG_FILE = ResourceUtils.getString(configBundle, "game_log", "game.log");

    /**
     * The format of the date/time part of the log messages.
     */
    public final String LOG_TIME_FORMAT = ResourceUtils.getString(configBundle, "log_time_format", "yyyy.MM.dd hh:mm:ss");

    /**
     * The number of threads handling job requests of medium and
     * low priority.
     */
    public final int NUM_JOB_THREADS = ResourceUtils.getInt(configBundle, "num_job_threads", 10);

    /**
     * The acceleration of gravity (g) in m/s².
     */
    public final double CONST_G = ResourceUtils.getDouble(configBundle, "const_g", 9.8);
}
