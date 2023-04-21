package pluginreceiver.model;

import pluginreceiver.logic.Observer;
import java.util.ArrayList;

/**
 * Container for storing HSID data
 * @author nicATC
 */
public class XPlaneSimDataRepository {

    public static final int SIM_FLIGHTMODEL_POSITION_GROUNDSPEED = 0;

    public static final int SIM_FLIGHTMODEL_POSITION_TRUE_AIRSPEED = 1;

    public static final int SIM_FLIGHTMODEL_POSITION_MAGPSI = 2;

    public static final int SIM_FLIGHTMODEL_POSITION_HPATH = 3;

    public static final int SIM_FLIGHTMODEL_POSITION_LATITUDE = 4;

    public static final int SIM_FLIGHTMODEL_POSITION_LONGITUDE = 5;

    public static final int SIM_FLIGHTMODEL_POSITION_PHI = 6;

    public static final int SIM_FLIGHTMODEL_POSITION_R = 7;

    public static final int SIM_FLIGHTMODEL_POSITION_MAGVAR = 8;

    public static final int SIM_FLIGHTMODEL_POSITION_ELEVATION = 9;

    public static final int SIM_COCKPIT_RADIOS_NAV1_FREQ_HZ = 100;

    public static final int SIM_COCKPIT_RADIOS_NAV2_FREQ_HZ = 101;

    public static final int SIM_COCKPIT_RADIOS_ADF1_FREQ_HZ = 102;

    public static final int SIM_COCKPIT_RADIOS_ADF2_FREQ_HZ = 103;

    public static final int SIM_COCKPIT_RADIOS_NAV1_DIR_DEGT = 104;

    public static final int SIM_COCKPIT_RADIOS_NAV2_DIR_DEGT = 105;

    public static final int SIM_COCKPIT_RADIOS_ADF1_DIR_DEGT = 106;

    public static final int SIM_COCKPIT_RADIOS_ADF2_DIR_DEGT = 107;

    public static final int SIM_COCKPIT_RADIOS_NAV1_DME_DIST_M = 108;

    public static final int SIM_COCKPIT_RADIOS_NAV2_DME_DIST_M = 109;

    public static final int SIM_COCKPIT_RADIOS_ADF1_DME_DIST_M = 110;

    public static final int SIM_COCKPIT_RADIOS_ADF2_DME_DIST_M = 111;

    public static final int SIM_COCKPIT_RADIOS_NAV1_OBS_DEGM = 112;

    public static final int SIM_COCKPIT_RADIOS_NAV2_OBS_DEGM = 113;

    public static final int SIM_COCKPIT_RADIOS_NAV1_COURSE_DEGM = 114;

    public static final int SIM_COCKPIT_RADIOS_NAV2_COURSE_DEGM = 115;

    public static final int SIM_COCKPIT_RADIOS_NAV1_CDI = 116;

    public static final int SIM_COCKPIT_RADIOS_NAV2_CDI = 117;

    public static final int SIM_COCKPIT_AUTOPILOT_STATE = 150;

    public static final int SIM_COCKPIT_AUTOPILOT_VERTICAL_VELOCITY = 151;

    public static final int SIM_COCKPIT_AUTOPILOT_ALTITUDE = 152;

    public static final int SIM_COCKPIT_AUTOPILOT_HEADING_MAG = 200;

    public static final int SIM_COCKPIT_SWITCHES_EFIS_MAP_RANGE_SELECTOR = 201;

    public static final int SIM_COCKPIT_SWITCHES_EFIS_DME_1_SELECTOR = 202;

    public static final int SIM_COCKPIT_SWITCHES_EFIS_DME_2_SELECTOR = 203;

    public static final int SIM_COCKPIT_SWITCHES_EFIS_SHOWS_WEATHER = 204;

    public static final int SIM_COCKPIT_SWITCHES_EFIS_SHOWS_TCAS = 205;

    public static final int SIM_COCKPIT_SWITCHES_EFIS_SHOWS_AIRPORTS = 206;

    public static final int SIM_COCKPIT_SWITCHES_EFIS_SHOWS_WAYPOINTS = 207;

    public static final int SIM_COCKPIT_SWITCHES_EFIS_SHOWS_VORS = 208;

    public static final int SIM_COCKPIT_SWITCHES_EFIS_SHOWS_NDBS = 209;

    public static final int SIM_WEATHER_WIND_SPEED_KT = 300;

    public static final int SIM_WEATHER_WIND_DIRECTION_DEGT = 301;

    public static final int SIM_TIME_ZULU_TIME_SEC = 302;

    public static final int SIM_TIME_LOCAL_TIME_SEC = 303;

    public static final int PLUGIN_VERSION_ID = 400;

    float[] sim_values = new float[600];

    long updates = 0;

    ArrayList<Observer> observers;

    public static boolean source_is_recording = false;

    private static XPlaneSimDataRepository single_instance;

    /**
         * Returns the (singleton) repository instance
         * @return the (singleton) repository instance
         */
    public static XPlaneSimDataRepository get_instance() {
        if (XPlaneSimDataRepository.single_instance == null) {
            XPlaneSimDataRepository.single_instance = new XPlaneSimDataRepository();
        }
        return XPlaneSimDataRepository.single_instance;
    }

    private XPlaneSimDataRepository() {
        observers = new ArrayList<Observer>();
    }

    /**
         * Adds an observer to the list of observers
         * @param observer the observer to add
         */
    public void add_observer(Observer observer) {
        this.observers.add(observer);
    }

    /**
         * Sets the value of the indicated data id
         * @param id the data number to which val should be assigned
         * @param value the value
         */
    public void store_sim_value(int id, float value) {
        sim_values[id] = value;
    }

    /**
         * Returns the indicated value
         * @param id the id of the value
         * @return the value
         */
    public float get_sim_value(int id) {
        return sim_values[id];
    }

    /**
         * Updates the registered observers
         */
    public void tick_updates() {
        this.updates += 1;
        for (int i = 0; i <= this.observers.size() - 1; i++) {
            ((Observer) this.observers.get(i)).update();
        }
    }

    /**
         * Returns the number of updates
         * @return the number of updates
         */
    public long get_nb_of_updates() {
        return this.updates;
    }
}
