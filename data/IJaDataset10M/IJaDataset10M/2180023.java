package org.sqlexp.preferences.data;

import java.io.File;
import org.sqlexp.util.data.AbstractIdentifierFactory;
import org.sqlexp.util.data.IStringIdentifiable;

/**
 * Driver definition preferences objects.
 * @author Matthieu Réjou
 */
public final class DriverDefinition implements IStringIdentifiable {

    private static AbstractIdentifierFactory identifierFactory = new AbstractIdentifierFactory() {

        @Override
        public IStringIdentifiable getObject(final String identifier) {
            return parseDriver(identifier);
        }

        @Override
        public String getIdentifier(final IStringIdentifiable object) {
            if (object instanceof DriverDefinition) {
                return ((DriverDefinition) object).getIdentifier();
            } else {
                return null;
            }
        }
    };

    /**
	 * Creates a driver definition from the given string representation.
	 * @param string representation of a driver
	 * @return new driver definition object, default one if object could not be created
	 */
    private static DriverDefinition parseDriver(final String string) {
        String[] split = string.split(";");
        if (split.length != 2) {
            return null;
        }
        return new DriverDefinition(split[0], split[1]);
    }

    private String jarFile;

    private String driverClass;

    /**
	 * Constructs a new driver definition.
	 * @param jarFile to set
	 * @param driverClass to set
	 */
    public DriverDefinition(final String jarFile, final String driverClass) {
        setJarFile(jarFile);
        setDriverClass(driverClass);
    }

    /**
	 * Sets the jar file URI.
	 * @return the jarFile
	 */
    public String getJarFile() {
        return jarFile;
    }

    /**
	 * Gets the jar file URI.
	 * @param jarFile the jarFile to set
	 */
    public void setJarFile(final String jarFile) {
        String currentDirectory = new File("").getAbsolutePath();
        String file = new File(jarFile).getAbsolutePath();
        if (file.startsWith(currentDirectory)) {
            this.jarFile = file.substring(currentDirectory.length() + 1);
        } else {
            this.jarFile = jarFile;
        }
    }

    /**
	 * Gets the driver class full name.
	 * @return driver class name
	 */
    public String getDriverClass() {
        return driverClass;
    }

    /**
	 * Sets the driver class full name.
	 * @param driverClass name
	 */
    public void setDriverClass(final String driverClass) {
        this.driverClass = driverClass;
    }

    @Override
    public AbstractIdentifierFactory getFactory() {
        return identifierFactory;
    }

    /**
	 * Creates a string identifier from the receiver.
	 * @return string identifier
	 */
    private String getIdentifier() {
        return getJarFile() + ";" + getDriverClass();
    }
}
