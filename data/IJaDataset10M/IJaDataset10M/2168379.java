package org.ourgrid.peer.to;

import java.util.List;
import org.ourgrid.reqtrace.Req;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;

/**
 */
public abstract class Consumer {

    private DeploymentID consumerID;

    private String publicKey;

    private Object consumerStub;

    public abstract Priority getPriority();

    public abstract List<AllocableWorker> getAllocableWorkers();

    public abstract void removeAllocableWorker(AllocableWorker allocableWorker);

    /**
	 * Verifies if the consumer is local.
	 * @return <code>TRUE</code> if it is local, <code>FALSE</code> otherwise
	 */
    public abstract boolean isLocal();

    /**
	 * Get the EntityID of the consumer of this <code>AllocableWorker</code>
	 * @return
	 */
    @Req("REQ038a")
    public DeploymentID getDeploymentID() {
        return this.consumerID;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    /**
	 * @return the consumerStub
	 */
    public Object getConsumerStub() {
        return consumerStub;
    }

    /**
	 * @param id
	 */
    public void setConsumerStub(DeploymentID id, Object consumerStub) {
        this.consumerID = id;
        this.publicKey = consumerID.getPublicKey();
        this.consumerStub = consumerStub;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((consumerID == null) ? 0 : consumerID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Consumer other = (Consumer) obj;
        if (consumerID == null) {
            if (other.consumerID != null) return false;
        } else if (!consumerID.equals(other.consumerID)) return false;
        return true;
    }

    public abstract Class<?> getConsumerType();
}
