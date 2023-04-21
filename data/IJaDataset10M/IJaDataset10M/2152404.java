package com.io_software.utils.web;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.server.RemoteObject;
import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.NotBoundException;
import java.net.PasswordAuthentication;
import java.net.MalformedURLException;
import java.net.Authenticator;
import java.net.InetAddress;
import java.io.IOException;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Vector;
import java.util.Enumeration;
import javax.naming.NamingException;

/** Registers itself with the RMI naming registry and allows remotely
    managing a set of {@link CrawlerImpl} objects.

    @author Axel Uhl
    @version $Id: CrawlerManagerImpl.java,v 1.4 2001/02/25 13:54:24 aul Exp $
  */
public class CrawlerManagerImpl extends UnicastRemoteObject implements CrawlerManager, Cloneable {

    /** does nothing but possibly throw a <tt>RemoteException</tt>. By
	making it provate, this enforces the singleton pattern.
      
	@see #getAdministrationServer
    */
    private CrawlerManagerImpl(String rootName) throws RemoteException {
        this.rootName = rootName;
        crawlers = new Vector();
    }

    /** This method is called immediately after the instance has been
	loaded from the database for the first time in this VM. This means
	that the crawlers managed by the instance have to be re-started.<p>
	
	The object is (re-)exported to the RMI subsystem (see {@link
	UnicastRemoteObject#exportObject(java.rmi.RemoteObject)}) by
	cloning it. The clone is assigned the {@link #theCrawlerManager}
	variable. All contained crawlers are cloned as well in order
	to re-export them to the RMI subsystem.

	@param session used to wrap starting the crawlers in a
	transaction. The expected modifications on the crawlers are
	the resets on their request managers.
	@param restart if <tt>false</tt>, the crawlers will only be
	cloned but not restarted.
      */
    public void initializeAfterLoad(boolean restart) throws RemoteException, CloneNotSupportedException {
        synchronized (CrawlerManagerImpl.class) {
            theCrawlerManager = (CrawlerManagerImpl) this.clone();
            Vector newCrawlers = new Vector();
            for (Enumeration e = crawlers.elements(); e.hasMoreElements(); ) {
                CrawlerImpl c = (CrawlerImpl) e.nextElement();
                CrawlerImpl newCrawler = (CrawlerImpl) c.clone();
                newCrawlers.addElement(newCrawler);
                if (restart) newCrawler.restart();
            }
            theCrawlerManager.crawlers = newCrawlers;
        }
    }

    /** Tries to retrieve the crawler manager on the specified
	server. This may fail for several reasons (e.g. no manager
	present on that host, communications problem etc.), in which
	case an exception is thrown.<p>
	
	The crawler manager is looked up in the remote RMI registry expected
	to run on the standard port. The name looked up is defined by the
	member {@link #RMI_REGISTRY_PORT}.
	
	@param addr the host on which to look for the crawler manager
	@return a reference to the found manager
      */
    public static CrawlerManager getCrawlerManager(InetAddress addr) throws RemoteException, NotBoundException, MalformedURLException {
        String host = addr.getHostAddress();
        CrawlerManager cm = (CrawlerManager) Naming.lookup("rmi://" + host + ":" + CrawlerManagerImpl.RMI_REGISTRY_PORT + "/" + CrawlerManagerImpl.CRAWLER_MANAGER_NAME);
        return cm;
    }

    /** Returns the single instance of this class and creates it if
	not yet done.  This implements the singleton pattern. The
	constructor is private and hence can't be called otherwise
	from outside this class.<p>
	
	Note: Exactly one instance of this class is persistend in the
	GemStone/J repository using the root name specified by
	{@link #GS_ROOT_NAME}. This method proceeds in three steps:
	If there is already a persistent instance of this class and
	it has been retrieved, return it. If nothing has been retrieved
	yet but it exists already in the database, retrieve and return
	it. Otherwise, create the instance and make it persistent in
	the GemStone/J repository.
	
	@param dbRootName tells the name under which to attempt to look up the
		crawler manager as a serialized object in the file system. If
		<tt>null</tt>, the default root name (see {@link
		#GS_ROOT_NAME}) is used.
	@param overwrite If <tt>false</tt> and a crawler manager is found at
		the specified root name, it is used. If <tt>true</tt>,
		no matter if a crawler manager exists under that name
		already, it is overwritten with a new copy. <b>NOTE: This
		will erase all collected data associated with this
		crawler manager up to that time from the database.</b>
	@param restart If <tt>true</tt>, the crawlers will be re-initialized
		if a crawler manager was found under the specified name
		in the database (see {@link initializeAfterLoad}). If
		<tt>false</tt>, the crawlers are left uninitialized after
		loading and {@link Crawler#restart} must be called
		explicitly on all of them in order to get the
		crawlers restarted.
	
	@return the singleton instance of this class
    */
    public static CrawlerManagerImpl getCrawlerManager(String dbRootName, boolean overwrite, boolean restart) throws RemoteException, IOException {
        if (theCrawlerManager == null) {
            String rootName = dbRootName;
            if (rootName == null) rootName = GS_ROOT_NAME;
            if (!overwrite) {
                System.out.println("Looking for crawler manager in " + "file system");
                try {
                    File f = new File(dbRootName);
                    if (f.exists()) {
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                        theCrawlerManager = (CrawlerManagerImpl) ois.readObject();
                        ois.close();
                        theCrawlerManager.initializeAfterLoad(restart);
                    } else {
                        System.out.println("Not found. Creating...");
                        theCrawlerManager = createAndStore(rootName);
                    }
                } catch (CloneNotSupportedException cnse) {
                    System.err.println("Couldn't re-export objects since " + "clone was not supported.");
                    cnse.printStackTrace();
                } catch (ClassNotFoundException cnfe) {
                    System.err.println("Couldn't read crawler manager:");
                    cnfe.printStackTrace();
                }
            } else {
                System.out.println("Creating...");
                theCrawlerManager = createAndStore(rootName);
            }
        }
        return theCrawlerManager;
    }

    /** Creates a new crawler manager, enters it into the DB with
	the given root name in a transaction, commits that transaction
	and returns the created crawler manager.
	
	@param session the database session to use for transaction handling
	@param rootName the root name to use. Does not look at
		{@link #GS_ROOT_NAME}.
	@return the created crawler manager
      */
    private static CrawlerManagerImpl createAndStore(String rootName) throws RemoteException, IOException {
        CrawlerManagerImpl result = new CrawlerManagerImpl(rootName);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rootName));
        oos.writeObject(result);
        oos.close();
        return result;
    }

    /** stores this crawler manager under the root name that has been
	configured during its creation by serializing it into the corresponding
	file.
    */
    public void persist() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rootName));
        oos.writeObject(this);
        oos.close();
    }

    /** This makes the class startable as a server application. It will
	create one instance of this class, locate / create an RMI registry
	on port 1099 and register the created server object with the registry.
	The name can be queried using the class attribute
	<tt>CRAWLER_MANAGER_NAME</tt>.<p>
	
        @param args ignored
	@see wtools.Project
      */
    public static void main(String[] args) {
        try {
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(RMI_REGISTRY_PORT);
            } catch (ExportException e) {
                System.out.println("Registry already exists, binding to it...");
                registry = LocateRegistry.getRegistry(RMI_REGISTRY_PORT);
            }
            String dbRootName = null;
            if (args.length > 0 && (args[0].equals("-h") || args[0].equals("--help") || args[0].equals("-?"))) usage(); else {
                if (args.length > 0) dbRootName = args[0];
                boolean overwrite = false;
                if (args.length > 1 && args[1].equals("overwrite")) overwrite = true;
                CrawlerManagerImpl crawlerManager = createAndExportCrawlerManager(registry, dbRootName, overwrite);
                AuthenticationHandler authenticationHandler = new AuthenticationHandler(crawlerManager);
                Authenticator.setDefault(authenticationHandler);
            }
        } catch (Exception e) {
            System.err.println("Could not register server for the following reason:");
            e.printStackTrace();
        }
    }

    /** Displays a usage message to <tt>stdout</tt> */
    private static void usage() {
        System.out.println("Usage: java ec.metrics.CrawlerManagerImpl " + "[ --help | <rootName> [ \"overwrite\" ]]");
        System.out.println("where --help displays this message, <rootName> tells");
        System.out.println("the name under which to register the crawler manager");
        System.out.println("in the database, and if specified, overwrite will");
        System.out.println("cause an existing object with this name to be");
        System.out.println("overwritten. If no <rootName> is given, a default");
        System.out.println("value will be used.");
    }

    /** creates an instance of <tt>CrawlerManagerImpl</tt> and exports it with
	the name given by the administration server class constant
	<tt>CRAWLER_MANAGER_NAME</tt>.

	@param registry the RMI naming registry to bind the found / created
		object to.	
	@param rootName tells the name under which to attempt to look
		up the crawler manager as a root in the database. If
		<tt>null</tt>, the default root name (see
		{@link #DB_ROOT_NAME}) is used.
	@param overwrite If <tt>false</tt> and a crawler manager is found at
		the specified root name, it is used. If <tt>true</tt>,
		no matter if a crawler manager exists under that name
		already, it is overwritten with a new copy. <b>NOTE: This
		will erase all collected data associated with this
		crawler manager up to that time from the database.</b>
	@return the created crawler manager
	@see wtools.CrawlerManagerImpl#CRAWLER_MANAGER_NAME
      */
    private static CrawlerManagerImpl createAndExportCrawlerManager(Registry registry, String rootName, boolean overwrite) throws RemoteException, IOException {
        CrawlerManager a = getCrawlerManager(rootName, overwrite, true);
        registry.rebind(CrawlerManagerImpl.CRAWLER_MANAGER_NAME, a);
        System.out.println("bound crawler manager to registry");
        return (CrawlerManagerImpl) a;
    }

    /** adds a crawler to the set of maintained crawlers. The crawler
	is initialized with the specified set of URLs and starts
	immediately to crawl on these documents concurrently.<p>
	
	@param urls a vector containing {@link URL} objects that the
		new crawler shall pick up and start crawling on
	@return the created crawler
      */
    public synchronized Crawler addCrawler(Vector urls) throws RemoteException {
        CrawlerImpl crawler;
        synchronized (CrawlerManagerImpl.class) {
            crawler = new CrawlerImpl(urls, this);
            crawlers.addElement(crawler);
        }
        return crawler;
    }

    /** adds a crawler to the set of maintained crawlers. The crawler
	is initialized with the specified set of URLs and starts
	immediately to crawl on these documents concurrently.<p>
	
	@param urls a vector containing {@link URL} objects that the
		new crawler shall pick up and start crawling on
	@param threads the number of threads to allow for the new
		crawler for concurrent document retrieval and analysis
	@return the created crawler
      */
    public synchronized Crawler addCrawler(Vector urls, int threads) throws RemoteException {
        CrawlerImpl crawler;
        synchronized (CrawlerManagerImpl.class) {
            crawler = new CrawlerImpl(urls, threads, this);
            crawlers.addElement(crawler);
        }
        return crawler;
    }

    /** retrieves the set of crawlers managed by this object. No
	guanrantees are given regarding the state of these crawlers.
	They may be stopped, suspended and resumed independently of
	this interface.
	
	@return a vector containing references to the {@link Crawler}
		objects managed by this object. Note that when accessing
		this via RMI, these references will be remote RMI
		references.
      */
    public Vector getCrawlers() {
        return crawlers;
    }

    /** Removes the specified crawler from the set of crawlers. Since
	the passed parameter might be an RMI reference to the object,
	the passed object is compared to the result of the <tt>toStub</tt>
	method applied to the crawlers stored locally.<p>
	
	Removing a crawler is performed in a transaction that commits
	before this method returns.
    
	@param crawler a reference to the crawler object to remove.
      */
    public void removeCrawler(Crawler crawler) throws NoSuchObjectException, RemoteException {
        synchronized (CrawlerManagerImpl.class) {
            CrawlerImpl c = null;
            Remote crawlerStub = RemoteObject.toStub(crawler);
            for (Enumeration e = crawlers.elements(); c == null && e.hasMoreElements(); ) {
                CrawlerImpl ci = (CrawlerImpl) e.nextElement();
                Remote stub = RemoteObject.toStub(ci);
                if (stub.equals(crawlerStub)) c = ci;
            }
            if (c != null) crawlers.remove(c);
        }
    }

    /** The VM-wide authenticator can use this method to present
	an authentication request to the crawler manager. The authenticator
	cannot know which individual crawler was responsible for the
	original URL request that caused the authentication request
	callback as can't the crawler manager.<p>
	
	Considering the usual case where the number of crawlers
	managed by a manager is relatively small, it seems sensible
	to forward the authentication request to all managed crawlers
	instead of building a complex management trying to map
	currently retrieved URLs to crawlers and then trying to map
	the authentication requests against the URLs<p>
	
	It may happen that two two crawlers managed by the same manager
	respond to the same request. Then the manager cannot
	uniquely resolve the authentication reqeust to one crawler.
	If additionally more than one crawler responds with a
	non-<tt>null</tt> result, one is picked randomly.
	
	@param ar the authentication request's details
	@return the password authentication returned by the crawler
		that the request was forwarded to. In case of the
		above mentioned multi-crawler dispatch, one
		of the returned non-<tt>null</tt> password authentications
		is chosen randomly if a non-<tt>null</tt> result is
		returned by one of the crawlers.
	@see #getCrawlersForAuthenticationRequest
      */
    public PasswordAuthentication requestAuthentication(AuthenticationRequest ar) {
        PasswordAuthentication result = null;
        for (Enumeration e = crawlers.elements(); e.hasMoreElements(); ) {
            CrawlerImpl ci = (CrawlerImpl) e.nextElement();
            PasswordAuthentication tmpResult = ci.requestAuthentication(ar);
            if (tmpResult != null) result = tmpResult;
        }
        return result;
    }

    /** Shuts the VM down by calling <tt>System.exit(0)</tt>.
	This, of course, is hazardous to all crawlers managed by
	this manager.
      */
    public void exit() {
        System.exit(0);
    }

    /** the singleton instance of this class */
    private static CrawlerManagerImpl theCrawlerManager = null;

    /** the set of crawlers maintained by this managers */
    private Vector crawlers;

    /** the name of the file in which this crawler is persisted */
    private String rootName;

    /** the name of the database in which the results and the to-do list
	are made persistent
      */
    private static final String DB_NAME = "Diss";

    /** tells tne name under which the crawler manager registers as
	a root object with the GemStone/J database.
      */
    private static final String GS_ROOT_NAME = "CrawlerManager2";

    /** name to use for exporting one instance of this class to the RMI
	registry
      */
    public static final String CRAWLER_MANAGER_NAME = "WebMetricsCrawlerManager";

    /** the port on which the RMI registry is searched / started */
    public static final int RMI_REGISTRY_PORT = 1100;
}
