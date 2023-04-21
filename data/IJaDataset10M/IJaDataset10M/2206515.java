package org.marcont2.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.marcont2.commons.ThreadModel;
import org.marcont2.commons.VersionModel;
import org.marcont2.exceptions.SemVersionInitializationException;
import org.marcont2.ontomanagement.OntologyManager;
import org.marcont2.repobrowse.RepositoryBrowser;
import org.marcont2.sesame.Comment;
import org.marcont2.sesame.Rate;
import org.marcont2.sesame.SesameConnection;
import org.marcont2.usermanagement.DRMRights;
import org.marcont2.usermanagement.User;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.semversion.LoginException;

/**
 * @author Szymon Pająk, Maciej Dabrowski
 */
public class MarcOntLogicBean {

    /**
     * A handle to the unique MarcOntLogicBean instance
     */
    private static MarcOntLogicBean _molb = null;

    private RepositoryBrowser repositoryBrowser = null;

    private OntologyManager ontologyManager = null;

    private SesameConnection sesameConnection = null;

    private User userFoaf = null;

    public static MarcOntLogicBean instance() {
        if (null == _molb) {
            try {
                _molb = new MarcOntLogicBean();
            } catch (SemVersionInitializationException ex) {
                Logger.getLogger(MarcOntLogicBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return _molb;
    }

    protected MarcOntLogicBean() throws SemVersionInitializationException {
        try {
            repositoryBrowser = new RepositoryBrowser();
            ontologyManager = new OntologyManager();
            sesameConnection = new SesameConnection();
        } catch (SemVersionInitializationException ex) {
            Logger.getLogger(MarcOntLogicBean.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    /**
     * Adds a child version to thread
     * @param thName - thread name
     * @param vm - child you add
     * @param parentUri - parent to be
     * @throws java.lang.Exception
     */
    public URI addChildToVersion(String thName, VersionModel vm, String parentUri, boolean isver) throws Exception {
        URI uri = ontologyManager.addChildToVersion(thName, vm, parentUri, isver);
        try {
            addThreadAuthor(uri.toString());
        } catch (Exception e) {
            addThreadAuthor(getUserFoaf().getEmail(), uri.toString());
        }
        return uri;
    }

    /**
     * Adds new thread to repository
     * @param name - thread name
     * @param desc - thread comment
     * @throws java.lang.Exception
     */
    public void addNewThread(String name, String desc, int dist, boolean and, int level) throws Exception {
        System.out.println(name + " " + desc + " " + dist + " " + and + " " + level);
        URI uri = ontologyManager.addNewThread(name, desc);
        addThreadAuthor(uri.toString());
        sesameConnection.addDRMRights(uri.toString(), dist, and, level);
    }

    /**
     * Adds root version to thread
     * @param thName - thread name
     * @param vm - model of added version
     * @throws java.lang.Exception
     * @return 
     */
    public URI addFirstVersionToThread(String thName, VersionModel vm) throws Exception {
        URI uri = ontologyManager.addFirstVersionToThread(thName, vm);
        try {
            addThreadAuthor(uri.toString());
        } catch (Exception e) {
            addThreadAuthor(getUserFoaf().getEmail(), uri.toString());
        }
        return uri;
    }

    /**
     * Deletes thread 
     * @param trim
     */
    public void deleteThread(String name) {
        URI uri = repositoryBrowser.deleteThread(name);
    }

    /**
     * Returns how many threads we have in repository
     * @return
     */
    public int getNumberOfThreads() {
        return repositoryBrowser.getNumberOfThreads();
    }

    /**
     * Gets first, root, version of thread
     * @param name - thread name
     * @return
     */
    public VersionModel getRootVersionOfThread(String name) {
        return repositoryBrowser.getRootVersionOfThread(name);
    }

    /**
     * Returns list of all threads for a given user
     * @return
     */
    public ThreadModel[] getThreads() {
        ThreadModel[] tm = repositoryBrowser.getThreads();
        List<ThreadModel> templist = new ArrayList<ThreadModel>();
        for (int i = 0; i < tm.length; i++) {
            if (isThreadVisible(tm[i].getURI().trim()) == true) templist.add(tm[i]);
        }
        tm = new ThreadModel[templist.size()];
        for (int i = 0; i < tm.length; i++) {
            tm[i] = templist.get(i);
        }
        return tm;
    }

    /**
     * Returns list of all threads
     * @return
     */
    public ThreadModel[] getAllThreads() {
        ThreadModel[] tm = repositoryBrowser.getThreads();
        List<ThreadModel> templist = new ArrayList<ThreadModel>();
        for (int i = 0; i < tm.length; i++) {
            templist.add(tm[i]);
        }
        tm = new ThreadModel[templist.size()];
        for (int i = 0; i < tm.length; i++) {
            tm[i] = templist.get(i);
        }
        return tm;
    }

    /**
     * Returns string with the whole repository content (use carefully)
     * @return
     */
    public String getRepositoryDump() {
        try {
            String dump = repositoryBrowser.getRepositoryDump();
            return dump;
        } catch (LoginException ex) {
            Logger.getLogger(MarcOntLogicBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * Returns thread description
     * @param trim - thread name
     * @return
     */
    public ThreadModel getThread(String trim) {
        return repositoryBrowser.getThread(trim);
    }

    /**
     * Returns thread description
     * @param uri - thread uri
     * @return
     */
    public ThreadModel getThread(URI uri) {
        return repositoryBrowser.getThread(uri);
    }

    /**
     * Adds author to a thread
     * 
     * @param ID - thread/version ID
     * @param mail - authors e-mail
     * @return
     */
    public boolean addThreadAuthor(String ID) {
        return getSesameConnection().addAuthor(ID);
    }

    /**
     * Adds author to a thread 2 
     * 
     * @param ID - thread/version ID
     * @param mail - authors e-mail
     * @return
     */
    public boolean addThreadAuthor(String email, String ID) {
        return getSesameConnection().addAuthor(email, ID);
    }

    /**
     * Returns the tread/version author
     * 
     * @param ID - thread/version ID
     * @return String - author's e-mail
     */
    public String getThreadAuthor(String ID) {
        return getSesameConnection().getAuthor(ID);
    }

    /**
     * Adds descriptio to a thread
     * 
     * @param ID - thread/version ID
     * @param description - authors e-mail
     * @return
     */
    public boolean addThreadDescription(String ID, String description) {
        return getSesameConnection().addDescription(ID, description);
    }

    /**
     * Returns the tread/version author
     * 
     * @param ID - thread/version ID
     * @return String - description
     */
    public String getThreadDescription(String ID) {
        return getSesameConnection().getDescription(ID);
    }

    /**
     * 
     * @param ID  - ID of the version/thread that is being commented
     * @param mail - mail adress of the author of the comment
     * @param text - the comment itself
     * @return
     */
    public boolean addThreadComment(String ID, String text) {
        return getSesameConnection().addComment(ID, text);
    }

    /**
     * Returns Comments 
     * 
     * @param ID - Thread's ID.
     * @return Collection of Comments(org.marcont2.sesame.Comment), that were the response to the query
     */
    public Collection<Comment> getThreadComments(String ID) {
        return getSesameConnection().getComments(ID);
    }

    /**
     * Updates description of a thread
     * 
     * @param ID - thread/version ID
     * @param description - authors e-mail
     * @return
     */
    public boolean updateThreadDescription(String ID, String description) {
        return getSesameConnection().updateDescription(ID, description);
    }

    /**
     * 
     * @param ID  - ID of the version/thread that is being rated
     * @param mail - mail adress of the author of the rating
     * @param rate - the rat itself
     * @return
     */
    public int addRating(String ID, String rate) {
        return getSesameConnection().addRating(ID, rate);
    }

    /**
     * Returns Rates 
     * 
     * @param ID - Thread's ID.
     * @return Collection of Rates(org.marcont2.sesame.Rate), that were the response to the query
     */
    public Collection<Rate> getRates(String ID) {
        return getSesameConnection().getRates(ID);
    }

    public double getAvgRate(String ID) {
        return getSesameConnection().getAvgRating(ID);
    }

    /**
     * Adds file attachment to the thread
     * 
     * @param ID - thread/version ID (uri)
     * @param file - file attachment to be added
     * @return
     */
    public boolean addThreadFileAttachment(String ID, FileAttachment fa) {
        return getSesameConnection().addFileAttachment(ID, fa);
    }

    /**
     * Returns the file attachment of the given thread
     * 
     * @param ID - thread/version ID (uri)
     * @return FileAttachment
     */
    public FileAttachment getThreadFileAttachment(String ID) {
        return getSesameConnection().getFileAttachment(ID);
    }

    /**
     * Returns all children of specified version
     * @param thread - thread name
     * @param uRI - parent version uri
     * @return
     * @throws java.lang.Exception
     */
    public List getVersionChildren(String thread, String uRI) throws Exception {
        return ontologyManager.getVersionChildren(thread, uRI);
    }

    /**
     * Retruns VersionModel with (almost) complete description of selected 
     * version
     * @param thName - thread name 
     * @param URI - version uri
     * @return
     * @throws java.lang.Exception
     */
    public VersionModel getVersionDescription(String thName, String URI) throws Exception {
        return ontologyManager.getVersionDescription(thName, URI);
    }

    /**
     * Retruns VersionModel with description of selected version with content model
     * @param thName - thread name 
     * @param URI - version uri
     * @return
     * @throws java.lang.Exception
     */
    public VersionModel getVersionDescriptionWithModel(String thName, String URI) throws Exception {
        return ontologyManager.getVersionDescriptionWithModel(thName, URI);
    }

    public DRMRights getDrmForThread(String URI) {
        return DRMRights.getDrmRightsForThrerad(URI);
    }

    public boolean updateDrmForThread(String URI, int dist, boolean and, int level) {
        return sesameConnection.updateDRMRights(URI, dist, and, level);
    }

    public boolean isThreadVisible(String URI) {
        return DRMRights.isThreadVisible(URI);
    }

    /**
     * Set version as release (whatever that means)
     * @param thName
     * @param URI
     */
    public void setAsRelease(String thName, String URI) {
        ontologyManager.setAsRelease(thName, URI);
    }

    public List<String[]> getDiff(String thread, String uri1, String uri2, String reasoner) throws Exception {
        List<String[]> result = null;
        if (reasoner.equals("none")) return ontologyManager.getSyntacticDiff(thread, uri1, uri2); else if (reasoner.equals("rdfs") || reasoner.equals("owl") || reasoner.equals("rdfsAndOwl")) return ontologyManager.getSemanticDiff(thread, uri1, uri2, reasoner); else return result;
    }

    public List<String[]> getSyntacticDiff(String thread, String uri1, String uri2) throws Exception {
        return ontologyManager.getSyntacticDiff(thread, uri1, uri2);
    }

    public List<String[]> getSemanticDiff(String thread, String uri1, String uri2, String reasoner) throws Exception {
        return ontologyManager.getSemanticDiff(thread, uri1, uri2, reasoner);
    }

    public void setUserFoaf(User userFoaf) {
        this.userFoaf = userFoaf;
    }

    public User getUserFoaf() {
        return userFoaf;
    }

    public SesameConnection getSesameConnection() {
        return sesameConnection;
    }
}
