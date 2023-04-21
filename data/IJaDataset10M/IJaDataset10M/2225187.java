package uk.co.brunella.osgi.bdt.ant.tasks;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import uk.co.brunella.osgi.bdt.repository.Deployer;
import uk.co.brunella.osgi.bdt.repository.profile.Profile;

public class OSGiCreate extends AbstractOSGiTask {

    private File repository;

    private boolean verbose = false;

    private String profileName = "OSGi/Minimum-1.0";

    public OSGiCreate() {
        setTaskName("osgi-create");
        setDescription("Creates a new bundle repository");
    }

    public void execute() {
        Deployer deployer = new Deployer(repository);
        deployer.setVerbose(verbose);
        try {
            deployer.create(profileName);
            log(deployer.getLogMessages());
        } catch (IOException e) {
            throw new BuildException("Creation of repository failed: " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
            throw new BuildException(t.getMessage());
        }
    }

    public void setRepository(File repository) {
        this.repository = repository;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setProfileName(String profileName) {
        if (!Profile.isValidProfileName(profileName)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid profile name: ").append(profileName).append('\n');
            sb.append("Allowed are:\n");
            for (String name : Profile.getProfileNameList()) {
                sb.append("  ").append(name).append("\n");
            }
            throw new BuildException(sb.toString());
        }
        this.profileName = profileName;
    }
}
