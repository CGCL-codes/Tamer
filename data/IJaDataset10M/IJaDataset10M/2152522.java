package jpcsp.connector;

import java.io.File;
import jpcsp.settings.Settings;

/**
 * @author gid15
 *
 */
public class Connector {

    public static final String baseDirectory = Settings.getInstance().readString("emu.tmppath") + File.separatorChar;

    public static final String basePSPDirectory = "ms0:/tmp/";

    public static final String jpcspConnectorName = "Jpcsp Connector 3xx";

    public static final String commandFileName = "command.txt";
}
