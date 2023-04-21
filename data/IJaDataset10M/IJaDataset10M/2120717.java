package net.sourceforge.jcoupling.wca;

import java.util.Set;
import java.util.HashSet;

/**
 * Allows model to express different capbilities of divergent forms of commuincation
 * technology.  i.e. JMS, email, sms e.t.c.
 * @author Lachlan Aldred
 */
public class TechnologyMetadata {

    public static final TechnologyMetadata JMSTechnologyMetaData = new TechnologyMetadata(false, false, TimeCoupling.Decoupled, SpaceCoupling.SendCompeteReceive, true);

    static Set<SpaceCoupling> scouplings;

    static Set<TimeCoupling> tcouplings;

    static {
        scouplings = new HashSet<SpaceCoupling>();
        scouplings.add(SpaceCoupling.SendReceive);
        scouplings.add(SpaceCoupling.PubSub);
        scouplings.add(SpaceCoupling.SendCompeteReceive);
        tcouplings = new HashSet<TimeCoupling>();
        tcouplings.add(TimeCoupling.Coupled);
        tcouplings.add(TimeCoupling.Decoupled);
    }

    public static final TechnologyMetadata MemoryTechnologyMetaData = new TechnologyMetadata(true, true, tcouplings, scouplings, true);

    public TechnologyMetadata(boolean supportsInvoke, boolean reportsRemoteFaults, TimeCoupling supportedTimeCoupling, SpaceCoupling supportedSpaceCoupling, boolean supportsUniDirMsging) {
        this.supportsInvoke = supportsInvoke;
        this.reportsRemoteFaults = reportsRemoteFaults;
        this.supportedTimeCouplings = new HashSet<TimeCoupling>();
        this.supportedTimeCouplings.add(supportedTimeCoupling);
        this.supportedSpaceCouplings = new HashSet<SpaceCoupling>();
        this.supportedSpaceCouplings.add(supportedSpaceCoupling);
        this.supportsUniDirMsging = supportsUniDirMsging;
        if (reportsRemoteFaults && !supportsInvoke) {
            throw new IllegalArgumentException("Reporting remote faults is only possible if invoke is supported.");
        }
    }

    public TechnologyMetadata(boolean supportsInvoke, boolean reportsRemoteFaults, Set<TimeCoupling> tcouplings, Set<SpaceCoupling> scouplings, boolean supportsUniDirMsging) {
        this.supportsInvoke = supportsInvoke;
        this.reportsRemoteFaults = reportsRemoteFaults;
        this.supportedTimeCouplings = tcouplings;
        this.supportedSpaceCouplings = scouplings;
        this.supportsUniDirMsging = supportsUniDirMsging;
        if (reportsRemoteFaults && !supportsInvoke) {
            throw new IllegalArgumentException("Reporting remote faults is only possible if invoke is supported.");
        }
    }

    private boolean supportsInvoke;

    private boolean reportsRemoteFaults;

    private Set<TimeCoupling> supportedTimeCouplings;

    private Set<SpaceCoupling> supportedSpaceCouplings;

    private boolean supportsUniDirMsging;

    public boolean isSupportsInvoke() {
        return supportsInvoke;
    }

    public boolean supportsUniDirMsging() {
        return this.supportsUniDirMsging;
    }

    public boolean isReportsRemoteFaults() {
        return reportsRemoteFaults;
    }

    public Set<TimeCoupling> getSupportedTimeCouplings() {
        return supportedTimeCouplings;
    }

    public Set<SpaceCoupling> getSupportedSpaceCouplings() {
        return supportedSpaceCouplings;
    }
}
