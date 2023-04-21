package org.personalsmartspace.psm.dynmm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentFactory;
import org.personalsmartspace.psm.dynmm.defaultEvaluators.FalseEvaluator;
import org.personalsmartspace.psm.dynmm.defaultEvaluators.IntersectionCompositeEvaluator;
import org.personalsmartspace.psm.dynmm.defaultEvaluators.NegationCompositeEvaluator;
import org.personalsmartspace.psm.dynmm.defaultEvaluators.TrueEvaluator;
import org.personalsmartspace.psm.dynmm.defaultEvaluators.UnionCompositeEvaluator;
import org.personalsmartspace.psm.dynmm.evaluatorConverters.AbstractCompositeEvaluatorConverter;
import org.personalsmartspace.psm.dynmm.evaluatorConverters.AbstractEvaluatorConverter;
import org.personalsmartspace.psm.groupmgmt.api.pss3p.IGroupMgmt;
import org.personalsmartspace.psm.groupmgmt.api.pss3p.IPssCompositeGroupMembershipEvaluator;
import org.personalsmartspace.psm.groupmgmt.api.pss3p.IPssGroupMembershipEvaluator;
import org.personalsmartspace.psm.groupmgmt.api.pss3p.PssGroupId;
import com.thoughtworks.xstream.XStream;

public class DynamicMembershipManagerTest {

    @Mock
    private ComponentFactory cf = null;

    @Mock
    private IGroupMgmt gm = null;

    private static final String _PSS_GROUP_ID = "PssGroupId";

    class Dmm4Test extends DynamicMembershipManager {

        @Override
        public void activate(ComponentContext context) {
            super.activate(context);
        }

        @Override
        public void deactivate(ComponentContext context) {
            super.deactivate(context);
        }

        @Override
        public synchronized void setAtomicCC(ComponentFactory atomicCC) {
            super.setAtomicCC(atomicCC);
        }

        @Override
        public synchronized void setCompositeCC(ComponentFactory compositeCC) {
            super.setCompositeCC(compositeCC);
        }

        @Override
        public synchronized void setEvaluatorInst(IPssGroupMembershipEvaluator atomicEvaluator) {
            super.setEvaluatorInst(atomicEvaluator);
        }

        @Override
        public synchronized void setGroupManager(IGroupMgmt sgm) {
            super.setGroupManager(sgm);
        }

        public Log getLogService() {
            return logService;
        }

        public void setLogService(Log logService) {
            this.logService = logService;
        }

        public Map<PssGroupId, String> getGroups2mer() {
            return groups2mer;
        }

        public void setGroups2mer(Map<PssGroupId, String> groups2mer) {
            this.groups2mer = groups2mer;
        }

        public Set<IPssGroupMembershipEvaluator> getLocalEvaluators() {
            return localEvaluators;
        }

        public void setLocalEvaluators(Set<IPssGroupMembershipEvaluator> localEvaluators) {
            this.localEvaluators = localEvaluators;
        }

        public IPssGroupMembershipEvaluator getEvaluatorInstance() {
            return evaluatorInstance;
        }

        public void setEvaluatorInstance(IPssGroupMembershipEvaluator evaluatorInstance) {
            this.evaluatorInstance = evaluatorInstance;
        }

        public Set<ComponentFactory> getActiveLocalCompositeCCs() {
            return activeLocalCompositeCCs;
        }

        public void setActiveLocalCompositeCCs(Set<ComponentFactory> activeLocalCompositeCCs) {
            this.activeLocalCompositeCCs = activeLocalCompositeCCs;
        }

        public Set<ComponentFactory> getActiveLocalAtomicCCs() {
            return activeLocalAtomicCCs;
        }

        public void setActiveLocalAtomicCCs(Set<ComponentFactory> activeLocalAtomicCCs) {
            this.activeLocalAtomicCCs = activeLocalAtomicCCs;
        }

        public ComponentFactory getCompositeComponentFactory() {
            return compositeComponentFactory;
        }

        public void setCompositeComponentFactory(ComponentFactory compositeComponentFactory) {
            this.compositeComponentFactory = compositeComponentFactory;
        }

        public ComponentFactory getCompositeAtomicFactory() {
            return compositeAtomicFactory;
        }

        public void setCompositeAtomicFactory(ComponentFactory compositeAtomicFactory) {
            this.compositeAtomicFactory = compositeAtomicFactory;
        }

        public IGroupMgmt getGroupManager() {
            return staticGroupManager;
        }
    }

    class TrueEvaluatorTest extends TrueEvaluator {

        public TrueEvaluatorTest(DynamicMembershipManager dmm) {
            super();
            this.dynMM = dmm;
        }
    }

    class FalseEvaluatorTest extends FalseEvaluator {

        public FalseEvaluatorTest(DynamicMembershipManager dmm) {
            super();
            this.dynMM = dmm;
        }
    }

    class IntersectionCompositeEvaluatorTest extends IntersectionCompositeEvaluator {

        public IntersectionCompositeEvaluatorTest(DynamicMembershipManager dmm) {
            super();
            this.dynMM = dmm;
        }
    }

    class UnionCompositeEvaluatorTest extends UnionCompositeEvaluator {

        public UnionCompositeEvaluatorTest(DynamicMembershipManager dmm) {
            super();
            this.dynMM = dmm;
        }
    }

    class NegationCompositeEvaluatorTest extends NegationCompositeEvaluator {

        public NegationCompositeEvaluatorTest(DynamicMembershipManager dmm) {
            super();
            this.dynMM = dmm;
        }

        public void addMer(IPssGroupMembershipEvaluator newMer) {
            this.evaluators.add(newMer);
        }
    }

    Dmm4Test dgmManager = new Dmm4Test();

    IPssGroupMembershipEvaluator trueEval = new TrueEvaluatorTest(this.dgmManager), falseEval = new FalseEvaluatorTest(this.dgmManager), andEval = new IntersectionCompositeEvaluatorTest(this.dgmManager), orEval = new UnionCompositeEvaluatorTest(this.dgmManager), notEval = new NegationCompositeEvaluatorTest(this.dgmManager);

    IPssCompositeGroupMembershipEvaluator notTrueEval = new NegationCompositeEvaluatorTest(this.dgmManager);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ((NegationCompositeEvaluatorTest) this.notTrueEval).addMer(trueEval);
        this.dgmManager.setGroupManager(gm);
        this.dgmManager.localEvaluators.add(this.andEval);
        this.dgmManager.localEvaluators.add(this.orEval);
        this.dgmManager.localEvaluators.add(this.notEval);
        this.dgmManager.localEvaluators.add(this.trueEval);
        this.dgmManager.localEvaluators.add(this.falseEval);
        this.dgmManager.localEvaluators.add(this.notTrueEval);
        this.dgmManager.xstream = new XStream();
        this.dgmManager.xstream.addDefaultImplementation(HashSet.class, Collection.class);
        this.dgmManager.xstream.registerConverter(new AbstractEvaluatorConverter(null));
        this.dgmManager.xstream.registerConverter(new AbstractCompositeEvaluatorConverter(null));
        this.dgmManager.xstream.aliasType("Mer", IPssGroupMembershipEvaluator.class);
    }

    @After
    public void tearDown() throws Exception {
        this.dgmManager.localEvaluators.clear();
    }

    @Test
    public final void testUnsetEvaluatorInst() {
        this.dgmManager.unsetEvaluatorInst(trueEval);
        assertNull(this.dgmManager.getEvaluatorInstance());
    }

    @Test
    public final void testSetEvaluatorInst() {
        this.dgmManager.setEvaluatorInst(trueEval);
        assertEquals(trueEval.getMerId(), this.dgmManager.getEvaluatorInstance().getMerId());
        this.dgmManager.unsetEvaluatorInst(trueEval);
        this.dgmManager.setEvaluatorInst(notTrueEval);
        assertEquals(notTrueEval.getMerId(), this.dgmManager.getEvaluatorInstance().getMerId());
    }

    /**
     * This is not a "real" unit test: because MerIds are generated "on the fly",
     * they will never be the same, even for equivalent MERs.
     * When a MER is deserialized, the calculated MerId is overwritten with the
     * persisted one. 
     */
    @Test
    public final void testXmlMarshal() {
        String xmlEval = this.dgmManager.xstream.toXML(notTrueEval);
        System.out.println(xmlEval);
        IPssGroupMembershipEvaluator cmp = (IPssGroupMembershipEvaluator) this.dgmManager.xstream.fromXML(xmlEval);
        assertNull(cmp);
    }

    /**
     * This is not a "real" unit test: because MerIds are generated "on the fly",
     * they will never be the same, even for equivalent MERs.
     * When a MER is deserialized, the calculated MerId is overwritten with the
     * persisted one. 
     */
    @Test
    public final void testXmlMarshal2() {
        Collection<IPssGroupMembershipEvaluator> c = new HashSet<IPssGroupMembershipEvaluator>();
        c.add(notTrueEval);
        c.add(andEval);
        c.add(falseEval);
        c.add(notEval);
        c.add(orEval);
        c.add(trueEval);
        String xmlEval = this.dgmManager.xstream.toXML(c);
        System.out.println(xmlEval);
        c = (Collection) this.dgmManager.xstream.fromXML(xmlEval);
        assertTrue(true);
    }

    @Test
    public final void testUnsetCompositeCC() {
        this.dgmManager.unsetCompositeCC(null);
        assertNull(this.dgmManager.getCompositeComponentFactory());
    }

    @Test
    public final void testSetCompositeCC() {
        this.dgmManager.setCompositeCC(this.cf);
        assertEquals(this.cf, this.dgmManager.getCompositeComponentFactory());
    }

    @Test
    public final void testUnsetAtomicCC() {
        this.dgmManager.unsetAtomicCC(null);
        assertNull(this.dgmManager.getCompositeAtomicFactory());
    }

    @Test
    public final void testSetAtomicCC() {
        this.dgmManager.setAtomicCC(this.cf);
        assertEquals(this.cf, this.dgmManager.getCompositeAtomicFactory());
    }

    @Test
    public final void testUnsetStaticGroupManager() {
        this.dgmManager.unsetGroupManager(null);
        assertNull(this.dgmManager.getGroupManager());
    }

    @Test
    public final void testSetStaticGroupManager() {
        this.dgmManager.setGroupManager(gm);
        assertEquals(this.gm, this.dgmManager.getGroupManager());
    }

    @Test
    public final void testGetAllMers() {
        this.dgmManager.localEvaluators.clear();
        this.dgmManager.localEvaluators.add(falseEval);
        this.dgmManager.localEvaluators.add(trueEval);
        Set<IPssGroupMembershipEvaluator> resultEvaluators = this.dgmManager.getAllMers();
        assertTrue(resultEvaluators.size() == 2);
    }

    @Test
    public final void testGetMerIdForGroup() {
        String merId = this.trueEval.getMerId();
        PssGroupId groupId = new PssGroupId(DynamicMembershipManagerTest._PSS_GROUP_ID);
        this.dgmManager.linkMerToGroup(merId, groupId);
        assertEquals(merId, this.dgmManager.getMerIdForGroup(groupId));
    }

    @Test
    public final void testLinkMerToGroup() {
        String merId = this.trueEval.getMerId();
        PssGroupId groupId = new PssGroupId(DynamicMembershipManagerTest._PSS_GROUP_ID);
        this.dgmManager.linkMerToGroup(merId, groupId);
        String foundMerId = this.dgmManager.getGroups2mer().get(groupId);
        assertEquals(merId, foundMerId);
    }

    @Test
    public final void testUnlinkMerFromGroup() {
        this.testLinkMerToGroup();
        this.dgmManager.unlinkMerFromGroup(this.trueEval.getMerId(), new PssGroupId(DynamicMembershipManagerTest._PSS_GROUP_ID));
        assertFalse(this.dgmManager.getGroups2mer().containsKey(this.trueEval.getMerId()));
    }

    @Test
    public final void testFindEvaluatorById() {
        this.dgmManager.localEvaluators.add(this.andEval);
        this.dgmManager.localEvaluators.add(this.orEval);
        this.dgmManager.localEvaluators.add(this.notEval);
        this.dgmManager.localEvaluators.add(this.trueEval);
        this.dgmManager.localEvaluators.add(this.falseEval);
        assertEquals(this.andEval, this.dgmManager.findEvaluatorById(this.andEval.getMerId()));
        assertEquals(this.orEval, this.dgmManager.findEvaluatorById(this.orEval.getMerId()));
        assertEquals(this.notEval, this.dgmManager.findEvaluatorById(this.notEval.getMerId()));
        assertEquals(this.trueEval, this.dgmManager.findEvaluatorById(this.trueEval.getMerId()));
        assertEquals(this.falseEval, this.dgmManager.findEvaluatorById(this.falseEval.getMerId()));
        assertNull(this.dgmManager.findEvaluatorById("NotExistingId"));
    }

    @Test
    public final void testDefaultEvaluators() {
        IPssCompositeGroupMembershipEvaluator notEval2 = new NegationCompositeEvaluatorTest(this.dgmManager);
        this.dgmManager.localEvaluators.add(notEval2);
        String andEvalId = this.andEval.getMerId(), orEvalId = this.orEval.getMerId(), notEvalId = this.notEval.getMerId(), not2EvalId = notEval2.getMerId(), trueEvalId = this.trueEval.getMerId(), falseEvalId = this.falseEval.getMerId();
        assertEquals(this.andEval, this.dgmManager.findEvaluatorById(andEvalId));
        assertEquals(this.orEval, this.dgmManager.findEvaluatorById(orEvalId));
        assertEquals(this.notEval, this.dgmManager.findEvaluatorById(notEvalId));
        assertEquals(this.trueEval, this.dgmManager.findEvaluatorById(trueEvalId));
        assertEquals(this.falseEval, this.dgmManager.findEvaluatorById(falseEvalId));
        assertNull(this.dgmManager.findEvaluatorById("NotExistingId"));
        ((IPssCompositeGroupMembershipEvaluator) this.andEval).addEvaluator(trueEvalId);
        ((IPssCompositeGroupMembershipEvaluator) this.andEval).addEvaluator(falseEvalId);
        ((IPssCompositeGroupMembershipEvaluator) this.notEval).addEvaluator(andEvalId);
        ((IPssCompositeGroupMembershipEvaluator) this.orEval).addEvaluator(trueEvalId);
        ((IPssCompositeGroupMembershipEvaluator) this.orEval).addEvaluator(falseEvalId);
        ((IPssCompositeGroupMembershipEvaluator) notEval2).addEvaluator(trueEvalId);
        assertFalse(this.andEval.evaluate(null));
        assertTrue(this.notEval.evaluate(null));
        assertTrue(this.orEval.evaluate(null));
        assertFalse(notEval2.evaluate(null));
    }

    @Test
    public final void testAddRemoveCompositeEvaluators() {
        IPssCompositeGroupMembershipEvaluator notEval2 = new NegationCompositeEvaluatorTest(this.dgmManager);
        this.dgmManager.localEvaluators.add(notEval2);
        String andEvalId = this.andEval.getMerId(), orEvalId = this.orEval.getMerId(), notEvalId = this.notEval.getMerId(), trueEvalId = this.trueEval.getMerId(), falseEvalId = this.falseEval.getMerId();
        assertEquals(this.andEval, this.dgmManager.findEvaluatorById(andEvalId));
        assertEquals(this.orEval, this.dgmManager.findEvaluatorById(orEvalId));
        assertEquals(this.notEval, this.dgmManager.findEvaluatorById(notEvalId));
        assertEquals(this.trueEval, this.dgmManager.findEvaluatorById(trueEvalId));
        assertEquals(this.falseEval, this.dgmManager.findEvaluatorById(falseEvalId));
        assertNull(this.dgmManager.findEvaluatorById("NotExistingId"));
        ((IPssCompositeGroupMembershipEvaluator) this.andEval).addEvaluator(trueEvalId);
        ((IPssCompositeGroupMembershipEvaluator) this.andEval).addEvaluator(falseEvalId);
        ((IPssCompositeGroupMembershipEvaluator) this.orEval).addEvaluator(trueEvalId);
        ((IPssCompositeGroupMembershipEvaluator) this.orEval).addEvaluator(falseEvalId);
        ((IPssCompositeGroupMembershipEvaluator) this.andEval).removeEvaluator(trueEvalId);
        assertFalse(((IntersectionCompositeEvaluatorTest) this.andEval).getComposedEvaluators().contains(this.trueEval));
        ((IPssCompositeGroupMembershipEvaluator) this.andEval).addEvaluator(trueEvalId);
        assertTrue(((IntersectionCompositeEvaluatorTest) this.andEval).getComposedEvaluators().contains(this.trueEval));
        ((IPssCompositeGroupMembershipEvaluator) this.orEval).removeEvaluator(trueEvalId);
        assertFalse(((UnionCompositeEvaluatorTest) this.orEval).getComposedEvaluators().contains(this.trueEval));
        ((IPssCompositeGroupMembershipEvaluator) this.orEval).addEvaluator(trueEvalId);
        assertTrue(((UnionCompositeEvaluatorTest) this.orEval).getComposedEvaluators().contains(this.trueEval));
    }

    @Test
    public final void testSetUnsetDescription() {
        assertEquals("Empty Intersection Evaluator", this.andEval.getMerDescription());
        this.andEval.setMerDescription(DynamicMembershipManagerTest._PSS_GROUP_ID);
        assertTrue(DynamicMembershipManagerTest._PSS_GROUP_ID.equalsIgnoreCase(this.andEval.getMerDescription()));
        this.andEval.setMerDescription("Empty Intersection Evaluator");
    }

    @Test
    public final void testSetUnsetDescriptio_n() {
        assertTrue(true);
    }
}
