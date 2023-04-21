package net.sf.agentopia.platform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import junit.framework.JUnit4TestAdapter;
import net.sf.agentopia.core.AgentopiaConstants;
import net.sf.agentopia.util.Config;
import net.sf.agentopia.util.Logger;
import net.sf.agentopia.util.Logger.IAgentopiaLogger;
import net.sf.agentopia.util.net.HostId;
import org.junit.Test;

/**
 * Tests the market place (without any networks).
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since 2008
 */
public class MarketPlaceTest {

    /**
     * @return A suite with this class.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MarketPlaceTest.class);
    }

    /**
     * @throws Exception If something failed.
     */
    @Test
    public void testMarketPlace() throws Exception {
        MockupEnvironment env = new MockupEnvironment();
        final IMarketPlace market1 = env.getMarket1();
        final IMarketPlace market2 = env.getMarket2();
        assertNotNull(market1);
        assertNotNull(market2);
        AgentopiaConstants.TREASURE_DEBUG = true;
        final String agentUid = "test-agent-21343242";
        IAgentopiaAgent agent = new MockupAgent(agentUid);
        agent.setMarketPlace(market1);
        agent.setLogger(Logger.getLogger());
        String key = agent.getUID() + "-secret-key-1";
        Object treasureOrig = new BigInteger("505690548690369038594368346289032");
        market1.buryTreasure(agent, key, treasureOrig);
        market1.addAgent(agent);
        assertTrue(market1.lookupTreasure(agent, key));
        agent.runActivity();
        assertFalse(market1.lookupTreasure(agent, key));
        assertNull(market1.getAgent(agentUid));
        assertEquals(0, market1.getAgentCount());
        agent = market2.getAgent(agentUid);
        assertNotNull(agent);
        assertEquals(agentUid, agent.getUID());
        key = agent.getUID() + "-secret-key-2";
        Object treasureBack = market2.retrieveTreasure(agent, key, 0);
        assertNotNull(treasureBack);
        assertEquals(treasureOrig, treasureBack);
        AgentopiaConstants.TREASURE_DEBUG = false;
        env.shutDown();
        assertEquals(0, Logger.getLogger().getWarningCount());
    }

    /**
     * @throws Exception If something failed.
     */
    @Test
    public void testMarketPlaceRestart() throws Exception {
        MockupEnvironment env = new MockupEnvironment();
        final IMarketPlace market1 = env.getMarket1();
        final IMarketPlace market2 = env.getMarket2();
        assertNotNull(market1);
        assertNotNull(market2);
        final String agentUid = "test-agent-21343242";
        IAgentopiaAgent agent = new MockupAgent(agentUid);
        agent.setMarketPlace(market1);
        agent.setLogger(Logger.getLogger());
        String key = agent.getUID() + "-secret-key-1";
        Object treasureOrig = new BigInteger("505690548690369038594368346289032");
        market1.buryTreasure(agent, key, treasureOrig);
        market1.addAgent(agent);
        assertTrue(market1.lookupTreasure(agent, key));
        final String memento = env.market1.retrieveTreasureHeapMemento();
        assertNotNull(memento);
        env.market1.restoreTreasureHeapFromMemento(memento);
        Object treasureRestored = market1.retrieveTreasure(agent, key, 0);
        assertNotNull(treasureRestored);
        assertEquals(treasureOrig, treasureRestored);
        env.shutDown();
        assertEquals(0, Logger.getLogger().getWarningCount());
    }

    /**
     * A mockup environment with two market places.
     * 
     * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
     * @since 2008
     */
    public static class MockupEnvironment {

        /** Two connected market places. */
        private MarketPlace market1;

        /** Two connected market places. */
        private MarketPlace market2;

        /** The connected host ids. */
        public HostId hostId1;

        /** The connected host ids. */
        public HostId hostId2;

        /**
         * A new, two-market-place, environment.
         * 
         * @throws Exception If creation failed.
         */
        public MockupEnvironment() throws Exception {
            final String hostString1 = "localhost:15907";
            final String hostString2 = "localhost:15908";
            hostId1 = new HostId(hostString1);
            hostId2 = new HostId(hostString2);
            Config conf = Config.getConfig();
            conf.put("host0", hostString2);
            MockupHost host1 = new MockupHost(hostId1);
            MockupHost host2 = new MockupHost(hostId2);
            market1 = new MarketPlace(host1);
            market2 = new MarketPlace(host2);
            host1.addKnownMarketPlace(market2);
            market1.runStartup();
            market2.runStartup();
            market1.runTick(false);
            market2.runTick(false);
            assertEquals(1, market1.getExitCount());
            assertEquals(1, market2.getExitCount());
            conf.clear();
        }

        /**
         * @return The first market place.
         */
        public IMarketPlace getMarket1() {
            return market1;
        }

        /**
         * @return The second market place.
         */
        public IMarketPlace getMarket2() {
            return market2;
        }

        /**
         * Shuts the environment (and the markt places) down.
         */
        public void shutDown() {
            market1.shutDown();
            market2.shutDown();
            market1.runShutDown();
            market2.runShutDown();
        }

        /**
         * Removes all exists from the markets.
         */
        public void removeExits() {
            market1.removeExit(market1.getExitAt(0));
            market2.removeExit(market2.getExitAt(0));
        }
    }

    /**
     * Simulates a host.
     * 
     * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
     * @since 30 May 2008
     */
    public static class MockupHost implements IAgentopiaHost {

        /** The home id. */
        private HostId homeId;

        /** Other market places (used to create fake sustainers). */
        private List<IMarketPlace> marketPlaceList = new ArrayList<IMarketPlace>();

        /** The list of registered agents. */
        private List<IAgentopiaAgent> agentList = new ArrayList<IAgentopiaAgent>();

        /**
         * A new mockup host.
         * 
         * @param homeId The home id.
         */
        public MockupHost(HostId homeId) {
            this.homeId = homeId;
        }

        /**
         * Returns the home id.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaHost#getHomeId()
         */
        public HostId getHomeId() {
            return homeId;
        }

        /**
         * Other routers cannot be found.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaHost#contactRouter(net.sf.agentopia.util.net.HostId)
         */
        public String[] contactRouter(HostId routerId) throws IOException {
            return new String[0];
        }

        /**
         * Creates a fake connection to another market place if that other place
         * is stored in the appropriate member list (see above).
         * 
         * @see net.sf.agentopia.platform.IAgentopiaHost#createSustainer(net.sf.agentopia.platform.IMarketPlace,
         *      net.sf.agentopia.util.net.HostId,
         *      net.sf.agentopia.util.net.HostId)
         */
        public void createSustainer(IMarketPlace marketPlace, HostId targetHostId, HostId sourceHostId) throws IOException {
            for (IMarketPlace otherPlace : marketPlaceList) {
                if (otherPlace.getHomeId().equals(targetHostId)) {
                    new MockupSustainer(otherPlace, marketPlace);
                    new MockupSustainer(marketPlace, otherPlace);
                }
            }
            throw new IOException("Mockup connection to " + targetHostId + " could not be created.");
        }

        /**
         * Never works, and throws an exception.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaHost#createDirectConnection(net.sf.agentopia.util.net.HostId,
         *      int)
         */
        public IAgentopiaConnection createDirectConnection(HostId targetHostId, int firstFlag) throws IOException {
            throw new IOException("Fake direct network access failed (and it will always do so).");
        }

        /**
         * Returns the list of registered agents.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaHost#getRegisteredAgents()
         */
        public List<IAgentopiaAgent> getRegisteredAgents() {
            return agentList;
        }

        /**
         * Adds a fake known market place that can be connected to.
         * 
         * @param marketPlace The other market place.
         */
        public void addKnownMarketPlace(IMarketPlace marketPlace) {
            this.marketPlaceList.add(marketPlace);
        }

        /**
         * Ignored.
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
        }

        /**
         * Ignored.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaServerRunnable#shutDown()
         */
        public void shutDown() {
        }
    }

    /**
     * A mockup sustainer to connect to hosts within the same virtual machine.
     * 
     * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
     * @since 30 May 2008
     */
    public static class MockupSustainer implements IAgentopiaConnection {

        /** The fake market places. */
        private IMarketPlace market1, market2;

        /**
         * A new mockup sustainer.
         * 
         * @param market1 The source market place.
         * @param market2 The destination market place.
         */
        public MockupSustainer(IMarketPlace market1, IMarketPlace market2) {
            if (null == market1 || null == market2) {
                throw new IllegalArgumentException("Both market1 and 2 must not be null.");
            }
            this.market1 = market1;
            this.market2 = market2;
            try {
                market1.addExit(this);
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }

        /**
         * @see net.sf.agentopia.platform.IAgentopiaConnection#getTargetId()
         */
        public HostId getTargetId() {
            return market2.getHomeId();
        }

        /**
         * Does nothing.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaConnection#readInt()
         */
        public int readInt() throws IOException {
            return 0;
        }

        /**
         * Does nothing.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaConnection#writeInt(int)
         */
        public void writeInt(int intFlag) throws IOException {
        }

        /**
         * Moves agent from source market to destination market.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaConnection#transferMe(net.sf.agentopia.platform.IAgentopiaAgent)
         */
        public void transferMe(IAgentopiaAgent agent) {
            market1.removeAgent(agent);
            market2.addAgent(agent);
            agent.setMarketPlace(market2);
            agent.setLogger(Logger.getLogger());
            agent.runActivity();
        }

        /**
         * Nothing to do.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaServerRunnable#shutDown()
         */
        public void shutDown() {
        }

        /**
         * Nothing to do.
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return "Sustainer: " + getTargetId().toString();
        }
    }

    /**
     * A simple transporter agent.
     * 
     * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
     * @since 1 Jun 2008
     */
    public static class MockupAgent implements IAgentopiaAgent {

        /** The UID. */
        private String uid;

        /** The market place. */
        private IMarketPlaceForAgents marketPlace;

        /** A payload (treasured). */
        private Object payload;

        /**
         * A new mockup agent.
         * 
         * @param uid The uid.
         */
        public MockupAgent(String uid) {
            this.uid = uid;
        }

        /**
         * @see net.sf.agentopia.platform.IAgentopiaAgent#getUID()
         */
        public String getUID() {
            return uid;
        }

        /**
         * Does nothing.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaAgent#getAgentState()
         */
        public AgentopiaAgentState getAgentState() {
            return null;
        }

        /**
         * @see net.sf.agentopia.platform.IAgentopiaAgent#getMarketPlace()
         */
        public IMarketPlaceForAgents getMarketPlace() {
            return marketPlace;
        }

        /**
         * @see net.sf.agentopia.platform.IAgentopiaAgent#setMarketPlace(net.sf.agentopia.platform.IMarketPlace)
         */
        public void setMarketPlace(IMarketPlace marketPlace) {
            this.marketPlace = marketPlace;
        }

        /**
         * @see net.sf.agentopia.platform.IAgentopiaAgent#getLogger()
         */
        public IAgentopiaLogger getLogger() {
            return Logger.getLogger();
        }

        /**
         * Does nothing.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaAgent#setLogger(net.sf.agentopia.util.Logger.IAgentopiaLogger)
         */
        public void setLogger(IAgentopiaLogger logger) {
        }

        /**
         * Not supported.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaAgent#readHome(java.lang.String)
         */
        public String readHome(String promptMessage) throws IOException {
            throw new IOException("Method not supported.");
        }

        /**
         * Does nothing.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaAgent#retrieveTreasure(net.sf.agentopia.util.net.HostId,
         *      java.lang.String)
         */
        public Object retrieveTreasure(HostId treasureHostId, String treasureKey) throws IOException {
            return null;
        }

        /**
         * Does nothing.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaAgent#runActivity()
         */
        public void runActivity() {
            final IMarketPlaceForAgents market = getMarketPlace();
            final int port = market.getHomeId().getPort();
            if (15907 == port) {
                payload = market.retrieveTreasure(this, getUID() + "-secret-key-1", 0);
                try {
                    market.getExitAt(0).transferMe(this);
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            } else if (15908 == port) {
                market.buryTreasure(this, getUID() + "-secret-key-2", payload);
                payload = null;
            }
        }

        /**
         * Does nothing.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaAgent#runResults()
         */
        public void runResults() {
        }

        /**
         * Does nothing.
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
        }

        /**
         * Does nothing.
         * 
         * @see net.sf.agentopia.platform.IAgentopiaServerRunnable#shutDown()
         */
        public void shutDown() {
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return "Agent " + getUID() + " on marketplace " + getMarketPlace().getHomeId();
        }
    }
}
