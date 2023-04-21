package mil.army.usace.ehlschlaeger.digitalpopulations;

/**
 * One "realization" of a PumsPopulation. Just like PumsHouseholdRealization,
 * this class "inherits" its attributes from PumsPopulation. Because of that,
 * population records are generally accessed as PumsPopulation objects through
 * PumsHousehold rather than as PumsPopulationRealization objects.
 * <p>
 * Copyright <a href="http://faculty.wiu.edu/CR-Ehlschlaeger2/">Charles R.
 * Ehlschlaeger</a>, work: 309-298-1841, fax: 309-298-3003, This software is
 * freely usable for research and educational purposes. Contact C. R.
 * Ehlschlaeger for permission for other purposes. Use of this software requires
 * appropriate citation in all published and unpublished documentation.
 */
public class PumsPopulationRealization {

    private PumsPopulation parent;

    private int num;

    public PumsPopulationRealization(PumsPopulation parent, int realizationNumber) {
        this.parent = parent;
        num = realizationNumber;
    }

    public int getRealizationNumber() {
        return num;
    }

    public String getVariableName(int variable) {
        return (parent.getAttributeName(variable));
    }

    public int getVariableValue(int variable) {
        if (realizedVariables[variable] == true) {
            throw new RuntimeException("realizedVariables[ variable] == true not implemented yet");
        }
        return (parent.getAttributeValue(variable));
    }

    public boolean[] realizedVariables = { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
}
