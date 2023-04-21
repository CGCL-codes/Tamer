package org.xiaoniu.suafe;

/**
 * Constant values used throughout the application.
 * 
 * @author Shaun Johnson
 */
public final class Constants {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static final String MIME_HTML = "text/html";

    public static final String MIME_TEXT = "text/plain";

    public static final String PATH_BASE_RESOURCE_DIR = "org/xiaoniu/suafe/resources";

    public static final String PATH_RESOURCE_BUNDLE = PATH_BASE_RESOURCE_DIR + "/Resources";

    public static final String PATH_RESOURCE_DIR_FULL = "/" + PATH_BASE_RESOURCE_DIR;

    public static final String PATH_RESOURCE_HELP_DIR = PATH_BASE_RESOURCE_DIR + "/help";

    public static final String PATH_RESOURCE_IMAGE_DIR = PATH_RESOURCE_DIR_FULL + "/images";

    public static final String TEXT_NEW_LINE = System.getProperty("line.separator");

    private Constants() {
        super();
    }
}
