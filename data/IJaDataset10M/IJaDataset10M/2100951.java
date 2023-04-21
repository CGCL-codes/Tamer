package fr.insa.rennes.pelias.pcreator.views;

import java.util.Collection;
import java.util.UUID;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import fr.insa.rennes.pelias.framework.Chain;
import fr.insa.rennes.pelias.framework.Service;
import fr.insa.rennes.pelias.pcreator.Application;
import fr.insa.rennes.pelias.platform.PObjectReference;
import fr.insa.rennes.pelias.platform.PSxSObjectReference;

/**
 * Classe qui represente le ContentProvider et le LabelProvider pour toute instane de Navigator
 * @author otilia damian
 *
 */
public class PCreatorAdapterFactory implements IAdapterFactory {

    public static PObjectReference chainROOT = new PObjectReference(Chain.class, UUID.randomUUID());

    public static PObjectReference serviceROOT = new PObjectReference(Service.class, UUID.randomUUID());

    private IWorkbenchAdapter chainAdapter = new IWorkbenchAdapter() {

        /**
		 * @param o : l'objet PObjectReference seletionné (chaine ou service)
		 * Returne la liste de tous les PSxSObjectReference(les versions) de o
		 */
        public Object[] getChildren(Object o) {
            Collection<? extends PObjectReference> versions = null;
            if (o.equals(chainROOT)) {
                versions = Application.getCurrentChainRepository().enumerateObjects();
            } else {
                versions = Application.getCurrentChainRepository().enumerateObjectVersions(((PObjectReference) o).getId());
            }
            return versions.toArray();
        }

        /**
		 * Aucune image associée 
		 */
        public ImageDescriptor getImageDescriptor(Object object) {
            return null;
        }

        /**
		 * @param o : l'objet PObjectReference selectionné
		 * return le libellé associé à l'objet
		 */
        public String getLabel(Object o) {
            return ((PObjectReference) o).getLabel();
        }

        /**
		 * returne ROOT, le parent courant de tous les PObjectReference
		 */
        public Object getParent(Object o) {
            return chainROOT;
        }
    };

    private IWorkbenchAdapter serviceAdapter = new IWorkbenchAdapter() {

        /**
		 * @param o : l'objet PObjectReference seletionné (chaine ou service)
		 * Returne la liste de tous les PSxSObjectReference(les versions) de o
		 */
        public Object[] getChildren(Object o) {
            Collection<? extends PObjectReference> versions;
            if (o.equals(serviceROOT)) {
                versions = Application.getCurrentServiceRepository().enumerateObjects();
            } else {
                versions = Application.getCurrentServiceRepository().enumerateObjectVersions(((PObjectReference) o).getId());
            }
            return versions.toArray();
        }

        /**
		 * Aucune image associée 
		 */
        public ImageDescriptor getImageDescriptor(Object object) {
            return null;
        }

        /**
		 * @param o : l'objet PObjectReference selectionné
		 * return le libellé associé à l'objet
		 */
        public String getLabel(Object o) {
            return ((PObjectReference) o).getLabel();
        }

        /**
		 * returne ROOT, le parent courant de tous les PObjectReference
		 */
        public Object getParent(Object o) {
            return serviceROOT;
        }
    };

    private IWorkbenchAdapter versionAdapter = new IWorkbenchAdapter() {

        /**
		 * les versions ne contient pas d'autre objets
		 */
        public Object[] getChildren(Object o) {
            return new Object[0];
        }

        /**
		 * Acune image associée 
		 */
        public ImageDescriptor getImageDescriptor(Object object) {
            return null;
        }

        /**
		 * @param o: l'objet PSxSObjectReference
		 * retourne le numéro de version
		 */
        public String getLabel(Object o) {
            return ((PSxSObjectReference) o).getVersion().toString();
        }

        /**
		 * @param o : objet de type PSxSObjectReference
		 * retourne l'objet de type PObjectReference associé
		 */
        public Object getParent(Object o) {
            return Application.getCurrentServiceRepository().getObject(((PSxSObjectReference) o).getId());
        }
    };

    @SuppressWarnings("unchecked")
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType == IWorkbenchAdapter.class && adaptableObject.getClass() == PSxSObjectReference.class) {
            return versionAdapter;
        }
        if (adapterType == IWorkbenchAdapter.class && (adaptableObject instanceof PObjectReference)) if (((PObjectReference) adaptableObject).getReferencedClass() == Chain.class) {
            return chainAdapter;
        }
        if (((PObjectReference) adaptableObject).getReferencedClass() == Service.class) {
            return serviceAdapter;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Class[] getAdapterList() {
        return new Class[] { IWorkbenchAdapter.class };
    }
}
