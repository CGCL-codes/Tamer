package net.raymanoz.command;

import net.raymanoz.config.Configuration;
import net.raymanoz.io.File;
import net.raymanoz.migrate.ScriptCreatorImpl;
import net.raymanoz.migrate.ScriptCreatorAssemblerImpl;
import net.raymanoz.migrate.ScriptList;
import net.raymanoz.migrate.ScriptListImpl;
import net.raymanoz.util.PlainProperties;
import net.raymanoz.util.Properties;

public class CommandAssemblerImpl implements CommandAssembler {

    private Configuration configuration;

    public CommandAssemblerImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    public ScriptCreatorImpl newScriptCreator(Properties properties) {
        return new ScriptCreatorImpl(new ScriptCreatorAssemblerImpl(configuration), configuration, properties);
    }

    public ScriptList buildScriptList(long dbVersion) {
        return newScriptList(configuration.getScriptDirectory(dbVersion).listFiles(), dbVersion);
    }

    public ScriptListImpl newScriptList(File[] files, long dbVersion) {
        return new ScriptListImpl(files, 1, dbVersion, configuration);
    }

    public Properties newProperties() {
        return new PlainProperties();
    }
}
