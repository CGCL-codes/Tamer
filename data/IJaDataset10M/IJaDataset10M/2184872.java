package net.sourceforge.yagsbook.encyclopedia.ant;

import net.sourceforge.yagsbook.encyclopedia.Encyclopedia;
import net.sourceforge.yagsbook.encyclopedia.articles.FileSystemSource;
import org.apache.tools.ant.*;
import java.io.File;
import java.util.*;

/**
 * This is an Ant task for providing an interface to the generation of
 * encyclopedias. All the options can be set up from within the ant
 * build.xml file for an encyclopedia. This should remove the need for
 * platform specific shell scripts etc.
 */
public class EncyclopediaTask extends Task {

    private String destination = null;

    private String configPath = null;

    private String mapDir = "maps";

    private String imageDir = "images";

    private Vector sources = new Vector();

    private Properties properties = new Properties();

    /**
     * The destination directory where the encyclopedia will be generated.
     * All HTML and image files will be located beneath this level.
     */
    public void setDestination(String destination) {
        System.out.println("Destination = " + destination);
        this.destination = destination;
    }

    public void setConfig(String configPath) {
        System.out.println("Configuration = " + configPath);
        this.configPath = configPath;
    }

    /**
     * Set the directory where generated maps will be located.
     * This defaults to "maps" if not set.
     */
    public void setMapdir(String mapDir) {
        this.mapDir = mapDir;
    }

    /**
     * Set the directory where images will be located. This defaults
     * to "images" if not set.
     */
    public void setImagedir(String imageDir) {
        this.imageDir = imageDir;
    }

    public void addConfiguredRepository(RepositoryTask repository) {
        sources.add(repository.getSrc());
    }

    public void addConfiguredProperty(PropertyTask property) {
        properties.setProperty(property.getName(), property.getValue());
    }

    public void execute() throws BuildException {
        Iterator iter = sources.iterator();
        try {
            Encyclopedia e = new Encyclopedia(new File(destination));
            while (iter.hasNext()) {
                String src = (String) iter.next();
                e.add(new FileSystemSource(new File(src)));
            }
            e.setProperties(properties);
            e.setConfiguration(new File(configPath));
            e.build();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BuildException("Failed to build encyclopedia");
        }
    }
}
