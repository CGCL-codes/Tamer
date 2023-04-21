package net.sf.dz2.test.dispatcher;

import net.sf.dz2.logger.impl.udp.xap.XapLogger;
import net.sf.dz2.logger.impl.udp.xpl.XplLogger;
import net.sf.dz2.signal.impl.SimpleDataDispatcher;
import net.sf.dz2.signal.model.DataDispatcher;
import net.sf.dz2.signal.model.DataSample;
import net.sf.dz2.signal.model.DataSink;
import net.sf.jukebox.conf.Configurable;
import net.sf.jukebox.conf.Configuration;
import net.sf.jukebox.conf.TextConfiguration;
import net.sf.jukebox.service.ActiveService;
import net.sf.jukebox.service.Service;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DispatcherTest extends ActiveService {

    private DataDispatcher<Number> dispatcher = new SimpleDataDispatcher<Number>();

    private Set<? extends DataSink<Number>> sinkSet = new HashSet<DataSink<Number>>();

    private Random rg = new Random();

    protected void startup() throws Throwable {
        Configuration cf = new TextConfiguration();
        dispatcher.add(init(new XapLogger<Number>(), cf), null);
        dispatcher.add(init(new XplLogger<Number>(), cf), null);
    }

    private DataSink<Number> init(DataSink<Number> ds, Configuration cf) throws InterruptedException {
        ((Configurable) ds).configure("dz", cf);
        if (!((Service) ds).start().waitFor()) {
            throw new Error("Can't start the data sink");
        }
        return ds;
    }

    protected void execute() throws Throwable {
        for (int i = 0; i < 100; i++) {
            dispatcher.consume(new DataSample<Number>("source", "sig", new Double(rg.nextDouble()), null));
        }
    }

    protected void shutdown() {
    }
}
