package it.unipi.miabot.utils;

import java.io.*;

/**
 * Utility class for holding hardcoded constants.
 */
public final class Hardcoded_Configuration {

    /**
   * The minimum non-kernel port number.
   */
    public static final int MIN_PORT_VALUE = 1024;

    /**
   * The maximum possible port number.
   */
    public static final int MAX_PORT_VALUE = 65535;

    /**
   * The default protocol timeout, in ms.
   */
    public static final long PROTOCOL_TIMEOUT = 3000;

    /**
   * The byte to send for terminal mode exit (ASCII escape character).
   */
    public static final byte[] TERMINAL_ESCAPE_COMMAND = { 0x1B };

    /**
   * The time to wait between wheels motion queries, in ms.
   */
    public static final long WAIT_END_OF_MOVE_INTERVAL = 300;

    /**
   * The estimated time needed for the longest gripper movement, in ms.
   */
    public static final long WAIT_END_OF_GRIP_INTERVAL = 300;

    /**
   * Holder for 90 degrees value.
   */
    public static final double NINETY_DEGREES = 90.0;

    /**
   * Holder for 10 cm value.
   */
    public static final double TEN_CENTIMETERS = 10.0;

    /**
   * Directory for configuration files.
   */
    public static final File CONFIG_BASE_DIR = new File("config");

    /**
   * Max bytes of payload parsed in a single track recording.
   */
    public static final int BLOB_TRACK_MAX_LENGTH = 2048;

    /**
   * Time between the ACK reception and the DT command in a single track
   * recording, in ms.
   */
    public static final long BLOB_TRACK_CAPTURE_TIME = 100;

    /**
   * Timeout for a terminal mode data reading.
   */
    public static final long TERMINAL_READ_TIMEOUT = 3000;

    /**
   * Primary time interval between terminal mode data read attempts.
   */
    public static final long TERMINAL_READ_RETRY = 100;

    /**
   * Secondary time interval between terminal mode data read attempts.
   */
    public static final long TERMINAL_READ_SHORT_RETRY = 10;

    /**
   * Payload length of a frame dump, in bytes.
   */
    public static final int DUMP_BYTES_LENGTH = 12888;

    /**
   * Payload length of a ACK or a NCK, in bytes.
   */
    public static final int ACK_OR_NCK_LENGTH = 4;

    /**
   * Number of retrials for wheels motion queries.
   * <p>
   * It is needed, in case of wheels adjustments, to ask more than once for
   * wheel motion states.
   */
    public static final int WAIT_STOP_RETRIES = 3;

    /**
   * Camera image width, in pixels.
   */
    public static final int IMAGE_WIDTH = 176;

    /**
   * Camera Image height, in pixels.
   */
    public static final int IMAGE_HEIGHT = 144;

    /**
   * Number of retrials for methods that retry on failure.
   */
    public static final int NUMBER_OF_RETRIALS = 3;

    /**
   * no instances.
   */
    private Hardcoded_Configuration() {
    }
}
