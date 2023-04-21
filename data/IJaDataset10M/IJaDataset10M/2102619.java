package net.sf.istcontract.wsimport.api.pipe;

import com.sun.istack.NotNull;
import java.io.PrintStream;
import net.sf.istcontract.wsimport.api.EndpointAddress;
import net.sf.istcontract.wsimport.api.WSBinding;
import net.sf.istcontract.wsimport.api.WSService;
import net.sf.istcontract.wsimport.api.model.wsdl.WSDLPort;
import net.sf.istcontract.wsimport.api.pipe.helper.PipeAdapter;
import net.sf.istcontract.wsimport.api.server.Container;

/**
 * Factory for well-known {@link Pipe} implementations
 * that the {@link PipelineAssembler} needs to use
 * to satisfy JAX-WS requirements.
 *
 * @author Kohsuke Kawaguchi
 * @deprecated Use {@link ClientTubeAssemblerContext}.
 */
public final class ClientPipeAssemblerContext extends ClientTubeAssemblerContext {

    public ClientPipeAssemblerContext(@NotNull EndpointAddress address, @NotNull WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding) {
        this(address, wsdlModel, rootOwner, binding, Container.NONE);
    }

    public ClientPipeAssemblerContext(@NotNull EndpointAddress address, @NotNull WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding, @NotNull Container container) {
        super(address, wsdlModel, rootOwner, binding, container);
    }

    /**
     * creates a {@link Pipe} that dumps messages that pass through.
     */
    public Pipe createDumpPipe(String name, PrintStream out, Pipe next) {
        return PipeAdapter.adapt(super.createDumpTube(name, out, PipeAdapter.adapt(next)));
    }

    /**
     * Creates a {@link Pipe} that performs WS-Addressig processing.
     * This pipe should be before {@link net.sf.istcontract.wsimport.protocol.soap.ClientMUTube}.
     */
    public Pipe createWsaPipe(Pipe next) {
        return PipeAdapter.adapt(super.createWsaTube(PipeAdapter.adapt(next)));
    }

    /**
     * Creates a {@link Pipe} that performs SOAP mustUnderstand processing.
     * This pipe should be before HandlerPipes.
     */
    public Pipe createClientMUPipe(Pipe next) {
        return PipeAdapter.adapt(super.createClientMUTube(PipeAdapter.adapt(next)));
    }

    /**
     * creates a {@link Pipe} that validates messages against schema
     */
    public Pipe createValidationPipe(Pipe next) {
        return PipeAdapter.adapt(super.createValidationTube(PipeAdapter.adapt(next)));
    }

    /**
     * Creates a {@link Pipe} that invokes protocol and logical handlers.
     */
    public Pipe createHandlerPipe(Pipe next) {
        return PipeAdapter.adapt(super.createHandlerTube(PipeAdapter.adapt(next)));
    }

    /**
     * Creates a {@link Tube} that adds container specific security
     */
    @NotNull
    public Pipe createSecurityPipe(@NotNull Pipe next) {
        return PipeAdapter.adapt(super.createSecurityTube(PipeAdapter.adapt(next)));
    }

    /**
     * Creates a transport pipe (for client), which becomes the terminal pipe.
     */
    public Pipe createTransportPipe() {
        return PipeAdapter.adapt(super.createTransportTube());
    }
}
