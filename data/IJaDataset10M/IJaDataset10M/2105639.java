package org.dctmutils.daaf.method.lifecycle;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dctmutils.common.LifecycleHelper;
import org.dctmutils.common.exception.LifecycleNotChangedException;
import org.dctmutils.daaf.DaafHelper;
import org.dctmutils.daaf.exception.DaafException;
import org.dctmutils.daaf.object.DaafMethodArguments;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfId;

/**
 * Suspend the document(s). Object type is an optional argument; if it's not
 * set, suspend all docs in workflow. If it is set, only suspend the documents
 * of that type.
 * 
 * @author <a href="mailto:luther@dctmutils.org">Luther E. Birdzell</a>
 */
public class SuspendLifecycleMethod extends LifecycleMethod {

    private static Log log = LogFactory.getLog(SuspendLifecycleMethod.class);

    /**
     * Creates a new <code>SuspendLifecycleMethod</code> instance.
     * 
     * @param helper
     * @param args
     * @throws DaafException
     */
    public SuspendLifecycleMethod(DaafHelper helper, DaafMethodArguments args) throws DaafException {
        super(helper, args);
    }

    /**
     * Suspend the document(s) in the workflow.
     * 
     * @exception DaafException
     */
    public void runMethod() throws DaafException {
        log.debug("exec: start");
        try {
            if (StringUtils.equals(packageName, WCM_SUPPORTING_PACKAGE_TYPE)) {
                suspendWcmSupportingDocument();
                return;
            }
            IDfSysObject document = helper.getDocument(packageName);
            suspend(document);
        } catch (Exception dfe) {
            log.error(dfe.getMessage(), dfe);
            throw new DaafException(dfe);
        }
    }

    /**
     * Suspend the supporting document(s) in a change set.
     * 
     * @exception DfException
     * @exception LifecycleNotChangedException
     */
    protected void suspendWcmSupportingDocument() throws DfException, LifecycleNotChangedException {
        List currentSupportingDocIds = helper.getSupportingDocumentIdsFromChangeSet();
        IDfId supportingDocId = null;
        Iterator ii = currentSupportingDocIds.iterator();
        while (ii.hasNext()) {
            supportingDocId = (IDfId) ii.next();
            IDfSysObject supportingDoc = (IDfSysObject) helper.getDfSession().getObject(supportingDocId);
            suspend(supportingDoc);
        }
    }

    /**
     * Suspend the document.
     * 
     * @param document
     * @throws DfException
     * @throws LifecycleNotChangedException
     */
    protected void suspend(IDfSysObject document) throws DfException, LifecycleNotChangedException {
        if (document != null) {
            String documentObjectType = document.getTypeName();
            if (StringUtils.equals(objectType, documentObjectType) || StringUtils.isBlank(objectType)) {
                LifecycleHelper.promoteAndSuspend(document, targetStateName);
            }
        }
    }
}
