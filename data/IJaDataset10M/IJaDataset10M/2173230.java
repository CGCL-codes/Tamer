package gumbo.net;

public class NetProps {

    private NetProps() {
    }

    /**
	 * If present and true, enables debugging of the life cycle of network
	 * resources in the net framework.
	 */
    public static final String DEBUG_NET_LIFE = "gumbo.net.NetProps.DEBUG_NET_LIFE";

    /**
	 * If present and true, enables debugging of the read and write thread locks
	 * in the net message framework.
	 */
    public static final String DEBUG_NET_LOCKS = "gumbo.net.NetProps.DEBUG_NET_LOCKS";

    /**
	 * If present and true, enables debugging of the network routing in the net
	 * message framework.
	 */
    public static final String DEBUG_NET_ROUTE = "gumbo.net.NetProps.DEBUG_NET_ROUTE";

    /**
	 * If present and true, enables reporting of the network read stream (remote
	 * to local). This only applies to wire formats that are human readable,
	 * such as JSON.
	 */
    public static final String DEBUG_NET_READ = "gumbo.net.NetProps.DEBUG_NET_READ";

    /**
	 * If present and true, enables reporting of the network write stream (local
	 * to remote). This only applies to wire formats that are human readable,
	 * such as JSON.
	 */
    public static final String DEBUG_NET_WRITE = "gumbo.net.NetProps.DEBUG_NET_WRITE";
}
