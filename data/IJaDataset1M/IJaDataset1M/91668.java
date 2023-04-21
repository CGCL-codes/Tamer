package net.sf.lavalamp.application;

import net.sf.lavalamp.site.Console;
import net.sf.lavalamp.LavaLogger;
import net.sf.lavalamp.Pause;

public interface Application {

    void exec(LavaLogger logger, Pause pause) throws Exception;

    void init() throws Exception;

    void setProperties(ApplicationProperties properties);

    void setConsole(Console console);
}
