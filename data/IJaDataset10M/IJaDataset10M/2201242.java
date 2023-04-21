package org.dctmutils.daaf.method;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dctmutils.common.ContentHelper;
import org.dctmutils.daaf.DaafHelper;
import org.dctmutils.daaf.exception.DaafException;
import org.dctmutils.daaf.object.DaafMethodArguments;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.IDfId;

/**
 * Swap the primary content for the specified rendition if it exist.
 * If the 'renditionFormat' argument is not specified this method will swap the primary 
 * content with the first rendition if it exists.  This method will attempt
 * to perform this process for all documents in the workflow unless the 
 * 'packageName' argument is specified.
 * 
 * @author <a href="mailto:luther@dctmutils.org">Luther E. Birdzell</a>
 */
public class SwapContentForRenditionMethod extends DaafMethodBase {

    private static Log log = LogFactory.getLog(SwapContentForRenditionMethod.class);

    /**
     * Method argument (optional).
     */
    public static final String RENDITION_FORMAT_ARG = "renditionFormat";

    /**
     * Method argument (optional).
     */
    public static final String PACKAGE_NAME_ARG = "packageName";

    /**
     * 
     */
    protected String packageName = null;

    /**
     * 
     */
    protected String renditionFormat = null;

    /**
     * Creates a new <code>SetAttributeMethod</code> instance.
     *
     * @param helper
     * @param args
     * @throws DaafException
     */
    public SwapContentForRenditionMethod(DaafHelper helper, DaafMethodArguments args) throws DaafException {
        super(helper);
        log.debug("DeleteDocumentMethod called with: " + args.toString());
        packageName = getArgumentValue(args, PACKAGE_NAME_ARG);
        renditionFormat = getArgumentValue(args, RENDITION_FORMAT_ARG);
    }

    /**
     * Swap the content for the specified rendition.
     *
     * @throws DaafException
     */
    public void runMethod() throws DaafException {
        try {
            IDfSysObject document = null;
            IDfId documentObjectId = null;
            List documentIds = null;
            if (StringUtils.isBlank(packageName)) {
                documentIds = helper.getAllDocumentIds();
            } else {
                document = helper.getDocument(packageName);
                if (document != null) {
                    documentIds = new ArrayList();
                    documentIds.add(document.getObjectId());
                }
            }
            if (CollectionUtils.isNotEmpty(documentIds)) {
                Iterator ii = documentIds.iterator();
                while (ii.hasNext()) {
                    documentObjectId = (IDfId) ii.next();
                    document = (IDfDocument) helper.getDfSession().getObject(documentObjectId);
                    if (StringUtils.isBlank(renditionFormat)) {
                        ContentHelper.swapContentForFirstRendition(helper.getDfSession(), documentObjectId.getId());
                    } else {
                        ContentHelper.swapContentForRendition(helper.getDfSession(), documentObjectId.getId(), renditionFormat);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DaafException(e);
        }
    }
}
