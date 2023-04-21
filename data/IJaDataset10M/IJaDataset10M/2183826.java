package net.sf.rcpforms.widgetwrapper.wrapper.event;

import java.lang.reflect.Method;
import net.sf.rcpforms.widgets2.IRCPWidget;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPWidget;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.events.TypedEvent;

/** @deprecated use {@link text } */
@Deprecated
public class ReflectiveTreeListener<WIDGET extends RCPWidget> extends ReflectiveBaseListener<WIDGET, TreeListener, TreeEvent> implements TreeListener {

    private static Method treeCollapsed = null;

    private static Method treeExpanded = null;

    public ReflectiveTreeListener(final WIDGET widget) {
        super(widget, TreeListener.class, TreeEvent.class);
        if (treeCollapsed == null) {
            treeCollapsed = findListenerMethod("treeCollapsed");
        }
        if (treeExpanded == null) {
            treeExpanded = findListenerMethod("treeExpanded");
        }
    }

    /**
     * Attaches the <code>TreeListener.treeCollapsed</code> and
     * <code>TreeListener.treeExpanded</code> method to the <code>model</code>'s
     * <code>treeMethodName</code> method. If <code>treeMethodName</code> has parameters, note that
     * these types are supported:
     * <ul>
     * <li>Boolean : gives <code>true</code> if the tree event is an <i>expanding</i> event,
     * <code>false</code> if it is <i>collapsing</i></li>
     * <li>TreeEvent : is filled with the event from the tree event.</li>
     * <li>RCPWidget or RCPTree : is filled with tree widget</li>
     * </ul>
     * Note too: multiple arguments of the same type are useless and are being filled with the same
     * parameters!
     */
    public void addTreeListener(final Object model, final String treeMethodName) {
        addListener0(model, treeMethodName);
    }

    /**
     * @see org.eclipse.swt.events.TreeListener#treeCollapsed(org.eclipse.swt.events.TreeEvent)
     */
    public void treeCollapsed(final TreeEvent e) {
        delegateEventFiring(e, treeCollapsed);
    }

    /**
     * @see org.eclipse.swt.events.TreeListener#treeExpanded(org.eclipse.swt.events.TreeEvent)
     */
    public void treeExpanded(final TreeEvent e) {
        delegateEventFiring(e, treeExpanded);
    }

    /**
     * @see net.sf.rcpforms.widgetwrapper.wrapper.event.ReflectiveBaseListener#chooseSpecialParameterChooserFor(java.lang.Class)
     */
    @Override
    protected IParameterChooser chooseSpecialParameterChooserFor(final Class<?> parameterType) {
        if (parameterType == Boolean.class || parameterType == boolean.class || parameterType == Boolean.TYPE) {
            return s_ExpandedFlagChooser;
        }
        return super.chooseSpecialParameterChooserFor(parameterType);
    }

    private static ExpandedFlagChooser s_ExpandedFlagChooser = new ExpandedFlagChooser();

    private static class ExpandedFlagChooser implements IParameterChooser {

        public Object chooseParameter(final IRCPWidget widget, final TypedEvent event, final Method method) {
            if (method == treeExpanded) {
                return true;
            }
            return false;
        }
    }
}
