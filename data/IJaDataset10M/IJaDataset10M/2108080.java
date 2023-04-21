package er.directtoweb;

import com.webobjects.appserver.*;
import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;
import com.webobjects.directtoweb.*;
import er.extensions.*;

public class D2WPick extends D2WEmbeddedComponent {

    public D2WPick(WOContext context) {
        super(context);
    }

    static class _D2WPickActionDelegate implements NextPageDelegate {

        public static NextPageDelegate instance = new _D2WPickActionDelegate();

        public WOComponent nextPage(WOComponent sender) {
            WOComponent target = (WOComponent) D2WEmbeddedComponent.findTarget(sender);
            WOComponent nextPage = null;
            if (target.hasBinding("branchDelegate")) {
                ERDBranchDelegate delegate = (ERDBranchDelegate) target.valueForBinding("branchDelegate");
                if (delegate == null) {
                    throw new RuntimeException("Null branch delegate. Sender: " + sender + " Target: " + target);
                } else {
                    nextPage = delegate.nextPage(sender);
                }
            } else {
                if (target.hasBinding("selectedObjects") && target.canSetValueForBinding("selectedObjects")) {
                    target.setValueForBinding(sender.valueForKey("selectedObjects"), "selectedObjects");
                }
                nextPage = (WOComponent) target.valueForBinding("action");
            }
            sender.takeValueForKey(new NSMutableArray(), "selectedObjects");
            return nextPage;
        }

        public EODataSource dataSource() {
            return dataSource();
        }
    }

    static {
        try {
            D2WSwitchComponent.addToPossibleBindings("selectedObjects");
        } catch (ExceptionInInitializerError e) {
            Throwable e2 = e.getException();
            e2.printStackTrace();
        }
    }

    public NextPageDelegate actionPageDelegate() {
        return _D2WPickActionDelegate.instance;
    }

    public NextPageDelegate newPageDelegate() {
        return _D2WPickActionDelegate.instance;
    }

    public EODataSource internalDataSource() {
        EODataSource ds = dataSource();
        ds = (ds == null) ? er.extensions.ERXExtensions.dataSourceForArray(list()) : ds;
        return ds;
    }

    public void setInternalDataSource(Object foo) {
    }

    public NSArray list() {
        return (hasBinding("list") ? (NSArray) valueForBinding("list") : null);
    }

    public EODataSource dataSource() {
        return hasBinding("dataSource") ? (EODataSource) valueForBinding("dataSource") : null;
    }
}
