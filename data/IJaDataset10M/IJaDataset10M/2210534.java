package org.vastenhouw.jphotar.plugins;

import java.io.File;
import java.util.Properties;
import java.util.List;
import javax.swing.JPanel;

/**
 * IExportPlugin defines the interface to define plugins to export directory
 * archives to another format, for example browsable HTML pages
 * <p>
 * <pre>
 *      Put an example here
 * </pre>
 * <p>
 * @version 1.120 04/22/99
 * @author John Vastenhouw
 */
public interface IExportPlugin {

    public String getName();

    public void initializeDefaultPluginProperties();

    public void setPluginProperties(Properties p);

    public Properties getPluginProperties();

    public void setMapProperties(Properties p);

    public Properties getMapProperties();

    public void setImageProperties(List imageProperties);

    public List getImageProperties();

    public JPanel getUIPanel();

    public void startExport();

    public void cancelExport();

    public void setInterActive(boolean b);

    public boolean getInterActive();

    public void setWorkingMap(File map);

    public File getWorkingMap();

    public void setPluginProperty(String key, String value);

    public String getPluginProperty(String key);

    public void setDebugLevel(int i);

    public int getDebugLevel();

    public void addExportTaskListener(IExportTaskListener l);

    public void removeExportTaskListener(IExportTaskListener l);

    public File getIndexFile();
}
