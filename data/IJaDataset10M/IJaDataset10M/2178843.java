package app.sgf;

import gtp.GtpEngine;

public interface GtpTerminal {

    public void append(GtpEngine engine, String s);

    public void appendError(GtpEngine engine, String s);

    public void closed(GtpEngine engine);
}
