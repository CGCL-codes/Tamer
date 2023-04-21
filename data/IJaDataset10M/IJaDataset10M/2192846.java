package org.posper.dngtester.config;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.gogo.commands.Argument;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.posper.dngtester.config.DngConfig;

@Command(scope = "dngtester:config", name = "delete", description = "delete a configuration point")
public class DeleteCommand extends OsgiCommandSupport {

    @Argument(name = "key", description = "the key of the configuration item", required = true)
    String key;

    @Override
    protected Object doExecute() throws Exception {
        DngConfig.getInstance().delete(key);
        return null;
    }
}
