package ontool.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Category;
import ontool.model.*;

/**
 * Page instance.
 *
 * <p>To each page instance it's assigned a unique OID, which is maintained
 * in this implementation by a scope object. This object controls which
 * topological elements belong to this page instance.
 *
 * <p> Scopes are arranged following an instantiation tree, as described in my
 * thesis.
 *
 * @author AntonioSRGomes
 * @version $Id: Page.java,v 1.1 2003/10/22 03:06:40 asrgomes Exp $
 */
public class Page {

    private Category cat = Category.getInstance(Page.class);

    private Page parent = null;

    private List childPages = new Vector();

    private SuperplaceModel splaceModel = null;

    /**
	 * @supplierRole places
	 * @link aggregation 
	 */
    private Place[] places;

    /**
	 * @supplierRole interfaces 
	 */
    private Interface[] ifaces;

    private PageModel model;

    private PageSupport support;

    /**
	 * Creates a new root scope.
	 * @param support page support   
	 */
    public Page(PageSupport pageSupport, PageModel model) {
        this.support = pageSupport;
        this.model = model;
    }

    /**
	 * Creates a new child scope.
	 * 
	 * @param support page support
	 * @param parent parent scope
	 * @param splaceModel reference superplace
	 */
    public Page(PageSupport pageSupport, Page parent, SuperplaceModel splaceModel) {
        this.support = pageSupport;
        this.parent = parent;
        this.splaceModel = splaceModel;
        model = splaceModel.getPageType();
        parent.childPages.add(this);
    }

    public List getChildPages() {
        return childPages;
    }

    /**
	 * Gets the reference superplace.
	 * @return superplace
	 */
    public SuperplaceModel getSuperplaceModel() {
        return splaceModel;
    }

    /**
	 * Gets the parent scope.
	 * @return scope
	 */
    public Page getParent() {
        return parent;
    }

    /**
	 * Gets the name of this scope. 
	 *
	 * <p>This follows a hierarquical notation, separated by slashes.
	 * @return name
	 */
    public String getName() {
        if (parent == null || splaceModel == null) return "/";
        String prefix = parent.getName();
        if (prefix.equals("/")) return "/" + splaceModel.getName(); else return prefix + "/" + splaceModel.getName();
    }

    public String getLocalName() {
        if (splaceModel == null) return ""; else return splaceModel.getName();
    }

    public PageModel getModel() {
        return model;
    }

    /**
	 * Gets the places defined whithin this scope.
	 * @return place array
	 */
    public Place[] getPlaces() {
        return places;
    }

    /**
	 * Gets the interfaces defined whithin this scope.
	 * @return link array
	 */
    public Interface[] getInterfaces() {
        return ifaces;
    }

    /**
	 * Finds a generic place given its original model.
	 * 
	 * @param pholder model
	 * @return generic place or <code>null</code> if no generic place could
	 *         be found.
	 */
    public GenericPlace findGenericPlace(PortHolderModel pholder) {
        for (int i = 0; i < places.length; i++) if (places[i].getModel() == pholder) return places[i];
        for (int i = 0; i < ifaces.length; i++) if (ifaces[i].getModel() == pholder) return ifaces[i];
        return null;
    }

    /**
	 * Finds a page instance that instantiates a given super-place model.
	 *  
	 * @param splace
	 * @return Page
	 */
    public Page findPage(SuperplaceModel splace) {
        for (Iterator i = childPages.iterator(); i.hasNext(); ) {
            Page s = (Page) i.next();
            if (s.getSuperplaceModel() == splace) return s;
        }
        return null;
    }

    /**
	 * Compiles all topological elements present in this page instance.
	 */
    public void compile() {
        for (int i = 0; i < places.length; i++) places[i].compile();
        for (int i = 0; i < ifaces.length; i++) ifaces[i].compile();
        performFusion();
        for (Iterator i = childPages.iterator(); i.hasNext(); ) ((Page) i.next()).compile();
    }

    /**
	 * Performs any required initialization in all places and interfaces
	 * defined in this scope. 
	 * 
	 * <p>Actually, all <code>PageSupport</code> objects will be initialized.
	 * 
	 * @throws UserException 
	 */
    public void init() throws UserException {
        cat.info("initing \"" + getName() + "\"");
        try {
            support.init();
        } catch (Exception e) {
            throw new UserException(this, e);
        }
        for (Iterator i = childPages.iterator(); i.hasNext(); ) ((Page) i.next()).init();
    }

    /**
	 * Performs any required clean-up.
	 * 
	 * @throws UserException
	 */
    public void clean() throws UserException {
        cat.info("cleaning \"" + getName() + "\"");
        try {
            support.clean();
        } catch (Exception e) {
            throw new UserException(this, e);
        }
        for (Iterator i = childPages.iterator(); i.hasNext(); ) ((Page) i.next()).init();
    }

    /**
	 * Finds an element by its binding path, starting recursively from this
	 * page.
	 * 
	 * @param path relative path
	 * @return GenericPlace
	 */
    public Place findPlace(String path) {
        if (path.startsWith("/")) throw new IllegalArgumentException("Path must be relative");
        StringTokenizer st = new StringTokenizer(path.trim(), "/");
        String elementName = null;
        List pageList = new ArrayList();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (elementName != null) pageList.add(elementName);
            elementName = token;
        }
        Page currentPage = this;
        for (Iterator iter = pageList.iterator(); iter.hasNext(); ) {
            String pageName = (String) iter.next();
            currentPage = currentPage.findChildPage(pageName);
        }
        Place[] p = currentPage.places;
        for (int i = 0; i < p.length; i++) {
            if (p[i].getName().equals(elementName)) return p[i];
        }
        return null;
    }

    public Page findChildPage(String name) {
        for (Iterator iterator = childPages.iterator(); iterator.hasNext(); ) {
            Page page = (Page) iterator.next();
            if (page.getLocalName().equals(name)) return page;
        }
        return null;
    }

    /**
	 * Sets the places associated with this scope
	 * @param c place collection
	 * @see Place
	 */
    protected void setPlaces(Collection c) {
        places = (Place[]) c.toArray(new Place[0]);
    }

    /**
	 * Sets the interfaces associated with this scope
	 * @param c interface collection
	 * @see Interface
	 */
    protected void setInterfaces(Collection c) {
        ifaces = (Interface[]) c.toArray(new Interface[0]);
    }

    /**
	 * Analyses the interfaces defined whithin this scope and performs all
	 * required fusions with elements in the enclosing page.
	 *
	 * <p>This method must be called after the methods setPlaces, setLinks,
	 * setInterfaces.
	 */
    protected void performFusion() {
        cat.info("performing fusion in page" + getName());
        for (int i = 0; i < ifaces.length; i++) {
            PortModel pagePort = (PortModel) splaceModel.getChild(new PortSelector(ifaces[i]));
            PortHolderModel portHolder;
            if (pagePort instanceof InPortModel) portHolder = (PortHolderModel) pagePort.getLink().getSource().getParent(); else portHolder = (PortHolderModel) pagePort.getLink().getDest().getParent();
            GenericPlace genericPlace = parent.findGenericPlace(portHolder);
            if (genericPlace == null) throw new EngineException("generic place model " + portHolder + " not instantiated");
            if (ifaces[i].getFusion() != null) throw new EngineException("interface " + ifaces[i] + " was already fused");
            ifaces[i].setFusion(genericPlace);
        }
    }

    /**
	 * This class selects the port model that references an interface model.
	 */
    private static class PortSelector implements ModelSelector {

        private Interface iface;

        /**
		 * Constructs a new selector.
		 * @param iface interface to which the desired port is attached
		 */
        public PortSelector(Interface iface) {
            this.iface = iface;
        }

        public boolean select(Model m) {
            if (m instanceof PrivatePort) {
                PrivatePort pp = (PrivatePort) m;
                return pp.getPortRef() == iface.getModel();
            } else return false;
        }
    }
}
