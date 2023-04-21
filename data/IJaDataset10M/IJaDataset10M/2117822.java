package protoj.shell.internal;

import joptsimple.OptionSpec;
import protoj.shell.Command;
import protoj.shell.CommandStore;
import protoj.shell.ProjectLayout;

/**
 * See ConfigureFeature.
 * 
 * @author Ashley Williams
 * 
 */
public final class ConfigureCommand {

    /**
	 * Provides the basic command functionality.
	 */
    private Command delegate;

    private OptionSpec<String> name;

    private OptionSpec<?> undo;

    public ConfigureCommand(BootstrapService service, String mainClass) {
        ProjectLayout layout = service.getLayout();
        CommandStore store = service.getCommandStore();
        StringBuilder description = new StringBuilder();
        description.append("\nConfigures the project by overlaying the contents of a profile directory onto");
        description.append("\nthe project directory. A profile directory can have any content whatsoever,");
        description.append("\nbut usually it will contain a similar directory structure to the project");
        description.append("\nitself and contain xml and property files that should replace those already");
        description.append("\nexisting in the project.");
        description.append("\n");
        description.append("\nAlso uniquely for properties files, any properties will automatically get");
        description.append("\nmerged with those where a a destination properties file already exists, with");
        description.append("\nthe profile properties taking precedence over the project properties. Here is");
        description.append("\nan example of a profile called jonny for a hypothetical developer of the same");
        description.append("\nname:");
        description.append("\n");
        description.append("\n");
        description.append("\n~/dev/myproj/");
        description.append("\n         |");
        description.append("\n         |____bin/                            copy will happen here");
        description.append("\n         |");
        description.append("\n         |____conf/ip-addresses.properties    merge will happen here");
        description.append("\n         |      |");
        description.append("\n         |      |____profile/                 create profiles under here");
        description.append("\n         |            |");
        description.append("\n         |            |____jonny/             an example profile directory");
        description.append("\n         |                  |");
        description.append("\n         |                  |____bin/useful-dev-script.sh");
        description.append("\n         |                  |");
        description.append("\n         |                  |____conf/ip-addresses.properties");
        description.append("\n         |");
        description.append("\n         |...");
        description.append("\n");
        description.append("\n");
        description.append("\nSo jonny has a special script that he likes to use and therefore has placed");
        description.append("\nit in the bin directory under his profile. Also it seems also that the");
        description.append("\nproject needs to know various ip addresses in order to work correctly.");
        description.append("\nTherefore he has placed his inside his conf directory, with the knowledge");
        description.append("\nthat after configuration, his properties will get merged into the properties");
        description.append("\nfile in the project directory and also take precedence.");
        description.append("\n");
        description.append("\nInterpolated properties are also supported, which basically means all ${var}");
        description.append("\nproperty placeholders get resolved during configuration in any text files");
        description.append("\nspecified in a profile. This can be especially useful for the many tools that");
        description.append("\nare unable to interpolate properties at runtime for themselves. It's a good");
        description.append("\nidea to set interpolation permanently on or off since the two modes aren't");
        description.append("\ncompatible. So the decision must be made at the API level at compile time by");
        description.append("\nspecifying true or false in the call to StandardProject.initConfig().");
        description.append("\n");
        description.append("\nThe following options are supported:");
        description.append("\n");
        description.append("\n   1. -name: the name of the profile directory to be applied during configuration");
        description.append("\n");
        description.append("\n   2. -undo: when specified all files in the project hierarchy that match the");
        description.append("\n      files in the profile hierarchy are deleted. Really this is only a partial");
        description.append("\n      undo since no state is saved for the original configure command, so");
        description.append("\n      properties files for example also get deleted rather than unmerged.");
        description.append("\n");
        description.append("\nExample: ");
        description.append(layout.getScriptName());
        description.append(" \"configure -name defaults\"");
        description.append("\n");
        description.append("\nExample: ");
        description.append(layout.getScriptName());
        description.append(" \"configure -name jonny-dev\"");
        description.append("\n");
        description.append("\nExample: ");
        description.append(layout.getScriptName());
        description.append(" \"configure -name clusternode1\"");
        description.append("\n");
        description.append("\nHint: try configuring profiles one after another to build up from the most");
        description.append("\ncommon settings to the most specific settings.");
        delegate = store.addCommand("configure", description.toString(), "16m", mainClass);
        delegate.initAliases("config", "conf");
        name = delegate.getParser().accepts("name").withRequiredArg();
        undo = delegate.getParser().accepts("undo");
    }

    public OptionSpec<String> getName() {
        return name;
    }

    public OptionSpec<?> getUndo() {
        return undo;
    }

    public Command getDelegate() {
        return delegate;
    }
}
