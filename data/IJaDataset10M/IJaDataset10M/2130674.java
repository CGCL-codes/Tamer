package fr.esrf.tangoatk.core;

import java.util.EventListener;

public interface IErrorHandler extends EventListener {

    public void connectionException(ConnectionException e);

    public void attributeErrorException(AttributeErrorException e);

    public void attributeReadException(AttributeReadException e);

    public void attributeSetException(AttributeSetException e);

    public void commandExecuteException(CommandExecuteException e);

    public void unknownException(Exception e);
}
