package vgrazi.concurrent.samples.examples;

import vgrazi.concurrent.samples.ConcurrentExampleConstants;
import java.awt.*;
import java.util.concurrent.Executors;

public class CachedThreadExecutorExample extends ExecutorsExample {

    /**
   * Each example must have a unique slide show index (or -1). Indexes must start with 0 and must be in sequence, no skipping
   *
   * @param label          the label to display at the top of the
   * @param frame          the launcher frame to display the example
   * @param slideShowIndex when configured as a slide show, this indicates the slide number. -1 for exclude from slide show - will still show in menu bar
   */
    public CachedThreadExecutorExample(String label, Container frame, int slideShowIndex) {
        super(label, frame, slideShowIndex);
    }

    protected void initializeComponents() {
        if (!initialized) {
            initializeExecuteButton();
            initializeThreadCountField(threadCountField);
            initialized = true;
        }
        reset();
    }

    @Override
    public void reset() {
        if (executor != null) {
            executor.shutdownNow();
        }
        initializeThreadPool();
        setState(2);
        resetThreadCountField(threadCountField);
        nextIndex = 1;
        message1(" ", ConcurrentExampleConstants.MESSAGE_COLOR);
        message2(" ", ConcurrentExampleConstants.MESSAGE_COLOR);
        super.reset();
    }

    @Override
    protected void initializeThreadPool() {
        executor = Executors.newCachedThreadPool();
    }

    @Override
    protected void setDefaultState() {
        sleepTime = 1000;
        setState(2);
    }

    protected String getSnippet() {
        String snippet = "<html><PRE><FONT style=\"font-family:monospaced;\" COLOR=\"#000000\"> \n" + "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"" + ConcurrentExampleConstants.HTML_DISABLED_COLOR + "\"><I>// CachedThreadPool Construction</I></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"#000000\"> \n" + "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state2:#000080>\"><B>final</B></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state2:#000000>\"> Executor executor = </FONT>\n" + "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state2:#000080>\"><FONT style=\"font-family:monospaced;\" COLOR=\"<state2:#000000>\">     Executors.newCachedThreadPool(); \n" + " \n" + "    </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"" + ConcurrentExampleConstants.HTML_DISABLED_COLOR + "\"><I>// Use the Executor to launch some Runnable </I></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state3:#000000>\"> \n" + "    executor.execute(</FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state3:#000080>\"><B>new</B></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state3:#000000>\"> Runnable(){ \n" + "        </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state3:#000080>\"><B>public</B></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state3:#000000>\"> </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state3:#000080>\"><B>void</B></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state3:#000000>\"> run(){ \n" + "          </FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state3:" + ConcurrentExampleConstants.HTML_DISABLED_COLOR + ">\"><I>// do work</I></FONT><FONT style=\"font-family:monospaced;\" COLOR=\"<state3:#000000>\"> \n" + "        }}); \n";
        return snippet;
    }
}
