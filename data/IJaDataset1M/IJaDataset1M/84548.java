package org.modss.facilitator.shared.init.commandline;

/**
 *
 */
public interface ArgHandler {

    public boolean wantIt(String arg);

    public void process(String arg, ArgSource argSource) throws CommandLineParseException;

    public boolean stop();

    public String[] usage();
}
