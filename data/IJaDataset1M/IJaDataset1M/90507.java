package net.sf.istcontract.wsimport.transport;

import com.sun.istack.NotNull;
import javax.xml.ws.BindingProvider;
import net.sf.istcontract.wsimport.api.EndpointAddress;
import net.sf.istcontract.wsimport.api.message.Packet;
import net.sf.istcontract.wsimport.api.pipe.*;
import net.sf.istcontract.wsimport.api.pipe.helper.AbstractTubeImpl;

/**
 * Proxy transport {@link Tube} and {@link Pipe} that lazily determines the
 * actual transport pipe by looking at {@link Packet#endpointAddress}.
 *
 * <p>
 * This pseudo transport is used when there's no statically known endpoint address,
 * and thus it's expected that the application will configure {@link BindingProvider}
 * at runtime before making invocation.
 *
 * <p>
 * Since a typical application makes multiple invocations with the same endpoint
 * address, this class implements a simple cache strategy to avoid re-creating
 * transport pipes excessively.
 *
 * @author Kohsuke Kawaguchi
 */
public final class DeferredTransportPipe extends AbstractTubeImpl {

    private Tube transport;

    private EndpointAddress address;

    private final ClassLoader classLoader;

    private final ClientTubeAssemblerContext context;

    public DeferredTransportPipe(ClassLoader classLoader, ClientPipeAssemblerContext context) {
        this(classLoader, new ClientTubeAssemblerContext(context.getAddress(), context.getWsdlModel(), context.getService(), context.getBinding(), context.getContainer()));
    }

    public DeferredTransportPipe(ClassLoader classLoader, ClientTubeAssemblerContext context) {
        this.classLoader = classLoader;
        this.context = context;
    }

    public NextAction processException(@NotNull Throwable t) {
        return transport.processException(t);
    }

    public NextAction processRequest(@NotNull Packet request) {
        if (request.endpointAddress == address) return transport.processRequest(request);
        if (transport != null) {
            transport.preDestroy();
            transport = null;
            address = null;
        }
        ClientTubeAssemblerContext newContext = new ClientTubeAssemblerContext(request.endpointAddress, context.getWsdlModel(), context.getService(), context.getBinding(), context.getContainer(), context.getCodec().copy());
        address = request.endpointAddress;
        transport = TransportTubeFactory.create(classLoader, newContext);
        assert transport != null;
        return transport.processRequest(request);
    }

    public NextAction processResponse(@NotNull Packet response) {
        return transport.processResponse(response);
    }

    public void preDestroy() {
        if (transport != null) {
            transport.preDestroy();
            transport = null;
            address = null;
        }
    }

    public DeferredTransportPipe copy(TubeCloner cloner) {
        DeferredTransportPipe copy = new DeferredTransportPipe(classLoader, context);
        cloner.add(this, copy);
        if (transport != null) {
            copy.transport = ((PipeCloner) cloner).copy(this.transport);
            copy.address = this.address;
        }
        return copy;
    }
}
