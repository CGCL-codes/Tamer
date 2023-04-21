package org.embl.ebi.analysis;

/**
 * An interface to metadata describing behaviour and possible
 * look-and-feel for any analysis (an analysis is a remote program,
 * often, but not necessarily, a command-line program). <p>
 *
 * This interface does not address the problem where to get metadata
 * from (for example by reading and parsing an XML file). It is up to
 * the implementation of this interface to solve it. The expected
 * scenario is that an XML string or a URL pointing to an XML source
 * will be given to a constructor of an implementation. <p>
 *
 * @author <A HREF="mailto:senger@ebi.ac.uk">Martin Senger</A>
 * @version $Id: AnalysisMetadataAccessor.java,v 1.1.1.1 2005/06/15 09:00:19 marsenger Exp $
 */
public interface AnalysisMetadataAccessor {

    AnalysisDef getAnalysisDef() throws AnalysisException;

    EventDef getEventDef() throws AnalysisException;

    ParamDef[] getParamDefs() throws AnalysisException;

    InputPropertyDef[] getInputDefs() throws AnalysisException;

    OutputPropertyDef[] getOutputDefs() throws AnalysisException;

    /******************************************************************************
     * Return the whole metadata.
     * It raises an exception when metadata does not exist at all.
     ******************************************************************************/
    public String describe() throws AnalysisException;
}
