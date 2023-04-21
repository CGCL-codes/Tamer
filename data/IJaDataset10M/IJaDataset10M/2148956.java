package cx.ath.contribs.klex.forester.views;

import cx.ath.contribs.klex.forester.ForesterPlugin;

public class ElementNavigator extends Navigator {

    public static final String ID = "ElementNavigator.view";

    public ElementNavigator() {
        super("%Logic");
    }

    public int getComponent() {
        return Navigator.APPLICATION;
    }

    public Navigator getInstance() {
        return this;
    }

    public String getDefaultNamespaces() {
        return ForesterPlugin.getInstance().getConfigProperty("DefaultNamespacesApp");
    }
}
