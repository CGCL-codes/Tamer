package uk.ac.reload.straker.editors;

import uk.ac.reload.straker.datamodel.AbstractEntry;

/**
 * Interface for EditorInput
 * 
 * @author Phillip Beauvoir
 * @version $Id: IStrakerEditorInput.java,v 1.2 2006/07/10 11:50:54 phillipus Exp $
 */
public interface IStrakerEditorInput {

    /**
     * @return The AbstractEntry associated with the Editor
     */
    AbstractEntry getAbstractEntry();
}
