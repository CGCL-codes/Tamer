package net.sourceforge.dsnk.gui;

/**
 * Basic interface for (logger) error messages. These are not translated.
 * 
 * @author Jochen Kraushaar
 */
public interface ErrorMessages {

    String LOGGING_SETUP_FAILED_MESSAGE = "Cannot create or open log file. See README file.";

    String LOGGING_SETUP_FAILED_TITLE = "Error";

    String LANGUAGES_SETUP_FAILED_MESSAGE = "Cannot open language resource bundle.";

    String PROPERTIES_FILE_NOT_FOUND_MESSAGE = "File viewer.props not found, properties cannot be loaded. Creating new properties.";

    String PROPERTIES_CANNOT_READ_MESSAGE = "Cannot read file viewer.props. Creating new properties.";

    String GUMPS_CANNOT_READ_MESSAGE = "Gumps cannot be read.";

    String PROPERTIES_FILE_CANNOT_OPEN_MESSAGE = "Cannot open or create viewer.props properties file. Properties will not be saved.";

    String PROPERTIES_FILE_CANNOT_SAVE_MESSAGE = "Cannot save changes to viewer.props properties file.";

    String GUMPHASH_FILE_NOT_FOUND_MESSAGE = "Cannot find gump hash file, will create new one";

    String GUMPHASH_FILE_CANNOT_READ_MESSAGE = "Cannot read gump hash file, will create new one.";

    String GUMPHASH_FILE_NOT_FOUND_WITHOUT_NEW_MESSAGE = "Cannot find gump hash file";

    String GUMPHASH_FILE_CANNOT_READ_WITHOUT_NEW_MESSAGE = "Cannot read gump hash file";

    String GUMPHASH_FILE_CANNOT_OPEN_MESSAGE = "Cannot open or create gump.hash file. Hash values will not be saved.";

    String GUMPHASH_FILE_CANNOT_SAVE_MESSAGE = "Cannot save changes to gump.hash file.";

    String COLOR_CANNOT_BE_NULL = "transparentColor cannot be null";

    String PIXELS_ARRAY_LENGTH_VIOLATION = "pixels array must have a length >= w*h";

    String HUEENTRY_CANNOT_BE_NULL = "hueEntry cannot be null";
}
