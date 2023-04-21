package net.jxta.impl.rendezvous.rendezvousMeter;

import net.jxta.impl.meter.*;

public interface RendezvousMeterBuildSettings extends MeterBuildSettings {

    public static final boolean RENDEZVOUS_METERING = ConditionalRendezvousMeterBuildSettings.isRuntimeMetering();
}
