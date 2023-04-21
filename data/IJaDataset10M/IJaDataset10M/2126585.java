package org.mobicents.protocols.sctp;

import java.nio.channels.spi.AbstractSelectableChannel;

/**
 * @author amit bhayani
 * 
 */
public final class ChangeRequest {

    public static final int REGISTER = 1;

    public static final int CHANGEOPS = 2;

    public static final int CONNECT = 3;

    public static final int CLOSE = 4;

    private int type;

    private int ops;

    private AbstractSelectableChannel socketChannel;

    private AssociationImpl association;

    private long executionTime;

    protected ChangeRequest(AbstractSelectableChannel socketChannel, AssociationImpl association, int type, int ops) {
        this.type = type;
        this.ops = ops;
        this.socketChannel = socketChannel;
        this.association = association;
    }

    protected ChangeRequest(AssociationImpl association, int type, long executionTime) {
        this(null, association, type, -1);
        this.executionTime = executionTime;
    }

    /**
	 * @return the type
	 */
    protected int getType() {
        return type;
    }

    /**
	 * @return the ops
	 */
    protected int getOps() {
        return ops;
    }

    /**
	 * @return the socketChannel
	 */
    protected AbstractSelectableChannel getSocketChannel() {
        return socketChannel;
    }

    /**
	 * @return the association
	 */
    protected AssociationImpl getAssociation() {
        return association;
    }

    /**
	 * @return the executionTime
	 */
    protected long getExecutionTime() {
        return executionTime;
    }
}
