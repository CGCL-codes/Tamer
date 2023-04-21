package org.rascalli.util.felix.shell.commands.configadmin;

import java.io.IOException;
import java.io.PrintStream;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.shell.Command;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

@Component(architecture = true, immediate = true)
@Provides
public class ListConfiguration implements Command {

    @Requires
    ConfigurationAdmin pm;

    public void execute(String line, PrintStream out, PrintStream err) {
        Configuration[] confs = null;
        try {
            confs = pm.listConfigurations(null);
            for (int i = 0; i < confs.length; i++) {
                out.println(confs[i].getPid() + " : " + confs[i].getProperties());
            }
        } catch (IOException e) {
            err.println("An error occurs when listing configurations : " + e.getMessage());
        } catch (InvalidSyntaxException e) {
            err.println("An error occurs when listing configurations : " + e.getMessage());
        }
    }

    public String getName() {
        return "listConf";
    }

    public String getShortDescription() {
        return "Command list configurations from the config admin";
    }

    public String getUsage() {
        return getName();
    }
}
