package org.orbeon.oxf.processor;

import org.xml.sax.ContentHandler;

public interface ProcessorReader {

    public void read(org.orbeon.oxf.pipeline.api.PipelineContext context, ContentHandler contentHandler);
}
