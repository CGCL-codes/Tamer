package gov.sns.apps.pta.rscmgt;

import gov.sns.apps.pta.MainApplication;
import java.io.IOException;
import java.util.Properties;

/**
 * <h1>AppProperties</h1>
 * <p>
 * Manages the configuration properties for the application.  The default 
 * properties, i.e. the properties used upon application installation or 
 * a <code>Preferences</code> node corruption,
 * are keep in the properties file indicated by the constant
 * <code>{@link #STR_FILE_DEFAULT}</code>.  The application configuration
 * attributes are available programmatically using the enumerations
 * internal to this class.  They return a <code>PropertyValue</code>
 * object which provides methods for type conversion.
 * </p>
 * 
 *
 * @since  Jun 17, 2009
 * @author Christopher K. Allen
 * 
 * @see AppProperties#STR_FILE_DEFAULT
 * @see Property
 * @see Property.IProperty
 */
public final class AppProperties extends PropertiesManager {

    /**  
     * The name of the properties file containing the default values of for
     * the application configuration properties.
     */
    public static final String STR_FILE_DEFAULT = "DefaultApp.properties";

    /**
     * Provides access to the singleton application properties
     * manager.  
     *
     * @return  The single instance of the application properties manager 
     * 
     * @since  Jul 9, 2010
     * @author Christopher K. Allen
     */
    public static synchronized AppProperties getInstance() {
        return MGR_APP_PROPS;
    }

    /**
     * <p>
     * Enumeration of the global configuration properties for the 
     * application.
     * </p>
     * <p>
     * The enumeration class initializes by loading the application
     * configuration file and creating the attribute dictionary.
     * Application property values are accessible from this
     * enumeration via the <tt>get<b>Type</b></tt> methods where
     * <tt><b>Type</b></tt> refers to the Java type requested
     * (i.e., <code>String</code>, <code>int</code>, etc).
     * Each value access method defers to the application properties
     * dictionary converting the string value to the
     * requested type. 
     * </p>
     * 
     * @author Christopher K. Allen
     * @since Jun 11, 2009
     *
     */
    public enum APP implements Property.IProperty {

        /** The application name  */
        NAME("AppName"), /** The size of each measurement set    */
        FACILITY("AppInstallation"), /** Label for persistent data     */
        TAG_DATA("AppDataLabel"), /** Any icon associated with the application */
        ICON("AppIcon"), /**  The default font size used by application */
        FONTSZ("AppFont"), /** The default font used by the application */
        FONTTYPE("AppFontType"), /** The value of position axis left end point  */
        SCR_WIDTH("AppScrWidth"), /** Length between position locations */
        SCR_HEIGHT("AppScrHeight");

        /**
         * Returns the value of the property as a 
         * <code>PropertyValue</code> object.  This
         * object can then be used to convert the 
         * raw (string) value of the property value
         * into the proper type.
         * 
         * @return      value of the property corresponding to
         *              this enumeration constant
         *  
         * @since 	Jul 15, 2009
         * @author  Christopher K. Allen
         *
         * @see gov.sns.apps.pta.rscmgt.Property.IProperty#getValue()
         */
        @Override
        public Property getValue() {
            return this.valProperty;
        }

        /** The application configuration property */
        private final Property valProperty;

        /**
         * <p> 
         * Constructs the application properties enumeration 
         * initializes the properties to their values in the 
         * configuration file.
         * </p>
         * 
         * @param       strPropName     name of the property in the property file
         * 
         * @since  Jun 11, 2009
         * @author Christopher K. Allen
         * 
         * 
         * @see        APP#STR_FILE_DEFAULT
         */
        private APP(String strPropName) {
            this.valProperty = MGR_APP_PROPS.getProperty(strPropName);
        }
    }

    /**
     * Enumeration of the bug report configuration
     * parameters for the application.
     *
     *
     * @since  Feb 1, 2010
     * @author Christopher K. Allen
     */
    public enum BGRPRT implements Property.IProperty {

        /** Application bug report file location */
        FILE("BgRprtFile"), /** Text editor character set */
        CHARSET("BgRprtEditorCharset"), /** General text editor screen width */
        SCR_WD("BgRprtEditorScrWd"), /** General text editor screen height */
        SCR_HT("BgRprtEditorScrHt");

        /**
         * Returns the value of the property as a 
         * <code>PropertyValue</code> object.  This
         * object can then be used to convert the 
         * raw (string) value of the property value
         * into the proper type.
         * 
         * @return      value of the property corresponding to
         *              this enumeration constant
         *  
         * @since       Jul 15, 2009
         * @author  Christopher K. Allen
         *
         * @see gov.sns.apps.pta.rscmgt.Property.IProperty#getValue()
         */
        @Override
        public Property getValue() {
            return this.valProperty;
        }

        /** The application configuration property */
        private final Property valProperty;

        /**
         * <p> 
         * Constructs the bug report properties enumeration 
         * initializes the properties to their values in the 
         * configuration file.
         * </p>
         * 
         * @param       strPropName     name of the property in the property file
         * 
         * @since  Jun 11, 2009
         * @author Christopher K. Allen
         * 
         * 
         * @see        APP#STR_FILE_DEFAULT
         */
        private BGRPRT(String strPropName) {
            this.valProperty = MGR_APP_PROPS.getProperty(strPropName);
        }
    }

    /**
     * Enumeration of the application's process variable (PV)
     * logging configuration parameters.
     *
     * @since  Mar 19, 2010
     * @author Christopher K. Allen
     */
    public enum PVLOG implements Property.IProperty {

        /**  Group ID of the PV log for measurements */
        MSMT_ID("PvLogMsmtId");

        /**
         * Returns the value of the property as a 
         * <code>PropertyValue</code> object.  This
         * object can then be used to convert the 
         * raw (string) value of the property value
         * into the proper type.
         * 
         * @return      value of the property corresponding to
         *              this enumeration constant
         *  
         * @since       Jul 15, 2009
         * @author  Christopher K. Allen
         *
         * @see gov.sns.apps.pta.rscmgt.Property.IProperty#getValue()
         */
        @Override
        public Property getValue() {
            return this.valProperty;
        }

        /** The application configuration property */
        private final Property valProperty;

        /**
         * <p> 
         * Constructs the application logging properties enumeration 
         * initializes the properties to their values in the 
         * configuration file.
         * </p>
         * 
         * @param       strPropName     name of the property in the property file
         * 
         * @since  Jun 11, 2009
         * @author Christopher K. Allen
         * 
         * 
         * @see        APP#STR_FILE_DEFAULT
         */
        private PVLOG(String strPropName) {
            this.valProperty = MGR_APP_PROPS.getProperty(strPropName);
        }
    }

    /**
     * Enumeration of application event logging properties.
     *
     * @since  Nov 20, 2009
     * @author Christopher K. Allen
     */
    public enum EVTLOG implements Property.IProperty {

        /** Logging on or off */
        ENABLE("EvtLogEnable"), /** Application logging file location */
        FILE("EvtLogFile"), /** Continuous logging flag (i.e., past one application instance) */
        CONTINUE("EvtLogContinuous"), /** Log verbose debugging information */
        VERBOSE("EvtLogVerbose");

        /**
         * Returns the value of the property as a 
         * <code>PropertyValue</code> object.  This
         * object can then be used to convert the 
         * raw (string) value of the property value
         * into the proper type.
         * 
         * @return      value of the property corresponding to
         *              this enumeration constant
         *  
         * @since       Jul 15, 2009
         * @author  Christopher K. Allen
         *
         * @see gov.sns.apps.pta.rscmgt.Property.IProperty#getValue()
         */
        @Override
        public Property getValue() {
            return this.valProperty;
        }

        /** The application configuration property */
        private final Property valProperty;

        /**
         * <p> 
         * Constructs the application logging properties enumeration 
         * initializes the properties to their values in the 
         * configuration file.
         * </p>
         * 
         * @param       strPropName     name of the property in the property file
         * 
         * @since  Jun 11, 2009
         * @author Christopher K. Allen
         * 
         * 
         * @see        APP#STR_FILE_DEFAULT
         */
        private EVTLOG(String strPropName) {
            this.valProperty = MGR_APP_PROPS.getProperty(strPropName);
        }
    }

    /**
     * Enumeration of the application's device control
     * configuration parameters.
     *
     * @since  Mar 19, 2010
     * @author Christopher K. Allen
     */
    public enum DEVCTRL implements gov.sns.apps.pta.rscmgt.Property.IProperty {

        /**  Group ID of the PV log for measurements */
        CMD_LATENCY("DevCtrlCmdLatency");

        /**
         * Returns the value of the property as a 
         * <code>PropertyValue</code> object.  This
         * object can then be used to convert the 
         * raw (string) value of the property value
         * into the proper type.
         * 
         * @return      value of the property corresponding to
         *              this enumeration constant
         *  
         * @since       Jul 15, 2009
         * @author  Christopher K. Allen
         *
         * @see gov.sns.apps.pta.rscmgt.Property.IProperty#getValue()
         */
        @Override
        public Property getValue() {
            return this.valProperty;
        }

        /** The application configuration property */
        private final Property valProperty;

        /**
         * <p> 
         * Constructs the application logging properties enumeration 
         * initializes the properties to their values in the 
         * configuration file.
         * </p>
         * 
         * @param       strPropName     name of the property in the property file
         * 
         * @since  Jun 11, 2009
         * @author Christopher K. Allen
         * 
         * 
         * @see        APP#STR_FILE_DEFAULT
         */
        private DEVCTRL(String strPropName) {
            this.valProperty = MGR_APP_PROPS.getProperty(strPropName);
        }
    }

    /**
     * Enumeration of the profile data plot configuration
     * parameters.
     *
     * @since  Jul 15, 2009
     * @author Christopher K. Allen
     */
    public enum PLT implements gov.sns.apps.pta.rscmgt.Property.IProperty {

        /**  The plot legend key for the horizontal projection data */
        LGD_KEY_HOR("PltLegendKeyHor"), /**  The plot legend key for the vertical projection data */
        LGD_KEY_VER("PltLegendKeyVer"), /**  The plot legend key for the diagonal projection data */
        LGD_KEY_DIA("PltLegendKeyDia"), /** Scan start icon */
        ICON_CLEAR("PltClearGraph"), /**  The color of the curve for horizontal projection data */
        CLR_CRV_HOR("PltCurveColorHor"), /**  The color of the curve for vertical projection data */
        CLR_CRV_VER("PltCurveColorVer"), /**  The color of the curve for diagonal projection data */
        CLR_CRV_DIA("PltCurveColorDia"), /** Profile plot RED color value */
        CLR_BGND("PltBgndColor"), /** Color of the lines used to draw the trace processing window */
        CLR_LN_PRCG("PltLineColorPrcgWnd"), /** 
         * Width of the profile data plots 
         * @deprecated  not used 
         */
        PROFILE_WD("ProfileDataPltWd"), /** 
         * Height of the profile data plots 
         * @deprecated not used
         */
        PROFILE_HT("ProfileDataPltHt"), /** Profile plot GREEN color value 
         * @deprecated not used
         */
        PROFILE_GREEN("PltBkColorGreen"), /** Profile plot RED color value
         * @deprecated not used 
         */
        PROFILE_BLUE("PltBkColorBlue");

        /**
         * Return the property value associated with this
         * enumeration constant.
         * 
         * @return      value of the property as a 
         *              <code>PropertyValue</code> object
         *              
         * @since 	Jul 15, 2009
         * @author  Christopher K. Allen
         *
         * @see gov.sns.apps.pta.rscmgt.Property.IProperty#getValue()
         */
        @Override
        public Property getValue() {
            return this.valProperty;
        }

        /** The property value object */
        private final Property valProperty;

        private PLT(String strPropName) {
            this.valProperty = MGR_APP_PROPS.getProperty(strPropName);
        }
    }

    /**
     * Enumeration of properties applicable to device
     * tables.
     *
     * @since  Sep 15, 2009
     * @author Christopher K. Allen
     */
    public enum DEVSEL implements gov.sns.apps.pta.rscmgt.Property.IProperty {

        /** Width of the profile data plots 
         * @deprecated Not used
         */
        TREE_WD("DevSelTreeTotalWd"), /** Height of the profile data plots 
         * @deprecated Not used
         */
        TREE_HT("DevSelTableTotalHt"), /** Width of the profile data plots 
         * @deprecated Not used
         */
        TABLE_WD("DevSelTableTotalWd"), /** Height of the profile data plots 
         * @deprecated Not used
         */
        TABLE_HT("DevSelTableTotalHt"), /** Device Id column width */
        DEVIDCOL_WD("DevSelTableDevIdColWd");

        /**
         * Return the property value associated with this
         * enumeration constant.
         * 
         * @return      value of the property as a 
         *              <code>Property</code> object
         *              
         * @since       Jul 15, 2009
         * @author  Christopher K. Allen
         *
         * @see gov.sns.apps.pta.rscmgt.Property.IProperty#getValue()
         */
        @Override
        public Property getValue() {
            return this.valProperty;
        }

        /** The property value object */
        private final Property valProperty;

        private DEVSEL(String strPropName) {
            this.valProperty = MGR_APP_PROPS.getProperty(strPropName);
        }
    }

    /**
     * Enumeration of properties applicable to Data Acquisition
     * Tables.
     *
     * @since  Sep 15, 2009
     * @author Christopher K. Allen
     */
    public enum DAQGUI implements gov.sns.apps.pta.rscmgt.Property.IProperty {

        /** Scan start icon */
        ICON_START("DaqIconStart"), /** Scan stop icon */
        ICON_STOP("DaqIconStop"), /** Scan abort icon */
        ICON_ABORT("DaqIconAbort"), /** Scan park actuator icon */
        ICON_PARK("DaqIconPark"), /** (Re)acquisition of data from diagnostic device */
        ICON_ACQUIRE("DaqIconAcquire"), /** Total Width of the DAQ controller panel */
        TOTAL_WD("DaqPrgTblWd"), /** Total Height of the DAQ controller panel */
        PRG_TBL_HT("DaqPrgTblHt"), /** Device Id column width */
        DEVIDCOL_WD("DaqPrgTblDevIdColWd"), /** The progress bar column width */
        PROGCOL_WD("DaqPrgTblProgBarColWd"), /** The motion state display column width */
        MOTIONCOL_WD("DaqPrgTblMvtStateColWd");

        /**
         * Return the property value associated with this
         * enumeration constant.
         * 
         * @return      value of the property as a 
         *              <code>Property</code> object
         *              
         * @since       Jul 15, 2009
         * @author  Christopher K. Allen
         *
         * @see gov.sns.apps.pta.rscmgt.Property.IProperty#getValue()
         */
        @Override
        public Property getValue() {
            return this.valProperty;
        }

        /** The property value object */
        private final Property valProperty;

        private DAQGUI(String strPropName) {
            this.valProperty = MGR_APP_PROPS.getProperty(strPropName);
        }
    }

    /**
     * Enumeration of properties applicable to the
     * scan configuration panel.
     *
     * @since  Sep 15, 2009
     * @author Christopher K. Allen
     * 
     * @deprecated not used
     */
    public enum SCAN_CFG implements gov.sns.apps.pta.rscmgt.Property.IProperty {

        /** Apply changes button icon 
         * @deprecated not used 
         */
        APPLY_ICON("ScanConfigApplyIcon"), /** 
         * Width of the profile data plots 
         * @deprecated  not used
         */
        TOTAL_WD("ScanConfigPanelWd"), /** 
         * Height of the profile data plots
         * @deprecated not used 
         */
        TOTAL_HT("ScanConfigPanelHt");

        /**
         * Return the property value associated with this
         * enumeration constant.
         * 
         * @return      value of the property as a 
         *              <code>PropertyValue</code> object
         *              
         * @since       Jul 15, 2009
         * @author  Christopher K. Allen
         *
         * @see gov.sns.apps.pta.rscmgt.PropertyValue.IPropertyEnumeration#getValue()
         */
        @Override
        public Property getValue() {
            return this.valProperty;
        }

        /** The property value object */
        private final Property valProperty;

        /** Create the enumeration constant */
        private SCAN_CFG(String strPropName) {
            this.valProperty = MGR_APP_PROPS.getProperty(strPropName);
        }
    }

    /** The singleton property manager for the persistent application properties */
    private static AppProperties MGR_APP_PROPS;

    /**
     *  Static block setting the single property manager instance 
     *  and default configuration properties.
     *
     * @since  Jul 15, 2009
     * @author Christopher K. Allen
     */
    static {
        try {
            Properties propsDefault = ResourceManager.getProperties(STR_FILE_DEFAULT);
            MGR_APP_PROPS = new AppProperties(propsDefault);
        } catch (IOException e) {
            String strErrMsg = "Application configuration mechanism corrupted. \n" + "See AppProperties class. \n";
            MainApplication.applicationLaunchFailure(strErrMsg, e);
        }
    }

    /**
     * Singleton class - prevent outside instantiation.
     *
     * @param   setDefProps     the set of default property values
     *
     * @since     Jul 15, 2009
     * @author    Christopher K. Allen
     */
    private AppProperties(Properties setDefProps) {
        super(AppProperties.class, setDefProps);
    }

    ;
}
