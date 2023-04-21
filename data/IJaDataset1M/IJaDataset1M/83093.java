package org.informaticisenzafrontiere.openstaff;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.informaticisenzafrontiere.openstaff.model.Anagrafica;
import org.informaticisenzafrontiere.openstaff.model.AnagraficaGroup;
import org.informaticisenzafrontiere.openstaff.model.Voci;

public class OpenStaffAdapterFactory implements IAdapterFactory {

    private IWorkbenchAdapter roleAdapter = new IWorkbenchAdapter() {

        public Object getParent(Object o) {
            return ((AnagraficaGroup) o).getParent();
        }

        public String getLabel(Object o) {
            AnagraficaGroup group = ((AnagraficaGroup) o);
            Anagrafica[] voci = group.getSottoMenu();
            for (int i = 0; i < voci.length; i++) {
                Anagrafica menu = voci[i];
            }
            return group.getName();
        }

        public Object[] getChildren(Object o) {
            return ((AnagraficaGroup) o).getSottoMenu();
        }

        @Override
        public ImageDescriptor getImageDescriptor(Object object) {
            return AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.GROUP);
        }
    };

    private IWorkbenchAdapter functionAdapter = new IWorkbenchAdapter() {

        public Object getParent(Object o) {
            return ((AnagraficaGroup) o).getParent();
        }

        public String getLabel(Object o) {
            AnagraficaGroup group = ((AnagraficaGroup) o);
            Anagrafica[] voci = group.getSottoMenu();
            for (int i = 0; i < voci.length; i++) {
                Anagrafica menu = voci[i];
            }
            return group.getName() + " (" + voci.length + ")";
        }

        public Object[] getChildren(Object o) {
            return ((AnagraficaGroup) o).getSottoMenu();
        }

        @Override
        public ImageDescriptor getImageDescriptor(Object object) {
            return AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.DIPENDENTE);
        }
    };

    private IWorkbenchAdapter entryAdapter = new IWorkbenchAdapter() {

        public Object getParent(Object o) {
            return ((Voci) o).getParent();
        }

        public String getLabel(Object o) {
            Voci sottoMenu = ((Voci) o);
            return sottoMenu.getName();
        }

        public Object[] getChildren(Object o) {
            return new Object[0];
        }

        @Override
        public ImageDescriptor getImageDescriptor(Object object) {
            Voci menu = ((Voci) object);
            return AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.DIPENDENTE);
        }
    };

    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof AnagraficaGroup && ((AnagraficaGroup) adaptableObject).getParent() != null && ((AnagraficaGroup) adaptableObject).getParent().getName().equals("RootGroup")) return functionAdapter; else if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof AnagraficaGroup && ((AnagraficaGroup) adaptableObject).getParent() != null && !((AnagraficaGroup) adaptableObject).getParent().getName().equals("RootGroup")) return roleAdapter; else if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof AnagraficaGroup) return roleAdapter; else if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof Voci) return entryAdapter;
        return null;
    }

    public Class[] getAdapterList() {
        return new Class[] { IWorkbenchAdapter.class };
    }
}
