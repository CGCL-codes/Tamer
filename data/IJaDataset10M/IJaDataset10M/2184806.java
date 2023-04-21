package net.sf.iqser.plugin.msx.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;
import com.iqser.core.exception.IQserTechnicalException;
import com.iqser.core.model.Concept;
import com.iqser.core.model.Content;
import com.iqser.core.model.ContentItem;
import com.iqser.core.model.Cooccurrence;
import com.iqser.core.model.Filter;
import com.iqser.core.model.Relation;
import com.iqser.core.model.SearchResult;
import com.iqser.core.repository.Repository;

/**
 * Supports testing of classes of the iQser Web Content Provider Family.
 * 
 * @author Jorg Wurzer
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MockRepository implements Repository {

    private ArrayList<Content> cl = null;

    public static final int ADD_CONTENT = 0;

    public static final int UPDATE_CONTENT = 1;

    public static final int DELETE_CONTENT = 2;

    private static Logger logger = Logger.getLogger(MockRepository.class);

    public void addConcept(Concept arg0) throws IQserTechnicalException {
    }

    public void addContent(Content c) throws IQserTechnicalException {
        logger.debug("addContent() called for content " + c.getContentUrl());
        Iterator iter = cl.iterator();
        while (iter.hasNext()) {
            Content oldc = (Content) iter.next();
            if (oldc.getContentUrl().equals(c.getContentUrl()) && oldc.getProvider().equals(c.getProvider())) {
                throw new IQserTechnicalException("Content object already exists", IQserTechnicalException.SEVERITY_ERROR);
            }
        }
        cl.add(c);
    }

    public void addContentItem(ContentItem arg0) throws IQserTechnicalException {
    }

    public void addCooccurrence(Cooccurrence arg0) throws IQserTechnicalException {
    }

    public void addRelation(Relation arg0) throws IQserTechnicalException {
    }

    public void close() throws IQserTechnicalException {
    }

    public boolean contains(String url, String provider) throws IQserTechnicalException {
        logger.debug("contains() called for " + url + " and " + provider);
        Iterator iter = cl.iterator();
        Content c = null;
        while (iter.hasNext()) {
            c = (Content) iter.next();
            if (c.getContentUrl().equals(url) && c.getProvider().equals(provider)) return true;
        }
        return false;
    }

    public void deleteConcept(Concept arg0) throws IQserTechnicalException {
    }

    public void deleteContent(Content c) throws IQserTechnicalException {
        logger.debug("deleteContent() called for " + c.getContentUrl());
        Iterator iter = cl.iterator();
        boolean removed = false;
        Content oldc = null;
        while (iter.hasNext()) {
            oldc = (Content) iter.next();
            if (oldc.getContentUrl().equals(c.getContentUrl()) && oldc.getProvider().equals(c.getProvider())) {
                removed = true;
                break;
            }
        }
        if (removed) cl.remove(oldc); else throw new IQserTechnicalException("Old content object was not found", IQserTechnicalException.SEVERITY_ERROR);
    }

    public void deleteContentItem(ContentItem arg0) throws IQserTechnicalException {
    }

    public void deleteCooccurrence(Cooccurrence arg0) throws IQserTechnicalException {
    }

    public void deleteRelation(long arg0, long arg1) throws IQserTechnicalException {
    }

    public ArrayList<ContentItem> getAllContentItem(long arg0) throws IQserTechnicalException {
        return null;
    }

    public Concept getConcept(String arg0) throws IQserTechnicalException {
        return null;
    }

    public Concept getConcept(long arg0) throws IQserTechnicalException {
        return null;
    }

    public Content getContent(long arg0) throws IQserTechnicalException {
        return null;
    }

    public Content getContent(long arg0, Collection<String> arg1) throws IQserTechnicalException {
        return null;
    }

    public Content getContent(String url, String provider) throws IQserTechnicalException {
        logger.debug("getContent() called for " + url + " and " + provider);
        Iterator iter = cl.iterator();
        Content c = null;
        while (iter.hasNext()) {
            c = (Content) iter.next();
            if (c.getContentUrl().equals(url) && c.getProvider().equals(provider)) return c;
        }
        if (c == null) throw new IQserTechnicalException("Content object was not found", IQserTechnicalException.SEVERITY_ERROR);
        return null;
    }

    public Collection getContentByProvider(String provider) throws IQserTechnicalException {
        logger.debug("getContentByProvider() called for " + provider);
        Iterator iter = cl.iterator();
        ArrayList out = new ArrayList();
        while (iter.hasNext()) {
            Content c = (Content) iter.next();
            if (c.getProvider().equals(provider)) out.add(c);
        }
        return out;
    }

    public Collection getContentByProvider(String provider, boolean arg1) throws IQserTechnicalException {
        return getContentByProvider(provider);
    }

    public ContentItem getContentItem(long arg0, long arg1) throws IQserTechnicalException {
        return null;
    }

    public Collection getContentTypes() throws IQserTechnicalException {
        return null;
    }

    public Cooccurrence getCooccurrence(long arg0, long arg1) throws IQserTechnicalException {
        return null;
    }

    public int getDocumentFrequency(Concept arg0) throws IQserTechnicalException {
        return 0;
    }

    public String getDocumentText(long arg0) throws IQserTechnicalException {
        return null;
    }

    public int getMaxDoc() throws IQserTechnicalException {
        return 0;
    }

    public Collection<SearchResult> getRelatedContent(long arg0, double arg1) throws IQserTechnicalException {
        return null;
    }

    public Relation getRelation(long arg0, long arg1) throws IQserTechnicalException {
        return null;
    }

    public int getRelationCount(long arg0) throws IQserTechnicalException {
        return 0;
    }

    public void init() throws IQserTechnicalException {
        cl = new ArrayList();
    }

    public boolean isEmpty() throws IQserTechnicalException {
        return false;
    }

    public boolean isRelated(long arg0, long arg1) throws IQserTechnicalException {
        return false;
    }

    public void saveContentItems(Collection<ContentItem> arg0) throws IQserTechnicalException {
    }

    public Collection search(Filter arg0) throws IQserTechnicalException {
        return null;
    }

    public void updateConcept(Concept arg0) throws IQserTechnicalException {
    }

    public void updateContent(Content newc) throws IQserTechnicalException {
        logger.debug("updateContent() called for " + newc.getContentUrl());
        Iterator iter = cl.iterator();
        boolean updated = false;
        while (iter.hasNext()) {
            Content oldc = (Content) iter.next();
            if (oldc.getContentUrl().equalsIgnoreCase(newc.getContentUrl()) && oldc.getProvider().equalsIgnoreCase(newc.getProvider())) {
                int index = cl.indexOf(oldc);
                cl.set(index, newc);
                updated = true;
            }
        }
        if (!updated) throw new IQserTechnicalException("Old content object was not found", IQserTechnicalException.SEVERITY_ERROR);
    }

    public void updateContentItem(ContentItem arg0) throws IQserTechnicalException {
    }

    public void updateCooccurrence(Cooccurrence arg0) throws IQserTechnicalException {
    }

    public void updateRelation(Relation arg0) throws IQserTechnicalException {
    }

    public Collection<String> getAttributesByProvider(String arg0) throws IQserTechnicalException {
        return null;
    }

    public Collection<String> getAttributesByType(String arg0) throws IQserTechnicalException {
        return null;
    }

    public Collection<Concept> getConcepts(long arg0) throws IQserTechnicalException {
        return null;
    }

    public Collection<Concept> getRelatedConcepts(long arg0) throws IQserTechnicalException {
        return null;
    }

    public Collection<Concept> getRelatedConceptsByConcept(Concept arg0) throws IQserTechnicalException {
        return null;
    }
}
