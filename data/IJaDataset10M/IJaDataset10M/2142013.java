package de.mpiwg.vspace.metamodel.service.internal;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import de.mpiwg.vspace.metamodel.ExhibitionFactory;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.service.ITextProvider;

public class TextProvider implements ITextProvider {

    public EClass getTextType() {
        return ExhibitionPackage.Literals.TEXT;
    }

    public EFactory getFactory() {
        return ExhibitionFactory.eINSTANCE;
    }
}
