package jolie.deploy.wsdl;

import jolie.InvalidIdException;
import jolie.net.CommProtocol;

public class OutputPort extends Port {

    private OutputPortType outputPortType;

    public OutputPort(String id, OutputPortType portType, CommProtocol.Identifier protocolId) {
        super(id, protocolId);
        this.outputPortType = portType;
    }

    public OutputPortType outputPortType() {
        return outputPortType;
    }

    public static OutputPort getById(String id) throws InvalidIdException {
        Port p = Port.getById(id);
        if (!(p instanceof OutputPort)) throw new InvalidIdException(id);
        return (OutputPort) p;
    }
}
