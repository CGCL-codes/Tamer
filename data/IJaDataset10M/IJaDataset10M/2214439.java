package joelib2.ext;

import wsi.ra.tool.BasicPropertyHolder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import org.apache.log4j.Category;

/**
 * Factory class to get external molecule process classes.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.7 $, $Date: 2005/02/17 16:48:29 $
 */
public class ExternalFactory {

    private static Category logger = Category.getInstance("joelib2.ext.ExternalFactory");

    private static ExternalFactory instance;

    private static final int DEFAULT_EXTERNAL_NUMBER = 10;

    private Hashtable extHolder;

    private BasicPropertyHolder propertyHolder;

    /**
     *  Constructor for the JOEFileFormat object
     */
    private ExternalFactory() {
        propertyHolder = BasicPropertyHolder.instance();
        extHolder = new Hashtable(DEFAULT_EXTERNAL_NUMBER);
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public static synchronized ExternalFactory instance() {
        if (instance == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Getting " + ExternalFactory.class.getClass().getName() + " instance.");
            }
            instance = new ExternalFactory();
            instance.loadInfos();
        }
        return instance;
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public Enumeration externals() {
        return extHolder.keys();
    }

    /**
     *  Gets the external attribute of the ExternalFactory class
     *
     * @param  externalName           Description of the Parameter
     * @return                        The external value
     * @exception  ExternalException  Description of the Exception
     */
    public External getExternal(String externalName) throws ExternalException {
        External external = null;
        ExternalInfo extInfo = getExternalInfo(externalName);
        if (extInfo == null) {
            return null;
        }
        try {
            external = (External) this.getClass().getClassLoader().loadClass(extInfo.getRepresentation()).newInstance();
        } catch (ClassNotFoundException ex) {
            throw new ExternalException(extInfo.getRepresentation() + " not found.");
        } catch (InstantiationException ex) {
            throw new ExternalException(extInfo.getRepresentation() + " can not be instantiated.");
        } catch (IllegalAccessException ex) {
            throw new ExternalException(extInfo.getRepresentation() + " can't be accessed.");
        }
        if (external == null) {
            throw new ExternalException("External class " + extInfo.getRepresentation() + " does'nt exist.");
        } else if (!external.isThisOSsupported()) {
            throw new ExternalException("External class " + extInfo.getRepresentation() + " can't be used on a " + ExternalHelper.getOperationSystemName() + " operating system.");
        } else {
            external.setExternalInfo(extInfo);
            return external;
        }
    }

    /**
     *  Gets the externalInfo attribute of the ExternalFactory object
     *
     * @param  name  Description of the Parameter
     * @return       The externalInfo value
     */
    public ExternalInfo getExternalInfo(String name) {
        return (ExternalInfo) extHolder.get(name);
    }

    /**
     *  Description of the Method
     *
     * @return             Description of the Return Value
     */
    private synchronized boolean loadInfos() {
        String name;
        String representation;
        String descriptionFile;
        String linuxExecutable;
        String windowsExecutable;
        String solarisExecutable;
        Properties prop = propertyHolder.getProperties();
        ExternalInfo extInfo;
        boolean allInfosLoaded = true;
        int i = 0;
        String external_i;
        StringBuffer sb = new StringBuffer(100);
        while (true) {
            i++;
            external_i = "joelib2.external." + i;
            name = prop.getProperty(external_i + ".name");
            if (name == null) {
                logger.info("" + (i - 1) + " external process informations loaded.");
                break;
            }
            representation = prop.getProperty(external_i + ".representation");
            descriptionFile = prop.getProperty(external_i + ".descriptionFile");
            linuxExecutable = prop.getProperty(external_i + ".linux");
            windowsExecutable = prop.getProperty(external_i + ".windows");
            solarisExecutable = prop.getProperty(external_i + ".solaris");
            if ((linuxExecutable == null) && (windowsExecutable == null) && (solarisExecutable == null)) {
                logger.error("There must at least one operation system specific" + " executable defined in external property " + i + ".");
            }
            int nn = 0;
            Vector args = new Vector(10);
            String argument = null;
            while (true) {
                nn++;
                argument = prop.getProperty(external_i + ".argument." + nn);
                if (argument == null) {
                    sb.append(name);
                    sb.append('(');
                    sb.append(nn - 1);
                    sb.append("),");
                    break;
                }
                args.add(argument);
            }
            extInfo = new ExternalInfo(name, representation, descriptionFile, linuxExecutable, windowsExecutable, solarisExecutable, args);
            if ((name != null) && (representation != null) && (descriptionFile != null)) {
                extHolder.put(name, extInfo);
            } else {
                allInfosLoaded = false;
                logger.error("External info number " + i + " not properly defined.");
            }
        }
        logger.info("Ext.(#Args): " + sb.toString());
        return allInfosLoaded;
    }
}
