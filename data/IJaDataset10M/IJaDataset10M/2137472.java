package as.ide.core.common;

import java.util.ArrayList;
import org.eclipse.core.resources.IProject;

public class ASProjectConfiguration {

    public static final String CFG_FILE_NAME = "configuration.xml";

    private ArrayList<String> pathElmentsList = new ArrayList<String>();

    private String srcFolderPath;

    private String outputFilePath;

    private String dftBuildFilePath;

    private String default_color;

    private int default_frame_rate;

    private int playerVersion;

    private int width;

    private int height;

    public ASProjectConfiguration(String[] pathElements, String srcFolderPath, String dftFile, String outputFile, String dftColor, int version, int frameRate, int width, int height) {
        this.dftBuildFilePath = dftFile;
        this.outputFilePath = outputFile;
        this.srcFolderPath = srcFolderPath;
        this.default_color = dftColor;
        this.playerVersion = version;
        this.default_frame_rate = frameRate;
        this.width = width;
        this.height = height;
        if (pathElements != null) {
            for (String element : pathElements) {
                pathElmentsList.add(element);
            }
        }
    }

    ASProjectConfiguration(IProject project) {
        new ConfigurationLoader().load(this, project);
    }

    public String getSourceFolderPath() {
        return srcFolderPath;
    }

    public void setSourceFolderPath(String path) {
        srcFolderPath = path;
    }

    public String getDefaultBuildFilePath() {
        return dftBuildFilePath;
    }

    public void setDefaultBuildFilePath(String filePath) {
        this.dftBuildFilePath = filePath;
    }

    public int getPlayerVersion() {
        return playerVersion;
    }

    public void setPlayerVersion(int playerVersion) {
        this.playerVersion = playerVersion;
    }

    public String[] getPathElements() {
        return pathElmentsList.toArray(new String[pathElmentsList.size()]);
    }

    public void addPathElement(String pathElement) {
        pathElmentsList.add(pathElement);
    }

    public String getDefaultColor() {
        return default_color;
    }

    public void setDefaultColor(String defaultColor) {
        default_color = defaultColor;
    }

    public int getFrameRate() {
        return default_frame_rate;
    }

    public void setFrameRate(int rate) {
        default_frame_rate = rate;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setPathElements(String[] sourcePaths) {
        pathElmentsList.clear();
        for (String s : sourcePaths) {
            this.pathElmentsList.add(s);
        }
    }

    public void setOutputFilePath(String filePath) {
        this.outputFilePath = filePath;
    }

    /**
	 * The output file, commonly = project name + ".swf"
	 * @return
	 */
    public String getOutputFilePath() {
        return this.outputFilePath;
    }
}
