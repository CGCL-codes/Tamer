package com.googlecode.lazifier.project;

import org.kohsuke.args4j.Option;
import com.googlecode.lazifier.command.Options;

/**
 * The ProjectOptions class. This class is to hold parameters from command line for Projector. 
 * 
 * @author Donny A. Wijaya
 * @version 0.0.9
 * @since April 2010
 */
public class ProjectOptions extends Options {

    @Option(name = "-name", metaVar = "VALUE", required = false, usage = "The project's name.")
    private String name = "";

    @Option(name = "-database", metaVar = "VALUE", required = false, usage = "The project's database.\nUse '-database list' to list the available databases")
    private String database = "";

    @Option(name = "-package", metaVar = "VALUE", required = true, usage = "The project's main/root package")
    private String _package = "";

    /**
	 * Get the name
	 * 
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * Get the database
	 * 
	 * @return String
	 */
    public String getDatabase() {
        return database;
    }

    /**
	 * Set the database
	 * 
	 * @param database
	 */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
	 * Get the package
	 * 
	 * @return String
	 */
    public String getPackage() {
        return _package;
    }

    /**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
    public String toString() {
        final String TAB = "    ";
        StringBuilder retValue = new StringBuilder();
        retValue.append("ConfigureOptions ( ").append(super.toString()).append(TAB).append("type = ").append(this.getType()).append(TAB).append("_package = ").append(this._package).append(TAB).append("database = ").append(this.database).append(TAB).append(" )");
        return retValue.toString();
    }
}
