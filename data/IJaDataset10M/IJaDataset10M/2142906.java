package org.arastreju.tools.nodespawn;

import java.io.File;

/**
 * Creator of arastreju nodes.
 * 
 * Created: 10.03.2009
 * 
 * @author Oliver Tigges
 */
public class NodeCreator {

    public static final String PROPERTY_DB_TYPE = "nodecreator.db-type";

    public static final String PROPERTY_DB_NAME = "nodecreator.db-name";

    public NodeCreator(final File nodeDefDir) {
    }

    public NodeCreator(final File nodeDefDir, final String profile) {
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected exactly one argument: path to nodedef directory");
        }
        String path = args[0];
        File defDir = new File(path);
        if (!defDir.isDirectory()) {
            throw new IllegalArgumentException("given path '" + path + "' doesn't specify existing directory");
        }
        new NodeCreator(defDir);
    }
}
