package org.orbeon.oxf.processor.xmldb;

import org.orbeon.oxf.pipeline.api.PipelineContext;
import org.orbeon.oxf.processor.ProcessorImpl;
import org.orbeon.oxf.processor.ProcessorInputOutputInfo;
import org.orbeon.oxf.processor.ProcessorOutput;
import org.xml.sax.ContentHandler;

/**
 *
 */
public class XMLDBQueryProcessor extends XMLDBProcessor {

    public XMLDBQueryProcessor() {
        addInputInfo(new ProcessorInputOutputInfo(INPUT_DATASOURCE, XMLDB_DATASOURCE_URI));
        addInputInfo(new ProcessorInputOutputInfo(INPUT_QUERY, XMLDB_QUERY_URI));
        addOutputInfo(new ProcessorInputOutputInfo(OUTPUT_DATA));
    }

    public ProcessorOutput createOutput(String name) {
        ProcessorOutput output = new ProcessorImpl.ProcessorOutputImpl(getClass(), name) {

            public void readImpl(PipelineContext pipelineContext, ContentHandler contentHandler) {
                executeOperation(pipelineContext, contentHandler);
            }
        };
        addOutput(name, output);
        return output;
    }
}
