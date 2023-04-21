package org.dicom4j.apps.risserver;

import org.dicom4j.apps.imageserver.Starter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Launcher {

    private static final long serialVersionUID = 1L;

    public static String PROPERTY_FILE_NAME = "./risserver.properties";

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Starter lApp = new Starter();
        if (!lApp.start(PROPERTY_FILE_NAME)) {
            System.exit(1);
        }
    }
}
