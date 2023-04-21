package net.sf.mavensynapseplugin.util;

public interface SynapseServerController {

    void start(SynapseConfiguration synapseConfiguration);

    void stop();
}
