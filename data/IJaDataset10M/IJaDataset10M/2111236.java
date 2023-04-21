package com.dfruits.ui.jphandlers;

import org.aspectj.lang.JoinPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import com.dfruits.access.AccessPlugin;
import com.dfruits.access.ComponentType;
import com.dfruits.access.handlers.JPAccessParams;
import com.dfruits.access.handlers.JoinPointAccessHandler;
import com.dfruits.queries.BindingContext;
import com.dfruits.ui.views.XUIView;

public class ViewCreationHandler implements JoinPointAccessHandler {

    public Runnable getAccessDeniedOperation(final JoinPoint jp) {
        return new Runnable() {

            public void run() {
                JPAccessParams ap = getAccessParams(jp);
                String msg = "View not accessible (id=" + ap.id + ")";
                XUIView view = (XUIView) jp.getThis();
                Composite parent = view.getParent();
                Composite container = new Composite(parent, SWT.None);
                container.setLayout(new GridLayout(1, true));
                CLabel label = new CLabel(container, SWT.None);
                label.setText(msg);
                label.setToolTipText(msg);
                label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
            }
        };
    }

    public JPAccessParams getAccessParams(JoinPoint jp) {
        IViewPart part = (IViewPart) jp.getThis();
        JPAccessParams ret = new JPAccessParams();
        ret.id = part.getSite().getId();
        ret.type = ComponentType.VIEW;
        ret.accesDeniedOperation = getAccessDeniedOperation(jp);
        ret.accesGrantedOperation = getAccessGrantedOperation(jp);
        XUIView view = (XUIView) jp.getThis();
        BindingContext ctx = view.getBindingContext();
        if (ctx != null) {
            ret.componentAccessChecker = ctx.getComponentAccessChecker();
            ret.userID = ctx.getConnectionUser();
            ret.userRole = ctx.getUserRole();
            ret.connectionID = ctx.getConnectionID();
        }
        return ret;
    }

    public Runnable getAccessGrantedOperation(JoinPoint jp) {
        return null;
    }

    public JPAccessParams getParentParams(JoinPoint jp) {
        XUIView view = (XUIView) jp.getThis();
        String parentID = view.getXuiDef().perspectiveID;
        if (parentID == null) {
            return null;
        }
        JPAccessParams ap = new JPAccessParams();
        ap.id = parentID;
        ap.type = ComponentType.PERSPECTIVE;
        return ap;
    }
}
