package net.sourceforge.blogentis.plugins.xmlrpc;

import org.apache.xmlrpc.XmlRpcServer;
import net.sourceforge.blogentis.plugins.IExtension;

/**
 * Extensions to the XML-RPC entry point will be declared by extensions
 * conforming to this interface.
 * 
 * @author abas
 */
public interface IRPCExtension extends IExtension {

    /**
     * This method will add any handlers to the RPC object that this
     * extension/plugin accepts. THese handlers are global, for all blogs. it is
     * recommended to also have a per-blog configuration page that will
     * configure the extension on a per-blog basis.
     * 
     * @param rpc
     */
    public void setupRPC(XmlRpcServer rpc);
}
