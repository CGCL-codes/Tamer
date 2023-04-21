package com.ail.core.document.generatedocument;

import com.ail.core.command.CommandArg;
import com.ail.core.document.model.DocumentData;
import com.ail.core.Type;
import com.ail.core.XMLString;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 * @stereotype arg
 */
public interface MergeDataArg extends CommandArg {

    /**
     * Getter for the modelArg property. This is the root of the object graph containing the data
     * to be merged.
     * @return Value of modelArg, or null if it is unset
     */
    Type getModelArg();

    /**
     * Setter for the modelArg property. 
     * @see #getModelArg
     * @param modelArg new value for property.
     */
    void setModelArg(Type modelArg);

    /**
     * Getter for the documentData property. Defines the document into which data from the model
     * must be merged.
     * @return Value of keyArg, or null if it is unset
     */
    DocumentData getDocumentDataArg();

    /**
     * Setter for the keyArg property. 
     * @see #getDocumentDataArg
     * @param keyArg new value for property.
     */
    void setDocumentDataArg(DocumentData documentDataArg);

    /**
     * Set the name of the product for which data is being merged.
     * @param productNameArg
     */
    void setProductNameArg(String productNameArg);

    /**
     * @see #setProductNameArg(String)
     * @return 
     */
    String getProductNameArg();

    /**
     * Getter for the mergedData property. The result of the merge process
     * @return Value of renderedDocumentRet, or null if it is unset
     */
    XMLString getMergedDataRet();

    /**
     * Setter for the mergedData property. 
     * @see #getMergedDataRet
     * @param renderedDocumentRet new value for property.
     */
    void setMergedDataRet(XMLString mergedDataRet);
}
