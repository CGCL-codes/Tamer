package com.superscape.anttasks.alienbrain;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

public class SetPropertyTask extends AlienBrainTask {

    public SetPropertyTask() {
    }

    public void setPath(String thePath) {
        path = Commandline.quoteArgument(thePath);
    }

    public void setName(String theName) {
        name = theName;
    }

    public void setValue(String theValue) {
        value = theValue;
    }

    public void setHandle(String theHandle) {
        handle = theHandle;
    }

    protected String getCommand() {
        return command;
    }

    protected void updateCommandline(Commandline cmdline) {
        if (null != path) cmdline.createArgument().setValue(path);
        cmdline.createArgument().setValue("-name");
        cmdline.createArgument().setValue(name);
        cmdline.createArgument().setValue("-value");
        cmdline.createArgument().setValue(value);
        if (null != handle) {
            cmdline.createArgument().setValue("-handle");
            cmdline.createArgument().setValue(handle);
        }
    }

    protected void verifyParameters() throws BuildException {
        super.verifyParameters();
        if (null == name) throw new BuildException(Strings.TEXT_MISSING_REQUIRED_PARAMETER + "name");
        if (null == value) throw new BuildException(Strings.TEXT_MISSING_REQUIRED_PARAMETER + "value");
        if (null == path && null == handle) throw new BuildException(Strings.TEXT_MISSING_ALTERNATIVE_PARAMETER + "path, handle");
        if (null != path && null != handle) throw new BuildException(Strings.TEXT_MULTIPLE_ALTERNATIVE_PARAMETERS + "path, handle");
    }

    private String path;

    private String name;

    private String value;

    private String handle;

    private static final String command = "setproperty";
}
