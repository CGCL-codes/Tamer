package lug.serenity.npc.model.traits;

import junit.framework.TestCase;

/**
 * @author Luggy
 *
 */
public class TraitListTest extends TestCase {

    private TraitList assetList;

    private TraitList complicationList;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        assetList = new TraitList(TraitType.ASSET);
        assetList.add(createMajorAsset("Reader"));
        assetList.add(createMinorAsset("Lucky"));
        complicationList = new TraitList(TraitType.COMPLICATION);
        complicationList.add(createMajorComplication("Blind"));
        complicationList.add(createMinorComplication("Clumsy"));
    }

    /**
	 * Construct a major asset.
	 * @param name name of the asset.
	 * @return
	 */
    private static final TraitData createMajorAsset(String name) {
        TraitData ret = TraitData.createMajorTrait(name, true);
        ret.setType(TraitType.ASSET);
        return ret;
    }

    /**
	 * Construct a minor asset.
	 * @param name name of the asset.
	 * @return
	 */
    private static final TraitData createMinorAsset(String name) {
        TraitData ret = TraitData.createMinorTrait(name, false);
        ret.setType(TraitType.ASSET);
        return ret;
    }

    /**
	 * Construct a major complication.
	 * @param name name of the complication.
	 * @return
	 */
    private static final TraitData createMajorComplication(String name) {
        TraitData ret = TraitData.createMajorTrait(name, false);
        ret.setType(TraitType.COMPLICATION);
        return ret;
    }

    /**
	 * Construct a minor complication.
	 * @param name name of the complication.
	 * @return
	 */
    private static final TraitData createMinorComplication(String name) {
        TraitData ret = TraitData.createMinorTrait(name, false);
        ret.setType(TraitType.COMPLICATION);
        return ret;
    }

    /**
	 * Test add.
	 */
    public void testAdd() {
        TraitList list = new TraitList(TraitType.ASSET);
        TraitData asset1 = createMajorAsset("Asset1");
        TraitData asset2 = createMajorAsset("Asset2");
        assertEquals(0, list.size());
        list.add(asset1);
        assertEquals(1, list.size());
        list.add(asset1);
        assertEquals(1, list.size());
        list.add(asset2);
        assertEquals(2, list.size());
    }

    /**
	 * Test add.
	 */
    public void testAddAt() {
        TraitList list = new TraitList(TraitType.ASSET);
        TraitData asset1 = createMajorAsset("Asset1");
        TraitData asset2 = createMajorAsset("Asset2");
        list.add(asset1);
        list.add(0, asset1);
        assertEquals(1, list.size());
        list.add(0, asset2);
        assertEquals(2, list.size());
        assertEquals(asset2, list.getElementAt(0));
    }

    /**
	 * Test add.
	 */
    public void testRemove() {
        TraitList list = new TraitList(TraitType.ASSET);
        TraitData asset1 = createMajorAsset("Asset1");
        TraitData asset2 = createMajorAsset("Asset2");
        list.add(asset1);
        list.add(asset2);
        assertEquals(2, list.size());
        list.remove(0);
        assertEquals(1, list.size());
        assertEquals(asset2, list.getElementAt(0));
        list.add(asset1);
        assertEquals(2, list.size());
        assertEquals(asset1, list.getElementAt(1));
        list.remove(asset2);
        assertEquals(1, list.size());
        assertEquals(asset1, list.getElementAt(0));
    }

    /**
	 * Test that only assets can be added to asset lists.
	 */
    public void testAssetListType() {
        TraitList assetList = new TraitList(TraitType.ASSET);
        assetList.add(createMinorAsset("SomeAsset"));
        try {
            assetList.add(createMinorComplication("SomeComplication"));
            fail("IllegalArgumentException should have been thrown when attempting to add a" + " Complication to an Asset list.");
        } catch (IllegalArgumentException iae1) {
        }
    }

    /**
	 * Test that only complications can be added to complication lists.
	 */
    public void testComplicationListType() {
        TraitList assetList = new TraitList(TraitType.COMPLICATION);
        assetList.add(createMinorComplication("SomeComplication"));
        try {
            assetList.add(createMinorAsset("SomeAsset"));
            fail("IllegalArgumentException should have been thrown when attempting to add an " + "asset to aComplication list.");
        } catch (IllegalArgumentException iae1) {
        }
    }

    /**
	 * Test that a list of unknown complications cannot be created.
	 */
    public void testUnknownList() {
        try {
            new TraitList(TraitType.UNKNOWN);
            fail("TraitList should not have been created with TraitType.UNKNOWN");
        } catch (IllegalArgumentException iae1) {
        }
    }

    /**
	 * Test the total points.
	 */
    public void testAssetsTotalCost() {
        TraitList list = new TraitList(TraitType.ASSET);
        TraitData major1 = createMajorAsset("Major1");
        TraitData major2 = createMajorAsset("Major2");
        TraitData minor1 = createMinorAsset("Minor1");
        TraitData minor2 = createMinorAsset("Minor2");
        assertEquals(0, list.getTotalCost());
        list.add(major1);
        assertEquals(4, list.getTotalCost());
        list.add(major1);
        assertEquals(4, list.getTotalCost());
        list.add(major2);
        assertEquals(8, list.getTotalCost());
        list.add(minor1);
        assertEquals(10, list.getTotalCost());
        list.remove(major2);
        assertEquals(6, list.getTotalCost());
        list.add(minor2);
        assertEquals(8, list.getTotalCost());
        list.remove(major1);
        assertEquals(4, list.getTotalCost());
        list.remove(major1);
        assertEquals(4, list.getTotalCost());
        list.remove(minor1);
        assertEquals(2, list.getTotalCost());
        list.remove(minor2);
        assertEquals(0, list.getTotalCost());
    }

    /**
	 * Test the total points.
	 */
    public void testComplicationsTotalCost() {
        TraitList list = new TraitList(TraitType.COMPLICATION);
        TraitData major1 = createMajorComplication("Major1");
        TraitData major2 = createMajorComplication("Major2");
        TraitData minor1 = createMinorComplication("Minor1");
        TraitData minor2 = createMinorComplication("Minor2");
        assertEquals(0, list.getTotalCost());
        list.add(major1);
        assertEquals(-4, list.getTotalCost());
        list.add(major1);
        assertEquals(-4, list.getTotalCost());
        list.add(major2);
        assertEquals(-8, list.getTotalCost());
        list.add(minor1);
        assertEquals(-10, list.getTotalCost());
        list.remove(major2);
        assertEquals(-6, list.getTotalCost());
        list.add(minor2);
        assertEquals(-8, list.getTotalCost());
        list.remove(major1);
        assertEquals(-4, list.getTotalCost());
        list.remove(major1);
        assertEquals(-4, list.getTotalCost());
        list.remove(minor1);
        assertEquals(-2, list.getTotalCost());
        list.remove(minor2);
        assertEquals(0, list.getTotalCost());
    }

    public void testReplace() {
        TraitList list = new TraitList(TraitType.COMPLICATION);
        TraitData major1 = createMajorComplication("Major1");
        TraitData major2 = createMajorComplication("Major2");
        TraitData minor1 = createMinorComplication("Minor1");
        list.add(major1);
        list.add(major2);
        assertTrue(list.contains(major1));
        assertFalse(list.contains(minor1));
        int idx = list.indexOf(major1);
        list.replace(major1, minor1);
        assertFalse(list.contains(major1));
        assertTrue(list.contains(minor1));
        assertEquals(idx, list.indexOf(minor1));
    }

    public void testContainsTrat() {
        TraitList list = new TraitList(TraitType.COMPLICATION);
        TraitData major1 = createMajorComplication("Major1");
        list.add(major1);
        Trait trait1 = new Trait();
        trait1.setName("Major1");
        Trait trait2 = new Trait();
        trait2.setName("Major2");
        assertTrue(list.containsTrait(trait1));
        assertFalse(list.containsTrait(trait2));
    }
}
