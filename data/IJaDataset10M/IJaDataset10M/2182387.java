package netgest.bo.xwc.xeo.workplaces.admin.viewersbeans;

import javax.faces.event.PhaseEvent;
import netgest.bo.utils.rebuilder.OperationStatus;
import netgest.bo.xwc.components.classic.Window;
import netgest.bo.xwc.components.classic.scripts.XVWScripts;
import netgest.bo.xwc.framework.XUIRequestContext;
import netgest.bo.xwc.framework.XUIScriptContext;
import netgest.bo.xwc.framework.components.XUIComponentBase;
import netgest.bo.xwc.framework.components.XUIForm;
import netgest.bo.xwc.framework.components.XUIViewRoot;
import netgest.bo.xwc.xeo.beans.XEOBaseBean;

public class MaintenanceLogBean extends XEOBaseBean {

    private OperationStatus operation;

    public void setOperation(OperationStatus operation) {
        this.operation = operation;
    }

    public String getLog() {
        XUIComponentBase f = getViewRoot().findComponent(XUIForm.class);
        XUIRequestContext.getCurrentContext().getScriptContext().add(XUIScriptContext.POSITION_FOOTER, f.getClientId() + "_scrollDown", "window.setTimeout(function(){try{ document.getElementById('" + f.getClientId() + ":logTab').scrollTop" + "=document.getElementById('" + f.getClientId() + ":logTab').scrollHeight;}catch(e){}},100);");
        return "<pre>" + this.operation.getLog() + "</pre>";
    }

    public void canCloseTab() {
        XUIRequestContext oRequestContext = XUIRequestContext.getCurrentContext();
        XUIViewRoot viewRoot = oRequestContext.getViewRoot();
        Window xWnd = (Window) viewRoot.findComponent(Window.class);
        if (xWnd != null) {
            if (xWnd.getOnClose() != null) {
                xWnd.getOnClose().invoke(oRequestContext.getELContext(), null);
            }
        }
        XVWScripts.closeView(viewRoot);
        oRequestContext.getViewRoot().setRendered(false);
        oRequestContext.getViewRoot().setTransient(true);
        oRequestContext.renderResponse();
    }

    public void triggerRefreshViewer(PhaseEvent p) {
        XUIComponentBase f = getViewRoot().findComponent(XUIForm.class);
        if (this.operation.isAlive()) {
            XUIRequestContext.getCurrentContext().getScriptContext().add(XUIScriptContext.POSITION_HEADER, f.getClientId() + "_syncView", "window.setTimeout(function() { try{ XVW.syncView('" + f.getClientId() + "');}catch(e){} },1000)");
        }
    }
}
