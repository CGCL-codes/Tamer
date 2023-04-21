package net.ontopia.persistence.proxy;

import java.util.*;
import net.ontopia.utils.*;
import net.ontopia.infoset.core.*;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.utils.*;
import net.ontopia.topicmaps.entry.TopicMaps;
import org.jgroups.Channel;
import org.jgroups.ChannelException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.MessageListener;
import org.jgroups.blocks.PullPushAdapter;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * INTERNAL: Client test class that receives events from a master. The
 * tests each check an individual aspect of the object model api.
 */
@Ignore
public class ClusterClientTest extends AbstractClusterTest {

    static Logger log = LoggerFactory.getLogger(ClusterClientTest.class.getName());

    Map tests;

    TopicMapIF topicmap;

    boolean testInitialProperties;

    int testsRun;

    int testsFailed;

    public ClusterClientTest(String clusterId, String clusterProps) {
        super(clusterId, clusterProps);
    }

    public void setUp() {
        TopicMapStoreIF store = TopicMaps.createStore("cluster-test", false);
        topicmap = store.getTopicMap();
        tests = new HashMap();
        tests.put("test:start", new ClientTest() {

            public void run(MasterTest mt) {
                done = false;
            }
        });
        tests.put("TopicMapIF.addItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                TopicMapIF m = (TopicMapIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Topic map source locator is not set", m.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Topic map not found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")).equals(m));
            }
        });
        tests.put("TopicMapIF.removeItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                TopicMapIF m = (TopicMapIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Topic map source locator is set", !m.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Topic map found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")) == null);
            }
        });
        tests.put("TopicMapIF.addTopic", new ClientTest() {

            public void run(MasterTest mt) {
                TopicIF t = (TopicIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Added topic not found", t != null);
                if (testInitialProperties) {
                    assertTrue("Subject locator is set", t.getSubjectLocators().isEmpty());
                    assertTrue("Source locators is set", t.getItemIdentifiers().isEmpty());
                    assertTrue("Subject identifiers is set", t.getSubjectIdentifiers().isEmpty());
                    assertTrue("Types is set", t.getTypes().isEmpty());
                    assertTrue("Base names is set", t.getTopicNames().isEmpty());
                    assertTrue("Occurrences is set", t.getOccurrences().isEmpty());
                }
            }
        });
        tests.put("TopicIF.setSubject", new ClientTest() {

            public void run(MasterTest mt) {
                TopicIF t = (TopicIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Subject locator is not set", t.getSubjectLocators().contains(Locators.getURILocator("x:subject")));
                assertTrue("Topic not found by subject locator", topicmap.getTopicBySubjectLocator(Locators.getURILocator("x:subject")).equals(t));
            }
        });
        tests.put("TopicIF.setSubject:clear", new ClientTest() {

            public void run(MasterTest mt) {
                TopicIF t = (TopicIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Subject locator is not null", t.getSubjectLocators().isEmpty());
                assertTrue("Topic found by subject locator", topicmap.getTopicBySubjectLocator(Locators.getURILocator("x:subject")) == null);
            }
        });
        tests.put("TopicIF.addSubjectIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                TopicIF t = (TopicIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Subject identifier is not set", t.getSubjectIdentifiers().contains(Locators.getURILocator("x:subject-indicator")));
                assertTrue("Topic not found by subject identifier", topicmap.getTopicBySubjectIdentifier(Locators.getURILocator("x:subject-indicator")).equals(t));
            }
        });
        tests.put("TopicIF.removeSubjectIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                TopicIF t = (TopicIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Subject identifier is set", !t.getSubjectIdentifiers().contains(Locators.getURILocator("x:subject-indicator")));
                assertTrue("Topic found by subject identifier", topicmap.getTopicBySubjectIdentifier(Locators.getURILocator("x:subject-indicator")) == null);
            }
        });
        tests.put("TopicIF.addItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                TopicIF t = (TopicIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Topic source locator is not set", t.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Topic not found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")).equals(t));
            }
        });
        tests.put("TopicIF.removeItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                TopicIF t = (TopicIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Topic source locator is set", !t.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Topic found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")) == null);
            }
        });
        tests.put("TopicIF.addType", new ClientTest() {

            public void run(MasterTest mt) {
                TopicIF t = (TopicIF) topicmap.getObjectById(mt.objectId);
                TopicIF type = (TopicIF) topicmap.getObjectById(mt.value);
                assertTrue("Topic type is not set", t.getTypes().contains(type));
            }
        });
        tests.put("TopicIF.removeType", new ClientTest() {

            public void run(MasterTest mt) {
                TopicIF t = (TopicIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Topic type is set", t.getTypes().isEmpty());
            }
        });
        tests.put("TopicIF.addTopicName", new ClientTest() {

            public void run(MasterTest mt) {
                TopicNameIF bn = (TopicNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Added base name not found", bn != null);
                if (testInitialProperties) {
                    assertTrue("Source locators is set", bn.getItemIdentifiers().isEmpty());
                    assertTrue("Scope is set", bn.getScope().isEmpty());
                    assertTrue("Type is set", bn.getType() == null);
                    assertTrue("Value is set", "".equals(bn.getValue()));
                    assertTrue("Variants is set", bn.getVariants().isEmpty());
                }
            }
        });
        tests.put("TopicNameIF.addItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                TopicNameIF bn = (TopicNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Base name source locator is not set", bn.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Base name not found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")).equals(bn));
            }
        });
        tests.put("TopicNameIF.removeItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                TopicNameIF bn = (TopicNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Base name source locator is set", !bn.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Base name found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")) == null);
            }
        });
        tests.put("TopicNameIF.addTheme", new ClientTest() {

            public void run(MasterTest mt) {
                TopicNameIF bn = (TopicNameIF) topicmap.getObjectById(mt.objectId);
                TopicIF theme = (TopicIF) topicmap.getObjectById(mt.value);
                assertTrue("Base name theme is not set", bn.getScope().contains(theme));
            }
        });
        tests.put("TopicNameIF.removeTheme", new ClientTest() {

            public void run(MasterTest mt) {
                TopicNameIF bn = (TopicNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Base name theme is set", bn.getScope().isEmpty());
            }
        });
        tests.put("TopicNameIF.setType", new ClientTest() {

            public void run(MasterTest mt) {
                TopicNameIF bn = (TopicNameIF) topicmap.getObjectById(mt.objectId);
                TopicIF type = (TopicIF) topicmap.getObjectById(mt.value);
                assertTrue("Base name theme is not set", type.equals(bn.getType()));
            }
        });
        tests.put("TopicNameIF.setType:clear", new ClientTest() {

            public void run(MasterTest mt) {
                TopicNameIF bn = (TopicNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Base name theme is set", bn.getType() == null);
            }
        });
        tests.put("TopicNameIF.setValue", new ClientTest() {

            public void run(MasterTest mt) {
                TopicNameIF bn = (TopicNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Base name value is not set", "New name".equals(bn.getValue()));
            }
        });
        tests.put("TopicNameIF.setValue:clear", new ClientTest() {

            public void run(MasterTest mt) {
                TopicNameIF bn = (TopicNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Base name value is not null", "".equals(bn.getValue()));
            }
        });
        tests.put("TopicNameIF.addVariant", new ClientTest() {

            public void run(MasterTest mt) {
                VariantNameIF vn = (VariantNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Added variant name not found", vn != null);
                if (testInitialProperties) {
                    assertTrue("Source locators is set", vn.getItemIdentifiers().isEmpty());
                    assertTrue("Scope is set", vn.getScope().isEmpty());
                    assertTrue("Value is set", vn.getValue() == null);
                    assertTrue("Locator is set", vn.getLocator() == null);
                }
            }
        });
        tests.put("VariantNameIF.addItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                VariantNameIF vn = (VariantNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Variant name source locator is not set", vn.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Variant name not found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")).equals(vn));
            }
        });
        tests.put("VariantNameIF.removeItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                VariantNameIF vn = (VariantNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Variant name source locator is set", !vn.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Variant name found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")) == null);
            }
        });
        tests.put("VariantNameIF.addTheme", new ClientTest() {

            public void run(MasterTest mt) {
                VariantNameIF vn = (VariantNameIF) topicmap.getObjectById(mt.objectId);
                TopicIF theme = (TopicIF) topicmap.getObjectById(mt.value);
                assertTrue("Variant name theme is not set", vn.getScope().contains(theme));
            }
        });
        tests.put("VariantNameIF.removeTheme", new ClientTest() {

            public void run(MasterTest mt) {
                VariantNameIF vn = (VariantNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Variant name theme is set", vn.getScope().isEmpty());
            }
        });
        tests.put("VariantNameIF.setValue", new ClientTest() {

            public void run(MasterTest mt) {
                VariantNameIF vn = (VariantNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Variant name value is not set", "New variant".equals(vn.getValue()));
            }
        });
        tests.put("VariantNameIF.setValue:clear", new ClientTest() {

            public void run(MasterTest mt) {
                VariantNameIF vn = (VariantNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Variant name value is not null", "".equals(vn.getValue()));
            }
        });
        tests.put("VariantNameIF.setLocator", new ClientTest() {

            public void run(MasterTest mt) {
                VariantNameIF vn = (VariantNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Variant name locator is not set", Locators.getURILocator("x:variant-locator").equals(vn.getLocator()));
            }
        });
        tests.put("VariantNameIF.setLocator:clear", new ClientTest() {

            public void run(MasterTest mt) {
                VariantNameIF vn = (VariantNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Variant name locator is not null", Locators.getURILocator("x:variant-locator:clear").equals(vn.getLocator()));
            }
        });
        tests.put("TopicNameIF.removeVariant", new ClientTest() {

            public void run(MasterTest mt) {
                VariantNameIF vn = (VariantNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Removed variant name found", vn == null);
            }
        });
        tests.put("TopicIF.removeTopicName", new ClientTest() {

            public void run(MasterTest mt) {
                TopicNameIF bn = (TopicNameIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Removed base name found", bn == null);
            }
        });
        tests.put("TopicIF.addOccurrence", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Added occurrence not found", o != null);
                if (testInitialProperties) {
                    assertTrue("Source locators is set", o.getItemIdentifiers().isEmpty());
                    assertTrue("Scope is set", o.getScope().isEmpty());
                    assertTrue("Type is set", o.getType() == null);
                    assertTrue("Value is set", o.getValue() == null);
                    assertTrue("Locator is set", o.getLocator() == null);
                }
            }
        });
        tests.put("OccurrenceIF.addItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Occurrence source locator is not set", o.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Occurrence not found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")).equals(o));
            }
        });
        tests.put("OccurrenceIF.removeItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Occurrence source locator is set", !o.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Occurrence found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")) == null);
            }
        });
        tests.put("OccurrenceIF.addTheme", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                TopicIF theme = (TopicIF) topicmap.getObjectById(mt.value);
                assertTrue("Occurrence theme is not set", o.getScope().contains(theme));
            }
        });
        tests.put("OccurrenceIF.removeTheme", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Occurrence theme is set", o.getScope().isEmpty());
            }
        });
        tests.put("OccurrenceIF.setType", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                TopicIF type = (TopicIF) topicmap.getObjectById(mt.value);
                assertTrue("Occurrence type is not set", type.equals(o.getType()));
            }
        });
        tests.put("OccurrenceIF.setType:clear", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Occurrence type is set", o.getType().getSubjectIdentifiers().contains(Locators.getURILocator("type:cleared")));
            }
        });
        tests.put("OccurrenceIF.setValue", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Occurrence value is not set", "New occurrence".equals(o.getValue()));
            }
        });
        tests.put("OccurrenceIF.setValue:clear", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Occurrence value is not null", "".equals(o.getValue()));
            }
        });
        tests.put("OccurrenceIF.setLocator", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Occurrence locator is not set", Locators.getURILocator("x:occurrence-locator").equals(o.getLocator()));
            }
        });
        tests.put("OccurrenceIF.setLocator:clear", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Occurrence locator is not null", Locators.getURILocator("x:occurrence-locator:clear").equals(o.getLocator()));
            }
        });
        tests.put("TopicIF.removeOccurrence", new ClientTest() {

            public void run(MasterTest mt) {
                OccurrenceIF o = (OccurrenceIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Removed occurrence found", o == null);
            }
        });
        tests.put("TopicMapIF.addAssociation", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationIF a = (AssociationIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Added association not found", a != null);
                if (testInitialProperties) {
                    assertTrue("Source locators is set", a.getItemIdentifiers().isEmpty());
                    assertTrue("Scope is set", a.getScope().isEmpty());
                    assertTrue("Type is set", a.getType() == null);
                    assertTrue("Roles is set", a.getRoles().isEmpty());
                }
            }
        });
        tests.put("AssociationIF.addItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationIF a = (AssociationIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Association source locator is not set", a.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Association not found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")).equals(a));
            }
        });
        tests.put("AssociationIF.removeItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationIF a = (AssociationIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Association source locator is set", !a.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Association found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")) == null);
            }
        });
        tests.put("AssociationIF.addTheme", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationIF a = (AssociationIF) topicmap.getObjectById(mt.objectId);
                TopicIF theme = (TopicIF) topicmap.getObjectById(mt.value);
                assertTrue("Association theme is not set", a.getScope().contains(theme));
            }
        });
        tests.put("AssociationIF.removeTheme", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationIF a = (AssociationIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Association theme is set", a.getScope().isEmpty());
            }
        });
        tests.put("AssociationIF.setType", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationIF a = (AssociationIF) topicmap.getObjectById(mt.objectId);
                TopicIF type = (TopicIF) topicmap.getObjectById(mt.value);
                assertTrue("Association type is not set", type.equals(a.getType()));
            }
        });
        tests.put("AssociationIF.setType:clear", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationIF a = (AssociationIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Association type is set", a.getType().getSubjectIdentifiers().contains(Locators.getURILocator("type:cleared")));
            }
        });
        tests.put("AssociationIF.addRole", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationRoleIF r = (AssociationRoleIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Added role not found", r != null);
                if (testInitialProperties) {
                    assertTrue("Source locators is set", r.getItemIdentifiers().isEmpty());
                    assertTrue("Type is set", r.getType() == null);
                    assertTrue("Player is set", r.getPlayer() == null);
                }
            }
        });
        tests.put("AssociationRoleIF.addItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationRoleIF r = (AssociationRoleIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Role source locator is not set", r.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Role not found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")).equals(r));
            }
        });
        tests.put("AssociationRoleIF.removeItemIdentifier", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationRoleIF r = (AssociationRoleIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Role source locator is set", !r.getItemIdentifiers().contains(Locators.getURILocator("x:source-locator")));
                assertTrue("Role found by source locator", topicmap.getObjectByItemIdentifier(Locators.getURILocator("x:source-locator")) == null);
            }
        });
        tests.put("AssociationRoleIF.setType", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationRoleIF r = (AssociationRoleIF) topicmap.getObjectById(mt.objectId);
                TopicIF type = (TopicIF) topicmap.getObjectById(mt.value);
                assertTrue("Role type is not set", type.equals(r.getType()));
            }
        });
        tests.put("AssociationRoleIF.setType:clear", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationRoleIF r = (AssociationRoleIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Role type is set", r.getType().getSubjectIdentifiers().contains(Locators.getURILocator("type:cleared")));
            }
        });
        tests.put("AssociationRoleIF.setPlayer", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationRoleIF r = (AssociationRoleIF) topicmap.getObjectById(mt.objectId);
                TopicIF player = (TopicIF) topicmap.getObjectById(mt.value);
                assertTrue("Role player is not set" + player, player.equals(r.getPlayer()));
                assertTrue("Player role is not set", player.getRoles().contains(r));
            }
        });
        tests.put("AssociationRoleIF.setPlayer:clear", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationRoleIF r = (AssociationRoleIF) topicmap.getObjectById(mt.objectId);
                TopicIF player = (TopicIF) topicmap.getObjectById(mt.value);
                assertTrue("Role player is set", r.getPlayer().getSubjectIdentifiers().contains(Locators.getURILocator("player:cleared")));
                assertTrue("Player roles is set", player.getRoles().isEmpty());
            }
        });
        tests.put("AssociationIF.removeRole", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationRoleIF r = (AssociationRoleIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Removed role found", r == null);
            }
        });
        tests.put("TopicMapIF.removeAssociation", new ClientTest() {

            public void run(MasterTest mt) {
                AssociationIF a = (AssociationIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Removed association found", a == null);
            }
        });
        tests.put("TopicMapIF.removeTopic", new ClientTest() {

            public void run(MasterTest mt) {
                TopicIF t = (TopicIF) topicmap.getObjectById(mt.objectId);
                assertTrue("Removed topic found", t == null);
            }
        });
        tests.put("test:end", new ClientTest() {

            public void run(MasterTest mt) {
                done = true;
            }
        });
        joinCluster();
    }

    public void tearDown() {
        leaveCluster();
        if (topicmap != null) topicmap.getStore().close();
    }

    public void run() throws InterruptedException {
        System.out.println("Client is ready.");
        while (true) {
            Thread.sleep(100);
            if (done) break;
        }
    }

    public void receive(Message msg) {
        try {
            MasterTest mt = (MasterTest) msg.getObject();
            testsRun++;
            System.out.println("Received test: " + mt.testname);
            ClientTest ct = (ClientTest) tests.get(mt.testname);
            if (ct == null) throw new OntopiaRuntimeException("Could not find test: " + mt.testname);
            ct.run(mt);
            topicmap.getStore().commit();
        } catch (Exception e) {
            testsFailed++;
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        CmdlineUtils.initializeLogging();
        CmdlineOptions options = new CmdlineOptions("ClusterClientTest", args);
        CmdlineUtils.registerLoggingOptions(options);
        String clusterId = "cluster-test";
        String clusterProps = null;
        ClusterClientTest tester = new ClusterClientTest(clusterId, clusterProps);
        try {
            tester.setUp();
            tester.run();
        } finally {
            tester.tearDown();
        }
        System.out.println("Tests: " + tester.testsRun + " failed: " + tester.testsFailed);
    }
}
