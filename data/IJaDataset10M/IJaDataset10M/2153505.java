package dr.app.beauti;

import jam.mac.MacHelpMenuFactory;
import jam.mac.MacWindowMenuFactory;
import jam.framework.DefaultMenuBarFactory;
import jam.framework.DefaultEditMenuFactory;
import jam.framework.DefaultHelpMenuFactory;
import dr.app.util.OSType;

public class BeautiMenuBarFactory extends DefaultMenuBarFactory {

    public BeautiMenuBarFactory() {
        if (OSType.isMac()) {
            registerMenuFactory(new BeautiMacFileMenuFactory());
            registerMenuFactory(new DefaultEditMenuFactory());
            registerMenuFactory(new MacWindowMenuFactory());
            registerMenuFactory(new MacHelpMenuFactory());
        } else {
            registerMenuFactory(new BeautiDefaultFileMenuFactory());
            registerMenuFactory(new DefaultEditMenuFactory());
            registerMenuFactory(new DefaultHelpMenuFactory());
        }
    }
}
