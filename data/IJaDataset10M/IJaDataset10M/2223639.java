package sheep.model;

import java.awt.Image;
import sheep.controller.Workspace;

/**
 *
 * @author geek
 */
public class Project extends Task {

    private Image pictures;

    private String picturePath;

    private String projectPath;

    public Project(String name, Workspace workSpace) {
        super(name, workSpace);
    }

    public Image getImage() {
        return getPictures();
    }

    public void setImage(Image pictures) {
        this.setPictures(pictures);
    }

    public Image getPictures() {
        return pictures;
    }

    public void setPictures(Image pictures) {
        this.pictures = pictures;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
