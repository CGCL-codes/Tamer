package com.koutra.dist.proc.model;

/**
 * This is the interface that sinks that take part in a demux/mux pipeline should
 * implement.
 * 
 * @author Pafsanias Ftakas
 */
public interface ISinkTemplate extends ISink {

    /**
	 * The clone of this sink template, using the pipeline item hooked up to the template.
	 * 
	 * @param pipelineItem the pipeline item is the faucet of this sink instance.
	 */
    void createClone(IPipelineItem pipelineItem);

    /**
	 * Is this a clone?
	 * @return true iff this is a clone of a template.
	 */
    boolean isClone();
}
