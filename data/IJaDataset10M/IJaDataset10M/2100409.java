package net.sourceforge.jepesi.controller;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.tree.MutableTreeNode;
import net.sourceforge.jepesi.addon.AddonInterface;
import net.sourceforge.jepesi.gui.StatusPanelInterface;
import net.sourceforge.jepesi.gui.tab.LogPanelInterface;
import net.sourceforge.jepesi.model.Cluster;
import net.sourceforge.jepesi.model.Host;
import net.sourceforge.jepesi.model.Language;

public interface JepesiListener extends ActionListener, WindowListener, JepesiInterface {

    public abstract ImageIcon getIcon(MutableTreeNode node);

    public abstract void addLoadedAddon(MutableTreeNode node, AddonInterface addon);

    public abstract void leftClickOnHostTree(Host host);

    public abstract void leftClickOnHostTree(Cluster cluster);

    public abstract void openAddon(Host host, MutableTreeNode node);

    public abstract List<AddonInterface> getAddons(Host host);

    public abstract void addHost(Host host);

    public abstract void processModelChange();

    public abstract LogPanelInterface getLogger();

    public StatusPanelInterface getStatusPanel();

    public List<Language> getLanguages();
}
