package net.sourceforge.squirrel_sql.plugins.laf;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.basic.BasicLookAndFeel;

/**
 * This is simply a placeholder class that allows SQuirreL to populate the LAF chooser with the name 
 * "Substance".  This is done because the Substance LAF doesn't have one skinnable LAF class that identifies 
 * itself as "Substance", but rather defines a LAF class per "skin", each LAF class identifying itself by it's
 * skin name. Since there are many of these, they would clutter up the LAF chooser if we listed them in 
 * LAFPluginResources.properties. So, rather than display a particular Substance LAF class to the user, this 
 * LAF sub-class has a generic name of "Substance". Even though technically, the "Autumn" LAF is the only 
 * one used, it is fine, because "Autumn" will be the first skin displayed in the skin chooser, when the 
 * Substance LAF is chosen.  Any change to the skin chooser will result in the Autumn LAF's setSkin method 
 * being called, transforming it into the LAF representing the chosen skin.   
 */
public class SubstanceLafPlaceholder extends BasicLookAndFeel {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    public SubstanceLafPlaceholder() {
    }

    @Override
    public String getDescription() {
        return "Substance";
    }

    @Override
    public String getID() {
        return "Substance";
    }

    @Override
    public String getName() {
        return "Substance";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return true;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return false;
    }

    public LookAndFeelInfo getLookAndFeelInfo() {
        return new UIManager.LookAndFeelInfo(this.getName(), SubstanceLafPlaceholder.class.getCanonicalName());
    }
}
