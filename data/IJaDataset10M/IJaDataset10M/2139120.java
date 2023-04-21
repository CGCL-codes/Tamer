package com.koutra.dist.proc.pipeline;

import java.util.Arrays;

/**
 * A pipeline item that reads from a byte stream and writes to a byte stream,
 * performing no transformation.
 * 
 * @author Pafsanias Ftakas
 */
public class IdentityStreamToStreamPipelineItem extends StreamToStreamPipelineItem {

    /**
	 * @deprecated Use any of the initializing constructors instead.
	 */
    public IdentityStreamToStreamPipelineItem() {
    }

    /**
	 * Initializing constructor.
	 * @param id the ID of the pipeline item.
	 */
    public IdentityStreamToStreamPipelineItem(String id) {
        super(id);
    }

    /**
	 * Implementation of the StreamToStreamPipelineItem interface for the stream
	 * transformation.
	 */
    @Override
    public byte[] transformBuffer(byte[] buffer, int off, int len) {
        if (len == -1) return new byte[0];
        return Arrays.copyOfRange(buffer, off, off + len);
    }
}
