package net.sf.buildbox.strictlogging.listener;

import java.util.List;
import net.sf.buildbox.strictlogging.AbstractStrictLoggerFactory;
import net.sf.buildbox.strictlogging.PrintWriterLoggerFactory;
import net.sf.buildbox.strictlogging.api.Severity;
import net.sf.buildbox.strictlogging.api.StrictLogger;
import org.junit.Assert;
import org.junit.Test;

public class ListeningStrictLoggerFactoryTest {

    @Test
    public void testRecording() {
        final AbstractStrictLoggerFactory origFactory = new PrintWriterLoggerFactory();
        final CumulatingLogListener recorder = new CumulatingLogListener();
        recorder.setMinSeverity(Severity.DEBUG);
        final ListeningStrictLoggerFactory factory = new ListeningStrictLoggerFactory(origFactory);
        factory.addListener(recorder);
        final StrictLogger a = factory.getInstance("a");
        a.debug("factory.getInstance(\"a\")");
        final StrictLogger ab1 = factory.getInstance("a.b");
        final StrictLogger ab2 = a.getSubLogger("b");
        ab1.debug("factory.getInstance(\"a.b\") ... ab1.hashCode=" + ab1.hashCode());
        ab2.trace("a.getSubLogger(\"b\")        ... ab2.hashCode=" + ab2.hashCode());
        Assert.assertTrue(ab1 == ab2);
        final StrictLogger abcde1 = factory.getInstance("a.b.c.d.e");
        final StrictLogger abcde2 = a.getSubLogger("b").getSubLogger("c").getSubLogger("d").getSubLogger("e");
        abcde1.debug("factory.getInstance(\"a.b.c.d.e\") ... abcde1.hashCode=" + abcde1.hashCode());
        abcde2.trace("a.b.c.d.getSubLogger(\"e\")        ... abcde2.hashCode=" + abcde2.hashCode());
        Assert.assertTrue(abcde1 == abcde2);
        for (Thread thread : recorder.getThreads()) {
            System.out.println("thread = " + thread);
            final List<StrictLogEvent> events = recorder.getRecords(thread);
            for (StrictLogEvent event : events) {
                System.out.println(" ... " + event);
            }
        }
    }
}
