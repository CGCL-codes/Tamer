package jdc.lib;

import org.apache.log4j.Logger;

/**
 * This class holds all information about a hub.
 */
public class Hub {

    /** get the logger for this package */
    protected static Logger libLogger = LoggerContainer.getLogger(Hub.class);

    protected static int _static_hub_ctr = 0;

    private int _unique_instance_number;

    private static Object _counter_mutex = new Object();

    /** Name of this hub. */
    private String _name;

    private String _unique_name;

    /** The initial Name of this hub. */
    private String _initial_name;

    /** Brief description of the hub and its' contents. */
    private String _description;

    /** Name of the host for this hub. */
    private String _host;

    /** Port to connect to. */
    private int _port;

    /** Number of users currently connected to this hub. */
    private long _current_nr_of_users;

    void setName(String name) {
        _name = name;
    }

    public void setDesc(String desc) {
        _description = desc;
    }

    public String getName() {
        return _name;
    }

    public String getUniqueName() {
        return _unique_name;
    }

    public String getInitialName() {
        return _initial_name;
    }

    public String getHost() {
        return _host;
    }

    public String getDescription() {
        return _description;
    }

    public int getPort() {
        return _port;
    }

    public long getCurrentNrOfUsers() {
        return _current_nr_of_users;
    }

    public String getKey() {
        return _host + _port + _unique_instance_number;
    }

    public String getHubListStr() {
        return _name + "|" + _host + ":" + _port + "|" + _description + "|" + _current_nr_of_users + "|" + "|||||\n";
    }

    public String getHubListStr(String desc) {
        return _name + "|" + _host + ":" + _port + "|" + desc + "|" + _current_nr_of_users + "|" + "|||||\n";
    }

    /**
   * Use this constructor to create a hub with the most important values
   *
   * @param name Name of the hub
   * @param host Host of the hub
   * @param port Port of the hub
   */
    public Hub(String name, String host, int port) {
        _name = name;
        _initial_name = name;
        _host = host;
        _port = port;
        synchronized (_counter_mutex) {
            if (_static_hub_ctr == Integer.MAX_VALUE) _static_hub_ctr = 0;
            _unique_instance_number = _static_hub_ctr++;
        }
        _unique_name = _initial_name + _unique_instance_number;
    }

    /**
   * Constructor taking the external representation of a Hub.
   *
   * @param external_rep String with the following look
   *  <name>|<host[:port]>|<description>|<nr of users>|...|
   */
    public Hub(String external_rep) {
        _host = "";
        _port = 411;
        _description = "";
        _current_nr_of_users = 0;
        int pos = 0;
        int start = 0;
        for (int i = 0; i < external_rep.length(); i++) {
            if (external_rep.charAt(i) == '|') {
                String to_add = "";
                if ((start != (i - 1)) && (i > 0)) to_add = external_rep.substring(start, i);
                _initValues(to_add, pos);
                start = i + 1;
                pos++;
            }
        }
        _initial_name = _name;
        synchronized (_counter_mutex) {
            if (_static_hub_ctr == Integer.MAX_VALUE) _static_hub_ctr = 0;
            _unique_instance_number = _static_hub_ctr++;
        }
        _unique_name = _initial_name + _unique_instance_number;
    }

    /**
   * Dumps this object via logger
   */
    public void print() {
        libLogger.debug("Hub");
        libLogger.debug("{");
        libLogger.debug("  Name: '" + _name + "'");
        libLogger.debug("  Description: '" + _description + "'");
        libLogger.debug("  Host: '" + _host + "'");
        libLogger.debug("  Port: '" + _port + "'");
        libLogger.debug("  Users: '" + _current_nr_of_users + "'");
        libLogger.debug("}");
    }

    /**
   * Return this object as string description.
   */
    public String hubText() {
        String out = "****\n" + "  Name: '" + _name + "'\n" + "  Description: '" + _description + "'\n" + "  Host: '" + _host + "'\n" + "  Port: '" + _port + "'\n" + "  Users: '" + _current_nr_of_users + "'\n" + "****\n";
        return out;
    }

    private void _getHostPort(String str) {
        _host = str;
        int cl_idx = str.indexOf(':');
        if (cl_idx > 0) {
            _host = str.substring(0, cl_idx);
            String port_str = str.substring(cl_idx + 1, str.length());
            int cl2_idx = port_str.indexOf(":");
            if (cl2_idx > 0) {
                port_str = port_str.substring(0, cl2_idx);
            }
            try {
                _port = Integer.parseInt(port_str);
            } catch (java.lang.NumberFormatException ex) {
                libLogger.debug("Couldn't turn this port " + "string into a number: " + port_str);
            }
        }
    }

    /**
   * Take the iterator and set the attribute corresponding
   * to that position in the list.
   *
   * @param itr The iterator holding the values
   */
    private void _initValues(String str, int pos) {
        switch(pos) {
            case 0:
                _name = str;
                break;
            case 1:
                _getHostPort(str);
                break;
            case 2:
                _description = str;
                break;
            case 3:
                {
                    if (str.length() > 0) {
                        try {
                            _current_nr_of_users = Long.parseLong(str);
                        } catch (NumberFormatException ex) {
                            libLogger.debug("Could not parse number of users: " + str);
                        }
                    }
                }
                break;
            default:
                ;
        }
    }
}
