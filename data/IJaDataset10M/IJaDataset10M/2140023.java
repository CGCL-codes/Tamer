package commands.model;

import commands.*;

/**
 * A command destinated to Model.
 * @author Michal Kolodziejski
 */
public class ModelCommand extends Command {

    /**
	 * A type of a command destinated to Model.
	 * @author Michal Kolodziejski
	 */
    public enum ModelCommandType {

        /** request to search for files in the list of shared files which match given criteria */
        SEARCH, /** request to return the list of shared files */
        GET_SHARED_FILES_LIST, /** request to add a file to the list of shared files */
        ADD_SHARED_FILE, /** request to remove a file from the list of shared files */
        REMOVE_SHARED_FILE, /** request to return the list of download files */
        GET_DOWNLOAD_FILES_LIST, /** request to add a file to the list of download files */
        ADD_DOWNLOAD_FILE, /** request to remove a file from the list of download files */
        REMOVE_DOWNLOAD_FILE, /** request to unblock stopped download file */
        START_DOWNLOAD_FILE, /** request to stop downloading a file */
        STOP_DOWNLOAD_FILE, /** request to change a position of a download file on the list of download files */
        MOVE_DOWNLOAD_FILE, /** request to return the list of hosts hosting a download file */
        GET_DOWNLOAD_FILE_HOSTS_LIST, /** request to add a host to the list of hosts hosting a download file */
        ADD_DOWNLOAD_FILE_HOST, /** request to remove a host from the list of hosts hosting a download file */
        REMOVE_DOWNLOAD_FILE_HOST, /** request to return a number of next lacking part of a download file */
        GET_NEXT_LACKING_PART, /** request to return a next host hosting a download file */
        GET_NEXT_HOST, /** request to return a part of a shared file */
        GET_PART_OF_FILE, /** request to save a part of a download file */
        SAVE_PART_OF_FILE, /** request to save current configuration */
        SAVE_CONFIGURATION, /** request to return a number of maximum active connections */
        GET_MAX_ACTIVE_CONNECTIONS_NUMBER, /** request to set a number of maximum active connections */
        SET_MAX_ACTIVE_CONNECTIONS_NUMBER, /** request to clear the list of hosts hosting a download file */
        CLEAR_DOWNLOAD_FILE_HOSTS_LIST, /** request to return a response if a file is on the list of shared files */
        IS_SHARED_FILE
    }

    ;

    /** Type of a command. */
    private final ModelCommandType commandType;

    public ModelCommand(ModelCommandType mct) {
        super(CommandReceiverType.MODEL);
        commandType = mct;
    }

    /**
	 * Returns a type of a command.
	 * @return type of a command
	 */
    public ModelCommandType getCommandType() {
        return commandType;
    }
}
