package org.eclipse.help.internal.search;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.help.ITocContribution;
import org.eclipse.help.ITopic;
import org.eclipse.help.internal.HelpPlugin;
import org.eclipse.help.internal.base.BaseHelpSystem;
import org.eclipse.help.internal.base.HelpBasePlugin;
import org.eclipse.help.internal.base.HelpBaseResources;
import org.eclipse.help.internal.base.util.HelpProperties;
import org.eclipse.help.internal.protocols.HelpURLConnection;
import org.eclipse.help.internal.toc.Toc;
import org.eclipse.help.internal.toc.TocFileProvider;
import org.eclipse.help.search.LuceneSearchParticipant;

/**
 * Indexing Operation represents a long operation, which performs indexing of
 * the group (Collection) of documents. It is used Internally by SlowIndex and
 * returned by its getIndexUpdateOperation() method.
 */
class IndexingOperation {

    private static final String ELEMENT_NAME_INDEX = "index";

    private static final String ATTRIBUTE_NAME_PATH = "path";

    private int numAdded;

    private int numRemoved;

    private SearchIndex index = null;

    /**
	 * Construct indexing operation.
	 * 
	 * @param ix
	 *            ISearchIndex already opened
	 */
    public IndexingOperation(SearchIndex ix) {
        this.index = ix;
    }

    private void checkCancelled(IProgressMonitor pm) throws OperationCanceledException {
        if (pm.isCanceled()) throw new OperationCanceledException();
    }

    /**
	 * Executes indexing, given the progress monitor.
	 * 
	 * @param pm
	 *            progres monitor to be used during this long operation for
	 *            reporting progress
	 * @throws OperationCanceledException
	 *             if indexing was cancelled
	 */
    protected void execute(IProgressMonitor pm) throws OperationCanceledException, IndexingException {
        checkCancelled(pm);
        Collection staleDocs = getRemovedDocuments(index);
        numRemoved = staleDocs.size();
        Collection newDocs = getAddedDocuments(index);
        numAdded = newDocs.size();
        if (numRemoved + numAdded <= 0) {
            pm.done();
            BaseHelpSystem.getLocalSearchManager().clearSearchParticipants();
            return;
        }
        pm.beginTask(HelpBaseResources.UpdatingIndex, numRemoved + 10 * numAdded);
        removeStaleDocuments(new SubProgressMonitor(pm, numRemoved), staleDocs);
        checkCancelled(pm);
        addNewDocuments(new SubProgressMonitor(pm, 10 * numAdded), newDocs, staleDocs.size() == 0);
        pm.done();
        BaseHelpSystem.getLocalSearchManager().clearSearchParticipants();
    }

    private Map calculateNewToRemove(Collection newDocs, Map prebuiltDocs) {
        Map docsToDelete = prebuiltDocs;
        ArrayList prebuiltHrefs = new ArrayList(prebuiltDocs.keySet());
        for (int i = 0; i < prebuiltHrefs.size(); i++) {
            String href = (String) prebuiltHrefs.get(i);
            URL u = SearchIndex.getIndexableURL(index.getLocale(), href);
            if (u == null) {
                docsToDelete.put(href, null);
            }
            if (newDocs.contains(u)) {
                if (docsToDelete.get(href) != null) {
                } else {
                    docsToDelete.remove(href);
                }
            } else {
                docsToDelete.put(href, null);
            }
        }
        return docsToDelete;
    }

    /**
	 * Returns documents that must be deleted
	 */
    private Map addNewDocuments(IProgressMonitor pm, Collection newDocs, boolean opened) throws IndexingException {
        Map prebuiltDocs = mergeIndexes(pm, opened);
        checkCancelled(pm);
        Collection docsToIndex = calculateDocsToAdd(newDocs, prebuiltDocs);
        checkCancelled(pm);
        Map docsToDelete = calculateNewToRemove(newDocs, prebuiltDocs);
        pm.beginTask("", 10 * docsToIndex.size() + docsToDelete.size());
        checkCancelled(pm);
        addDocuments(new SubProgressMonitor(pm, 10 * docsToIndex.size()), docsToIndex, docsToDelete.size() == 0);
        checkCancelled(pm);
        removeNewDocuments(new SubProgressMonitor(pm, docsToDelete.size()), docsToDelete);
        pm.done();
        return docsToDelete;
    }

    private Collection calculateDocsToAdd(Collection newDocs, Map prebuiltDocs) {
        Collection docsToIndex = null;
        int newDocSize = newDocs.size();
        if (prebuiltDocs.size() > 0) {
            docsToIndex = new HashSet(newDocs);
            for (Iterator it = prebuiltDocs.keySet().iterator(); it.hasNext(); ) {
                String href = (String) it.next();
                URL u = SearchIndex.getIndexableURL(index.getLocale(), href);
                if (u != null) {
                    docsToIndex.remove(u);
                }
            }
        } else {
            docsToIndex = newDocs;
        }
        if (HelpPlugin.DEBUG_SEARCH) {
            System.out.println("Building search index-  new docs: " + newDocSize + ", preindexed: " + prebuiltDocs.size() + ", remaining: " + docsToIndex.size());
        }
        return docsToIndex;
    }

    /**
	 * @param docsToDelete
	 *            Keys are /pluginid/href of all merged Docs. Values are null to
	 *            delete href, or String[] of indexIds to delete duplicates with
	 *            given index IDs
	 */
    private void removeNewDocuments(IProgressMonitor pm, Map docsToDelete) throws IndexingException {
        pm = new LazyProgressMonitor(pm);
        pm.beginTask("", docsToDelete.size());
        checkCancelled(pm);
        Set keysToDelete = docsToDelete.keySet();
        if (keysToDelete.size() > 0) {
            if (!index.beginRemoveDuplicatesBatch()) {
                throw new IndexingException();
            }
            MultiStatus multiStatus = null;
            for (Iterator it = keysToDelete.iterator(); it.hasNext(); ) {
                String href = (String) it.next();
                String[] indexIds = (String[]) docsToDelete.get(href);
                if (indexIds == null) {
                    index.removeDocument(href);
                    continue;
                }
                IStatus status = index.removeDuplicates(href, indexIds);
                if (status.getCode() != IStatus.OK) {
                    if (multiStatus == null) {
                        multiStatus = new MultiStatus(HelpBasePlugin.PLUGIN_ID, IStatus.WARNING, "Some help documents could not removed from index.", null);
                    }
                    multiStatus.add(status);
                }
                checkCancelled(pm);
                pm.worked(1);
                if (multiStatus != null) {
                    HelpBasePlugin.logStatus(multiStatus);
                }
            }
            if (!index.endRemoveDuplicatesBatch()) {
                throw new IndexingException();
            }
        }
        pm.done();
    }

    private void addDocuments(IProgressMonitor pm, Collection addedDocs, boolean lastOperation) throws IndexingException {
        pm = new LazyProgressMonitor(pm);
        pm.beginTask("", addedDocs.size());
        checkCancelled(pm);
        pm.subTask(HelpBaseResources.UpdatingIndex);
        MultiStatus multiStatus = null;
        for (Iterator it = addedDocs.iterator(); it.hasNext(); ) {
            URL doc = (URL) it.next();
            IStatus status = index.addDocument(getName(doc), doc);
            if (status.getCode() != IStatus.OK) {
                if (multiStatus == null) {
                    multiStatus = new MultiStatus(HelpBasePlugin.PLUGIN_ID, IStatus.ERROR, "Help documentation could not be indexed completely.", null);
                }
                multiStatus.add(status);
            }
            checkCancelled(pm);
            pm.worked(1);
        }
        if (multiStatus != null) {
            HelpBasePlugin.logStatus(multiStatus);
        }
        pm.subTask(HelpBaseResources.Writing_index);
        if (!index.endAddBatch(addedDocs.size() > 0, lastOperation)) throw new IndexingException();
        pm.done();
    }

    private void removeStaleDocuments(IProgressMonitor pm, Collection removedDocs) throws IndexingException {
        pm = new LazyProgressMonitor(pm);
        pm.beginTask("", removedDocs.size());
        pm.subTask(HelpBaseResources.Preparing_for_indexing);
        checkCancelled(pm);
        if (numRemoved > 0) {
            if (!index.beginDeleteBatch()) {
                throw new IndexingException();
            }
            checkCancelled(pm);
            pm.subTask(HelpBaseResources.UpdatingIndex);
            MultiStatus multiStatus = null;
            for (Iterator it = removedDocs.iterator(); it.hasNext(); ) {
                URL doc = (URL) it.next();
                IStatus status = index.removeDocument(getName(doc));
                if (status.getCode() != IStatus.OK) {
                    if (multiStatus == null) {
                        multiStatus = new MultiStatus(HelpBasePlugin.PLUGIN_ID, IStatus.WARNING, "Uninstalled or updated help documents could not be removed from index.", null);
                    }
                    multiStatus.add(status);
                }
                checkCancelled(pm);
                pm.worked(1);
            }
            if (multiStatus != null) {
                HelpBasePlugin.logStatus(multiStatus);
            }
            if (!index.endDeleteBatch()) {
                throw new IndexingException();
            }
        }
        pm.done();
    }

    /**
	 * Returns the document identifier. Currently we use the document file name
	 * as identifier.
	 */
    private String getName(URL doc) {
        String name = doc.getFile();
        int i = name.indexOf('?');
        if (i != -1) name = name.substring(0, i);
        return name;
    }

    public class IndexingException extends Exception {

        private static final long serialVersionUID = 1L;
    }

    /**
	 * Returns IDs of plugins which need docs added to index.
	 */
    private Collection getAddedPlugins(SearchIndex index) {
        Collection addedPlugins = index.getDocPlugins().getAdded();
        if (addedPlugins == null || addedPlugins.isEmpty()) return new ArrayList(0);
        return addedPlugins;
    }

    /**
	 * Returns the documents to be added to index. The collection consists of
	 * the associated PluginURL objects.
	 */
    private Collection getAddedDocuments(SearchIndex index) {
        Collection addedPlugins = getAddedPlugins(index);
        if (HelpPlugin.DEBUG_SEARCH) {
            traceAddedContributors(addedPlugins);
        }
        Set urls = getAllDocuments(index.getLocale());
        Set addedDocs = new HashSet(urls.size());
        for (Iterator docs = urls.iterator(); docs.hasNext(); ) {
            String doc = (String) docs.next();
            if (doc.startsWith("//")) {
                doc = doc.substring(1);
            }
            int i = doc.indexOf('/', 1);
            String plugin = i == -1 ? "" : doc.substring(1, i);
            if (!addedPlugins.contains(plugin)) {
                continue;
            }
            URL url = SearchIndex.getIndexableURL(index.getLocale(), doc);
            if (url != null) {
                addedDocs.add(url);
            }
        }
        LuceneSearchParticipant[] participants = BaseHelpSystem.getLocalSearchManager().getGlobalParticipants();
        for (int j = 0; j < participants.length; j++) {
            String participantId;
            try {
                participantId = participants[j].getId();
            } catch (Throwable t) {
                HelpBasePlugin.logError("Failed to get help search participant id for: " + participants[j].getClass().getName() + "; skipping this one.", t);
                continue;
            }
            Set set;
            try {
                set = participants[j].getAllDocuments(index.getLocale());
            } catch (Throwable t) {
                HelpBasePlugin.logError("Failed to retrieve documents from one of the help search participants: " + participants[j].getClass().getName() + "; skipping this one.", t);
                continue;
            }
            for (Iterator docs = set.iterator(); docs.hasNext(); ) {
                String doc = (String) docs.next();
                String id = null;
                int qloc = doc.indexOf('?');
                if (qloc != -1) {
                    String query = doc.substring(qloc + 1);
                    doc = doc.substring(0, qloc);
                    HashMap arguments = new HashMap();
                    HelpURLConnection.parseQuery(query, arguments);
                    id = (String) arguments.get("id");
                }
                int i = doc.indexOf('/', 1);
                String plugin = i == -1 ? "" : doc.substring(1, i);
                if (!addedPlugins.contains(plugin)) {
                    continue;
                }
                URL url = SearchIndex.getIndexableURL(index.getLocale(), doc, id, participantId);
                if (url != null) {
                    addedDocs.add(url);
                }
            }
        }
        return addedDocs;
    }

    private void traceAddedContributors(Collection addedContributors) {
        for (Iterator iter = addedContributors.iterator(); iter.hasNext(); ) {
            String id = (String) iter.next();
            System.out.println("Updating search index for contributor :" + id);
        }
    }

    /**
	 * Returns the documents to be removed from index. The collection consists
	 * of the associated PluginURL objects.
	 */
    private Collection getRemovedDocuments(SearchIndex index) {
        Collection removedPlugins = index.getDocPlugins().getRemoved();
        if (removedPlugins == null || removedPlugins.isEmpty()) return new ArrayList(0);
        HelpProperties indexedDocs = index.getIndexedDocs();
        Set removedDocs = new HashSet(indexedDocs.size());
        for (Iterator docs = indexedDocs.keySet().iterator(); docs.hasNext(); ) {
            String doc = (String) docs.next();
            int i = doc.indexOf('/', 1);
            String plugin = i == -1 ? "" : doc.substring(1, i);
            if (!removedPlugins.contains(plugin)) {
                continue;
            }
            URL url = SearchIndex.getIndexableURL(index.getLocale(), doc);
            if (url != null) {
                removedDocs.add(url);
            }
        }
        return removedDocs;
    }

    /**
	 * Adds the topic and its subtopics to the list of documents
	 */
    private void add(ITopic topic, Set hrefs) {
        String href = topic.getHref();
        add(href, hrefs);
        ITopic[] subtopics = topic.getSubtopics();
        for (int i = 0; i < subtopics.length; i++) add(subtopics[i], hrefs);
    }

    private void add(String href, Set hrefs) {
        if (href != null && !href.equals("") && !href.startsWith("http://") && !href.startsWith("https://")) hrefs.add(href);
    }

    /**
	 * Returns the collection of href's for all the help topics.
	 */
    private Set getAllDocuments(String locale) {
        HashSet hrefs = new HashSet();
        Toc[] tocs = index.getTocManager().getTocs(locale);
        for (int i = 0; i < tocs.length; i++) {
            ITopic[] topics = tocs[i].getTopics();
            for (int j = 0; j < topics.length; j++) {
                add(topics[j], hrefs);
            }
            ITocContribution contrib = tocs[i].getTocContribution();
            String[] extraDocs = contrib.getExtraDocuments();
            for (int j = 0; j < extraDocs.length; ++j) {
                add(extraDocs[j], hrefs);
            }
            ITopic tocDescriptionTopic = tocs[i].getTopic(null);
            if (tocDescriptionTopic != null) add(tocDescriptionTopic, hrefs);
        }
        return hrefs;
    }

    /**
	 * Obtains PluginIndexes pointing to prebuilt indexes
	 * 
	 * @param pluginIds
	 * @param locale
	 * @return
	 */
    private PrebuiltIndexes getIndexesToAdd(Collection pluginIds) {
        PrebuiltIndexes indexes = new PrebuiltIndexes(index);
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IConfigurationElement[] elements = registry.getConfigurationElementsFor(TocFileProvider.EXTENSION_POINT_ID_TOC);
        for (int i = 0; i < elements.length; ++i) {
            IConfigurationElement elem = elements[i];
            try {
                if (elem.getName().equals(ELEMENT_NAME_INDEX)) {
                    String pluginId = elem.getNamespaceIdentifier();
                    if (pluginIds.contains(pluginId)) {
                        String path = elem.getAttribute(ATTRIBUTE_NAME_PATH);
                        if (path != null) {
                            indexes.add(pluginId, path);
                            if (HelpPlugin.DEBUG_SEARCH) {
                                System.out.println("Search index for " + pluginId + " is prebuilt with path \"" + path + '"');
                            }
                        } else {
                            String msg = "Element \"index\" in extension of \"org.eclipse.help.toc\" must specify a \"path\" attribute (plug-in: " + pluginId + ")";
                            HelpBasePlugin.logError(msg, null);
                        }
                    }
                }
            } catch (InvalidRegistryObjectException e) {
            }
        }
        return indexes;
    }

    private Map mergeIndexes(IProgressMonitor monitor, boolean opened) throws IndexingException {
        Collection addedPluginIds = getAddedPlugins(index);
        PrebuiltIndexes indexes = getIndexesToAdd(addedPluginIds);
        PluginIndex[] pluginIndexes = indexes.getIndexes();
        Map mergedDocs = null;
        if (!index.beginAddBatch(opened)) {
            throw new IndexingException();
        }
        if (pluginIndexes.length > 0) {
            mergedDocs = index.merge(pluginIndexes, monitor);
        }
        if (mergedDocs == null) {
            return Collections.EMPTY_MAP;
        }
        return mergedDocs;
    }
}
