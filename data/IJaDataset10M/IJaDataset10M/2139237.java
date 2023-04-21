package org.sqlexp.preferences;

/**
 * Tree preference object.
 * @author Matthieu Réjou
 */
public final class TreesPreference extends Preference {

    private static final boolean DEFAULT_LINK_WITH_EDITOR = false;

    private static final String DEFAULT_DEFAULTSERVER = null;

    private static final String DEFAULT_DEFAULTDATABASE = null;

    private static TreesPreference instance;

    /**
	 * Gets the tree preference instance.
	 * @return unique instance
	 */
    public static TreesPreference getInstance() {
        if (instance == null) {
            instance = new TreesPreference();
        }
        return instance;
    }

    private boolean linkWithEditor;

    private String defaultServer;

    private String defaultDatabase;

    @Override
    public void loadDefaults() {
        linkWithEditor = DEFAULT_LINK_WITH_EDITOR;
        defaultServer = DEFAULT_DEFAULTSERVER;
        defaultDatabase = DEFAULT_DEFAULTDATABASE;
    }

    /**
	 * Gets if editor focus should change tree selection.
	 * @return true if so, false otherwise
	 */
    public boolean isLinkWithEditor() {
        return linkWithEditor;
    }

    /**
	 * Sets if editor focus should change tree selection.
	 * @param linkWithEditor true if so, false otherwise
	 */
    public void setLinkWithEditor(final boolean linkWithEditor) {
        this.linkWithEditor = linkWithEditor;
    }

    /**
	 * Gets the default SQL server.
	 * @return the default server internal name ("/group/server/")
	 */
    public String getDefaultServer() {
        return defaultServer;
    }

    /**
	 * Sets the default SQL server.
	 * @param defaultServer internal name ("/group/server/")
	 */
    public void setDefaultServer(final String defaultServer) {
        this.defaultServer = defaultServer;
    }

    /**
	 * Gets the default SQL database (ignored if default server is not set).
	 * @return the default database name
	 */
    public String getDefaultDatabase() {
        return defaultDatabase;
    }

    /**
	 * Sets the default SQL database (ignored if default server is not set).
	 * @param defaultDatabase to set
	 */
    public void setDefaultDatabase(final String defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
    }
}
