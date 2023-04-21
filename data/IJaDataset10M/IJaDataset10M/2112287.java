package hu.sztaki.lpds.pgportal.services.pgrade;

import java.util.*;
import java.io.*;
import hu.sztaki.lpds.pgportal.services.utils.PropertyLoader;
import hu.sztaki.lpds.pgportal.services.is.mds2.JobManagerSettingsFacade;

public class GridConfigs {

    public static final String CLUSTERGRID_NAME = "CLUSTER-GRID";

    public static final String LCG_BROKER_NAME = "LCG_2_BROKER";

    public static final String GLITE_BROKER_NAME = "GLITE_BROKER";

    private static GridConfigs instance = null;

    private Hashtable gridConfigs = null;

    private String configFilePath = null;

    public static synchronized GridConfigs getInstance() {
        if (GridConfigs.instance == null) {
            System.out.println("GridConfigs.getInstance() called - create instance");
            GridConfigs.instance = new GridConfigs();
        }
        return GridConfigs.instance;
    }

    public synchronized GridConfiguration getGridConfig(String gridName) {
        GridConfigs gc = GridConfigs.getInstance();
        GridConfiguration[] gcs = gc.getGridConfigs();
        for (int i = 0; i < gcs.length; i++) {
            if (gcs[i].getName().equals(gridName)) {
                return gcs[i];
            }
        }
        return null;
    }

    private GridConfigs() {
        System.out.println("GridConfigs() constr. called");
        this.gridConfigs = new Hashtable();
        try {
            this.configFilePath = PropertyLoader.getInstance().getProperty("portal.prefix.dir") + "/users/.grid.resources.conf";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (this.configFilePath != null) {
            this.loadConfiguration();
        }
    }

    public synchronized boolean addGridConfiguration(GridConfiguration gc, boolean doSave) {
        if (this.gridConfigs.get(gc.getName()) != null) {
            return false;
        }
        this.gridConfigs.put(gc.getName(), gc);
        if (doSave) {
            this.saveGridConfigs();
        }
        return true;
    }

    public static boolean existsGrid(String gridName) {
        GridConfigs gc = GridConfigs.getInstance();
        if (gc.getGridConfig(gridName) == null) {
            return false;
        }
        return true;
    }

    public synchronized void deleteGridConfiguration(String gridName) {
        System.out.println("deleteGridConfiguration(" + gridName + ") called");
        this.getGridConfig(gridName).removeIS();
        this.gridConfigs.remove(gridName);
        this.deleteAllJMConfigFiles(gridName);
        GridJobManagerConfigs gJMConfs = GridJobManagerConfigs.getDefaultInstance(gridName);
        gJMConfs.removeAllConfigurations(gridName);
        this.saveGridConfigs();
    }

    public GridConfiguration[] getGridConfigs() {
        Vector gcs = new Vector();
        Set keys = this.gridConfigs.keySet();
        for (Iterator iter = keys.iterator(); iter.hasNext(); ) {
            GridConfiguration gc = (GridConfiguration) this.gridConfigs.get(iter.next());
            gcs.add(gc);
        }
        Collections.sort(gcs);
        GridConfiguration[] result = new GridConfiguration[gcs.size()];
        for (int i = 0; i < gcs.size(); i++) {
            result[i] = (GridConfiguration) gcs.elementAt(i);
        }
        return result;
    }

    public synchronized void saveGridConfigs() {
        System.out.println("GridConfigs.saveGridConfigs()");
        this.saveToFile();
    }

    private void deleteAllJMConfigFiles(String gridName) {
        System.out.println("deleteAllJMConfigFiles(" + gridName + ") called");
        File gridConfigs = new File(this.configFilePath);
        File[] userDirs = gridConfigs.getParentFile().listFiles(new FileFilter() {

            public boolean accept(File f) {
                return f.isDirectory() ? true : false;
            }
        });
        for (int i = 0; i < userDirs.length; i++) {
            File f = new File(userDirs[i].getAbsolutePath() + "/.resources.conf." + gridName);
            System.out.println("deleteAllJMConfigFiles - f[" + i + "] : " + f.getAbsolutePath());
            if (f.isFile()) f.delete();
        }
        File f = new File(gridConfigs.getParent() + "/.resources.conf." + gridName);
        System.out.println("deleteAllJMConfigFiles - f: " + f.getAbsolutePath());
        if (f.isFile()) f.delete();
    }

    /**
   * Saves current content of the configuration to the user's configuration file.
   */
    private synchronized boolean saveToFile() {
        try {
            File dest = new File(this.configFilePath);
            if (!dest.getParentFile().exists()) dest.getParentFile().mkdir();
            BufferedWriter bw = new BufferedWriter(new FileWriter(dest));
            bw.write("# GRID CONFIGURATIONS for P-GRADE PORTAL\n");
            for (Iterator iter = this.gridConfigs.keySet().iterator(); iter.hasNext(); ) {
                GridConfiguration item = (GridConfiguration) this.gridConfigs.get(iter.next());
                System.out.println("GridConfigs.saveToFile() - item: " + item);
                bw.write("" + item);
                bw.newLine();
            }
            bw.flush();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private synchronized void loadConfiguration() {
        System.out.println("GridConfigs.loadConfiguration() START");
        File cf = new File(this.configFilePath);
        BufferedReader r = null;
        if (!cf.exists()) {
            System.out.println("GridConfigs.loadConfiguration() TERMINATED as not existent file:" + this.configFilePath);
            return;
        } else {
            try {
                r = new BufferedReader(new FileReader(cf));
                String line = null;
                do {
                    line = r.readLine();
                    if (line != null) {
                        if (!line.startsWith("#")) {
                            while (line.startsWith(" ")) {
                                line = line.substring(1);
                            }
                            String[] elements = line.split(" ");
                            System.out.println("GridConfigs.loadConfiguration() - elements.length:" + elements.length);
                            if (elements.length == 0) {
                            } else if (elements.length == 3) {
                                System.out.println("GridConfigs.loadConfiguration() - 3");
                                GridConfiguration gc = new GridConfiguration(elements[0]);
                                this.addGridConfiguration(gc, false);
                            } else if (elements.length == 6) {
                                System.out.println("GridConfigs.loadConfiguration() - 6");
                                if (elements[2].equals(GridConfiguration.IS_TYPES[2])) {
                                    GridConfiguration gc = new GridConfiguration(elements[0]);
                                    gc.defineIS((short) 2, elements[3], elements[4], elements[5]);
                                    this.addGridConfiguration(gc, false);
                                }
                            } else if (elements.length == 8) {
                                System.out.println("GridConfigs.loadConfiguration() - 8");
                                if (elements[2].equals(GridConfiguration.IS_TYPES[1])) {
                                    GridConfiguration gc = new GridConfiguration(elements[0]);
                                    gc.defineIS((short) 1, elements[3], elements[4], elements[5]);
                                    gc.setMPSData(elements[6], elements[7]);
                                    this.addGridConfiguration(gc, false);
                                }
                            }
                        }
                    }
                } while (line != null);
                r.close();
                System.out.println("GridConfigs.loadConfiguration() END");
            } catch (Exception ex) {
                System.out.println("GridConfigs.loadConfiguration() from '" + this.configFilePath + "'  ~ failed:" + ex.getMessage());
            } finally {
                System.out.println("GridConfigs.loadConfiguration() END - finally");
            }
        }
    }

    public static boolean isBrokered(String gridName) {
        return gridName.endsWith(CLUSTERGRID_NAME) || gridName.endsWith(LCG_BROKER_NAME) || gridName.endsWith(GLITE_BROKER_NAME);
    }

    public static synchronized String[] getGridNames() {
        GridConfiguration[] grids = GridConfigs.getInstance().getGridConfigs();
        if (grids == null || grids.length == 0) {
            System.out.println("GridConfigs.getGridNames() - EMPTY!!!");
            return null;
        }
        Vector names = new Vector(grids.length);
        for (int i = 0; i < grids.length; i++) {
            names.add(i, grids[i].getName());
        }
        String[] result = new String[grids.length];
        return (String[]) names.toArray(result);
    }
}
