package de.sonivis.tool.mediawikiconnector;

public class MediaWikiRDBConnection {

    /***************************************************************************
	 * Attributes
	 **************************************************************************/
    private static final MediaWikiRDBConnection INSTANCE = new MediaWikiRDBConnection();

    /***************************************************************************
	 * Constructors
	 **************************************************************************/
    public MediaWikiRDBConnection() {
    }

    public static synchronized MediaWikiRDBConnection getInstance() {
        return INSTANCE;
    }
}
