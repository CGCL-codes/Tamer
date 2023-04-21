package uk.ac.ebi.intact.psimitab;

import psidev.psi.mi.tab.converter.tab2xml.XmlConversionException;
import psidev.psi.mi.tab.converter.xml2tab.InteractorConverter;
import psidev.psi.mi.tab.converter.xml2tab.TabConversionException;
import psidev.psi.mi.tab.converter.xml2tab.CrossReferenceConverter;
import psidev.psi.mi.tab.converter.IdentifierGenerator;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.tab.model.CrossReference;
import psidev.psi.mi.xml.model.*;
import psidev.psi.mi.xml.model.Parameter;
import uk.ac.ebi.intact.psimitab.model.*;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * Interactor converter for the extended interactor.
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: IntactInteractorConverter.java 13135 2009-05-13 09:35:06Z skerrien $
 */
public class IntactInteractorConverter extends InteractorConverter<ExtendedInteractor> {

    private CrossReferenceConverter xrefConverter;

    public IntactInteractorConverter() {
        xrefConverter = new CrossReferenceConverter();
    }

    @Override
    protected ExtendedInteractor newInteractor(List<CrossReference> identifiers) {
        return new ExtendedInteractor(identifiers);
    }

    @Override
    public ExtendedInteractor toMitab(Interactor interactor) throws TabConversionException {
        ExtendedInteractor tabInteractor = (ExtendedInteractor) super.toMitab(interactor);
        return tabInteractor;
    }

    @Override
    public Interactor fromMitab(psidev.psi.mi.tab.model.Interactor tabInteractor) throws XmlConversionException {
        ExtendedInteractor extInteractor = (ExtendedInteractor) tabInteractor;
        Interactor interactor = super.fromMitab(extInteractor);
        Collection<DbReference> secondaryRefs = getSecondaryRefs(interactor, extInteractor.getProperties());
        Xref interactorXref = new Xref(interactor.getXref().getPrimaryRef(), secondaryRefs);
        interactor.setXref(interactorXref);
        return interactor;
    }

    public Participant buildParticipantA(psidev.psi.mi.xml.model.Interactor interactor, BinaryInteraction binaryInteraction, int index) throws XmlConversionException {
        IntactBinaryInteraction ibi = (IntactBinaryInteraction) binaryInteraction;
        return buildParticipant(interactor, ibi.getInteractorA(), index);
    }

    public Participant buildParticipantB(psidev.psi.mi.xml.model.Interactor interactor, BinaryInteraction binaryInteraction, int index) throws XmlConversionException {
        IntactBinaryInteraction ibi = (IntactBinaryInteraction) binaryInteraction;
        return buildParticipant(interactor, ibi.getInteractorB(), index);
    }

    private Participant buildParticipant(psidev.psi.mi.xml.model.Interactor interactor, ExtendedInteractor extInteractor, int index) throws XmlConversionException {
        Participant participant = new Participant();
        participant.setId(IdentifierGenerator.getInstance().nextId());
        participant.setInteractor(interactor);
        CrossReference expRoleXref = extInteractor.getExperimentalRoles().get(index);
        participant.getExperimentalRoles().add(xrefConverter.fromMitab(expRoleXref, ExperimentalRole.class));
        CrossReference bioRoleXref = extInteractor.getBiologicalRoles().get(index);
        participant.setBiologicalRole(xrefConverter.fromMitab(bioRoleXref, BiologicalRole.class));
        CrossReference interactorType = extInteractor.getInteractorType();
        participant.getInteractor().setInteractorType(xrefConverter.fromMitab(interactorType, InteractorType.class));
        for (Annotation annotation : extInteractor.getAnnotations()) {
            Attribute attr = new Attribute(annotation.getType(), annotation.getText());
            final Collection<Attribute> attributes = participant.getInteractor().getAttributes();
            if (!attributes.contains(attr)) {
                attributes.add(attr);
            }
        }
        for (uk.ac.ebi.intact.psimitab.model.Parameter parameter : extInteractor.getParameters()) {
            Parameter param = new Parameter(parameter.getType(), parameter.getFactor());
            param.setUnit(parameter.getUnit());
            param.setBase(parameter.getBase());
            param.setExponent(parameter.getExponent());
            participant.getParameters().add(param);
        }
        return participant;
    }

    private Collection<DbReference> getSecondaryRefs(Interactor interactor, List<CrossReference> properties) {
        Collection<DbReference> refs = new ArrayList<DbReference>();
        for (CrossReference property : properties) {
            boolean isPrimaryRef = interactor.getXref().getPrimaryRef().getId().equals(property.getIdentifier());
            if (!isPrimaryRef) {
                DbReference secDbRef = new DbReference();
                secDbRef.setDb(property.getDatabase());
                secDbRef.setId(property.getIdentifier());
                if (property.getDatabase().equals("interpro")) {
                    secDbRef.setDbAc("MI:0449");
                } else if (property.getDatabase().equals("intact")) {
                    secDbRef.setDbAc("MI:0469");
                } else if (property.getDatabase().equals("go")) {
                    secDbRef.setDbAc("MI:0448");
                } else if (property.getDatabase().equals("uniprotkb")) {
                    secDbRef.setDbAc("MI:0486");
                } else if (property.getDatabase().equals("drugbank")) {
                    secDbRef.setDbAc("MI:2002");
                } else if (property.getDatabase().equals("pubmed")) {
                    secDbRef.setDbAc("MI:0446");
                } else if (property.getDatabase().equals("ensembl")) {
                    secDbRef.setDbAc("MI:0476");
                } else if (property.getDatabase().equals("refseq")) {
                    secDbRef.setDbAc("MI:0481");
                } else if (property.getDatabase().equals("chebi")) {
                    secDbRef.setDbAc("MI:0474");
                }
                if (property.hasText()) {
                    secDbRef.setSecondary(property.getText());
                }
                refs.add(secDbRef);
            }
        }
        return refs;
    }
}
