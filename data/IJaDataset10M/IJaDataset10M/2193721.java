package org.wijiscommons.cdcl.gatepoint;

import java.util.Map;
import org.wijiscommons.cdcl.Custodian;
import org.wijiscommons.semantic.SemanticEntry;

/**
 * TODO: Add Java Doc
 *
 * @author Pattabi Doraiswamy (http://pattabidoraiswamy.com)
 * @since Jan 13, 2009
 */
public interface ProblemDocumentMetaData {

    public Map<SemanticEntry, String> getProblemDocumentAttributes();

    public Custodian getPrimaryCustodian();

    public SemanticEntry getProblemSpaceIdentifier();
}
