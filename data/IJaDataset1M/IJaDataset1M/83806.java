package nl.utwente.ewi.stream.network.attributes.triggers;

import java.util.logging.Level;
import java.util.logging.Logger;
import nl.utwente.ewi.stream.network.AbstractPE;
import nl.utwente.ewi.stream.network.QueryNetworkManager;
import nl.utwente.ewi.stream.network.attributes.BufferData;
import nl.utwente.ewi.stream.network.attributes.Trigger;

public class OfflineTrigger extends Trigger {

    private static final Logger logger = Logger.getLogger(OfflineTrigger.class.getName());

    @Override
    public String getTriggerPredicate() {
        return null;
    }

    @Override
    public void setProcessingElement(AbstractPE processingElement) {
        super.setProcessingElement(processingElement);
    }

    @Override
    public String getTriggerType() {
        return "Offline";
    }

    @Override
    public TriggerType getType() {
        return Trigger.TriggerType.OFFLINE;
    }

    @Override
    public void activate() {
        super.activate();
        final AbstractPE pe = getProcessingElement();
        if (pe != null) {
            final BufferData[] clonedBuffers = pe.getBuffersBufferData();
            if (clonedBuffers != null && clonedBuffers.length > 0) {
                QueryNetworkManager.pool.execute(new Runnable() {

                    public void run() {
                        logger.log(Level.FINEST, "Triggering process '" + pe.name + "'");
                        pe.triggerProcess(clonedBuffers);
                    }
                });
            } else logger.log(Level.INFO, "Offline trigger not executed: the buffer was empty.");
        } else logger.log(Level.INFO, "Offline trigger not executed: no associated processing element found.");
    }
}
