package net.sf.alc.messaging;

/**
 * @author alain.caron
 */
public class MessageSequencerFilterTest {

    private MessageSequencerFilterTest() {
    }

    public static void main(String args[]) throws Exception {
        final MessageSequencedPipe<String> sequenced = new MessageSequencedPipe<String>();
        final TextFileSource source = new TextFileSource("input.txt");
        final TextFileSink sink = new TextFileSink("output.txt");
        MessagePipes.bind(source, sequenced);
        MessagePipes.bind(sequenced, sink);
        final Thread t = new Thread() {

            public void run() {
                for (; ; ) {
                    try {
                        Thread.sleep(2000L);
                        sequenced.decreasePending();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };
        t.start();
        sink.start();
        sink.waitStopped();
        t.interrupt();
    }
}
