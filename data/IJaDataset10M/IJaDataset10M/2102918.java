package gov.sns.xal.smf;

public interface DeltaAccelEvent {

    public void accelCreated(Accelerator p_source);

    public void nodeAdded(Accelerator p_source, AcceleratorNode p_node);

    public void nodeRemoved(Accelerator p_source, AcceleratorNode p_node);

    public void seqAdded(Accelerator p_source, AcceleratorSeq p_seq);

    public void seqRemoved(Accelerator p_source, AcceleratorSeq p_seq);
}
