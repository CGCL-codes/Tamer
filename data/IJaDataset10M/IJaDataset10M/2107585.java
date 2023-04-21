package de.mpiwg.vspace.util.emf;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.service.ExtensionImageProviderRegistry;
import de.mpiwg.vspace.metamodel.service.IExtensionImageProvider;

public class ClassHierarchyHelper {

    public static final ClassHierarchyHelper INSTANCE = new ClassHierarchyHelper();

    private ClassHierarchyHelper() {
    }

    public List<EClass> getSubclasses(EClass superClass) {
        List<EObject> classes = ExhibitionPackage.eINSTANCE.eContents();
        if (classes == null) return null;
        List<EClass> subclasses = new ArrayList<EClass>();
        for (EObject cl : classes) {
            if (!(cl instanceof EClass)) continue;
            List<EClass> supertypes = ((EClass) cl).getESuperTypes();
            if (supertypes.contains(superClass)) subclasses.add((EClass) cl);
        }
        return subclasses;
    }
}
