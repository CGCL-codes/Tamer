package org.apache.myfaces.trinidad.event;

import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import org.apache.myfaces.trinidad.context.RequestContext;

/**
 * Event delivered to indicate that a dialog is about to be
 * launched.  A {@link LaunchListener} can serve to add parameters that
 * should be passed to the dialog by using the {@link #getDialogParameters}
 * map, or add UI hints for any dialog that is being raised by
 * using the {@link #getWindowProperties} map.
 * <p>
 * @version $Name:  $ ($Revision: 244 $) $Date: 2008-11-25 18:45:49 -0500 (Tue, 25 Nov 2008) $
 */
public class LaunchEvent extends FacesEvent {

    /**
   * Create a LaunchEvent.
   * @param source the component responsible for launching the dialog
   * @param viewRoot the UIViewRoot to be displayed at the start of
   *   the dialog
   */
    public LaunchEvent(UIComponent source, UIViewRoot viewRoot) {
        super(source);
        _viewRoot = viewRoot;
        _dialogParameters = new HashMap<String, Object>();
        _windowProperties = new HashMap<String, Object>();
        setPhaseId(PhaseId.ANY_PHASE);
    }

    /**
   *
   */
    public void launchDialog(boolean useWindow) {
        RequestContext afContext = RequestContext.getCurrentInstance();
        afContext.launchDialog(getViewRoot(), getDialogParameters(), getComponent(), useWindow, getWindowProperties());
    }

    /**
   * The UIViewRoot that will be displayed at the start of the dialog.
   */
    public UIViewRoot getViewRoot() {
        return _viewRoot;
    }

    /**
   * A map of parameters to pass to the dialog.  All entries
   * in the map will be added to the pageFlowScope.
   */
    public Map<String, Object> getDialogParameters() {
        return _dialogParameters;
    }

    /**
   * A map of user interface hints used to configure a dialog.
   * The set of property keys will depend on the current RenderKit.
   * However, "width" and "height" are common examples.
   * The map will be ignored if a dialog is not used.
   * =-=AEW getWindowProperties or getDialogProperties or ??
   */
    public Map<String, Object> getWindowProperties() {
        return _windowProperties;
    }

    @Override
    public void processListener(FacesListener listener) {
        ((LaunchListener) listener).processLaunch(this);
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return (listener instanceof LaunchListener);
    }

    @Override
    public int hashCode() {
        int result = (getComponent() == null) ? 0 : getComponent().hashCode();
        result = 37 * result + ((_viewRoot == null) ? 0 : _viewRoot.hashCode());
        result = 37 * result + _windowProperties.hashCode();
        result = 37 * result + _dialogParameters.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof LaunchEvent) {
            LaunchEvent that = (LaunchEvent) o;
            if (!this.getComponent().equals(that.getComponent())) return false;
            if (_viewRoot == null) {
                if (that._viewRoot != null) return false;
            } else {
                if (!_viewRoot.equals(that._viewRoot)) return false;
            }
            if (!_dialogParameters.equals(that._dialogParameters)) return false;
            if (!_windowProperties.equals(that._windowProperties)) return false;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName());
        sb.append("[component=");
        sb.append(getComponent());
        sb.append(", viewRoot=");
        sb.append(getViewRoot());
        sb.append(", dialogParameters=");
        sb.append(getDialogParameters());
        sb.append(", windowProperties=");
        sb.append(getWindowProperties());
        sb.append(']');
        return sb.toString();
    }

    private transient UIViewRoot _viewRoot;

    private Map<String, Object> _dialogParameters;

    private Map<String, Object> _windowProperties;

    private static final long serialVersionUID = 1L;
}
