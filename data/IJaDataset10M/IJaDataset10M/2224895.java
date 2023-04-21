package cz.cuni.mff.ksi.jinfer.xsdimporter.utils;

import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.xsdimporter.interfaces.XSDParser;
import cz.cuni.mff.ksi.jinfer.xsdimporter.properties.XSDImportPropertiesPanel;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Helper class for accessing settings from module's properties panel.
 * @author reseto
 */
public final class XSDImportSettings {

    private XSDImportSettings() {
    }

    private static final Properties PROPERTIES = RunningProject.getActiveProjectProps(XSDImportPropertiesPanel.PANEL_NAME);

    /**
   * Get the selected parser from XSD Import project properties.
   * Parser is selected from all available parsers using lookup for <code>XSDParser</code> interface.
   * @return Selected parser.
   */
    public static XSDParser getParser() {
        return ModuleSelectionHelper.lookupImpl(XSDParser.class, PROPERTIES.getProperty(XSDImportPropertiesPanel.PARSER_PROP));
    }

    /**
   * Check if more verbose information should be logged.
   * @return True if verbose setting enabled, else false. False on error.
   */
    public static boolean isVerbose() {
        return Boolean.parseBoolean(PROPERTIES.getProperty(XSDImportPropertiesPanel.VERBOSE_INFO_PROP, XSDImportPropertiesPanel.VERBOSE_INFO_DEFAULT));
    }

    /**
   * Get current log level for module XSD Importer. On error, or by default this method returns root log level.
   * @return Current log level for XSD Importer.
   */
    public static Level getLogLevel() {
        return Level.toLevel(PROPERTIES.getProperty(XSDImportPropertiesPanel.LOG_LEVEL_PROP, XSDImportPropertiesPanel.LOG_LEVEL_DEFAULT), Logger.getRootLogger().getLevel());
    }

    /**
   * Check if import process should halt on any error.
   * If disabled, the file is just skipped and error is logged.
   * By default this setting is enabled.
   * @return True if "Stop on error" checkbox is checked, else false. True if exception occurs.
   */
    public static boolean isStopOnError() {
        return Boolean.parseBoolean(PROPERTIES.getProperty(XSDImportPropertiesPanel.STOP_ON_ERROR_PROP, XSDImportPropertiesPanel.STOP_ON_ERROR_DEFAULT));
    }
}
