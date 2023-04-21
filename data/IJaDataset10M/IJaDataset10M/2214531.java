package org.j4me.bluetoothgps;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import org.j4me.logging.Log;
import org.j4me.util.ConnectorHelper;

/**
 * Main class for communication with GPS receiver. Use this class to access
 * GPS receiver from other classes.
 */
class BluetoothGPS implements Runnable {

    /**
     * The timeout value for Bluetooth connections in milliseconds. Since this
     * tries to connect on 10 Bluetooth channels, the total timeout for
     * connecting is 10x this number.
     * <p>
     * The emulator's default timeout is 10,000 ms.
     */
    private static final short BLUETOOTH_TIMEOUT = 3000;

    /**
     * Time in ms to wait until resume to receive.
     */
    private static final short BREAK = 500;

    /**
     * Wait after calling disconnect
     */
    private static final short DISCONNECT_WAIT = 1000;

    /**
     * Time in ms to sleep before each read. This seems to solve the problem
     * or read hangs.
     */
    public static final short SLEEP_BEFORE_READ = 100;

    /**
     * How long to wait before we just kill the read. We add the sleep value
     * since this sleep is performed before every read and we start the timer
     * before the pre-read sleep.
     * <p>
     * After experimenting with combinations of Bluetooth GPS devices and
     * phones 3 seconds seems to work. Motorola phones seem to be the only
     * ones with Bluetooth implementations that need this.
     */
    public static final short READ_TIMEOUT = BluetoothGPS.SLEEP_BEFORE_READ + 3000;

    /**
     * How long to wait to initialize the bluetooth connection
     */
    public static final short BLUETOOTH_CONNECTION_INIT_SLEEP = 200;

    /**
     * Conversion constant to convert between knots and meters per second
     * (m/s).
     */
    private static final float MS_PER_KNOT = 0.514444444444444f;

    /**
     * The number of days since January 1 for the start of a month. This does
     * not include leap year days.
     * 
     * @see #convertUTCTime(String, String)
     */
    private static final int MONTH_OFFSET[] = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };

    /**
     * Connection to bluetooth device.
     */
    private StreamConnection connection;

    /**
     * The input stream from the GPS device. Location data is received through
     * it.
     */
    private InputStream inputStream;

    /**
     * The output stream to the GPS device. Configuration commands are sent
     * through it.
     */
    private OutputStream outputStream;

    /**
     * Receiving happens in separate thread.
     */
    private Thread runner;

    /**
     * Flag to indicate that the runner thread should stop
     */
    private boolean stop = false;

    /**
     * URL used to connect to bluetooth device.
     */
    private String url;

    /**
     * Listener to notify of location events. If this is <code>null</code>
     * then don't notify anything of location events.
     */
    private LocationListener locationListener;

    /**
     * The source of location information.
     */
    private final BluetoothLocationProvider locationProvider;

    /**
     * The interval, in milliseconds, between calls made to the registered
     * <code>LocationListener.locationUpdated</code>.
     * 
     * @see #setLocationListener(LocationListener, BluetoothLocationProvider)
     */
    private long locationUpdateInterval;

    /**
     * The system time for when <code>LocationListener.locationUpdated</code>
     * was last raised.
     * 
     * @see #locationUpdateInterval
     */
    private long lastLocationUpdateTime;

    /**
     * The last <code>Location</code> obtained from the GPS. This is the
     * provider's best guess about the current location. It is usually less
     * than a second old.
     */
    private Location location;

    /**
     * Creates new receiver. Does not start automatically, use start()
     * instead.
     * 
     * @param url -
     *                URL of bluetooth device to connect to.
     */
    public BluetoothGPS(final BluetoothLocationProvider provider, final String url) {
        locationProvider = provider;
        this.url = url;
    }

    /**
     * @return The last <code>Location</code> obtained from the GPS or
     *         <code>null</code> if no location has yet been obtained.
     */
    public Location getLastKnownLocation() {
        return location;
    }

    /**
     * Establishes a bluetooth serial connection (specified in GPS_BT_URL) and
     * opens an input stream.
     * 
     * @see #isConnected()
     * @see #disconnect()
     * 
     * @throws ConnectionNotFoundException -
     *                 If the target of the name cannot be found, or if the
     *                 requested protocol type is not supported.
     * @throws IOException -
     *                 If error occurs while establishing bluetooth connection
     *                 or opening input stream.
     * @throws SecurityException -
     *                 May be thrown if access to the protocol handler is
     *                 prohibited.
     */
    private synchronized void connect() throws ConnectionNotFoundException, IOException, SecurityException {
        if (!isConnected()) {
            Log.info("Connecting to Bluetooth device at " + url);
            connection = (StreamConnection) ConnectorHelper.open(url, Connector.READ_WRITE, BluetoothGPS.BLUETOOTH_TIMEOUT);
            Log.debug("Bluetooth connection established");
            configureBluetoothGPSSettings(connection);
            inputStream = connection.openInputStream();
            outputStream = connection.openOutputStream();
        }
    }

    /**
     * Configure the GPS device.
     * <p>
     * This sends NMEA input sentences to the Bluetooth GPS device. The type
     * and frequency of output sentences sent back from the device should be
     * altered such that we do not receive messages we do not care about (to
     * avoid the overhead of processing them) and we receive the messages we
     * do care about once a second (maximum rate so that if any are corrupt we
     * get our data as fast as possible).
     * <p>
     * On a few devices it is possible to adjust the baud rate. However, this
     * is not a good option. It isn't the speed data is transmitted (actually
     * faster is better), it is the amount of quality data that comes through.
     * <p>
     * A good list of the proprietary input sentences is here:
     * http://www.gpsinformation.org/dale/nmea.htm
     * 
     * @param connection
     *                is the connection object to the Bluetooth GPS unit. The
     *                connection must be {@link Connector#READ} or
     *                {@link Connector#READ_WRITE}.
     */
    private void configureBluetoothGPSSettings(final StreamConnection connection) {
        if (outputStream != null) {
            try {
                outputStream.write(BluetoothGPS.createSentence("PSRF103,00,00,01,01"));
                outputStream.write(BluetoothGPS.createSentence("PSRF103,02,00,01,01"));
                outputStream.write(BluetoothGPS.createSentence("PSRF103,04,00,01,01"));
                outputStream.write(BluetoothGPS.createSentence("PSRF103,01,00,00,01"));
                outputStream.write(BluetoothGPS.createSentence("PSRF103,03,00,00,01"));
                outputStream.write(BluetoothGPS.createSentence("PSRF103,05,00,00,01"));
                outputStream.write(BluetoothGPS.createSentence("PSRF103,06,00,00,01"));
                outputStream.write(BluetoothGPS.createSentence("PSRF103,07,00,00,01"));
                outputStream.write(BluetoothGPS.createSentence("PSRF103,08,00,00,01"));
                outputStream.write(BluetoothGPS.createSentence("PSRF103,09,00,00,01"));
                outputStream.write(BluetoothGPS.createSentence("PSRF103,10,00,00,01"));
                outputStream.write(BluetoothGPS.createSentence("PGRMO,,2"));
                outputStream.write(BluetoothGPS.createSentence("PGRMO,GPGGA,1"));
                outputStream.write(BluetoothGPS.createSentence("PGRMO,GPGSA,1"));
                outputStream.write(BluetoothGPS.createSentence("PGRMO,GPRMC,1"));
                outputStream.flush();
                Log.debug("Configured GPS device settings");
            } catch (final IOException e) {
                Log.warn("Could not send configuration sentences to Bluetooth GPS", e);
            }
        }
    }

    /**
     * Sends a sentence through the output steam. The sentence is first
     * packaged with a leading '$' and trailing checksum. For example, to send
     * the sentence "$PSRF103,01,00,00,01*25\r\n" pass in
     * "PSRF103,01,00,00,01".
     * 
     * @param sentence
     *                is the NMEA sentence before being packaged.
     * @return <code>sentence</code> converted so it may be sent through an
     *         ouput stream.
     */
    public static byte[] createSentence(final String sentence) {
        final byte[] input = sentence.getBytes();
        int checksum = 0;
        for (int i = 0; i < input.length; i++) {
            checksum ^= input[i];
        }
        String hexChecksum = Integer.toHexString(checksum);
        hexChecksum = hexChecksum.toUpperCase();
        final StringBuffer buffer = new StringBuffer();
        buffer.append('$');
        buffer.append(sentence);
        buffer.append('*');
        buffer.append(hexChecksum);
        buffer.append("\r\n");
        final String packagedSentence = buffer.toString();
        final byte[] data = packagedSentence.getBytes();
        return data;
    }

    /**
     * Closes input stream and bluetooth connection as well as sets the
     * corresponding objects to null.
     * 
     * @see #disconnect()
     */
    private synchronized void disconnect() {
        Log.debug("Disconnecting from GPS device");
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (final IOException e) {
            Log.warn("Problem closing GPS connection", e);
        }
        inputStream = null;
        outputStream = null;
        connection = null;
    }

    /**
     * @return True, if connected and input stream opened
     */
    public synchronized boolean isConnected() {
        return (connection != null) && (inputStream != null);
    }

    /**
     * Forces the GPS device to re-acquire its location fix. This is done
     * through a warm start which takes around 30+ seconds. It is useful when
     * the GPS location is not considered accurate.
     * <p>
     * Currently only the SiRF chipset is supported. It uses static navigation
     * to help steady the GPS readings. However, the technology only works
     * well for driving and forces the locations to be several meters off over
     * time. If the location does not seem correct, for example it is
     * consistently 10 meters off the road, calling this method will cause the
     * receiver to re-initialize and acquire the correct location.
     */
    public synchronized void reacquireFix() {
        if (outputStream != null) {
            try {
                String sentence = "PSRF104,0,0,0,0,0,0,12,2";
                if ((location != null) && location.isValid()) {
                    final QualifiedCoordinates coordinates = location.getQualifiedCoordinates();
                    final long time = location.getTimestamp() - 936921587000L;
                    final long oneWeek = 604800000L;
                    final long weeks = time / oneWeek;
                    final long seconds = (time % oneWeek) / 1000;
                    final StringBuffer sb = new StringBuffer();
                    sb.append("PSRF104,");
                    sb.append(coordinates.getLatitude());
                    sb.append(",");
                    sb.append(coordinates.getLongitude());
                    sb.append(",");
                    sb.append(Float.isNaN(coordinates.getAltitude()) ? 0 : coordinates.getAltitude());
                    sb.append(",0,");
                    sb.append(seconds);
                    sb.append(",");
                    sb.append(weeks);
                    sb.append(",12,3");
                    sentence = sb.toString();
                }
                outputStream.write(BluetoothGPS.createSentence(sentence));
            } catch (final IOException e) {
                Log.warn("Could not re-acquire Bluetooth GPS fix", e);
            }
        }
    }

    /**
     * Reads in records sent by the GPS receiver. When a supported record has
     * been received pauses for specified amount of time. Continues on I/O
     * errors.
     */
    public synchronized void run() {
        boolean process = true;
        boolean processedGPRMC = false;
        byte[] outputBytes = null;
        short skipCnt = 0;
        int result = 0;
        final NMEAParser parser = new NMEAParser();
        try {
            Thread.sleep(BluetoothGPS.BLUETOOTH_CONNECTION_INIT_SLEEP);
        } catch (final InterruptedException e) {
        }
        final BluetoothReadTimeoutThread btrtt = new BluetoothReadTimeoutThread(runner, BluetoothGPS.READ_TIMEOUT);
        boolean firstItr = true;
        while (process) {
            try {
                if (stop) {
                    stop();
                    return;
                }
                if (!isConnected()) {
                    connect();
                    try {
                        Thread.sleep(BluetoothGPS.BLUETOOTH_CONNECTION_INIT_SLEEP);
                    } catch (final InterruptedException e) {
                    }
                }
                outputBytes = new byte[NMEAParser.OUTPUT_BUFFER_MAX_SIZE];
                if (firstItr) {
                    btrtt.start();
                    firstItr = false;
                } else {
                    btrtt.restart();
                }
                try {
                    Thread.sleep(BluetoothGPS.SLEEP_BEFORE_READ);
                } catch (final InterruptedException e) {
                }
                result = inputStream.read(outputBytes, 0, NMEAParser.OUTPUT_BUFFER_MAX_SIZE);
                if (result < 0) {
                    throw new IOException("Bluetooth device closed connection");
                }
                btrtt.setReadSuccess(true);
                if ((processedGPRMC) && (result >= NMEAParser.OUTPUT_BUFFER_MAX_SIZE) && (skipCnt < 4)) {
                    parser.flush();
                    skipCnt++;
                } else {
                    processedGPRMC = false;
                    skipCnt = 0;
                    try {
                        final int parseResult = parser.parse(outputBytes, result);
                        if ((parseResult & NMEAParser.TYPE_GPRMC) != 0) {
                            processedGPRMC = true;
                            processRecord(parser.getRecordBuffer());
                            synchronized (this) {
                                try {
                                    wait(BluetoothGPS.BREAK);
                                } catch (final InterruptedException e) {
                                }
                            }
                        } else if (parseResult == NMEAParser.TYPE_NOTHING_TO_PROCESS) {
                            synchronized (this) {
                                try {
                                    wait(BluetoothGPS.BREAK);
                                } catch (final InterruptedException e) {
                                }
                            }
                        }
                    } catch (final Throwable t) {
                        Log.warn("Problem parsing GPS data", t);
                    }
                }
            } catch (final Throwable t) {
                if (t instanceof InterruptedIOException) {
                    Log.info("Bluetooth GPS stalled.  Disconnecting and reconnecting.");
                } else if (t instanceof IOException) {
                    Log.info("Bluetooth device dropped connection.  Reconnecting.");
                } else if (t instanceof InterruptedException) {
                    process = false;
                } else {
                    Log.warn("Unexpected GPS read error", t);
                }
                setProviderState(LocationProvider.TEMPORARILY_UNAVAILABLE);
                disconnect();
                synchronized (this) {
                    try {
                        wait(BluetoothGPS.DISCONNECT_WAIT);
                    } catch (final InterruptedException e) {
                    }
                }
                parser.flush();
            }
        }
    }

    /**
     * Process the record. Convert the string values to floats and notify the
     * location listener.
     * 
     * @param record -
     *                the record to process. If <code>null</code> this
     *                method is a no-op.
     */
    private void processRecord(final GPSRecord record) {
        if ((record == null) || (record.quality == null)) {
            return;
        }
        if ((record.quality.equals("1")) || (record.quality.equals("2")) || (record.quality.equals("3"))) {
            try {
                final float altitude = (record.altitude == null ? Float.NaN : Float.parseFloat(record.altitude));
                final float horizontalAccuracy = Float.parseFloat(record.hdop);
                final float verticalAccuracy = Float.parseFloat(record.vdop);
                float speed = Float.parseFloat(record.speed);
                speed *= BluetoothGPS.MS_PER_KNOT;
                final float course = (record.course == null ? Float.NaN : Float.parseFloat(record.course));
                final float lattitudeDegrees = BluetoothGPS.convertToDegress(record.lattitude, record.lattitudeDirection);
                final float longitudeDegrees = BluetoothGPS.convertToDegress(record.longitude, record.longitudeDirection);
                final QualifiedCoordinates qualifiedCoordinates = new QualifiedCoordinates(lattitudeDegrees, longitudeDegrees, altitude, horizontalAccuracy, verticalAccuracy);
                final long timestamp = BluetoothGPS.convertUTCTime(record.date, record.secondsSinceMidnight);
                location = new LocationImpl(qualifiedCoordinates, speed, course, timestamp);
                setProviderState(LocationProvider.AVAILABLE);
                if (locationListener != null) {
                    final long now = System.currentTimeMillis();
                    if (now - lastLocationUpdateTime >= locationUpdateInterval) {
                        lastLocationUpdateTime = now;
                        try {
                            locationListener.locationUpdated(locationProvider, location);
                        } catch (final Throwable userT) {
                            Log.warn("Unhandled exception in LocationProvider.locationUpdated\n" + location, userT);
                        }
                    }
                }
            } catch (final NumberFormatException e) {
            } catch (final NullPointerException e) {
            }
        } else {
            setProviderState(LocationProvider.TEMPORARILY_UNAVAILABLE);
        }
    }

    /**
     * Sets the <code>locationProvider</code> state and notifies any
     * registered <code>locationListener</code>. This should be called from
     * the reader thread.
     * 
     * @param newState
     *                is the new state of the location provider.
     */
    public void setProviderState(final int newState) {
        if (locationProvider.getState() != newState) {
            locationProvider.setState(newState);
            if (locationListener != null) {
                try {
                    locationListener.providerStateChanged(locationProvider, newState);
                } catch (final Throwable t) {
                    Log.warn("Unhandled exception in LocationProvider.providerStateChanged to " + newState, t);
                }
            }
            synchronized (this) {
                try {
                    Thread.sleep(0);
                } catch (final InterruptedException e) {
                }
            }
        }
    }

    /**
     * Convert from the format Degrees Minutes (DDMM.mmmm) -- note there are
     * no seconds -- to decimal degress(DD.dddd).
     * 
     * @param value -
     *                the latitude or longitude value
     * @param direction -
     *                either "S" for south or "W" for west
     * @return the value converted to degrees. If the value direction is "S"
     *         or "W" then the return value will be negative.
     */
    public static float convertToDegress(final String value, final char direction) {
        if (value != null) {
            final int idx = value.indexOf('.');
            if (idx > 0) {
                final int mmstart = idx - 2;
                final float dd = Float.parseFloat(value.substring(0, mmstart));
                final float mmmmmm = Float.parseFloat(value.substring(mmstart));
                final float val = mmmmmm / 60;
                final float result = dd + val;
                if ((direction == 'S') || (direction == 's')) {
                    return result * -1;
                } else if ((direction == 'W') || (direction == 'w')) {
                    return result * -1;
                } else {
                    return result;
                }
            }
        }
        return 0;
    }

    /**
     * Converts a UTC date and time string into Java's time. Java uses the
     * POSIX standard with 1 January 1970 00:00:00 as the epoch. It has a
     * precision of milliseconds.
     * 
     * @param date
     *                is the current year, month, and day. NMEA gives it to us
     *                like "140207" which means February 14, 2007. If this is
     *                <code>null</code> the current system time is returned.
     * @param time
     *                is the number of seconds since midnight. NMEA gives it
     *                to us like "063559.998" where milliseconds come after
     *                the decimal and can be any number of digits. If this is
     *                <code>null</code> the current system time is returned.
     * @return The Java time in milliseconds.
     * 
     * @see java.lang.System#currentTimeMillis()
     */
    public static long convertUTCTime(final String date, final String time) {
        if ((date == null) || (time == null)) {
            return System.currentTimeMillis();
        }
        double d = Double.parseDouble(time);
        d *= 1000;
        final int today = (int) d;
        final int day = Integer.parseInt(date.substring(0, 2));
        final int month = Integer.parseInt(date.substring(2, 4));
        final int year = Integer.parseInt(date.substring(4, 6));
        long days = 10957;
        days += year * 365;
        days += ((year - 1) / 4) + 1;
        days += BluetoothGPS.MONTH_OFFSET[month - 1];
        if ((year % 4 == 0) && (month >= 3)) {
            days += 1;
        }
        days += day;
        long milliseconds = days * 86400000;
        milliseconds += today;
        return milliseconds;
    }

    /**
     * Starts receving of data (if not yet started).
     * 
     */
    public synchronized void start() throws IOException {
        connect();
        if (runner == null) {
            stop = false;
            runner = new Thread(this);
            runner.start();
        }
    }

    /**
     * Stops receiving of data and disconnects from bluetooth device.
     * 
     */
    public synchronized void stop() {
        if (runner != null) {
            if (Thread.currentThread() == runner) {
                runner = null;
                disconnect();
                setProviderState(LocationProvider.TEMPORARILY_UNAVAILABLE);
            } else {
                stop = true;
                runner.interrupt();
            }
        } else {
            disconnect();
        }
        synchronized (this) {
            try {
                Thread.sleep(40);
            } catch (final InterruptedException e) {
            }
        }
    }

    /**
     * Set the location listener for this class. The listener will be notified
     * when a record is parsed from the remote GPS device
     * 
     * @param locationListener -
     *                the location listener defined by the user
     * @param interval
     *                is the time in seconds between delivering new locations
     *                to <code>locationListener.locationUpdated</code>.
     * @param timeout
     *                is how long it can take to get coordinates.
     * @param maxAge
     *                is the oldest a location can be, in seconds, that is
     *                delivered to
     *                <code>locationListener.locationUpdated</code>.
     * 
     * @see LocationProvider#setLocationListener(LocationListener, int, int,
     *      int)
     */
    public void setLocationListener(final LocationListener locationListener, final int interval, final int timeout, final int maxAge) {
        this.locationListener = locationListener;
        if (interval < 1) {
            locationUpdateInterval = 1000;
        } else {
            locationUpdateInterval = interval * 1000;
        }
    }
}

/**
 * Waits while reading from the input stream so that if we get wedged reading
 * from the input stream, this will interrupt the read.
 */
final class BluetoothReadTimeoutThread extends Thread {

    /**
     * Timeout in milliseconds. This how long a read from the input stream
     * should take.
     */
    private short timeout;

    /**
     * The thread that is doing the read
     */
    private Thread runner;

    /**
     * Set to true when the read completed successfully
     */
    private boolean readSuccess = false;

    /**
     * Create the read time out thread.
     * 
     * @param runner
     *                the thread that does the reading
     * @param timeout
     *                the timeout for reads
     */
    BluetoothReadTimeoutThread(final Thread runner, final short timeout) {
        this.timeout = timeout;
        this.runner = runner;
    }

    /**
     * Used to restart the thread. Wakes it up.
     */
    public synchronized void restart() {
        interrupt();
    }

    /**
     * Wait the specified timeout to kill the read thread
     */
    public synchronized void run() {
        while (true) {
            readSuccess = false;
            try {
                wait(timeout);
            } catch (final InterruptedException e) {
                readSuccess = true;
            }
            if (!readSuccess) {
                runner.interrupt();
            }
            try {
                wait();
            } catch (final InterruptedException e) {
            }
        }
    }

    /**
     * Let the read timeout thread know that the read finished.
     * 
     * @param readSuccess
     *                <code>true</code> if the read completed successfully
     */
    public synchronized void setReadSuccess(final boolean readSuccess) {
        this.readSuccess = readSuccess;
        interrupt();
    }
}
