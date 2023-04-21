package com.superscape.anttasks.alienbrain;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

public class BucketSetOnlineTask extends AlienBrainTask {

    public BucketSetOnlineTask() {
    }

    public void setBucketName(String theBucketName) {
        bucketName = Commandline.quoteArgument(theBucketName);
    }

    public void setMountPath(File theMountPath) {
        mountPath = theMountPath;
    }

    protected String getCommand() {
        return command;
    }

    protected void updateCommandline(Commandline cmdline) {
        cmdline.createArgument().setValue(bucketName);
        cmdline.createArgument().setValue("-mountpath");
        cmdline.createArgument().setFile(mountPath);
    }

    protected void verifyParameters() throws BuildException {
        super.verifyParameters();
        if (null == bucketName) throw new BuildException(Strings.TEXT_MISSING_REQUIRED_PARAMETER + "bucketname");
        if (null == mountPath) throw new BuildException(Strings.TEXT_MISSING_REQUIRED_PARAMETER + "mountpath");
    }

    private String bucketName;

    private File mountPath;

    private static final String command = "bucketsetonline";
}
