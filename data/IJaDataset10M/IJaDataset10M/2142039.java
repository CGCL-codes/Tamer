package be.vds.jtbdive.view.docking.dockable;

import be.vds.jtbdive.utils.LanguageManager;
import be.vds.jtbdive.utils.ResourceManager;
import be.vds.jtbdive.view.panel.DiverDetailPanel;
import be.vds.jtbdive.view.util.LocaleUpdatable;

public class DiverDetailDockable extends BorderDefaultDockable implements LocaleUpdatable {

    private DiverDetailPanel diverDetailPanel;

    public DiverDetailDockable(DiverDetailPanel diverDetailPanel) {
        super("diverdetail", diverDetailPanel, LanguageManager.getKey("detail"), ResourceManager.getImageIcon("diverdetail16.png"), TRADITIONAL_DOCKING_MODES);
        this.diverDetailPanel = diverDetailPanel;
        LanguageManager.addLocaleUpdatable(this);
    }

    public DiverDetailPanel getDiverDetailPanel() {
        return diverDetailPanel;
    }

    @Override
    public void updateLocale() {
        this.setTitle(LanguageManager.getKey("detail"));
    }
}
