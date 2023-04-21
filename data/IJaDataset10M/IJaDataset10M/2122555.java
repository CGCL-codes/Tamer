package org.gamio.conf;

import org.gamio.comm.DescriptorPool;
import org.gamio.comm.Packer;
import org.gamio.system.Context;

/**
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 23 $ $Date: 2008-10-05 21:00:52 -0400 (Sun, 05 Oct 2008) $
 */
public final class ProtocalHandlerProps {

    private String packerRef = null;

    private String descriptorRef = null;

    private Packer packer = null;

    private DescriptorPool descriptorPool = null;

    public ProtocalHandlerProps(String packerRef, String descriptorRef) {
        this.packerRef = packerRef;
        this.descriptorRef = descriptorRef;
    }

    public String getPackerRef() {
        return packerRef;
    }

    public void setPackerRef(String packerRef) {
        this.packerRef = packerRef;
    }

    public String getDescriptorRef() {
        return descriptorRef;
    }

    public void setDescriptorRef(String descriptorRef) {
        this.descriptorRef = descriptorRef;
    }

    public Packer getPacker() {
        return packer;
    }

    public DescriptorPool getDescriptorPool() {
        return descriptorPool;
    }

    public void initProtocalHandler() {
        packer = Context.getInstance().getPacker(packerRef);
        descriptorPool = Context.getInstance().getDescriptorPool(descriptorRef);
    }
}
