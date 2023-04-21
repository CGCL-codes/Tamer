package com.ail.openquote.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import com.ail.core.Identified;
import com.ail.core.Type;

/**
 * Base class for all UI elements. Base properties common to all elements are implemented here along
 * with implementations of lifecycle methods.
 */
public abstract class PageElement extends Type implements Identified, Comparable<PageElement> {

    private static final long serialVersionUID = 1L;

    private static Pattern outlineFormat = Pattern.compile("^[0-9.]*$");

    private transient PropertyChangeSupport changes = new PropertyChangeSupport(this);

    /** Id which is unique within the element's container */
    protected String id;

    /** 
     * Optional order indicator in outline style (e.g. "1" "1.2" "1.2.1"). Container elements use this
     * to order their elements during rendering 
     */
    protected String order;

    /**
     * JXPath binding related to the context of this element. The expression always relates to some part 
     * of the {@link com.ail.openquote.Quotation Quotation} model. Most containers pass their element's
     * some sub part of the Quotation to work with. For example, the RowScroller element passes each of
     * it's elements a context pointing to the thing they are 'Scrolling' over.
     */
    protected String binding;

    /**
     * List of action associated with this element.
     */
    private ArrayList<Action> action;

    /**
     * Default constructor
     */
    public PageElement() {
        super();
        id = "#" + Integer.toHexString((int) (Math.random() * 100000));
        action = new ArrayList<Action>();
    }

    /**
     * The property change listener is primarily provided for listening
     * to changes to the {@link #getOrder() order} property. The {@link com.ail.openquote.ui.util.OrderedLinkedList}
     * uses this listener to detect changes to element's order properties.
     * @param l Listener
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    /**
     * Get the model binding if any. The model binding is an xpath expression
     * which binds this page element to some part of the model. In the case of
     * elements held within a {@link Repeater} (e.g. a {@link RowScroller}) the
     * binding is partial. The Repeater is bound to a collection in the 
     * model, and the PageElements within the Repeater are bound relative to the
     * Repeater.
     * @return binding XPath expression
	 */
    public String getBinding() {
        return binding;
    }

    /**
     * Set the model binding for this page element.
     * @see #getBinding()
     * @param binding Page element's model binding
     */
    public void setBinding(String binding) {
        this.binding = binding;
    }

    /**
     * Elements must have an ID associated with them, by default the system will generate an ID
     * for each element as it is created, but these should be considered as unreliable as they
     * change each time the system is restarted.
     * The IDs are important for
     * @return Element's ID
     */
    public String getId() {
        return id;
    }

    /**
     * @see #getId()
     * @param id Page element's ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * A list of the actions associated with this element. An action is some processing which
     * takes place in response to a life-cycle even, or a page event (e.g. pressing a button).
     * @return List of Actions for this element.
     */
    public ArrayList<Action> getAction() {
        return action;
    }

    /**
     * @see #getAction()
     * @param action Actions to be associated with this element.
     */
    public void setAction(ArrayList<Action> action) {
        this.action = action;
    }

    /**
     * Life-cycle method invoked at the beginning of the response handling process. UI components
     * are expected to use this event to update their model state with respect to the values
     * passed back from the page in the <code>request</code> parameter.
     * @param request
     * @param response
     * @param model
     */
    public void applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
    }

    /**
     * Life-cycle method invoked following {@link #applyRequestValues(ActionRequest, ActionResponse, Type)}. 
     * Components use this as an opportunity to validate their model state.
     * Note: This step is skipped if the request is marked as {@link QuotationPortlet#immediate(ActionRequest) immediate}
     * @param request
     * @param response
     * @param model
     * @return true if any validation errors are found, false otherwise.
     */
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        return false;
    }

    /**
     * Life-cycle method invoked following {@link #applyRequestValues(ActionRequest, ActionResponse, Type)}. This 
     * event is only invoked if the request is marked as {@link QuotationPortlet#immediate(ActionRequest) immediate}, or
     * {@link #processValidations(ActionRequest, ActionResponse, Type)} returned false - indicating that there are no
     * page errors.
     * @param request
     * @param response
     * @param model
     */
    public void processActions(ActionRequest request, ActionResponse response, Type model) {
        for (Action a : action) {
            a.processActions(request, response, model);
        }
    }

    /**
     * This event is fired just prior to {@link #renderResponse(RenderRequest, RenderResponse, Type)} to give components
     * the chance to generate page level output before the portlet page's main form opens. For example, components might
     * add javascript code to the start of the portlet using this event. Note: 
     * As we're in a portlet environment this method does not write to HTML &lt;HEAD&gt; itself, it simply outputs at the beginning
     * of the portlet.  
     * @param request
     * @param response
     * @param model
     * @throws IllegalStateException
     * @throws IOException
     */
    public void renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
    }

    /**
     * This event is fired just after {@link #renderResponse(RenderRequest, RenderResponse, Type)} to give components
     * the chance to generate page level output after the portlet page's main form closes. 
     * @param request
     * @param response
     * @param model
     * @throws IllegalStateException
     * @throws IOException
     */
    public void renderPageFooter(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
    }

    /**
     * This is the last event fired in the request/response process. Elements are expected to render themselves at this point. 
     * @param request
     * @param response
     * @param model
     * @throws IllegalStateException
     * @throws IOException
     */
    public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        for (Action a : action) {
            a.renderResponse(request, response, model);
        }
    }

    /**
     * Page elements are considered to be the same by id if their id's match and are not empty, or
     * if their values of {@link #order} match.
     */
    public boolean compareById(Object that) {
        if ((that instanceof PageElement)) {
            PageElement thatPageElement = (PageElement) that;
            return (this.id != null && this.id.equals(thatPageElement.id));
        } else {
            return false;
        }
    }

    /**
     * The comparison of PageElements is based on the value of their respective {@link #order} fields. 
     * <ol><li>If the values of <i>order</i> are identical, a zero is returned indicating that they should be considered 
     * to be same.</li>
     * <li>If both have outline style IDs (e.g. "1.1", "1.1.3" etc) - that is to say they match the
     * regular expression '^[0-9.]$' - then they are compared and ordered as they would be in the context
     * of a document (1.1 comes before 1.2; 10.5 comes after 10.).</li>
     * <li>As a fall back, a simple {@link String#compareTo(String)} is used.</li></ol>
     */
    public int compareTo(PageElement that) {
        if (that.order == null) {
            return 1;
        } else if (this.order.equals(that.order)) {
            return 0;
        } else if (outlineFormat.matcher(this.order).matches() && outlineFormat.matcher(that.order).matches()) {
            String[] saThis = this.order.split("\\.");
            String[] saThat = that.order.split("\\.");
            try {
                for (int i = 0; i < saThis.length; i++) {
                    if (!saThis[i].equals(saThat[i])) {
                        return Integer.parseInt(saThis[i]) - Integer.parseInt(saThat[i]);
                    }
                }
                return -1;
            } catch (NoSuchElementException e) {
                return 1;
            }
        } else {
            return this.order.compareTo(that.order);
        }
    }

    public boolean equals(Object that) {
        if (that instanceof PageElement) {
            return this.id.equals(((PageElement) that).id);
        } else {
            return false;
        }
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        String oldOrder = this.order;
        this.order = order;
        changes.firePropertyChange("order", oldOrder, order);
    }
}
