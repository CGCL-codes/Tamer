package org.jtomtom.device;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.jtomtom.JTomtomException;
import org.jtomtom.connector.POIsDbInfos;
import org.jtomtom.connector.RadarsConnector;
import org.jtomtom.tools.JTomTomUtils;

/**
 * Class for map management
 * 
 * @author Frédéric Combes
 *
 */
public class TomtomMap {

    private static final Logger LOGGER = Logger.getLogger(TomtomMap.class);

    /**
	 * Map name
	 */
    private String name;

    /**
	 * Full version of the map (major.minor)
	 */
    private String version;

    /**
	 * Absolute path of the map
	 */
    private String absolutePath;

    /**
	 * Radars informations
	 */
    private POIsDbInfos radarsInformations;

    /**
	 * FilenameFilter use for finding .pna file inside map directories
	 */
    private static final FilenameFilter PNA_FILE_FILTER = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return name.endsWith(".pna");
        }
    };

    private TomtomMap() {
        name = "";
        version = "";
        absolutePath = "";
    }

    /**
	 * Test if a directory is a map directory or not
	 * @param directory		Directory under test
	 * @return				True if the directory is a Tomtom Map directory
	 */
    public static final boolean isMapDirectory(File directory) {
        return (directory.isDirectory() && directory.list(PNA_FILE_FILTER).length != 0);
    }

    /**
	 * Create a map without GPS from the name and path of the map
	 * @param p_path	Absolute path of the map
	 * @return			Map not linked to a GPS
	 */
    public static TomtomMap createMapFromPath(String p_path) {
        if (p_path.isEmpty()) throw new JTomtomException(new IllegalArgumentException());
        File mapDirectory = new File(p_path);
        String[] pnaFileList = mapDirectory.list(PNA_FILE_FILTER);
        if (pnaFileList == null || pnaFileList.length <= 0) throw new JTomtomException("The path '" + p_path + "' is not a Tomtom map directory !", new FileNotFoundException("*.pna"));
        File pnaFile = new File(mapDirectory, pnaFileList[0]);
        Scanner sc = null;
        String version = "";
        try {
            sc = new Scanner(pnaFile);
            sc.nextLine();
            sc.nextLine();
            version = sc.nextLine();
            String build = sc.nextLine();
            version = version.trim() + "." + build.trim().split("=")[1];
        } catch (FileNotFoundException e) {
            throw new JTomtomException("The file " + pnaFile + " does not exists or can not be read !", e);
        } finally {
            try {
                sc.close();
            } catch (Exception e) {
            }
        }
        TomtomMap theMap = new TomtomMap();
        theMap.name = mapDirectory.getName();
        theMap.absolutePath = p_path;
        theMap.version = version;
        return theMap;
    }

    /**
	 * Read or refresh radars informations of the map for a given radar conector
	 * @param radars	RadarConnector
	 */
    public void setRadarsInfos(RadarsConnector radars) {
        radarsInformations = radars.getLocalDbInfos(absolutePath);
    }

    /**
	 * Update radar database with a .ov2 files list
	 * @param files	Files need to be installed
	 * @return
	 */
    public boolean updateRadars(List<File> files, RadarsConnector radars) {
        File mapDirectory = new File(absolutePath);
        if (!mapDirectory.exists()) {
            mapDirectory = new File(absolutePath.toLowerCase());
        }
        if (!mapDirectory.exists()) {
            throw new JTomtomException("org.jtomtom.errors.gps.map.directorynotfound");
        }
        if (!mapDirectory.canWrite()) {
            throw new JTomtomException("org.jtomtom.errors.gps.map.directoryreadonly", mapDirectory.getAbsolutePath());
        }
        for (File current : files) {
            File dest = new File(mapDirectory, current.getName());
            if (JTomTomUtils.copy(current, dest, true)) {
                LOGGER.debug(current.getName() + " copy done.");
            } else {
                throw new JTomtomException("org.jtomtom.errors.gps.radars.installfail", current.getName());
            }
        }
        setRadarsInfos(radars);
        return true;
    }

    /**
	 * Return map name
	 * @return
	 */
    public final String getName() {
        return name;
    }

    /**
	 * Give the map absolute path
	 * @return
	 */
    public final String getPath() {
        return absolutePath;
    }

    /**
	 * Give the map version
	 * @return
	 */
    public final String getVersion() {
        return version;
    }

    /**
	 * Return the radars informations of the map inside POIsDbInfos object
	 * @param radars	Radar connector used from find Radars infos
	 * @return POIsDbInfos
	 */
    public final POIsDbInfos getRadarsInfos(RadarsConnector radars) {
        if (radarsInformations == null) setRadarsInfos(radars);
        return radarsInformations;
    }
}
