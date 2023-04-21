package ca.etsmtl.latis.config;

import gnu.getopt.Getopt;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import ca.etsmtl.latis.constants.IConstants;

/**
 * <p>
 * Provides a way to store the configuration parameters used throught
 * out all the application.
 * </p>
 * 
 * <p>
 * The user can initialize the singleton instance the first time by
 * calling the {@link #getParameters(String[])} method. The {@code
 * String[]} parameter should contain a {@code -f} option with the path
 * to an appropriate property file to be able to specify parameters
 * that are not modifiable otherwise (there is no corresponding
 * command-line option for most of the parameters).
 * </p>
 * 
 * <p>
 * <pre>
 *     // &hellip;
 *     
 *     String[] options = {"-f", "anonym.properties"};
 *     
 *      // Call the getParamters method only for its side effect,
 *      // initializing the data structure.
 *     Configuration.getParameters(options);
 *     
 *     // &hellip;
 * </pre>
 * </p>
 * 
 * <p>
 * Usually, the user will pass the options on the command-line. The
 * initalization process can be executed only once at the beginning of
 * the program like this:
 * </p>
 * 
 * <p>
 * <pre>
 *     public class ParameterDemo {
 *     
 *         // &hellip;
 *         
 *         private ParametersBean parameters;
 *     
 *         // &hellip;
 *
 *         // The entry point of the program.
 *         public static void main(String[] args) {
 *         
 *             // &hellip;
 *         
 *             parameters = Configuration.getParameters(args)
 *         
 *             // &hellip;
 *         
 *         }
 *         
 *         // &hellip;
 *         
 *     }
 * </pre>
 * </p>
 * 
 * <p>
 * Thus, the user can call the application with the appropriate
 * command-line options:
 * </p>
 * 
 * <p>
 * <pre>
 *     $ java -cp . ParameterDemo -f anonym.properties
 * </pre>
 * </p>
 * 
 * <p>
 * After the first call to the parameterized {@link #getParameters(String[])}
 * method, the user of the class can only use the plain {@link #getParameters()}
 * method to get the unique instance of the {@link ParametersBean}
 * class which is a bean that's containing all the profile of the
 * application:
 * </p>
 * 
 * <p>
 * <pre>
 *     // &hellip;
 *     
 *     ParametersBean parameters = Configuration.getParameters();
 *     
 *     String repository = parameters.getImageRepository();
 *     
 *     // &hellip;
 * </pre>
 * </p>
 * 
 * <p>
 * Calling the {@link #getParameters(String[])} method more than once
 * is not harmful because the bean is initialized only one time by the
 * {@code Configuration} class.
 * </p>
 *
 * <p>
 * The {@code Configuration} class determines a parameter value by
 * following the steps described bellow:
 * <ol>
 * <li>It looks first in the list of parameters specified on the
 *     command-line when the program is invoked (if those parameters
 *     can be specified that way.</li>
 * <li>if it doesn't find an appropriate value on the command-line,
 *     then it looks in the property file specified with the {@code -f}
 *     command-line option.</li>
 * <li>if their is no value neither on the command-line and in the
 *     property file , it takes the default values hard-wired in the
 *     program.</li>
 * </ol>
 * These steps are followed in this order.
 * </p>
 * 
 * @version 0.0 Wed, May 17th, 2006
 * @author  Alain Lemay
 */
public class Configuration {

    /** This is the Log4j logger for the {@code Configuration} class. */
    private static final Logger logger = Logger.getLogger(Configuration.class);

    /** The unique instance of the Configration singleton class. */
    private static Configuration instance;

    /** Contains all the configuration parameters. */
    private static ParametersBean parameters = new ParametersBean();

    /** Signals that there is no more arguement on the command-line. */
    private static final int END_OF_OPTIONS_FLAG = -1;

    /** The list of options supported options */
    private static final String OPTIONS_STRING = "f:h:l:m:p:r:";

    /**
	 * <p>
	 * Constructs a new instance of the {@code Configuration} class
	 * for the specified list of options.
	 * </p>
	 * 
	 * <p>
	 * This constructor is declared {@code private} to prevent the user
	 * to instanciate the class himself.
	 * </p>
	 * 
	 * @param  args the list of command-line options.
	 * @throws IOException if an I/O exception occurs during the
	 *         reading of the property file.
	 */
    private Configuration(String[] args) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering Configuration(String[] args).");
            logger.debug("args = " + Arrays.asList(args) + ".");
        }
        Properties defaultProperties = new Properties();
        Properties propFileProperties = new Properties(defaultProperties);
        Properties cmdLineProperties = new Properties(propFileProperties);
        initDefaultProperties(defaultProperties);
        initCommandLineProperties(cmdLineProperties, args);
        initPropertyFileProperties(propFileProperties, cmdLineProperties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_PROPERTY_FILE));
        setNetworkParameters(cmdLineProperties);
        setGeneralParameters(cmdLineProperties);
        setLocaleParameters(cmdLineProperties);
        logger.debug("Exiting Configuration(String[] args).");
    }

    /**
	 * <p>
	 * Returns the instanciation status of this class.
	 * </p>
	 * 
	 * <p>
	 * This class respects the Singleton pattern but there is no way to
	 * get the instance of the {@code Configuration} class. Instead,
	 * there is a {@link #getParameters()} which returns the list of
	 * parameters as an instance of the {@link ParametersBean} class.
	 * </p>
	 * 
	 * @return {@code true} if the singleton is instanciated.
	 */
    public static boolean isInstanciated() {
        logger.debug("Entering isInstanciated().");
        boolean isInstanciated = (instance != null);
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting isInstanciated(); RV = [" + isInstanciated + "].");
        }
        return isInstanciated;
    }

    /**
	 * Gets the application's parameters according to a specified list
	 * of command-line options.
	 *  
	 * @param  args the list of options accepted from the command-line.
	 * @return an instance of a bean class with all the application's
	 *         parameters.
	 * @throws IOException if an I/O exception happens during the
	 *         initialization of the structure.
	 */
    public static ParametersBean getParameters(String[] args) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering getParameters(String[] args).");
            logger.debug("args = " + Arrays.asList(args) + ".");
        }
        if (instance == null) {
            instance = new Configuration(args);
        }
        ParametersBean parameters = Configuration.parameters;
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getParameters(String[] args); RV = [" + parameters + "].");
        }
        return parameters;
    }

    /**
	 * Gets the list of parameters once they have been initialized.
	 * 
	 * @return the application parameters.
	 */
    public static ParametersBean getParameters() {
        logger.debug("Entering getParameters().");
        ParametersBean parameters = null;
        try {
            parameters = getParameters(new String[] {});
        } catch (IOException ioex) {
            logger.error(ioex, ioex);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting getParameters(); RV = [" + parameters + "].");
        }
        return parameters;
    }

    /**
	 * Sets the parameters to their hard wired defaults.
	 * 
	 * @param defaultProperties a property list that contains all
	 *        parameter's default at the end of the execution of the
	 *        method.
	 */
    private void initDefaultProperties(Properties defaultProperties) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering initDefaultProperties(Properties defaultProperties).");
            logger.debug("defaultProperties = [" + defaultProperties + "].");
        }
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_IMAGE_REPOSITORY, ParametersBean.DEFAULT_IMAGE_REPOSITORY);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_IMAGE_EXTENSION, ParametersBean.DEFAULT_IMAGE_EXTENSION);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_MESSAGE_BUNDLE, ParametersBean.DEFAULT_MESSAGE_BUNDLE);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_IMPLEMENTATION_PROVIDER_ID, ParametersBean.DEFAULT_DICOM_IMPLEMENTATION_PROVIDER_ID);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_HOSTNAME, ParametersBean.DEFAULT_PIXELMED_IMPLEMENTATION_HOSTNAME);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_PORT, Integer.toString(ParametersBean.DEFAULT_PIXELMED_IMPLEMENTATION_PORT));
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_CALLED_APPLICATION_ENTITY_TITLE, ParametersBean.DEFAULT_PIXELMED_IMPLEMENTATION_CALLED_APPLICATION_ENTITY_TITLE);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_CALLING_APPLICATION_ENTITY_TITLE, ParametersBean.DEFAULT_PIXELMED_IMPLEMENTATION_CALLING_APPLICATION_ENTITY_TITLE);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_MOVE_ORIGINATOR_APPLICATION_ENTITY_TITLE, ParametersBean.DEFAULT_PIXELMED_IMPLEMENTATION_MOVE_ORIGINATOR_APPLICATION_ENTITY_TITLE);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_MOVE_ORIGINATOR_APPLICATION_MESSAGE_ID, Integer.toString(ParametersBean.DEFAULT_PIXELMED_IMPLEMENTATION_MOVE_ORIGINATOR_MESSAGE_ID));
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_COMPRESSION_LEVEL, Integer.toString(ParametersBean.DEFAULT_PIXELMED_IMPLEMENTATION_COMPRESSION_LEVEL));
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_DEBUG_LEVEL, Integer.toString(ParametersBean.DEFAULT_PIXELMED_IMPLEMENTATION_DEBUG_LEVEL));
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_TEMPORARY_DIRECTORY, ParametersBean.DEFAULT_DICOM_TEMPORARY_DIRECTORY);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_LANGUAGE, ParametersBean.DEFAULT_LOCALE_LANGUAGE);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_COUNTRY, ParametersBean.DEFAULT_LOCALE_COUNTRY);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_VARIANT, ParametersBean.DEFAULT_LOCALE_VARIANT);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_NETWORK_HOSTNAME, ParametersBean.DEFAULT_HOSTNAME);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_NETWORK_PORT_NUMBER, Integer.toString(ParametersBean.DEFAULT_PORT_NUMBER));
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_IMPLEMENTATION_PROVIDER_ID, ParametersBean.DEFAULT_SCP_IMPLEMENTATION_PROVIDER_ID);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_NETWORK_PORT_NUMBER, Integer.toString(ParametersBean.DEFAULT_SCP_PIXELMED_IMPLEMENTATION_NETWORK_PORT_NUMBER));
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_OUR_MAXIMUM_LENGTH_RECEIVED, Integer.toString(ParametersBean.DEFAULT_SCP_PIXELMED_IMPLEMENTATION_OUR_MAXIMUM_LENGTH_RECEIVED));
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_SOCKET_RECEIVE_BUFFER_SIZE, Integer.toString(ParametersBean.DEFAULT_SCP_PIXELMED_IMPLEMENTATION_SOCKET_RECEIVE_BUFFER_SIZE));
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_SOCKET_SEND_BUFFER_SIZE, Integer.toString(ParametersBean.DEFAULT_SCP_PIXELMED_IMPLEMENTATION_SOCKET_SEND_BUFFER_SIZE));
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_CALLED_APPLICATION_ENTITY_TITLE, ParametersBean.DEFAULT_SCP_PIXELMED_IMPLEMENTATION_CALLED_APPLICATION_ENTITY_TITLE);
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_SECURE_TRANSPORT, Boolean.toString(ParametersBean.DEFAULT_SCP_PIXELMED_IMPLEMENTATION_SECURE_TRANSPORT));
        defaultProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_DEBUG_LEVEL, Integer.toString(ParametersBean.DEFAULT_SCP_PIXELMED_IMPLEMENTATION_DEBUG_LEVEL));
        logger.debug("Exiting initDefaultProperties(Properties defaultProperties");
    }

    /**
	 * Sets the properties from the command-line list of options.
	 * 
	 * @param cmdLineProperties a property list that contains all the
	 *        parameter specified on the command-line at the end of the
	 *        execution of the method.
	 * @param args the list of command-line options.
	 */
    private void initCommandLineProperties(Properties cmdLineProperties, String[] args) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering initCommandLineProperties(Properties cmdLineProperties, String[] args).");
            logger.debug("cmdLineProperties = [" + cmdLineProperties + "].");
            logger.debug("args = " + Arrays.asList(args) + ".");
        }
        ResourceBundle bundle = ResourceBundle.getBundle(parameters.getMessageBundle(), parameters.getLocale());
        String applicationName = bundle.getString(IConstants.ANONYM_SCP_APPLICATION_NAME);
        Getopt g = new Getopt(applicationName, args, OPTIONS_STRING);
        int c;
        while ((c = g.getopt()) != END_OF_OPTIONS_FLAG) {
            switch(c) {
                case 'f':
                    cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_PROPERTY_FILE, g.getOptarg());
                    break;
                case 'h':
                    cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_NETWORK_HOSTNAME, g.getOptarg());
                    break;
                case 'l':
                    String[] tokens = g.getOptarg().split("_");
                    if (tokens.length == 1) {
                        cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_COUNTRY, tokens[0]);
                        cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_LANGUAGE, "");
                        cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_VARIANT, "");
                    } else if (tokens.length == 2) {
                        cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_LANGUAGE, tokens[0]);
                        cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_COUNTRY, tokens[1]);
                        cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_VARIANT, "");
                    } else if (tokens.length == 3) {
                        cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_LANGUAGE, tokens[0]);
                        cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_COUNTRY, tokens[1]);
                        cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_VARIANT, tokens[2]);
                    } else {
                        logger.error("Error");
                    }
                    break;
                case 'm':
                    cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_MESSAGE_BUNDLE, g.getOptarg());
                    break;
                case 'p':
                    cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_NETWORK_PORT_NUMBER, g.getOptarg());
                    break;
                case 'r':
                    cmdLineProperties.setProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_IMAGE_REPOSITORY, g.getOptarg());
                    break;
                default:
                    break;
            }
        }
        logger.debug("Exiting initCommandLineProperties(Properties cmdLineProperties, String[] args).");
    }

    /**
	 * Sets the properties from the specified property file.
	 *  
	 * @param  propFileProperties a property list that contains all the
	 *         parameter specified in the property file at the end of
	 *         the execution of the method.
	 * @param  propertyFileName the name of the property file.
	 * @throws IOException if an I/O exception occurs during the file
	 *         is read. 
	 */
    private void initPropertyFileProperties(Properties propFileProperties, String propertyFileName) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering initPropertyFileProperties(Properties propFileProperties, String propertyFileName).");
            logger.debug("propFileProperties = [" + propFileProperties + "].");
            logger.debug("propertyFileName = \"" + propertyFileName + "\".");
        }
        if (propertyFileName != null) {
            BufferedInputStream bufferedIS = new BufferedInputStream(new FileInputStream(propertyFileName));
            propFileProperties.load(bufferedIS);
        }
        logger.debug("Exiting initPropertyFileProperties(Properties propFileProperties, String propertyFileName).");
    }

    /**
	 * Sets the &quote;Network&quote; parameters.
	 * 
	 * @param properties the property list.
	 */
    private void setNetworkParameters(Properties properties) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering setNetworkParameters(Properties properties).");
            logger.debug("properties = [" + properties + "].");
        }
        parameters.setHostName(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_NETWORK_HOSTNAME));
        parameters.setPortNumber(Integer.parseInt(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_NETWORK_PORT_NUMBER)));
        logger.debug("Exiting setNetworkParameters(Properties properties).");
    }

    /**
	 * Sets the &quote;General&quote; parameters.
	 *   
	 * @param properties the property list.
	 */
    private void setGeneralParameters(Properties properties) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering setGeneralParameters(Properties properties).");
            logger.debug("properties = [" + properties + "].");
        }
        parameters.setImageRepository(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_IMAGE_REPOSITORY));
        parameters.setImageExtension(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_IMAGE_EXTENSION));
        parameters.setPixelMedImplementationHostname(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_HOSTNAME));
        parameters.setPixelMedImplementationPort(Integer.parseInt(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_PORT)));
        parameters.setPixelMedImplementationCalledApplicationEntityTitle(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_CALLED_APPLICATION_ENTITY_TITLE));
        parameters.setPixelMedImplementationCallingApplicationEntityTitle(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_CALLING_APPLICATION_ENTITY_TITLE));
        parameters.setPixelMedImplementationMoveOriginatorApplicationEntityTitle(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_MOVE_ORIGINATOR_APPLICATION_ENTITY_TITLE));
        parameters.setPixelMedImplementationMoveOriginatorMessageId(Integer.parseInt(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_MOVE_ORIGINATOR_APPLICATION_MESSAGE_ID)));
        parameters.setPixelMedImplementationCompressionLevel(Integer.parseInt(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_COMPRESSION_LEVEL)));
        parameters.setPixelMedImplementationDebugLevel(Integer.parseInt(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_PIXELMED_IMPLEMENTATION_DEBUG_LEVEL)));
        parameters.setDicomImplementationProviderId(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_IMPLEMENTATION_PROVIDER_ID));
        parameters.setDicomTemporaryDirectory(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_TEMPORARY_DIRECTORY));
        parameters.setScpImplementationProviderId(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_IMPLEMENTATION_PROVIDER_ID));
        parameters.setScpPixelMedImplementationCalledApplicationEntityTitle(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_CALLED_APPLICATION_ENTITY_TITLE));
        parameters.setScpPixelMedImplementationDebugLevel(Integer.parseInt(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_DEBUG_LEVEL)));
        parameters.setScpPixelMedImplementationNetworkPortNumber(Integer.parseInt(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_NETWORK_PORT_NUMBER)));
        parameters.setScpPixelMedImplementationOurMaximumLengthReceived(Integer.parseInt(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_OUR_MAXIMUM_LENGTH_RECEIVED)));
        parameters.setScpPixelMedImplementationSecureTransport(Boolean.parseBoolean(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_SECURE_TRANSPORT)));
        parameters.setScpPixelMedImplementationSocketReceiveBufferSize(Integer.parseInt(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_SOCKET_RECEIVE_BUFFER_SIZE)));
        parameters.setScpPixelMedImplementationSocketSendBufferSize(Integer.parseInt(properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_GENERAL_DICOM_SCP_PIXELMED_IMPLEMENTATION_SOCKET_SEND_BUFFER_SIZE)));
        logger.debug("Exiting setGeneralParameters(Properties properties).");
    }

    /**
	 * Sets the parameters related with the locale.
	 * 
	 * @param properties the property list.
	 */
    private void setLocaleParameters(Properties properties) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering setLocaleParameters(Properties properties).");
            logger.debug("properties = [" + properties + "].");
        }
        String language = properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_LANGUAGE);
        String country = properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_COUNTRY);
        String variant = properties.getProperty(IConstants.ANONYM_PROPERTY_NAME_LOCAL_VARIANT);
        parameters.setLocale(new Locale(language, country, variant));
        logger.debug("Exiting setLocaleParameters(Properties properties).");
    }
}
