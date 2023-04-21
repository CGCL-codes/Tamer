package ecskernel.coordinate.dcop.messages;

import ecskernel.coordinate.IMessage;
import ecskernel.coordinate.dcop.objects.IContext;
import ecskernel.coordinate.dcop.objects.IDCOPVariable;

public interface IValue extends IMessage {

    public IContext getContext();

    public IDCOPVariable getVariable();

    public Number getValue();
}
