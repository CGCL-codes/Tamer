package be.vds.jtbdive.view.docking.dockable;

import be.vds.jtbdive.utils.LanguageManager;
import be.vds.jtbdive.utils.ResourceManager;
import be.vds.jtbdive.view.listener.DiveSelectionListener;
import be.vds.jtbdive.view.panel.DiveTablePanel;
import be.vds.jtbdive.view.util.LocaleUpdatable;

public class DiveTableDockable extends BorderDefaultDockable implements LocaleUpdatable {

    private DiveTablePanel diveTablePanel;

    public DiveTableDockable(DiveTablePanel diveTablePanel) {
        super("tabledivepanel", diveTablePanel, LanguageManager.getKey("logbook"), ResourceManager.getImageIcon("logbook16.png"), TRADITIONAL_DOCKING_MODES);
        this.diveTablePanel = diveTablePanel;
        LanguageManager.addLocaleUpdatable(this);
    }

    public void addDiveSelectionListener(DiveSelectionListener diveSelectionListener) {
        diveTablePanel.addDiveSelectionListener(diveSelectionListener);
    }

    public double getMinimumWidth() {
        return diveTablePanel.getMinimumSize().getWidth();
    }

    @Override
    public void updateLocale() {
        this.setTitle(LanguageManager.getKey("logbook"));
        diveTablePanel.updateLocale();
    }
}
