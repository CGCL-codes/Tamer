package uk.ac.ebi.intact.psimitab;

import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.tab.model.builder.*;

/**
 * TODO comment that class header
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: IntactDocumentDefinition.java 12409 2008-11-04 14:48:30Z baranda $
 */
public class IntactDocumentDefinition extends AbstractDocumentDefinition<IntactBinaryInteraction> {

    public static final int EXPERIMENTAL_ROLE_A = 15;

    public static final int EXPERIMENTAL_ROLE_B = 16;

    public static final int BIOLOGICAL_ROLE_A = 17;

    public static final int BIOLOGICAL_ROLE_B = 18;

    public static final int PROPERTIES_A = 19;

    public static final int PROPERTIES_B = 20;

    public static final int INTERACTOR_TYPE_A = 21;

    public static final int INTERACTOR_TYPE_B = 22;

    public static final int HOST_ORGANISM = 23;

    public static final int EXPANSION_METHOD = 24;

    public static final int DATASET = 25;

    public static final int ANNOTATIONS_A = 26;

    public static final int ANNOTATIONS_B = 27;

    public static final int PARAMETERS_A = 28;

    public static final int PARAMETERS_B = 29;

    public static final int PARAMETERS_INTERACTION = 30;

    public IntactDocumentDefinition() {
        super();
        addColumnDefinition(new ColumnDefinition("Experimental role(s) interactor A", "experimentalRoleA", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Experimental role(s) interactor B", "experimentalRoleB", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Biological role(s) interactor A", "biologicalRoleA", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Biological role(s) interactor B", "biologicalRoleB", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Properties interactor A", "propertiesA_exact", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Properties interactor B", "propertiesB_exact", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Type(s) interactor A", "typeA", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Type(s) interactor B", "typeB", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("HostOrganism(s)", "hostOrganism", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Expansion method(s)", "expansion", new PlainTextFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Dataset name(s)", "dataset", new PlainTextFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Annotation(s) interactor A", "annotationA", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Annotation(s) interactor B", "annotationB", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Parameter(s) interactor A", "parameterA", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Parameter(s) interactor B", "parameterB", new CrossReferenceFieldBuilder()));
        addColumnDefinition(new ColumnDefinition("Parameter(s) interaction", "parameterInteraction", new CrossReferenceFieldBuilder()));
    }

    public InteractionRowConverter<IntactBinaryInteraction> createInteractionRowConverter() {
        return new IntactInteractionRowConverter();
    }
}
