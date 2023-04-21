package timeKeeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import base.util.ModelTime;
import gnu.io.PortInUseException;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import base.resources.Resources;
import timeKeeper.client.ImpulsTochterUhrTimeListener;
import timeKeeper.client.SlaveClockUpdateMonitor;
import timeKeeper.client.TochterUhrTimeListener;
import timeKeeper.core.Clock;
import timeKeeper.gui.GraphicConsole;
import timeKeeper.gui.GraphicTerminal;
import timeKeeper.gui.GraphicView;
import timeKeeper.gui.dialogs.SlaveClockInfo;
import timeKeeper.net.MulticastTimeListener;
import timeKeeper.net.XmlRpcTimeService;

/**
 * TimeKeeper Class is the main application for the model time.
 * This class starts the clock kernel, the view and if configured the time services e.g. COM, XML-RPC
 */
public class TimeKeeper {

    public static final Properties PROPS = new Properties();

    /**
     * Der mainmethode kann der Name einer Inidatei übergeben werden. Erfolgt dies nicht wird
     * nach der Datei clock.ini im Startverzeichnis gesucht.
     *
     * @param args configuration file
     */
    public static void main(final String[] args) {
        PropertyConfigurator.configureAndWatch("log4j.properties", 60 * 1000);
        TimeKeeperSettings.getInstance().configure();
        String fileName = "clock.ini";
        if (args.length > 0) {
            fileName = args[0];
        }
        final File file = new File(fileName);
        try {
            final FileInputStream fis = new FileInputStream(file);
            try {
                PROPS.load(fis);
            } catch (IOException e) {
            } catch (IllegalArgumentException iae) {
                System.out.println("Konfigurationsdatei " + file.getAbsolutePath() + " enthält ungültige Unicode-Sequenzen");
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Konfigurationsdatei " + file.getAbsolutePath() + " nicht gefunden");
        }
        new TimeKeeper();
    }

    /** 
     * Constructor TimeKeeper
     * reads the properties of the file, configures the clock kernel with starttime
     * and scale, starts the gui and time services
     */
    public TimeKeeper() {
        final ModelTime start = TimeKeeperSettings.getInstance().getCurrentTime();
        final double scale = TimeKeeperSettings.getInstance().getClockScale();
        final Clock clock = new Clock(start, scale);
        final GraphicView terminal;
        if (!java.awt.GraphicsEnvironment.isHeadless()) {
            terminal = new GraphicTerminal(clock);
        } else {
            LogManager.getRootLogger().removeAppender("stdout");
            terminal = new GraphicConsole(clock);
        }
        try {
            if ("true".equalsIgnoreCase(PROPS.getProperty("tochter"))) {
                final TochterUhrTimeListener tl;
                SlaveClockUpdateMonitor sci;
                if (!java.awt.GraphicsEnvironment.isHeadless()) {
                    sci = new SlaveClockInfo((GraphicTerminal) terminal);
                } else {
                    sci = (GraphicConsole) terminal;
                }
                if ("false".equalsIgnoreCase(PROPS.getProperty("impuls"))) {
                    tl = new TochterUhrTimeListener(sci);
                } else {
                    tl = new ImpulsTochterUhrTimeListener(sci);
                }
                clock.addTimeListener(tl);
                terminal.setActiveSlaveClock(tl);
            }
        } catch (PortInUseException e) {
            terminal.showMessage(Resources.getText("timekeeper.COM-Error"));
        } catch (UnsatisfiedLinkError ule) {
            terminal.showMessage(Resources.getText("timekeeper.lib_error"));
        }
        try {
            if ("true".equalsIgnoreCase(PROPS.getProperty("timeService"))) {
                XmlRpcTimeService.init(clock);
            }
            if ("true".equalsIgnoreCase(PROPS.getProperty("multicast"))) {
                clock.addTimeListener(new MulticastTimeListener());
            }
        } catch (Exception e) {
            terminal.showMessage(e.getMessage());
        }
        clock.startClock();
    }
}
