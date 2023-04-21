package org.hibnet.lune.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.window.Window;
import org.hibnet.lune.ui.IndexInput;
import org.hibnet.lune.ui.LuneUIPlugin;
import org.hibnet.lune.ui.plugin.ConfigurableBeanFactory;
import org.hibnet.lune.ui.plugin.ConfigurationDialog;

public class GenericNewIndexInputAction extends Action {

    private final ConfigurableBeanFactory<IndexInput> factory;

    public GenericNewIndexInputAction(ConfigurableBeanFactory<IndexInput> factory) {
        this.factory = factory;
        setText("New " + factory.getName());
        setToolTipText("Create a " + factory.getName());
        ImageDescriptor base = factory.getImageDescriptor();
        if (base != null) {
            ImageDescriptor overlay = LuneUIPlugin.getDescriptor("newOverlay");
            DecorationOverlayIcon deco = new DecorationOverlayIcon(base.createImage(), overlay, IDecoration.TOP_RIGHT);
            setImageDescriptor(deco);
        }
    }

    @Override
    public void run() {
        ConfigurationDialog<IndexInput> dialog = new ConfigurationDialog<IndexInput>(LuneUIPlugin.getActiveWorkbenchShell(), factory, null);
        if (dialog.open() == Window.OK) {
            IndexInput index = dialog.getNewBean();
            LuneUIPlugin.getIndexManager().add(index);
            LuneUIPlugin.getIndexesView().refresh();
        }
    }
}
