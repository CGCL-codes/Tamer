package org.posper.dngtester.config;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.gogo.commands.Argument;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.posper.dngtester.config.DngConfig;

@Command(scope = "dngtester:config", name = "update", description = "add/update a configuration point")
public class UpdateCommand extends OsgiCommandSupport {

    @Argument(name = "key", description = "the key of the configuration item", required = true)
    String key;

    @Argument(name = "value", description = "value to set the configuration item to", index = 1, required = true)
    String value;

    @Override
    protected Object doExecute() throws Exception {
        DngConfig.getInstance().update(key, value);
        return null;
    }
}
