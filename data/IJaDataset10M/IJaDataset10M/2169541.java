package unbbayes.prs.mebn.ssbn;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import unbbayes.io.mebn.UbfIO;
import unbbayes.prs.mebn.MFrag;
import unbbayes.prs.mebn.MultiEntityBayesianNetwork;
import unbbayes.prs.mebn.OrdinaryVariable;
import unbbayes.prs.mebn.ResidentNode;
import unbbayes.prs.mebn.entity.TypeContainer;

/**
 * @author Shou Matsumoto
 *
 */
public class SSBNNodeListTest extends TestCase {

    private MultiEntityBayesianNetwork mebn = null;

    private MFrag mfrag = null;

    private ResidentNode resident = null;

    private SSBNNode ssbnnode1 = null;

    private SSBNNode ssbnnode2 = null;

    private SSBNNode ssbnnode3 = null;

    private SSBNNodeList list = null;

    /**
	 * @param arg0
	 */
    public SSBNNodeListTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.list = new SSBNNodeList();
        UbfIO ubf = UbfIO.getInstance();
        try {
            mebn = ubf.loadMebn(new File("examples/mebn/SSBNNodeTest.ubf"));
        } catch (Exception e) {
            fail(e.getMessage());
            return;
        }
        if (this.mebn == null) {
            fail("Unable to load file");
            return;
        }
        this.resident = mebn.getDomainResidentNode("HarmPotential");
        if (this.resident == null) {
            fail("Unable to retreve resident node");
            return;
        }
        this.ssbnnode1 = SSBNNode.getInstance(null, this.resident);
        this.ssbnnode1.addArgument(resident.getOrdinaryVariableList().get(0), "ST4");
        this.ssbnnode1.addArgument(resident.getOrdinaryVariableList().get(1), "T0");
        this.list.add(this.ssbnnode1);
        this.ssbnnode2 = SSBNNode.getInstance(null, this.resident);
        this.ssbnnode2.addArgument(resident.getOrdinaryVariableList().get(0), "ST0");
        this.ssbnnode2.addArgument(resident.getOrdinaryVariableList().get(1), "T4");
        this.list.add(this.ssbnnode2);
        this.ssbnnode3 = SSBNNode.getInstance(null, this.resident);
        this.ssbnnode3.addArgument(resident.getOrdinaryVariableList().get(0), "ST4");
        this.ssbnnode3.addArgument(resident.getOrdinaryVariableList().get(1), "T4");
        this.list.add(this.ssbnnode3);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test method for {@link unbbayes.prs.mebn.ssbn.SSBNNodeList#getNodeByArgument(java.lang.String[])}.
	 */
    public final void testGetNodeByArgumentStringArray() {
        assertEquals(0, this.list.getNodeByArgument((String) null).size());
        assertEquals(0, this.list.getNodeByArgument((String[]) null).size());
        assertEquals(0, this.list.getNodeByArgument("").size());
        assertTrue(this.list.getNodeByArgument("ST4").contains(this.ssbnnode1));
        assertTrue(this.list.getNodeByArgument("ST4").contains(this.ssbnnode3));
        assertEquals(2, this.list.getNodeByArgument("ST4").size());
        assertTrue(this.list.getNodeByArgument("ST0").contains(this.ssbnnode2));
        assertEquals(1, this.list.getNodeByArgument("ST0").size());
        assertTrue(this.list.getNodeByArgument("T4").contains(this.ssbnnode2));
        assertTrue(this.list.getNodeByArgument("T4").contains(this.ssbnnode3));
        assertEquals(2, this.list.getNodeByArgument("T4").size());
        assertTrue(this.list.getNodeByArgument("T0").contains(this.ssbnnode1));
        assertEquals(1, this.list.getNodeByArgument("T0").size());
    }

    /**
	 * Test method for {@link unbbayes.prs.mebn.ssbn.SSBNNodeList#getNodeByArgument(java.util.Collection)}.
	 */
    public final void testGetNodeByArgumentCollectionOfOrdinaryVariable() {
        assertEquals(0, this.list.getNodeByArgument((Collection<OrdinaryVariable>) null).size());
        assertEquals(0, this.list.getNodeByArgument(new ArrayList<OrdinaryVariable>()).size());
        assertTrue(this.list.getNodeByArgument(this.ssbnnode1.getOVs()).contains(this.ssbnnode1));
        assertTrue(this.list.getNodeByArgument(this.ssbnnode2.getOVs()).contains(this.ssbnnode2));
        assertTrue(this.list.getNodeByArgument(this.ssbnnode3.getOVs()).contains(this.ssbnnode3));
        assertEquals(3, this.list.getNodeByArgument(this.ssbnnode1.getOVs()).size());
        assertEquals(3, this.list.getNodeByArgument(this.ssbnnode2.getOVs()).size());
        assertEquals(3, this.list.getNodeByArgument(this.ssbnnode3.getOVs()).size());
    }

    /**
	 * Test method for {@link unbbayes.prs.mebn.ssbn.SSBNNodeList#getNode(java.lang.String)}.
	 */
    public final void testGetNode() {
        assertEquals(0, this.list.getNode("adsfadsf").size());
        assertEquals(0, this.list.getNode(null).size());
        assertEquals(0, this.list.getNode("").size());
        assertEquals(this.ssbnnode1, this.list.getNode(this.ssbnnode1.getName()).iterator().next());
        assertEquals(this.ssbnnode2, this.list.getNode(this.ssbnnode2.getName()).iterator().next());
        assertEquals(this.ssbnnode3, this.list.getNode(this.ssbnnode3.getName()).iterator().next());
    }

    /**
	 * Test method for {@link unbbayes.prs.mebn.ssbn.SSBNNodeList#contains(Object)}.
	 */
    public final void testContains() {
        OrdinaryVariable st = new OrdinaryVariable("st", this.mebn.getTypeContainer().getType("Starship_label"), this.resident.getMFrag());
        OrdinaryVariable t = new OrdinaryVariable("t", this.mebn.getTypeContainer().getType("TimeStep_label"), this.resident.getMFrag());
        assertNotNull(st);
        assertNotNull(t);
        SSBNNode node1 = SSBNNode.getInstance(null, this.resident);
        assertNotNull(this.ssbnnode1);
        assertNotNull(node1);
        try {
            node1.addArgument(st, "ST4");
            node1.addArgument(t, "T0");
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        SSBNNodeList list = new SSBNNodeList();
        this.ssbnnode1.getName();
        node1.getName();
        list.add(this.ssbnnode1);
        assertTrue(list.contains(node1));
    }
}
