package org.privale.utils.network.test;

import org.privale.utils.network.ChannelProcessor;
import org.privale.utils.network.ProcessorPool;

public class ClientProcessor extends ChannelProcessor {

    public ClientProcessor(ProcessorPool p) {
        super(p);
    }

    public void Init() {
    }

    public void Process() {
    }

    public boolean isComplete() {
        return false;
    }
}
