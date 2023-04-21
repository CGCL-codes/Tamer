package uk.ac.ebi.intact.application.editor.business;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import uk.ac.ebi.intact.application.editor.exception.EmptyTopicsException;
import uk.ac.ebi.intact.application.editor.struts.framework.util.EditorConstants;
import uk.ac.ebi.intact.application.commons.util.UrlUtil;
import uk.ac.ebi.intact.model.Experiment;
import uk.ac.ebi.intact.model.Interaction;
import uk.ac.ebi.intact.model.Institution;
import uk.ac.ebi.intact.business.IntactHelper;
import uk.ac.ebi.intact.business.IntactException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * This class provides the general editor services common to all the users.
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: EditorService.java 4744 2006-03-29 13:14:26Z baranda $
 */
public class EditorService {

    /**
     * Only instance of this class.
     */
    private static EditorService ourInstance;

    /**
     * The editor resource bundle.
     */
    private ResourceBundle myResources;

    /**
     * Intact topic Types.
     */
    private ResourceBundle myTopics;

    /**
     * The topics already sorted in an alphebetical order;
     * cached to save recompuation.
     */
    private List<String> myTopicsCache = new ArrayList<String>();

    /**
     * The search server URL.
     */
    private String mySearchUrl;

    /**
     * The help server URL.
     */
    private String myHelpUrl;

    /**
     * The institution; only one instance among many users.
     */
    private static Institution myInstitution;

    static {
        try {
            ourInstance = new EditorService("uk.ac.ebi.intact.application.editor.EditorResources");
        } catch (Exception ex) {
            Logger.getLogger(EditorConstants.LOGGER).error("", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Returns the only instance of this class.
     * @return the only instance of this class. The instance can never be null.
     */
    public static EditorService getInstance() {
        return ourInstance;
    }

    /**
     * Returns the topic name for given class.
     * @param clazz the Class object to extract the tipic name.
     * @return the class name without the package prefix. The returned
     * value equals to given class's class name if there is no package
     * information associated with <code>clazz</code>
     */
    public static String getTopic(Class clazz) {
        String className = clazz.getName();
        int lastIdx = className.lastIndexOf('.');
        if (lastIdx != -1) {
            return className.substring(lastIdx + 1);
        }
        return className;
    }

    /**
     * Construts with the resource file.
     * @param name the name of the resource file.
     * @exception MissingResourceException thrown when the resource file is
     * not found.
     * @exception EmptyTopicsException thrown for an empty resource file.
     * @exception IntactException for errors in initializing the institution.
     */
    private EditorService(String name) throws MissingResourceException, EmptyTopicsException, IntactException {
        myResources = ResourceBundle.getBundle(name);
        myTopics = ResourceBundle.getBundle(myResources.getString("topics"));
        if (!myTopics.getKeys().hasMoreElements()) {
            throw new EmptyTopicsException("Editor topic resource file can't be empty");
        }
        CollectionUtils.addAll(myTopicsCache, myTopics.getKeys());
        Collections.sort(myTopicsCache);
        moveToFront(getTopic(Interaction.class));
        moveToFront(getTopic(Experiment.class));
        IntactHelper helper = new IntactHelper();
        try {
            myInstitution = helper.getInstitution();
        } finally {
            helper.closeStore();
        }
        assert myInstitution != null : "Institution not set";
    }

    /**
     * Returns the class name associated with the given topic.
     * @param topic the topic to search in the Intact types resource.
     * @return the classname saved under <code>topic</code>.
     */
    public String getClassName(String topic) {
        return myTopics.getString(topic);
    }

    /**
     * True if given type is recognized as a valid editable type. This
     * method is used when a type is specified as a parameter in an URL
     * to directly access an editor.
     * @param type the type to validate.
     * @return true if <code>type</code> exists among current list of types.
     */
    public boolean isValidTopic(String type) {
        return myTopicsCache.contains(type);
    }

    /**
     * Returns a collection of Intact types.
     * @return an <code>ArrayList</code> of Intact types. The list sorted on an
     * alphabetical order. Since this reference refers to this class's
     * internal cache, handle this reference with care (do not modify contents).
     */
    public Collection getIntactTypes() {
        return myTopicsCache;
    }

    /**
     * Returns the relative link to the search application.
     * @param request the request object to get the context path.
     * This is only used once when this method is called for the first time.
     * For subsequent calls, the cached value is returned.
     * @return the relative path to the search page.
     */
    public String getSearchURL(HttpServletRequest request) {
        if (mySearchUrl == null) {
            String relativePath = UrlUtil.absolutePathWithoutContext(request);
            mySearchUrl = relativePath.concat(myResources.getString("search.url"));
        }
        return mySearchUrl;
    }

    /**
     * Returns the relative link to the help page.
     * @param request the request object to get the context path.
     * This is only used once when this method is called for the first time.
     * For subsequent calls, the cached value is returned.
     * @return the relative path to the help page.
     */
    public String getHelpURL(HttpServletRequest request) {
        if (myHelpUrl == null) {
            String relativePath = UrlUtil.absolutePathWithoutContext(request);
            myHelpUrl = relativePath.concat(myResources.getString("help.url"));
        }
        return myHelpUrl;
    }

    /**
     * Retrieves the resource for given key from the editor resource file.
     * @param key the key to search for the resource.
     * @return the resource for <code>key</code> if it is found.
     */
    public String getResource(String key) {
        return myResources.getString(key);
    }

    /**
     * Retrieves the resource for given key from the editor resource file as an int.
     * @param key the key to search for the resource. This must be a key to an integer
     * property.
     * @return the resource for <code>key</code> if it is found as an integer.
     */
    public int getInteger(String key) {
        return Integer.parseInt(myResources.getString(key));
    }

    /**
     * A convenient method to return the interaction limit for JSPs. This method
     * is equivalent to calling {@link #getInteger(String)} with exp.interaction.limit
     * as the key.
     * @return the maximum number of interactions allowed to display in the experiment
     * editor.
     *
     * @see #getInteger(String)
     */
    public int getInteractionLimit() {
        return getInteger("exp.interaction.limit");
    }

    /**
     * A convenient method to return the interaction per page limit for JSPs.
     * This method is equivalent to calling {@link #getResource(String)} with
     * exp.interaction.page.limit as the key.
     * @return the maximum number of interactions allowed (per page) to display
     * in the experiment editor.
     *
     * @see #getResource(String)
     */
    public String getInteractionPageLimit() {
        return getResource("exp.interaction.page.limit");
    }

    /**
     * Returns the default xref qualifier.
     * @return the default xref qualifier as a string.
     */
    public String getDefaultXrefQualifier() {
        return getResource("default.xref.qualifier");
    }

    /**
     * @return the institution or the owner for new objects.
     *
     * <pre>
     * post: return != null
     * </pre>
     */
    public Institution getOwner() {
        return myInstitution;
    }

    /**
     * Returns an anchor name by (1). error message, (2) the dispatch event.
     * <code>null</code> is returned if no anchor was found for the above two.
     * This method is protected for a subclass to overide it. For example,
     * this allows an anchor to determine by analysing another method other than
     * default two methods.
     *
     * @param anchorMap the Map to search for anchors.
     * @param request the request holds the error message.
     * @param dispatch the variable to search the map (mots likely a submit
     * button name).
     * @return anchor appropriate anchor for (1) error in <code>request</code>
     * or (2) dispatch (in this order). <code>null</code> if no none found.
     */
    public String getAnchor(Map anchorMap, HttpServletRequest request, String dispatch) {
        String errorkey = Globals.ERROR_KEY;
        if (request.getAttribute(errorkey) != null) {
            ActionErrors errors = (ActionErrors) request.getAttribute(errorkey);
            ActionError error = (ActionError) errors.get().next();
            String key = error.getKey();
            if (anchorMap.containsKey(key)) {
                return (String) anchorMap.get(key);
            }
        }
        return anchorMap.containsKey(dispatch) ? (String) anchorMap.get(dispatch) : null;
    }

    /**
     * Moves the given item to the front of the topics list.
     * @param item the item to move; this is only moved if it exists.
     */
    private void moveToFront(String item) {
        int pos = myTopicsCache.indexOf(item);
        if (pos != -1) {
            myTopicsCache.remove(pos);
            myTopicsCache.add(0, item);
        }
    }
}
