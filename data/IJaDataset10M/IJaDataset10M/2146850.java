package ch.oxinia.webdav.davcommander;

import java.util.Vector;

/**
 * This is a small container for URIs that are used by the user.
 * The URIs are persisted in the DAVExplorer.dat file in java.home.
 *
 * Copyright:   Copyright (c) 2003 Thoralf Rickert
 * Copyright:   Copyright (c) 2003-2004 Regents of the University of California. All rights reserved.
 * @author      Thoralf Rickert
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @version     0.1
 * date         07 April 2003
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * date         08 February 2004
 * Changes:     Added Javadoc templates
 */
public class URIContainer {

    protected static URIContainer instance = null;

    protected Vector<String> uris = new Vector<String>();

    protected URIContainer() {
        loadURIs();
    }

    public static URIContainer getInstance() {
        if (instance == null) {
            instance = new URIContainer();
        }
        return instance;
    }

    public void loadURIs() {
        Vector<String> data = GlobalData.getGlobalData().readConfigEntries("uri");
        for (int i = 0; i < data.size(); i++) {
            addURI(data.elementAt(i));
        }
    }

    public void saveURIs() {
        GlobalData.getGlobalData().writeConfigEntries("uri", uris);
    }

    public void addAndSaveURI(String uri) {
        if (addURI(uri)) {
            GlobalData.getGlobalData().writeConfigEntry("uri", uri, false);
        }
    }

    public boolean addURI(String uri) {
        if (uri == null || uri.length() == 0 || uris.contains(uri)) {
            return false;
        }
        uris.addElement(uri);
        return true;
    }

    public Vector<String> getURIs() {
        return uris;
    }

    public void removeAllURIs() {
        uris.removeAllElements();
    }
}
