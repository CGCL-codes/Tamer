package druid.panels.database.generation.modules.general;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.dlib.gui.FlexLayout;
import org.dlib.gui.MultiPanel;
import druid.core.modules.ModuleManager;
import druid.data.DatabaseNode;
import druid.interfaces.BasicModule;
import druid.interfaces.DataGenModule;
import druid.interfaces.ModuleOptions;

public class SpecificPanel extends MultiPanel {

    private Hashtable htModules = new Hashtable();

    public SpecificPanel() {
        add(new JPanel(), "blank");
        for (Enumeration e = ModuleManager.getAllModules(); e.hasMoreElements(); ) {
            BasicModule mod = (BasicModule) e.nextElement();
            if (mod instanceof DataGenModule) {
                DataGenModule dgMod = (DataGenModule) mod;
                ModuleOptions modOpt = dgMod.getModuleOptions(BasicModule.DATABASE);
                if (modOpt != null) {
                    String modName = ModuleManager.getAbsoluteID(mod);
                    htModules.put(mod, modOpt);
                    JComponent c = modOpt.getPanel();
                    if (!dgMod.hasLargePanel()) c = new PanelWrapper(c);
                    add(c, modName);
                }
            }
        }
    }

    public void refresh(DataGenModule mod, DatabaseNode node) {
        ModuleOptions modOpt = (ModuleOptions) htModules.get(mod);
        if (modOpt != null) {
            modOpt.refresh(node);
            show(ModuleManager.getAbsoluteID(mod));
        } else show("blank");
    }
}

class PanelWrapper extends JPanel {

    public PanelWrapper(JComponent c) {
        FlexLayout flexL = new FlexLayout(1, 1);
        flexL.setColProp(0, FlexLayout.EXPAND);
        setLayout(flexL);
        add("0,0,x", c);
    }
}
