package ru.pit.skype.rssfeeder;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import ru.pit.skype.rssfeeder.errors.FeederError;
import ru.pit.skype.rssfeeder.jobsrunner.JobsRunner;
import ru.pit.skype.rssfeeder.modules.Core;
import ru.pit.skype.rssfeeder.modules.Feeder;
import ru.pit.skype.rssfeeder.modules.Listener;
import ru.pit.skype.rssfeeder.modules.Messenger;
import ru.pit.skype.rssfeeder.modules.Storage;
import ru.pit.skype.rssfeeder.parser.CmdParser;

public class SkypeRSSFeeder {

    private static MutablePicoContainer pico = new DefaultPicoContainer();

    public static void main(String[] args) {
        try {
            pico.registerComponentImplementation(Core.class);
            pico.registerComponentImplementation(Listener.class);
            pico.registerComponentImplementation(Messenger.class);
            pico.registerComponentImplementation(Feeder.class);
            pico.registerComponentImplementation(CmdParser.class);
            pico.registerComponentImplementation(Storage.class);
            pico.registerComponentImplementation(JobsRunner.class);
            pico.start();
        } catch (FeederError e) {
            e.printStackTrace();
        }
    }
}
