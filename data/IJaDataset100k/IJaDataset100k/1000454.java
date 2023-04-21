package net.sourceforge.pebble.service;

import net.sourceforge.pebble.ContentCache;
import net.sourceforge.pebble.comparator.StaticPageByNameComparator;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.dao.StaticPageDAO;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.StaticPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Service that encompasses all functionality related to getting, putting
 * and removing static pages.
 *
 * @author    Simon Brown
 */
public class StaticPageService {

    private static final Log log = LogFactory.getLog(StaticPageService.class);

    /**
   * Gets the list of static pages for the given blog.
   *
   * @param blog    the Blog
   * @return  a list of BlogEntry instances
   * @throws  StaticPageServiceException if something goes wrong
   */
    public List<StaticPage> getStaticPages(Blog blog) throws StaticPageServiceException {
        List<StaticPage> staticPages = new ArrayList<StaticPage>();
        try {
            DAOFactory factory = DAOFactory.getConfiguredFactory();
            StaticPageDAO dao = factory.getStaticPageDAO();
            staticPages.addAll(dao.loadStaticPages(blog));
        } catch (PersistenceException pe) {
            throw new StaticPageServiceException(blog, pe);
        }
        Collections.sort(staticPages, new StaticPageByNameComparator());
        return staticPages;
    }

    /**
   * Gets the page with the specified id.
   *
   * @param pageId   the id of the static page
   * @param blog    the Blog
   * @return  a Page instance, or null if the page couldn't be found
   * @throws  StaticPageServiceException if something goes wrong
   */
    public StaticPage getStaticPageById(Blog blog, String pageId) throws StaticPageServiceException {
        StaticPage staticPage;
        ContentCache cache = ContentCache.getInstance();
        try {
            staticPage = cache.getStaticPage(blog, pageId);
            if (staticPage != null) {
                log.debug("Got static page " + pageId + " from cache");
            } else {
                log.debug("Loading static page " + pageId + " from disk");
                DAOFactory factory = DAOFactory.getConfiguredFactory();
                StaticPageDAO dao = factory.getStaticPageDAO();
                staticPage = dao.loadStaticPage(blog, pageId);
                if (staticPage != null) {
                    staticPage.setPersistent(true);
                    cache.putStaticPage(staticPage);
                }
            }
        } catch (PersistenceException pe) {
            throw new StaticPageServiceException(blog, pe);
        }
        if (staticPage != null) {
            staticPage = (StaticPage) staticPage.clone();
        }
        return staticPage;
    }

    /**
   * Gets the static page with the specified name.
   *
   * @param name    the name of the static page
   * @param blog    the Blog
   * @return  a StaticPage instance, or null if the page couldn't be found
   * @throws  StaticPageServiceException if something goes wrong
   */
    public StaticPage getStaticPageByName(Blog blog, String name) throws StaticPageServiceException {
        String id = blog.getStaticPageIndex().getStaticPage(name);
        return getStaticPageById(blog, id);
    }

    /**
   * Puts the static page.
   *
   * @param   staticPage    the StaticPage instance to store
   * @throws  StaticPageServiceException if something goes wrong
   */
    public void putStaticPage(StaticPage staticPage) throws StaticPageServiceException {
        ContentCache cache = ContentCache.getInstance();
        DAOFactory factory = DAOFactory.getConfiguredFactory();
        StaticPageDAO dao = factory.getStaticPageDAO();
        Blog blog = staticPage.getBlog();
        synchronized (blog) {
            try {
                StaticPage sp = getStaticPageById(blog, staticPage.getId());
                if (!staticPage.isPersistent() && sp != null) {
                    staticPage.setDate(new Date(staticPage.getDate().getTime() + 1));
                    putStaticPage(staticPage);
                } else {
                    dao.storeStaticPage(staticPage);
                    staticPage.setPersistent(true);
                    cache.removeStaticPage(staticPage);
                }
                staticPage.getBlog().getSearchIndex().index(staticPage);
                staticPage.getBlog().getStaticPageIndex().index(staticPage);
            } catch (PersistenceException pe) {
                throw new StaticPageServiceException(blog, pe);
            }
        }
    }

    /**
   * Removes a static page.
   *
   * @param staticPage    the StaticPage instance to remove
   * @throws  StaticPageServiceException if something goes wrong
   */
    public void removeStaticPage(StaticPage staticPage) throws StaticPageServiceException {
        ContentCache cache = ContentCache.getInstance();
        DAOFactory factory = DAOFactory.getConfiguredFactory();
        StaticPageDAO dao = factory.getStaticPageDAO();
        Blog blog = staticPage.getBlog();
        try {
            dao.removeStaticPage(staticPage);
            cache.removeStaticPage(staticPage);
            staticPage.getBlog().getSearchIndex().unindex(staticPage);
            staticPage.getBlog().getStaticPageIndex().unindex(staticPage);
        } catch (PersistenceException pe) {
            cache.removeStaticPage(staticPage);
            throw new StaticPageServiceException(staticPage.getBlog(), pe);
        }
    }

    /**
   * Locks a given static page.
   *
   * @param staticPage    the static page to lock
   * @return  true if the page could be locked, false otherwise
   */
    public boolean lock(StaticPage staticPage) {
        if (staticPage.isPersistent()) {
            boolean success = DAOFactory.getConfiguredFactory().getStaticPageDAO().lock(staticPage);
            ContentCache.getInstance().removeStaticPage(staticPage);
            return success;
        } else {
            return true;
        }
    }

    /**
   * Unlocks a given static page.
   *
   * @param staticPage    the static page to unlock
   * @return  true if the page could be unlocked, false otherwise
   */
    public boolean unlock(StaticPage staticPage) {
        if (staticPage.isPersistent()) {
            boolean success = DAOFactory.getConfiguredFactory().getStaticPageDAO().unlock(staticPage);
            ContentCache.getInstance().removeStaticPage(staticPage);
            return success;
        } else {
            return true;
        }
    }
}
