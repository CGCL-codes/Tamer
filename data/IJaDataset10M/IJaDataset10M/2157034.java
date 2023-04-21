package net.sf.spooler;

import org.apache.log4j.Logger;

public interface Monitor {

    void stop();

    Logger getLog();
}
