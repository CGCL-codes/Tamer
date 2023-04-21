package org.opencms.search;

import org.opencms.db.CmsPublishedResource;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsProject;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.report.I_CmsReport;
import org.opencms.search.fields.CmsSearchField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

/**
 * Implementation for an indexer indexing VFS Cms resources.<p>
 * 
 * @author Carsten Weinholz 
 * @author Thomas Weckert  
 * 
 * @version $Revision: 1.37 $ 
 * 
 * @since 6.0.0 
 */
public class CmsVfsIndexer implements I_CmsIndexer {

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsVfsIndexer.class);

    /** The OpenCms user context to use when reading resources from the VFS during indexing. */
    private CmsObject m_cms;

    /** The index. */
    private CmsSearchIndex m_index;

    /** The report. */
    private I_CmsReport m_report;

    /**
     * @see org.opencms.search.I_CmsIndexer#deleteResources(org.apache.lucene.index.IndexReader, java.util.List)
     */
    public void deleteResources(IndexReader reader, List resourcesToDelete) {
        if ((resourcesToDelete == null) || resourcesToDelete.isEmpty()) {
            return;
        }
        List resourcesAlreadyDeleted = new ArrayList(resourcesToDelete.size());
        Iterator i = resourcesToDelete.iterator();
        while (i.hasNext()) {
            CmsPublishedResource res = (CmsPublishedResource) i.next();
            String rootPath = res.getRootPath();
            if (!resourcesAlreadyDeleted.contains(rootPath)) {
                resourcesAlreadyDeleted.add(rootPath);
                Term term = new Term(CmsSearchField.FIELD_PATH, rootPath);
                try {
                    reader.deleteDocuments(term);
                } catch (IOException e) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn(Messages.get().getBundle().key(Messages.LOG_IO_INDEX_DOCUMENT_DELETE_2, rootPath, m_index.getName()), e);
                    }
                }
            }
        }
    }

    /**
     * @see org.opencms.search.I_CmsIndexer#getUpdateData(org.opencms.search.CmsSearchIndexSource, java.util.List)
     */
    public CmsSearchIndexUpdateData getUpdateData(CmsSearchIndexSource source, List publishedResources) {
        CmsSearchIndexUpdateData result = new CmsSearchIndexUpdateData(source, this);
        Iterator i = publishedResources.iterator();
        while (i.hasNext()) {
            CmsPublishedResource resource = (CmsPublishedResource) i.next();
            if (!resource.getStructureId().isNullUUID()) {
                if (CmsProject.isInsideProject(source.getResourcesNames(), resource.getRootPath())) {
                    if (resource.getState().isNew()) {
                        if (isResourceInTimeWindow(resource)) {
                            result.addResourceToUpdate(resource);
                        }
                    } else if (resource.getState().isDeleted()) {
                        result.addResourceToDelete(resource);
                    } else if (resource.getState().isChanged() || resource.getState().isUnchanged()) {
                        result.addResourceToDelete(resource);
                        if (isResourceInTimeWindow(resource)) {
                            result.addResourceToUpdate(resource);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * @see org.opencms.search.I_CmsIndexer#newInstance(org.opencms.file.CmsObject, org.opencms.report.I_CmsReport, org.opencms.search.CmsSearchIndex)
     */
    public I_CmsIndexer newInstance(CmsObject cms, I_CmsReport report, CmsSearchIndex index) {
        CmsVfsIndexer indexer = new CmsVfsIndexer();
        indexer.m_cms = cms;
        indexer.m_report = report;
        indexer.m_index = index;
        return indexer;
    }

    /**
     * @see org.opencms.search.I_CmsIndexer#rebuildIndex(org.apache.lucene.index.IndexWriter, org.opencms.search.CmsIndexingThreadManager, org.opencms.search.CmsSearchIndexSource)
     */
    public void rebuildIndex(IndexWriter writer, CmsIndexingThreadManager threadManager, CmsSearchIndexSource source) throws CmsIndexException {
        List resourceNames = source.getResourcesNames();
        Iterator i = resourceNames.iterator();
        while (i.hasNext()) {
            String resourceName = (String) i.next();
            List resources = null;
            try {
                resources = m_cms.readResources(resourceName, CmsResourceFilter.DEFAULT.addRequireFile());
            } catch (CmsException e) {
                if (m_report != null) {
                    m_report.println(Messages.get().container(Messages.RPT_UNABLE_TO_READ_SOURCE_2, resourceName, e.getLocalizedMessage()), I_CmsReport.FORMAT_WARNING);
                }
                if (LOG.isWarnEnabled()) {
                    LOG.warn(Messages.get().getBundle().key(Messages.LOG_UNABLE_TO_READ_SOURCE_2, resourceName, m_index.getName()), e);
                }
            }
            if (resources != null) {
                Iterator j = resources.iterator();
                while (j.hasNext()) {
                    CmsResource resource = (CmsResource) j.next();
                    updateResource(writer, threadManager, resource);
                }
            }
        }
    }

    /**
     * @see org.opencms.search.I_CmsIndexer#updateResources(org.apache.lucene.index.IndexWriter, org.opencms.search.CmsIndexingThreadManager, java.util.List)
     */
    public void updateResources(IndexWriter writer, CmsIndexingThreadManager threadManager, List resourcesToUpdate) throws CmsIndexException {
        if ((resourcesToUpdate == null) || resourcesToUpdate.isEmpty()) {
            return;
        }
        List resourcesAlreadyUpdated = new ArrayList(resourcesToUpdate.size());
        Iterator i = resourcesToUpdate.iterator();
        while (i.hasNext()) {
            CmsPublishedResource res = (CmsPublishedResource) i.next();
            CmsResource resource = null;
            try {
                resource = m_cms.readResource(res.getRootPath());
            } catch (CmsException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn(Messages.get().getBundle().key(Messages.LOG_UNABLE_TO_READ_RESOURCE_2, res.getRootPath(), m_index.getName()), e);
                }
            }
            if (resource != null) {
                if (!resourcesAlreadyUpdated.contains(resource.getRootPath())) {
                    resourcesAlreadyUpdated.add(resource.getRootPath());
                    updateResource(writer, threadManager, resource);
                }
            }
        }
    }

    /**
     * Checks if the published resource is inside the time window set with release and expiration date.<p>
     * 
     * @param resource the published resource to check
     * @return true if the published resource is inside the time window, otherwise false
     */
    protected boolean isResourceInTimeWindow(CmsPublishedResource resource) {
        return m_cms.existsResource(m_cms.getRequestContext().removeSiteRoot(resource.getRootPath()), CmsResourceFilter.DEFAULT);
    }

    /**
     * Updates (writes) a single resource in the index.<p>
     * 
     * @param writer the index writer to use
     * @param threadManager the thread manager to use when extracting the document text
     * @param resource the resource to update
     * 
     * @throws CmsIndexException if something goes wrong
     */
    protected void updateResource(IndexWriter writer, CmsIndexingThreadManager threadManager, CmsResource resource) throws CmsIndexException {
        if (resource.isInternal()) {
            return;
        }
        try {
            if (m_report != null) {
                m_report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_SUCCESSION_1, String.valueOf(threadManager.getCounter() + 1)), I_CmsReport.FORMAT_NOTE);
                m_report.print(Messages.get().container(Messages.RPT_SEARCH_INDEXING_FILE_BEGIN_0), I_CmsReport.FORMAT_NOTE);
                m_report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_ARGUMENT_1, m_report.removeSiteRoot(resource.getRootPath())));
                m_report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_DOTS_0), I_CmsReport.FORMAT_DEFAULT);
            }
            threadManager.createIndexingThread(m_cms, writer, resource, m_index, m_report);
        } catch (Exception e) {
            if (m_report != null) {
                m_report.println(Messages.get().container(Messages.RPT_SEARCH_INDEXING_FAILED_0), I_CmsReport.FORMAT_WARNING);
            }
            if (LOG.isWarnEnabled()) {
                LOG.warn(Messages.get().getBundle().key(Messages.ERR_INDEX_RESOURCE_FAILED_2, resource.getRootPath(), m_index.getName()), e);
            }
            throw new CmsIndexException(Messages.get().container(Messages.ERR_INDEX_RESOURCE_FAILED_2, resource.getRootPath(), m_index.getName()));
        }
    }
}
