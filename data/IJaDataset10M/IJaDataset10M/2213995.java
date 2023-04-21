package oculus.commport;

import oculus.Application;
import oculus.Observer;
import oculus.State;
import oculus.Util;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class ArduinoCommSonar extends AbstractArduinoComm implements SerialPortEventListener, ArduioPort, Observer {

    public static final long SONAR_DELAY = 1500;

    public ArduinoCommSonar(Application app) {
        super(app);
        new WatchDog().start();
        state.addObserver(this);
    }

    /** inner class to check if getting responses in timely manor */
    private class WatchDog extends Thread {

        public WatchDog() {
            this.setDaemon(true);
        }

        public void run() {
            Util.delay(SETUP);
            while (true) {
                if (getReadDelta() > SONAR_DELAY * 5) {
                    System.out.println("sonar arduino watchdog time out, reboot!");
                    Util.beep();
                }
                if (getReadDelta() > SONAR_DELAY) {
                    new Sender(SONAR);
                    Util.delay(SONAR_DELAY);
                }
            }
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            super.manageInput();
        }
    }

    @Override
    public void execute() {
        String response = "";
        for (int i = 0; i < buffSize; i++) response += (char) buffer[i];
        if (response.equals("reset")) {
            isconnected = true;
            version = null;
            new Sender(GET_VERSION);
            updateSteeringComp();
        } else if (response.startsWith("version:")) {
            if (version == null) {
                version = response.substring(response.indexOf("version:") + 8, response.length());
                application.message("arduinoSonar v: " + version, null, null);
            }
        } else if (response.charAt(0) != GET_VERSION[0]) {
            if (response.startsWith("sonar")) {
                final String[] param = response.split(" ");
                final int range = Integer.parseInt(param[2]);
                if (param[1].equals("back")) {
                    if (Math.abs(range - state.getInteger(State.sonarback)) > 1) state.set(State.sonarback, range);
                } else if (param[1].equals("right")) {
                    if (Math.abs(range - state.getInteger(State.sonarright)) > 1) state.set(State.sonarright, range);
                } else if (param[1].equals("left")) {
                    if (Math.abs(range - state.getInteger(State.sonarleft)) > 1) state.set(State.sonarleft, range);
                }
            } else application.message("oculusSonar : " + response, null, null);
        }
    }

    @Override
    public void connect() {
        try {
            serialPort = (SerialPort) CommPortIdentifier.getPortIdentifier(state.get(State.serialport)).open(ArduinoCommSonar.class.getName(), SETUP);
            serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            out = serialPort.getOutputStream();
            in = serialPort.getInputStream();
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public void updated(String key) {
        if (key.equals("laser")) {
            System.out.println("updated.. " + key);
            String value = state.get(key);
            if (value.equals("on")) {
                new Sender(new byte[] { 'q', '1' });
            } else if (value.equals("off")) {
                new Sender(new byte[] { 'q', '1' });
            }
        }
    }
}
