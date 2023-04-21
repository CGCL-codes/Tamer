package netgest.bo.xwc.framework.components;

import java.util.Map;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.ActionSource2;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.Renderer;
import netgest.bo.xwc.components.classic.scripts.XVWScripts;
import netgest.bo.xwc.framework.XUIActionEvent;
import netgest.bo.xwc.framework.XUIRequestContext;
import com.sun.faces.application.MethodBindingMethodExpressionAdapter;
import com.sun.faces.application.MethodExpressionMethodBindingAdapter;

/**
 * <p><strong>XUICommand</strong> is a {@link UIComponent} that represents
 * a user interface component which, when activated by the user, triggers
 * an application specific "command" or "action".  Such a component is
 * typically rendered as a push button, a menu item, or a hyperlink.</p>
 *
 * <p>When the <code>decode()</code> method of this {@link XUICommand}, or
 * its corresponding {@link Renderer}, detects that this control has been
 * activated, it will queue an {@link ActionEvent}.
 * Later on, the <code>broadcast()</code> method will ensure that this
 * event is broadcast to all interested listeners.</p>
 * 
 * <p>Listeners will be invoked in the following order:
 * <ol>
 *  <li>{@link ActionListener}s, in the order in which they were registered.
 *  <li>The "actionListener" {@link MethodExpression} (which will cover
 *  the "actionListener" that was set as a <code>MethodBinding</code>).
 *  <li>The default {@link ActionListener}, retrieved from the
 *      {@link Application} - and therefore, any attached "action"
 *      {@link MethodExpression}.
 * </ol>
 * </p>
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Button</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */
@SuppressWarnings("deprecation")
public class XUICommand extends XUIComponentBase implements ActionSource2 {

    /**
     * <p>Create a new {@link XUICommand} instance with default property
     * values.</p>
     */
    public XUICommand() {
        super();
        setRendererType("javax.faces.Button");
    }

    private Object value = null;

    private Object commandArgument = null;

    public void actionPerformed(ActionEvent event) {
    }

    /**
     * {@inheritDoc}
     * @deprecated This has been replaced by {@link #getActionExpression}.
     */
    @Deprecated
    public MethodBinding getAction() {
        MethodBinding result = null;
        MethodExpression me;
        if (null != (me = getActionExpression())) {
            if (me.getClass().equals(MethodExpressionMethodBindingAdapter.class)) {
                result = ((MethodExpressionMethodBindingAdapter) me).getWrapped();
            } else {
                result = new MethodBindingMethodExpressionAdapter(me);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * @deprecated This has been replaced by {@link #setActionExpression(javax.el.MethodExpression)}.
     */
    @Deprecated
    public void setAction(MethodBinding action) {
        MethodExpressionMethodBindingAdapter adapter;
        if (null != action) {
            adapter = new MethodExpressionMethodBindingAdapter(action);
            setActionExpression(adapter);
        } else {
            setActionExpression(null);
        }
    }

    /**
     * {@inheritDoc}
     * @deprecated Use {@link #getActionListeners} instead.
     */
    @Deprecated
    public MethodBinding getActionListener() {
        return this.methodBindingActionListener;
    }

    /**
     * {@inheritDoc}
     * @deprecated This has been replaced by {@link #addActionListener(javax.faces.event.ActionListener)}.
     */
    @Deprecated
    public void setActionListener(MethodBinding actionListener) {
        this.methodBindingActionListener = actionListener;
    }

    /**
     * <p>The immediate flag.</p>
     */
    private Boolean immediate;

    public boolean isImmediate() {
        if (this.immediate != null) {
            return (this.immediate);
        }
        ValueExpression ve = getValueExpression("immediate");
        if (ve != null) {
            try {
                return (Boolean.TRUE.equals(ve.getValue(getFacesContext().getELContext())));
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return (false);
        }
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    /**
     * <p>Returns the <code>value</code> property of the
     * <code>XUICommand</code>. This is most often rendered as a label.</p>
     */
    public Object getValue() {
        if (this.value != null) {
            return (this.value);
        }
        ValueExpression ve = getValueExpression("value");
        if (ve != null) {
            try {
                return (ve.getValue(getFacesContext().getELContext()));
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return (null);
        }
    }

    /**
     * <p>Sets the <code>value</code> property of the <code>XUICommand</code>.
     * This is most often rendered as a label.</p>
     *
     * @param value the new value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    private MethodBinding methodBindingActionListener = null;

    /**
     * <p>The {@link MethodExpression} that, when invoked, yields the
     * literal outcome value.</p>
     */
    private MethodExpression actionExpression = null;

    public MethodExpression getActionExpression() {
        return actionExpression;
    }

    public void setActionExpression(MethodExpression actionExpression) {
        this.actionExpression = actionExpression;
    }

    /** 
     * @throws NullPointerException {@inheritDoc}
     */
    public void addActionListener(ActionListener listener) {
        addFacesListener(listener);
    }

    public ActionListener[] getActionListeners() {
        ActionListener al[] = (ActionListener[]) getFacesListeners(ActionListener.class);
        return (al);
    }

    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void removeActionListener(ActionListener listener) {
        removeFacesListener(listener);
    }

    private Object[] values;

    public Object saveState(FacesContext context) {
        if (values == null) {
            values = new Object[5];
        }
        values[0] = super.saveState(context);
        values[1] = saveAttachedState(context, methodBindingActionListener);
        values[2] = saveAttachedState(context, actionExpression);
        values[3] = immediate;
        values[4] = value;
        return (values);
    }

    public void restoreState(FacesContext context, Object state) {
        values = (Object[]) state;
        super.restoreState(context, values[0]);
        methodBindingActionListener = (MethodBinding) restoreAttachedState(context, values[1]);
        actionExpression = (MethodExpression) restoreAttachedState(context, values[2]);
        immediate = (Boolean) values[3];
        value = values[4];
    }

    /**
     * <p>In addition to to the default {@link UIComponent#broadcast}
     * processing, pass the {@link ActionEvent} being broadcast to the
     * method referenced by <code>actionListener</code> (if any),
     * and to the default {@link ActionListener} registered on the
     * {@link javax.faces.application.Application}.</p>
     *
     * @param event {@link FacesEvent} to be broadcast
     *
     * @throws AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     * @throws IllegalArgumentException if the implementation class
     *  of this {@link FacesEvent} is not supported by this component
     * @throws NullPointerException if <code>event</code> is
     * <code>null</code>
     */
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        try {
            if (event instanceof ActionEvent) {
                XUIRequestContext.getCurrentContext().setEvent(new XUIActionEvent((ActionEvent) event));
            }
            super.broadcast(event);
            if (event instanceof ActionEvent) {
                if (event instanceof ActionEvent) {
                    actionPerformed((ActionEvent) event);
                }
                FacesContext context = getFacesContext();
                MethodBinding mb = getActionListener();
                if (mb != null) {
                    mb.invoke(context, new Object[] { event });
                }
                ActionListener listener = context.getApplication().getActionListener();
                if (listener != null) {
                    listener.processAction((ActionEvent) event);
                }
            }
        } finally {
            if (XUIRequestContext.getCurrentContext() != null) {
                XUIRequestContext.getCurrentContext().setEvent(null);
            }
        }
    }

    /**
     * <p>Intercept <code>queueEvent</code> and, for {@link ActionEvent}s,
     * mark the phaseId for the event to be
     * <code>PhaseId.APPLY_REQUEST_VALUES</code> if the
     * <code>immediate</code> flag is true,
     * <code>PhaseId.INVOKE_APPLICATION</code> otherwise.</p>
     */
    public void queueEvent(FacesEvent e) {
        UIComponent c = e.getComponent();
        if (e instanceof ActionEvent && c instanceof ActionSource) {
            if (((ActionSource) c).isImmediate()) {
                e.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            } else {
                e.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }
        }
        super.queueEvent(e);
        XUIForm oForm = (XUIForm) findParentComponent(XUIForm.class);
        if (oForm != null && this != oForm) {
            oForm.setSubmittedAction(true);
        }
    }

    @Override
    public void decode() {
        super.decode();
        if (wasClicked(getRequestContext())) {
            ActionEvent e = new ActionEvent(this);
            queueEvent(e);
        }
    }

    public static class ActionPerformed implements ActionListener {

        public void processAction(ActionEvent event) {
            ((XUICommand) event.getComponent()).actionPerformed(event);
        }
    }

    private boolean wasClicked(XUIRequestContext context) {
        Map<String, String> requestParamMap = context.getRequestParameterMap();
        if (requestParamMap.containsKey(this.getClientId())) {
            setCommandArgument(requestParamMap.get(this.getClientId()));
            return true;
        }
        return false;
    }

    public void setCommandArgument(Object value) {
        this.commandArgument = value;
    }

    public Object getCommandArgument() {
        return this.commandArgument;
    }
}
