package de.mpiwg.vspace.modules.editor.expand;

import java.net.URL;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
import org.eclipse.swt.graphics.Image;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.provider.ExhibitionItemProviderAdapterFactory;
import de.mpiwg.vspace.util.images.ImageRegistry;

public class ExpandItemInitializer {

    public static void updateModuleExpandItem(ModuleExpandItem item, EObject eobject) {
        DelegatingWrapperItemProvider delWrapItemProvider = new DelegatingWrapperItemProvider(eobject, eobject, ExhibitionPackage.eINSTANCE.eContainingFeature(), 0, new ExhibitionItemProviderAdapterFactory());
        item.setText(delWrapItemProvider.getText(eobject));
        URL url = (URL) delWrapItemProvider.getImage(eobject);
        Image image = ImageRegistry.REGISTRY.getImage(url);
        if (image != null) item.setImage(image);
        item.updateContent();
    }

    public static void initModuleExpandItem(ModuleExpandItem item, EObject eObject) {
        updateModuleExpandItem(item, eObject);
    }
}
