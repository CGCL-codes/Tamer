package org.waveprotocol.wave.concurrencycontrol.client;

import junit.framework.TestCase;
import org.waveprotocol.wave.concurrencycontrol.common.DeltaPair;
import org.waveprotocol.wave.model.document.operation.impl.DocOpBuilder;
import org.waveprotocol.wave.model.operation.OpComparators;
import org.waveprotocol.wave.model.operation.TransformException;
import org.waveprotocol.wave.model.operation.wave.BlipContentOperation;
import org.waveprotocol.wave.model.operation.wave.VersionUpdateOp;
import org.waveprotocol.wave.model.operation.wave.WaveletBlipOperation;
import org.waveprotocol.wave.model.operation.wave.WaveletOperation;
import org.waveprotocol.wave.model.testing.DeltaTestUtil;
import org.waveprotocol.wave.model.util.CollectionUtils;
import org.waveprotocol.wave.model.version.HashedVersion;
import java.util.List;

/**
 * Test we transform a delta pair correctly.
 *
 * @author zdwang@google.com (David Wang)
 */
public class DeltaPairTest extends TestCase {

    private static final DeltaTestUtil CLIENT_UTIL = new DeltaTestUtil("client@example.com");

    private static final DeltaTestUtil SERVER_UTIL = new DeltaTestUtil("server@example.com");

    /**
   * Test multiple server and client ops
   * @throws TransformException
   */
    public void testMultipleClientServerOps() throws TransformException {
        List<WaveletOperation> client = CollectionUtils.newArrayList();
        client.add(CLIENT_UTIL.insert(1, "A", 1, null));
        client.add(CLIENT_UTIL.insert(3, "B", 0, null));
        List<WaveletOperation> server = CollectionUtils.newArrayList();
        server.add(SERVER_UTIL.insert(2, "1", 0, null));
        server.add(SERVER_UTIL.insert(1, "2", 2, null));
        DeltaPair pair = new DeltaPair(client, server);
        pair = pair.transform();
        assertEquals(2, pair.getClient().size());
        checkInsert(pair.getClient().get(0), 1, "A", 3);
        checkInsert(pair.getClient().get(1), 4, "B", 1);
        assertEquals(2, pair.getServer().size());
        checkInsert(pair.getServer().get(0), 4, "1", 0);
        checkInsert(pair.getServer().get(1), 2, "2", 3);
    }

    private void checkInsert(WaveletOperation operation, int location, String content, int remaining) {
        if (operation instanceof WaveletBlipOperation) {
            WaveletBlipOperation waveOp = (WaveletBlipOperation) operation;
            if (waveOp.getBlipOp() instanceof BlipContentOperation) {
                BlipContentOperation blipOp = (BlipContentOperation) waveOp.getBlipOp();
                DocOpBuilder builder = new DocOpBuilder();
                builder.retain(location).characters(content);
                if (remaining > 0) {
                    builder.retain(remaining);
                }
                assertTrue(OpComparators.SYNTACTIC_IDENTITY.equal(builder.build(), blipOp.getContentOp()));
                return;
            }
        }
        fail("Did not get an insertion operation.");
    }

    /**
   * Simple test for deltas that have the same operations and the same author.
   * @throws TransformException
   */
    public void testIsSame() throws TransformException {
        List<WaveletOperation> client = CollectionUtils.newArrayList();
        client.add(CLIENT_UTIL.insert(1, "A", 1, null));
        client.add(CLIENT_UTIL.insert(3, "B", 0, null));
        HashedVersion resultingVersion = HashedVersion.of(1L, new byte[] { 1, 2, 3, 4 });
        List<WaveletOperation> server = CollectionUtils.newArrayList();
        server.add(CLIENT_UTIL.insert(1, "A", 1, null));
        server.add(CLIENT_UTIL.insert(3, "B", 0, resultingVersion));
        assertTrue(DeltaPair.areSame(client, server));
        DeltaPair pair = new DeltaPair(client, server);
        pair = pair.transform();
        assertEquals(0, pair.getClient().size());
        assertEquals(2, pair.getServer().size());
        checkVersionUpdate(pair.getServer().get(0), 1, null);
        checkVersionUpdate(pair.getServer().get(1), 1, resultingVersion);
    }

    private void checkVersionUpdate(WaveletOperation operation, long versionIncrement, HashedVersion distinctVersion) {
        assertTrue(operation instanceof VersionUpdateOp);
        VersionUpdateOp vop = (VersionUpdateOp) operation;
        assertEquals(versionIncrement, vop.getContext().getVersionIncrement());
        assertEquals(distinctVersion, vop.getContext().getHashedVersion());
    }
}
