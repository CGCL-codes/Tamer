package fr.umlv.jee.hibou.bdd;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import fr.umlv.jee.hibou.bdd.exception.HibouTechnicalException;
import fr.umlv.jee.hibou.bdd.table.Document;
import fr.umlv.jee.hibou.bdd.table.Project;
import fr.umlv.jee.hibou.bdd.util.HibernateUtil;

/**
 * Class that manages documents database acces.
 * @author micka, alex, nak, matt
 */
public class DocumentsManager {

    /**
	 * Add a new entry into the Document table as a uploaded document.
	 * @param projectName the project's name where the document is added.
	 * @param filename the document's name.
	 * @param type the document's type (Archives, Documentations or Maven).
	 * @param isValidated a boolean value to indicates if the document is validated or not.
	 * @return true if no error occurs.
	 * @throws HibouTechnicalException
	 */
    public boolean uploadDocument(String projectName, String filename, String type, boolean isValidated) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        try {
            Document oldDocument = getDocument(projectName, filename, type);
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            if (oldDocument == null) {
                Project project = new ProjectManager().getProject(projectName);
                Document document = new Document(type, filename, isValidated, false, project);
                session.save(document);
                tr.commit();
            }
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
        return true;
    }

    /**
	 * Return all downloadable documents of a projet.
	 * @param projectName the project's name.
	 * @param type the documents' type (Archives, Documentations or Maven).
	 * @return the documents' list.
	 * @throws HibouTechnicalException
	 */
    @SuppressWarnings("unchecked")
    public List<Document> getDownloadableDocuments(String projectName, String type) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            Criteria criteria = session.createCriteria(Document.class).add(Restrictions.eq("project.id", projectName)).add(Restrictions.eq("type", type)).add(Restrictions.eq("validated", true)).add(Restrictions.eq("deleted", false));
            return criteria.list();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
    }

    /**
	 * Return all project's documentations that are waiting for validation.
	 * @param projectName the project's name.
	 * @return the pending documentations' list.
	 * @throws HibouTechnicalException
	 */
    @SuppressWarnings("unchecked")
    public List<Document> getPendingDocumentations(String projectName) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            Criteria criteria = session.createCriteria(Document.class).add(Restrictions.eq("project.id", projectName)).add(Restrictions.eq("type", "Documentations")).add(Restrictions.eq("validated", false)).add(Restrictions.eq("deleted", false));
            return criteria.list();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
    }

    /**
	 * Return all project's undeleted documents.
	 * @param projectName the project's name.
	 * @param type the document's type (Archives, Documentations or Maven).
	 * @return the documents' list.
	 * @throws HibouTechnicalException
	 */
    @SuppressWarnings("unchecked")
    public List<Document> getDocuments(String projectName, String type) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            Criteria criteria = session.createCriteria(Document.class).add(Restrictions.eq("project.id", projectName)).add(Restrictions.eq("type", type)).add(Restrictions.eq("deleted", false));
            return criteria.list();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
    }

    /**
	 * Return all documents of a project.
	 * @param projectName the project's name.
	 * @return the documents' list.
	 * @throws HibouTechnicalException
	 */
    @SuppressWarnings("unchecked")
    public List<Document> getAllDocuments(String projectName) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            Criteria criteria = session.createCriteria(Document.class).add(Restrictions.eq("project.id", projectName));
            return criteria.list();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
    }

    /**
	 * Validate a documentation of a project.
	 * @param projectName the project's name.
	 * @param filename the documentation's name.
	 * @return true if no error occurs.
	 * @throws HibouTechnicalException
	 */
    public boolean validateDocumentations(String projectName, String filename) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            Document document = (Document) session.createCriteria(Document.class).add(Restrictions.eq("project.id", projectName)).add(Restrictions.eq("type", "Documentations")).add(Restrictions.eq("filename", filename)).uniqueResult();
            document.setValidated(true);
            session.update(document);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
        return true;
    }

    /**
	 * Delete a document of a project.
	 * @param projectName the project's name.
	 * @param filename the document's name.
	 * @param type the document's type (Archives, Documentations or Maven).
	 * @return true if no error occurs.
	 * @throws HibouTechnicalException
	 */
    public boolean deleteDocument(String projectName, String filename, String type) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            Document document = (Document) session.createCriteria(Document.class).add(Restrictions.eq("project.id", projectName)).add(Restrictions.eq("type", type)).add(Restrictions.eq("filename", filename)).uniqueResult();
            session.delete(document);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
        return true;
    }

    /**
	 * Get a document by its id.
	 * @param id the document's id.
	 * @return the document or null.
	 * @throws HibouTechnicalException
	 */
    public Document getDocument(long id) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        Document document = null;
        try {
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            document = (Document) session.get(Document.class, id);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
        return document;
    }

    /**
	 * Check if a project has an archive.
	 * @param projectName the project's name.
	 * @return true if the project has an archive, false otherwise.
	 * @throws HibouTechnicalException
	 */
    public boolean hasArchive(String projectName) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            Criteria criteria = session.createCriteria(Document.class).add(Restrictions.eq("project.id", projectName)).add(Restrictions.eq("type", "Archives"));
            return criteria.list().size() == 0 ? false : true;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
    }

    /**
	 * Get a document by its type, name and the project's name it belongs to.
	 * @param projectName the project's name.
	 * @param filename the document's name.
	 * @param type the document's type (Archives, Documentations or Maven).
	 * @return the document or null.
	 * @throws HibouTechnicalException
	 */
    public Document getDocument(String projectName, String filename, String type) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            Criteria criteria = session.createCriteria(Document.class).add(Restrictions.eq("project.id", projectName)).add(Restrictions.eq("type", type)).add(Restrictions.eq("filename", filename));
            return (Document) criteria.uniqueResult();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
    }

    /**
	 * Check if a document is validated.
	 * @param projectName the document's project's name.
	 * @param filename the document's name.
	 * @param type the document's type (Archives, Documentations or Maven).
	 * @return true if the document is validated, false otherwise.
	 * @throws HibouTechnicalException
	 */
    public boolean isDocumentValidated(String projectName, String filename, String type) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            Document document = getDocument(projectName, filename, type);
            return document == null ? false : document.isValidated();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
    }

    /**
	 * Set a document as deleted.
	 * @param projectName the document's project's name.
	 * @param filename the document's name.
	 * @param type the document's type (Archives, Documentations or Maven).
	 * @return true if no error occurs.
	 * @throws HibouTechnicalException
	 */
    public boolean setDocumentAsDeleted(String projectName, String filename, String type) throws HibouTechnicalException {
        Transaction tr = null;
        Session session = null;
        DocumentSchedulerManager manager = new DocumentSchedulerManager();
        try {
            session = HibernateUtil.openSession();
            tr = session.beginTransaction();
            Document document = (Document) session.createCriteria(Document.class).add(Restrictions.eq("project.id", projectName)).add(Restrictions.eq("type", type)).add(Restrictions.eq("filename", filename)).uniqueResult();
            document.setDeleted(true);
            session.update(document);
            tr.commit();
            manager.createRemovedDocument(document.getId());
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new HibouTechnicalException(e);
        } finally {
            session.close();
        }
        return true;
    }
}
