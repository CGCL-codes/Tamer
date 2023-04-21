package net.sourceforge.x360mediaserve.plugins.jaudiotagger.osgi;

import java.util.Properties;
import net.sourceforge.x360mediaserve.api.formats.Tagger;
import net.sourceforge.x360mediaserve.plugins.jaudiotagger.impl.JAudioTagger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class JAudioTaggerActivator implements BundleActivator {

    JAudioTagger tagger;

    BundleContext context;

    public void start(BundleContext context) throws Exception {
        this.context = context;
        this.tagger = new JAudioTagger();
        context.registerService(Tagger.class.getName(), tagger, new Properties());
    }

    public void stop(BundleContext context) throws Exception {
    }
}
