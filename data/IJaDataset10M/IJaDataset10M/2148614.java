package org.nex.ts.server.tago.model;

import java.util.*;
import org.nex.ts.TopicSpacesException;
import org.nex.ts.smp.SubjectMapProvider;
import org.nex.ts.smp.api.IMapDataProvider;
import org.nex.ts.smp.api.ISubjectMap;
import org.nex.ts.smp.api.ISubjectProxy;
import org.nex.ts.smp.api.ITopicSpacesOntology;
import org.nex.ts.smp.api.IRoleBasedRelation;
import org.nex.ts.server.common.model.BaseModel;
import org.nex.ts.server.common.model.Ticket;
import org.nex.ts.server.common.model.Environment;
import org.nex.ts.server.tago.api.IBookmark;
import org.nex.ts.server.tago.api.ITaggableResource;
import org.nex.ts.server.tago.api.ITagomizerOntology;
import org.nex.ts.server.tago.api.ITagomizerTag;
import org.nex.ts.server.tago.api.ITagomizerUser;
import org.nex.ts.server.common.StringUtils;
import org.openrdf.model.Graph;

/**
 * 
 * @author park
 *
 */
public class UpdateWorkerThread extends BaseModel {

    private TagomizerModel host;

    private ISubjectMap theMap;

    private List<UpdatePojo> bookmarks = new ArrayList<UpdatePojo>();

    private List<UpdatePojo> subjectmarks = new ArrayList<UpdatePojo>();

    private boolean isRunning = true;

    /**
	 * This <code>graph</code> will stay null
	 */
    private Graph graph = null;

    public UpdateWorkerThread(TagomizerModel host) throws TopicSpacesException {
        super();
        this.host = host;
        theMap = host.getSubjectMap();
        new Worker().start();
    }

    public void updateBookmark(UpdatePojo pj) {
        synchronized (bookmarks) {
            bookmarks.add(pj);
            bookmarks.notify();
        }
    }

    public void updateSubjectmark(UpdatePojo pj) {
        synchronized (bookmarks) {
            subjectmarks.add(pj);
            bookmarks.notify();
        }
    }

    public void halt() {
        synchronized (bookmarks) {
            isRunning = false;
            bookmarks.notify();
        }
    }

    protected void finalize() {
        halt();
    }

    class Worker extends Thread {

        public Worker() {
        }

        public void run() {
            boolean haveBookmarks = false;
            boolean haveSubjectmarks = false;
            while (isRunning) {
                synchronized (bookmarks) {
                    haveBookmarks = (bookmarks.size() > 0);
                }
                synchronized (bookmarks) {
                    haveSubjectmarks = (subjectmarks.size() > 0);
                }
                if (!isRunning) return;
                if (haveBookmarks) {
                    synchronized (bookmarks) {
                        _updateBookmark(bookmarks.remove(0));
                    }
                }
                if (!isRunning) return;
                if (haveSubjectmarks) {
                    synchronized (bookmarks) {
                        _updateSubjectmark(subjectmarks.remove(0));
                    }
                }
                if (!isRunning) return;
                if (!haveBookmarks && !haveSubjectmarks) {
                    synchronized (bookmarks) {
                        try {
                            bookmarks.wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }

        void _updateBookmark(UpdatePojo pj) {
            try {
                String resourceURL = pj.proxyLocator;
                List<String> tagNames = pj.tagNames;
                int tagType = pj.tagType;
                String resourceTitle = pj.resourceTitle;
                String commentString = pj.commentString;
                long datestamp = pj.datestamp;
                Ticket credentials = pj.credentials;
                String userLocator = credentials.getOwner();
                String userURI = database.locatorToURI(userLocator);
                String bookmarkLocator = this.bookmarkToLocator(userLocator, resourceURL);
                environment.logDebug("UpdateWorkerThread.updateBookmark- " + bookmarkLocator + " " + resourceURL + " " + userLocator);
                boolean bmExists = theMap.existsProxyByURI(database.locatorToURI(bookmarkLocator));
                environment.logDebug("UpdateWorkerThread.updateBookmark-1 " + bookmarkLocator + " " + bmExists);
                if (!bmExists) {
                    ITagomizerUser user = host.getUser(userLocator);
                    environment.assertNotNull(user, "UpdateWorkerThread missing user " + userLocator);
                    database.installGraph(user);
                    environment.logDebug("UpdateWorkerThread.updateBookmark-2 " + user + " " + userLocator);
                    IBookmark bm = new BookmarkProxy(graph, database, bookmarkLocator, userLocator);
                    Thread.yield();
                    String resourceLocator = resourceToLocator(resourceURL);
                    boolean brExists = database.existsProxyByURI(database.locatorToURI(resourceLocator));
                    ITaggableResource br = host.getBookmarkableResource(resourceLocator);
                    environment.logDebug("UpdateWorkerThread.updateBookmark-3 " + resourceLocator + " " + br);
                    if (br == null) {
                        br = new ResourceProxy(graph, database, resourceLocator, userLocator);
                        environment.logDebug("UpdateWorkerThread.updateBookmark-4 " + resourceLocator + " " + br);
                        br.setResourceURL(resourceURL);
                        br.setRFC4151Property(RFC4151.toResourceURI("global", resourceURL));
                        br.setResourceTitle(resourceTitle);
                        br.addNameWithLanguage("Resource: " + resourceURL, ITopicSpacesOntology.DEFAULT_LANGUAGE);
                    } else {
                        if (brExists) database.installGraph(br);
                    }
                    environment.logDebug("UpdateWorkerThread.updateBookmark-5 ");
                    Thread.yield();
                    String username = user.getUserName();
                    if (username == null || username.equals("")) username = userLocator;
                    String descriptionString = "Bookmark createdBy: " + username + ", resource: " + resourceURL;
                    String nameString = "Bookmark-" + username + "-resourceURL";
                    bm.addNameWithLanguage(nameString, defaultLanguage);
                    bm.addDescription(descriptionString, defaultLanguage, userLocator);
                    bm.setCreationDate(datestamp);
                    bm.setCreator(userLocator);
                    bm.setBookmarkableResourceURL(resourceURL);
                    environment.logDebug("UpdateWorkerThread.updateBookmark-6 " + commentString);
                    if (commentString != null && !commentString.equals("")) bm.addNote(commentString, credentials);
                    Thread.yield();
                    environment.logDebug("UpdateWorkerThread.updateBookmark-7");
                    assertResourceUser(graph, br, user, userLocator);
                    assertBookmarkUser(graph, bm, user, userLocator);
                    assertBookmarkResource(graph, bm, br, userLocator);
                    cacheBookmark(bm);
                    Thread.yield();
                    Iterator<String> itr = tagNames.iterator();
                    ITagomizerTag t;
                    String ts;
                    environment.logDebug("UpdateWorkerThread.updateBookmark-9 " + tagType);
                    if (tagType == ITagomizerTag.STRING_TYPE) {
                        boolean tagExists = false;
                        String tlox;
                        while (itr.hasNext()) {
                            Thread.yield();
                            ts = itr.next().trim();
                            if (!ts.equals("")) {
                                tlox = tagStringToLocator(ts);
                                tagExists = database.existsProxyByURI(database.locatorToURI(tlox));
                                t = host.getTag(tlox);
                                if (t == null) {
                                    t = host.createStringTag(graph, ts, username, userLocator);
                                } else {
                                    if (tagExists) database.installGraph(t);
                                }
                                environment.logDebug("UpdateWorkerThread.updateBookmark-10 " + tlox + " " + t);
                                assertBookmarkTag(graph, bm, t, userLocator);
                                assertResourceTag(graph, br, t, userLocator);
                                assertTagUser(graph, t, user, userLocator);
                                environment.logDebug("UpdateWorkerThread.updateBookmark-10");
                                if (!tagExists) database.submitProxyForMerge(t); else {
                                    database.putGraph(t.getGraph(), t.getURIString());
                                    environment.federateUpdatedProxy(t);
                                }
                            }
                        }
                    } else {
                        environment.logError("UpdateWorkerThread.updateBookmark " + "ITagomizerTag.IMAGE_TYPE not implemented yet");
                        throw new TopicSpacesException("UpdateWorkerThread.updateBookmark " + "ITagomizerTag.IMAGE_TYPE not implemented yet");
                    }
                    environment.logDebug("UpdateWorkerThread.updateBookmark+ " + resourceURL);
                    database.submitProxyNoMerge(bm);
                    if (brExists) database.putGraph(br.getGraph(), br.getURIString()); else database.submitProxyNoMerge(br);
                    database.putGraph(user.getGraph(), user.getURIString());
                }
            } catch (Exception e) {
                environment.logError("UpdateWorkerThread.updateBookmark error " + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        void cacheBookmark(ISubjectProxy bookmark) throws TopicSpacesException {
            host.tagManager.newBookmark(bookmark);
            environment.logDebug("TagomizerModel.cacheBookmark+");
        }

        void assertResourceUser(Graph graph, ISubjectProxy resource, ISubjectProxy user, String userLocator) throws TopicSpacesException {
            IRoleBasedRelation x = theMap.createRoleBasedRelation(graph, TAGO.RESOURCE_USER_ASSOCIATION_TYPE.toString(), resource, user, TAGO.USERS_RESOURCE_ROLE.toString(), TAGO.RESOURCES_USER_ROLE.toString(), userLocator);
            database.submitProxyNoMerge(x);
        }

        void assertBookmarkUser(Graph graph, ISubjectProxy bookmark, ISubjectProxy user, String userLocator) throws TopicSpacesException {
            IRoleBasedRelation x = theMap.createRoleBasedRelation(graph, TAGO.BOOKMARK_USER_ASSOCIATION_TYPE.toString(), bookmark, user, TAGO.USERS_BOOKMARK_ROLE.toString(), TAGO.BOOKMARKS_USER_ROLE.toString(), userLocator);
            database.submitProxyNoMerge(x);
        }

        void assertBookmarkResource(Graph graph, ISubjectProxy bookmark, ISubjectProxy resource, String userLocator) throws TopicSpacesException {
            IRoleBasedRelation x = theMap.createRoleBasedRelation(graph, TAGO.BOOKMARK_RESOURCE_ASSOCIATION_TYPE.toString(), bookmark, resource, TAGO.RESOURCES_BOOKMARK_ROLE.toString(), TAGO.BOOKMARKS_RESOURCE_ROLE.toString(), userLocator);
            database.submitProxyNoMerge(x);
        }

        void assertBookmarkTag(Graph graph, ISubjectProxy bookmark, ISubjectProxy tag, String userLocator) throws TopicSpacesException {
            IRoleBasedRelation x = theMap.createRoleBasedRelation(graph, TAGO.BOOKMARK_TAG_ASSOCIATION_TYPE.toString(), bookmark, tag, TAGO.TAGS_BOOKMARK_ROLE.toString(), TAGO.BOOKMARKS_TAG_ROLE.toString(), userLocator);
            database.submitProxyNoMerge(x);
        }

        void assertResourceTag(Graph graph, ISubjectProxy resource, ISubjectProxy tag, String userLocator) throws TopicSpacesException {
            IRoleBasedRelation x = theMap.createRoleBasedRelation(graph, TAGO.RESOURCE_TAG_ASSOCIATION_TYPE.toString(), resource, tag, TAGO.TAGS_RESOURCE_ROLE.toString(), TAGO.RESOURCES_TAG_ROLE.toString(), userLocator);
            database.submitProxyNoMerge(x);
        }

        void assertTagUser(Graph graph, ISubjectProxy tag, ISubjectProxy user, String userLocator) throws TopicSpacesException {
            IRoleBasedRelation x = theMap.createRoleBasedRelation(graph, TAGO.TAG_USER_ASSOCIATION_TYPE.toString(), tag, user, TAGO.USERS_TAG_ROLE.toString(), TAGO.TAGS_USER_ROLE.toString(), userLocator);
            database.submitProxyNoMerge(x);
        }

        /**
		 * Create a bookmark URI from <code>userLocator</code>
		 * and <code>resourceURL</code>
		 * @param userLocator
		 * @param resourceURL
		 * @return
		 */
        String bookmarkToLocator(String userLocator, String resourceURL) {
            String result = userLocator + StringUtils.sanitizeString(resourceURL);
            return StringUtils.trimLength(result);
        }

        /**
		 * Create a resource URI from <code>resourceURL</code>
		 * @param userLocator
		 * @param resourceURL
		 * @return
		 */
        String resourceToLocator(String resourceURL) {
            String result = StringUtils.sanitizeString(resourceURL);
            return StringUtils.trimLength(result);
        }

        String tagStringToLocator(String tagString) {
            String result = tagString.replaceAll(" ", "_");
            result = StringUtils.sanitizeString(result) + ITagomizerOntology.TAG_SUFFIX;
            return result;
        }

        void _updateSubjectmark(UpdatePojo pj) {
            String proxyLocator = pj.proxyLocator;
            List<String> tagNames = pj.tagNames;
            int tagType = pj.tagType;
            String resourceTitle = pj.resourceTitle;
            String commentString = pj.commentString;
            long datestamp = pj.datestamp;
            Ticket credentials = pj.credentials;
        }
    }
}
