package org.chargecar;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import edu.cmu.ri.createlab.serial.SerialPortException;
import edu.cmu.ri.createlab.serial.device.connectivity.SerialDeviceConnectionEventListener;
import edu.cmu.ri.createlab.serial.device.connectivity.SerialDeviceConnectionState;
import edu.cmu.ri.createlab.serial.device.connectivity.SerialDeviceConnectivityManager;
import edu.cmu.ri.createlab.serial.device.connectivity.SerialDeviceConnectivityManagerImpl;
import edu.cmu.ri.createlab.userinterface.component.Spinner;
import edu.cmu.ri.createlab.userinterface.util.DialogHelper;
import edu.cmu.ri.createlab.userinterface.util.SwingWorker;
import edu.cmu.ri.createlab.util.runtime.LifecycleManager;
import org.apache.log4j.Logger;
import org.chargecar.gps.GPSEventListener;
import org.chargecar.gps.nmea.NMEAReader;
import org.chargecar.sensorboard.EfficiencyController;
import org.chargecar.sensorboard.EfficiencyModel;
import org.chargecar.sensorboard.EfficiencyView;
import org.chargecar.sensorboard.PedalPositionsModel;
import org.chargecar.sensorboard.PowerController;
import org.chargecar.sensorboard.PowerModel;
import org.chargecar.sensorboard.PowerView;
import org.chargecar.sensorboard.SpeedAndOdometryController;
import org.chargecar.sensorboard.SpeedAndOdometryModel;
import org.chargecar.sensorboard.SpeedAndOdometryView;
import org.chargecar.sensorboard.TemperaturesModel;
import org.chargecar.sensorboard.TemperaturesView;
import org.chargecar.sensorboard.serial.proxy.SensorBoardSerialDeviceProxyCreator;
import org.jdesktop.layout.GroupLayout;

/**
 * @author Chris Bartley (bartley@cmu.edu)
 */
public class InDashDisplay {

    private static final Logger LOG = Logger.getLogger(InDashDisplay.class);

    private static final PropertyResourceBundle RESOURCES = (PropertyResourceBundle) PropertyResourceBundle.getBundle(InDashDisplay.class.getName());

    private static final String APPLICATION_NAME = RESOURCES.getString("application.name");

    public static void main(final String[] args) {
        NMEAReader nmeaReader;
        if (args.length < 1) {
            LOG.warn("GPS receiver serial port not specified, so no GPS data will be available!");
            nmeaReader = null;
        } else {
            nmeaReader = new NMEAReader(APPLICATION_NAME);
            final String gpsSerialPortName = args[0];
            try {
                nmeaReader.connect(gpsSerialPortName);
            } catch (SerialPortException e) {
                LOG.error("SerialPortException while connecting to the GPS receiver.  Setting the NMEAReader to null, so no reading will be attempted.", e);
                nmeaReader = null;
            } catch (IOException e) {
                LOG.error("IOException while connecting to the GPS receiver.  Setting the NMEAReader to null, so no reading will be attempted.", e);
                nmeaReader = null;
            } catch (Exception e) {
                LOG.error("Exception while connecting to the GPS receiver.  Setting the NMEAReader to null, so no reading will be attempted.", e);
                nmeaReader = null;
            }
        }
        final NMEAReader gpsReader = nmeaReader;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                final GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
                try {
                    new InDashDisplay(graphicsDevice, gpsReader);
                } finally {
                    if (graphicsDevice.isFullScreenSupported()) {
                        graphicsDevice.setFullScreenWindow(null);
                    }
                }
            }
        });
    }

    private final SetStageRunnable setStageForIsScanningRunnable;

    private final SetStageRunnable setStageForIsConnectedRunnable;

    private final SetStageRunnable setStageForIsDisconnectedRunnable;

    private InDashDisplay(final GraphicsDevice graphicsDevice, final NMEAReader gpsReader) {
        if (gpsReader != null) {
            gpsReader.addEventListener(new GPSEventListener() {

                public void handleLocationEvent(final String latitude, final String longitude, final int numSatellitesBeingTracked) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("GPS{" + latitude + "\t" + longitude + "\t" + numSatellitesBeingTracked + "}");
                    }
                }

                public void handleElevationEvent(final int elevationInFeet) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("GPS Elevation{" + elevationInFeet + "}");
                    }
                }
            });
        }
        final JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        final JFrame jFrame = new JFrame(APPLICATION_NAME);
        jFrame.setUndecorated(true);
        jFrame.setContentPane(panel);
        graphicsDevice.setFullScreenWindow(jFrame);
        final SerialDeviceConnectivityManager serialDeviceConnectivityManager = new SerialDeviceConnectivityManagerImpl(new SensorBoardSerialDeviceProxyCreator());
        serialDeviceConnectivityManager.addConnectionEventListener(new SerialDeviceConnectionEventListener() {

            public void handleConnectionStateChange(final SerialDeviceConnectionState oldState, final SerialDeviceConnectionState newState, final String serialPortName) {
                if (oldState.equals(newState)) {
                    return;
                }
                if (SerialDeviceConnectionState.SCANNING.equals(newState)) {
                    SwingUtilities.invokeLater(setStageForIsScanningRunnable);
                } else if (SerialDeviceConnectionState.CONNECTED.equals(newState)) {
                    SwingUtilities.invokeLater(setStageForIsConnectedRunnable);
                } else {
                    SwingUtilities.invokeLater(setStageForIsDisconnectedRunnable);
                }
            }
        });
        final LifecycleManager lifecycleManager = new MyLifecycleManager(jFrame, graphicsDevice, serialDeviceConnectivityManager, gpsReader);
        final SpeedAndOdometryModel speedAndOdometryModel = new SpeedAndOdometryModel();
        final TemperaturesModel temperaturesModel = new TemperaturesModel();
        final PowerModel powerModel = new PowerModel();
        final EfficiencyModel efficiencyModel = new EfficiencyModel();
        final PedalPositionsModel pedalPositionsModel = new PedalPositionsModel();
        final PowerController powerController = new PowerController(powerModel);
        final SpeedAndOdometryController speedAndOdometryController = new SpeedAndOdometryController(speedAndOdometryModel);
        final EfficiencyController efficiencyController = new EfficiencyController(efficiencyModel);
        final InDashDisplayController inDashDisplayController = new InDashDisplayController(lifecycleManager, serialDeviceConnectivityManager, speedAndOdometryModel, temperaturesModel, powerModel, efficiencyModel, pedalPositionsModel);
        final SpeedAndOdometryView speedAndOdometryView = new SpeedAndOdometryView(speedAndOdometryController);
        final TemperaturesView temperaturesView = new TemperaturesView();
        final PowerView powerView = new PowerView(powerController);
        final EfficiencyView efficiencyView = new EfficiencyView();
        final InDashDisplayView inDashDisplayView = new InDashDisplayView(inDashDisplayController, speedAndOdometryView, temperaturesView, powerView, powerController, efficiencyView, efficiencyController);
        speedAndOdometryModel.addEventListener(speedAndOdometryView);
        temperaturesModel.addEventListener(temperaturesView);
        powerModel.addEventListener(powerView);
        efficiencyModel.addEventListener(efficiencyView);
        final Spinner spinner = new Spinner(RESOURCES.getString("label.connecting-to-sensor-board"));
        setStageForIsScanningRunnable = new SetStageRunnable(panel, spinner);
        setStageForIsConnectedRunnable = new SetStageRunnable(panel, inDashDisplayView);
        setStageForIsDisconnectedRunnable = new SetStageRunnable(panel, new JLabel(RESOURCES.getString("label.disconnected")));
        setStageForIsScanningRunnable.run();
        jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jFrame.setBackground(Color.WHITE);
        jFrame.setResizable(true);
        jFrame.addWindowListener(new WindowAdapter() {

            public void windowOpened(final WindowEvent e) {
                LOG.debug("InDashDisplay.windowOpened()");
                lifecycleManager.startup();
            }

            public void windowClosing(final WindowEvent event) {
                lifecycleManager.shutdown();
            }
        });
        jFrame.setVisible(true);
    }

    private class SetStageRunnable implements Runnable {

        private final JPanel parentContainer;

        private final Component component;

        private SetStageRunnable(final JPanel parentPanel, final Component component) {
            this.parentContainer = parentPanel;
            this.component = component;
        }

        public void run() {
            parentContainer.removeAll();
            final GroupLayout layout = new GroupLayout(parentContainer);
            final Component leftGlue = Box.createGlue();
            final Component rightGlue = Box.createGlue();
            parentContainer.setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup().add(leftGlue).add(component).add(rightGlue));
            layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.CENTER).add(leftGlue).add(component).add(rightGlue));
        }
    }

    private static class MyLifecycleManager implements LifecycleManager {

        private final Runnable startupRunnable;

        private final Runnable shutdownRunnable;

        private final JFrame jFrame;

        private MyLifecycleManager(final JFrame jFrame, final GraphicsDevice graphicsDevice, final SerialDeviceConnectivityManager serialDeviceConnectivityManager, final NMEAReader gpsReader) {
            this.jFrame = jFrame;
            startupRunnable = new Runnable() {

                public void run() {
                    serialDeviceConnectivityManager.scanAndConnect();
                    if (gpsReader == null) {
                        LOG.info("InDashDisplay$MyLifecycleManager.run(): NMEA Reader given to the LifecycleManager constructor was null, so GPS data won't be read.");
                    } else {
                        LOG.info("InDashDisplay$MyLifecycleManager.run(): Starting the NMEAReader...");
                        gpsReader.startReading();
                    }
                }
            };
            shutdownRunnable = new Runnable() {

                public void run() {
                    serialDeviceConnectivityManager.disconnect();
                    if (gpsReader == null) {
                        LOG.info("InDashDisplay$MyLifecycleManager.run(): NMEA Reader given to the LifecycleManager constructor was null, so we won't try to shut it down.");
                    } else {
                        LOG.info("InDashDisplay$MyLifecycleManager.run(): Stopping the NMEAReader...");
                        gpsReader.stopReading();
                        LOG.info("InDashDisplay$MyLifecycleManager.run(): Disconnecting from the GPS...");
                        gpsReader.disconnect();
                    }
                    graphicsDevice.setFullScreenWindow(null);
                    System.exit(0);
                }
            };
        }

        public void startup() {
            LOG.debug("LifecycleManager.startup()");
            run(startupRunnable);
        }

        public void shutdown() {
            LOG.debug("LifecycleManager.shutdown()");
            if (DialogHelper.showYesNoDialog(RESOURCES.getString("dialog.title.exit-confirmation"), RESOURCES.getString("dialog.message.exit-confirmation"), jFrame)) {
                run(shutdownRunnable);
            }
        }

        private void run(final Runnable runnable) {
            if (SwingUtilities.isEventDispatchThread()) {
                final SwingWorker worker = new SwingWorker() {

                    public Object construct() {
                        runnable.run();
                        return null;
                    }
                };
                worker.start();
            } else {
                runnable.run();
            }
        }
    }
}
