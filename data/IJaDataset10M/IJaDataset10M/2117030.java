package de.jrummler.xtm.helpclasses;

import java.io.File;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import org.tmapi.core.*;
import org.tmapi.index.*;
import org.tinytim.mio.TopicMapWriter;
import org.tinytim.mio.XTM10TopicMapWriter;
import org.tinytim.mio.TopicMapReader;
import org.tinytim.mio.XTMTopicMapReader;
import org.apache.log4j.Logger;

/**
 * @author - Jens Rummler
 * @version $Rev: 4 $ - $Date: 2009-02-24 07:41:39 -0500 (Tue, 24 Feb 2009) $
 */
public class TinyTIMConnector {

    private static final Logger LOG = Logger.getLogger(TinyTIMConnector.class);

    private String session = null;

    private String searchTerm = null;

    public TinyTIMConnector(String searchTerm, String session) {
        this.session = session;
        this.searchTerm = searchTerm;
    }

    public OutputStream run(OutputStream out) {
        try {
            TopicMapSystemFactory tmSysFactory = TopicMapSystemFactory.newInstance();
            TopicMapSystem tmSys = tmSysFactory.newTopicMapSystem();
            TopicMap tm = tmSys.createTopicMap("test");
            File source = new File("./" + session + ".xtm");
            TopicMapReader reader = new XTMTopicMapReader(tm, source);
            reader.read();
            LiteralIndex litIdx = tm.getIndex(LiteralIndex.class);
            if (!litIdx.isAutoUpdated()) {
                litIdx.reindex();
            }
            Collection<Name> names = litIdx.getNames(searchTerm);
            Set<Topic> topic = new HashSet();
            for (Name n : names) {
                topic.add(n.getParent());
            }
            TopicMapSystemFactory tmSysFactory2 = TopicMapSystemFactory.newInstance();
            TopicMapSystem tmSys2 = tmSysFactory2.newTopicMapSystem();
            TopicMap tmOut = tmSys2.createTopicMap("");
            org.tinytim.internal.utils.CopyUtils.copy(tm, tmOut, topic);
            TopicMapWriter writer = new XTM10TopicMapWriter(out, "http://tinytim.sourceforge.net/examplemap.ctm");
            writer.write(tmOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }
}
