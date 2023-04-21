package radius.server.filter;

import java.net.InetSocketAddress;
import radius.RadiusPacket;
import radius.chain.FilterChain;
import radius.server.RadiusContext;
import radius.server.RadiusFilter;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class IdFilter extends AbstractRadiusFilter {

    private IdentifierHolder idHolder;

    public IdFilter() {
        super(RadiusFilter.AFTER_CHECK_TYPE);
        idHolder = new IdentifierHolder(Integer.MAX_VALUE);
    }

    protected void doRadiusFilter(RadiusContext context, FilterChain chain) {
        RadiusPacket source = context.getSourcePacket();
        InetSocketAddress address = (InetSocketAddress) context.getSourceAddress();
        String ip = address.getAddress().getHostAddress();
        int port = address.getPort();
        int id = source.getIdentifier();
        if (idHolder.hasIdentifier(ip, port, id)) {
            log.warn("repeat packet,sourceaddress=" + address + ",id=" + id);
            return;
        } else {
            chain.doFilter(context);
        }
    }
}
