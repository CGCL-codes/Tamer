package ch.olsen.servicecontainer.internalservice.persistence;

import ch.olsen.products.util.configuration.Configuration;
import ch.olsen.products.util.configuration.IntegerProperty;
import ch.olsen.products.util.configuration.StringProperty;

public class DB4OConfiguration extends Configuration<DB4OConfiguration> {

    private static final long serialVersionUID = 1L;

    public IntegerProperty startport;

    public IntegerProperty endport;

    public StringProperty username;

    public StringProperty password;

    public DB4OConfiguration() {
        startport = new IntegerProperty("startport", "Port range start for all db4o servers", 4441, 1, 65535, false);
        endport = new IntegerProperty("endport", "Port range end for all db4o servers", 5441, 1, 65535, false);
        username = new StringProperty("username", "User name for DB4O", "user", false);
        password = new StringProperty("password", "Password for DB4O", "password", false);
    }

    public void clear() {
    }

    public String getDescription() {
        return "Persistence configuration (DB4O)";
    }

    public String getName() {
        return "persistence";
    }
}
