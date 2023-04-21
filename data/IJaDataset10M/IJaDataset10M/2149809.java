package org.devtools.webtrans.util;

import java.util.Enumeration;
import org.devtools.util.adt.DirectedGraph;
import org.devtools.util.adt.JDKDirectedGraph;
import org.devtools.webtrans.BadResourceException;
import org.devtools.webtrans.ContentDependencies;
import org.devtools.webtrans.TranslatorServices;

/**
 * Implements a dependency graph by URL.
 *
 * @author rus@devtools.org
 *
 **/
public class DependencyGraph implements ContentDependencies {

    /** 
		 * The services for this document type.
		 **/
    protected TranslatorServices srv;

    /** 
		 * A graph where each node is a document URL and each edge from A
		 * to B represents the fact that if B is changed, A should
		 * become dirty.
		 **/
    private DirectedGraph changeDep = new JDKDirectedGraph();

    /** 
		 * A graph where each node is a document URL and each edge from A
		 * to B represents the fact that if B is created, A should
		 * become dirty.
		 **/
    private DirectedGraph createDep = new JDKDirectedGraph();

    /** 
		 * A graph where each node is a document URL and each edge from A
		 * to B represents the fact that if B is deleted, A should
		 * become dirty.
		 **/
    private DirectedGraph deleteDep = new JDKDirectedGraph();

    /** 
		 * Initializes the graph with the given TranslatorServices.  The
		 * graph can assume that this method will be called exactly
		 * once, before any other method is called.
		 *
		 * @param srv The services that this TranslatorManager should use
		 * for all of its service needs.
		 **/
    public void init(TranslatorServices srv) {
        this.srv = srv;
    }

    /** 
		 * implements a generic version of adding a dependent
		 **/
    private void addDep(String childURL, String parentURL, DirectedGraph dep) throws BadResourceException {
        srv.assertValidURL(childURL);
        srv.assertValidURL(parentURL);
        dep.addEdge(childURL, parentURL);
        D.msg("detail", "dependency: " + childURL + " -> " + parentURL);
    }

    /**
		 * Generic version of returning the dependents
		 **/
    private Enumeration getDep(String URL, DirectedGraph dep) throws BadResourceException {
        srv.assertValidURL(URL);
        return dep.getParents(URL);
    }

    /** 
		 * implements a generic version of removing a dependent
		 **/
    private void removeDep(String childURL, String parentURL, DirectedGraph dep) throws BadResourceException {
        srv.assertValidURL(childURL);
        srv.assertValidURL(parentURL);
        dep.removeEdge(childURL, parentURL);
        D.msg("detail", "nodep: " + childURL + " -> " + parentURL);
    }

    /**
		 * implements a generic version of removing all outgoing dependencies
		 **/
    private void removeAllDeps(String URL, DirectedGraph dep) throws BadResourceException {
        srv.assertValidURL(URL);
        dep.removeAllOutgoingEdges(URL);
    }

    /** 
		 * Creates a dependency between the given pages, used for
		 * determining how to refresh caching information.  Once added,
		 * the page at childURL should be considered "dirty" if the page
		 * at parentURL is created.
		 *
		 * @param childURL The page to become dependent on the other page.
		 *
		 * @param parentURL The page that the other page depends on.
		 **/
    public void addCreateDependency(String childURL, String parentURL) throws BadResourceException {
        addDep(childURL, parentURL, createDep);
    }

    /** 
		 * Removes a dependency between the given pages, used for
		 * determining how to refresh caching information.  Once removed,
		 * the page at childURL will not be considered "dirty" just
		 * because the page at parentURL was created.
		 *
		 * @param childURL The page to become independent of the other page.
		 *
		 * @param parentURL The page that the other page should no longer
		 * depend on.
		 **/
    public void removeCreateDependency(String childURL, String parentURL) throws BadResourceException {
        removeDep(childURL, parentURL, createDep);
    }

    /** 
		 * Removes all of the dependencies that the given URL has on other
		 * URLs.  This method will NOT remove incoming dependencies of
		 * this URL.
		 *
		 * @param URL The page that will no longer depend on any pages.
		 **/
    public void removeAllCreateDependencies(String URL) throws BadResourceException {
        removeAllDeps(URL, createDep);
    }

    /**
		 * Returns an enumeration of the URLs of all of the pages that are
		 * dependent on creations of the given page.
		 **/
    public Enumeration getCreateDependencies(String URL) throws BadResourceException {
        return getDep(URL, createDep);
    }

    /** 
		 * Creates a dependency between the given pages, used for
		 * determining how to refresh caching information.  Once added,
		 * the page at childURL should be considered "dirty" whenever the
		 * page at parentURL has changed.
		 *
		 * @param childURL The page to become dependent on the other page.
		 *
		 * @param parentURL The page that the other page depends on.
		 **/
    public void addChangeDependency(String childURL, String parentURL) throws BadResourceException {
        addDep(childURL, parentURL, changeDep);
    }

    /** 
		 * Removes a dependency between the given pages, used for
		 * determining how to refresh caching information.  Once removed,
		 * the page at childURL will not be considered "dirty" just
		 * because the page at parentURL has changed.
		 *
		 * @param childURL The page to become independent of the other page.
		 *
		 * @param parentURL The page that the other page should no longer
		 * depend on.
		 **/
    public void removeChangeDependency(String childURL, String parentURL) throws BadResourceException {
        removeDep(childURL, parentURL, changeDep);
    }

    /** 
		 * Removes all of the dependencies that the given URL has on other
		 * URLs.  This method will NOT remove incoming dependencies of
		 * this URL.
		 *
		 * @param URL The page that will no longer depend on any pages.
		 **/
    public void removeAllChangeDependencies(String URL) throws BadResourceException {
        removeAllDeps(URL, changeDep);
    }

    /**
		 * Returns an enumeration of the URLs of all of the pages that are
		 * dependent on changes to the given page.
		 **/
    public Enumeration getChangeDependencies(String URL) throws BadResourceException {
        return getDep(URL, changeDep);
    }

    /** 
		 * Creates a dependency between the given pages, used for
		 * determining how to refresh caching information.  Once added,
		 * the page at childURL should be considered "dirty" whenever the
		 * page at parentURL is deleted.
		 *
		 * @param childURL The page to become dependent on the other page.
		 *
		 * @param parentURL The page that the other page depends on.
		 **/
    public void addDeleteDependency(String childURL, String parentURL) throws BadResourceException {
        addDep(childURL, parentURL, deleteDep);
    }

    /** 
		 * Removes a dependency between the given pages, used for
		 * determining how to refresh caching information.  Once removed,
		 * the page at childURL will not be considered "dirty" just
		 * because the page at parentURL has been deleted.
		 *
		 * @param childURL The page to become independent of the other page.
		 *
		 * @param parentURL The page that the other page should no longer
		 * depend on.
		 **/
    public void removeDeleteDependency(String childURL, String parentURL) throws BadResourceException {
        removeDep(childURL, parentURL, deleteDep);
    }

    /** 
		 * Removes all of the dependencies that the given URL has on other
		 * URLs.  This method will NOT remove incoming dependencies of
		 * this URL.
		 *
		 * @param URL The page that will no longer depend on any pages.
		 **/
    public void removeAllDeleteDependencies(String URL) throws BadResourceException {
        removeAllDeps(URL, deleteDep);
    }

    /**
		 * Returns an enumeration of the URLs of all of the pages that are
		 * dependent on deletions of the given page.
		 **/
    public Enumeration getDeleteDependencies(String URL) throws BadResourceException {
        return getDep(URL, deleteDep);
    }

    private org.devtools.util.debug.Debugger D = new org.devtools.util.debug.Debugger("DependencyGraph");

    {
        D.setDisplayGroups(true);
        D.enable("*");
    }
}
