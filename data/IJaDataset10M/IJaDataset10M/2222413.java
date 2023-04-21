package org.orbeon.oxf.processor.xmldb;

import org.orbeon.oxf.pipeline.api.PipelineContext;
import org.orbeon.oxf.processor.ProcessorInputOutputInfo;

/**
 *
 */
public class XMLDBDeleteProcessor extends XMLDBProcessor {

    public XMLDBDeleteProcessor() {
        addInputInfo(new ProcessorInputOutputInfo(INPUT_DATASOURCE, XMLDB_DATASOURCE_URI));
        addInputInfo(new ProcessorInputOutputInfo(INPUT_QUERY, XMLDB_QUERY_URI));
    }

    public void start(PipelineContext pipelineContext) {
        executeOperation(pipelineContext, null);
    }
}
