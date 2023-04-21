package com.wpjr.simulator.system;

public class NodeSystemConstants {

    public static final String PROPERTY_DETECTION_PROBABILITY = "DETECTION_PROBABILITY";

    public static final int SCHEDULER_STEP_LEVEL = 5;

    public static final int DEFAULT_HELLO_STEP_LEVEL = 1;

    public static final int INF = 1000;

    public static final int MAX_NEGHBORS = 5;

    public static final int MAX_NODES = 300;

    public static final int MAX_RANGE = 100;

    public static final int MIN_RANGE = 10;

    public static final int MAX_GRID_SIZE = 800;

    public static final int CONST_SIGNAL_STRENGTH = 10000;

    public static final double MIN_PERCENTAGEM_PACKET_LOSS = 0.00;

    public static final int INDEX_MESSAGES_SENT = 0;

    public static final int INDEX_MESSAGES_RECEIVED = 1;

    public static final int DEBUG_NO_TEXT = 0;

    public static final int DEBUG_MINIMUM = 1;

    public static final int DEBUG_AVERAGE = 2;

    public static final int DEBUG_HIGH = 3;

    public static final int DEBUG_MAXIMUM = 4;

    public static final int MAX_MESSAGES = 100;

    public static final int MAX_MESSAGE_ID = 2000000;

    public static final int MAX_NEIGHBORS = 200;

    public static final int PARAMETER_FILENAME = 0;

    public static final int PARAMETER_DEBUG_LEVEL = 1;

    public static final int PARAMETER_NUMBER_OF_NODES = 0;

    public static final int PARAMETER_GRID_SIZE = 0;

    public static final int PARAMETER_NODE_RANGE = 0;

    public static final String ARGUMENT_FILE = "-cf";

    public static final String ARGUMENT_NUMBER_OF_NODES = "-non";

    public static final String ARGUMENT_GRID_SIZE = "-gs";

    public static final String ARGUMENT_GRID_X = "-gx";

    public static final String ARGUMENT_GRID_Y = "-gy";

    public static final String ARGUMENT_NODE_RANGE = "-nr";

    public static final String ARGUMENT_DEBUG_LEVEL = "-dl";

    public static final String PROPERTY_FILE = "NodeSystem.properties";

    public static final String PROPERTY_MIN_NODES = "MIN_NODES";

    public static final String PROPERTY_MAX_NODES = "MAX_NODES";

    public static final String PROPERTY_MIN_GRID_SIZE = "MIN_GRID_SIZE";

    public static final String PROPERTY_MAX_GRID_SIZE = "MAX_GRID_SIZE";

    public static final String PROPERTY_MIN_RANGE = "MIN_RANGE";

    public static final String PROPERTY_MAX_RANGE = "MAX_RANGE";

    public static final String PROPERTY_RADIO_PROPAGATION_MODEL = "RADIO_PROPAGATION_MODEL";

    /** the type of malicious node in the network*/
    public static final String MALICIOUS_NODE_TYPE = "MALICIOUS_NODE_TYPE";

    /** the weak malicious node, can only send messages to its neighbors*/
    public static final int MALICIOUS_NODE_TYPE_WEAK = 1;

    /** the strong malicious node, can send messages to everyone directly*/
    public static final int MALICIOUS_NODE_TYPE_STRONG = 2;

    public static final String MALICIOUS_NODE_RECEIVE_RANGE = "MALICIOUS_NODE_RECEIVE_RANGE";

    public static final String MALICIOUS_NODE_TRANSMIT_RANGE = "MALICIOUS_NODE_TRANSMIT_RANGE";

    public static final String MALICIOUS_NODE_X_COORD = "MALICIOUS_NODE_X_COORD";

    public static final String MALICIOUS_NODE_Y_COORD = "MALICIOUS_NODE_Y_COORD";

    /**standard propagation model, based only on the distance between nodes
     * and their transmission and reception range*/
    public static final int RADIO_PROPAGATION_MODEL_STANDARD = 0;

    /**takes into account the signal strength calculated by a formula inversely
     * proportional to the distance squared*/
    public static final int RADIO_PROPAGATION_MODEL_SIGNAL_STRENGTH = 1;

    public static int DEFAULT_SUSPICIOUS_INFO_STEP_LEVEL = 1;

    public static final double DEFAULT_MAXIMUM_DISTANCE_DIFFERENCE = 0.30;

    public static final double DEFAULT_MESSAGE_CHECK_PROBABILITY = 1.0;

    public static final double DEFAULT_MALICIOUS_MULTIPLIER = 2;
}
