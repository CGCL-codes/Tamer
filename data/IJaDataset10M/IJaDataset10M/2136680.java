package com.technosophos.rhizome.repository;

import com.technosophos.rhizome.document.RhizomeDocument;
import com.technosophos.rhizome.repository.DocumentNotFoundException;
import com.technosophos.rhizome.repository.RepositoryAccessException;
import com.technosophos.rhizome.RhizomeException;

/**
 * The main entry point to the Rhizome backend.
 * <p>
 * The repository manager provides the necessary mechanisms to get 
 * instances of the backend repository, index, and searcher. Also, it 
 * has a handful of convenience methods that make it much easier to 
 * perform simple tasks like getting and storing documents.
 * </p><p>
 * There should be only one instance of RepositoryManager per repository.
 * Multiple instances may have undefined behavior (depending on the 
 * backend implementation).
 * </p><p>
 * The backend repository, indexer, and searcher interfaces can have 
 * different implementations. The default implementation is loaded 
 * automatically. To use alternative implementations, set the 
 * class names with the set*ClassName() methods. You must do this
 * before one of the accessors for those objects is called, or else 
 * you might get unpredictable results.
 * </p>
 * @author mbutcher
 *
 */
public class RepositoryManager {

    /**
	 * Default indexer class. 
	 * If no alternate indexer is given, this one will be used.
	 */
    public static final String DEFAULT_INDEXER_CLASS_NAME = "com.technosophos.rhizome.repository.lucene.LuceneIndexerDepot";

    /**
	 * Default repository class name.
	 * If no other repository class is given, this one will be used.
	 */
    public static final String DEFAULT_REPOSITORY_CLASS_NAME = "com.technosophos.rhizome.repository.fs.FileSystemRepositoryDepot";

    /**
	 * Default repository searcher class name.
	 * If no other repository searcher class is given, this one will be used.
	 */
    public static final String DEFAULT_REPOSITORY_SEARCHER_CLASS_NAME = "com.technosophos.rhizome.repository.lucene.LuceneSearcherDepot";

    public static final String CXT_INDEXER_CLASS_NAME = "indexer_class";

    public static final String CXT_REPOSITORY_CLASS_NAME = "repository_class";

    public static final String CXT_REPOSITORY_SEARCHER_CLASS_NAME = "searcher_class";

    private RepositoryContext context = null;

    private DocumentRepository repoInstance = null;

    private DocumentIndexer indexerInstance = null;

    private RepositorySearcher searchInstance = null;

    private DocumentRepositoryDepot drDepot = null;

    private DocumentIndexerDepot diDepot = null;

    private RepositorySearcherDepot searchDepot = null;

    private Class<?> indexerClass = null;

    private Class<?> repositoryClass = null;

    private Class<?> searcherClass = null;

    /**
	 * The main constructor.
	 * <p>This builds a new Repository Manager and initializes it. Do <b>not</b>
	 * call init after this.</p>
	 * 
	 * <p>RepositoryManager initializes the *Depot classes, which are used, in turn,
	 * to control access to one or more backend repositories. Depots are created during
	 * initialization and reused indefinitely.</p>
	 *
	 * @param context The configuration information for this RepositoryManager.
	 * @see DocumentIndexerDepot
	 * @see DocumentRepositoryDepot
	 * @see RepositorySearcherDepot
	 */
    public RepositoryManager(final RepositoryContext context) throws RhizomeException {
        this.init(context);
    }

    /**
	 * Construct a new RepositoryManager.
	 * You <b>Must</b> call {@link #init(RepositoryContext)} after this.
	 */
    public RepositoryManager() {
    }

    /**
	 * When a new Rhizome Manager is created with an empty constructor, it must be initialized.
	 * <p>This method initializes a new Rhizome Manager. Along with some trivial 
	 * initialization, it performs all class loading needed for this manager, and
	 * failures to load classes will result in an exception.</p>
	 * @param context The new repository context.
	 * @throws RhizomeInitializationException if the classes cannot be loaded, and 
	 * RhizomeAccessExceptions if certain conditions obtain when initializing repositories.
	 * @see #RepositoryManager(RepositoryContext) 
	 */
    public void init(final RepositoryContext context) throws RhizomeException {
        this.context = context;
        try {
            Class tempClass;
            if (context.hasKey(CXT_INDEXER_CLASS_NAME)) tempClass = Class.forName(context.getParam(CXT_INDEXER_CLASS_NAME)); else tempClass = Class.forName(DEFAULT_INDEXER_CLASS_NAME);
            this.diDepot = (DocumentIndexerDepot) tempClass.newInstance();
            if (context.hasKey(CXT_REPOSITORY_CLASS_NAME)) tempClass = Class.forName(context.getParam(CXT_REPOSITORY_CLASS_NAME)); else tempClass = Class.forName(DEFAULT_REPOSITORY_CLASS_NAME);
            this.drDepot = (DocumentRepositoryDepot) tempClass.newInstance();
            if (context.hasKey(CXT_REPOSITORY_SEARCHER_CLASS_NAME)) tempClass = Class.forName(context.getParam(CXT_REPOSITORY_SEARCHER_CLASS_NAME)); else tempClass = Class.forName(DEFAULT_REPOSITORY_SEARCHER_CLASS_NAME);
            this.searchDepot = (RepositorySearcherDepot) tempClass.newInstance();
        } catch (ClassNotFoundException cnfe) {
            throw new RhizomeInitializationException("Failed to load class: " + cnfe.getMessage(), cnfe);
        } catch (InstantiationException ie) {
            throw new RhizomeInitializationException("Failed to instantiate class: " + ie.getMessage(), ie);
        } catch (IllegalAccessException iae) {
            throw new RhizomeInitializationException("Class access problem: " + iae.getMessage(), iae);
        }
    }

    /**
	 * Get the current configuration context
	 * @return Returns the RepositoryContext, which could be null.
	 */
    public RepositoryContext getContext() {
        return this.context;
    }

    /**
	 * Put a document into Rhizome.
	 * <p>
	 * This puts a document into the repository. If a document with the same
	 * document ID already exists, then this document will replace the other
	 * (in other words, it acts as a modification operation). If no other document
	 * exists, this will be added to the repository.</p>
	 * <p>Adding a document this way automatically puts it into the search
	 * index, so there is no reason to interact directly with the indexer.</p>
	 * <p><b>Warning:</b> This method creates new instances of the 
	 * {@link DocumentRepository} repository and {@link DocumentIndexer} indexer.
	 * If you are doing lots of interactions (like storing several documents), you will
	 * get better performance by instantiating one repository and one index, and then 
	 * doing the updating yourself.</p>
	 * @param repoName the name of the repository.
	 * @param document to add to repository.
	 * @see DocumentRepsitory.storeDocument(RhizomeDocument, boolean)
	 * @see DocumentIndexer.updateIndex(RhizomeDocument) 
	 */
    public void storeDocument(String repoName, RhizomeDocument doc) throws RhizomeException {
        DocumentRepository repo = this.getRepository(repoName);
        DocumentIndexer indexer = this.getIndexer(repoName);
        repo.storeDocument(doc, true);
        indexer.updateIndex(doc);
    }

    /**
	 * This attempts to remove a document from the index (first) and then the 
	 * repository.
	 * <p>If a document cannot be removed from the index, then it will not be removed from
	 * the repository. Otherwise, there would be the possiblity for much misleading
	 * information.</p>
	 * <p>On the other hand, if a document is successfully removed from the index, 
	 * and then cannot be removed from the repository, the transaction is not rolled
	 * back. The document is left in the repository, and omitted from the index.
	 * <b>This behavior may change in future versions.</b></p>
	 * <p>In both cases, if a delete fails, a 
	 * <code>RepositoryAccessException</code> will be thrown.</p>
	 * <p><b>Warning:</b> This method creates new instances of the 
	 * {@link DocumentRepository} repository and {@link DocumentIndexer} indexer.
	 * If you are doing lots of interactions (like removing several documents), you will
	 * get better performance by instantiating one repository and one index, and then 
	 * doing the removing with the correct methods.</p>
	 * 
	 * @param repoName The name of the repository from which the doc will be deleted
	 * @param docID document to delete
	 * @throws RepositoryAccessException, RhizomeException
	 * @see DocumentRepository.removeDocument(String)
	 * @see DocumentIndexer.deleteFromIndex(String)
	 */
    public void removeDocument(String repoName, String docID) throws RhizomeException {
        DocumentRepository repo = this.getRepository(repoName);
        DocumentIndexer indexer = this.getIndexer(repoName);
        if (!indexer.deleteFromIndex(docID)) throw new RepositoryAccessException("Could not remove document from index. Document is still available.");
        if (!repo.removeDocument(docID)) {
            String err = "Could not remove document from repository. ";
            try {
                indexer.updateIndex(docID, this);
            } catch (RhizomeException e) {
                throw new RepositoryAccessException(err + "Document is not in index.");
            }
            throw new RepositoryAccessException(err + "Document is still in index.");
        }
    }

    /**
	 * Return a document.
	 * <p>Given a document ID, attempt to fetch the document from the repository.</p>
	 * <p><b>WARNING: </b> This method creates and destroys a new repository. If you are
	 * doing multiple repository operations, it is better to get a new instance of a 
	 * repository ({@link #getRepository(String)} and then perform manipulations on that
	 * object.</p>
	 * <p>If the file is not found, a <code>repositoryAccessException</code> is thrown.</p>
	 * @param docID
	 * @return
	 * @throws RhizomeException
	 */
    public RhizomeDocument getDocument(String repoName, String docID) throws DocumentNotFoundException, RhizomeException {
        return this.getRepository(repoName).getDocument(docID);
    }

    /**
	 * Set the repository context.
	 * 
	 * <p><b>WARNING:</b> The following caveats apply.</p>
	 * <ul>
	 * <li>How this method works depends largely on the backend implementation.</li>
	 * <li>The Depot classes will not be reloaded</li>
	 * <li>If the depots cache information, changing context may or may not have an 
	 * impact. That is up to the depot implementors.</li>
	 * @param context
	 */
    public void setContext(final RepositoryContext context) {
        this.context = context;
    }

    /**
	 * Returns true if there is a full repository for this name.
	 * This returns true iff there is a DocumentRepository and a DocumentIndex for the
	 * given name.
	 * @param name The name of the repository
	 * @return true if a repository and index exists, false otherwise.
	 */
    public boolean hasRepository(String name) {
        return this.drDepot.hasNamedRepository(name, this.context) && this.diDepot.hasIndex(name, this.context);
    }

    /**
	 * Create a new repository and search index.
	 * 
	 * @param name Name of the new repository.
	 * @throws RhizomeInitializationException
	 * @throws RepositoryAccessException
	 */
    public void createRepository(String name) throws RhizomeInitializationException, RepositoryAccessException {
        this.drDepot.createNamedRepository(name, this.context);
        this.diDepot.createIndex(name, this.context);
    }

    /**
	 * Delete an existing repository and a search index pair.
	 * @param name Name of the repository to be deleted.
	 * @throws RepositoryAccessException if either of the items cannot be deleted.
	 */
    public void removeRepository(String name) throws RepositoryAccessException {
        this.diDepot.deleteIndex(name, this.context);
        this.drDepot.deleteNamedRepository(name, this.context);
    }

    /**
	 * Get a document repository.
	 * 
	 * Depending on the repository class's isReusable() method, this may
	 * return a fresh instance or a cached copy.
	 * 
	 * @return initialized document repository.
	 */
    public DocumentRepository getRepository(String repoName) throws RhizomeInitializationException {
        assert repoName != null;
        return this.drDepot.getNamedRepository(repoName, this.context);
    }

    /**
	 * Get a document indexer.
	 * 
	 * Depending on the indexer class's isReusable() method, this may
	 * return a fresh instance or a cached copy.
	 * 
	 * @return initialized indexer.
	 */
    public DocumentIndexer getIndexer(String indName) throws RhizomeInitializationException {
        assert indName != null;
        return this.diDepot.getIndexer(indName, this.context);
    }

    /**
	 * Get a repository searcher.
	 * 
	 * Depending on the Searcher class's isReusable() method, this may
	 * return a fresh instance or a cached copy.
	 * 
	 * @return initialized repository searcher.
	 * @throws RhizomeInitializationException when the search tables cannot be created, opened, or read.
	 */
    public RepositorySearcher getSearcher(String searcherName) throws RhizomeInitializationException {
        assert searcherName != null;
        return this.searchDepot.getSearcher(searcherName, this.context);
    }

    /**
	 * Get a document repository.
	 * 
	 * Depending on the repository class's isReusable() method, this may
	 * return a fresh instance or a cached copy.
	 * 
	 * @return initialized document repository.
	 * @deprecated
	 */
    public DocumentRepository getRepository() throws RhizomeInitializationException {
        if (this.repoInstance != null) return this.repoInstance;
        DocumentRepository repoInst;
        try {
            repoInst = (DocumentRepository) this.repositoryClass.newInstance();
            repoInst.setConfiguration(this.context);
        } catch (InstantiationException e) {
            String errmsg = "Cannot create object of class " + this.repositoryClass.getCanonicalName() + "(Reason: " + e.getMessage() + ")";
            throw new RhizomeInitializationException(errmsg, e);
        } catch (IllegalAccessException e) {
            String errmsg = "Cannot create object of class " + this.repositoryClass.getCanonicalName() + "(Reason: Illegal access." + e.getMessage() + ")";
            throw new RhizomeInitializationException(errmsg, e);
        }
        if (repoInst.isReusable()) this.repoInstance = repoInst;
        return repoInst;
    }

    /**
	 * Get a document indexer.
	 * 
	 * Depending on the indexer class's isReusable() method, this may
	 * return a fresh instance or a cached copy.
	 * 
	 * @return initialized indexer.
	 * @deprecated Use {@link #getIndexer(String)} instead.
	 */
    public DocumentIndexer getIndexer() throws RhizomeInitializationException {
        if (this.indexerInstance != null) return this.indexerInstance;
        DocumentIndexer indInst;
        try {
            indInst = (DocumentIndexer) this.indexerClass.newInstance();
            indInst.setConfiguration(this.context);
        } catch (Exception e) {
            String errmsg = "Cannot create object of class " + this.indexerClass.getCanonicalName() + "(Reason: " + e.getMessage() + ")";
            throw new RhizomeInitializationException(errmsg);
        }
        if (indInst.isReusable()) this.indexerInstance = indInst;
        return indInst;
    }

    /**
	 * Get a repository searcher.
	 * 
	 * Depending on the Searcher class's isReusable() method, this may
	 * return a fresh instance or a cached copy.
	 * 
	 * @return initialized repository searcher.
	 * @deprecated Use {@link #getRepositorySearcher()} instead.
	 */
    public RepositorySearcher getRepositorySearcher() throws RhizomeInitializationException {
        if (this.searchInstance != null) return this.searchInstance;
        RepositorySearcher searchInst;
        try {
            searchInst = (RepositorySearcher) this.searcherClass.newInstance();
            searchInst.setConfiguration(this.context);
        } catch (Exception e) {
            String errmsg = "Cannot create object of class " + this.searcherClass.getCanonicalName() + "(Reason: " + e.getMessage() + ")";
            throw new RhizomeInitializationException(errmsg);
        }
        if (searchInst.isReusable()) this.searchInstance = searchInst;
        return searchInst;
    }

    /**
	 * Prints out information about what searcher, indexer and repository depots are being used.
	 */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Search: ");
        sb.append(this.searchDepot.getClass().getCanonicalName());
        sb.append("\nIndex: ");
        sb.append(this.diDepot.getClass().getCanonicalName());
        sb.append("\nRepository: ");
        sb.append(this.drDepot.getClass().getCanonicalName());
        sb.append("\n");
        return sb.toString();
    }
}

class RhizomeClassInstanceException extends RhizomeInitializationException {

    private static final long serialVersionUID = 1L;

    public RhizomeClassInstanceException() {
        super("Cannot change classes after the class has been instantiated.");
    }

    public RhizomeClassInstanceException(String str) {
        super(str);
    }
}
