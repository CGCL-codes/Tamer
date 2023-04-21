package net.sourceforge.jaulp.file.search;

import java.io.File;

/**
 * The Class PathFinder.
 */
public class PathFinder {

    /**
     * The Constant SOURCE_FOLDER_SRC_MAIN_RESOURCES keeps the relative
     * path for the source folder 'src/main/resources' in maven projects.
     * */
    public static final String SOURCE_FOLDER_SRC_MAIN_RESOURCES = "src/main/resources";

    /**
     * The Constant SOURCE_FOLDER_SRC_MAIN_JAVA keeps the relative
     * path for the source folder 'src/main/java' in maven projects.
     * */
    public static final String SOURCE_FOLDER_SRC_MAIN_JAVA = "src/main/java";

    /**
     * The Constant SOURCE_FOLDER_SRC_TEST_RESOURCES keeps the relative
     * path for the source folder 'src/test/resources' in maven projects.
     * */
    public static final String SOURCE_FOLDER_SRC_TEST_RESOURCES = "src/test/resources";

    /**
     * The Constant SOURCE_FOLDER_SRC_TEST_JAVA keeps the relative
     * path for the source folder 'src/test/java' in maven projects.
     * */
    public static final String SOURCE_FOLDER_SRC_TEST_JAVA = "src/test/java";

    /**
     * Gets the absolute path.
     *
     * @param file the file
     * @param removeLastChar the remove last char
     * @return the absolute path
     */
    public static String getAbsolutePath(final File file, final boolean removeLastChar) {
        String absolutePath = file.getAbsolutePath();
        if (removeLastChar) {
            absolutePath = absolutePath.substring(0, absolutePath.length() - 1);
        }
        return absolutePath;
    }

    /**
     * Gets the project directory.
     *
     * @param currentDir the current dir
     * @return the project directory
     */
    public static File getProjectDirectory(final File currentDir) {
        final String projectPath = PathFinder.getAbsolutePath(currentDir, true);
        final File projectFile = new File(projectPath);
        return projectFile;
    }

    /**
     * Gets the src main resources dir.
     *
     * @param projectDirectory the project directory
     * @return the src main resources dir
     */
    public static File getSrcMainResourcesDir(final File projectDirectory) {
        return new File(projectDirectory, PathFinder.SOURCE_FOLDER_SRC_MAIN_RESOURCES);
    }

    /**
     * Gets the src main java dir.
     *
     * @param projectDirectory the project directory
     * @return the src main java dir
     */
    public static File getSrcMainJavaDir(final File projectDirectory) {
        return new File(projectDirectory, PathFinder.SOURCE_FOLDER_SRC_MAIN_JAVA);
    }

    /**
     * Gets the src test resources dir.
     *
     * @param projectDirectory the project directory
     * @return the src test resources dir
     */
    public static File getSrcTestResourcesDir(final File projectDirectory) {
        return new File(projectDirectory, PathFinder.SOURCE_FOLDER_SRC_TEST_RESOURCES);
    }

    /**
     * Gets the src test java dir.
     *
     * @param projectDirectory the project directory
     * @return the src test java dir
     */
    public static File getSrcTestJavaDir(final File projectDirectory) {
        return new File(projectDirectory, PathFinder.SOURCE_FOLDER_SRC_TEST_JAVA);
    }
}
