package solidbase.core;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.Test;
import solidbase.core.UpgradeSegment.Type;
import solidstack.io.FileResource;
import solidstack.io.RandomAccessSourceReader;

/**
 * Tests {@link UpgradeFile}.
 *
 * @author Ren� M. de Bloois
 */
public class PatchFileTests {

    /**
	 * Tests whether the {@link UpgradeFile} returns the correct set of tip targets.
	 *
	 * @throws IOException Whenever it needs to.
	 */
    @Test
    public void testCollectTipVersions1() throws IOException {
        RandomAccessSourceReader ralr = new RandomAccessSourceReader(new FileResource("testpatch1.sql"));
        UpgradeFile upgradeFile = new UpgradeFile(ralr);
        upgradeFile.scan();
        upgradeFile.close();
        Map<String, Collection<UpgradeSegment>> patches = upgradeFile.segments;
        put(patches, "1.1", new UpgradeSegment(Type.UPGRADE, "1.1", "1.2", false));
        put(patches, "1.2", new UpgradeSegment(Type.UPGRADE, "1.2", "1.3", false));
        put(patches, "1.3", new UpgradeSegment(Type.UPGRADE, "1.3", "1.4", false));
        put(patches, "1.4", new UpgradeSegment(Type.UPGRADE, "1.4", "1.5", false));
        put(patches, "1.5", new UpgradeSegment(Type.SWITCH, "1.5", "2.1", false));
        put(patches, "1.3", new UpgradeSegment(Type.UPGRADE, "1.3", "2.1", false));
        put(patches, "2.1", new UpgradeSegment(Type.UPGRADE, "2.1", "2.2", false));
        put(patches, "2.2", new UpgradeSegment(Type.UPGRADE, "2.2", "2.3", false));
        put(patches, "2.3", new UpgradeSegment(Type.UPGRADE, "2.3", "2.4", false));
        put(patches, "2.4", new UpgradeSegment(Type.UPGRADE, "2.4", "2.5", false));
        put(patches, "2.5", new UpgradeSegment(Type.SWITCH, "2.5", "3.1", false));
        put(patches, "2.3", new UpgradeSegment(Type.UPGRADE, "2.3", "3.1", false));
        put(patches, "3.1", new UpgradeSegment(Type.UPGRADE, "3.1", "3.2", false));
        upgradeFile.versions.add("1.1");
        upgradeFile.versions.add("1.3");
        Set<String> result = new HashSet<String>();
        upgradeFile.collectTargets("1.1", null, true, false, null, result);
        for (String tip : result) System.out.println(tip);
        Set<String> expected = new HashSet<String>();
        expected.add("1.5");
        expected.add("2.5");
        expected.add("3.2");
        Assert.assertEquals(result, expected);
        result = new HashSet<String>();
        upgradeFile.collectTargets("1.3", "2.1", true, false, null, result);
        for (String tip : result) System.out.println(tip);
        expected = new HashSet<String>();
        expected.add("2.5");
        expected.add("3.2");
        Assert.assertEquals(result, expected);
        Path path = upgradeFile.getUpgradePath("1.3", "2.1", false);
        Assert.assertEquals(path.size(), 1);
        Assert.assertEquals(path.iterator().next().getTarget(), "2.1");
    }

    public static void put(Map<String, Collection<UpgradeSegment>> map, String key, UpgradeSegment value) {
        Collection<UpgradeSegment> patches = map.get(key);
        if (patches == null) map.put(key, patches = new LinkedList<UpgradeSegment>());
        patches.add(value);
    }

    /**
	 * Tests whether the {@link UpgradeFile} returns the correct set of tip targets. This one specifies a target wildcard.
	 *
	 * @throws IOException Whenever it needs to.
	 */
    @Test
    public void testCollectTipVersions2() throws IOException {
        RandomAccessSourceReader ralr = new RandomAccessSourceReader(new FileResource("testpatch1.sql"));
        UpgradeFile upgradeFile = new UpgradeFile(ralr);
        upgradeFile.scan();
        upgradeFile.close();
        Map<String, Collection<UpgradeSegment>> patches = upgradeFile.segments = new HashMap<String, Collection<UpgradeSegment>>();
        put(patches, "1.1", new UpgradeSegment(Type.UPGRADE, "1.1", "1.2", false));
        put(patches, "1.2", new UpgradeSegment(Type.UPGRADE, "1.2", "1.3", false));
        put(patches, "1.3", new UpgradeSegment(Type.UPGRADE, "1.3", "1.4", false));
        put(patches, "1.4", new UpgradeSegment(Type.UPGRADE, "1.4", "2.1", false));
        put(patches, "2.1", new UpgradeSegment(Type.UPGRADE, "2.1", "2.2", false));
        put(patches, "2.2", new UpgradeSegment(Type.UPGRADE, "2.2", "2.3", false));
        put(patches, "2.3", new UpgradeSegment(Type.UPGRADE, "2.3", "2.4", false));
        upgradeFile.versions.add("1.1");
        Set<String> result = new HashSet<String>();
        upgradeFile.collectTargets("1.1", null, true, false, "1.", result);
        for (String tip : result) System.out.println(tip);
        Set<String> expected = new HashSet<String>();
        expected.add("1.4");
        Assert.assertEquals(result, expected);
    }

    /**
	 * Tests whether {@link UpgradeFile} returns the correct set of targets. This one has an open segment.
	 *
	 * @throws IOException Whenever it needs to.
	 */
    @Test
    public void testOpenPatch() throws IOException {
        RandomAccessSourceReader ralr = new RandomAccessSourceReader(new FileResource("testpatch1.sql"));
        UpgradeFile upgradeFile = new UpgradeFile(ralr);
        upgradeFile.close();
        Map<String, Collection<UpgradeSegment>> patches = upgradeFile.segments;
        put(patches, "1.1", new UpgradeSegment(Type.UPGRADE, "1.1", "1.2", false));
        put(patches, "1.2", new UpgradeSegment(Type.UPGRADE, "1.2", "1.3", false));
        put(patches, "1.3", new UpgradeSegment(Type.UPGRADE, "1.3", "1.4", false));
        put(patches, "1.4", new UpgradeSegment(Type.UPGRADE, "1.4", "1.5", false));
        put(patches, "1.5", new UpgradeSegment(Type.SWITCH, "1.5", "2.1", false));
        put(patches, "1.3", new UpgradeSegment(Type.UPGRADE, "1.3", "2.1", false));
        put(patches, "2.1", new UpgradeSegment(Type.UPGRADE, "2.1", "2.2", true));
        put(patches, "2.2", new UpgradeSegment(Type.UPGRADE, "2.2", "2.3", false));
        put(patches, "2.3", new UpgradeSegment(Type.UPGRADE, "2.3", "2.4", false));
        put(patches, "2.4", new UpgradeSegment(Type.UPGRADE, "2.4", "2.5", false));
        put(patches, "2.5", new UpgradeSegment(Type.SWITCH, "2.5", "3.1", false));
        put(patches, "2.3", new UpgradeSegment(Type.UPGRADE, "2.3", "3.1", false));
        put(patches, "3.1", new UpgradeSegment(Type.UPGRADE, "3.1", "3.2", false));
        upgradeFile.versions.addAll(patches.keySet());
        upgradeFile.versions.add("3.2");
        Set<String> result = new HashSet<String>();
        upgradeFile.collectTargets("1.1", null, false, false, null, result);
        for (String target : result) System.out.println(target);
        Set<String> expected = new HashSet<String>();
        expected.add("1.1");
        expected.add("1.2");
        expected.add("1.3");
        expected.add("1.4");
        expected.add("1.5");
        expected.add("2.1");
        expected.add("2.2");
        Assert.assertEquals(result, expected);
    }
}
