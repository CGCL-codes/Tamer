package eu.redseeds.scl.model.rsl.rslrequirements.requirementsrelationships;

import eu.redseeds.scl.model.rsl.rslrequirements.requirementsspecifications.RequirementDTO;

public interface NonInvocationRelationshipDTO {

    public static final int CONFLICTS_WITH = 1;

    public static final int CONSTRAINS = 2;

    public static final int DEPENDS_ON = 3;

    public static final int DERIVES_FROM = 4;

    public static final int ELABORATES = 5;

    public static final int FULFILS = 6;

    public static final int IS_SIMILAR_TO = 7;

    public static final int MAKES_POSSIBLE = 8;

    public static final int OPERATIONALIZES = 9;

    public void setSource(RequirementDTO req);

    public void setTarget(RequirementDTO req);

    public RequirementDTO getSource();

    public RequirementDTO getTarget();

    public int getRelationshipType();

    public void deleteRelationship();
}
