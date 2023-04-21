package org.mobicents.ant;

import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.mobicents.ant.tasks.ActivateRaEntityTask;
import org.mobicents.ant.tasks.ActivateServiceTask;
import org.mobicents.ant.tasks.ChangeSleeStateTask;
import org.mobicents.ant.tasks.CreateProfileTableTask;
import org.mobicents.ant.tasks.CreateProfileTask;
import org.mobicents.ant.tasks.CreateRaEntityTask;
import org.mobicents.ant.tasks.CreateRaLinkTask;
import org.mobicents.ant.tasks.DeactivateRaEntityTask;
import org.mobicents.ant.tasks.DeactivateServiceTask;
import org.mobicents.ant.tasks.InstallTask;
import org.mobicents.ant.tasks.RemoveProfileTableTask;
import org.mobicents.ant.tasks.RemoveProfileTask;
import org.mobicents.ant.tasks.RemoveRaEntityTask;
import org.mobicents.ant.tasks.RemoveRaLinkTask;
import org.mobicents.ant.tasks.UninstallTask;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public class MobicentsManagementAntTask extends Task {

    public void execute() throws BuildException {
        final ClassLoader origCL = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            try {
                String jndiurl = "jnp://" + host + ":" + jnpPort;
                SleeCommandInterface slee = null;
                slee = new SleeCommandInterface(jndiurl);
                for (int i = 0; i < subTasks.size(); i++) {
                    SubTask task = (SubTask) subTasks.elementAt(i);
                    task.run(slee);
                }
            } catch (BuildException buildEx) {
                throw buildEx;
            } catch (Exception e) {
                throw new BuildException(e);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(origCL);
        }
    }

    public void addChangeSleeState(ChangeSleeStateTask task) {
        subTasks.add(task);
    }

    public void addInstall(InstallTask task) {
        subTasks.add(task);
    }

    public void addUninstall(UninstallTask task) {
        subTasks.add(task);
    }

    public void addActivateService(ActivateServiceTask task) {
        subTasks.add(task);
    }

    public void addDeactivateService(DeactivateServiceTask task) {
        subTasks.add(task);
    }

    public void addCreateRaEntity(CreateRaEntityTask task) {
        subTasks.add(task);
    }

    public void addActivateRaEntity(ActivateRaEntityTask task) {
        subTasks.add(task);
    }

    public void addDeactivateRaEntity(DeactivateRaEntityTask task) {
        subTasks.add(task);
    }

    public void addRemoveRaEntity(RemoveRaEntityTask task) {
        subTasks.add(task);
    }

    public void addBindRaLinkName(CreateRaLinkTask task) {
        subTasks.add(task);
    }

    public void addUnbindRaLinkName(RemoveRaLinkTask task) {
        subTasks.add(task);
    }

    public void addCreateProfileTable(CreateProfileTableTask task) {
        subTasks.add(task);
    }

    public void addRemoveProfileTable(RemoveProfileTableTask task) {
        subTasks.add(task);
    }

    public void addCreateProfile(CreateProfileTask task) {
        subTasks.add(task);
    }

    public void addRemoveProfile(RemoveProfileTask task) {
        subTasks.add(task);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setJnpPort(String jnpPort) {
        this.jnpPort = jnpPort;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user = null;

    private String password = null;

    private String host = "localhost";

    private String jnpPort = "1099";

    private Vector subTasks = new Vector();
}
