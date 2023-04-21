package buildinvoker;

import java.io.File;
import jetbrains.buildServer.util.StringUtil;

public class Artifact {

    String FullName;

    String SizeHumanReadable;

    public Artifact(File file, File startingDir) {
        this.FullName = file.getPath().replace(startingDir.getPath(), "").replace("\\", "/");
        if (this.FullName.startsWith("/")) this.FullName = this.FullName.substring(1);
        this.SizeHumanReadable = StringUtil.formatFileSize(file.length());
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getSizeHumanReadable() {
        return SizeHumanReadable;
    }

    public void setSizeHumanReadable(String sizeHumanReadable) {
        SizeHumanReadable = sizeHumanReadable;
    }
}
