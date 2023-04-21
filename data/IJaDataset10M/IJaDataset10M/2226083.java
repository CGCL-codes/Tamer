package tests.core;

import org.pfyshnet.core.CoreCodecInterface;
import org.pfyshnet.core.NodeHello;
import org.pfyshnet.core.RouteTransfer;
import org.pfyshnet.utils.StaticRandom;

public class TestNodeTransfer extends RouteTransfer {

    private CoreCodecInterface Core;

    public TestNodeTransfer(Object payload, NodeHello node, CoreCodecInterface core) {
        Core = core;
        setPayload(payload);
        setDestination(node);
    }

    /**
	 * The transfer was successful.
	 */
    public void Success() {
    }

    /**
	 * The transfer failed.
	 */
    public void Failure() {
        if (StaticRandom.Random.nextFloat() < 0.8) {
            Core.SendRoute(this);
        }
    }
}
