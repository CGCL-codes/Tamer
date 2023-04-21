package ar.com.coonocer.CodingJoy.utils;

import java.io.File;
import ar.com.coonocer.framework.guid.SimpleLocalIDGenerator;

public class FileUtils {

    public static final String TEMPLATES_FOLDER = "templates";

    public static final String APPLICATION_FOLDER = "application";

    public static final String PROJECTS_FOLDER = "savedProjects";

    public static final String LIBRARIES_FOLDER = "j2ee-libraries";

    public static final String TEMP_FOLDER = "tempFiles";

    public static String getAbsolutePath(String s) {
        return new File(".").getAbsolutePath() + "/../../" + s;
    }

    public static String getTempFolder() {
        return getAbsolutePath(TEMP_FOLDER + "/" + SimpleLocalIDGenerator.getNewId());
    }

    public static String getCodingJoyWebAppPath() {
        return "webapps/CodingJoyWebApp";
    }
}
