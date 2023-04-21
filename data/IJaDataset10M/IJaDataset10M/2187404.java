package net.sf.tacos.components.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import net.sf.tacos.model.IKeyProvider;
import net.sf.tacos.model.ITreeContentProvider;
import net.sf.tacos.model.IdentityKeyProvider;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.bean.EvenOdd;
import org.apache.tapestry.components.Any;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.listener.ListenerInvoker;

/**
 * Base component for providing Tree-like semantics for displaying data.
 * 
 * @author phraktle
 */
public abstract class Tree extends BaseComponent implements IDirect {

    /** Logger */
    private static final Log log = LogFactory.getLog(Tree.class);

    /** Default key provider */
    private static final IKeyProvider identityKeyProvider = new IdentityKeyProvider();

    /** Default iterator */
    private TreeIterator treeIterator;

    /** Parent nodes */
    private Stack parts;

    /**
     * The default {@link IdentityKeyProvider}.
     * 
     * @return The default key provider.
     */
    public IKeyProvider getIdentityKeyProvider() {
        return identityKeyProvider;
    }

    /**
     * The default {@link TreeIterator} provided if no other is specified.
     * 
     * @return Instance of {@link TreeIterator}
     */
    public TreeIterator getTreeIterator() {
        return treeIterator;
    }

    /**
     * {@inheritDoc}
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        boolean dynamic = cycle.getResponseBuilder().isDynamic();
        if (!cycle.isRewinding() && isDelayedLoad() && !dynamic) {
            renderDynamicLoad(writer, cycle);
            return;
        }
        treeIterator = new TreeIter();
        parts = new Stack();
        try {
            super.renderComponent(writer, cycle);
        } finally {
            try {
                while (!parts.isEmpty()) {
                    String partId = parts.pop().toString();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            treeIterator = null;
            parts = null;
        }
    }

    private void renderDynamicLoad(IMarkupWriter writer, IRequestCycle cycle) {
        Any anyContainer = (Any) getComponent("treeDiv");
        writer.begin(anyContainer.getElement());
        super.renderInformalParameters(writer, cycle);
        writer.end();
        String treeId = getClientId();
        DirectServiceParameter dsp = new DirectServiceParameter(this);
        Map parms = new HashMap();
        parms.put("treeId", treeId);
        parms.put("url", StringEscapeUtils.escapeJavaScript(getDirectService().getLink(true, dsp).getAbsoluteURL()));
        if (getLoadElement() != null) parms.put("loadElement", getLoadElement());
        PageRenderSupport pageRenderSupport = TapestryUtils.getPageRenderSupport(cycle, this);
        getScript().execute(this, cycle, pageRenderSupport, parms);
    }

    /**
     * Called to request the expansion of a tree node.
     * 
     * @param cycle
     */
    public void expansion(IRequestCycle cycle) {
        Object[] params = cycle.getListenerParameters();
        Serializable key = (Serializable) params[0];
        boolean expanded = ((Boolean) params[1]).booleanValue();
        getManager().setExpandedKey(key, expanded);
        setState(getState());
    }

    /**
     * Invoked by contentLinkToggle component, will invoke
     * {@link #expansion(IRequestCycle)} first before checking if the invoking
     * component specified an {@link IActionListener} and any optional
     * parameters it may need.
     * 
     * @param cycle
     */
    public void contentExpansion(IRequestCycle cycle) {
        expansion(cycle);
        if (getLinkListener() == null) {
            log.warn("contentExpansion() called but no linkListener was specified on the tree. " + "Use the linkListener parameter if you want your page/component to be invoked " + "as well.");
            return;
        }
        getListenerInvoker().invokeListener(getLinkListener(), this, cycle);
    }

    /**
     * @param element
     *            to test expanded of
     * @return True if element should be displayed/expanded.
     */
    public boolean isExpanded(Object element) {
        return getManager().isExpanded(element);
    }

    private final class TreeIter extends TreeIterator {

        /**
         * Creates new iterator
         *
         */
        private TreeIter() {
            super(sorted(getContentProvider().getElements()));
        }

        /**
         * 
         * {@inheritDoc}
         */
        protected Collection getChildren(Object node) {
            if (!isExpanded(node)) {
                return Collections.EMPTY_LIST;
            }
            Collection coll = getContentProvider().getChildren(node);
            return sorted(coll);
        }
    }

    /**
     * Default comparator/sorter for tree elements.
     * 
     * @param elements
     * @return
     */
    private Collection sorted(Collection elements) {
        Comparator comp = getSorter();
        if (comp == null) {
            return elements;
        }
        List l = new ArrayList(elements);
        Collections.sort(l, comp);
        return l;
    }

    /**
     * @return The {@link ITreeManager} for this tree.
     */
    public ITreeManager getManager() {
        return new TreeManager(getState(), getContentProvider(), getKeyProvider());
    }

    /**
     * {@inheritDoc}
     */
    public void beforeRenderBody(IMarkupWriter writer, IRequestCycle cycle, Serializable partId, boolean isRendering) {
        if (treeIterator.getDepth() <= treeIterator.getPreviousDepth() && !parts.isEmpty()) {
            int pops = 1;
            if (treeIterator.getDepth() < treeIterator.getPreviousDepth()) {
                pops = treeIterator.getPreviousDepth() - treeIterator.getDepth();
                pops++;
            }
            while (pops > 0 && !parts.isEmpty()) {
                Serializable id = (Serializable) parts.pop();
                if (isRendering) {
                    {
                        IMarkupWriter pwriter = findParentPart();
                        if (pwriter != null) pwriter.end(); else writer.end();
                    }
                }
                pops--;
            }
        }
        if (isRendering) {
            writer.begin("div");
            writer.attribute("id", partId.toString());
            if (isRowStyleInOuterDiv() && getRowStyle() != null) {
                writer.attribute("class", getRowStyle().getNext());
            } else if (getPartialBlockClass() != null) {
                writer.attribute("class", getPartialBlockClass());
            }
            parts.push(partId.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void incrementNonRenderBlock() {
        if (getRowStyle() != null) getRowStyle().getNext();
        beforeRenderBody(null, null, getKeyProvider().getKey(getValue()), false);
    }

    /**
     * {@inheritDoc}
     */
    public IMarkupWriter getPartWriter(Serializable partId) {
        String partStr = partId.toString();
        IMarkupWriter ret = null;
        if (treeIterator.getDepth() < treeIterator.getPreviousDepth() && !parts.isEmpty() && ((parts.size() - 1) > (treeIterator.getPreviousDepth() - treeIterator.getDepth()))) {
            ret = findParentPart();
        } else if (treeIterator.getDepth() > treeIterator.getPreviousDepth() && !parts.isEmpty()) {
            ret = findParentPart();
        } else if (treeIterator.getDepth() == treeIterator.getPreviousDepth() && parts.size() > 1) {
            ret = findParentPart();
        }
        return ret;
    }

    /**
     * Loops through the current part stack looking for a part that returns a
     * valid {@link IMarkupWriter}.
     * 
     * @return Valid writer, or null.
     */
    private IMarkupWriter findParentPart() {
        IMarkupWriter ret = null;
        Serializable[] partIds = (Serializable[]) parts.toArray(new Serializable[parts.size()]);
        return ret;
    }

    public boolean getCurrentHasChildren() {
        return getContentProvider().hasChildren(getValue());
    }

    public Serializable getCurrentKey() {
        return getKeyProvider().getKey(getValue());
    }

    public boolean getCurrentNotExpanded() {
        return !isExpanded(getValue());
    }

    public String getCurrentOffset() {
        int off = (getContentProvider().hasChildren(getValue())) ? 0 : 1;
        return "" + (off + getTreeIterator().getDepth()) * getOffset();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStateful() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void trigger(IRequestCycle cycle) {
    }

    /** Injected script */
    public abstract IScript getScript();

    /** Injected ajax engine */
    public abstract IEngineService getDirectService();

    /** Current value being rendered */
    public abstract Object getValue();

    /** Gets default/specified state */
    public abstract Set getState();

    /** Saves tree state */
    public abstract void setState(Set state);

    /** Injected content provider */
    public abstract ITreeContentProvider getContentProvider();

    /** Injected key provider */
    public abstract IKeyProvider getKeyProvider();

    /** Injected sort {@link Comparator} */
    public abstract Comparator getSorter();

    /** Optional EvenOdd component */
    public abstract EvenOdd getRowStyle();

    /** The offset in pixels for depth-indentation */
    public abstract int getOffset();

    /** Where to apply the row style */
    public abstract boolean isRowStyleInOuterDiv();

    /** Partial block class */
    public abstract String getPartialBlockClass();

    /** For invoking linkToggleListener, if it exists */
    public abstract ListenerInvoker getListenerInvoker();

    /** Listener to invoke */
    public abstract IActionListener getLinkListener();

    /** Delayed loading parameter */
    public abstract boolean isDelayedLoad();

    /** Delayed loading display element id */
    public abstract String getLoadElement();
}
