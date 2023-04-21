package org.creativor.rayson.transport.server.transfer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.creativor.rayson.api.TransferArgument;
import org.creativor.rayson.api.TransferSocket;

/**
 * A call of transfer protocol.
 * 
 * @author Nick Zhang
 */
class TransferCall {

    private TransferSocket transferSocket;

    private TransferInvoker invoker;

    private TransferArgument argument;

    /**
	 * Instantiates a new transfer call.
	 * 
	 * @param invoker
	 *            the invoker
	 * @param argument
	 *            the argument
	 * @param transferSocket
	 *            the transfer socket
	 */
    public TransferCall(TransferInvoker invoker, TransferArgument argument, TransferSocket transferSocket) {
        this.transferSocket = transferSocket;
        this.invoker = invoker;
        this.argument = argument;
    }

    /**
	 * Process this call, finally close the transfer socket.
	 * 
	 * @throws TransferCallException
	 *             If invoke this call error.
	 */
    public void process() throws TransferCallException {
        try {
            this.invoker.invoke(this.argument, this.transferSocket);
        } catch (Exception e) {
            Throwable throwable = e;
            if (e instanceof InvocationTargetException) {
                throwable = ((InvocationTargetException) e).getTargetException();
            }
            throw new TransferCallException(throwable);
        } finally {
            try {
                this.transferSocket.close();
            } catch (IOException e) {
                throw new TransferCallException(e);
            }
        }
    }

    /**
	 * To string.
	 * 
	 * @return the string
	 */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("argument: ");
        sb.append(argument.toString());
        sb.append(", ");
        sb.append("transfer code: ");
        sb.append(this.invoker.getCode());
        sb.append("}");
        return sb.toString();
    }
}
