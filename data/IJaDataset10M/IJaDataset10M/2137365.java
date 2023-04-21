package net.sf.doolin.app.sc.server.cli;

import net.sf.doolin.cli.CLIService;
import net.sf.doolin.cli.command.AbstractCLICommand;
import net.sf.doolin.cli.command.CLIConfig;
import net.sf.doolin.util.Strings;
import org.apache.commons.cli.CommandLine;

public class VersionCommand extends AbstractCLICommand<CLIConfig> {

    public VersionCommand() {
        super("version", "Displays version information");
    }

    @Override
    public void run(CLIService service, CLIConfig config) {
        String version = Strings.get(null, "App.version");
        String buildNumber = Strings.get(null, "App.buildNumber");
        String svnRevision = Strings.get(null, "App.svnRevision");
        service.format("Version: %s%nBuild number: %s%nRevision: %s%n", version, buildNumber, svnRevision);
    }

    @Override
    protected CLIConfig createConfig(CommandLine commandLine) {
        return new CLIConfig();
    }
}
