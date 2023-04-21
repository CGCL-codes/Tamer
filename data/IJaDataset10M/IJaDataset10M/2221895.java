package org.blue.star.plugins;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Usage: check_dummy <integer state> [optional text]
 */
public class check_dummy extends check_base {

    protected String getAuthor() {
        return "Richard Friedman richardfriedman@yahoo.com";
    }

    protected String getCopyright() {
        return "Richard Friedman 2007";
    }

    protected String getDescription() {
        return "This plugin will simply return the state corresponding to the numeric value\n" + "of the <state> argument with optional text.\n\n";
    }

    protected String getNotes() {
        return "";
    }

    int state = -1;

    String message = null;

    String state_string = null;

    String extra_text = null;

    public static void main(String[] args) {
        new check_dummy().process_request(args);
    }

    public void init_command() {
    }

    public void add_command_arguments(Options options) {
    }

    public void process_command_option(Option o) throws IllegalArgumentException {
    }

    /**
    * Process remaining command line arguments.
    */
    public void process_command_arguments(String[] args) throws IllegalArgumentException {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("State must be first argument");
        }
        state_string = args[0];
        if (args.length >= 2) extra_text = args[1];
    }

    /**
    * Validate the argument set.
    */
    public void validate_command_arguments() throws IllegalArgumentException {
        try {
            state = Integer.parseInt(state_string);
        } catch (NumberFormatException nfE) {
            throw new IllegalArgumentException("Arguments to check_dummy must be an integer\n");
        }
    }

    public boolean execute_check() {
        switch(state) {
            case common_h.STATE_OK:
                message = "OK";
                break;
            case common_h.STATE_WARNING:
                message = "WARNING";
                break;
            case common_h.STATE_CRITICAL:
                message = "CRITICAL";
                break;
            case common_h.STATE_UNKNOWN:
                message = "UNKNOWN";
                break;
            default:
                message = "Status " + state + " is not a supported error state\n";
                break;
        }
        if (extra_text != null) message += " :" + extra_text;
        return true;
    }

    public int check_state() {
        return state;
    }

    public String check_message() {
        return message;
    }
}
